package locks;

import java.util.Date;
import java.util.Map;

/**
 * Reaps locks based on their age
 */
public class LockReaperImpl<T> extends Thread {
	
	/** Time in MINUTES to expire locks */
	private static final int DEFAULT_TIMEOUT = 15;
	/** Time in SECONDS to sleep between runs;
	 * will not be an exact "interval timer" on most JVMs
	 */
	private final int timeOutMinutes;
	private static final int RUN_INTERVAL = 60;
	private final PessimisticLockManager<T> mgr;
	private int sleepSeconds = RUN_INTERVAL;
	private boolean done;
	
	/** Construct a Reaper with the default timeout */
	public LockReaperImpl(PessimisticLockManager<T> mgr) {
		this(mgr, DEFAULT_TIMEOUT);
	}
	
	/** Construct a Reaper with the given number of
	 * minutes' timeout
	 * @param i Minutes to timeout
	 */
	public LockReaperImpl(PessimisticLockManager<T> mgr, int i) {
		this.timeOutMinutes = i;
		this.mgr = mgr;
	}
	
	/**
	 * Does the work of reaping expired locks.
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		while (!done) {
			Map<Lock,T> map = ((PessimisticLockManagerImpl)mgr).getLockStore();
			System.out.println("Locks currently held at " + new Date() + ":");
			for (Lock lock : map.keySet()) {
				LockImpl<T> l = (LockImpl) lock;
				System.out.println(l);
				final long now = System.currentTimeMillis();
				final long then = l.getCreationTime();
				if (now - then > timeOutMinutes * 60000) {
					System.out.println("Removing stale lock " + l);					
				}
				mgr.releaseLock(l);
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
