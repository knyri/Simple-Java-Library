/**
 *
 */
package simple.collections;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * Iterator classes for convenience.
 * @author Kenneth Pierce
 *
 */
public final class IteratorFactory{

	private IteratorFactory(){}

	public static <E> Iterable<E> create(E[] arr){
		return new ArrayIterator<E>(arr);
	}
	/**This can only be iterated over once.
	 * @param enu Enumeration to be iterated over.
	 * @return
	 */
	public static <E> Iterable<E> create(Enumeration<E> enu){
		return new EnumIterator<E>(enu);
	}

	static class ArrayIterator<E> implements Iterable<E> {
		private final E[] array;

		public ArrayIterator(final E[] array) {
			this.array = array;
		}
		@Override
		public Iterator<E> iterator(){
			return new Iterator<E>(){
				private int pos = 0;
				@Override
				public boolean hasNext() {
					return (pos<array.length);
				}

				@Override
				public E next() {
					if (hasNext())
						return array[pos++];
					else
						return null;
				}
				@Override
				public void remove() {}
			};
		}
	}
	static  class EnumIterator<T> implements Iterable<T>{
			private final Enumeration<T> enumer;
			public EnumIterator(Enumeration<T> en){
				enumer=en;
			}
			@Override
			public Iterator<T> iterator(){
				return new Iterator<T>(){
					@Override
					public boolean hasNext(){
						return enumer.hasMoreElements();
					}
					@Override
					public T next(){
						return enumer.nextElement();
					}
					@Override
					public void remove(){	}
				};
			}
		}
}
