/**
 *
 */
package simple.util;

import java.util.Enumeration;

/**
 * Convenience factory for creating Enumerations
 * @author Kenneth Pierce
 *
 */
public final class EnumerationFactory{

	private EnumerationFactory(){}

	public static <E> Enumeration<E> create(E[] arr){
		return new ArrayEnumerator<E>(arr);
	}
	 static class ArrayEnumerator<E> implements Enumeration<E> {
			private final E[] array;
			private int pos = 0;

			public ArrayEnumerator(final E[] array) {
				this.array = array;
			}
			@Override
			public boolean hasMoreElements() {
				return (pos<array.length);
			}
			@Override
			public E nextElement() {
				if (hasMoreElements())
					return array[pos++];
				else
					return null;
			}
	 }
}
