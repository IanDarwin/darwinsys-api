package locks;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Test;

public class PessimisticLockManagerImplTest {

	final PessimisticLockManagerImpl<Integer> mgr =
		new PessimisticLockManagerImpl<Integer>();
	
	@Test
	public final void testTryLock() {
		Lock l = mgr.tryLock(123);
		assertNotNull(l);
		// ...
		l.release();
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
		System.out.println("Timeout Test sleeping 90 s to simulate user activity");
		Thread.sleep(90000);
		assertFalse(mgr.getLockStore().containsKey(l));
	}
	
	@After
	public void done() {
		mgr.close();
	}
}
