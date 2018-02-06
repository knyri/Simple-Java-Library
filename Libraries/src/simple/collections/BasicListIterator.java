package simple.collections;

import java.util.List;
import java.util.ListIterator;

public class BasicListIterator<E> implements ListIterator<E>{
	private final List<E> list;
	private int
		index= -1,
		offset;
	public BasicListIterator(List<E> backingList){
		this(backingList, 0);
	}
	public BasicListIterator(List<E> backingList, int offset){
		list= backingList;
		this.offset= offset;
	}

	@Override
	public void add(E e){
		list.add(index+offset,e);
	}

	@Override
	public boolean hasNext(){
		return (index+offset+1 < list.size());
	}

	@Override
	public boolean hasPrevious(){
		return index > 0;
	}

	@Override
	public E next(){
		index++;
		return list.get(index+offset);
	}

	@Override
	public int nextIndex(){
		return index+1;
	}

	@Override
	public E previous(){
		index--;
		return list.get(index+offset);
	}

	@Override
	public int previousIndex(){
		return index-1;
	}

	@Override
	public void remove(){
		list.remove(index+offset);
	}

	@Override
	public void set(E e){
		list.set(index+offset,e);
	}

}
