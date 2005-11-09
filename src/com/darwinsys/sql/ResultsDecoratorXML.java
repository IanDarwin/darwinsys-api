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
	WebRowSet results;
	
	public ResultsDecoratorXML(PrintWriter out, Verbosity v) {
		super(out, v);
		
		try {
			// The class name is uncommitted so subject to change.
			Class c = Class.forName("com.sun.rowset.WebRowSetImpl");
			results = (WebRowSet)c.newInstance();
			
		} catch (Exception ex){
			throw new IllegalArgumentException(
			"can't load WebRowSetImpl, check CLASSPATH");
		}
	}
	
	public void write(ResultSet rs) throws SQLException {
		results.writeXml(rs, parent);
	}

	public void write(int rowCount) throws IOException {
		println("RowCount: " + rowCount);
		
	}

	/* (non-Javadoc)
	 * @see ResultsDecorator#getName()
	 */
	public String getName() {
		return "XML";
	}
}
