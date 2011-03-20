package com.darwinsys.unix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Structure of the readable password on a BSD system;
 * on Linux your milage may vary, and on MS-Windows
 * you're out of luck...
 */
public class UnixPasswdEntry {

	public static final String DEFAULT_FILE_NAME = "/etc/passwd";

	private String name; 		// User's login name.
	private String dummyPassword; // User's encrypted password - only in shadow
	private int uid; 			// User's login user ID.
	private int	gid; 			// User's login group ID.
	private String loginClass; 	// NOT PUBLIC User's general classification (see login.conf(5)).
	private String change; 		// NOT PUBLIC Password change time.
	private String expire; 		// NOT PUBLIC Account expiration time.
	private String gecos; 		// General information about the user.
	private String homeDir; 	// User's home directory.
	private String shell; 		// User's login shell.

	static final Pattern patt = Pattern.compile(
		"([\\w-]+):(.*?):(\\d+):(\\d+):(.*?):([/\\w]+):([-/\\w]*)");

	public static List<UnixPasswdEntry> getPwEntries() throws IOException {
		return getPwEntries(DEFAULT_FILE_NAME);
	}

    public static List<UnixPasswdEntry> getPwEntries(String fileName) throws IOException {
    	List<UnixPasswdEntry> all = new ArrayList<UnixPasswdEntry>();
    	BufferedReader rdr = new BufferedReader(new FileReader(fileName));
    	String line;
    	try {
    		while ((line = rdr.readLine()) != null) {
    			Matcher m = patt.matcher(line);
    			if (!m.matches()) {
    				throw new IOException("Line doesn't match RE " + line);
    			}
    			UnixPasswdEntry ent = new UnixPasswdEntry();
    			ent.name = m.group(1);
    			ent.dummyPassword = m.group(2);
    			ent.uid = Integer.parseInt(m.group(3));
    			ent.gid = Integer.parseInt( m.group(4));
    			ent.gecos = m.group(5);
    			ent.homeDir = m.group(6);
    			ent.shell = m.group(7);
    			all.add(ent);
    		}
    	} finally {
    		if (rdr != null)
    			rdr.close();
    	}
    	return all;
    }

    @Override
    public String toString() {
    	return String.format("pw_ent(%s, %d, %s, %s)", name, uid, gecos, homeDir);
    }

	public String getChange() {
		return change;
	}

	public void setChange(String change) {
		this.change = change;
	}

	public String getDummyPassword() {
		return dummyPassword;
	}

	public void setDummyPassword(String dummyPassword) {
		this.dummyPassword = dummyPassword;
	}

	public String getExpire() {
		return expire;
	}

	public void setExpire(String expire) {
		this.expire = expire;
	}

	public String getGecos() {
		return gecos;
	}

	public void setGecos(String gecos) {
		this.gecos = gecos;
	}

	public int getGid() {
		return gid;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	public String getHomeDir() {
		return homeDir;
	}

	public void setHomeDir(String homeDir) {
		this.homeDir = homeDir;
	}

	public String getLoginClass() {
		return loginClass;
	}

	public void setLoginClass(String loginClass) {
		this.loginClass = loginClass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShell() {
		return shell;
	}

	public void setShell(String shell) {
		this.shell = shell;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public static Pattern getPattern() {
		return patt;
	}
}
