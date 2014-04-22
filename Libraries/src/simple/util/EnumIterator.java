package simple.util;

import java.util.Enumeration;
import java.util.Iterator;

public class EnumIterator<T> implements Iterable<T>{
	private final Enumeration<T> enumer;
	public EnumIterator(Enumeration<T> en){
		enumer=en;
	}
	@Override
	public Iterator<T> iterator(){
		return new Iterator<T>(){
			@Override
			public boolean hasNext(){
				return enumer.hasMoreElements();
			}
			@Override
			public T next(){
				return enumer.nextElement();
			}
			@Override
			public void remove(){	}
		};
	}

}
