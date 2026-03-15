package simple.concurrent;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Pool with multiple queues
 * @author Ken Pierce
 *
 * @param <K>
 * @param <D>
 */
public interface MapWorkerPool<K,D>{
	public void addAll(Map<K, Collection<D>> items);
	public Set<K> getKeys();
	/**
	 * Attempts to add item to the work queue
	 * @param key
	 * @param item
	 * @return
	 */
	public boolean addItem(K key, D item);
	/**
	 * Put the item in the work queue, Waiting if needed.
	 * @param key
	 * @param item
	 * @throws InterruptedException
	 */
	public default void putItem(K key, D item) throws InterruptedException{
		putItem(key, item, 0, null);
	}
	/**
	 * Attempts to put the item in the queue, waiting if needed
	 * @param key
	 * @param item
	 * @param timeout
	 * @param unit
	 * @return
	 * @throws InterruptedException
	 */
	public boolean putItem(K key, D item, long timeout, TimeUnit unit) throws InterruptedException;
	/**
	 * Attempts to remove the item from the queue
	 * @param key
	 * @param item
	 * @return true if the item was removed. Should return false if the item was not present
	 */
	public boolean removeItem(K key, D item);
	/**
	 * @param key
	 * @param item
	 * @return true if the item is in the work queue
	 */
	public boolean hasItem(K key, D item);
	public int getItemsRemaining();
	public int getItemsRemaining(K key);
	/**
	 * Waits for the pool to finish
	 * @param timeout
	 * @param unit
	 * @throws InterruptedException
	 */
	public void waitFor(long timeout, TimeUnit unit) throws InterruptedException;
	/**
	 * Waits for the pool to finish. Default calls waitFor(0, null)
	 * @throws InterruptedException
	 */
	public default void waitFor() throws InterruptedException{
		waitFor(0, null);
	}
	public int getActiveThreadCount();
	public boolean isRunning();
	public boolean isDone();
	public boolean isRunning(K key);
	public boolean isDone(K key);
	public void stop();
	public void stopNow();
	public boolean isStopping();

}
