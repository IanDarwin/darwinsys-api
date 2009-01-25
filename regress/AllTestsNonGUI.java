
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test the non-GUI components (those that don't require interaction).
 * DO NOT INCLUDE the stuff in nativeregress since it must be done by hand.
 * @version $Id$
 */
@RunWith(value=Suite.class)
@SuiteClasses(value={
		com.darwinsys.calendar.AllTests.class,
		com.darwinsys.csv.AllTests.class,
		com.darwinsys.html.AllTests.class,
		com.darwinsys.io.AllTests.class,
		com.darwinsys.lang.AllTests.class,
		com.darwinsys.reflection.AllTests.class,
		com.darwinsys.sql.AllTests.class,
		com.darwinsys.util.AllTests.class
})
public class AllTestsNonGUI {
	// No code needed
}
