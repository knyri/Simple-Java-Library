/**
 * 
 */
package simple.gui;

import javax.swing.JFrame;
import javax.swing.JProgressBar;


/**
 * <br>Created: Aug 25, 2008
 * @author Kenneth Pierce
 */
public class ProgressBarWindow extends SDialog {

	private static final long serialVersionUID = 1398573989930777657L;
	private final JProgressBar pBar;
	/**
	 * Creates a LogWindow with a progress bar. The progress bar has a minimum
	 * value of 0 and a maximum value of 100.
	 * @param frame
	 * @param title
	 * @param modal
	 */
	public ProgressBarWindow(JFrame frame, String title, boolean modal) {
		this(frame, title, modal, 0, 100);
	}
	public ProgressBarWindow(JFrame frame, String title, boolean modal, int start, int end) {
		super(frame, title, modal);
		pBar = new JProgressBar(start, end);
		addCenter(pBar);
		pBar.setStringPainted(true);
	}
	public JProgressBar getProgressBar() {
		return pBar;
	}
	public int getMax() {
		return pBar.getMaximum();
	}
	public int getMin() {
		return pBar.getMinimum();
	}
	public int getValue() {
		return pBar.getValue();
	}
	public void setMax(int max) {
		pBar.setMaximum(max);
	}
	public void setMin(int min) {
		pBar.setMinimum(min);
	}
	public void setValue(int v) {
		pBar.setValue(v);
	}
	public void setString(String s) {
		pBar.setString(s);
	}
}
