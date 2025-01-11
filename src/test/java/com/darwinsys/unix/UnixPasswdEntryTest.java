package com.darwinsys.unix;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.regex.Matcher;

import org.junit.jupiter.api.Test;

public class UnixPasswdEntryTest {

	/** Skip tests if not running on Unix */
	public static boolean isUnix() {
		String os = System.getProperty("os.name");
		// System.out.println("os.name = " + os);
		if (os.contains("BSD") || os.contains("Linux"))
			return true;
		return false;
	}

	/** Test that the regex appears to work */
	@Test
	void regEx() {
		if (!isUnix())
			return;
		String sample = "foo:*:101:102:Foo Bar:/home/foo:/bin/ksh";
		Matcher m = UnixPasswdEntry.patt.matcher(sample);
		assertTrue(m.matches(), "line matches");
		assertEquals("foo", m.group(1), "match name");
		assertEquals("*", m.group(2), "match dummy pw");
		assertEquals("101", m.group(3), "match uid");
		assertEquals("102", m.group(4), "match gid");
		assertEquals("Foo Bar", m.group(5), "match gecos");
		assertEquals("/home/foo", m.group(6), "match homedir");
		assertEquals("/bin/ksh", m.group(7), "match shell");
	}

	/** Test for real from this system's password file */
	@Test
	void getPwEntriesString() throws Exception {
		if (!isUnix())
			return;
		List<UnixPasswdEntry> pwEntries = UnixPasswdEntry.getPwEntries();
		assertNotNull(pwEntries);
		assertTrue(pwEntries.size() > 2, "at least root & current user");
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
