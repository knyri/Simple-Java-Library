package simple.util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import simple.io.DoubleParsePosition;

public final class StrUtil{
	/**
	 * Splits a string
	 *
	 * @param str
	 * @param on
	 * @return
	 */
	public static List<String> split(String str, char on){
		List<String> parts= new LinkedList<String>();
		int i= 0, end= str.length(), last= 0;
		for(; i < end; i++){
			if(str.charAt(i) == on){
				if(i == last){
					parts.add("");
				}else{
					parts.add(str.substring(last, i));
				}
				last= i + 1;
			}
		}
		if(last < end){
			parts.add(str.substring(last));
		}
		return parts;
	}
	public static final String
		UNIX_LINE_ENDING = "\n",
		LINUX_LINE_ENDING = UNIX_LINE_ENDING,
		MAC_LINE_ENDING = UNIX_LINE_ENDING,
		WINDOWS_LINE_ENDING = "\r\n";
	private StrUtil() {}
	/**
	 * Returns the indexOf if it does not equal -1 or the default value if it does.
	 * This is for those situations where a -1 result is not optimal.
	 * @param indexOf The result of an indexOf(..) or anything that returns -1 for failure.
	 * @param value The default value
	 * @return The indexOf result if not -1 or the default value.
	 */
	public static final int defaultValue(final int indexOf, final int value) {
		if(indexOf == -1){
			return value;
		}
		return indexOf;
	}
	public static final CharSequence substring(final CharSequence str, final DoubleParsePosition pos) {
		return str.subSequence(pos.start, pos.end);
	}
	public static final String substring(final String str, final DoubleParsePosition pos) {
		return str.substring(pos.start, pos.end);
	}
	/**Tests to see if the character is one of the characters in the list.
	 * @param c The character in question
	 * @param list List of wanted characters
	 * @return true if the character is in the list.
	 */
	public static final boolean equals(final char c, final CharSequence list) {
		return indexOf(list, c, 0) != -1;
	}
	/** Returns the index of the first occurrence such that startsWith(s,f,offset) is true.
	 * @param haystack The string to search
	 * @param needle The string to find
	 * @param offset The starting index
	 * @return The index f is found or -1
	 */
	public static final int indexOf(final CharSequence haystack, final CharSequence needle, int offset) {
		if(needle.length() > haystack.length()){
			return -1;
		}
		final int max= haystack.length() - needle.length();
		for (; offset <= max; offset++){
			if(startsWith(haystack, needle, offset)){
				break;
			}
		}
		if(offset > max){
			return -1;
		}
		return offset;
	}
	/** Returns the index of the first occurrence such that startsWith(s,f,offset) is true.
	 * @param haystack The string to search
	 * @param needle The string to find
	 * @param offset The starting index
	 * @param limit Index to stop searching at(inclusive)
	 * @return The index f is found or -1
	 * @throws StringIndexOutOfBoundsException
	 */
	public static final int indexOf(final CharSequence haystack, final CharSequence needle, int offset, final int limit) {
		checkBounds(haystack, offset, limit);

		final int max= limit - needle.length();
		if (max == 0){
			return -1;
		}
		for(; offset < max; offset++)
			if(startsWith(haystack, needle, offset)){
				break;
			}
		if(offset == max){
			return -1;
		}
		return offset;
	}
	/**Exists only to add indexOf(char,int) to classes like StringBuilder.
	 * @param haystack The string to search
	 * @param needle The string to find
	 * @param offset Index to start from
	 * @return The index of needle or -1.
	 */
	public static final int indexOf(final CharSequence haystack, final char needle, final int offset) {
		return indexOf(haystack,needle,offset,haystack.length()-1);
	}
	/**indexOf(..) with a twist. Will only search to the limit specified.
	 * @param haystack The string to search
	 * @param needle The string to find
	 * @param offset Index to start from
	 * @param limit Index to stop at(inclusive)
	 * @return The index of needle or -1.
	 */
	public static final int indexOf(final CharSequence haystack, final char needle, int offset, final int limit) {
		if(offset > limit || offset < 0){
			return -1;
		}
		for (; offset <= limit; offset++) {
			if(haystack.charAt(offset) == needle){
				return offset;
			}
		}
		return -1;
	}
	/** Returns the index after the first occurrence such that startsWith(s,f,offset) is true.
	 * @param haystack The string to search
	 * @param needle The string to find
	 * @param offset The starting index
	 * @return The index f is found plus the length of f or -1
	 */
	public static final int indexAfter(final CharSequence haystack, final CharSequence needle, int offset) {
		final int max = haystack.length()-needle.length();
		for (;offset<=max; offset++)
			if (startsWith(haystack,needle,offset)) {
				break;
			}
		if (offset>max) return -1;
		return offset+needle.length();
	}
	/** Returns the index after the first occurrence such that startsWith(s,f,offset) is true.
	 * @param haystack The string to search
	 * @param needle The string to find
	 * @param offset The starting index
	 * @param limit Index to stop searching at(inclusive)
	 * @return The index f is found plus the length of f or -1
	 */
	public static final int indexAfter(final CharSequence haystack, final CharSequence needle, int offset, final int limit) {
		checkBounds(haystack, offset, limit);

		final int max = limit - needle.length();
		if(max == 0){
			return -1;
		}
		for(; offset < max; offset++){
			if(startsWith(haystack, needle, offset)){
				break;
			}
		}
		if(offset == max){
			return -1;
		}
		return offset + needle.length();
	}
	/**
	 * Throws an error if the start or end are out of bounds
	 *
	 * @param string
	 * @param start
	 * @param end
	 * @throws StringIndexOutOfBoundsException
	 */
	public static void checkBounds(final CharSequence string, final int start, final int end) throws StringIndexOutOfBoundsException{
		if(end < start){
			throw new StringIndexOutOfBoundsException("End["+end+"] must be greater than or equal to the Start["+start+"]");
		}
		if(start > string.length()){
			throw new StringIndexOutOfBoundsException("Start["+start+"] must be less than or equal to the string length["+string.length()+"]");
		}
		if(end > string.length()){
			throw new StringIndexOutOfBoundsException("End["+end+"] must be less than or equal to the string length["+string.length()+"]");
		}
		if(start < 0){
			throw new StringIndexOutOfBoundsException("The end["+start+"] is negative.");
		}
	}
	/**
	 * Throws an error if the start or end are out of bounds. Assumes this will be used for {@link CharSequence#subSequence(int, int)} where the end index is exclusive
	 *
	 * @param string
	 * @param start
	 * @param end
	 * @throws StringIndexOutOfBoundsException
	 */
	public static void checkBounds(final CharSequence string, DoubleParsePosition limits) throws StringIndexOutOfBoundsException{
		if(limits.isNegativeDiff()){
			throw new StringIndexOutOfBoundsException("End["+limits.end+"] must be greater than or equal to the start["+limits.start+"]");
		}
		if(limits.start > string.length()){
			throw new StringIndexOutOfBoundsException("Start["+limits.start+"] must be less than or equal to the string length["+string.length()+"]");
		}
		if(limits.end > string.length()){
			throw new StringIndexOutOfBoundsException("End["+limits.end+"] must be less than or equal to the string length["+string.length()+"]");
		}
		if(limits.start < 0){
			throw new StringIndexOutOfBoundsException("The start["+limits.start+"] is negative.");
		}

	}
	/**
	 * Throws an error if the offset or offset + length are out of bounds
	 *
	 * @param string
	 * @param offset
	 * @param length
	 * @throws StringIndexOutOfBoundsException
	 */
	public static void checkOffsetLength(final CharSequence string, final int offset, final int length) throws StringIndexOutOfBoundsException{
		checkBounds(string, offset, offset + length);
	}
	/**
	 * Throws an error if the start or end are out of bounds
	 *
	 * @param string
	 * @param start
	 * @param end
	 * @throws StringIndexOutOfBoundsException
	 */
	public static DoubleParsePosition adjustBounds(final CharSequence string, final int start, final int end){
		DoubleParsePosition ret= new DoubleParsePosition();
		ret.start= start;
		ret.end= end;
		if(ret.end < 0){
			ret.end= string.length() + ret.end;
		}
		if(ret.start < 0){
			ret.start= string.length() + ret.start;
		}
		if(ret.isNegativeDiff()){
			ret.end= start;
			ret.start= end;
		}
		if(ret.start >= string.length()){
			ret.start= string.length() - 1;
		}
		if(ret.end >= string.length()){
			ret.end= string.length() - 1;
		}
		return ret;
	}
	/** Returns the index of the first occurrence of any character in <code>list</code>.
	 * @param haystack String to search
	 * @param needles List of characters to search for
	 * @param start Index to start searching
	 * @param end Index to stop searching(inclusive)
	 * @return The index where one of the listed characters were found or -1.
	 */
	public static final int indexOfAny(final CharSequence haystack, final CharSequence needles, int start, final int end) {
		checkBounds(haystack, start, end);

		int j, needleCount= needles.length();

		for (;start <= end; start++) {
			for (j= 0; j < needleCount; j++){
				if(haystack.charAt(start) == needles.charAt(j)){
					return start;
				}
			}
		}
		return -1;
	}
	/** Returns the index of the first occurrence of any character in <code>list</code>.
	 * @param haystack String to search
	 * @param needles List of characters to search for
	 * @param start Index to start searching
	 * @return The index where one of the listed characters were found or -1.
	 */
	public static final int indexOfAny(final CharSequence haystack, final CharSequence needles, final int start) {
		return indexOfAny(haystack, needles, start, haystack.length()-1);
	}
	/** Returns the index of the first occurrence of any character in <code>list</code>.
	 * @param haystack String to search
	 * @param needles List of characters to search for
	 * @return The index where one of the listed characters were found or -1.
	 */
	public static final int indexOfAny(final CharSequence haystack, final CharSequence needles) {
		return indexOfAny(haystack, needles, 0, haystack.length()-1);
	}
	/** Returns the index of the first occurrence where the character in
	 * <code>s</code> at the index is not found at any index in <code>list</code>.
	 * @param haystack The source.
	 * @param list The characters to pass over.
	 * @param start Index to start at.
	 * @param end Index to stop at(inclusive).
	 * @return Index of the unlisted character.
	 */
	public static final int indexOfMissing(final CharSequence haystack, final CharSequence list, int start, final int end) {
		checkBounds(haystack, start, end);
		int j = 0;
		for (;start<=end;start++) {
			for (j = 0; j < list.length(); j++)
				if (haystack.charAt(start)==list.charAt(j)) {
					break;
				}
			if (j==list.length()) return start;//character was not found in the list
		}
		return -1;
	}
	/**Convenience method for {@link #indexOfMissing(CharSequence, CharSequence, int, int)}
	 * @param s
	 * @param list
	 * @param offset
	 * @return Index of a character not in the list or -1 if none were found
	 * @see #indexOfMissing(CharSequence, CharSequence, int, int)
	 */
	public static final int indexOfMissing(final CharSequence s, final CharSequence list, final int offset) {
		return indexOfMissing(s, list, offset, s.length()-1);
	}
	/**Convenience method for {@link #indexOfMissing(CharSequence, CharSequence, int, int)}
	 * @param s
	 * @param list
	 * @return  Index of a character not in the list or -1 if none were found
	 * @see #indexOfMissing(CharSequence, CharSequence, int, int)
	 */
	public static final int indexOfMissing(final CharSequence s, final CharSequence list) {
		return indexOfMissing(s, list, 0, s.length()-1);
	}
	/**Exists only to add lastIndexOf(char,int) to classes like StringBuilder.
	 * @param haystack The haystack
	 * @param needle The needle
	 * @param start Index to start from
	 * @param end Index to stop at(inclusive; must be less than offset)
	 * @return The index of <code>f</code> or -1.
	 */
	public static final int lastIndexOf(final CharSequence haystack, final char needle, int start, final int end) {
		checkBounds(haystack, start, end);
		for (;start >= end; start--) {
			if (haystack.charAt(start) == needle){
				return start;
			}
		}
		return -1;
	}
	public static final int lastIndexOf(final CharSequence haystack, final CharSequence needle, int start, final int end) {
		checkBounds(haystack, start, end);
		for (;start >= end; start--) {
			if (startsWith(haystack, needle, start)){
				return start;
			}
		}
		return -1;
	}
	public static final int lastIndexOf(final CharSequence haystack, final String needle) {
		return lastIndexOf(haystack, needle, haystack.length()-1, 0);
	}
	/**Exists only to add lastIndexOf(char,int) to classes like StringBuilder.
	 * @param haystack String to search
	 * @param needle String to find
	 * @param offset Index to start from
	 * @return The index of <code>f</code> or -1.
	 */
	public static final int lastIndexOf(final CharSequence haystack, final char needle, final int offset) {
		return lastIndexOf(haystack, needle, offset, 0);
	}
	/**Adds startsWith() to StringBuilder. Short for {@link #startsWith(CharSequence, CharSequence, int)}.
	 * @param haystack
	 * @param needle
	 * @return true if haystack starts with needle
	 * @see #startsWith(CharSequence, CharSequence, int)
	 */
	public static final boolean startsWith(final CharSequence haystack, final CharSequence needle) {
		return startsWith(haystack, needle, 0);
	}
	public static final boolean startsWith(final CharSequence haystack, final CharSequence needle, final int offset) {
		if((needle.length() + offset) > haystack.length()){
			return false;
		}
		for(int i= 0; i < needle.length(); i++) {
			if(haystack.charAt(offset+i) != needle.charAt(i)){
				return false;
			}
		}
		return true;
	}
	/**Adds endsWith() to StringBuilder
	 * @param haystack String to search
	 * @param needle String to find
	 * @return <code>true</code> if the string ends with <code>f</code>
	 */
	public static final boolean endsWith(final CharSequence haystack, final CharSequence needle) {
		return startsWith(haystack, needle, haystack.length() - needle.length());
	}
	/**Adds a special endsWith(). Tests to see if the characters before the offset match.
	 * @param haystack String to search
	 * @param needle String to find
	 * @param offset Index to start
	 * @return <code>true</code> if the string ends with <code>f</code>.
	 */
	public static final boolean endsWith(final CharSequence haystack, final CharSequence needle, final int offset) {
		return startsWith(haystack, needle, offset - needle.length());
	}
	/**
	 * Skips anything that makes Character.isWhitespace return true.
	 * @param s
	 * @param offset Place to start
	 * @return Index where the whitespace ends.
	 */
	public static final int skipWhitespace(final CharSequence s, int offset) {
		int end= s.length();
		while(offset < end && Character.isWhitespace(s.charAt(offset))){
			offset++;
		}
		return offset;
	}
	/** Ignores <code>length</code> characters or until <code>until</code> is found.
	 * @param s
	 * @param offset
	 * @param length Number of characters to skip.
	 * @param until Character to stop early on.
	 * @return Index where either condition is met.
	 */
	public static final int ingore(final CharSequence s, int offset, int length, final char until) {
		if(offset + length > s.length()){
			length= s.length();
		}else{
			length+= offset;
		}
		for(; offset < length; offset++){
			if(s.charAt(offset) == until){
				break;
			}
		}
		return offset;
	}

