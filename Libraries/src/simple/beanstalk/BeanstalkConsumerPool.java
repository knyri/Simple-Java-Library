package simple.beanstalk;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Easy way to have multiple consumers that do the same thing
 */
public class BeanstalkConsumerPool implements Runnable{
//	private final Logger log= LoggerFactory.getLogger(BeanstalkConsumerPool.class);

	private final BeanstalkConsumer worker;
	private final BeanstalkClientConfig config;
	private boolean done= false;
	private final ThreadPoolExecutor threadPool;
	private final int threadCount;
//	private final Thread[] threads;

	/**
	 * @param worker The worker that will do the work
	 * @param config The default config
	 * @param threads Thread count
	 */
	public BeanstalkConsumerPool(BeanstalkConsumer worker, BeanstalkClientConfig config, int threads) {
		worker.setPool(this);
		this.worker= worker;
		this.config= config;
		threadCount= threads;
		threadPool= new ThreadPoolExecutor(threads, threads, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(threads, true));
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
	 * Waits until all worker threads are complete.
	 * @throws InterruptedException If one of the worker threads is interrupted.
	 */
	public void waitFor() throws InterruptedException{
		waitFor(0);
	}
	/**
	 * @param timeout Time, in milliseconds, to wait for the threads to complete.
	 * 		0 waits forever.
	 * @throws InterruptedException If one of the worker threads is interrupted.
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
	}

	public boolean isRunning(){
		return threadPool.isTerminating();
	}
	public boolean isDone(){
		return done;
	}
	@Deprecated
	public void stop(){
		stop(false);
	}
	public void stop(boolean now){
		worker.stop(now);
	}
	/**
	 * Calls interrupt() on all the workers.
	 */
	public void interruptThreads(){
		worker.stop(true);
		threadPool.shutdownNow();
	}
	/**
	 * Does the dirty work
	 */
	public static abstract class BeanstalkConsumer implements Runnable {
		private volatile boolean isStopped= false;
		protected abstract void stopNow();
		public void stop(boolean now){
			isStopped= true;
			if(now){
				stopNow();
			}
		}
		private BeanstalkConsumerPool pool;
		private void setPool(BeanstalkConsumerPool pool){
			this.pool= pool;
		}

		protected final boolean isStopped(){
			return isStopped;
		}
		/**
		 * @return A beanstalk client
		 * @throws BeanstalkException
		 * @throws IOException
		 */
		protected final BeanstalkClient getClient() throws BeanstalkException, IOException{
			return new BeanstalkClient(pool.config);
		}

	}

}
