package simple.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class ArrayQueue<E> implements Queue<E>{
	private E[] ary;
	private int start= 0, end;
	public ArrayQueue(E[] ary){
		this.ary= ary;
		end= ary.length;
	}

	@Override
	public boolean addAll(Collection<? extends E> c){
		if(c.size() == 0){
			return false;
		}
		if(start != 0){
			System.arraycopy(ary, start, ary, 0, end - start);
			end= end - start;
			start= 0;
		}
		if(end + c.size() >= ary.length){
			Object[] tmp= new Object[(int)Math.ceil((end + c.size()) * 1.33)];
			System.arraycopy(ary, start, tmp, 0, end - start);
			ary= (E[])tmp;
		}
		for(E cur: c){
			if(!add(cur)){
				return false;
			}
		}
		return true;
	}

	@Override
	public void clear(){
		start= end= 0;
	}

	@Override
	public boolean contains(Object o){
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c){
		return false;
	}

	@Override
	public boolean isEmpty(){
		return start == end;
	}

	@Override
	public Iterator<E> iterator(){
		return new ArrayIterator<E>(Arrays.copyOfRange(ary, start, end)).iterator();
	}

	@Override
	public boolean remove(Object o){
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
	public int size(){
		return end - start;
	}

	@Override
	public Object[] toArray(){
		return Arrays.copyOfRange(ary, start, end);
	}

	@Override
	public <T> T[] toArray(T[] a){
		return null;
	}

	@Override
	public boolean add(E v){
		if(end == ary.length){
			if(start == 0){
				Object[] tmp= new Object[(int)Math.ceil(ary.length * 1.33)];
				System.arraycopy(ary, start, tmp, 0, end - start);
				ary= (E[])tmp;
			}else{
				System.arraycopy(ary, start, ary, 0, end - start);
				end= end - start;
				start= 0;
			}
		}
		ary[end++]= v;
		return true;
	}


	@Override
	public E element(){
		if(start == end) {throw new NoSuchElementException();}
		return ary[start];
	}

	@Override
	public boolean offer(E v){
		return add(v);
	}

	@Override
	public E peek(){
		return (start == end) ? null : ary[start];
	}

	@Override
	public E poll(){
		return (start == end) ? null : ary[start++];
	}

	@Override
	public E remove(){
		if(start == end) {throw new NoSuchElementException();}
		return ary[start++];
	}

}
