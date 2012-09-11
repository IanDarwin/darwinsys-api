package com.darwinsys.xml;


import java.io.Console;
import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

/**
 * Simple interactive use of XPath, which is supported in JAXP (in 
 * JavaSE package javax.xml.xpath) since JDK 1.5.
 */
public class XPathRunner {

	private static String fileName;
	private static String expr;
	private static Document document;

	public static void main(String[] args) throws Exception {
		
		switch (args.length) {
		case 0:
			System.err.println("Usage: XPathRunner file [xpath-expr]\n");
			return;
		case 2:
			expr = args[1];
			/*FALLTHROUGH*/
		case 1:
			fileName = args[0];
			break;
		}

		DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		document = parser.parse(new FileInputStream(fileName));

		boolean done = false;
		if (expr == null) do {
			expr = promptForPathExpr();
			System.out.println(eval(expr));
		} while (!done);
		else {
			System.out.println(eval(expr));
		}
	}
	
	private static String eval(String expr) throws XPathExpressionException {		
		/** evaluate the XPath expression against the Document */
		XPath xpath = XPathFactory.newInstance().newXPath();
		String secNum = (String) xpath.evaluate(expr, document, XPathConstants.STRING);
		return secNum;
	}

	private static String promptForPathExpr() {
		Console c = System.console();
		if (c == null) {
			throw new IllegalStateException("This app needs a 'controlling terminal' to prompt; use with filename and expr arguments");
		}
		return c.readLine("Enter XPath prompt for %s", fileName);
	}
}


