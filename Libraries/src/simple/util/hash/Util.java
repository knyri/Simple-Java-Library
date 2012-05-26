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
	/**Does an addition modulus 2^32 and returns the result. Needed since
	 * Java does not have unsigned integers. UPDATE: They act the same. This
	 * really is not needed.
	 * @param a first integer
	 * @param b second integer
	 * @return the new integer
	 */
	public static final long m232(long a, long b) {
		return (a+b)%0x100000000L;
	}
	/** Performs a bitwise not operation. Negates the integer and subtracts one.
	 * NOTE: ~ performs the not operation.
	 * @param x integer to be flipped
	 * @return the flipped integer
	 */
	public static final int not(int x) {
		return (-x-1);
	}
	/**Rotates the bits left s positions.
	 * @param x Bits to rotate
	 * @param s number of positions to shift
	 * @return the rotated bits
	 */
	public static final int rotateLeft(int x, int s) {
		return (x << s) | (x >> (32-s));
	}
	public static final byte rotateLeft(byte x, int s) {
		return (byte)((x << s) | (x >> (8-s)));
	}
	/**Transforms a byte array into an integer array.
	 * @param out destination
	 * @param in source
	 */
	public static void decode(int out[], byte in[]) {
		for (int i = 0, j = 0; i < in.length; i++, j+=4) {
			out[i] = in[i] | (in[j+1] << 8) | (in[j+2] << 16) | (in[j+3] << 24);
		}
	}
	/**Transforms an integer array into a byte array.
	 * @param out destination
	 * @param in source
	 */
	public static void encode(byte out[], int in[]) {
		for (int i = 0, j = 0; i < in.length; i++, j+=4) {
			out[j] = (byte)in[i];
			out[j+1] = (byte)(in[i]>>8);
			out[j+2] = (byte)(in[i]>>16);
			out[j+3] = (byte)(in[i]>>24);
		}
	}
}
