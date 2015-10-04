package simple.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Vector;

/**Depends on do_sort
 * <br>Created: Jan 27, 2009
 * @author Kenneth Pierce
 */
public final class Utils {
	private final static Date time = new Date();
	protected Utils() {}
	public static String getTimeDate() {
		time.setTime(System.currentTimeMillis());
		return time.toString();
	}
	/**
	 * @return A new timer.
	 */
	public static Timer getTimer() {
		return new Timer();
	}
	/**
	 * Takes a long value and returns a byte array with its value.
	 * @param value the long number
	 * @return The resulting array.
	 */
	public static byte[] toByteArray(final long value) {
		final byte[] x = new byte[8];
		x[0] = (byte)value;
		x[1] = (byte)(value>>>8);
		x[2] = (byte)(value>>>16);
		x[3] = (byte)(value>>>24);
		x[4] = (byte)(value>>>32);
		x[5] = (byte)(value>>>40);
		x[6] = (byte)(value>>>48);
		x[7] = (byte)(value>>>56);
		return x;
	}
	/**
	 * Takes an error and returns a String representation of the stack trace.
	 * @param ex Error/Exception to get the stack trace from.
	 * @return String containing the stack trace.
	 */
	public static String getStackTrace(final Throwable ex) {
		String result = "";
		try(StringWriter sw = new StringWriter()){
			try(PrintWriter pw = new PrintWriter(sw)){
				ex.printStackTrace(pw);
			}
			result = sw.toString();
		} catch(final Exception e)  {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * Sorts a list alphabetically using each element's toString() function.
	 * @param <E> type of the list
	 * @param list vector to sort
	 * @return The sorted vector.
	 */
	public static <E> Vector<E> sort(final Vector<E> list) {
		E tmp = null;
		boolean change = true;
		while (change) {
			change = false;
			for (int i = 0;i<list.size()-1;i++) {
//System.out.println(list[i]+" "+list[i+1]+" = "+compare(list[i], list[i+1]));
				if (compare(list.get(i), list.get(i+1))<0) {
					tmp = list.get(i);
					list.set(i, list.get(i+1));
					list.set(i+1, tmp);
					change = true;
				}
			}
		}
		return list;
	}
	/**
	 * Determines order between two objects by using their toString() method.
	 * @param object1 compare object 1
	 * @param object2 compare object 2
	 * @return -1 if object2 comes before object1<br>
	 * 	1 if object1 comes before object2<br>
	 * 	0 if object1 and object2 are the same.
	 */
	public static int compare(final Object object1, final Object object2) {
		final String orig = object1.toString();
		final String comp = object2.toString();
		final int stop = Math.min(orig.length(), comp.length());
		int result = 0;
		for (int i = 0; i<stop; i++) {
			if (orig.charAt(i)!=comp.charAt(i)) {
				if (orig.charAt(i)>comp.charAt(i)) {
					result = -1;
				} else {
					result = 1;
				}
				break;
			}
		}
		if (result==0)
			if (orig.length()!=comp.length())
				if (orig.length()>comp.length())
					result = -1;
				else
					result = 1;
		return result;
	}
	/*
	public static String getTime(final long milliseconds) {
		if (milliseconds <= 0)
			return "0:0:0";
		final long totalSeconds	= milliseconds/1000,
			totalMinutes	= totalSeconds/60,
			totalHours		= totalMinutes/60;
		return (totalHours % 24)+":"+(totalMinutes % 60)+":"+(totalSeconds % 60);
	}
	public static String getTimeLong(final long milliseconds) {
		if (milliseconds <= 0)
			return "0 seconds";
		final StringBuffer buf = new StringBuffer(32);
		final long totalSeconds	= milliseconds/1000,
			totalMinutes	= totalSeconds/60,
			totalHours		= totalMinutes/60;
		if (totalHours%24 > 0)
			buf.append((totalHours % 24)+((totalHours%24 > 1)?" hours":" hour"));
		if (totalMinutes%60 > 0)
			buf.append(((totalHours%24 > 0)?", ":"")+(totalMinutes % 60)+((totalMinutes%60 > 1)?" minutes":" minute"));
		if (totalSeconds%60 > 0)
			buf.append(((totalMinutes%60 > 0)?", ":"")+(totalSeconds % 60)+((totalSeconds%60 > 1)?" seconds":" second"));
		return buf.toString();
	}
	public static String getTimePrecise(final long milliseconds) {
		if (milliseconds <= 0)
			return "0:0:0.0";
		final long totalSeconds	= milliseconds/1000,
			totalMinutes	= totalSeconds/60,
			totalHours		= totalMinutes/60;
		return (totalHours % 24)+":"+(totalMinutes % 60)+":"+((totalSeconds % 60)+(milliseconds / 1000.0));
	}
	public static String getTimeLongPrecise(final long milliseconds) {
		if (milliseconds <= 0)
			return "0 milliseconds";
		final StringBuffer buf = new StringBuffer(50);
		final long totalSeconds	= milliseconds/1000,
			totalMinutes	= totalSeconds/60,
			totalHours		= totalMinutes/60;
		if (totalHours%24 > 0)
			buf.append((totalHours % 24)+((totalHours%24 > 1)?" hours":" hour"));
		if (totalMinutes%60 > 0)
			buf.append(((totalHours%24 > 0)?", ":"")+(totalMinutes % 60)+((totalMinutes%60 > 1)?" minutes":" minute"));
		if (totalSeconds%60 > 0)
			buf.append(((totalMinutes%60 > 0)?", ":"")+(totalSeconds % 60)+((totalSeconds%60 > 1)?" seconds":" second"));
		if (milliseconds%1000 > 0)
			buf.append(((totalSeconds%60 > 0)?", ":"")+(milliseconds % 60)+((milliseconds%1000 > 1)?" milliseconds":" millisecond"));
		return buf.toString();
	}*/

	/*public static Vector<String> addToVector(Vector<String> vec, String[] add) {
		for (int i =0;i<add.length;i++) {
			vec.addElement(add[i]);
		}
		return vec;
	}//*/
	/** Removes the duplicates and returns a new array. NOTE: this WILL modify the original array(sorts it).
	 * Do not use if you need the original array unchanged.
	 * @param s list of strings
	 * @param ignorecase should case be ignored
	 * @param keeporder Set to true only if the array is unsorted and order is important. (decreases speed significantly)
	 * @return The new array of unique values. Will be the same length as original with trailing nulls.
	 */
	public static final String[] removeDuplicates(String[] s, final boolean ignorecase, final boolean keeporder) {
		final String[] res = new String[s.length];
		int index = 0;
		if (!keeporder) {
			do_sort.quickSort(s);
			if (ignorecase) {
				for (int i = 0; i < s.length-1; i++) {
					if (s[i].equalsIgnoreCase(s[i+1])) {
						continue;
					}
					res[index++] = s[i];
				}
				if (!s[s.length-1].equalsIgnoreCase(s[s.length-2])) {
					res[index++] = s[s.length-1];
				}
			} else {
				for (int i = 0; i < s.length-1; i++) {
					if (s[i].equals(s[i+1])) {
						continue;
					}
					res[index++] = s[i];
				}
				if (!s[s.length-1].equals(s[s.length-2])) {
					res[index++] = s[s.length-1];
				}
			}
		} else {//keep order
			if (ignorecase) {
				for (int i = 0; i < s.length; i++) {
					if (s[i]==null) {
						continue;
					}
					res[index++] = s[i];
					for (int j = i+1; j < s.length; j++) {
						if (s[i].equalsIgnoreCase(s[j])) {
							s[j] = null;
						}
					}
				}
			} else {
				for (int i = 0; i < s.length; i++) {
					if (s[i]==null) {
						continue;
					}
					res[index++] = s[i];
					for (int j = i+1; j < s.length; j++) {
						if (s[i].equals(s[j])) {
							s[j] = null;
						}
					}
				}
			}
		}
		s = new String[index];
		for (int i = 0; i < index && res[i]!=null; i++) {
			s[i] = res[i];
		}
		return s;
	}
}