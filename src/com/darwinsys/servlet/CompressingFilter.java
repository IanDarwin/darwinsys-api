package com.darwinsys.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Servlet Filter to do compression.
 * @author Main class by Stephen Neal(?), hacked on by Ian Darwin.
 * GzipResponseWrapper class by Ian Darwin.
 */
public class CompressingFilter implements Filter {

	/*
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig arg0) throws ServletException {
		// nothing to do.
	}

	/**
	 * If the request is of type HTTP *and* the user's browser will
	 * accept GZIP encoding, do it; otherwise just pass the request on.
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		System.out.println("CompressingFilter.doFilter()");
		if (req instanceof HttpServletRequest) {
			HttpServletRequest request = (HttpServletRequest) req;
			System.out.println("CompressingFilter.doFilter(): " + request.getRequestURI());
			HttpServletResponse response = (HttpServletResponse) resp;
			// XXX should maybe use getHeaders() and iterate?
			String acceptableEncodings = request.getHeader("accept-encoding");
			if (acceptableEncodings != null
					&& acceptableEncodings.indexOf("gzip") != -1) {

				System.out.println("CompressingFilter.doFilter(): doing compression!");

				// Create a delegate for the Response object; all methods
				// are directly delegated except getOutputStream.
				// This wrapper class is defined below.
				GZipResponseWrapper wrappedResponse = new GZipResponseWrapper(
						response);
				try {
					chain.doFilter(req, wrappedResponse);
				} finally {
					wrappedResponse.flush();
				}
				return;
			}
		}
		System.out.println("CompressingFilter.doFilter(): bottom");
		chain.doFilter(req, resp);
	}

	public void destroy() {
		// nothing to do.
	}

	/**
	 * Inner Class is a ServletResponse that does compression
	 * @author Ian Darwin
	 */
	static class GZipResponseWrapper extends HttpServletResponseWrapper {

		/**
		 * @param response
		 * @throws IOException 
		 */
		public GZipResponseWrapper(HttpServletResponse response) throws IOException {
			super(response);
			createOutputStream();
		}

		/**
		 * @return 
		 * @throws IOException
		 */
		private ServletOutputStream createOutputStream() throws IOException {
			servletOutputStream = super.getOutputStream();
			GZIPOutputStream zippedOutputStream = new GZIPOutputStream(servletOutputStream);
			myServletOutputStream = new MyServletOutputStream(
							zippedOutputStream);
			return myServletOutputStream;
		}
		
		private PrintWriter writer = null;
		
		private OutputStream stream = null;

		/** Inner inner class that is just needed because
		 * getOutputStream has to return a ServletOutputStream.
		 * @author Ian Darwin
		 */
		static class MyServletOutputStream extends ServletOutputStream {
			private OutputStream os;

			MyServletOutputStream(GZIPOutputStream os) {
				super();
				this.os = os;
			}

			/** Delegate the write() to the GzipOutputStream */
			public void write(int arg0) throws IOException {
				os.write(arg0);
			}
		}

		/**
		 * The original output stream that we are wrapping;
		 * needs to be a field so we can flush() it.
		 */
		ServletOutputStream servletOutputStream;

		/** The gzipped output stream */
		MyServletOutputStream myServletOutputStream;
		
		/** getOutputStream() override that gives you the GzipOutputStream.
		 * XXX Assumes you only call this once!!
		 * @see javax.servlet.ServletResponse#getOutputStream()
		 */
		public ServletOutputStream getOutputStream() throws IOException {
			if (writer != null)
	            throw new IllegalStateException("getWriter() was already called for this response");

	        if (stream == null)
	            stream = createOutputStream();
	        
			return myServletOutputStream;
		}
		
		@Override
		public PrintWriter getWriter() throws IOException {
			if (stream != null) {
				throw new IllegalStateException("getOutputStream was already called");
			}
			if (writer == null) {
				writer = new PrintWriter(getOutputStream());
			}
			return writer;
		}

		/** Added method so we can be sure the GZipOutputStream
		 * gets flushed.
		 * @throws IOException
		 */
		public void flush() throws IOException {
			myServletOutputStream.flush();
			servletOutputStream.flush();
		}

	}
}
