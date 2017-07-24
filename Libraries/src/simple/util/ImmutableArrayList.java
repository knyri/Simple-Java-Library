package simple.util;

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
		return false;
	}
	@Override
	public void add(int idx, E el) {
		throw new UnsupportedOperationException();
	}
	@Override
	public boolean addAll(Collection<? extends E> col) {
		return false;
	}
	@Override
	public boolean addAll(int idx, Collection<? extends E> col) {
		return false;
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
			if(this.contains(cur)){
				return true;
			}
		}
		return false;
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
			iterator=  new ArrayIterator<>(list);
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
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ListIterator<E> listIterator(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean remove(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public E remove(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean removeAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean retainAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public E set(int arg0, E arg1) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public List<E> subList(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public <T> T[] toArray(T[] arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