	/** Returns a <code>String</code> filled with <var>repeat</var> <var>c</var>'s.
	 * @param c Character to be repeated
	 * @param repeat Number of <var>c</var>'s
	 * @return Returns a <code>String</code> filled with <var>repeat</var> <var>c</var>'s.
	 * Returns an empty string if <code><var>repeat</var> &lt;= 0</code>
	 */
	public static final String repeat(final char c, int repeat) {
		if (repeat <= 0) return "";
		char[] ch= new char[repeat];
		Arrays.fill(ch, c);
		return new String(ch);
	}
	public static final String repeat(final String str, int repeat) {
		if (repeat <= 0) return "";
		final StringBuilder buf = new StringBuilder(repeat * str.length());
		while(--repeat >= 0) {
			buf.append(str);
		}
		return buf.toString();
	}
	/**
	 * Pads <var>input</var> by inserting <var>pad</var> on the left<br>
	 * until its length equals <var>cols</var>. If the length of <var>input</var> is<br>
	 * already equal to or greater than <var>cols</var> then<br>
	 * the original string is returned.
	 *
	 * @param cols Number of columns to pad to.
	 * @param pad The character to be used for padding.
	 * @param input String to be padded.
	 * @return The padded string.
	 */
	public static final String padLeft(final int cols, final char pad, final String input) {
		if (cols <= input.length()){
			return input;
		}

		final char[] buf= new char[cols];
		final int diff= cols - input.length();
		Arrays.fill(buf, 0, diff, pad);
		System.arraycopy(input.toCharArray(), 0, buf, diff, input.length());

		return new String(buf);
	}
	/**
	 * Pads <var>input</var> by inserting <var>pad</var> on the right<br>
	 * until its length equals <var>cols</var>. If the length of <var>input</var> is<br>
	 * already equal to or greater than <var>cols</var> then<br>
	 * the original string is returned.
	 *
	 * @param cols Number of columns to pad to.
	 * @param pad The character to be used for padding.
	 * @param input String to be padded.
	 * @return The padded string.
	 */
	public static final String padRight(final int cols, final char pad, final String input) {
		if (cols <= input.length()){
			return input;
		}

		char[] buf= new char[cols];
		System.arraycopy(input.toCharArray(), 0, buf, 0, input.length());
		Arrays.fill(buf, input.length(), buf.length, pad);

		return new String(buf);
	}

