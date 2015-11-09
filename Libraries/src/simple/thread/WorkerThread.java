/**
 *
 */
package simple.thread;

import java.util.List;

/**
 * @author Ken Pierce
 *
 */
public abstract class WorkerThread<T> implements Runnable {
	private final List<T> queue;
	/**
	 * @param queue
	 */
	public WorkerThread(List<T> queue) {
		this.queue= queue;
	}

	@Override
	public abstract void run();
	/**
	 * Removes and returns the item on the top of the queue.<br>
	 * Returns null if the list is empty.
	 * @return The next item or null
	 */
	protected final T getNext(){
		synchronized(queue){
			if(queue.isEmpty()){
				return null;
			}
			return queue.remove(0);
		}
	}
}
