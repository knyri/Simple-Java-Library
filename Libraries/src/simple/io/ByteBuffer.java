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
	private int end = -1;
	/** number of bytes in use */
	private int size = 0;
	/** Threshold before growing */
	private final float fillLimit;
	/** percentage of total to grow */
	private final float growthRate;
	/**
	 * @param initialSize starting size
	 * @param growAt How full it can get before growing ( must be less than 1 )
	 * @param growBy How much to grow by ( must be less than one )
	 */
	public ByteBuffer(int initialSize, float growAt, float growBy){
		buf = new byte[initialSize];
		fillLimit =growAt;
		growthRate= 1+growBy;
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
		if (growthRate != 0 && size/(float)buf.length >= fillLimit) {
			grow();
		}
	}

	/**
	 * Places the byte into the buffer.
	 * @param v the value
	 */
	public void put(byte v) {
		synchronized(buf) {
			ensureCapacity();
			if (++end == buf.length)
				end = 0;
			if(size != 0 && end == start) throw new BufferOverflowException();
			buf[end] = v;
			size++;
		}
	}
	/**
	 * @param at Can be negative
	 * @param v the value
	 */
	public void set(int at, byte v) {
		if(at < 0) at = size + at;
		at= (start + at)%buf.length;
		if(at > end) throw new IndexOutOfBoundsException();
		buf[at] = v;
	}

	public void increment(int at, int by){
		if(at < 0) at = size + at;
		at= (start + at)%buf.length;
		if(at > end) throw new IndexOutOfBoundsException();
		buf[at] += by;
	}
	public void incrementLast(int by){
		if (size == 0)
			throw new BufferUnderflowException();
		buf[end] += by;
	}
	public void incrementFirst(int by){
		if (size == 0)
			throw new BufferUnderflowException();
		buf[start] += by;
	}

	public boolean isEmpty(){
		return size == 0;
	}

	/** Gets the next byte and increments the pointer.
	 * @return The next byte.
	 */
	public byte get() {
		synchronized(buf) {
			if (size == 0)
				throw new BufferUnderflowException();
			byte b = buf[start++];
			if (start == buf.length)
				start = 0;
			size--;
			return b;
		}
	}
	/**
	 * Removes and returns the last byte
	 * @return The last byte
	 */
	public byte getLast(){
		synchronized(buf) {
			if (size == 0)
				throw new BufferUnderflowException();
			byte ret= buf[end--];
			if(end<0)
				end= buf.length-1;
			size--;
			return ret;
		}
	}
	public void clear(){
		start= size= 0;
		end= -1;
	}
	public byte[] asArray(){
		byte[] ret= new byte[size];
		if (start > end) {
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
	public byte peek(){
		return buf[start];
	}
	public byte peek(int at){
		if(at < 0) at = size + at;
		at= (start+at)%buf.length;
		if(at > end) throw new IndexOutOfBoundsException();
		return buf[at];
	}

}
