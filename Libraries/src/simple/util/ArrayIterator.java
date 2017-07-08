package simple.util;

import java.util.Iterator;

/**
 * For things that are backed by an array
 * @param <E>
 */
public class ArrayIterator<E> implements Iterable<E> {
	private final Object[] array;

	public ArrayIterator(Object[] array) {
		this.array= array;
	}

	@Override
	public Iterator<E> iterator(){
		return iterator(0, array.length);
	}

	public Iterator<E> iterator(int start, int end){
		return new Iterator<E>(){
			private int pos= start;
			@Override
			public boolean hasNext() {
				return (pos != end);
			}

			@SuppressWarnings("unchecked")
			@Override
			public E next() {
				if (hasNext()){
					E retVal= (E)array[pos];
					pos= (pos + 1) % array.length;
					return retVal;
				}else{
					return null;
				}
			}
			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
}
