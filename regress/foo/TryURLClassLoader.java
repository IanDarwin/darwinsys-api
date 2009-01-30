package foo;

import java.net.URL;
import java.net.URLClassLoader;

import org.junit.Test;

public class TryURLClassLoader {
	@Test
	public void _() throws Exception {
		URL u1 = new URL("file:///tmp/");
		URL u2 = new URL("file:///home/ian/lib/darwinsys.jar");
		URLClassLoader cl =
			new URLClassLoader(new URL[]{u1,u2});
		cl.loadClass("com.darwinsys.util.DateSimple");
		// Load non-java but from JRE/JDK:
		cl.loadClass("sun.jdbc.odbc.JdbcOdbcDriver");
	}
}
