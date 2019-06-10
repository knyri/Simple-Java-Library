package simple.concurrent;

public abstract class WorkerPoolWorker<T> implements Runnable{
	private volatile boolean stopped= false;
	private WorkerDataPool<T> dataPool= null;
	private WorkerPool<T> workerPool;
	/**
	 * Sets the stop flag
	 */
	public void stop(){
		stopped= true;
	}
	public boolean isStopped(){
		return stopped;
	}
	protected WorkerDataPool<T> getDataPool(){
		return dataPool;
	}
	protected WorkerPool<T> getWorkerPool(){
		return workerPool;
	}
	/**
	 * Sets the worker pool. Optional. Used to let workers get extra information
	 * @param pool
	 */
	public final void setWorkerPool(WorkerPool<T> pool){
		workerPool= pool;
	}
	/**
	 * Sets the data pool
	 * @param pool
	 */
	public final void setPool(WorkerDataPool<T> pool){
		dataPool= pool;
	}
	/**
	 * Optional
	 * @param item
	 * @return
	 */
	protected boolean putBack(T item){
		synchronized(dataPool){
			return dataPool.putBack(item);
		}
	}
	/**
	 * @return The next Object to work on or null
	 */
	protected T getNext(){
		if(stopped){
			return null;
		}
		synchronized(dataPool){
			return dataPool.getNext();
		}
	};
}
