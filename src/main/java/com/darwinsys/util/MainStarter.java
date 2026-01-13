package com.darwinsys.util;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
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

	final static String PROPS_FILE = ".mydesktop.properties";

	/**
	 * The input consists of a class name and ONE optional argument.
	 * If no args, these pairs are read from ~/PROPS_FILE.
	 * If there are args, each is treated as a class name, with no args.
	 */
	private record ClassAndArg(String clazzName, String arg){}

	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			processFile(Path.of(System.getProperty("user.home"), PROPS_FILE));
		} else {
			for (String s : args) {
				process(new ClassAndArg(s, ""));
			}
		}
	}

	/**
	 * Process a properties-like file with class name and an arg for each
	 */
	static void processFile(Path path) throws IOException {
		if (!Files.exists(path)) {
			throw new IllegalArgumentException(path + " " + "Does not exist or not readable");
		}
		Files.lines(path).map(s -> s.split("="))
					.map(a -> new ClassAndArg(a[0], 1==a.length?"":a[1]))
							.forEach(caa -> process(caa));
	};

	static final ExecutorService tPool = Executors.newCachedThreadPool();

	/**
	 * Invoke one ClassAndArg.
	 * @param caa the ClassAndArg to process
	 */
	static void process(ClassAndArg caa) {

			// Catch errors so one mistake doesn't kill the whole thing...
			try {
				final Class<?> c = Class.forName(caa.clazzName);
				final Method m = c.getMethod("main", String[].class);
				tPool.submit( () -> {
						try {
							final Object[] args = new Object[1];
							args[0] = new String[] {caa.arg};
							m.invoke(null, args);
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
