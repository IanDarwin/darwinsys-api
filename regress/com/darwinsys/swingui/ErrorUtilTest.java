package regress;

import java.sql.SQLException;
import com.darwinsys.swingui.ErrorUtil;

public class ErrorUtilTest {
	public static void main(String[] args) {
		SQLException ex1 = new SQLException("One");
		SQLException ex2 = new SQLException("Two");
		SQLException ex3 = new SQLException("Three");
		ex1.setNextException(ex2);
		ex2.setNextException(ex3);
		ErrorUtil.showExceptions(null, ex3);

		Exception ioe = new Exception("Read error",
			new java.io.FileNotFoundException("No such file"));
		ErrorUtil.showExceptions(null, ioe);

		System.exit(0);
	}
}
