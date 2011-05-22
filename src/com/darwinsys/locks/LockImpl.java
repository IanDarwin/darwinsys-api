package com.darwinsys.locks;

public class LockImpl<T> implements Lock {

	private PessimisticLockManager<T> mgr;
	private T id;
	private long now;
	private boolean released;

	public LockImpl(PessimisticLockManager<T> mgr, T id) {
		this.mgr = mgr;
		this.id = id;
		this.now = System.currentTimeMillis();
	}

	public boolean release() {
		return mgr.releaseLock(this);
	}
	
	public boolean isReleased() {
		return released;
	}
	
	public void setReleased(boolean r) {
		this.released = r;
	}
	
	public long getCreationTime() {
		return now;
	}
	
	@Override
	public String toString() {
		return "Lock[" + id + "]";
	}
}
