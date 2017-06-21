package simple.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Turns a List into a Queue.
 * @param <E>
 */
public class ListQueue<E> implements Queue<E> {
	private final List<E> backingList;
	public ListQueue(List<E> list) {
		this.backingList= list;
	}

	@Override
	public boolean addAll(Collection<? extends E> arg0) {
		return backingList.addAll(arg0);
	}

	@Override
	public void clear() {
		backingList.clear();
	}

	@Override
	public boolean contains(Object arg0) {
		return backingList.contains(arg0);
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		return backingList.containsAll(arg0);
	}

	@Override
	public boolean isEmpty() {
		return backingList.isEmpty();
	}

	@Override
	public Iterator<E> iterator() {
		return backingList.iterator();
	}

	@Override
	public boolean remove(Object arg0) {
		return backingList.remove(arg0);
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		return backingList.removeAll(arg0);
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		return backingList.retainAll(arg0);
	}

	@Override
	public int size() {
		return backingList.size();
	}

	@Override
	public Object[] toArray() {
		return backingList.toArray();
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		return backingList.toArray(arg0);
	}

	@Override
	public boolean add(E arg0) {
		return backingList.add(arg0);
	}

	@Override
	public E element() {
		if(backingList.isEmpty()){
			throw new NoSuchElementException();
		}
		return backingList.get(0);
	}

	@Override
	public boolean offer(E arg0) {
		return backingList.add(arg0);
	}

	@Override
	public E peek() {
		return backingList.get(0);
	}

	@Override
	public E poll() {
		return backingList.isEmpty() ? null : backingList.remove(0);
	}

	@Override
	public E remove() {
		if(backingList.isEmpty()){
			throw new NoSuchElementException();
		}
		return backingList.remove(0);
	}

}
