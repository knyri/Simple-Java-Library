package simple.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * Pool of worker threads.
 * @author Ken Pierce
 *
 * @param <T>
 */
public interface WorkerPool<T>{
	/**
	 * Attempts to add the item. Does not wait for space.
	 * @param item
	 * @return
	 */
	public boolean addItem(T item);
	/**
	 * Adds the item to the queue, waiting if needed. Default calls putItem(item, 0, null)
	 * @param item
	 * @throws InterruptedException
	 */
	public default void putItem(T item) throws InterruptedException{
		putItem(item, 0, null);
	}
	/**
	 * Adds the item to the queue, waiting up to [amount]
	 * @param item
	 * @param amount
	 * @param unit
	 * @return
	 * @throws InterruptedException
	 */
	public boolean putItem(T item, long amount, TimeUnit unit) throws InterruptedException;
	/**
	 * Attempts to remove the item from the work queue.
	 * @param item
	 * @return false if the item couldn't be removed from the queue.
	 */
	public boolean removeItem(T item);
	/**
	 * True if the item is in the work queue
	 * @param item
	 * @return
	 */
	public boolean hasItem(T item);
	/**
	 * Items remaining in the work queue
	 * @return
	 */
	public int getItemsRemaining();
	/**
	 * Waits at most timeout units for this pool to finish
	 * @param timeout
	 * @param unit
	 * @throws InterruptedException
	 */
	public void waitFor(long timeout, TimeUnit unit) throws InterruptedException;
	/**
	 * Waits for this pool to finish. Calls waitFor(0, null)
	 * @throws InterruptedException
	 */
	public default void waitFor() throws InterruptedException{
		waitFor(0, null);
	}
	public int getActiveThreadCount();
	public boolean isRunning();
	public boolean isDone();
	public void stop();
	public void stopNow();
	public boolean isStopping();
}
