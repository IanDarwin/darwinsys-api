package com.darwinsys.locks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;

public class PessimisticLockManagerImplTest {

	/**
	 * Number of seconds to sleep in reclaim test. Must be > 60 (seconds per
	 * minute).
	 */
	private static final int SECONDS_TO_ENSURE_RECLAIM = 70;

	PessimisticLockManagerImpl<Integer> mgr;
	
	@Before
	public void setup() {
		mgr = new PessimisticLockManagerImpl<Integer>();
		mgr.setTimeout(1);		// minute
		mgr.start();
	}
	
	
	@Test
	public final void testTryLock() {
		Lock l = mgr.tryLock(123);
		assertNotNull(l);
		// ...
		assertTrue(l.release());
		assertTrue(l.isReleased());
	}
	
	@Test(expected=PessimisticLockException.class)
	public final void testTryLockExclusivity() {
		mgr.tryLock(123);
		mgr.tryLock(123);
	}

	@Test
	public final void testReleaseLock() {
		Lock l = mgr.tryLock(123);
		assertTrue(mgr.getLockStore().containsKey(l));
		assertTrue(mgr.getLockStore().containsValue(123));
		l.release();
		assertFalse(mgr.getLockStore().containsKey(l));
		assertEquals("release", 0, mgr.getLockStore().keySet().size());
	}
	
	@Ignore("test passes but takes 70 seconds")
	@Test
	public final void testTimeoutReclaimsLock() throws Exception {
		Lock l = mgr.tryLock(123);
		assertTrue(mgr.getLockStore().containsKey(l));
		assertTrue(mgr.getLockStore().containsValue(123));
		System.out.println("Timeout Test sleeping " + SECONDS_TO_ENSURE_RECLAIM + " seconds to simulate user activity");
		Thread.sleep(SECONDS_TO_ENSURE_RECLAIM * 1000);
		System.out.println("Done sleeping");
		assertTrue(l.isReleased());
		assertFalse(l.release());
		assertFalse(mgr.getLockStore().containsKey(l));
		assertFalse(mgr.getLockStore().containsValue(123));
	}
	
	@Test
	public final void testTwoTriesOneRelease() {
		Integer i = 123;
		Lock l = mgr.tryLock(i);
		assertEquals("t2t1r", 1, mgr.getLocks().size());
		try {
			Integer i2 = 123;
			mgr.tryLock(i2);
			assertSame(1, i2);
			fail("Didn't throw PLE here");
		} catch (PessimisticLockException e) {
			// OK
		}
		assertEquals("t2t1r", 1, mgr.getLocks().size());
		if (!l.release()) {
			fail("Lock.release returned false, it did!");
		}
		assertEquals("t2t1r", 0, mgr.getLocks().size());		
	}
	
	@After
	public void done() {
		mgr.close();
	}
}
