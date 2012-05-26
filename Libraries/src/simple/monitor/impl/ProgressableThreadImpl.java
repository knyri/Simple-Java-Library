package simple.monitor.impl;

import javax.swing.JProgressBar;

import simple.monitor.ProgressableThread;

/** Simple implementation.
 * <br>Created: Jun 26, 2006
 * @author Kenneth Pierce
 */
public abstract class ProgressableThreadImpl implements ProgressableThread {
	protected final Object waiter = new Object();
	protected int max = 0, min = 0, cur = 0, exit = 0;
	protected boolean running = false;
	protected String mes;
	protected JProgressBar pbar;
	protected ProgressableThreadImpl() {}
	protected ProgressableThreadImpl(int min, int max) {
		setMinimum(min);
		setMaximum(max);
		setCurrentValue(min);
	}
	public void setProgressBar(JProgressBar pbar) {
		pbar.setMaximum(max);
		pbar.setMinimum(min);
		pbar.setValue(cur);
		this.pbar = pbar;
	}
	public JProgressBar getProgressBar() {
		if (pbar == null) {
			pbar = new JProgressBar(min, max);
			pbar.setValue(cur);
		}
		return pbar;
	}
	protected void setMaximum(int val) {
		max = val;
		if (getCurrentValue()>max)
			setCurrentValue(max);
		if (pbar != null)
			pbar.setMaximum(val);
	}
	protected void setMinimum(int val) {
		min = val;
		if (getCurrentValue()<min)
			setCurrentValue(min);
		if (pbar != null)
			pbar.setMinimum(val);
	}
	protected void setCurrentValue(int val) {
		cur = val;
		if (cur>getMaximum()) {
			cur = max;
		} else if (cur<getMinimum()) {
			cur = min;
		}
		if (pbar != null)
			pbar.setValue(val);
	}
	protected void setExitCode(int code) {
		exit = code;
	}
	protected void setMessage(String message) {
		mes = message;
	}
	protected void setRunning(boolean b) {
		running = b;
	}
	protected boolean isRunning() {
		return running;
	}
	protected int getExitCode() {
		return exit;
	}
	public int waitFor() {
		while(isRunning()) {
			synchronized (waiter) {
				try {
					waiter.wait(3000);
				} catch (InterruptedException e) {}
			}
		}
		return exit;
	}

	public int getCurrentValue() {
		return cur;
	}

	public int getMaximum() {
		return max;
	}

	public String getMessage() {
		return mes;
	}

	public int getMinimum() {
		return min;
	}
}
