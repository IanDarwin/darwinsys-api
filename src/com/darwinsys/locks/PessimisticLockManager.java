package locks;

/** PessimisticLockManager keeps track of Locks and
* their id's (which would normally be the (compound?)
* primary key of the row being locked).
* Example:
* PessimisticLockManager<Integer> mgr =
* new PessimisticLockManagerImpl();
* Lock l = mgr.tryLock(123);
* ...
* l.release();
* @author Ian Darwin, based on a design by Stephen Neal
* */
public interface PessimisticLockManager<T> {
	
	/** Try to get the lock for the given ID */
	Lock tryLock(T id);
	
	/** Release the given lock. */
	boolean releaseLock(Lock lock);
}
