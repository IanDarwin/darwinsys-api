package regress;

import junit.framework.*;

import com.darwinsys.lang.StringFormat;

public class StringFormatTest extends TestCase {
	String mesg = "JavaFun";

	public void testLeft() {
		assertEquals("JavaF",
			new StringFormat(5, StringFormat.JUST_LEFT).format(mesg));
		assertEquals("JavaFun   ",
			new StringFormat(10, StringFormat.JUST_LEFT).format(mesg));
	}
	public void testCentre() {
		assertEquals("JavaF",
			new StringFormat(5, StringFormat.JUST_CENTER).format(mesg));
		assertEquals(" JavaFun  ",
			new StringFormat(10, StringFormat.JUST_CENTER).format(mesg));
	}
	public void testRight() {
		assertEquals("JavaF",
			new StringFormat(5, StringFormat.JUST_RIGHT).format(mesg));
		assertEquals("   JavaFun",
			new StringFormat(10, StringFormat.JUST_RIGHT).format(mesg));
	}
}
