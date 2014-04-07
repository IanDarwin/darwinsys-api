package com.darwinsys.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Stack;

/**
 * Reverse a file by lines (simple algorithm, keeps file in memory).
 */
public class RevLines {
	public static void main(String[] argv) throws Throwable {
		String line;

		BufferedReader is = new BufferedReader(new FileReader(argv[0]));

		Stack<String> myStack = new Stack<String>();

		// Put it in the stack frontwards
		while ((line = is.readLine()) != null) {
			myStack.push(line);
		}

		// Print the stack backwards
		while (!myStack.empty()) {
			System.out.println(myStack.pop());
		}
	}
}
