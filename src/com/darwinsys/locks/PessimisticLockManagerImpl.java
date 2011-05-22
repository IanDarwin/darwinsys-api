package com.darwinsys.locks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	private int timeout = DEFAULT_TIMEOUT;
	Class<?> type;
	
	private Map<Lock, T> locks = new HashMap<Lock, T>();
	
	/** Return a clone of the Map */
	public Map<Lock, T> getLockStore() {
		return new HashMap<Lock, T>(locks);
	}
	
	/** Return a clone of the keyset as a List */
	public List<Lock> getLocks() {
		return new ArrayList<Lock>(locks.keySet());
	}
	
	private LockReaperImpl<T> lockReaper;
	
	public PessimisticLockManagerImpl() {
		setTimeout(DEFAULT_TIMEOUT);
	}
	
	public void start() {
		lockReaper = new LockReaperImpl<T>(this, timeout); // timeout in minutes
		if (!lockReaper.isAlive()) {
			lockReaper.start();
		}
	}
	
	/** Try to get the lock for the given ID */
	public synchronized Lock tryLock(T id) {
		if (!locks.containsValue(id)) { // Lock available
			Lock l = new LockImpl<T>(this, id);
			locks.put(l, id);
			return l;
		} else {			// Lock not available
			throw new PessimisticLockException("Lock in use for " + id);
		}
	}
	
	/** Release the given lock.
	 * Synchronized as it depends on "locks" being stable.
	 */
	public synchronized boolean releaseLock(Lock lock) {
		if (locks.containsKey(lock)) {
			locks.remove(lock);
			((LockImpl)lock).setReleased(true);
			return true;
		}
		return false;
	}

	/**
	 *  Shutdown this LockManager; just tells its LockReaper to go away.
	 */
	public void close() {
		lockReaper.setDone(true);
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	@Override
	public String toString() {
		return String.format(
			"PessimisticLockManagerImpl with %d locks", locks.keySet().size());
	}
}
