package com.darwinsys.locks;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class PessimisticLockManagerImplTest {

	/**
	 * Number of seconds to sleep in reclaim test. Must be > 60 (seconds per
	 * minute).
	 */
	private static final int SECONDS_TO_ENSURE_RECLAIM = 70;

	PessimisticLockManagerImpl<Integer> mgr;

	@BeforeEach
	void setup() {
		mgr = new PessimisticLockManagerImpl<Integer>();
		mgr.setTimeout(1);		// minute
		mgr.start();
	}


	@Test
	final void tryLock() {
		Lock l = mgr.tryLock(123);
		assertNotNull(l);
		// ...
		assertTrue(l.release());
		assertTrue(l.isReleased());
	}

	@Test
	final void tryLockExclusivity() {
		mgr.tryLock(123);
		assertThrows(PessimisticLockException.class, () ->
			mgr.tryLock(123));
	}

	@Test
	final void releaseLock() {
		Lock l = mgr.tryLock(123);
		assertTrue(mgr.getLockStore().containsKey(l));
		assertTrue(mgr.getLockStore().containsValue(123));
		l.release();
		assertFalse(mgr.getLockStore().containsKey(l));
		assertEquals(0, mgr.getLockStore().keySet().size(), "release");
	}

	@Disabled("test passes but takes 70 seconds")
	@Test
	final void timeoutReclaimsLock() throws Exception {
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
	final void twoTriesOneRelease() {
		Integer i = 123;
		Lock l = mgr.tryLock(i);
		assertEquals(1, mgr.getLocks().size(), "t2t1r");
		try {
			Integer i2 = 123;
			mgr.tryLock(i2);
			assertSame(1, i2);
			fail("Didn't throw PLE here");
		} catch (PessimisticLockException e) {
			// OK
		}
		assertEquals(1, mgr.getLocks().size(), "t2t1r");
		if (!l.release()) {
			fail("Lock.release returned false, it did!");
		}
		assertEquals(0, mgr.getLocks().size(), "t2t1r");		
	}

	@AfterEach
	void done() {
		mgr.close();
	}
}
