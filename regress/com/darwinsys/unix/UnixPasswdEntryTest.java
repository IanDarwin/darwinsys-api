package com.darwinsys.unix;

import java.util.List;
import java.util.regex.Matcher;

import junit.framework.TestCase;

public class UnixPasswdEntryTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testRegEx() {
		String sample = "foo:*:101:102:Foo Bar:/home/foo:/bin/ksh";
		Matcher m = UnixPasswdEntry.patt.matcher(sample);
		assertTrue("line matches", m.matches());
		assertEquals("match name", "foo", m.group(1));
		assertEquals("match dummy pw", "*", m.group(2));
		assertEquals("match uid", "101", m.group(3));
		assertEquals("match gid", "102", m.group(4));
		assertEquals("match gecos", "Foo Bar", m.group(5));
		assertEquals("match homedir", "/home/foo", m.group(6));
		assertEquals("match shell", "/bin/ksh", m.group(7));
	}

	public void testGetPwEntriesString() throws Exception {
		List<UnixPasswdEntry> pwEntries = UnixPasswdEntry.getPwEntries();
		assertNotNull(pwEntries);
		assertTrue("at least root & current user", pwEntries.size() > 2);
		String loggedInAs = System.getProperty("user.name");
		for (UnixPasswdEntry ent : pwEntries) {
			if (loggedInAs.equals(ent.getName())) {
				System.out.println("Current user = " + ent);
				return;
			}
		}
		fail("Did not find entry for current user name");
	}

}
