package simple.io;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;

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
	private final float fillLimit;
	/** percentage of total to grow */
	private final float growthRate;
	public ByteBuffer(int initialSize, float growAt, float growBy){
		buf = new byte[initialSize];
		fillLimit =growAt;
		growthRate= growBy;
	}
	public ByteBuffer(int initialSize) {
		this(initialSize, 0.75f, 0.33f);
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
				// Might as well place them in order while we're here
				System.arraycopy(buf, start, tmp, 0, buf.length - start);
				System.arraycopy(buf, 0, tmp, buf.length - start, end);
			} else {
				System.arraycopy(buf, start, tmp, 0, end - start);
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
		if (growthRate != 0 && size/buf.length >= fillLimit) {
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
				end = 0;
			if(end == start) throw new BufferOverflowException();
			buf[end] = (byte)Byte;
			size++;
		}
	}
	public boolean isEmpty(){
		return start != end;
	}
	/** Gets the next byte and increments the pointer.
	 * @return The next byte.
	 */
	public byte get() {
		synchronized(buf) {
			if (start == end) {
				throw new BufferUnderflowException();
			}
			byte b = buf[start];
			start++;
			if (start == buf.length)
				start = 0;
			size--;
			return b;
		}
	}
	public void clear(){
		start= end= size= 0;
	}
	public byte[] asArray(){
		byte[] ret= new byte[size];
		if (start > end) {
			// Might as well place them in order while we're here
			System.arraycopy(buf, start, ret, 0, buf.length - start);
			System.arraycopy(buf, 0, ret, buf.length - start, end);
		} else {
			System.arraycopy(buf, start, ret, 0, end - start);
		}
		return ret;
	}
	public byte peekLast(){
		return buf[end];
	}
	public byte peekFirst(){
		return buf[start];
	}
}
