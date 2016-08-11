package com.darwinsys.locks;

/** 
 * PessimisticLockManager keeps track of Locks and
 * their id's, which would normally be the (possibly compound)
 * primary key of the row being locked).
 * There's no expiry notification (indeed, the thread that
 * got the lock might not be active when expiry happens),
 * so it's crucial that you check isReleased()
 * before proceding with the operation that was locked.
 * Example:
 * PessimisticLockManager&lt;Integer&lt; mgr =
 * new PessimisticLockManagerImpl&lt;Integer&lt;();
 * Lock l = mgr.tryLock(123);
 * ...
 * if (l.isReleased()) {
 *   message("Sorry, you took too long");
 *   return;
 *   }
 * ... OK, commit the order... 
 * l.release();
 * @author Ian Darwin, based on a design from Stephen Neal
 */
public interface PessimisticLockManager<T> {
	
	/** Start the given lock manager */
	void start();
	
	/** Try to get the lock for the given ID
	 * @param id The primary key object, which must implement equals+hashCode correctly
	 * @return The lock
	 */
	Lock tryLock(T id);
	
	/** Try to get the lock for the given ID, with reportable owner
	 * @param id The primary key object, which must implement equals+hashCode correctly
	 * @param owner An arbitrary object for reporting, but often the customer or user on 
	 * whose behalf the app is locking the id.
	 * @return The lock
	 */
	Lock tryLock(T id, Object owner);
	
	/** Release the given lock. 
	 * @param lock The lock
	 * @return True if the lock was held
	 */
	boolean releaseLock(Lock lock);

	/** Get the timeout period for locks expiry, in minutes 
	 * @return Timeout period
	 */
	int getTimeout();
}
