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
 * PessimisticLockManager&lt;Integer&gt; mgr =
 * 		new PessimisticLockManagerImpl();
 * Lock l = mgr.tryLock(123);
 * @author Ian Darwin, based on a design from Stephen Neal
 */
public class PessimisticLockManagerImpl<T> implements PessimisticLockManager<T> {

	/** The time in minutes that locks will expired */
	public static final int DEFAULT_TIMEOUT = 15;
	private int timeout = DEFAULT_TIMEOUT;

	private Map<Lock, T> locks = new HashMap<Lock, T>();
	
	/** Return a clone of the Map
	 * @return A copy of the map
	 */
	public Map<Lock, T> getLockStore() {
		return new HashMap<Lock, T>(locks);
	}
	
	/** Return a clone of the keyset as a List
	 * @return A copy of the keyset
	 */
	public List<Lock> getLocks() {
		return new ArrayList<Lock>(locks.keySet());
	}
	
	private LockReaperImpl<T> lockReaper;
	
	public PessimisticLockManagerImpl() {
		setTimeout(DEFAULT_TIMEOUT);
	}
	
	/* @inheritDoc */
	public void start() {
		lockReaper = new LockReaperImpl<T>(this, timeout); // timeout in minutes
		if (!lockReaper.isAlive()) {
			lockReaper.start();
		}
	}
	
	/* @inheritDoc */
	public Lock tryLock(T id) {
		 return tryLock(id, null);
	}
	
	/* @inheritDoc */
	public synchronized Lock tryLock(T id, Object owner) {
		if (lockReaper == null) {
			start();
		}
		if (!locks.containsValue(id)) { // Lock available
			Lock l = new LockImpl<T>(this, id, owner);
			locks.put(l, id);
			return l;
		} else {			// Lock not available
			throw new PessimisticLockException("Lock in use for " + id);
		}
	}
	
	/** Release the given lock.
	 * Synchronized as it depends on "locks" being stable.
	 * @return True if the lock could be removed
	 */
	public synchronized boolean releaseLock(Lock lock) {
		if (locks.containsKey(lock)) {
			((LockImpl)lock).setReleased(true);
			final boolean removed = locks.remove(lock) != null;
			return removed;
		}
		return false;
	}

	/**
	 *  Shutdown this LockManager; tells its LockReaper to go away
	 *  and releases and discards all locks.
	 */
	public synchronized void close() {
		lockReaper.setDone(true);
		for (Lock l : getLocks()) {
			l.release();
			locks.remove(l);
		}
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
