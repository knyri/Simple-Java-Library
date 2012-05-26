/**
 * 
 */
package simple.time;

import simple.util.logging.Log;

/**
 * <br>Created: Nov 9, 2010
 * @author Kenneth Pierce
 */
public interface TimeRemainingEstimator {
	/**Gets the amount of time, in milliseconds, that remain. 
	 * @return the remaining time in milliseconds
	 */
	public long getRemaining();
	/**Gets the number of items per second.
	 * @return number of items per second. May be negative if the total number
	 * of samples exceeds the total number of items.
	 */
	public double getRate();
	/** Enters a time sample */
	public void sample();
	/**The number of samples used for estimation.
	 * @return The number of samples used for estimation
	 */
	public int getSampleCount();
	/**The total number of samples taken.
	 * @return The total number of samples taken.
	 */
	public int getTotalSampleCount();
	/**Gets the total number of items that will be sampled.
	 * @return The total number of items that will be sampled.
	 */
	public int getTotalItems();
	/**Sets the number of items to be sampled.<br>
	 * The number of times sample() will be called.
	 * @param total the total
	 */
	public void setTotalItems(int total);
	/** Resets the samples */
	public void reset();
	public void debug(Log log);
}
