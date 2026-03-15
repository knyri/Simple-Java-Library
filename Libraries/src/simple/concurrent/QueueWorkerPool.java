package simple.concurrent;

import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import simple.collections.ListQueue;

/**
 * Creates threads equal to the number of available processors to do work
 * by the worker class.
 * Warning: This class treats the pool as a queue and Removes items from
 * the pool. Pass a copy of the pool if you don't want the pool modified.
 * @param <T> The type of the object being worked on.
 */
public class QueueWorkerPool<T> extends AbstractWorkerPool<T>{
	private final BlockingDeque<T> workItems= new LinkedBlockingDeque<>();

	/**
	 * @param worker The worker that will do the work
	 * @param pool The pool of items to work on
	 * @param threads Thread count
	 */
	public QueueWorkerPool(WorkerPoolWorker<T> worker, Collection<T> pool, int threads) {
		super(worker, threads);
		pool.addAll(pool);
	}
	/**
	 * Thread count will be {@linkplain java.lang.Runtime#availableProcessors()}.
	 * @param worker The worker that will do the work
	 * @param pool The pool of items to work on
	 */
	public QueueWorkerPool(WorkerPoolWorker<T> worker, Collection<T> pool) {
		this(worker, pool, Runtime.getRuntime().availableProcessors());
	}
	/**
	 * @param worker The worker that will do the work
	 * @param pool The pool of items to work on
	 * @param threads Thread count
	 */
	public QueueWorkerPool(WorkerPoolWorker<T> worker, List<T> pool, int threads) {
		this(worker, new ListQueue<T>(pool), threads);
	}
	/**
	 * Thread count will be {@linkplain java.lang.Runtime#availableProcessors()}.
	 * @param worker The worker that will do the work
	 * @param pool The pool of items to work on
	 */
	public QueueWorkerPool(WorkerPoolWorker<T> worker, List<T> pool) {
		this(worker, pool, Runtime.getRuntime().availableProcessors());
	}
	@Override
	public int getItemsRemaining(){
		return workItems.size();
	}
	static final class QueueWorkerDataPool<T> implements WorkerDataPool<T>{
		private final Queue<T> data;
		public QueueWorkerDataPool(Queue<T> data){
			this.data= data;
		}
		@Override
		public T getNext(){
			return data.poll();
		}

		@Override
		public boolean putBack(T item){
			return data.add(item);
		}

	}
	@Override
	protected T nextItem(){
		return workItems.poll();
	}
	@Override
	public boolean addItem(T item){
		return workItems.offer(item);
	}
	@Override
	public void putItem(T item) throws InterruptedException{
		workItems.put(item);
	}
	@Override
	public boolean putItem(T item, long amount, TimeUnit unit) throws InterruptedException{
		return workItems.offer(item, amount, unit);
	}
	@Override
	public boolean removeItem(T item){
		return workItems.remove(item);
	}
	@Override
	public boolean hasItem(T item){
		return workItems.contains(item);
	}
}
