package com.darwinsys.locks;

import java.util.Date;
import java.util.Map;

/**
 * Reaps locks based on their age
 */
public class LockReaperImpl<T> extends Thread {
	
	private static final int DEFAULT_TIMEOUT_MINUTES = 15;
	/** Time in MINUTES to expire locks */
	private int timeOutMinutes;
	
	/** Time in SECONDS to sleep between runs;
	 * will not be an exact "interval timer" on most JVMs
	 */
	private static final int RUN_INTERVAL = 60;
	private int sleepSeconds = RUN_INTERVAL;
	/** The lock manager that we are part of */
	private final PessimisticLockManager<T> mgr;
	/** Flag to allow the Thread to terminate when finished */
	private boolean done;
	
	/** Construct a Reaper with the default timeout */
	public LockReaperImpl(PessimisticLockManager<T> mgr) {
		this(mgr, DEFAULT_TIMEOUT_MINUTES);
	}
	
	/** Construct a Reaper with the given number of
	 * minutes' timeout
	 * @param minutes Minutes to timeout
	 */
	public LockReaperImpl(PessimisticLockManager<T> mgr, int minutes) {
		this.timeOutMinutes = minutes;
		this.mgr = mgr;
	}
	
	/**
	 * Reap expired locks.
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		while (!done) {
			timeOutMinutes = mgr.getTimeout(); // update dynamically
			Map<Lock,T> map = ((PessimisticLockManagerImpl)mgr).getLockStore();
			if (map.keySet().size() > 0) {
				System.out.println("LockReaper: Locks currently held at " + new Date() + ":");
			}
			for (Lock lock : map.keySet()) {
				LockImpl<T> l = (LockImpl) lock;
				System.out.println("LockReaper: " + l);
				final long now = System.currentTimeMillis();
				final long then = l.getCreationTime();
				if (now - then > timeOutMinutes * 60000) {
					System.out.println("LockReaper: Removing stale lock " + l);					
					mgr.releaseLock(l);
				}
			}
			try {
				Thread.sleep(1000 * sleepSeconds);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void setDone(boolean done) {
		this.done = done;
	}
}
