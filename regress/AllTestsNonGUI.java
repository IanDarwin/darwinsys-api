package regress;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test the non-GUI components (those that don't require interaction)
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
		//$JUnit-END$
		return suite;
	}
}