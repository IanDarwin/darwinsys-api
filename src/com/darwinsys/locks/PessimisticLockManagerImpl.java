package com.darwinsys.locks;

import java.util.HashMap;
import java.util.Map;

/**
 * PessimisticLockManager implementation using a
 * simple in-JVM Map to keep track of Locks and
 * their id's (which would normally be the (compound?)
 * primary key of the row being locked).
 * Example:
 * PessimisticLockManager<Integer> mgr =
 * 		new PessimisticLockManagerImpl();
 * Lock l = mgr.tryLock(123);
 * @author Ian Darwin, based on a design I got from Stephen Neal
 */
public class PessimisticLockManagerImpl<T> implements PessimisticLockManager<T> {

	/** The time in minutes that locks will expired */
	public static final int DEFAULT_TIMEOUT = 15;	
	
	private Map<Lock, T> locks = new HashMap<Lock, T>();
	
	Map<Lock, T> getLockStore() {
		return locks;
	}
	
	final LockReaperImpl<T> lockReaper;
	
	public PessimisticLockManagerImpl() {
		this(DEFAULT_TIMEOUT);
	}
	
	public PessimisticLockManagerImpl(int timeout) {
		lockReaper = new LockReaperImpl<T>(this, timeout); // timeout in minutes
		lockReaper.start();
	}
	
	/** Try to get the lock for the given ID */
	public synchronized Lock tryLock(T id) {
		if (!locks.containsValue(id)) { // Lock available
			Lock l = new LockImpl<T>(this, id);
			locks.put(l, id);
			return l;
		} else {			// Lock not available
			throw new LockManagementException("Lock in use for " + id);
		}
	}
	
	public boolean releaseLock(Lock lock) {
		if (locks.containsKey(lock)) {
			locks.remove(lock);
			((LockImpl)lock).setReleased(true);
			return true;
		}
		return false;
	}

	public void close() {
		lockReaper.setDone(true);
	}
}
