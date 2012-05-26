package simple.util;

import java.util.Enumeration;

/**
 * Takes an Object array and creates an enumerator from it.
 * <br>Created: 2006
 * @author KP
 * @param <E> Class of the data in the array.
 */
public class ArrayEnumerator<E> implements Enumeration<E> {
	private final E[] array;
	private int pos = 0;
	
	public ArrayEnumerator(final E[] array) {
		this.array = array;
	}

	public boolean hasMoreElements() {
		return (pos<array.length);
	}

	public E nextElement() {
		if (hasMoreElements())
			return array[pos++];
		else
			return null;
	}

}
