package simple.util.system;

import simple.io.StreamEater;

public class ProcessEater implements Runnable {

	private final Thread
		outputEater,
		errorEater;
	private final Process proc;
	public ProcessEater(Process process) {
		outputEater= new Thread(new StreamEater(process.getInputStream()));
		errorEater= new Thread(new StreamEater(process.getErrorStream()));
		proc= process;
	}

	@Override
	public void run() {
		outputEater.start();
		errorEater.start();
	}

	public int waitFor() throws InterruptedException{
		int exitVal= proc.waitFor();
		outputEater.join();
		errorEater.join();
		return exitVal;
	}
}
