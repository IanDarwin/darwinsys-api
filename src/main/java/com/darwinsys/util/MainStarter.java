package com.darwinsys.util;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.security.Permission;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

import javax.swing.JOptionPane;

/**
 * Try to build a really simple multiple-widget launcher.
 * Each widget has a standard main() method, and
 * can have 0 or 1 args passed to it (not 2 or 3).
 * The properties file lists the class and its arg, if any.
 * ALL classes must be on classpath when you run this.
 * Each widget is invoked in a distinct Thread;
 * Java needs a REPLACEMENT for SecurityManager.checkExit()
 * as we don't want one of these items to exit the whole mess.
 */
@SuppressWarnings("ALL")
public class MainStarter {

	final static String PROPS_FILE = "mydesktop.properties";

	/** The input consists of a class name and ONE optional argument */
	record ClassAndArg(String clazzName, String arg){}

	public static void main(String[] args) throws Exception {

		if (new File(PROPS_FILE).exists()) {
			Properties props = new Properties();
			props.load(new FileInputStream(PROPS_FILE));
			for (Object o : props.keySet()) {
				String s = (String)o;
				ClassAndArg caa = new ClassAndArg(s, props.getProperty(s));
				process(caa);
			}
		} else {
			if (args.length == 0) {
				eHandler.accept("Usage: MainStarter propsfile | clazzName [...]", null);
				System.exit(1);
			}
			for (String s : args) {
				process(new ClassAndArg(s, ""));
			}
		}
	}

	static void process(ClassAndArg caa) {
		ExecutorService tPool = Executors.newCachedThreadPool();

			// Catch errors so one mistake doesn't kill the whole thing...
			try {
				final Class<?> c = Class.forName(caa.clazzName);
				final Method m = c.getMethod("main", caa.arg.getClass());
				tPool.submit( () -> {
						try {
							m.invoke(null, caa.arg);
						} catch (Exception e) {
							eHandler.accept(caa.clazzName, e);
						}
				});
			} catch (Exception e) {
				eHandler.accept(caa.clazzName, e);
			}
	}

	static BiConsumer<String, Exception> eHandler = (String message, Exception e) -> {
		JOptionPane.showMessageDialog(
			null, message + (e != null? ": " + e : ""),
			"Error", JOptionPane.ERROR_MESSAGE);
		if (e != null) {
			e.printStackTrace();
		}
	};
}
