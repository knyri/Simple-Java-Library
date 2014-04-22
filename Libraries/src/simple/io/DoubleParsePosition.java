/**
 *
 */
package simple.io;

//import simple.util.logging.Log;
//import simple.util.logging.LogFactory;

/**Object to hold start and end positions.<br>
 * Start and end positions can be marked and restored.<br>
 * Has several convenience methods.
 * <br>Created: Sep 12, 2010
 * @author Kenneth Pierce
 */
public final class DoubleParsePosition implements Cloneable {
	//private static final Log log = LogFactory.getLogFor(DoubleParsePosition.class);
	public int start = 0, end = 0;
	private int mStart = 0, mEnd = 0;
	private int lineCount = 0;
	/** marks the current start position. */
	public final void markStart() {
		mStart = start;
		//log.debug("marked start", start);
	}
	public final void setMarkStart(int i) {
		mStart = i;
		//log.debug("marked start", i);
	}
	public final int getMarkStart() {
		return mStart;
	}
	/** sets end to its saved position. */
	public final void toMarkEnd() {
		//log.debug("set end to mark",end+" to "+mEnd);
		end = mEnd;
	}
	/**Marks the end position */
	public final void markEnd() {
		mEnd = end;
		//log.debug("marked end", end);
	}
	public final void setMarkEnd(int i) {
		mEnd = i;
		//log.debug("set marked end", i);
	}
	public final int getMarkEnd() {
		return mEnd;
	}
	/**sets start to its previously marked position. */
	public final void toMarkStart() {
//		log.debug("set start to mark",start+" to "+mStart);
		start = mStart;
	}
	/**sets start and end to their marked positions. */
	public final void toMarkAll() {
		toMarkEnd();
		toMarkStart();
	}
	/** Increments the line count.
	 */
	public final void incLine() {
		lineCount++;
	}
	/** Increments the line count and resets start and end to zero.
	 */
	public final void newLine() {
		start = end = 0;
		lineCount++;
	}
	/** Sets the end marker to the start marker.
	 */
	public final void resetEnd() {
		end = start;
		//log.debug("end moved to start");
	}
	/** Sets the start marker to the end marker
	 */
	public final void resetStart() {
		start = end;
		//log.debug("start moved to end");
	}
	/** same as ++this.start
	 * @return the value of start after incrementing
	 */
	public final int incStart() {
		return ++start;
	}
	/** same as --this.start
	 * @return the value of start after decrementing
	 */
	public final int decStart() {
		return --start;
	}
	/** same as this.start++
	 * @return Value of start before incrementing
	 */
	public final int postIncStart() {
		int tmp = start++;
		return tmp;
	}
	/** same as ++this.end
	 * @return the value of end after incrementing
	 */
	public final int incEnd() {
		return ++end;
	}
	/** same as --this.end
	 * @return the value of end after decrementing
	 */
	public final int decEnd() {
		return --end;
	}
	/** same as this.end++
	 * @return the value of end before incrementing
	 */
	public final int postIncEnd() {
		int tmp = end++;
		return tmp;
	}
	/**
	 * Resets start, end, and lineCount to zero.
	 * markA and markB are left as is.
	 */
	public final void reset() {
		start = end = lineCount = 0;
	}
	public final void setStart(int s) {
		start = s;
	}
	public final void setEnd(int e) {
		end = e;
	}
	public final int getStart() {
		return start;
	}
	public final int getEnd() {
		return end;
	}
	public final int getLine() {
		return lineCount;
	}
	/** Checks if start is greater than end.
	 * @return true if <var>start</var> &gt; <var>end</var>
	 */
	public final boolean isNegativeDiff() {
		return (start > end);
	}
	/** Checks if start is less than end.
	 * @return true if <var>start</var> &lt; <var>end</var>
	 */
	public final boolean isPositiveDiff() {
		return (start < end);
	}
	/** Checks if start equals end.
	 * @return true if start==end
	 */
	public final boolean isEqual() {
		return start==end;
	}
	/** Tests to see if <code>start &gt; -1</code>.
	 * @return <code>true</code> if <code>start</code> is greater than -1.
	 */
	public final boolean validStart() {
		return start>-1;
	}
	/** Tests to see if <code>end &gt; -1</code>.
	 * @return <code>true</code> if <code>end</code> is greater than -1.
	 */
	public final boolean validEnd() {
		return end>-1;
	}
	@Override
	public String toString() {
		return lineCount+"["+start+","+end+"]("+mStart+","+mEnd+")";
	}
	/** Copies this object's field values to the target.
	 * @param target Object to be copied to.
	 */
	public void copyInto(DoubleParsePosition target) {
		target.start = start;
		target.end = end;
		target.lineCount = lineCount;
		target.mEnd = mEnd;
		target.mStart = mStart;
	}
	@Override
	public DoubleParsePosition clone() {
		DoubleParsePosition tmp = new DoubleParsePosition();
		copyInto(tmp);
		return tmp;

	}
}
