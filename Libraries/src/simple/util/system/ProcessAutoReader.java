package simple.util.system;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import simple.io.FileUtil;

public class ProcessAutoReader{
	private final OutputStream sout, eout;
	private final Process p;
	private final int bufferSize;
	public ProcessAutoReader(Process p, OutputStream stdout, OutputStream errout, int bufferSize){
		this.p= p;
		sout= stdout;
		eout= errout;
		this.bufferSize= bufferSize;
	}
	public ProcessAutoReader(Process p, OutputStream stdout, OutputStream errout){
		this(p, stdout, errout, 4096);
	}

	/**
	 * Starts two copy threads. Non-blocking
	 */
	public void start(){
		new Thread(new Runnable(){@Override
			public void run(){
				try(InputStream sIn= p.getInputStream()){
					FileUtil.copy(sIn, sout, bufferSize);
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}).start();
		new Thread(new Runnable(){@Override
			public void run(){
				try(InputStream eIn= p.getErrorStream()){
					FileUtil.copy(eIn, eout, bufferSize);
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}).start();
	}

}
