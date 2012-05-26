/**
 * 
 */
package simple.gui.factory;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**Makes JFrames!<br>
 * Slimmer alternative to {@link simple.gui.factory.SwingFactory}
 * <br>Created: Mar 6, 2009
 * @author Kenneth Pierce
 */
public final class SJFrame {
	protected SJFrame() {}
	/**
	 * Creates a default JFrame of size 300x300 that exits when closed.
	 * 
	 * @return An empty JFrame of size 300x300 that exits when closed.
	 */
	public static JFrame makeDefaultJFrame() {
		JFrame fTmp = new JFrame();
		fTmp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fTmp.setSize(300,300);
		return fTmp;
	}
	/**
	 * Calls {@link #makeDefaultJFrame()} and sets the title.
	 * 
	 * @param title Title for the frame.
	 * @return An empty JFrame with a set title that exits when closed.
	 */
	public static JFrame makeDefaultJFrame(String title) {
		JFrame fTmp = makeDefaultJFrame();
		fTmp.setTitle(title);
		return fTmp;
	}
	/**
	 * Sets up a frame and displays it.
	 * 
	 * Calls the following methods on <var>frame</var>:<br>
	 * <code>
	 * frame.pack();<br>
	 * frame.setLocationRelativeTo(null);<br>
	 * frame.setVisible(true);<br>
	 * </code>
	 * @param frame JFrame to be setup and shown.
	 */
	public static void showFrame(JFrame frame) {
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	public static JFrame makeJFrameAndShow(String title, JPanel content) {
		JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		content.setOpaque(true);
		frame.setContentPane(content);
		showFrame(frame);
		return frame;
	}
}
