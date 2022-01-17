import org.apache.jcs.JCS;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(value=Parameterized.class)
public class JCSRemovalSimpleConcurrentTest {
	private int count;
	private String expectedResult;
	private static JCS jcs;
	
	@BeforeClass
	public static void configureEnvironment() throws Exception {
		JCS.setConfigFilename("/TestRemoval.ccf");
		jcs = JCS.getInstance( "testCache1" );
	}
	
    public JCSRemovalSimpleConcurrentTest(int count, String expectedResult) {
    	this.count = count;
    	this.expectedResult = expectedResult;
    }
    
    @Parameters
	public static Collection<Object[]> getParameters() {
		return Arrays.asList(new Object[][] {
			{500, null},
		});
	}
	
	@Test
	public void testTwoDeepRemoval() throws Exception
	    {
	        System.out.println( "------------------------------------------" );
	        System.out.println( "testTwoDeepRemoval" );

	        for ( int i = 0; i <= this.count; i++ )
	        {
	            jcs.put( "key:" + i + ":anotherpart", "data" + i );
	        }

	        for ( int i = this.count; i >= 0; i-- )
	        {
	            String res = (String) jcs.get( "key:" + i + ":anotherpart" );
	            if ( res == null )
	            {
	                assertNotEquals( "[key:" + i + ":anotherpart] should not be null, " + jcs.getStats(), res, this.expectedResult );
	            }
	        }
	        System.out.println( "Confirmed that " + this.count + " items could be found" );

	        for ( int i = 0; i <= this.count; i++ )
	        {
	            jcs.remove( "key:" + i + ":" );
	            assertEquals( jcs.getStats(), jcs.get( "key:" + i + ":anotherpart" ), this.expectedResult);
	        }
	        System.out.println( "Confirmed that " + this.count + " items were removed" );

	        System.out.println( jcs.getStats() );
	    }
	
	@Test
	public void testSingleDepthRemoval() throws Exception
	    {
	        System.out.println( "------------------------------------------" );
	        System.out.println( "testSingleDepthRemoval" );
	        
	        for ( int i = 0; i <= this.count; i++ )
	        {
	            jcs.put( i + ":key", "data" + i );
	        }

	        for ( int i = this.count; i >= 0; i-- )
	        {
	            String res = (String) jcs.get( i + ":key" );
	            if ( res == null )
	            {
	                assertNotEquals( "[" + i + ":key] should not be null", res , this.expectedResult);
	            }
	        }
	        System.out.println( "Confirmed that " + this.count + " items could be found" );

	        for ( int i = 0; i <= this.count; i++ )
	        {
	            jcs.remove( i + ":" );
	            assertEquals( jcs.get( i + ":key" ), this.expectedResult);
	        }
	        System.out.println( "Confirmed that " + this.count + " items were removed" );
	        System.out.println( jcs.getStats() );
	    }
	
	@Test
	public void testClear() throws Exception{
	        System.out.println( "------------------------------------------" );
	        System.out.println( "testRemoveAll" );

	        for ( int i = 0; i <= this.count; i++ )
	        {
	            jcs.put( i + ":key", "data" + i );
	        }

	        for ( int i = this.count; i >= 0; i-- )
	        {
	            String res = (String) jcs.get( i + ":key" );
	            if ( res == null )
	            {
	                assertNotEquals( "[" + i + ":key] should not be null", res, this.expectedResult);
	            }
	        }
	        System.out.println( "Confirmed that " + this.count + " items could be found" );
	        System.out.println( jcs.getStats() );

	        jcs.clear();

	        for ( int i = this.count; i >= 0; i-- )
	        {
	            String res = (String) jcs.get( i + ":key" );
	            if ( res != null )
	            {
	                assertEquals( "[" + i + ":key] should be null after remvoeall" + jcs.getStats(), res, this.expectedResult);
	            }
	        }
	        System.out.println( "Confirmed that all items were removed" );

	    }
	
	@Test
	public void testClearRepeatedlyWithoutError() throws Exception{
	        System.out.println( "------------------------------------------" );
	        System.out.println( "testRemoveAll" );
	        
	        jcs.clear();

	        for ( int i = 0; i <= this.count; i++ )
	        {
	            jcs.put( i + ":key", "data" + i );
	        }

	        for ( int i = this.count; i >= 0; i-- )
	        {
	            String res = (String) jcs.get( i + ":key" );
	            if ( res == null )
	            {
	                assertNotEquals( "[" + i + ":key] should not be null", res , this.expectedResult);
	            }
	        }
	        System.out.println( "Confirmed that " + this.count + " items could be found" );
	        System.out.println( jcs.getStats() );

	        for ( int i = this.count; i >= 0; i-- )
	        {
	            jcs.put( i + ":key", "data" + i );
	            jcs.clear();
	            String res = (String) jcs.get( i + ":key" );
	            if ( res != null )
	            {
	                assertEquals( "[" + i + ":key] should be null after remvoeall" + jcs.getStats(), res , this.expectedResult);
	            }
	        }
	        System.out.println( "Confirmed that all items were removed" );

	    }
	
}
