package com.darwinsys.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletResponseWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter to do compression.
 * @author Main class by Stephen Neal(?), hacked on by Ian Darwin.
 * GzipResponseWrapper class by Ian Darwin.
 * Status: UNPROVEN
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

		if (req instanceof HttpServletResponse) {
			HttpServletRequest request = (HttpServletRequest) req;
			HttpServletResponse response = (HttpServletResponse) resp;
			String acceptableEncodings = request.getHeader("accept-encoding");
			if (acceptableEncodings != null
					&& acceptableEncodings.indexOf("gzip") != -1) {

				// Create a delegate for the Response object; all methods
				// are directly delegated except getOutputStream.
				// This wrapper class is defined below.
				GZipResponseWrapper wrappedResponse = new GZipResponseWrapper(
						response);
				chain.doFilter(req, wrappedResponse);
				wrappedResponse.flush();
				return;
			}
		}
		chain.doFilter(req, resp);
	}

	public void destroy() {
		// nothing to do.
	}

	/**
	 * Inner Class is a ServletResponse that does compression
	 * @author Ian Darwin
	 */
	static class GZipResponseWrapper extends ServletResponseWrapper {

		/**
		 * @param ressponse
		 */
		public GZipResponseWrapper(ServletResponse response) {
			super(response);
		}

		/** Inner inner class that is a ServletOutputStream.
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
		GZIPOutputStream zippedOutputStream;
		
		/** getOutputStream() override that gives you the GzipOutputStream.
		 * @see javax.servlet.ServletResponse#getOutputStream()
		 */
		public ServletOutputStream getOutputStream() throws IOException {
			servletOutputStream = super.getOutputStream();
			zippedOutputStream = new GZIPOutputStream(servletOutputStream);
			return new MyServletOutputStream(
				zippedOutputStream);
		}

		/** Added method so we can be sure the GZipOutputStream
		 * gets flushed.
		 * @throws IOException
		 */
		public void flush() throws IOException {
			zippedOutputStream.flush();
			servletOutputStream.flush();
		}

	}
}
