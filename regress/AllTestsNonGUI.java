
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.darwinsys.html.SimpleTagValidatorTest;
import com.darwinsys.io.FileIOTest;
import com.darwinsys.lang.GetOptConstructorTest;
import com.darwinsys.lang.GetOptDescTest;
import com.darwinsys.lang.GetOptTestNewPattern;
import com.darwinsys.lang.GetOptTestOldPattern;
import com.darwinsys.lang.MutableIntegerTest;
import com.darwinsys.lang.StringFormatTest;
import com.darwinsys.lang.StringUtilTest;
import com.darwinsys.sql.ConnectionUtilTest;

/**
 * Test the non-GUI components (those that don't require interaction).
 * DO NOT INCLUDE the stuff in nativeregress since it must be done by hand.
 * @version $Id$
 */
@RunWith(value=Suite.class)
@SuiteClasses(value={
		ConnectionUtilTest.class,
		SimpleTagValidatorTest.class,
		StringFormatTest.class,
		FileIOTest.class,
		StringUtilTest.class,
		GetOptConstructorTest.class,
		GetOptDescTest.class,
		GetOptTestOldPattern.class,
		GetOptTestNewPattern.class,
		MutableIntegerTest.class,
		com.darwinsys.util.AllTests.class
})

public class AllTestsNonGUI {
	// No code needed
}
