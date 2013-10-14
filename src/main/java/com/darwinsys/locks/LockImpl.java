package com.darwinsys.locks;

import java.util.Date;

public class LockImpl<T> implements Lock {

	private PessimisticLockManager<T> mgr;
	private T id;
	private long now;
	private boolean released;
	private Object owner;

	public LockImpl(PessimisticLockManager<T> mgr, T id, Object owner) {
		this.mgr = mgr;
		this.id = id;
		this.owner = owner;
		this.now = System.currentTimeMillis();
	}
	
	@Override
	/** Just to make explicit that we use identity equals here */
	public boolean equals(Object obj) {
		return this == obj;
	}

	@Override
	/** Just to silence complaints, since we implement equals() */
	public int hashCode() {
		return super.hashCode();
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

	public T getId() {
		return id;
	}

	public Object getOwner() {
		return owner;
	}
	
	@Override
	public String toString() {
		Date when = new Date(getCreationTime());
		return "Lock[" + id + (owner != null ? ", " + owner : "(nobody)") + "@" + when + "]";
	}
}
