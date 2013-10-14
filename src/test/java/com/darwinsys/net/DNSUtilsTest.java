package com.darwinsys.net;

import junit.framework.TestCase;

public class DNSUtilsTest extends TestCase {

	/** This test is obviously dependant upon my DNS server being up: YMMV. */
	public final void testDNSUtils() throws Exception {
		DNSUtils util = new DNSUtils("ns0.darwinsys.com");
		String mx = util.findMX("www.darwinsys.com");
		System.out.println(mx);
	}

	public final void testFindMX() {
		// XXX write a test for this.
	}

}
