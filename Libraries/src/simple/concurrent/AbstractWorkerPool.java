package simple.concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class AbstractWorkerPool<T> implements WorkerPool<T>, Runnable{
	private volatile boolean done= false;
	private final WorkerPoolWorker<T> worker;
	private final ThreadPoolExecutor threadPool;
	protected final int threadCount;

	protected AbstractWorkerPool(WorkerPoolWorker<T> worker, int threadCount){
		this.worker= worker;
		this.threadCount= threadCount;
		threadPool= new ThreadPoolExecutor(threadCount,threadCount,5,TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(threadCount, true));
		worker.setWorkerPool(this);
	}

	@Override
	public void run(){
		if(threadPool.isShutdown()){
			return;
		}
		for(int i= 0; i < threadCount; i++){
			threadPool.submit(worker);
		}
		threadPool.shutdown();
	}

	@Override
	public void waitFor(long timeout, TimeUnit unit) throws InterruptedException{
		if(isDone()){
			return;
		}
		if(timeout == 0){
			while(!isDone()){
				done= threadPool.awaitTermination(10, TimeUnit.SECONDS);
			}
		}else{
			done= threadPool.awaitTermination(timeout, unit);
		}
	}

	@Override
	public int getActiveThreadCount(){
		return threadPool.getActiveCount();
	}

	@Override
	public boolean isRunning(){
		return threadPool.isTerminating();
	}

	@Override
	public boolean isDone(){
		return done;
	}

	@Override
	public void stop(){
		worker.stop();
	}

	@Override
	public void stopNow(){
		stop();
		threadPool.shutdownNow();
	}

	@Override
	public boolean isStopping(){
		return worker.isStopped();
	}
	protected abstract T nextItem();
	public static abstract class WorkerPoolWorker<T> implements Runnable{
		private volatile boolean stopped= false;
		private AbstractWorkerPool<T> workerPool= null;
		public void stop(){
			this.stopped= true;
		}
		public boolean isStopped(){
			return stopped;
		}
		protected AbstractWorkerPool<T> getWorkerPool(){
			return workerPool;
		}
		public final void setWorkerPool(AbstractWorkerPool<T> pool){
			if(workerPool != null){
				throw new IllegalStateException("Worker already assigned to a worker pool");
			}
			workerPool= pool;
		}
		protected boolean putBack(T item){
			return workerPool.addItem(item);
		}
		protected T getNext(){
			if(stopped){
				return null;
			}
			return workerPool.nextItem();
		}
		@Override
		public abstract void run();
	}
	@Override
	public abstract boolean addItem(T item);
	@Override
	public abstract boolean putItem(T item, long amount, TimeUnit unit) throws InterruptedException;
	@Override
	public abstract boolean removeItem(T item);
	@Override
	public abstract boolean hasItem(T item);
	@Override
	public abstract int getItemsRemaining();

}
