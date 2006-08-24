package csv;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class CSVExportTest extends TestCase {
    
    List<Object> line = new ArrayList<Object>();
    
    /**
     * Test basics
     */
	public void testBasic() throws Exception {		
		line.add("123");
		line.add(42);
		line.add("Hello, world");
		line.add("foo");
		String result = CSVExport.toString(line);
        System.out.println(result);
        assertEquals("123,42,\"Hello, word\",foo", result);     
    }
 
    /**
     * Test quoted string
     */
    void testQuoted() throws Exception {
		line = new ArrayList<Object>();
		line.add(123);
		line.add("\"Hello, \"ian\"\"");
		String result= CSVExport.toString(line);
        System.out.println(result);
        assertEquals("123,\"Hello, \"ian\"", result);
	}
    
    /**
     * Test null
     */
    void testNullField() throws Exception {
        line = new ArrayList<Object>();
        line.add(123);
        line.add(null);
        line.add(456);
        String result= CSVExport.toString(line);
        System.out.println(result);
        assertEquals("123,\"\",456", result);
    }
}
