package simple.concurrent;

import java.util.Map;
import java.util.Queue;

import simple.collections.ArrayQueue;

public class MapWorkerPool<K, V> extends WorkerPool<MapWorkerPool.WorkerEntry<K,V>>{
	protected final Queue<K> keys;
	protected final Map<K, V> pool;
	private final int poolSize;
	/**
	 * @param worker The worker that will do the work
	 * @param pool The pool of items to work on
	 * @param threads Thread count
	 */
	public MapWorkerPool(Worker<K, V> worker, Map<K, V> pool, int threads) {
		super(worker, threads);
		this.pool= pool;
		poolSize= pool.size();
		keys= new ArrayQueue<K>((K[])pool.keySet().toArray());
		worker.setPool(new MapWorkerDataPool<K, V>(keys, pool));
	}
	/**
	 * Thread count will be {@linkplain java.lang.Runtime#availableProcessors()}.
	 * @param worker The worker that will do the work
	 * @param pool The pool of items to work on
	 */
	public MapWorkerPool(Worker<K, V> worker, Map<K, V> pool) {
		this(worker, pool, Runtime.getRuntime().availableProcessors());
	}
	/**
	 * Items left in the pool
	 * @return
	 */
	@Override
	public int getItemsRemaining(){
		return keys.size();
	}
	/**
	 * Does the dirty work
	 */
	public static abstract class Worker<K, V> extends WorkerPoolWorker<WorkerEntry<K, V>> {
		private final WorkerEntry<K,V> entry= new WorkerEntry<K,V>();
		/**
		 * @return The next Object to work on or null
		 */
		@SuppressWarnings("unchecked")
		@Override
		protected final WorkerEntry<K, V> getNext(){
			return ((MapWorkerDataPool<K, V>)getDataPool()).getNext(entry);
		}
	}
	public static class WorkerEntry<K, V>{
		public K key= null;
		public V value= null;
		public K key(){return key;}
		public V value(){return value;}
	}
	static class MapWorkerDataPool<K, V> implements WorkerDataPool<WorkerEntry<K, V>>{
		protected final Queue<K> keys;
		protected final Map<K, V> data;
		public MapWorkerDataPool(Queue<K> keys, Map<K, V> data){
			this.keys= keys;
			this.data= data;
		}
		public WorkerEntry<K, V> getNext(WorkerEntry<K, V> entry){
			synchronized(keys){
				if(keys.isEmpty()){
					return null;
				}
				entry.key= keys.poll();
				entry.value= data.get(entry.key);
			}
			return entry;
		}
		@Override
		public WorkerEntry<K, V> getNext(){
			return getNext(new WorkerEntry<K, V>());
		}

		@Override
		public boolean putBack(WorkerEntry<K, V> entry){
			synchronized(keys){
				return keys.add(entry.key);
			}
		}

	}
	@Override
	public int getTotalItems(){
		return poolSize;
	}

}
