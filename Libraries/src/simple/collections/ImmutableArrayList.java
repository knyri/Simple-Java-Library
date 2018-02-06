package simple.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ImmutableArrayList<E> implements List<E> {
	private final E[] list;
	private ArrayIterator<E> iterator= null;
	public ImmutableArrayList(E[] ary){
		list= ary;
	}
	@Override
	public boolean add(E el) {
		throw new UnsupportedOperationException();
	}
	@Override
	public void add(int idx, E el) {
		throw new UnsupportedOperationException();
	}
	@Override
	public boolean addAll(Collection<? extends E> col) {
		throw new UnsupportedOperationException();
	}
	@Override
	public boolean addAll(int idx, Collection<? extends E> col) {
		throw new UnsupportedOperationException();
	}
	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}
	@Override
	public boolean contains(Object el) {
		if(el == null){
			for(E cur: list){
				if(cur == el){
					return true;
				}
			}
		}else{
			for(E cur: list){
				if(cur == el || el.equals(cur)){
					return true;
				}
			}
		}
		return false;
	}
	@Override
	public boolean containsAll(Collection<?> col) {
		for(Object cur : col){
			if(!this.contains(cur)){
				return false;
			}
		}
		return true;
	}
	@Override
	public E get(int idx) {
		return list[idx];
	}
	@Override
	public int indexOf(Object el) {
		int idx= 0, end= list.length;
		if(el == null){
			for(; idx < end; idx++){
				if(el == list[idx]){
					return idx;
				}
			}
		}else{
			for(; idx < end; idx++){
				if(el == list[idx] || el.equals(list[idx])){
					return idx;
				}
			}
		}
		return -1;
	}
	@Override
	public boolean isEmpty() {
		return list.length == 0;
	}
	@Override
	public Iterator<E> iterator() {
		if(iterator == null){
			iterator=  new ArrayIterator<E>(list);
		}
		return iterator.iterator();
	}
	@Override
	public int lastIndexOf(Object el) {
		int idx= list.length - 1;
		if(el == null){
		for(; idx > -1; idx--){
				if(el == list[idx]){
					break;
				}
			}
		}else{
			for(; idx > -1; idx--){
				if(el == list[idx] || el.equals(list[idx])){
					break;
				}
			}
		}
		return idx;
	}
	@Override
	public ListIterator<E> listIterator() {
		return listIterator(0);
	}
	@Override
	public ListIterator<E> listIterator(final int start) {
		return new ListIterator<E>(){
			private int next= start;
			@Override
			public void add(E obj) {
				throw new UnsupportedOperationException();
			}

			@Override
			public boolean hasNext() {
				return next < list.length;
			}

			@Override
			public boolean hasPrevious() {
				return next > start;
			}

			@Override
			public E next() {
				return list[next++];
			}

			@Override
			public int nextIndex() {
				return next;
			}

			@Override
			public E previous() {
				return list[(--next) - 1];
			}

			@Override
			public int previousIndex() {
				return next - 2;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

			@Override
			public void set(E newObj) {
				throw new UnsupportedOperationException();
			}

		};
	}
	@Override
	public boolean remove(Object arg0) {
		throw new UnsupportedOperationException();
	}
	@Override
	public E remove(int arg0) {
		throw new UnsupportedOperationException();
	}
	@Override
	public boolean removeAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}
	@Override
	public boolean retainAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}
	@Override
	public E set(int idx, E obj) {
		throw new UnsupportedOperationException();
	}
	@Override
	public int size() {
		return this.list.length;
	}
	@Override
	public List<E> subList(int start, int end) {
		throw new UnsupportedOperationException();
	}
	@Override
	public Object[] toArray() {
		Object[] ret= new Object[list.length];
		System.arraycopy(list, 0, ret, 0, list.length);
		return ret;
	}
	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] ary) {
		if(ary.length >= list.length){
			System.arraycopy(list, 0, ary, 0, list.length);
			return ary;
		}
		return (T[])toArray();
	}

}
