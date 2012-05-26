package simple.util;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
//import java.util.Vector;

/**
 * Provides a Last in Last Out stack.
 * <br>Created: 2008
 * @author KP
 * @param <E>
 */
public class Queue<E> implements Deque<E> {
	private LinkedList<E> list = new LinkedList<E>();
	public void push(E o) {
		addFirst(o);
	}
	public E pop() {
		if (size()==0) return null;
		return list.removeFirst();
	}
	public E peek() {
		if (list.size()==0) return null;
		return list.getFirst();
	}
	public boolean add(E o) {
		return list.add(o);
	}
	public void addFirst(E o) {
		list.push(o);
	}
	public void addLast(E o) {
		list.add(o);
	}
	public boolean contains(Object o) {
		return list.contains(o);
	}
	public Iterator<E> descendingIterator() {
		return list.descendingIterator();
	}
	public E element() {
		return list.getFirst();
	}
	public E getFirst() {
		return list.getFirst();
	}
	public E getLast() {
		return list.getLast();
	}
	public Iterator<E> iterator() {
		return list.iterator();
	}
	public boolean offer(E o) {
		return list.offer(o);
	}
	public boolean offerFirst(E o) {
		return list.offerFirst(o);
	}
	public boolean offerLast(E o) {
		return list.offerLast(o);
	}
	public E peekFirst() {
		return list.getFirst();
	}
	public E peekLast() {
		return list.getLast();
	}
	public E poll() {
		return list.remove(0);
	}
	public E pollFirst() {
		return list.remove(0);
	}
	public E pollLast() {
		return list.remove(list.size());
	}
	public E remove() {
		return list.remove();
	}
	public boolean remove(Object o) {
		return list.remove(o);
	}
	public E removeFirst() {
		return list.remove();
	}
	public boolean removeFirstOccurrence(Object o) {
		return list.removeFirstOccurrence(o);
	}
	public E removeLast() {
		return list.removeLast();
	}
	public boolean removeLastOccurrence(Object o) {
		list.remove(list.lastIndexOf(o));
		return true;
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
