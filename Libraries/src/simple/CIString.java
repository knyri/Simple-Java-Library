/**
 *
 */
package simple;

import java.nio.charset.Charset;

/**Case insensitive string for hash keys.
 * <br>Created: Jun 23, 2010
 * @author Kenneth Pierce
 */
public final class CIString implements CharSequence {
	public static final CIString EMPTY = new CIString("");
	private final String string;
	private final int hashcode;
	public CIString(String str) {
		string = str;
		hashcode = calcHashCode(str);
	}
	@Override
	public int hashCode() {
		return hashcode;
	}
	@Override
	public String toString() {
		return string;
	}
	@Override
	public boolean equals(Object obj) {
		if (obj==string) return true;
		return string.equalsIgnoreCase(obj.toString());
	}
	/* (non-Javadoc)
	 * @see java.lang.CharSequence#charAt(int)
	 */
	@Override
	public char charAt(int index) {
		return string.charAt(index);
	}

	/* (non-Javadoc)
	 * @see java.lang.CharSequence#length()
	 */
	@Override
	public int length() {
		return string.length();
	}

	/* (non-Javadoc)
	 * @see java.lang.CharSequence#subSequence(int, int)
	 */
	@Override
	public CharSequence subSequence(int beginIndex, int endIndex) {
		return string.subSequence(beginIndex, endIndex);
	}

	private static final char[] lc = new char[256];

	static {
		// just ISO-8859-1
		for (char idx=0; idx<256; idx++)
			lc[idx] = Character.toLowerCase(idx);
	}

	/**
     * We smash case before calculation so that the hash code is
     * "case insensitive". This is based on code snarfed from
     * java.lang.String.hashCode().
     */
	private static final int calcHashCode(String str) {
		int  hash  = 0;
		char llc[] = lc;
		int  len   = str.length();

		for (int idx= 0; idx<len; idx++) {
			if (str.charAt(idx) < 256)
				hash = 31*hash + llc[str.charAt(idx)];
		}
		return hash;
	}
	public byte[] getBytes(Charset charset) {
		return string.getBytes(charset);
	}
}
