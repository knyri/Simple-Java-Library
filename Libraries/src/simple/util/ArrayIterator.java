package simple.util;

import java.util.Iterator;

/**
 * For things that are backed by an array.
 * It will allow start > end. If start > end it will go
 * from the start position to the end of the array and then from the
 * start of the array to the end position.
 * @param <E>
 */
public class ArrayIterator<E> implements Iterable<E> {
	private final E[] array;

	public ArrayIterator(E[] array) {
		this.array= array;
	}

	@Override
	public Iterator<E> iterator(){
		return iterator(0, array.length);
	}

	public Iterator<E> iterator(final int start, final int end){
		if(start == end || (start + 1) == end){
			return new Iterator<E>(){
				boolean notCalled= true;
				@Override
				public boolean hasNext() {
					return notCalled;
				}

				@Override
				public E next() {
					notCalled= false;
					return array[start];
				}
			};
		}
		return new Iterator<E>(){
			private int pos= start;
			@Override
			public boolean hasNext() {
				return (pos != end);
			}

			@Override
			public E next() {
				if (hasNext()){
					E retVal= array[pos];
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
