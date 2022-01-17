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
public class JCSLightLoadUnitTest {
	private int items;
	private String expectedResult;
	private static JCS jcs;
	
	@BeforeClass
	public static void configureEnvironment() throws Exception {
		JCS.setConfigFilename("/TestSimpleLoad.ccf");
		jcs = JCS.getInstance( "testCache1" );
	}
	
	public JCSLightLoadUnitTest(int items, String expectedResult) {
		this.items = items;
		this.expectedResult = expectedResult;
	}
	
	@Parameters
	public static Collection<Object[]> getParameters() {
		return Arrays.asList(new Object[][] {
			{2000, null}
		});
	}
	
	@Test
	public void testSimpleLoad() throws Exception {
		for ( int i = 1; i <= this.items; i++ )
        {
            jcs.put( i + ":key", "data" + i );
        }
		for ( int i = this.items; i > 0; i-- )
        {
            String res = (String) jcs.get( i + ":key" );
            if ( res == null )
            {
                assertNotEquals( "[" + i + ":key] should not be null", res, this.expectedResult);
            }
        }
		jcs.remove( "300:key" );
        assertEquals( jcs.get( "300:key" ), this.expectedResult);
	}
	
}
