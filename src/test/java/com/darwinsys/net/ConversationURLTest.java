package com.darwinsys.net;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ConversationURLTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testMakeBasicAuthString() {
		assertEquals("Basic dGVzdGluZzoxIDIgMw==", 
			ConversationURL.makeBasicAuthString("testing", "1 2 3"));
	}

	@Test @Ignore("Test passes with real auth, obv cannot commit that way")
	public void testConverse() throws Exception {
		URL url = new URL("https://darwinsys.com/todorest/rs/iadmin/task/new");
		String postBody = "{\"priority\":\"High\",\"name\":\"TEST ConversationURLTest.testConverse()\"," +
			"\"creationDate\":{\"year\":2015,\"month\":11,\"day\":1},\"project\":null," +
			"\"context\":{\"id\":1,\"name\":\"Life\"},\"dueDate\":null,\"status\":\"ACTIVE\"," +
			"\"completedDate\":null,\"modified\":1448292432791,\"description\":\"None\"}";
		Map<String,String> headers = new HashMap<>();
		headers.put("content-type", "application/json");
		headers.put("accept", "text/plain");
		headers.put("authorization", ConversationURL.makeBasicAuthString("xxx", "yyy"));
		ConversationURL.converse(url, postBody, headers);
		// If we get here, no exceptions thrown, we're probably good...
	}
}
