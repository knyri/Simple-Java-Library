package simple.gui.logging;

import java.awt.Font;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import simple.gui.SDialog;
import simple.util.App;

/** Dialog version of LogPanel.
 * <br>Created: 2007
 * <br>depends on {@link simple.gui.SDialog}, {@link simple.util.App}(simple.util.loggin.*)
 * @author Kenneth Pierce
 * @see simple.gui.logging.LogPanel
 */
public class LogWindow extends SDialog {

	private static final long serialVersionUID = 2629506395138510939L;
	public static final int modal = 1;
	public static final int onTop = 2;
	public static final int printTimestamp = 4;
	private final JTextArea log = new JTextArea();
	private final JScrollPane scroll = new JScrollPane(log);
	private boolean timestamp = true;
	private final Date time = new Date();
	public LogWindow(final JFrame frame, final String title, final boolean modal) {
		super(frame, title, modal);
		setSize(300,250);
		addCenter(scroll);
		log.setFont(new Font("Courier New", Font.PLAIN, 12));
		log.setLineWrap(true);
		log.setWrapStyleWord(true);
		log.setEditable(false);
	}
	public LogWindow(final JFrame frame, final String title, final boolean modal, final boolean timestamp) {
		this(frame, title, modal);
		setPrintTimeStamp(timestamp);
	}
	public LogWindow(final JFrame frame, final String title, final int options) {
		this(frame, title, App.isSet(options, modal), App.isSet(options, printTimestamp));
		setAlwaysOnTop(App.isSet(options, onTop));
	}
	public void setAutoscroll(final boolean autoscroll) {
		scroll.setAutoscrolls(autoscroll);
	}
	public void setLineWrap(final boolean wraps) {
		log.setLineWrap(wraps);
	}
	public void setPrintTimeStamp(final boolean timestamp) {
		this.timestamp = timestamp;
	}
	public boolean isAutoscrolls() {
		return scroll.getAutoscrolls();
	}
	public boolean isLineWrap() {
		return log.getLineWrap();
	}
	public boolean isPrintTimeStamp() {
		return timestamp;
	}
	private synchronized String getTimeStamp() {
		if (!timestamp) return "";
		time.setTime(System.currentTimeMillis());
		return "["+time.toString()+"]: ";
	}
	public synchronized void append(final String s) {
		log.append(getTimeStamp()+s);
	}
	public synchronized void appendln(final String s) {
		log.append(getTimeStamp()+s+"\n");
	}
	public synchronized void appendln() {
		log.append("\n");
	}
	public synchronized void clear() {
		log.setText("");
	}
}
