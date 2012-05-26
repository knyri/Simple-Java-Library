/**
 * 
 */
package simple.time;

/**Simple timer.
 * <br>Created: Nov 9, 2010
 * @author Kenneth Pierce
 */
public class Timer {
	private long t;

	public Timer() {
		reset();
	}
	/**Sets the start time to the current time. */
	public void reset() {
		t = System.currentTimeMillis();
	}
	/**Subtracts the start time from the current time.
	 * @return The time elapsed since the start time in milliseconds.
	 */
	public long elapsed() {
		return System.currentTimeMillis() - t;
	}
	/**Displays the elapsed time on the standard system output stream.
	 * @param s String to display with elapsed time.
	 */
	public void print(String s) {
		System.out.println(s + ": " + elapsed());
	}
}
