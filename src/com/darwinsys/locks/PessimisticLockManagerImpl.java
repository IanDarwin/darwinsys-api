package locks;

import java.util.HashMap;
import java.util.Map;

/**
 * PessimisticLockManager implementation using a
 * simple in-JVM Map to keep track of Locks and
 * their id's (which would normally be the (compound?)
 * primary key of the row being locked).
 * Example:
 * PessimisticLockManager<Integer> mgr =
 * new PessimisticLockManagerImpl();
 * Lock l = mgr.tryLock(123);
 * @author Ian Darwin, based on a design by Stephen Neal
 */
public class PessimisticLockManagerImpl<T> implements PessimisticLockManager<T> {

	private Map<Lock, T> locks = new HashMap<Lock, T>();
	
	@SuppressWarnings("unchecked")
	Map<Lock, Object> getLockStore() {
		return (Map<Lock, Object>) locks;
	}
	final LockReaperImpl<T> lockReaper = new LockReaperImpl<T>(this, 1);
	public PessimisticLockManagerImpl() {
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
			return true;
		}
		return false;
	}

	public void close() {
		lockReaper.setDone(true);
	}
}
