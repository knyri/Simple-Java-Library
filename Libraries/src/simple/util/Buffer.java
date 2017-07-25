package simple.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class Buffer<E> implements Queue<E>, List<E> {
	/** the buffer */
	protected Object[] buf;
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

	private ArrayIterator<E> iterator;

	/**
	 * @param initialSize starting size
	 * @param growAt How full it can get before growing ( must be less than 1 )
	 * @param growBy How much to grow by ( must be less than one )
	 */
	public Buffer(int initialSize, float growAt, float growBy){
		buf = new Object[initialSize];
		fillLimit =growAt;
		growthRate= 1+growBy;
		iterator= new ArrayIterator<E>((E[])buf);
	}
	public Buffer(int initialSize) {
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

	@Override
	public boolean addAll(Collection<? extends E> col) {
		if(!ensureCapacity(size + col.size())){
			return false;
		}
		for(E el: col){
			_add(el);
		}
		return true;
	}

	@Override
	public void clear() {
		size= start= end= 0;
		for(int i= 0, len= buf.length; i < len; i++){
			buf[i]= null;
		}
	}

	@Override
	public boolean contains(Object needle) {

		if(needle == null){
			// no nulls allowed so this is guaranteed
			return false;
		}
		for(Object o: buf){
			if(o == needle || needle.equals(o)){
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> col) {
		for(Object o: col){
			if(!contains(o)){
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public Iterator<E> iterator() {
		return iterator.iterator(start, end);
	}

	@Override
	public boolean remove(Object o) {
		if(o == null){
			throw new NullPointerException("Parameter cannot be null");
		}

		int idx= _indexOf(o);
		if(idx != -1){
			_removeElementAt(idx);
			return true;
		}
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> col) {
		for(Object o: col){
			if(!remove(o)){
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean retainAll(Collection<?> col) {
		for(int pos= start, len= buf.length; pos != end; pos= (pos + 1) % len){
			if(!col.contains(buf[pos])){
				_removeElementAt(pos);
			}
		}
		return true;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Object[] toArray() {
		Object[] tmp = new Object[size];
		if (start > end) {
			System.arraycopy(buf, start, tmp, 0, buf.length - start);
			System.arraycopy(buf, 0, tmp, buf.length - start, end);
		} else {
			System.arraycopy(buf, start, tmp, 0, end - start);
		}
		return tmp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] ary) {
		if(ary == null){
			throw new NullPointerException("Array cannot be null.");
		}

		if(ary.length < size){
			return (T[])toArray();
		}
		if (start > end) {
			System.arraycopy(buf, start, ary, 0, buf.length - start);
			System.arraycopy(buf, 0, ary, buf.length - start, end);
		} else {
			System.arraycopy(buf, start, ary, 0, end - start);
		}
		return ary;
	}

	@Override
	public boolean add(E o) {
		if(!ensureCapacity(size + 1) || !_add(o)){
			throw new IllegalStateException();
		}
		return true;
	}
	@Override
	public boolean offer(E o) {
		return ensureCapacity(size + 1) && (size == 0 || start != end) && _add(o);
	}


	@SuppressWarnings("unchecked")
	@Override
	public E element() {
		if(size == 0){
			throw new NoSuchElementException();
		}
		return (E)buf[start];
	}
	@SuppressWarnings("unchecked")
	@Override
	public E peek() {
		return size == 0 ? null : (E)buf[start];
	}

	@SuppressWarnings("unchecked")
	@Override
	public E poll() {
		if(size == 0){
			return null;
		}

		Object ret= buf[start];
		incrementStart();

		return (E)ret;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E remove() {
		if(size == 0){
			throw new NoSuchElementException();
		}
		Object ret= buf[start];
		incrementStart();

		return (E)ret;
	}
	@Override
	public void add(int at, E o) {
		throw new UnsupportedOperationException("Will be added sometime.");
	}
	@Override
	public boolean addAll(int at, Collection<? extends E> col) {
		throw new UnsupportedOperationException("Too lazy to add this right now.");
	}
	@SuppressWarnings("unchecked")
	@Override
	public E get(int at) {
		_boundsCheckRel(at);
		return (E)buf[_absoluteIdx(at)];
	}
	@Override
	public int lastIndexOf(Object o) {
		if(o == null){
			return -1;
		}
		for(int i= end; end != start; i--){
			if(i == -1){
				i= buf.length - 1;
			}
			if(o == buf[i] || o.equals(buf[i])){
				return _relativeIdx(i);
			}
		}
		return -1;
	}
	@Override
	public ListIterator<E> listIterator() {
		throw new UnsupportedOperationException();
	}
	@Override
	public ListIterator<E> listIterator(int arg0) {
		throw new UnsupportedOperationException();
	}
	@SuppressWarnings("unchecked")
	@Override
	public E remove(int at) {
		_boundsCheckRel(at);
		at= _absoluteIdx(at);
		Object old= buf[at];
		_removeElementAt(at);
		return (E)old;
	}
	@SuppressWarnings("unchecked")
	@Override
	public E set(int at, E o) {
		if(o == null){
			throw new NullPointerException("Object cannot be null");
		}
		_boundsCheckRel(at);
		at= _absoluteIdx(at);
		Object old= buf[at];
		buf[at]= o;
		return (E)old;
	}
	@Override
	public List<E> subList(int arg0, int arg1) {
		throw new UnsupportedOperationException("Won't be implemented");
	}
	@Override
	public int indexOf(Object o) {
		int idx= _indexOf(o);
		if(idx != -1){
			idx= _relativeIdx(idx);
		}
		return idx;
	}
	/* *******************************************
	 * Utility functions
	 */

	/**
	 * Grows the array
	 * @param newSize requested size
	 */
	private void grow(int newSize) {
		// because this is one thing that can't screw up
		synchronized(buf){
			Object[] tmp = new Object[newSize < size ? (int)(size * (1 + growthRate)) : (int)(newSize * (1 + growthRate))];
			if (start > end) {
				// Might as well place them in order while we're here
				System.arraycopy(buf, start, tmp, 0, buf.length - start);
				System.arraycopy(buf, 0, tmp, buf.length - start, end);
			} else {
				System.arraycopy(buf, start, tmp, 0, end - start);
			}
			buf= tmp;
			iterator= new ArrayIterator<E>((E[])buf);
			start= 0;
			end= size - 1;
		}
	}
	/**
	 * grows the internal array if needed.
	 */
	private boolean ensureCapacity(int newSize) {
		if(growthRate == 0){
			return newSize < buf.length;
		}

		if (newSize/(float)buf.length >= fillLimit) {
			grow(newSize);
		}
		return true;
	}

	/**
	 * Adds and element to the end
	 * @param v
	 * @return
	 */
	private boolean _add(E v){
		if(v == null){
			throw new NullPointerException("Parameter cannot be null.");
		}

		if(start == end && size > 0){
			return false;
		}
		buf[end]= v;

		incrementEnd();
		return true;
	}

	/**
	 * Translates an absolute index to a relative index
	 * @param absolute
	 * @return
	 */
	private final int _relativeIdx(int absolute){
		if(start > end){
			return buf.length - start + absolute + 1;
		}else{
			return absolute - start;
		}
	}
	/**
	 * Translates a relative index to an absolute index
	 * @param relative
	 * @return
	 */
	private final int _absoluteIdx(int relative){
		return (start + relative) % buf.length;
	}
	/**
	 * Checks if relative index is in bounds
	 * @param relative
	 * @throws IndexOutOfBoundsException
	 */
	private final void _boundsCheckRel(int relative){
		if(relative < 0 || relative >= size){
			throw new IndexOutOfBoundsException(""+relative);
		}
	}
	/**
	 * Sets the start to null, increments the start,
	 * and reduces the size
	 */
	private final void incrementStart(){
		buf[start]= null;
		start= (start + 1) % buf.length;
		size-= 1;
	}
	/**
	 * Sets the element to null, decrements the end,
	 * and reduces the size
	 */
	private final void decrementEnd(){
		buf[end]= null;
		if(end == 0){
			end= buf.length - 1;
		}else{
			end-= 1;
		}
		size-= 1;
	}
	/**
	 * Increments the end and increases the size
	 */
	private final void incrementEnd(){
		end= (end + 1) % buf.length;
		size+= 1;
	}


	/**
	 * Removes the object at the absolute index
	 * @param absolute
	 */
	private void _removeElementAt(int absolute){
		if(buf[absolute] == null){
			throw new IndexOutOfBoundsException();
		}
		if(absolute == start){
			// simple increment and set null case
			incrementStart();
		}else if(absolute == end){
			// simple decrement and set null case
			decrementEnd();
		}else{
			// shift all elements
			int len= buf.length;
			int prev= absolute;
			absolute= (absolute + 1) % len;
			for(; absolute != end; prev= absolute, absolute= (absolute + 1) % len){
				buf[prev]= buf[absolute];
			}

			// set last item, now duplicated, to null
			decrementEnd();
		}
	}

	/**
	 * Absolute index of the object or -1
	 * @param o
	 * @return
	 */
	private final int _indexOf(Object o){
		if(o == null){
			return -1;
		}
		int len= buf.length;
		for(int pos= start; pos != end; pos= (pos + 1) % len){
			if(o == buf[pos] || o.equals(buf[pos])){
				return pos;
			}
		}
		return -1;
	}
	/*private final void incrementEnd(int by){
		int newEnd= (end + by);
		if(newEnd > buf.length){
			// need to wrap around
			newEnd= newEnd % buf.length;
			if(newEnd > start){
				throw new BufferOverflowException();
			}
		}else if(end < start && newEnd > start){
			throw new BufferOverflowException();
		}
		end= newEnd;
		size+= by;
	}*/
	/*private final void decrementEnd(int by){
		int newEnd= end - by;
		if(newEnd < 0){
			newEnd= buf.length + newEnd;
			if(start > newEnd){
				throw new BufferUnderflowException();
			}
		}else if(start < end && start > newEnd){
			throw new BufferUnderflowException();
		}
		end= newEnd;
		size-= by;
	}*/
	/*private final void incrementStart(int by){
		int newStart= start + by;
		if(start < end && newStart > end){
			throw new BufferUnderflowException();
		}
		if(newStart > buf.length){
			newStart= newStart % buf.length;
			if(newStart >= end){
				throw new BufferUnderflowException();
			}
		}
		start= newStart;
		size-= by;
	}*/
	/*private final void decrementStart(int by){
		int newStart= start - by;
		if(newStart < 0){
			newStart= buf.length + newStart;
			if(newStart < end){
				throw new BufferOverflowException();
			}
		}else if(start > end && newStart < end){
			throw new BufferOverflowException();
		}
		start= newStart;
		size+= by;
	}*/


}
