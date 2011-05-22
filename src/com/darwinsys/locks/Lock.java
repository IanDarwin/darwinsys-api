package com.darwinsys.locks;

/** The representation of one lock in the
 * PessimisticLockManager.
 */
public interface Lock {
	
	/** Release this lock
	 * @return true if we released it, false if it was already released.
	 */
	public boolean release();
	
	/** Find out if this lock got released
	 * (typically due to being timed out)
	 * before we commit the action based on it.
	 */
	public boolean isReleased();
}
