package locks;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Test;

public class PessimisticLockManagerImplTest {

	/** Number of seconds to sleep in reclaim test.
	 * Must be > 60 (seconds per minute).
	 */
	private static final int NSEC = 70;

	final PessimisticLockManagerImpl<Integer> mgr =
		new PessimisticLockManagerImpl<Integer>();
	
	@Test
	public final void testTryLock() {
		Lock l = mgr.tryLock(123);
		assertNotNull(l);
		// ...
		l.release();
		assertTrue(l.isReleased());
	}
	
	@Test(expected=LockManagementException.class)
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
	}
	
	@Test
	public final void testTimeoutReclaimsLock() throws Exception {
		Lock l = mgr.tryLock(123);
		assertTrue(mgr.getLockStore().containsKey(l));
		assertTrue(mgr.getLockStore().containsValue(123));
		System.out.println("Timeout Test sleeping " + NSEC + " s to simulate user activity");
		Thread.sleep(NSEC * 1000);
		assertTrue(l.isReleased());
		assertFalse(mgr.getLockStore().containsKey(l));
		assertFalse(mgr.getLockStore().containsValue(123));
	}
	
	@After
	public void done() {
		mgr.close();
	}
}
