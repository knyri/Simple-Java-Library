package simple.concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A pool of worker threads
 * @param <T>
 */
public abstract class WorkerPool<T> implements Runnable{
	private volatile boolean done= false;
	private final WorkerPoolWorker<T> worker;
	protected ThreadPoolExecutor threadPool;
	protected final int threadCount;
	protected WorkerPool(WorkerPoolWorker<T> worker, int threadCount){
		this.worker= worker;
		this.threadCount= threadCount;
		threadPool= new ThreadPoolExecutor(threadCount, threadCount, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(threadCount, true));
		worker.setWorkerPool(this);
	}
	public WorkerPoolWorker<T> getWorker(){
		return worker;
	}
	protected ThreadPoolExecutor getThreadPool(){
		return threadPool;
	}
	@Override
	public void run(){
		if(threadPool.isShutdown()){
			return;
		}
		for (int i= 0; i < threadCount; i++){
			threadPool.submit(worker);
		}
		threadPool.shutdown();
	}
	/**
	 * Number of items in the pool.
	 * @return
	 * @see #getItemsRemaining()
	 * @see #getEffectiveItemsRemaining()
	 */
	public abstract int getTotalItems();
	/**
	 * Items left in the pool + active thread count
	 * A more accurate representation of the work left to be done.
	 * @return
	 */
	public int getEffectiveItemsRemaining(){
		return getItemsRemaining() + getActiveThreadCount();
	}
	/**
	 * Items left in the pool
	 * @return
	 */
	public abstract int getItemsRemaining();
	/**
	 * Waits until all worker threads are complete.
	 * @throws InterruptedException If interrupted while waiting.
	 */
	public void waitFor() throws InterruptedException{
		waitFor(0);
	};
	/**
	 * @param timeout Time, in milliseconds, to wait for the threads to complete.
	 * 		0 waits forever.
	 * @throws InterruptedException If interrupted while waiting
	 */
	public void waitFor(long timeout) throws InterruptedException{
		if(done){
			return;
		}
		if(timeout == 0){
			while(!done){
				done= threadPool.awaitTermination(10, TimeUnit.SECONDS);
			}
		}else{
			done= threadPool.awaitTermination(timeout, TimeUnit.MILLISECONDS);
		}
	};

	/**
	 * Optional. The number of workers doing work. Default returns 0.
	 * @return
	 */
	public int getActiveThreadCount(){
		return threadPool.getActiveCount();
	}
	public boolean isRunning(){
		return threadPool.isTerminating();
	}
	public final boolean isDone(){
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
	}
}
