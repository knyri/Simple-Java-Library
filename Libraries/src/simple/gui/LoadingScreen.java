/**
 * 
 */
package simple.gui;

import java.awt.Dimension;

import javax.swing.JProgressBar;
import javax.swing.JWindow;

import simple.gui.container.SimpleBorderLayoutPane;


/**
 * <br>Created: Aug 31, 2008
 * @author Kenneth Pierce
 */
public class LoadingScreen extends JWindow {
	private final JProgressBar pBar = new JProgressBar();
	private static final long serialVersionUID = 1L;
	public LoadingScreen() {
		super();
		this.setAlwaysOnTop(true);
		SimpleBorderLayoutPane layout = new SimpleBorderLayoutPane();
		pBar.setPreferredSize(new Dimension(50, 150));
		layout.addBottom(pBar);
	}
	public void setProgressBarMinimum(int min) {
		pBar.setMinimum(min);
	}
	public void setProgressBarMaximum(int max) {
		pBar.setMaximum(max);
	}
	public void setProgressBarValue(int value) {
		pBar.setValue(value);
	}
	public int getProgressBarMinimum() {
		return pBar.getMinimum();
	}
	public int getProgressBarMaximum() {
		return pBar.getMaximum();
	}
	public int getProgressBarValue() {
		return pBar.getValue();
	}
}
