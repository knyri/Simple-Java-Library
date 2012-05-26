package simple.gui.logging;

import javax.swing.JFrame;
import javax.swing.JProgressBar;


/** A LogWindow with a progress bar.
 * <br>Created: 2007
 * @author Kenneth Pierce
 * @see simple.gui.logging.LogWindow
 */
public class ProgressWindow extends LogWindow {

	private static final long serialVersionUID = 1398573989930777657L;
	private final JProgressBar pBar;
	/**
	 * Creates a LogWindow with a progress bar. The progress bar has a minimum
	 * value of 0 and a maximum value of 100.
	 * @param frame
	 * @param title
	 * @param modal
	 */
	public ProgressWindow(JFrame frame, String title, boolean modal) {
		this(frame, title, modal, 0, 100);
	}
	public ProgressWindow(JFrame frame, String title, boolean modal, int start, int end) {
		super(frame, title, modal);
		pBar = new JProgressBar(start, end);
		addBottom(pBar);
		pBar.setStringPainted(true);
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
