package com.darwinsys.servlet;

import java.lang.reflect.Method;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * SalmonPropsPath replacement - Sets up for SOFIA to use e.g., 
 * WEB-INF for props path.
 * @version $Id$
 */
public class SalmonPropsPath extends HttpServlet {
	
	private static final long serialVersionUID = 3978421403932571703L;
	/** Where to look in the Context Properties for the salmon path.
	 * This should be kept in sync with the property of the same name
	 * in class com.salmonllc.properties.Props. It was copied here
	 * to avoid making com.darwinsys compilation depend on SOFIA for
	 * this obvious String property... and so we use Reflection to
	 * get at Props to set the path.
	 */
	public static final String SYS_PROPS_DIR_PROPERTY = "salmon.props.path";

	public void init(ServletConfig sc) throws ServletException {
		String salmonPropsPath = sc.getInitParameter(SYS_PROPS_DIR_PROPERTY);
		String fullPath = sc.getServletContext().getRealPath(salmonPropsPath);

		// Do "Props.setPropsPath(fullPath, true);" using Reflection.
		Class c = null;
		try {
			c = Class.forName("com.salmonllc.properties.Props");
			Class[] argTypes = { String.class, boolean.class };
			Method m = c.getMethod("setPropsPath", argTypes);
			Object[] args = { fullPath, Boolean.TRUE };
			System.out.println("Invoking: " + m);
			m.invoke(null, args);
		} catch (Exception ex) {
			System.out.println("Caught exception: " + ex);
			throw new ServletException(ex);
		}
		super.init(sc);
	}
}
