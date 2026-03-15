package simple.concurrent;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AbstractMapWorkerPool<K,D> implements MapWorkerPool<K, D>, Runnable{
	private final ConcurrentHashMap<K, BlockingDeque<D>> pool= new ConcurrentHashMap<>();
	private final MapWorker<K, D> worker;
	private final ThreadPoolExecutor threadPool;
	private final BlockingQueue<K> keys= new LinkedBlockingQueue<>();
	private volatile boolean done= false;
	protected final int threadCount;
	public AbstractMapWorkerPool(MapWorker<K, D> worker, int threadCount){
		this.worker= worker;
		this.threadCount= threadCount;
		threadPool= new ThreadPoolExecutor(threadCount, threadCount, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(threadCount));
	}
	public AbstractMapWorkerPool(MapWorker<K, D> worker, int threadCount, Map<K, Collection<D>> items){
		this(worker, threadCount);
		addAll(items);
	}
	protected K nextQueueKey(){
		if(keys.isEmpty()){
			keys.addAll(getKeys());
		}
		return keys.poll();
	}
	@Override
	public void addAll(Map<K, Collection<D>> items){
		for(K k: items.keySet()){
			getQueue(k).addAll(items.get(k));
		}
	}
	public void removeAll(K key){
		pool.remove(key);
	}

	@Override
	public Set<K> getKeys(){
		return pool.keySet();
	}

	@Override
	public boolean addItem(K key,D item){
		return getQueue(key).offer(item);
	}

	@Override
	public boolean putItem(K key, D item, long timeout, TimeUnit unit) throws InterruptedException{
		return getQueue(key).offer(item, timeout, unit);
	}

	@Override
	public boolean removeItem(K key, D item){
		BlockingDeque<D> q= pool.get(key);
		if(q != null){
			return q.remove(item);
		}
		return false;
	}

	@Override
	public boolean hasItem(K key, D item){
		BlockingDeque<D> q= pool.get(key);
		if(q != null){
			return q.contains(item);
		}
		return false;
	}

	@Override
	public int getItemsRemaining(){
		return pool.size();
	}

	@Override
	public int getItemsRemaining(K key){
		BlockingDeque<D> q= pool.get(key);
		if(q != null){
			return q.size();
		}
		return 0;
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

	public abstract static class MapWorker<K, D> implements Runnable{
		public static class WorkEntry<K, D>{
			public final K key;
			public final D data;
			public WorkEntry(K k, D d){
				this.key= k;
				this.data= d;
			}
		}
		private volatile boolean stopped= false;
		public void stop(){
			this.stopped= true;
		}
		public boolean isStopped(){
			return stopped;
		}
		private AbstractMapWorkerPool<K, D> pool;
		private K currentQueue;
		private final boolean helpOtherQueues;
		protected AbstractMapWorkerPool<K, D> getWorkerPool(){
			return pool;
		}
		protected K getCurrentQueueKey(){
			return currentQueue;
		}
		protected MapWorker(boolean helpOtherQueues){
			this.helpOtherQueues= helpOtherQueues;
		}
		protected void setWorkerPool(AbstractMapWorkerPool<K, D> pool){
			this.pool= pool;
			this.currentQueue= pool.nextQueueKey();
		}
		protected WorkEntry<K, D> nextEntry(){
			D next= pool.nextItem(currentQueue);
			if(next == null && helpOtherQueues){
				K nextKey= pool.nextQueueKey();
				if(nextKey == null){
					return null;
				}
				next= pool.nextItem(nextKey);
				if(next == null){
					return null;
				}
				return new WorkEntry<>(nextKey, next);
			}
			if(next == null){
				return null;
			}
			return new WorkEntry<>(currentQueue, next);
		}
		protected boolean putBack(WorkEntry<K, D> data){
			return pool.addItem(data.key, data.data);
		}
	}
	protected final D nextItem(K queue){
		BlockingDeque<D> q= pool.get(queue);
		if(null != q){
			return q.poll();
		}
		return null;
	}
	protected BlockingDeque<D> getQueue(K queue){
		BlockingDeque<D> q= pool.get(queue);
		if(q == null){
			q= pool.putIfAbsent(queue, new LinkedBlockingDeque<>());
		}
		return q;
	}
	@Override
	public boolean isRunning(K key){
		throw new UnsupportedOperationException();
	}
	@Override
	public boolean isDone(K key){
		throw new UnsupportedOperationException();
	}
}
