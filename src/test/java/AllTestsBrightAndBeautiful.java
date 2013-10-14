import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test ONLY the GUI components (many of which require interaction).
 * @version $Id$
 */
@RunWith(value=Suite.class)
@SuiteClasses(value={
		com.darwinsys.graphics.AllTests.class,
		com.darwinsys.swingui.AllTests.class
})
public class AllTestsBrightAndBeautiful {
	// No code needed
}
