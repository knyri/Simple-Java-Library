/**
 *
 */
package simple.time;

import java.util.HashMap;

/**
 * Simple class to maintain several timers at once.
 * <br>Created: Oct 1, 2009
 * @author Kenneth Pierce
 */
public class TimerGroup {
	private final HashMap<String, Timer> timers = new HashMap<String, Timer>();
	private final HashMap<String, Long> lap = new HashMap<String, Long>();

	public TimerGroup() {};
	public Timer addTimer(String name) {
		return timers.put(name, new Timer());
	}
	public void resetTimer(String name) {
		timers.get(name).reset();
	}
	public long getElapsed(String name) {
		return timers.get(name).elapsed();
	}
	/**
	 * Stores the current elapsed time for later.
	 * @param name Name of the timer
	 * @return The value from the last call or 0 if it was the first.
	 */
	public long lap(String name) {
		if (lap.get(name)==null) {
			lap.put(name, timers.get(name).elapsed());
			return 0;
		}
		return lap.put(name, timers.get(name).elapsed());
	}
	/**
	 * Stores the current elapsed time for later.
	 * @param name Name of the timer
	 * @param reset
	 * @return The value from the last call or null if it was the first.
	 */
	public long lap(String name, boolean reset) {
		long value = lap(name);
		if (reset) resetTimer(name);
		return value;
	}
	public Timer removeTimer(String name) {
		return timers.remove(name);
	}
}
