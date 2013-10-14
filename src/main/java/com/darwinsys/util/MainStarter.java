package com.darwinsys.util;

import java.lang.reflect.Method;
import java.security.Permission;

/** Start a bunch of Main programs, to avoid starting a JVM for each
 * XXX TODO a taskbar icon with a menu to exit, and a SecurityManager
 * that allows all but System.exit for other classes.
 */
public class MainStarter {
	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("Usage: MainStarter class class [...]");
		}
		Class[] typeParams = { args.getClass() };

		for (final String clazz : args) {
			try {
				final Class<?> c = Class.forName(clazz);
				final Method m = c.getMethod("main", typeParams);
				final String[] noArgs = new String[0];
				final Object[] passedArgs = { noArgs };
				new Thread(new Runnable() {
					public void run() {
						try {
							m.invoke(null, passedArgs);
						} catch (Throwable t) {
							System.out.println(clazz + " failed: " + t);
							t.printStackTrace();
						}
					}
				}).start();

			} catch (Throwable t) {
				System.out.println(clazz + " failed: " + t);
				t.printStackTrace();
			}
		}

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// stupid exception
		}

		// Now the tasks have had time to start, put in a security manager to stop them from exiting.
		// Since this is a main application, allow everything else (for now).
		System.setSecurityManager(new SecurityManager() {
			public void checkPermission(Permission p) {
				// empty
			}
			public void checkPermission(Permission p, Object secContext) {
				// empty
			}
			public void checkExit(int status) {
				throw new SecurityException("One started task cannot exit this whole mess");
			}
		});
	}
}
