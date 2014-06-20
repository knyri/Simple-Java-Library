package simple.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
//import java.util.Vector;

/**
 * Provides a First in First Out stack.
 * <br>Created: 2008
 * @author KP
 * @param <E>
 */
public class Queue<E> implements java.util.Queue<E> {
	private LinkedList<E> list = new LinkedList<E>();
	public E peek() {
		return list.peek();
	}
	public E peek(int i){
		return list.get(i);
	}
	public boolean add(E o) {
		return list.add(o);
	}
	public boolean contains(Object o) {
		return list.contains(o);
	}
	public E element() {
		return list.getFirst();
	}
	public Iterator<E> iterator() {
		return list.iterator();
	}
	public boolean offer(E o) {
		return list.offer(o);
	}
	public E poll() {
		return list.pollFirst();
	}
	public E remove() {
		return list.removeFirst();
	}
	public boolean remove(Object o) {
		return list.remove(o);
	}
	public int size() {
		return list.size();
	}
	public boolean addAll(Collection<? extends E> c) {
		return list.addAll(c);
	}
	public void clear() {
		list.clear();
	}
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}
	public boolean isEmpty() {
		return list.isEmpty();
	}
	public boolean removeAll(Collection<?> c) {
		return list.removeAll(c);
	}
	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}
	public Object[] toArray() {
		return list.toArray();
	}
	public <T> T[] toArray(T[] arr) {
		return list.toArray(arr);
	}
}
