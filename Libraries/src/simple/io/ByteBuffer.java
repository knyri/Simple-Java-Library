package simple.io;

/**Byte buffer utilizing a circular array that can auto-grow.
 * <br>Created: 2006
 * @author Kenneth Pierce
 */
public class ByteBuffer {
	/** the buffer */
	protected byte[] buf;
	/** index of the first byte */
	private int start = 0;
	/** index of the last byte */
	private int end = 0;
	/** number of bytes in use */
	private int size = 0;
	/** Threshold before growing */
	private float fillLimit = 0.75f;
	/** percentage of total to grow */
	private float growthRate = 0.33f;
	public ByteBuffer(int initialSize) {
		buf = new byte[initialSize];
	}
	/**The max number of elements
	 * @return the maximum number of elements
	 */
	public int getCapacity() {
		return buf.length;
	}
	/** number of elements in use
	 * @return number of elements in use
	 */
	public int getSize() {
		return size;
	}
	private void grow() {
		synchronized(buf) {
			byte[] tmp = new byte[(int)(size*(1+growthRate))];
			if (start > end) {
				System.arraycopy(buf, start, tmp, 0, buf.length - start);
				System.arraycopy(buf, 0, tmp, buf.length - start, end);
			} else {
				System.arraycopy(buf, start, buf, 0, end - start);
			}
			buf = tmp;
			start = 0;
			end = size - 1;
		}
	}
	/**
	 * grows the internal array if needed.
	 */
	private void ensureCapacity() {
		if (size/buf.length >= fillLimit) {
			grow();
		}
	}
	/**Places the byte into the buffer.
	 * @param Byte
	 */
	public void put(int Byte) {
		synchronized(buf) {
			ensureCapacity();
			if (++end == buf.length)
				end = 0;//loop around
			//took me awhile to grasp the concept after I thought of it
			buf[end] = (byte)Byte;
			size++;
		}
	}
	/** Gets the next byte and increments the pointer.
	 * @return The next byte.
	 */
	public byte get() {
		synchronized(buf) {
			if (start == end) {
				return 0;
			}
			byte b = buf[start];
			start++;
			if (start == buf.length)
				start = 0;
			size--;
			return b;
		}
	}
}
