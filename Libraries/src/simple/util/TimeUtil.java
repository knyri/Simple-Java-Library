/**
 *
 */
package simple.util;

import java.util.Calendar;

/**
 * <hr>
 * <br>Created: Sep 10, 2011
 * @author Kenneth Pierce
 */
public final class TimeUtil{
	private static Calendar cal = Calendar.getInstance();
	private static long calLife = System.currentTimeMillis();
	/** Formats the time h:m:s. where h is 0-23.
	 * @param milliseconds
	 * @return the formatted time
	 */
	public static final String getTime(final long milliseconds) {
		final long second	= milliseconds/1000,
				minute		= second/60,
				hour		= minute/60;
		return ((hour<10?"0":"")+hour+":"+(minute<10?"0":"")+minute+":"+(second<10?"0":"")+second);
	}
	public static final String getTime() {
		if ((System.currentTimeMillis()-TimeUtil.calLife)>1000) {
			TimeUtil.cal=Calendar.getInstance();
		}
		return String.format("%1$tH:%1$tM:%1$tS",cal);
	}
	/** Formats the time "h hour(s), m minute(s), s second(s)".
	 * If h or m is 0 then that section is ommitted.
	 * @param milliseconds
	 * @return the formatted time
	 */
	public static final String getTimeLong(final long milliseconds) {
		if (milliseconds <= 0)
			return "0 seconds";
		final StringBuffer buf = new StringBuffer(32);
		final long totalSeconds	= milliseconds/1000,
		totalMinutes	= totalSeconds/60,
		totalHours		= totalMinutes/60;
		if (totalHours%24 > 0) {
			buf.append((totalHours % 24)+((totalHours%24 > 1)?" hours":" hour"));
		}
		if (totalMinutes%60 > 0) {
			buf.append(((totalHours%24 > 0)?", ":"")+(totalMinutes % 60)+((totalMinutes%60 > 1)?" minutes":" minute"));
		}
		if (totalSeconds%60 > 0) {
			buf.append(((totalMinutes%60 > 0)?", ":"")+(totalSeconds % 60)+((totalSeconds%60 > 1)?" seconds":" second"));
		}
		return buf.toString();
	}
	/** Formats the time "h:m:s.ms".
	 * h is 0-24.
	 * @param milliseconds
	 * @return the formatted time
	 */
	public static final String getTimePrecise(final long milliseconds) {
		if (milliseconds <= 0)
			return "0:0:0.0";
		final long totalSeconds	= milliseconds/1000,
		totalMinutes	= totalSeconds/60,
		totalHours		= totalMinutes/60;
		return (totalHours % 24)+":"+(totalMinutes % 60)+":"+((totalSeconds % 60)+(milliseconds / 1000.0));
	}
	/** Formats the time "h hour(s), m minute(s), s second(s), ms millisecond(s)".
	 * If h, m, or s are 0 then that section is ommitted.
	 * @param milliseconds
	 * @return the formatted time
	 */
	public static final String getTimeLongPrecise(final long milliseconds) {
		if (milliseconds <= 0)
			return "0 milleseconds";
		final StringBuffer buf = new StringBuffer(50);
		final long totalSeconds	= milliseconds/1000,
		totalMinutes	= totalSeconds/60,
		totalHours		= totalMinutes/60;
		if (totalHours%24 > 0) {
			buf.append((totalHours % 24)+((totalHours%24 > 1)?" hours":" hour"));
		}
		if (totalMinutes%60 > 0) {
			buf.append(((totalHours%24 > 0)?", ":"")+(totalMinutes % 60)+((totalMinutes%60 > 1)?" minutes":" minute"));
		}
		if (totalSeconds%60 > 0) {
			buf.append(((totalMinutes%60 > 0)?", ":"")+(totalSeconds % 60)+((totalSeconds%60 > 1)?" seconds":" second"));
		}
		if (milliseconds%1000 > 0) {
			buf.append(((totalSeconds%60 > 0)?", ":"")+(milliseconds % 60)+((milliseconds%1000 > 1)?" milliseconds":" millisecond"));
		}
		return buf.toString();
	}
}
