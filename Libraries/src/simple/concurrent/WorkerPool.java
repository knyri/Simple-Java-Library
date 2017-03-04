package simple.concurrent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Creates threads equal to the number of available processors to do work
 * by the worker class.
 * Warning: This class treats the pool as a queue and Removes items from
 * the pool. Pass a copy of the pool if you don't want the pool modified.
 * @param <T> The type of the object being worked on.
 */
public class WorkerPool<T> implements Runnable{
	private final Worker<T> worker;
	private final List<T> pool;
//	private boolean running= false,done= false;
	private boolean done= false;
	private final ThreadPoolExecutor threadPool;
	private final int threadCount;
//	private final Thread[] threads;
	private final int poolSize;

	/**
	 * @param worker The worker that will do the work
	 * @param pool The pool of items to work on
	 * @param threads Thread count
	 */
	public WorkerPool(Worker<T> worker, List<T> pool, int threads) {
		this.worker= worker;
		this.pool= Collections.synchronizedList(pool);
		worker.setPool(this);
//		this.threads= new Thread[threads];
		threadCount= threads;
		poolSize= pool.size();
		threadPool= new ThreadPoolExecutor(threads, threads, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(threads, true));
	}
	/**
	 * Thread count will be {@linkplain java.lang.Runtime#availableProcessors()}.
	 * @param worker The worker that will do the work
	 * @param pool The pool of items to work on
	 */
	public WorkerPool(Worker<T> worker, List<T> pool) {
		this(worker, pool, Runtime.getRuntime().availableProcessors());
	}
	@Override
	public void run(){
		if(threadPool.isShutdown()){
			return;
		}
//		running= true;
		for (int i= 0; i < threadCount; i++){
			threadPool.submit(worker);
		}
		threadPool.shutdown();
/*
		for (int i= 0; i < threads.length; i++){
			threads[i]= new Thread(worker);
			threads[i].start();
		}
		activeThreads= threads.length;
		lastCount= System.currentTimeMillis();
/**/
	}
	public int getTotal(){
		return poolSize;
	}
	/**
	 * Waits until all worker threads are complete.
	 * @throws InterruptedException If interrupted while waiting.
	 */
	public void waitFor() throws InterruptedException{
		waitFor(0);
	}
	/**
	 * @param timeout Time, in milliseconds, to wait for the threads to complete.
	 * 		0 waits forever.
	 * @throws InterruptedException If interrupted while waiting
	 */
	public void waitFor(long timeout) throws InterruptedException{
		if(done){
			return;
		}
/*
		if(!running){
			return;
		}
		running= true;
*/
		if(timeout == 0){
			while(!done){
				done= threadPool.awaitTermination(10, TimeUnit.SECONDS);
			}
		}else{
			done= threadPool.awaitTermination(timeout, TimeUnit.MILLISECONDS);
		}
/*
		final Object waiter= new Object();
		long time= System.currentTimeMillis();
		boolean wait;
		int i;
		do{
			try{
				synchronized(waiter){
					waiter.wait(1000);
				}
			}catch(InterruptedException e){}
			if(timeout > 0 && (System.currentTimeMillis() - time) > timeout){
				return;
			}
			wait= false;
			for (i= 0; i < threads.length; i++){
				if(threads[i].isInterrupted()){
					running= false;
					throw new InterruptedException("A worker thread was interrupted.");
				}
				if (threads[i].isAlive()){
					wait= true;
				}
			}
		}while(wait);
/**/
//		running= false;
//		done= true;
	}
	public int threadCount(){
		return threadPool.getActiveCount();
	}
//	private long lastCount= 0;
//	private int activeThreads= 0;
	public int getActiveThreads(){
		return threadCount();
/*
		if(pool.size() < threads.length && 5000 < (System.currentTimeMillis() - lastCount)){
			lastCount= System.currentTimeMillis();
			int count= 0;
			for(Thread t : threads){
				if(t.isAlive()){
					count++;
				}
			}
			activeThreads= count;
		}
		return activeThreads;
/**/
	}
	/**
	 * Items left in the pool + active thread count
	 * A more accurate representation of the work left to be done.
	 * @return
	 */
	public int getEffectiveItemsRemaining(){
		return getActiveThreads() + pool.size();
	}
	/**
	 * Items left in the pool
	 * @return
	 */
	public int itemsRemaining(){
		return pool.size();
	}
	public boolean isRunning(){
		return threadPool.isTerminating();
/*
		for(Thread t: threads){
			if(t.isAlive()){
				return true;
			}
		}
		return false;
/**/
	}
	public boolean isDone(){
		return done;
	}
	/**
	 * Sets the stop flag to signal the workers to stop gracefully.
	 * Unless the stop flag is ignored.
	 */
	public void stop(){
		worker.stop();
	}
	/**
	 * Sets the stop flag and calls interrupt() on all the workers.
	 */
	public void interruptThreads(){
		worker.stop();
		threadPool.shutdownNow();
/*
		for (int i= 0; i < threads.length; i++){
			if(!threads[i].isInterrupted()){
				threads[i].interrupt();
			}
		}
/**/
	}
	/**
	 * Does the dirty work
	 */
	public static abstract class Worker<T> implements Runnable {
		private volatile boolean stop= false;
		/**
		 * Sets the stop flag
		 */
		public void stop(){
			stop= true;
		}
		public boolean isStopped(){
			return stop;
		}
		private WorkerPool<T> pool= null;
		private void setPool(WorkerPool<T> pool){
			this.pool= pool;
		}
		protected final int getRemaining(){
			return pool.itemsRemaining();
		}
		protected final int getTotal(){
			return pool.poolSize;
		}
		protected final int getThreadCount(){
			return pool.threadCount();
		}
		protected final void putBack(T item){
			synchronized(pool.pool){
				pool.pool.add(item);
			}
		}
		/**
		 * @return The next Object to work on or null
		 */
		protected final T getNext(){
			synchronized(pool.pool){
				if(stop || pool.pool.isEmpty()){
					pool.done = true;
					return null;
				}
				return pool.pool.remove(pool.pool.size()-1);
			}
		}

	}
}
