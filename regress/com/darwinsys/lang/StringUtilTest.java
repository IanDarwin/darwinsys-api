package regress;

import junit.framework.*;

import com.darwinsys.lang.StringUtil;

public class StringUtilTest extends TestCase {

	public void testToCommaList() {
		String[] list = { "apples", "oranges", "pumpkins", "bananas" };
		assertEquals(
			"apples, oranges, pumpkins and bananas",
			StringUtil.arrayToCommaList(list));
	}

	public void testSubst() {
		String oldStr = "Old Mc${fred} had a farm, had ${fred}";
		String expect = "Old McFRED had a farm, had FRED";
		String newStr = StringUtil.subst("${fred}", "FRED", oldStr);
		System.out.println("OLD: " + oldStr);
		System.out.println("EXP: " + expect);
		System.out.println("GOT: " + newStr);
		assertEquals(expect, newStr);
	}
}
