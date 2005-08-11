package com.darwinsys.net;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class DNSUtils {
	private DirContext ctx;
	private Pattern p;
	
	/** Construct a DNSUtils object. 
	 * @param dnsHost The name of a host that runs a DNS server
	 * @throws NamingException
	 */
	public DNSUtils(String dnsHost) throws NamingException {
		System.out.println("DNSUtils: Creating DirContext");
		System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
		System.setProperty(Context.PROVIDER_URL, "dns://" + dnsHost);
		ctx = new InitialDirContext();
		System.out.println("Done Creating DirContext");
		p = Pattern.compile("(\\d+)\\s+([\\w.]+)");
	}
	
	/** Find the MX record for a given host.
	 * This implementation returns the first DNS host listed;
	 * a later version will sort them and pick the best one.
	 * @param host
	 * @return The MX host, or the input if there is no MX.
	 * @throws NamingException
	 */
	public String findMX(String host) throws NamingException {
		System.out.println("DNSUtils: Getting attributes...");
		Attributes attrs = ctx.getAttributes(host, new String[] {"MX"});
		NamingEnumeration enumeration = attrs.getAll();
		while (enumeration.hasMore()) {
			final Attribute attr = (Attribute)enumeration.next();
			System.out.println("<< " + attr.getClass().getName());
			System.out.println(attr.getID());
			String mx = (String)attr.get();
			Matcher m = p.matcher(mx);
			if (m.matches()) {
				String rating = m.group(1);
				String mailServerHost = m.group(2);
				System.out.println(mailServerHost + " --> " + rating);
				return mailServerHost;
			} else {
				throw new IllegalStateException("RegEx Match Failed");
			}
		}
		return host;
	}
}
