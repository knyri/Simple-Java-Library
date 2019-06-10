package simple.concurrent;

public interface WorkerDataPool<T>{
	public T getNext();
	public boolean putBack(T item);
}
