package simple.gui.logging;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.PrintStream;
import java.io.Writer;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import simple.io.TextAreaOutputStream;
import simple.io.TextAreaWriter;

/** A JPanel that is simply a log.
 * <br>Created: 2007
 * <br>depends on {@link simple.io.TextAreaWriter}, {@link simple.io.TextAreaOutputStream}
 * @author Kenneth Pierce
 */
public class LogPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private final JTextArea log = new JTextArea();
	private final JScrollPane scroll = new JScrollPane(log);
	private boolean timestamp = true;
	private Date time = new Date();
	public LogPanel() {
		super(new BorderLayout());
		log.setFont(new Font("Courier New", Font.PLAIN, 12));
		log.setLineWrap(true);
		log.setWrapStyleWord(true);
		log.setEditable(false);
		add(scroll);
	}
	/**@see javax.swing.JTextArea#setLineWrap(boolean)
	 * @param wraps
	 */
	public void setLineWrap(boolean wraps) {
		log.setLineWrap(wraps);
	}
	public void setPrintTimeStamp(boolean timestamp) {
		this.timestamp = timestamp;
	}
	/**@see javax.swing.JTextArea#getLineWrap()
	 * @return if the lines will be wrapped
	 */
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
	public synchronized void append(String s) {
		log.append(getTimeStamp()+s);
	}
	public synchronized void appendln(String s) {
		log.append(getTimeStamp()+s+"\n");
	}
	public synchronized void appendln() {
		log.append("\n");
	}
	/**
	 * @return A PrintStream wrapping {@link simple.io.TextAreaOutputStream}
	 */
	public PrintStream getPrintStream() {
		return new PrintStream(new TextAreaOutputStream(log));
	}
	/**
	 * @return An instance of {@link simple.io.TextAreaWriter}
	 */
	public Writer getWriter() {
		return new TextAreaWriter(log);
	}
	public void clear() {
		log.setText("");
	}
}
