package com.darwinsys.net;

import org.junit.Ignore;
import org.junit.Test;

public class DNSUtilsTest  {

	/** This test is obviously dependent upon my DNS server being up: YMMV. */
	@Test @Ignore // DNS issues!
	public final void testDNSUtils() throws Exception {
		DNSUtils util = new DNSUtils("ns0.darwinsys.com");
		String mx = util.findMX("www.darwinsys.com");
		System.out.println(mx);
	}

	public final void testFindMX() {
		// XXX write a test for this.
	}
}
