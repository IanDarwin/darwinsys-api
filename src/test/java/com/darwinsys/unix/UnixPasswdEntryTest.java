package com.darwinsys.unix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.regex.Matcher;

import org.junit.Test;

public class UnixPasswdEntryTest {

	/** Skip tests if not running on Unix */
	private boolean isUnix() {
		String os = System.getProperty("os.name");
		// System.out.println("os.name = " + os);
		if (os.contains("BSD") || os.contains("Linux"))
			return true;
		return false;
	}
	
	/** Test that the regex appears to work */
	@Test
	public void testRegEx() {
		if (!isUnix())
			return;
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

	/** Test for real from this system's password file */
	@Test
	public void testGetPwEntriesString() throws Exception {
		if (!isUnix())
			return;
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
