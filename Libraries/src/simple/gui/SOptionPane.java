package simple.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import simple.gui.container.ConfigLayoutPane;
import simple.gui.factory.SwingFactory;
import simple.util.App;

/**Using an underlying ConfigLayoutPane it adds the supplied labels and fields.<br>
 * Blocks until the window is closed or until a button is pressed.<br>
 * The pressed button is returned.<br>
 * Depends on:<br>
 * simple.gui.ConfigLayoutPane<br>
 * simple.gui.factory.SwingFactory<br>
 * simple.util.App(simple.util.logging)
 * <br>Created: ??
 * @author Kenneth Pierce
 */
public final class SOptionPane {
	public static final int B_OK = 1, B_NO = 2, B_YES = 4, B_CANCEL = 8,
		B_YESNO = 6, B_OKCANCEL = 9;
	public static final int WINDOW_CLOSED = -1;
	public static final int showInputDialog(final JFrame owner, final String title, final int buttons, final String[] labels, final JComponent[] fields) {
		final SDialog frame = new SDialog(owner, title, true);
		final ConfigLayoutPane pane = new ConfigLayoutPane(ConfigLayoutPane.CENTER);
		frame.addCenter(pane);
		for (int i = 0; i < labels.length; i++) {
			pane.addLeft(new JLabel(labels[i]));
		}
		for (int i = 0; i < fields.length; i++) {
			pane.addRight(fields[i]);
		}
		final ButtonListener listener = new ButtonListener(frame);
		frame.addBottom(getButtons(listener, buttons));
		frame.pack();
		frame.center();
		frame.setVisible(true);
		synchronized (listener) {
//			try {
				System.out.println("Waiting");
//				listener.wait();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
		System.out.println("Returning");
		return listener.button;
	}
	private static JPanel getButtons(final ButtonListener listener, final int options) {
		final JPanel pane = new JPanel();
		if (App.isSet(options, B_OK)) {
			pane.add(SwingFactory.makeJButton("OK", "ok", listener));
		} else if (App.isSet(options, B_CANCEL)) {
			pane.add(SwingFactory.makeJButton("Cancel", "cancel", listener));
		} else if (App.isSet(options, B_YES)) {
			pane.add(SwingFactory.makeJButton("Yes", "yes", listener));
		} else if (App.isSet(options, B_NO)) {
			pane.add(SwingFactory.makeJButton("No", "no", listener));
		}
		return pane;
	}
	private static class ButtonListener implements ActionListener {
		public int button = -1;
		public final SDialog dialog;
		public ButtonListener(final SDialog window) {
			dialog = window;
		}
		public void actionPerformed(final ActionEvent e) {
			final String cmd = e.getActionCommand();
			if ("ok".equals(cmd)) {
				button = B_OK;
			} else if ("no".equals(cmd)) {
				button = B_NO;
			} else if ("yes".equals(cmd)) {
				button = B_YES;
			} else if ("cancel".equals(cmd)) {
				button = B_CANCEL;
			}
			dialog.setVisible(false);
			synchronized(this) {
				this.notify();
			}
		}

	}
}
