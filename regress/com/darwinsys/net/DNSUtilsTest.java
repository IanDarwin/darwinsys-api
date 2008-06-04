package com.darwinsys.net;

import junit.framework.TestCase;

public class DNSUtilsTest extends TestCase {

	public final void testDNSUtils() throws Exception {
		DNSUtils util = new DNSUtils("dns1.sympatico.ca");
		String mx = util.findMX("www.darwinsys.com");
		System.out.println(mx);
	}

	public final void testFindMX() {
		// XXX write a test for this.
	}

}
