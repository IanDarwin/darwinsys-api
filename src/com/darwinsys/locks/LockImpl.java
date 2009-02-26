package locks;

public class LockImpl<T> implements Lock {

	private PessimisticLockManager<T> mgr;
	private T id;
	private long now;

	public LockImpl(PessimisticLockManager<T> mgr, T id) {
		this.mgr = mgr;
		this.id = id;
		this.now = System.currentTimeMillis();
	}

	public void release() {
		mgr.releaseLock(this);
	}
	
	public long getCreationTime() {
		return now;
	}
	
	@Override
	public String toString() {
		return "Lock[" + id + ']';
	}
}
