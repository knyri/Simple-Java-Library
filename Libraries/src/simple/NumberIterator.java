/**
 * 
 */
package simple;

import java.util.Iterator;

import simple.util.do_str;

/** Iterates over a range of numbers.<br>
 * [001..100] = 001,002,003,..,099<br>
 * [001...100] = 001,002,003,..,100<br>
 * [1..100] = 1,2,3,..,99<br>
 * [0..100,5] = 0,5,10,..,95<br>
 * [0...100,5] = 0,5,10,..,100<br>
 * Inspired by the Range class in Ruby.
 * <br>Created: Jul 9, 2010
 * @author Kenneth Pierce
 */
public final class NumberIterator implements Iterator<String> {
	private final int start, end, step, places;
	private final boolean inclusive;
	private int current;
	private final int total_count;
	/**
	 * @param start Number to start from
	 * @param end Number to end at
	 * @param step Number to increment by
	 * @param places Precision(left padded with 0)
	 * @param inclusive Include the last number?
	 */
	public NumberIterator(int start, int end, int step, int places, boolean inclusive) {
		this.start = start;
		this.end = end;
		this.step = step;
		this.places = places;
		this.inclusive = inclusive;
		current = start;
		total_count = (end-start-(inclusive?0:1))/step;
	}
	/** Creates an inclusive iterator.
	 * @param start Number to start from
	 * @param end Number to end at
	 * @param step Number to increment by
	 * @param places Precision(left padded with 0)
	 */
	public NumberIterator(int start, int end, int step, int places) {
		this(start, end, step, places, true);
	}
	/** Creates an inclusive iterator with no padding.
	 * @param start Number to start from
	 * @param end Number to end at
	 * @param step Number to increment by
	 */
	public NumberIterator(int start, int end, int step) {
		this(start, end, step, 0);
	}
	/**Create a number iterator based on a range.<br>
	 * [001..100] = 001,002,003,..,099<br>
	 * [001...100] = 001,002,003,..,100<br>
	 * [1..100] = 1,2,3,..,99<br>
	 * [0..100,5] = 0,5,10,..,95<br>
	 * [0...100,5] = 0,5,10,..,100
	 * @param format
	 */
	public NumberIterator(String format) {
		int start =0, end=0, step=0, places=0, index=0;
		if (format.charAt(0)=='[' || format.charAt(0)=='(') {
			format = format.substring(1, format.length()-1);
		}//trim the first and last character
		index = format.indexOf('.');//find first '.'
		if (format.charAt(0)=='0' || format.charAt(0)=='-' && format.charAt(1)=='0') {
			//assign the places if there is a leading 0(a 0 places value speeds padding if it's not needed)
			if (format.charAt(0)=='-') {
				places = index-1;
			} else {
				places = index;
			}
		}
		start = Integer.valueOf(format.substring(0, index));
		
		end = index;
		while(format.charAt(++index)=='.');
		if (index-end == 3)
			this.inclusive = true;
		else
			this.inclusive = false;

		end = format.indexOf(',', index);
		if (end < 0) {
			end = Integer.valueOf(format.substring(index, format.length()));
		} else {
			step = Integer.valueOf(format.substring(end+1, format.length()));
			end = Integer.valueOf(format.substring(index, end));
		}
		this.start = start;
		current = start;
		this.end = end;
		if (step != 0)
			this.step = step;
		else {
			if (start < end) //[1..2]
				this.step = 1;
			else//[2..1]
				this.step = -1;
		}
		this.places = places;
		total_count = (this.end-this.start-(inclusive?0:1))/this.step;
	}
	/** The total numbers in the range.
	 * @return total numbers in the samples
	 */
	public int size() {
		return total_count;
	}
	/** A string representation of this range. Can be used with {@linkplain #NumberIterator(String)}
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder ret = new StringBuilder(30);
		ret.append('[');
		if (start >= 0) {
			ret.append(do_str.padLeft(places,'0',Integer.toString(start)));
		} else {
			ret.append('-');
			ret.append(do_str.padLeft(places,'0',Integer.toString(-start)));
		}
		if (!inclusive)
			ret.append("..");
		else
			ret.append("...");
		if (end >= 0) {
			ret.append(do_str.padLeft(places,'0',Integer.toString(end)));
		} else {
			ret.append('-');
			ret.append(do_str.padLeft(places,'0',Integer.toString(-end)));
		}
		ret.append(',');
		ret.append(Integer.toString(step));
		ret.append(']');
		return ret.toString();
	}
	/**
	 * resets the iterator to the beginning
	 */
	public void reset() {
		current = start;
	}
	public boolean hasNext() {
		if (step > 0) {
			if (inclusive)
				return (current<=end);
			else
				return (current<end);
		} else {
			if (inclusive)
				return (current>=end);
			else
				return (current>end);
		}
	}
	public String next() {
		if (!hasNext()) return null;
		String ret;
		if (current >= 0)
			ret = do_str.padLeft(places,'0',Integer.toString(current));
		else
			ret = '-'+do_str.padLeft(places,'0',Integer.toString(-current));
		current += step;
		return ret;
	}

	/** not used.
	 */
	public void remove() {}

}
