package simple.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
//import java.util.Vector;

/**
 * Provides a First in First Out stack.
 * <br>Created: 2008
 * @param <E>
 * @deprecated Backed by a linked list; which is a queue
 */
@Deprecated
public class Queue<E> implements java.util.Queue<E> {
	private LinkedList<E> list = new LinkedList<E>();
	@Override
	public E peek() {
		return list.peek();
	}
	public E peek(int i){
		return list.get(i);
	}
	@Override
	public boolean add(E o) {
		return list.add(o);
	}
	@Override
	public boolean contains(Object o) {
		return list.contains(o);
	}
	@Override
	public E element() {
		return list.getFirst();
	}
	@Override
	public Iterator<E> iterator() {
		return list.iterator();
	}
	@Override
	public boolean offer(E o) {
		return list.offer(o);
	}
	@Override
	public E poll() {
		return list.pollFirst();
	}
	@Override
	public E remove() {
		return list.removeFirst();
	}
	@Override
	public boolean remove(Object o) {
		return list.remove(o);
	}
	@Override
	public int size() {
		return list.size();
	}
	@Override
	public boolean addAll(Collection<? extends E> c) {
		return list.addAll(c);
	}
	@Override
	public void clear() {
		list.clear();
	}
	@Override
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}
	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}
	@Override
	public boolean removeAll(Collection<?> c) {
		return list.removeAll(c);
	}
	@Override
	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}
	@Override
	public Object[] toArray() {
		return list.toArray();
	}
	@Override
	public <T> T[] toArray(T[] arr) {
		return list.toArray(arr);
	}
}
