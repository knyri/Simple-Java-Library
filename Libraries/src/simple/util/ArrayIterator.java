package simple.util;

import java.util.Iterator;

/**
 * Takes an Object array and creates an iterator.
 * <br>Created: 2006
 * @author KP
 * @param <E> Class of the elements in the array
 */
public class ArrayIterator<E> implements Iterator<E> {
	private final E[] array;
	private int pos = 0;
	/**
	 * @param array Array to iterate.
	 */
	public ArrayIterator(final E[] array) {
		this.array = array;
	}
	public boolean hasNext() {
		return (pos<array.length);
	}

	public E next() {
		if (hasNext())
			return array[pos++];
		else
			return null;
	}
	/**
	 * Not used.
	 */
	public void remove() {
		// not implemented
	}

}
