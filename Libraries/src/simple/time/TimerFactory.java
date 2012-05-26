/**
 * 
 */
package simple.time;

import simple.util.Time;
import simple.util.logging.Log;

/**Creates various timers.
 * <br>Created: Nov 9, 2010
 * @author Kenneth Pierce
 */
public final class TimerFactory {
	public static enum Algorithm {SAMPLE, TOTALAVG};
	/**Gets a timer with the specified algorithm. sampleSize does not apply to
	 * TOTALAVG.
	 * @param algorithm
	 * @param sampleSize
	 * @return A timer that uses the specified algorithm.
	 */
	public static TimeRemainingEstimator getTRETimer(Algorithm algorithm, final int sampleSize) {
		switch (algorithm) {
		case SAMPLE:
			return new TimeRemainingEstimator () {
				private final long[] samples = new long[sampleSize];
				private int /*start = 0, end = 0, */count = 0;
				private int totalItems = 0;
				{
					for(int i = 0; i < sampleSize; i++) {
						samples[i] = System.currentTimeMillis();
					}
				}
				public long getRemaining() { return Math.max((totalItems-count)*(long)getRate(),0); }
				public double getRate() {
					if (count==0) return 0;
					
					if (count < sampleSize)
						return (samples[count%sampleSize]-samples[(count+1)%sampleSize])/count;
					else
						return (samples[count%sampleSize]-samples[(count+1)%sampleSize])/sampleSize;
				}
				public void sample() {
					count++;
					samples[count%sampleSize] = System.currentTimeMillis();
					/*end++;
					if (end%samples.length==0)
						end = 0;
					if (end==start)
						start++;
					if (start%samples.length==0)
						start = 0;
					count++;
					samples[end] = System.currentTimeMillis();*/
				}
				public void setTotalItems(int total) { totalItems = total; }
				public void reset() {
					samples[0] = System.currentTimeMillis();
					/*start = end = */count = 0;
				}
				public int getSampleCount() {
					return sampleSize;
				}
				public int getTotalSampleCount() {
					return count;
				}
				public int getTotalItems() {
					return totalItems;
				}
				public void debug(Log log) {
					log.debug("index", count%sampleSize);
					log.debug("sample size", sampleSize);
					log.debug("total samples", count);
					log.debug("sample difference", samples[count%sampleSize]-samples[(count+1)%sampleSize]);
					log.debug("total items", totalItems);
					log.debug("rate", getRate());
					log.debug("remaining time", getRemaining());
				}
			};
		case TOTALAVG:
			return new TimeRemainingEstimator() {
				int totalItems = 0,
					count = 0;
				long start = System.currentTimeMillis();
				public long getRemaining() {
					return (long)(getRate()*(totalItems-count));
				}
				public double getRate() {
					return count/(System.currentTimeMillis()-start);
				}
				public void sample() { count++; }
				public void setTotalItems(int total) { totalItems = total; }
				public void reset() {
					start = System.currentTimeMillis();
					count = 0;
				}
				public int getSampleCount() {
					return count;
				}
				public int getTotalSampleCount() {
					return count;
				}
				public int getTotalItems() {
					return totalItems;
				}
				public void debug(Log log) {
				}
			};
		}
		return null;
	}
	/**Formats the time to H:m:s
	 * @param milli
	 * @return the formatted time
	 */
	public static String getTime(long milli) {
		return Time.getTime(milli);
	}
	/**Formats the time to "H hours, m minutes, s seconds"
	 * @param milli
	 * @return the formatted time
	 */
	public static String getTimeLong(long milli) {
		long totalSeconds	= milli/1000,
			totalMinutes	= totalSeconds/60,
			totalHours		= totalMinutes/60;
		return (totalHours % 24)+" hours, "+(totalMinutes % 60)+" minutes, "+(totalSeconds % 60)+" seconds";
	}
	/**Formats the time to H:m:s.M
	 * @param milli
	 * @return the formatted time
	 */
	public static String getTimePrecise(long milli) {
		long totalSeconds	= milli/1000,
			totalMinutes	= totalSeconds/60,
			totalHours		= totalMinutes/60;
		return (totalHours % 24)+":"+(totalMinutes % 60)+":"+((totalSeconds % 60)+"."+(milli % 1000));
	}
	/**Formats the time to "H hours, m minutes, s seconds, M milliseconds"
	 * @param milli
	 * @return the formatted time
	 */
	public static String getTimeLongPrecise(long milli) {
		long totalSeconds	= milli/1000,
			totalMinutes	= totalSeconds/60,
			totalHours		= totalMinutes/60;
		return (totalHours % 24)+" hours, "+(totalMinutes % 60)+" minutes, "
			+(totalSeconds % 60)+" seconds, "+(milli % 1000)+" milliseconds";
	}
	public static void main(String[] arg){
		System.out.println(getTime(0));
		System.out.println(getTime(15000));
		System.out.println(getTime(60*60*1000));
	}
}
