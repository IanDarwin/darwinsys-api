package locks;

/** The representation of one lock in the
 * PessimisticLockManager.
 */
public interface Lock {
	public void release();
}
