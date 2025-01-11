package com.darwinsys.csv;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class CSVExportTest {

    List<Object> line = new ArrayList<Object>();

	/**
	* Test basics
	*/
	@Test
	void basic() throws Exception {
		line.add("123");
		line.add(42);
		line.add("\"Hello, world\"");
		line.add("foo");
		String result = CSVExport.toString(line);
        System.out.println(result);
        assertEquals("123,42,\"Hello, world\",foo", result);
    }

	/**
	* Test quoted string
	*/
	@Test
	void quoted() throws Exception {
		line = new ArrayList<Object>();
		line.add(123);
		line.add("\"Hello, \\\"ian\\\"\"");
		String result= CSVExport.toString(line);
        System.out.println(result);
        assertEquals("123,\"Hello, \\\"ian\\\"\"", result);

        line.clear();
        line.add("123,456.7");
        result = CSVExport.toString(line);
        assertEquals("\"123,456.7\"", result);

        line.clear();
        line.add(123);
        line.add("\"\"");
        assertEquals("123,\"\"", CSVExport.toString(line), "quote at end");
	}

	/**
	* Test null
	*/
	@Test
	void nullField() throws Exception {
        line = new ArrayList<Object>();
        line.add(123);
        line.add(null);
        line.add(456);
        String result= CSVExport.toString(line);
        System.out.println(result);
        assertEquals("123,\"\",456", result);
    }
}
