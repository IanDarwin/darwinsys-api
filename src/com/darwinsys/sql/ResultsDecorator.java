package com.darwinsys.sql;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.darwinsys.util.Verbosity;

/** Base class for a series of ResultSet printers.
 * @version $Id$
 */
public abstract class ResultsDecorator {
	PrintWriter parent;
	Verbosity verbosity;

	ResultsDecorator(PrintWriter wr, Verbosity v) {
		this.parent = wr;
		this.verbosity = v;
	}
	
	/** Print the name of this Decorator's output format */
	public abstract String getName();
	
	/** Print the contents of a ResultSet */
	public abstract void write(ResultSet rs) throws IOException, SQLException;
	
	/** Print the results of an operation as a Count */
	public abstract void write(int rowCount) throws IOException;
	
	public void println(String line) throws IOException {
		parent.println(line);
	}
	public void println() throws IOException {
		parent.println();
	}
	public void print(String lineSeg) throws IOException {
		parent.print(lineSeg);
	}
}
