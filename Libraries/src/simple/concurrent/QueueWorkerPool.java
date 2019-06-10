package simple.concurrent;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import simple.collections.ListQueue;

/**
 * Creates threads equal to the number of available processors to do work
 * by the worker class.
 * Warning: This class treats the pool as a queue and Removes items from
 * the pool. Pass a copy of the pool if you don't want the pool modified.
 * @param <T> The type of the object being worked on.
 */
public class QueueWorkerPool<T> extends WorkerPool<T>{
	private final Queue<T> pool;
	private final int poolSize;

	/**
	 * @param worker The worker that will do the work
	 * @param pool The pool of items to work on
	 * @param threads Thread count
	 */
	public QueueWorkerPool(WorkerPoolWorker<T> worker, Queue<T> pool, int threads) {
		super(worker, threads);
		this.pool= pool;
		worker.setPool(new QueueWorkerDataPool<>(pool));
		poolSize= pool.size();
		threadPool= new ThreadPoolExecutor(threads, threads, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(threads, true));
	}
	/**
	 * Thread count will be {@linkplain java.lang.Runtime#availableProcessors()}.
	 * @param worker The worker that will do the work
	 * @param pool The pool of items to work on
	 */
	public QueueWorkerPool(WorkerPoolWorker<T> worker, Queue<T> pool) {
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
	public int getTotalItems(){
		return poolSize;
	}
	@Override
	public int getItemsRemaining(){
		return pool.size();
	}
	static final class QueueWorkerDataPool<T> implements WorkerDataPool<T>{
		private final Queue<T> data;
		public QueueWorkerDataPool(Queue<T> data){
			this.data= data;
		}
		@Override
		public T getNext(){
			if(data.isEmpty()){
				return null;
			}
			return data.poll();
		}

		@Override
		public boolean putBack(T item){
			return data.add(item);
		}

	}
}
