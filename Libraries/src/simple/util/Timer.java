/**
 *
 */
package simple.util;

/**
 * <hr>
 * <br>Created: Sep 10, 2011
 * @author Kenneth Pierce
 */
public class Timer{
	private long t;

	public Timer() {
		reset();
	}
	/**
	 * Sets the start time to the current time.
	 */
	public void reset() {
		t = System.currentTimeMillis();
	}
	/**
	 * Subtracts the start time from the current time.
	 * @return The time elapsed since the start time in milliseconds.
	 */
	public long elapsed() {
		return System.currentTimeMillis() - t;
	}
	/**
	 * Displays the elapsed time on the standard system output stream.
	 * @param s String to display with elapsed time.
	 */
	public void print(final String s) {
		System.out.println(s + ": " + elapsed());
	}
	public String getTime() {
		final long totalSeconds	= elapsed()/1000,
			totalMinutes	= totalSeconds/60,
			totalHours		= totalMinutes/60;
		return (totalHours % 24)+":"+(totalMinutes % 60)+":"+(totalSeconds % 60);
	}
	public String getTimeLong() {
		final long totalSeconds	= elapsed()/1000,
			totalMinutes	= totalSeconds/60,
			totalHours		= totalMinutes/60;
		return (totalHours % 24)+" hours, "+(totalMinutes % 60)+" minutes, "+(totalSeconds % 60)+" seconds";
	}
	public String getTimePrecise() {
		final long totalMillis =	elapsed(),
			totalSeconds	= totalMillis/1000,
			totalMinutes	= totalSeconds/60,
			totalHours		= totalMinutes/60;
		return (totalHours % 24)+":"+(totalMinutes % 60)+":"+((totalSeconds % 60)+(totalMillis / 1000));
	}
	public String getTimeLongPrecise() {
		final long totalMillis =	elapsed(),
			totalSeconds	= totalMillis/1000,
			totalMinutes	= totalSeconds/60,
			totalHours		= totalMinutes/60;
		return (totalHours % 24)+" hours, "+(totalMinutes % 60)+" minutes, "
			+(totalSeconds % 60)+" seconds, "+(totalMillis % 1000)+" milliseconds";
	}
}
