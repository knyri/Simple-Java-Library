package simple.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class FixedSizeArrayList<E> implements List<E>{
	private final E[] list;
	private final Iterable<E> iterator;
	public FixedSizeArrayList(E[] list){
		this.list= list;
		iterator= IteratorFactory.create(list);
	}

	@Override
	public boolean add(E e){
		return false;
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
		throw new UnsupportedOperationException();
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
		return iterator.iterator();
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
		E ret= list[index];
		list[index]= element;
		return ret;
	}

	@Override
	public int size(){
		return list.length;
	}

	@Override
	public List<E> subList(int fromIndex,int toIndex){
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] toArray(){
		Object[] a= new Object[list.length];
		return toArray(a);
	}

	@Override
	public <T>T[] toArray(T[] a){
		System.arraycopy(list,0,a,0,Math.min(a.length,list.length));
		return a;
	}

}
