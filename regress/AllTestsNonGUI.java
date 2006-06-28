
import junit.framework.Test;
import junit.framework.TestSuite;

import com.darwinsys.html.SimpleTagValidatorTest;
import com.darwinsys.io.FileIOTest;
import com.darwinsys.lang.MutableIntegerTest;
import com.darwinsys.lang.StringFormatTest;
import com.darwinsys.lang.StringUtilTest;
import com.darwinsys.sql.ConnectionUtilTest;
import com.darwinsys.util.ArrayIteratorTest;
import com.darwinsys.util.FilePropertiesTest;
import com.darwinsys.util.GetOptTest;
import com.darwinsys.util.IndexListTest;
import com.darwinsys.util.ScaledNumberFormatTest;
import com.darwinsys.util.VariableMapTest;

/**
 * Test the non-GUI components (those that don't require interaction).
 * DO NOT INCLUDE the stuff in nativeregress since it must be done by hand.
 * @version $Id$
 */
public class AllTestsNonGUI {

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(AllTestsNonGUI.class);
	}

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for regress");
		//$JUnit-BEGIN$
		suite.addTestSuite(ConnectionUtilTest.class);
		suite.addTestSuite(FilePropertiesTest.class);
		suite.addTestSuite(SimpleTagValidatorTest.class);
		suite.addTestSuite(IndexListTest.class);
		suite.addTestSuite(StringFormatTest.class);
		suite.addTestSuite(ScaledNumberFormatTest.class);
		suite.addTestSuite(FileIOTest.class);
		suite.addTestSuite(ArrayIteratorTest.class);
		suite.addTestSuite(StringUtilTest.class);
		suite.addTestSuite(GetOptTest.class);
		suite.addTestSuite(MutableIntegerTest.class);
		suite.addTestSuite(VariableMapTest.class);
		//$JUnit-END$
		return suite;
	}
}
