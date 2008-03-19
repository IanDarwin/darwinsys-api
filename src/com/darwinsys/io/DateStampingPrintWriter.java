package com.darwinsys.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Date;

public class DateStampingPrintWriter extends PrintWriter {

	private static final String FMT = "%yyyy%m%d-%M%h %s";
	private Date d = new Date();
	
	@Override
	public void println(String s) {
		d.setTime(System.currentTimeMillis());
		super.printf(FMT, d, s);
	}
	
	@Override
	public void print(String s) {
		d.setTime(System.currentTimeMillis());
		super.printf(FMT, d, s);
	}
	
	public DateStampingPrintWriter(File file, String csn) throws FileNotFoundException, UnsupportedEncodingException {
		super(file, csn);
		// TODO Auto-generated constructor stub
	}

	public DateStampingPrintWriter(File file) throws FileNotFoundException {
		super(file);
		// TODO Auto-generated constructor stub
	}

	public DateStampingPrintWriter(OutputStream out, boolean autoFlush) {
		super(out, autoFlush);
		// TODO Auto-generated constructor stub
	}

	public DateStampingPrintWriter(OutputStream out) {
		super(out);
		// TODO Auto-generated constructor stub
	}

	public DateStampingPrintWriter(String fileName, String csn) throws FileNotFoundException, UnsupportedEncodingException {
		super(fileName, csn);
		// TODO Auto-generated constructor stub
	}

	public DateStampingPrintWriter(String fileName) throws FileNotFoundException {
		super(fileName);
		// TODO Auto-generated constructor stub
	}

	public DateStampingPrintWriter(Writer out, boolean autoFlush) {
		super(out, autoFlush);
		// TODO Auto-generated constructor stub
	}

	public DateStampingPrintWriter(Writer out) {
		super(out);
		// TODO Auto-generated constructor stub
	}

}
