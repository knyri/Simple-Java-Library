/**
 * 
 */
package simple.util;

/**
 * @author Ken
 *
 */
public final class Bytes {
	/**
	 * @param b1
	 * @param b2
	 * @return True of the elements of both are identical.
	 */
	public static boolean compareBytes(byte[] b1, byte[] b2) {
		if (b1.length == b2.length) {
			for (int i = 0; i<b1.length; i++) {
				if (b1[i]!=b2[i]) return false;
			}
		} else {
			return false;
		}
		return true;
	}
	/**
	 * @param b1
	 * @param b2
	 * @param offset
	 * @param length
	 * @return true if the range is identical
	 */
	public static boolean compareBytes(byte[] b1, byte[] b2, int offset, int length) {
		if (offset < b1.length && offset < b2.length) {
			if (offset+length > b1.length)
				length = b1.length - offset;
			if (offset+length > b2.length)
				length = b2.length - offset;
			int max = offset+length;
			for (; offset<max; offset++) {
				if (b1[offset]!=b2[offset]) return false;
			}
		} else {
			return false;
		}
		return true;
	}
}
