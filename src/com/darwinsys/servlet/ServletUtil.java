package com.darwinsys.util;

import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

/** Utilities for use in Servlet/JSP code.
 * @version $Id$
 */
public class ServletUtil {

	/** Common need to get a JDBC connection from within a Servlet,
	 * but cache it in the Application or ServletContext object
	 * (Connection objects are ThreadSafe).
	 * @return a JDBC connection, or null.
	 */
	public static Connection getConnection(
		ServletContext application,
		String connAttrName)
		throws SQLException, ClassNotFoundException {


		Connection conn = null;

		// Look if it's already in the Application object
		conn = (Connection)application.getAttribute(connAttrName);

		// If not, get it from JDBC
		if (conn == null) {
			// Do the JDBC thingamie.
			String driver = application.getInitParameter("db.driver");
			Class.forName(driver);
			String dbURL  = application.getInitParameter("db.url");
			String dbUser = application.getInitParameter("db.user");
			String dbPass = application.getInitParameter("db.password");

			// Now save back in Application for next time.
			application.setAttribute(connAttrName, conn);
		}

		return conn;
	}
}
