/**
 *
 */
package simple.util.hash;


/**
 * <hr>
 * <br>Created: Dec 13, 2010
 * @author Kenneth Pierce
 */
public final class Util {
	/**Rotates the bits left s positions.
	 * @param a Bits to rotate
	 * @param s number of positions to shift
	 * @return the rotated bits
	 */
	public static final int rotateLeft(int a, int s) {
		s= s%32;
		return ((a << s) | (a >>> (32-s)));
	}
	public static final byte rotateLeft(byte x, int s) {
		s= s%8;
		return (byte)((x << s) | (x >>> (8-s)));
	}
	/**Transforms a byte array into an integer array.
	 * @param ibuf destination
	 * @param in source
	 * @param offset start index
	 */
	public static void decode(int[] ibuf, byte in[], int offset) {
		for (int x = 0, y = offset; y < in.length; x++, y+=4) {
			ibuf[x] =
					(in[y] & 0xFF)
					| (( in[y+1] & 0xFF) << 8)
					| (( in[y+2] & 0xFF) << 16)
					| (( in[y+3] & 0xFF) << 24);
		}
	}
}