	public static final boolean equals(final char[] c0, final char[] c1) {
		if(c0 == c1){
			return true;
		}
		if (c0 == null || c1 == null || c0.length != c1.length){
			return false;
		}
		for (int i = 0; i < c0.length; i++) {
			if (c0[i] != c1[i]){
				return false;
			}
		}
		return true;
	}

	public static final String capitalize(String str){
		if(str==null || str.isEmpty())return str;
		char[] chars=str.toCharArray();
		chars[0]=Character.toTitleCase(chars[0]);
		for(int i =0;i<chars.length;i++){
			if(Character.isWhitespace(chars[i]) && (i+1)<chars.length)
				chars[i+1]=Character.toTitleCase(chars[i+1]);
		}
		return new String(chars);
	}

	/**
	 * Adds a line break every <code>cols</code>. If a Line break falls in the
	 * middle of a word and <code>wrapWord</code> is set then the line break
	 * will be inserted at the previous white space.
	 * @param val String to be formatted.
	 * @param cols Column number to wrap at.
	 * @param wrapWord Preserve whole words.
	 * @return The formatted string.
	 */
	public static final String wrapString(final String val, final int cols, final boolean wrapWord) {
		return wrapString(val, cols, wrapWord, UNIX_LINE_ENDING);
	}
	/**
	 * Adds a line break every <code>cols</code>. If a line break falls in the
	 * middle of a word and <code>wrapWord</code> is set then the line break
	 * will be inserted at the previous white space.
	 * <code>lineSep</code> can be set to anything. However, convenience variables
	 * have been made:
	 * <ul>
	 * <li>{@link #UNIX_LINE_ENDING}</li>
	 * <li>{@link #LINUX_LINE_ENDING}</li>
	 * <li>{@link #MAC_LINE_ENDING}</li>
	 * <li>{@link #WINDOWS_LINE_ENDING}</li>
	 * </ul>
	 * @param val String to be formatted.
	 * @param cols Column number to wrap at.
	 * @param wrapWord Preserve whole words.
	 * @param lineSep String that will be used to mark the end of a line.
	 * @return The formatted string.
	 */
	public static final String wrapString(final String val, final int cols, final boolean wrapWord, final String lineSep) {
		final StringBuilder buf= new StringBuilder(val.length());
		// index of the last line break
		int prevBreakIdx= 0,
			newBreakIdx= 0,
			end= val.length() + cols,
			valLen= val.length();
		for (int curIdx= 0; curIdx < end; curIdx+= cols) {
			if (curIdx >= valLen) {
				buf.append(val.substring(prevBreakIdx));
				break;
			}
			if(wrapWord){
				// back-up to a whitespace
				for(newBreakIdx= curIdx; (val.charAt(newBreakIdx) != ' ') && (newBreakIdx != prevBreakIdx); newBreakIdx--);
			}else{
				newBreakIdx= curIdx;
			}
			if (newBreakIdx > prevBreakIdx){
				buf.append(val.substring(prevBreakIdx, newBreakIdx));
			} else {
				buf.append(val.substring(prevBreakIdx, curIdx));
				newBreakIdx= curIdx;
			}
			prevBreakIdx= newBreakIdx;
			buf.append(lineSep);
		}
		return buf.toString();
	}

	public static final String ltrim(String str){
		int i= 0, len= str.length();
		for(;i < len && Character.isWhitespace(str.charAt(i)); i++);
		return str.substring(i);
	}

	public static final String rtrim(String str){
		int i= str.length() - 1;
		for(;i > -1 && Character.isWhitespace(str.charAt(i));i--);
		return str.substring(0, i);
	}
}
