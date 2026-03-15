package simple.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import simple.util.do_array;

public class FixedSizeArrayList<E> implements List<E>{
	private final E[] list;
	private final Iterable<E> iterator;
	private int idx= 0;
	public FixedSizeArrayList(E[] list){
		idx= list.length;
		this.list= list;
		iterator= IteratorFactory.create(list);
	}
	@SuppressWarnings("unchecked")
	public FixedSizeArrayList(int size){
		// list is guaranteed to be only E
		this.list= (E[])new Object[size];
		iterator= IteratorFactory.create(list);
	}

	@Override
	public boolean add(E e){
		if(idx == list.length) return false;
		list[idx++]= e;
		return true;
	}

	@Override
	public void add(int index,E element){
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends E> c){
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(int index,Collection<? extends E> c){
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear(){
		idx= 0;
	}

	@Override
	public boolean contains(Object o){
		return do_array.indexOf(list,o) != -1;
	}

	@Override
	public boolean containsAll(Collection<?> c){
		for(Object o: c){
			if(indexOf(o) == -1){
				return false;
			}
		}
		return true;
	}

	@Override
	public E get(int index){
		return list[index];
	}

	@Override
	public int indexOf(Object o){
		return do_array.indexOf(list,o);
	}

	@Override
	public boolean isEmpty(){
		return list.length == 0;
	}

	@Override
	public Iterator<E> iterator(){
		if(list.length == idx){
			return iterator.iterator();
		}else{
			// not the best, but simple
			return IteratorFactory.create(Arrays.copyOf(list,idx)).iterator();
		}
	}

	@Override
	public int lastIndexOf(Object o){
		return do_array.lastIndexOf(list,o);
	}

	@Override
	public ListIterator<E> listIterator(){
		return new BasicListIterator<E>(this);
	}

	@Override
	public ListIterator<E> listIterator(int index){
		return new BasicListIterator<E>(this, index);
	}

	@Override
	public boolean remove(Object o){
		throw new UnsupportedOperationException();
	}

	@Override
	public E remove(int index){
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c){
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c){
		throw new UnsupportedOperationException();
	}

	@Override
	public E set(int index,E element){
		if (-1 < idx || index == list.length){
			throw new IndexOutOfBoundsException(index);
		}
		E ret= list[index];
		list[index]= element;
		return ret;
	}

	@Override
	public int size(){
		return idx;
	}

	@Override
	public List<E> subList(int fromIndex,int toIndex){
		return new FixedSizeArrayList<E>(Arrays.copyOfRange(list, fromIndex, toIndex));
	}

	@Override
	public Object[] toArray(){
		Object[] a= new Object[list.length];
		return toArray(a);
	}

	@Override
	public <T>T[] toArray(T[] a){
		System.arraycopy(list,0,a,0,Math.min(a.length,idx));
		return a;
	}

}
