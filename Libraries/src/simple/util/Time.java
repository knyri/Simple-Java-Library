/**
 * 
 */
package simple.util;

/**Time conversions.
 * <br>Created: Mar 30, 2010
 * @author Kenneth Pierce
 */
public final class Time {
	private static final int
		MILLISECONDS_DAY = 86400000,
		MILLISECONDS_HOUR = 3600000,
		MILLISECONDS_MINUTE = 60000,
		MILLISECONDS_SECOND = 1000,
		MINUTES_DAY = 1440,
		MINUTES_HOUR = 60,
		SECONDS_DAY = 86400,
		SECONDS_HOUR = 3600,
		SECONDS_MINUTE = 60,
		HOURS_DAY = 24;
	public static final long millisecondsFromDays(int days) {
		return days*MILLISECONDS_DAY;
	}
	public static final long millisecondsFromHours(int hours) {
		return hours*MILLISECONDS_HOUR;
	}
	public static final long millisecondsFromMinutes(int minutes) {
		return minutes*MILLISECONDS_MINUTE;
	}
	public static final long millisecondsFromSeconds(int seconds) {
		return seconds*MILLISECONDS_SECOND;
	}
	public static final long secondsFromDays(int days) {
		return days*SECONDS_DAY;
	}
	public static final long secondsFromHours(int hours) {
		return hours*SECONDS_HOUR;
	}
	public static final long secondsFromMinutes(int minutes) {
		return minutes*SECONDS_MINUTE;
	}
	public static final long minutesFromDays(int days) {
		return days*MINUTES_DAY;
	}
	public static final long minutesFromHours(int hours) {
		return hours*MINUTES_HOUR;
	}
	public static final long hoursFromDays(int days) {
		return days*HOURS_DAY;
	}
	public static final long daysFromMilliseconds(long milliseconds) {
		return milliseconds/MILLISECONDS_DAY;
	}
	public static final long daysFromSeconds(long seconds) {
		return seconds/SECONDS_DAY;
	}
	public static final long daysFromMinutes(long minutes) {
		return minutes/MINUTES_DAY;
	}
	public static final long daysFromHours(long hours) {
		return hours/HOURS_DAY;
	}
	public static final long hoursFromMilliseconds(long milliseconds) {
		return milliseconds/MILLISECONDS_HOUR;
	}
	public static final long hoursFromSeconds(long seconds) {
		return seconds/SECONDS_HOUR;
	}
	public static final long hoursFromMinutes(long minutes) {
		return minutes/MINUTES_HOUR;
	}
	public static final long minutesFromMilliseconds(long milliseconds) {
		return milliseconds/MILLISECONDS_MINUTE;
	}
	public static final long minutesFromSeconds(long seconds) {
		return seconds/SECONDS_MINUTE;
	}
	public static final long secondsFromMilliseconds(long milliseconds) {
		return milliseconds/MILLISECONDS_SECOND;
	}
	/**Formats a time string.
	 *  d: days
	 *  h: hour in am/pm format (1-12)
	 *  H: hour of day (0-23)
	 *  m: minute in hour (0-59)
	 *  s: second in minute (0-59)
	 *  z: millisecond in second (0-999)
	 *  t<h|m|s|z>: total. "tm" would give the total minutes
	 *  You can pad with 0's by repeating the letter.
	 *  For h, H, m, and s the max padding is 2.
	 *  For z it is 3.
	 *  For d, th, tm, ts, and tz it is unlimited.
	 * @param milliseconds
	 * @param format
	 * @return the formatted time
	 */
	public static final String formatTime(long milliseconds, String format) {
		long days = daysFromMilliseconds(milliseconds);
		long hours = hoursFromMilliseconds(milliseconds%MILLISECONDS_DAY);
		long hours2 = (hours>12)?hours-12:(hours==0)?12:hours;
		long minutes = minutesFromMilliseconds(milliseconds%MILLISECONDS_HOUR);
		long seconds = secondsFromMilliseconds(milliseconds%MILLISECONDS_MINUTE);
		long milli = milliseconds % 1000;
		long tmp;
		long pow;
		StringBuilder buf = new StringBuilder(20);
		for (int i = 0; i < format.length(); i++) {
			switch(format.charAt(i)) {
			case 'h':
				if (format.charAt(i+1)=='h') {
					i++;
					if (hours2 < 10)
						buf.append("0");
				}
				buf.append(hours2);
				break;
			case 'H':
				if (format.charAt(i+1)=='H') {
					i++;
					if (hours < 10)
						buf.append("0");
				}
				buf.append(hours);
				break;
			case 'm':
				if (format.charAt(i+1)=='m') {
					i++;
					if (minutes < 10)
						buf.append("0");
				}
				buf.append(minutes);
				break;
			case 's':
				if (format.charAt(i+1)=='s') {
					i++;
					if (seconds < 10)
						buf.append("0");
				}
				buf.append(seconds);
				break;
			case 'z':
				if (format.charAt(i+1)=='z') {
					i++;
					if (format.charAt(i+1)=='z') {
						i++;
						if (milli < 100)
							buf.append("0");
					}
					if (milli < 10)
						buf.append("0");
				}
				buf.append(milli);
				break;
			case 'd':
				pow = 10;
				while(format.charAt(i+1)=='d') {
					i++;
					if (days<pow)
						buf.append("0");
					pow *= 10;
				}
				buf.append(days);
				break;
			case 't':
				pow = 10;
				i++;
				if (format.charAt(i)=='h') {
					tmp = hoursFromMilliseconds(milliseconds);
					while(format.charAt(i+1)=='h') {
						i++;
						if (tmp<pow)
							buf.append("0");
						pow *= 10;
					}
					buf.append(tmp);
				} else if (format.charAt(i)=='m') {
					tmp = minutesFromMilliseconds(milliseconds);
					while(format.charAt(i+1)=='m') {
						i++;
						if (tmp<pow)
							buf.append("0");
						pow *= 10;
					}
					buf.append(tmp);
				} else if (format.charAt(i)=='s') {
					tmp = secondsFromMilliseconds(milliseconds);
					while(format.charAt(i+1)=='s') {
						i++;
						if (tmp<pow)
							buf.append("0");
						pow *= 10;
					}
					buf.append(tmp);
				} else if (format.charAt(i)=='z') {
					while(format.charAt(i+1)=='z') {
						i++;
						if (milliseconds<pow)
							buf.append("0");
						pow *= 10;
					}
					buf.append(milliseconds);
				} else {
					buf.append('t');
					buf.append(format.charAt(i));
				}
				break;
				default:
					buf.append(format.charAt(i));
			}
		}
		return buf.toString();
	}
	/**Not yet implemented
	 * @param text
	 * @param format
	 * @return 0
	 */
	public static long parse(String text, String format) {
		return 0;
	}
	public static String getTime(long milli) {
		long hours = hoursFromMilliseconds(milli%MILLISECONDS_DAY);
		long minutes = minutesFromMilliseconds(milli%MILLISECONDS_HOUR);
		long seconds = secondsFromMilliseconds(milli%MILLISECONDS_MINUTE);
		return hours+":"+minutes+":"+seconds;
	}
	/**Used to test
	 * @param arg
	 */
	public static void main(String[] arg) {
		System.out.println(formatTime(MILLISECONDS_DAY*6+MILLISECONDS_HOUR*15+MILLISECONDS_MINUTE+MILLISECONDS_SECOND+800, "dddd|thh,HH:mm:ss:zzz"));
		System.out.println(formatTime(MILLISECONDS_MINUTE*90, "thh:mm:ss,zzz"));
	}
}
