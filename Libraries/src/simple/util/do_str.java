package simple.util;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import simple.io.DoubleParsePosition;
/**
 * Does various things for formatting and comparing Strings.
 * <br>Created: 2004
 * @author Kenneth Pierce
 * @version 1.5
 */
public final class do_str {
	/**
	 * Sets whether functions should return a "not found" value or throw a
	 * descriptive error when parameters are incorrect.
	 */
	public static boolean DEBUG = false;
	public static final String UNIX_LINE_ENDING = "\n",
	LINUX_LINE_ENDING = do_str.UNIX_LINE_ENDING,
	MAC_LINE_ENDING = do_str.UNIX_LINE_ENDING,
	WINDOWS_LINE_ENDING = "\r\n";
	private do_str() {}
	/**Returns the indexOf if it does not equal -1 or the default value if it does.
	 * This is for those situations where a -1 result is not optimal.
	 * @param indexOf The result of an indexOf(..) or anything that returns -1 for failure.
	 * @param value The default value
	 * @return The indexOf result if not -1 or the default value.
	 */
	public static final int defaultValue(final int indexOf, final int value) {
		if (indexOf == -1) return value;
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
		return do_str.indexOf(list, c, 0) != -1;
	}
	/** Returns the index of the first occurrence such that startsWith(s,f,offset) is true.
	 * @param haystack The string to search
	 * @param needle The string to find
	 * @param offset The starting index
	 * @return The index f is found or -1
	 */
	public static final int indexOf(final CharSequence haystack, final CharSequence needle, int offset) {
		if (needle.length()>haystack.length())return-1;
		final int max = haystack.length()-needle.length();
		for (;offset<=max; offset++)
			if (do_str.startsWith(haystack,needle,offset)) {
				break;
			}
		if (offset>max) return -1;
		return offset;
	}
	/** Returns the index of the first occurrence such that startsWith(s,f,offset) is true.
	 * @param haystack The string to search
	 * @param needle The string to find
	 * @param offset The starting index
	 * @param limit Index to stop searching at(inclusive)
	 * @return The index f is found or -1
	 */
	public static final int indexOf(final CharSequence haystack, final CharSequence needle, int offset, final int limit) {
		if (limit < offset)
			if (do_str.DEBUG) throw new StringIndexOutOfBoundsException("Limit["+limit+"] must be greater than or equal to the Offset["+offset+"]"); else return -1;
		if (offset>haystack.length())
			if (do_str.DEBUG) throw new StringIndexOutOfBoundsException("Offset["+offset+"] must be less than or equal to the length["+haystack.length()+"]"); else return -1;
		if (offset<0)
			if (do_str.DEBUG) throw new StringIndexOutOfBoundsException("The offset["+offset+"] is negative."); else return -1;
		final int max = limit-needle.length()-1;
		if (max < 0) return -1;
		for (;offset<=max; offset++)
			if (do_str.startsWith(haystack,needle,offset)) {
				break;
			}
		if (offset>max) return -1;
		return offset;
	}
	/**Exists only to add indexOf(char,int) to classes like StringBuilder.
	 * @param haystack The string to search
	 * @param needle The string to find
	 * @param offset Index to start from
	 * @return The index of <code>f</code> or -1.
	 */
	public static final int indexOf(final CharSequence haystack, final char needle, final int offset) {
		return do_str.indexOf(haystack,needle,offset,haystack.length()-1);
	}
	/**indexOf(..) with a twist. Will only search to the limit specified.
	 * @param haystack The string to search
	 * @param needle The string to find
	 * @param offset Index to start from
	 * @param limit Index to stop at(inclusive)
	 * @return The index of <code>f</code> or -1.
	 */
	public static final int indexOf(final CharSequence haystack, final char needle, int offset, final int limit) {
		if (offset>limit || offset<0) return -1;
		for (;offset<=limit;offset++) {
			if (haystack.charAt(offset)==needle) return offset;
		}
		return -1;
	}
	/**Exists only to add indexOf(char,int) to classes like StringBuilder.
	 * @param haystack The string to search
	 * @param needle The string to find
	 * @param offset Index to start from
	 * @return The index of <code>f</code> or -1.
	 */
	public static final int indexOf(final CharSequence haystack, final char needle, char quote, final int offset) {
		return do_str.indexOf(haystack,needle, quote, offset, haystack.length()-1);
	}
	/**indexOf(..) with a twist. Will only search to the limit specified.
	 * @param haystack The string to search
	 * @param needle The string to find
	 * @param offset Index to start from
	 * @param limit Index to stop at(inclusive)
	 * @return The index of <code>f</code> or -1.
	 */
	public static final int indexOf(final CharSequence haystack, final char needle, char quote, int offset, final int limit) {
		if (offset>limit || offset<0) return -1;
		boolean quoted= false;
		for (;offset<=limit;offset++) {
			if(haystack.charAt(offset)==quote){
				quoted = !quoted;
			} else
			if (haystack.charAt(offset)==needle && !quoted){
				return offset;
			}
		}
		return -1;
	}
	/**Exists only to add indexOf(char,int) to classes like StringBuilder.
	 * @param haystack The string to search
	 * @param needle The string to find
	 * @param offset Index to start from
	 * @return The index of <code>f</code> or -1.
	 */
	public static final int indexOfQuoted(final CharSequence haystack, final char needle, final int offset) {
		return do_str.indexOfQuoted(haystack,needle, offset, haystack.length()-1);
	}
	/**indexOf(..) with a twist. Will only search to the limit specified.
	 * @param haystack The string to search
	 * @param needle The string to find
	 * @param offset Index to start from
	 * @param limit Index to stop at(inclusive)
	 * @return The index of <code>f</code> or -1.
	 */
	public static final int indexOfQuoted(final CharSequence haystack, final char needle, int offset, final int limit) {
		if (offset>limit || offset<0) return -1;
		boolean
			squoted= false,
			dquoted= false;
		for (;offset<=limit;offset++) {
			if(haystack.charAt(offset)== '"' && !squoted){
				dquoted = !dquoted;
			} else
			if(haystack.charAt(offset)== '\'' && !dquoted){
				squoted = !squoted;
			} else
			if (haystack.charAt(offset)==needle && !dquoted && !squoted){
				return offset;
			}
		}
		return -1;
	}
	/** Returns the index after the first occurrence such that startsWith(s,f,offset) is true.
	 * @param haystack The string to search
	 * @param needle The string to find
	 * @param offset The starting index
	 * @return The index f is found or -1
	 */
	public static final int indexAfter(final CharSequence haystack, final CharSequence needle, int offset) {
		final int max = haystack.length()-needle.length();
		for (;offset<=max; offset++)
			if (do_str.startsWith(haystack,needle,offset)) {
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
	 * @return The index f is found or -1
	 */
	public static final int indexAfter(final CharSequence haystack, final CharSequence needle, int offset, final int limit) {
		if (limit < offset)
			if (do_str.DEBUG) throw new StringIndexOutOfBoundsException("Limit["+limit+"] must be greater than or equal to the Offset["+offset+"]"); else return -1;
		if (offset>haystack.length())
			if (do_str.DEBUG) throw new StringIndexOutOfBoundsException("Offset["+offset+"] must be less than or equal to the length["+haystack.length()+"]"); else return -1;
		if (offset<0)
			if (do_str.DEBUG) throw new StringIndexOutOfBoundsException("The offset["+offset+"] is negative."); else return -1;
		final int max = limit-needle.length()-1;
		if (max < 0) return -1;
		for (;offset<=max; offset++)
			if (do_str.startsWith(haystack,needle,offset)) {
				break;
			}
		if (offset>max) return -1;
		return offset+needle.length();
	}
	/** Returns the index of the first occurrence of any character in <code>list</code>.
	 * @param haystack String to search
	 * @param needles List of characters to search for
	 * @param offset Index to start searching
	 * @param limit Index to stop searching(inclusive)
	 * @return The index where one of the listed characters were found or -1.
	 */
	public static final int indexOfAny(final CharSequence haystack, final CharSequence needles, int offset, final int limit) {
		if (limit < offset)
			if (do_str.DEBUG) throw new StringIndexOutOfBoundsException("Limit["+limit+"] must be greater than or equal to the Offset["+offset+"]"); else return -1;
		if (offset>haystack.length())
			if (do_str.DEBUG) throw new StringIndexOutOfBoundsException("Offset["+offset+"] must be less than or equal to the length["+haystack.length()+"]"); else return -1;
		if (offset<0)
			if (do_str.DEBUG) throw new StringIndexOutOfBoundsException("The offset["+offset+"] is negative."); else return -1;
		for (;offset<=limit;offset++) {
			for (int j = 0; j < needles.length(); j++)
				if (haystack.charAt(offset)==needles.charAt(j)) return offset;
		}
		return -1;
	}
	/** Returns the index of the first occurrence of any character in <code>list</code>.
	 * @param haystack String to search
	 * @param needles List of characters to search for
	 * @param offset Index to start searching
	 * @return The index where one of the listed characters were found or -1.
	 */
	public static final int indexOfAny(final CharSequence haystack, final CharSequence needles, final int offset) {
		return do_str.indexOfAny(haystack, needles, offset, haystack.length()-1);
	}
	/** Returns the index of the first occurrence of any character in <code>list</code>.
	 * @param haystack String to search
	 * @param needles List of characters to search for
	 * @return The index where one of the listed characters were found or -1.
	 */
	public static final int indexOfAny(final CharSequence haystack, final CharSequence needles) {
		return do_str.indexOfAny(haystack, needles, 0, haystack.length()-1);
	}
	/** Returns the index of the first occurrence where the character in
	 * <code>s</code> at the index is not found at any index in <code>list</code>.
	 * @param s The source.
	 * @param list The characters to pass over.
	 * @param offset Index to start at.
	 * @param limit Index to stop at(inclusive).
	 * @return Index of the unlisted character.
	 */
	public static final int indexOfMissing(final CharSequence s, final CharSequence list, int offset, final int limit) {
		if (limit < offset)
			if (do_str.DEBUG) throw new StringIndexOutOfBoundsException("Limit["+limit+"] must be greater than or equal to the Offset["+offset+"]"); else return -1;
		if (offset>s.length())
			if (do_str.DEBUG) throw new StringIndexOutOfBoundsException("Offset["+offset+"] must be less than or equal to the length["+s.length()+"]"); else return -1;
		if (offset<0)
			if (do_str.DEBUG) throw new StringIndexOutOfBoundsException("The offset["+offset+"] is negative."); else return -1;
		int j = 0;
		for (;offset<=limit;offset++) {
			for (j = 0; j < list.length(); j++)
				if (s.charAt(offset)==list.charAt(j)) {
					break;
				}
			if (j==list.length()) return offset;//character was not found in the list
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
		return do_str.indexOfMissing(s, list, offset, s.length()-1);
	}
	/**Convenience method for {@link #indexOfMissing(CharSequence, CharSequence, int, int)}
	 * @param s
	 * @param list
	 * @return  Index of a character not in the list or -1 if none were found
	 * @see #indexOfMissing(CharSequence, CharSequence, int, int)
	 */
	public static final int indexOfMissing(final CharSequence s, final CharSequence list) {
		return do_str.indexOfMissing(s, list, 0, s.length()-1);
	}
	/**Exists only to add lastIndexOf(char,int) to classes like StringBuilder.
	 * @param s The haystack
	 * @param f The needle
	 * @param offset Index to start from
	 * @param limit Index to stop at(inclusive; must be less than offset)
	 * @return The index of <code>f</code> or -1.
	 */
	public static final int lastIndexOf(final CharSequence s, final char f, int offset, final int limit) {
		if (limit > offset)
			if (do_str.DEBUG) throw new StringIndexOutOfBoundsException("Limit["+limit+"] must be less than or equal to the Offset["+offset+"]"); else return -1;
		if (offset>s.length())
			if (do_str.DEBUG) throw new StringIndexOutOfBoundsException("Offset["+offset+"] must be less than or equal to the length["+s.length()+"]"); else return -1;
		if (offset<0)
			if (do_str.DEBUG) throw new StringIndexOutOfBoundsException("The offset["+offset+"] is negative."); else return -1;
		for (;offset>=limit;offset--) {
			if (s.charAt(offset)==f) return offset;
		}
		return -1;
	}
	public static final int lastIndexOf(final CharSequence s, final String f, int offset, final int limit) {
		if (limit > offset)
			if (do_str.DEBUG) throw new StringIndexOutOfBoundsException("Limit["+limit+"] must be less than or equal to the Offset["+offset+"]"); else return -1;
		if (offset>s.length())
			if (do_str.DEBUG) throw new StringIndexOutOfBoundsException("Offset["+offset+"] must be less than or equal to the length["+s.length()+"]"); else return -1;
		if (offset<0)
			if (do_str.DEBUG) throw new StringIndexOutOfBoundsException("The offset["+offset+"] is negative."); else return -1;
		for (;offset>=limit;offset--) {
			if (do_str.startsWith(s,f,offset)) return offset;
		}
		return -1;
	}
	public static final int lastIndexOf(final CharSequence s, final String f) {
		return do_str.lastIndexOf(s,f,s.length(),0);
	}
	/**Exists only to add lastIndexOf(char,int) to classes like StringBuilder.
	 * @param s The haystack
	 * @param f The needle
	 * @param offset Index to start from
	 * @return The index of <code>f</code> or -1.
	 */
	public static final int lastIndexOf(final CharSequence s, final char f, final int offset) {
		return do_str.lastIndexOf(s,f,offset,0);
	}
	/** All methods are case insensitive.
	 * <br>Created: Oct 19, 2010
	 * @author Kenneth Pierce
	 */
	public static final class CI {
		/**Exists only to add lastIndexOf(char,int) to classes like StringBuilder.
		 * @param s The haystack
		 * @param f The needle
		 * @param offset Index to start from
		 * @param limit Index to stop at(inclusive; must be less than offset)
		 * @return The index of <code>f</code> or -1.
		 */
		public static final int lastIndexOf(final CharSequence s, final char f, final int offset, final int limit) {
			return do_str.lastIndexOf(s.toString().toLowerCase(),f,offset,limit);
		}
		public static final int lastIndexOf(final CharSequence s, final String f, final int offset, final int limit) {
			return do_str.lastIndexOf(s.toString().toLowerCase(),f.toLowerCase(),offset,limit);
		}
		public static final int lastIndexOf(final CharSequence s, final String f) {
			return do_str.CI.lastIndexOf(s,f,s.length(),0);
		}
		/**Exists only to add lastIndexOf(char,int) to classes like StringBuilder.
		 * @param s The haystack
		 * @param f The needle
		 * @param offset Index to start from
		 * @return The index of <code>f</code> or -1.
		 */
		public static final int lastIndexOf(final CharSequence s, final char f, final int offset) {
			return do_str.lastIndexOf(s.toString().toLowerCase(),f,offset,0);
		}
		/** Returns the index of the first occurrence such that startsWith(s,f,offset) is true.
		 * @param s The string to search
		 * @param f The string to find
		 * @param offset The starting index
		 * @return The index f is found or -1
		 */
		public static final int indexOf(final CharSequence s, final CharSequence f, int offset) {
			final int max = s.length()-f.length();
			for (;offset<=max; offset++)
				if (CI.startsWith(s,f,offset)) {
					break;
				}
			if (offset>max) return -1;
			return offset;
		}
		/**Adds startsWith() to StringBuilder.
		 * @param s String to search
		 * @param f String to find
		 * @param offset index to start the comparison
		 * @return <code>true</code> if the character sequence represented by the argument is a prefix of the character sequence represented by this string; <code>false</code> otherwise.
		 */
		public static final boolean startsWith(final CharSequence s, final CharSequence f, final int offset) {
			if (offset < 0) throw new IllegalArgumentException("Offset cannot be negative.");
			if (offset+f.length() > s.length()) return false;
			for (int i=0; i<f.length(); i++)
				if (Character.toLowerCase(s.charAt(i+offset)) != Character.toLowerCase(f.charAt(i))) return false;
			return true;
		}
		/**Adds endsWith() to StringBuilder
		 * @param s String to search
		 * @param f String to find
		 * @return <code>true</code> if the string ends with <code>f</code>
		 */
		public static final boolean endsWith(final CharSequence s, final CharSequence f) {
			return CI.startsWith(s,f,s.length()-f.length());
		}
	}
	/**Adds startsWith() to StringBuilder. Short for {@link #startsWith(CharSequence, CharSequence, int)}.
	 * @param haystack
	 * @param needle
	 * @return true if haystack starts with needle
	 * @see #startsWith(CharSequence, CharSequence, int)
	 */
	public static final boolean startsWith(final CharSequence haystack, final CharSequence needle) {
		return do_str.startsWith(haystack, needle, 0);
	}
	public static final boolean startsWith(final CharSequence haystack, final CharSequence needle, final int offset) {
		if ((needle.length()+offset)>haystack.length()) return false;
		for (int i = 0; i < needle.length(); i++) {
			if (haystack.charAt(offset+i) != needle.charAt(i)) return false;
		}
		return true;
	}
	/**Adds endsWith() to StringBuilder
	 * @param haystack String to search
	 * @param needle String to find
	 * @return <code>true</code> if the string ends with <code>f</code>
	 */
	public static final boolean endsWith(final CharSequence haystack, final CharSequence needle) {
		return do_str.startsWith(haystack,needle,haystack.length()-needle.length());
	}
	/**Adds a special endsWith(). Tests to see if the characters before the offset match.
	 * @param haystack String to search
	 * @param needle String to find
	 * @param offset Index to start
	 * @return <code>true</code> if the string ends with <code>f</code>.
	 */
	public static final boolean endsWith(final CharSequence haystack, final CharSequence needle, final int offset) {
		return do_str.startsWith(haystack,needle,offset-needle.length());
	}
	/**
	 * Skips anything that makes Character.isWhitespace return true.
	 * @param s
	 * @param offset Place to start
	 * @return Index where the whitespace ends.
	 */
	public static final int skipWhitespace(final CharSequence s, int offset) {
		while (offset < s.length() && Character.isWhitespace(s.charAt(offset)))
		{ offset++; }
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
		if (offset+length > s.length()) {
			length = s.length();
		} else {
			length += offset;
		}
		for (;offset < length; offset++) {
			if (s.charAt(offset)==until) {	break;	}
		}
		return offset;
	}
	/**
	 * Basically a start with where order doesn't matter.
	 * Compares two <code>String</code>s on a partial basis. Order is not
	 * important.
	 *
	 * @param s1
	 * @param s2
	 * @param ignorecase
	 * @return True as long as no one character differs. The two Strings can be
	 *          different lengths.<br>Ex. part_comp("Tutu", "Tu") returns true
	 *          <br>part_comp("tutu", "Tu") returns true
	 *          <br>part_comp("tutu", "tulip") returns false
	 */
	public static final boolean part_comp(String s1, String s2, final boolean ignorecase) {
		if (ignorecase) {
			s1 = s1.toLowerCase();
			s2 = s2.toLowerCase();
		}
		final int len = ((s1.length() > s2.length()) ? s2.length() : s1.length());
		for (int i = 0; i < len; i++) {
			if (s1.charAt(i) != s2.charAt(i))
				return false;
		}
		return true;
	}

	/**
	 * Compares two <code>String</code>s on a partial basis. <var>s1</var>.length()
	 * MUST be &lt;= <var>s2</var>.length().
	 *
	 * @param comparator
	 * @param original
	 * @param ignorecase
	 * @return True as long as <var>s1</var>.length() &lt;= <var>s2</var>.length()
	 *          and no one character differs.<br>
	 * Ex. part_comp("tu", "Tutu") returns true
	 * <br>part_comp("tut", "Tu") returns false
	 * <br>part_comp("tuT", "tut") returns true
	 */
	public static final boolean part_comp2(String comparator, String original, final boolean ignorecase) {
		if (comparator.length() > original.length())
			return false;
		if (ignorecase) {
			comparator = comparator.toLowerCase();
			original = original.toLowerCase();
		}
		for (int i = 0; i < comparator.length(); i++) {
			if (comparator.charAt(i) != original.charAt(i))
				return false;
		}
		return true;
	}
	/**
	 * Compares two <code>String</code>s and counts the number of same
	 * <code>char</code>s. Case matters.
	 *
	 * @param s1
	 * @param s2
	 * @return Number of same chars.<br>
	 * Ex. part_comp("Tutu", "Tu") returns 2
	 * <br>part_comp("tutu", "Tut") returns 0
	 * <br>part_comp("tutu", "tot") returns 1
	 */
	public static final int count_same(final String s1, final String s2) {
		final int len = ((s1.length() > s2.length()) ? s2.length() : s1.length());
		int i = 0;
		for (; i < len; i++) {
			if (s1.charAt(i) != s2.charAt(i)) {
				break;
			}
		}
		return i;
	}
	/**
	 * Compares two <code>String</code>s and counts the number of same
	 * <code>char</code>s. Case does not matter.
	 *
	 * @param s1
	 * @param s2
	 * @return Number of same chars.<br>
	 * Ex. part_comp("Tutu", "Tu") returns 2
	 * <br>part_comp("tutu", "Tut") returns 3
	 * <br>part_comp("tutu", "tot") returns 1
	 */
	public static final int count_sameIgnoreCase(String s1, String s2) {
		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();
		final int len = ((s1.length() > s2.length()) ? s2.length() : s1.length());
		int i = 0;
		for (; i < len; i++) {
			if (s1.charAt(i) != s2.charAt(i)) {
				break;
			}
		}
		return i;
	}
	/**
	 * Like count_same(String, String) but for a list.
	 * @param list
	 * @return The number of characters the list has in common.
	 */
	public static final int count_same(final String[] list) {
		int len = Integer.MAX_VALUE;
		int i = 0;
		for (; i < list.length; i++) {
			len = Math.min(len, list[i].length());
		}
		out:
			for (i = 0; i < len; i++) {
				for (int j = 0; j < list.length-1; j++)
					if (list[j].charAt(i) != list[j+1].charAt(i)) {
						break out;
					}
			}
		return i;
	}
	/**
	 * Like count_sameIgnoreCase(String, String) but for a list.
	 * @param list
	 * @return The number of characters the list has in common.
	 */
	public static final int count_sameIgnoreCase(final String[] list) {
		int len = Integer.MAX_VALUE;
		int i = 0;
		for (; i < list.length; i++) {
			list[i] = list[i].toLowerCase();
			len = Math.min(len, list[i].length());
		}
		out:
			for (i = 0; i < len; i++) {
				for (int j = 0; j < list.length-1; j++)
					if (list[j].charAt(i) != list[j+1].charAt(i)) {
						break out;
					}
			}
		return i;
	}
	/**
	 * Splits a <code>String</code> and returns the <var>i</var><sup>th</sup> element in
	 * the resulting array. If <var>i</var> &gt; the split array's length then
	 * the last element in the array is returned.
	 * @param input
	 * @param i
	 * @return <var>i<sup>th</sup></var> argument.
	 */
	public static final String argn(final String input, final int i) {
		final String z[] = input.split(" ");
		if (i > (z.length - 1))
			return z[z.length - 1];
		return z[i];
	}
	/** Returns a <code>String</code> filled with <var>repeat</var> <var>c</var>'s.
	 * @param c Character to be repeated
	 * @param repeat Number of <var>c</var>'s
	 * @return Returns a <code>String</code> filled with <var>repeat</var> <var>c</var>'s.
	 * Returns an empty string if <code><var>repeat</var> &lt;= 0</code>
	 */
	public static final String repeat(final char c, int repeat) {
		if (repeat <= 0) return "";
		final StringBuilder buf = new StringBuilder(repeat);
		while(--repeat>=0) {
			buf.append(c);
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
		if (cols<=input.length())
			return input;
		final char[] buf = new char[cols];
		final int diff = cols-input.length();
		int i= 0;
		for (; i<diff; i++) {
			buf[i]= pad;
		}
		for (; i<cols; i++) {
			buf[i]= input.charAt(i-diff);
		}
		return buf.toString();
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
		if (cols<=input.length())
			return input;
		final StringBuffer buf = new StringBuffer(cols);
		buf.append(input);
		final int diff = cols-input.length();
		for (int i = 0; i<diff; i++) {
			buf.append(pad);
		}
		return buf.toString();
	}
	/**
	 * Sorts a list descending by the char's value.<br>
	 * Order will be similar to this:<br>
	 * <ul>
	 * <li>Symbols</li>
	 * <li>Lowercase a-z</li>
	 * <li>Uppercase A-Z</li>
	 * <li>International characters</li>
	 * </ul>
	 * @param list String array to be sorted.
	 * @return The sorted list
	 */
	public static final String[] sort(final String[] list) {
		String tmp = null;
		boolean change = true;
		while (change) {
			change = false;
			for (int i = 0;i<list.length-1;i++) {
				if (do_str.compare(list[i], list[i+1])<0) {
					tmp = list[i];
					list[i] = list[i+1];
					list[i+1] = tmp;
					change = true;
				}
			}
		}
		return list;
	}
	/**
	 * Sorts a list ascending by the char's value.<br>
	 * Order will be similar to this:<br>
	 * <ul>
	 * <li>International characters</li>
	 * <li>Uppercase Z-A</li>
	 * <li>Lowercase z-a</li>
	 * <li>Symbols</li>
	 * </ul>
	 * @param list String array to be sorted.
	 * @return The sorted list
	 */
	public static final String[] sortReverse(final String[] list) {
		String tmp = null;
		boolean change = true;
		while (change) {
			change = false;
			for (int i = 0;i<list.length-1;i++) {
				if (do_str.compare(list[i], list[i+1])>0) {
					tmp = list[i];
					list[i] = list[i+1];
					list[i+1] = tmp;
					change = true;
				}
			}
		}
		return list;
	}
	/**Compares two strings to see which comes first.
	 * @param orig
	 * @param comp
	 * @return positive if <var>orig</var> comes before <var>comp</var> aplhabetically. Lowercase comes before uppercase.
	 */
	public static final int compare(final String orig, final String comp) {
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
		if (result==0) {
			if (orig.length()!=comp.length()) {
				if (orig.length()>comp.length()) {
					result = -1;
				} else {
					result = 1;
				}
			}
		}
		return result;
	}
	/**Compares two strings to see which comes first.
	 * @param orig
	 * @param comp
	 * @return result from compare(String, String)
	 */
	public static final int compareIgnoreCase(final String orig, final String comp) {
		return do_str.compare(orig.toLowerCase(), comp.toLowerCase());
	}
	/**
	 * @param s
	 * @return Returns something similar to the declaration code.
	 */
	public static final String toString(final String[] s) {
		final StringBuffer buf = new StringBuffer();
		buf.append("{");
		for (int i = 0;i<s.length;i++) {
			buf.append(s[i]+", ");
		}
		buf.delete(buf.length()-2,buf.length());
		buf.append("}");
		return buf.toString();
	}
	public static final String toString(final String[] s, final String seperator) {
		final StringBuffer buf = new StringBuffer(255);
		for(final String cur : s) {
			buf.append(cur + seperator);
		}
		buf.delete(buf.length()-seperator.length(), buf.length());
		return buf.toString();
	}
	public static final String toString(final Iterator<String> iter, final String seperator) {
		final StringBuffer buf = new StringBuffer(2048);
		while (iter.hasNext()) {
			buf.append(iter.next()+seperator);
		}
		buf.delete(buf.length()-seperator.length(), buf.length());
		return buf.toString();
	}
	public static final String toString(final List<String> iter, final String seperator) {
		return do_str.toString(iter.iterator(), seperator);
	}
	/**
	 * @param num Number to parse.
	 * @param sep Character to insert every three digits.
	 * @return A string value of <var>num</var> with <var>sep</var> inserted every three digits.
	 */
	public static final String toString (final long num, final char sep) {
		final char[] n = String.valueOf(num).toCharArray();
		final StringBuffer buf = new StringBuffer(n.length*(1+(n.length/3)-n.length%3));
		for (int i = 0;i < n.length; i++) {
			buf.append(n[i]);
			if (i%3==0) {
				buf.append(sep);
			}
		}
		return buf.toString();
	}
	/**
	 * Pattern used by {@link #isNaN(String) }.
	 */
	private static final Pattern nPattern = Pattern.compile("^[+\\-]?\\d*[,\\d{3}]*[\\.]?\\d*");
	/**
	 * Tests <var>x</var> to see if it contains any non-numerical<br>
	 * characters.
	 *
	 * @param x String to be tested.
	 * @return True if the string contains non-numerical characters.<br>
	 * 		False otherwise.
	 */
	public static final boolean isNaN(final String x) {
		if (x==null) return true;
		if (x.length()==0)
			return true;
		return !do_str.nPattern.matcher(x).matches();
	}
	/**
	 * Returns either an empty string or val.
	 * @param val String to test.
	 * @return Returns an empty String if val==null. If val
	 * is non-null, then it returns val.
	 */
	public static final String getNonNullValue(final String val) {
		return val==null?"":val;
	}
	/**
	 * Returns either def or val.
	 * @param val String to test.
	 * @param def Value to return if val is null.
	 * @return Returns val if val is non-null. If val is null
	 * then it returns def.
	 */
	public static final String getNonNullValue(final String val, final String def) {
		return val==null?def:val;
	}
	public static final boolean equals(final char[] c0, final char[] c1) {
		if (c0 == null || c1 == null) return false;
		if (c0.length != c1.length) return false;
		for (int i = 0; i < c0.length; i++) {
			if (c0[i] != c1[i])
				return false;
		}
		return true;
	}
	public static final boolean isWhiteSpace(final char c) {
		return (c == ' ' || c == '\n' || c == '\r' || c == '\t');
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
	 * @deprecated Use {@link #wrapString(String,int,boolean)} instead
	 */
	@Deprecated
	public static final String formatString(final String val, final int cols, final boolean wrapWord) {
		return do_str.wrapString(val, cols, wrapWord);
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
		return do_str.wrapString(val, cols, wrapWord, do_str.UNIX_LINE_ENDING);
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
		final StringBuffer buf = new StringBuffer(val.length());
		int breakIndex = 0;
		int j = 0;
		for (int i = 0; i<val.length()+cols; i+=cols) {
			if (i >= val.length()) {
				buf.append(val.substring(breakIndex));
				break;
			}
			if (wrapWord) {
				for (j = i; (val.charAt(j)!=' ')&&(j!=breakIndex) ; j--) {}
			} else {
				j = i;
			}
			if (j > breakIndex) {
				buf.append(val.substring(breakIndex, j));
			} else {
				buf.append(val.substring(breakIndex, i));
				j = i;
			}
			breakIndex = j;
			buf.append(lineSep);
		}
		return buf.toString();
	}
}