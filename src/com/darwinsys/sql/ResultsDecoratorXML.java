package com.darwinsys.sql;

import java.io.IOException;
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
	
	ResultsDecoratorXML(ResultsDecoratorPrinter out, Verbosity v) {
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
		results.writeXml(rs, parent.getPrintWriter());
	}

	void write(int rowCount) throws IOException {
		println("RowCount: " + rowCount);
		
	}

	/* (non-Javadoc)
	 * @see ResultsDecorator#getName()
	 */
	String getName() {
		return "XML";
	}
}
