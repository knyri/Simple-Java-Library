package simple.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ImmutableSet<E> implements Set<E> {
	private final E[] data;
	private Iterable<E> iterator= null;
	public ImmutableSet(E[] data){
		this.data= data;
	}
	@SuppressWarnings("unchecked")
	public ImmutableSet(Set<E> data){
		this.data= (E[])data.toArray();
	}
	@SuppressWarnings("unchecked")
	public ImmutableSet(List<E> data){
		this.data= (E[])data.toArray();
	}
	@Override
	public boolean add(E v) {
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends E> v) {
		return false;
	}

	@Override
	public void clear(){}

	@Override
	public boolean contains(Object v) {
		for(E cur: data){
			if(cur == v || cur.equals(v)){
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> v) {
		for(Object cur: v){
			if(!contains(cur)){
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isEmpty() {
		return data.length == 0;
	}

	@Override
	public Iterator<E> iterator() {
		if(iterator == null){
			iterator= new ArrayIterator<E>(data);
		}
		return iterator.iterator();
	}

	@Override
	public boolean remove(Object v) {
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> v) {
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> v) {
		return false;
	}

	@Override
	public int size() {
		return data.length;
	}

	@Override
	public Object[] toArray() {
		return toArray(new Object[data.length]);
	}

	@Override
	public <T> T[] toArray(T[] arr) {
		System.arraycopy(data, 0, arr, 0, data.length);
		return arr;
	}

}
