import junit.framework.Test;
import junit.framework.TestSuite;

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
		suite.addTestSuite(util.FilePropertiesTest.class);
		suite.addTestSuite(SimpleTagValidatorTest.class);
		suite.addTestSuite(util.IndexListTest.class);
		suite.addTestSuite(lang.StringFormatTest.class);
		suite.addTestSuite(ScaledNumberFormatTest.class);
		suite.addTestSuite(io.FileIOTest.class);
		suite.addTestSuite(util.ArrayIteratorTest.class);
		suite.addTestSuite(lang.StringUtilTest.class);
		suite.addTestSuite(util.GetOptTest.class);
		suite.addTestSuite(lang.MutableIntegerTest.class);
		//$JUnit-END$
		return suite;
	}
}
