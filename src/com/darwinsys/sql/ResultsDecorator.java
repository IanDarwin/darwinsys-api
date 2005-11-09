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
	PrintWriter out;
	Verbosity verbosity;

	ResultsDecorator(PrintWriter wr, Verbosity v) {
		this.out = wr;
		this.verbosity = v;
	}
	
	/** Print the name of this Decorator's output format */
	public abstract String getName();
	
	/** Print the contents of a ResultSet */
	public abstract int write(ResultSet rs) throws IOException, SQLException;
	
	public void printRowCount(int n) throws IOException {
		out.println("Row Count = " + n);
	}
	public void println(String line) throws IOException {
		out.println(line);
	}
	public void println() throws IOException {
		out.println();
	}
	public void print(String lineSeg) throws IOException {
		out.print(lineSeg);
	}
}
