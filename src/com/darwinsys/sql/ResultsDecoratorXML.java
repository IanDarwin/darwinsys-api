package com.darwinsys.sql;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.rowset.WebRowSet;

import com.darwinsys.util.Verbosity;

/**
 * This guy's primary raison d'etre is to generate an XML output file
 * for use in JUnit testing of the ResultsDecoratorSQL!
 * @version $Id$
 */
public class ResultsDecoratorXML extends ResultsDecorator {
	private static final String SUN_WEBROWSET_IMPL_CLASS = "com.sun.rowset.WebRowSetImpl";
	WebRowSet results;
	
	public ResultsDecoratorXML(PrintWriter out, Verbosity v) {
		super(out, v);
		
		try {
			// The class name is uncommitted so subject to change.
			Class c = Class.forName(SUN_WEBROWSET_IMPL_CLASS);
			results = (WebRowSet)c.newInstance();
			
		} catch (Exception ex){
			throw new IllegalArgumentException(
			"can't load " + SUN_WEBROWSET_IMPL_CLASS + ", check CLASSPATH");
		}
	}
	
	@Override
	public int write(ResultSet rs) throws SQLException {
		results.writeXml(rs, out);
		return results.getRow();
	}

	@Override
	public void printRowCount(int rowCount) throws IOException {
		System.err.println("RowCount: " + rowCount);
		
	}

	/* (non-Javadoc)
	 * @see ResultsDecorator#getName()
	 */
	@Override
	public String getName() {
		return "XML";
	}
}
