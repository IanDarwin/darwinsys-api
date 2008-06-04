
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
import com.darwinsys.util.ArrayIteratorTest;
import com.darwinsys.util.FilePropertiesTest;
import com.darwinsys.util.IndexListTest;
import com.darwinsys.util.ScaledNumberFormatFormat2Test;
import com.darwinsys.util.ScaledNumberFormatFormatTest;
import com.darwinsys.util.ScaledNumberFormatParseTest;
import com.darwinsys.util.VariableMapTest;

/**
 * Test the non-GUI components (those that don't require interaction).
 * DO NOT INCLUDE the stuff in nativeregress since it must be done by hand.
 * @version $Id$
 */
@RunWith(value=Suite.class)
@SuiteClasses(value={
		ConnectionUtilTest.class,
		FilePropertiesTest.class,
		SimpleTagValidatorTest.class,
		IndexListTest.class,
		StringFormatTest.class,
		ScaledNumberFormatFormatTest.class,
		ScaledNumberFormatFormat2Test.class,
		ScaledNumberFormatParseTest.class,
		FileIOTest.class,
		ArrayIteratorTest.class,
		StringUtilTest.class,
		GetOptConstructorTest.class,
		GetOptDescTest.class,
		GetOptTestOldPattern.class,
		GetOptTestNewPattern.class,
		MutableIntegerTest.class,
		VariableMapTest.class,
})

public class AllTestsNonGUI {
	// No code needed
}
