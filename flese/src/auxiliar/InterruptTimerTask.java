package auxiliar;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class InterruptTimerTask extends TimerTask {

	public static final class KCtes {
		public static final long normalWaitingTimeInMs = 5 * 60 * 1000l; // 5 mins.
	}

	final static private Log LOG = LogFactory.getLog(InterruptTimerTask.class);

	/*
	 * A TimerTask that interrupts the specified thread when run.
	 * http://stackoverflow.com/questions/5243233/set-running-time-limit-on-a-
	 * method-in-java
	 */

	private static HashMap<Long, InterruptTimerTask> interruptedTimerTasksByThread;

	private Thread theThread;
	long threadId;
	Timer timer;
	boolean inBadState;

	private static synchronized void createHashMap() {
		interruptedTimerTasksByThread = new HashMap<Long, InterruptTimerTask>();
	}

	private static InterruptTimerTask getFromHash(long id) {
		if (interruptedTimerTasksByThread == null) {
			createHashMap();
		}
		return interruptedTimerTasksByThread.get(id);
	}

	private static void putInHash(long id, InterruptTimerTask interruptTimerTask) {
		if (interruptedTimerTasksByThread == null) {
			createHashMap();
		}
		synchronized (interruptedTimerTasksByThread) {
			interruptedTimerTasksByThread.put(id, interruptTimerTask);
		}
	}

	public static InterruptTimerTask getInstance(Thread theThread) {
		long id = theThread.getId();
		InterruptTimerTask interruptTimerTask = getFromHash(id);

		if (interruptTimerTask == null) {
			interruptTimerTask = new InterruptTimerTask(theThread);
			putInHash(id, interruptTimerTask);
		}
		return interruptTimerTask;
	}

	private InterruptTimerTask(Thread theThread) {
		this.theThread = theThread;
		this.threadId = theThread.getId();
		this.timer = null;
		this.inBadState = false;

		LOG.debug("New InterruptTimerTask for thread with id " + threadId);
	}

	public void deactivate() {
		// Reset current timer.
		if (timer != null)
			this.timer.cancel();

		this.cancel();
		this.inBadState = true;

		putInHash(threadId, null);
		LOG.debug("Cancelled interruption of thread " + threadId);
	}

	public void reschedule(long nextExecInMs) {
		if (nextExecInMs <= 0) {
			nextExecInMs = KCtes.normalWaitingTimeInMs;
		}

		if (!inBadState) {
			this.timer = new Timer(false);
			this.timer.schedule(this, nextExecInMs);
			this.inBadState = true;
			LOG.debug("Interrupting thread " + threadId + " execution in " + nextExecInMs + "ms.");
		} else {
			LOG.debug("Reschedule of interrupting thread " + threadId + " needs recreation ... ");
			this.deactivate();
			InterruptTimerTask.getInstance(this.theThread).reschedule(nextExecInMs);
		}
	}

	@Override
	public void run() {
		LOG.debug("Interrupting execution of thread " + threadId);
		this.theThread.interrupt();
	}

}
