/* µCOMP:javac -classpath ".." %sµ */
package simple;

import javax.swing.JOptionPane;

import simple.io.WriterOutputStream;

import java.io.*;
import java.util.Calendar;
import java.util.Properties;
import java.util.Vector;

/**
 * <p>Title: Debugger</p>
 * <p>Description: Provides debugging functions</p>
 * Depends on simple.io.WriterOutputStream
 * <p>Created: 2004</p>
 * @author Kenneth Pierce
 * @version 2.6
 * @deprecated
 * @see simple.util.logging.Log
 */
public final class Debug {
	/**
	 * message type {@link javax.swing.JOptionPane#INFORMATION_MESSAGE}
	 */
	public static final int mINF  = JOptionPane.INFORMATION_MESSAGE;
	/**
	 * message type {@link javax.swing.JOptionPane#PLAIN_MESSAGE}
	 */
	public static final int mGEN  = JOptionPane.PLAIN_MESSAGE;
	/**
	 * message type {@link javax.swing.JOptionPane#QUESTION_MESSAGE}
	 */
	public static final int mASK  = JOptionPane.QUESTION_MESSAGE;
	/**
	 * message type {@link javax.swing.JOptionPane#WARNING_MESSAGE}
	 */
	public static final int mWARN = JOptionPane.WARNING_MESSAGE;
	/**
	 * message option {@link javax.swing.JOptionPane#YES_NO_OPTION}
	 */
	public static final int moYN  = JOptionPane.YES_NO_OPTION;
	/**
	 * message option {@link javax.swing.JOptionPane#OK_CANCEL_OPTION}
	 */
	public static final int moOC  = JOptionPane.OK_CANCEL_OPTION;
	/**
	 * message option {@link javax.swing.JOptionPane#YES_NO_CANCEL_OPTION}
	 */
	public static final int moYNC = JOptionPane.YES_NO_CANCEL_OPTION;
	/**
	 * message option {@link javax.swing.JOptionPane#OK_OPTION}
	 */
	public static final int moOK  = JOptionPane.OK_OPTION;
	/**
	 * message option {@link javax.swing.JOptionPane#NO_OPTION}
	 */
	public static final int moNO  = JOptionPane.NO_OPTION;
	/**
	 * message option {@link javax.swing.JOptionPane#CANCEL_OPTION}
	 */
	public static final int moCA  = JOptionPane.CANCEL_OPTION;
	/** Prints message to System.out and shows a message dialog.
	 * @param mess message to show
	 * @param title title for dialog
	 */
	public static final void mes(String mess, String title) {
		System.out.println(title+": "+mess);
		JOptionPane.showMessageDialog(null, mess, title, mGEN);
	}
	/** Prints message to System.out and shows a message dialog.
	 * @param mess message to show
	 * @param title title for dialog
	 * @param type message type
	 */
	public static final void mes(String mess, String title, int type) {
		System.out.println(title+": "+mess);
		JOptionPane.showMessageDialog(null, mess, title, type);
	}
	/** Gets the current time. Formatted: HH:mm:ss
	 * @return the current time.
	 */
	public static final String getTime() {
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR), minute = c.get(Calendar.MINUTE), second = c.get(Calendar.SECOND);
		return ((hour<10?"0":"")+hour + ":" + (minute<10?"0":"")+minute  + ":" + (second<10?"0":"")+second);
	}
	/** Gets the current time. Formatted: HHmmss
	 * @return the current time.
	 */
	public static final String getTimeStamp() {
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR), minute = c.get(Calendar.MINUTE), second = c.get(Calendar.SECOND);
		return ((hour<10?"0":"") + hour + (minute<10?"0":"") + minute  + (second<10?"0":"") + second);
	}
	/** Gets the current date. Formatted: yyyymmdd
	 * @return the current date
	 */
	public static final String getDateStamp() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR), month = c.get(Calendar.MONTH), day = c.get(Calendar.DAY_OF_MONTH);
		return (Integer.toString(year) + (month<10?"0":"")+Integer.toString(month) + (day<10?"0":"")+Integer.toString(day));
	}
//Debug Logging
	/** Output stream for logging. */
	private final BufferedOutputStream _out;
	/** The file(if any) we are writing to. */
	private final File _file;
	/** Show time stamp? */
	private boolean _showTime = true;
	/** How much of the error to show. */
	private int _errorDepth = errFULL;
	/** What to log. */
	private int _errlvl = INFO_WARN_ERROR;
	/** full: show message and stack trace.
	 * short: show message
	 */
	public static final int errFULL = 0, errSHORT = 1;
	public static final int lvlERROR = 1, lvlWARN = 2, lvlINFO = 4, lvlDEBUG = 8;
	public static final int ERROR_DEBUG = lvlERROR+lvlDEBUG,
			INFO_ERROR = lvlINFO+lvlERROR,
			WARN_ERROR = lvlWARN+lvlERROR,
			INFO_WARN_ERROR = lvlINFO+lvlWARN+lvlERROR,
			WARN_ERROR_DEBUG = WARN_ERROR+lvlDEBUG,
			INFO_WARN_ERROR_DEBUG = lvlERROR + lvlWARN + lvlINFO + lvlDEBUG;
	/** Outputs to System.out 
	 */
	public Debug() {
		this(System.out);
	}
	/** Opens and clears the file for output.
	 * @param file file to use
	 * @throws FileNotFoundException
	 */
	public Debug(File file) throws FileNotFoundException {
		this(false, file);
	}
	/** Opens and clears the file for output and sets the error reporting level.
	 * @param file file to use
	 * @param errLvl error reporting level
	 * @throws FileNotFoundException
	 */
	public Debug(File file, int errLvl) throws FileNotFoundException {
		this(file);
		_errlvl = errLvl;
	}
	/** Outputs to System.out and sets the error reporting level.
	 * @param errLvl error reporting level
	 */
	public Debug(int errLvl) {
		this(System.out, errLvl);
	}
	/** Opens and clears the file for output.
	 * @param file file to use
	 * @throws FileNotFoundException
	 */
	public Debug(String file) throws FileNotFoundException {
		this(false, new File(file));
	}
	/** Opens and clears the file for output and sets the error reporting level.
	 * @param file file to use
	 * @param errLvl error reporting level
	 * @throws FileNotFoundException
	 */
	public Debug(String file, int errLvl) throws FileNotFoundException {
		this(false, new File(file), errLvl);
	}
	/** Opens the file for output.
	 * @param append true if you want to append to the file or false if you want to clear the file's contents
	 * @param file the file to use
	 * @throws FileNotFoundException
	 */
	public Debug(boolean append, File file) throws FileNotFoundException {
		_file = file;
		String path = _file.getAbsolutePath();
		path = path.substring(0,path.lastIndexOf(File.separatorChar)+1);
		new File(path).mkdirs();
		_out = new BufferedOutputStream(new FileOutputStream(_file, append), 2048);
	}
	/** Opens the file for output and sets the error level.
	 * @param append true if you want to append to the file or false if you want to clear the file's contents
	 * @param file the file to use
	 * @param errLvl error reporting level
	 * @throws FileNotFoundException
	 */
	public Debug(boolean append, File file, int errLvl) throws FileNotFoundException {
		this(append, file);
		_errlvl = errLvl;
	}
	public Debug(boolean append, String file) throws FileNotFoundException {
		this(append, new File(file));
	}
	public Debug(boolean append, String file, int errLvl) throws FileNotFoundException {
		this(append, new File(file), errLvl);
	}
	/** Sets the target stream for logging output.
	 * @param os stream to use
	 */
	public Debug(OutputStream os) {
		_file = null;
		if (os instanceof BufferedOutputStream)
			_out = (BufferedOutputStream)os;
		else
			_out = new BufferedOutputStream(os);
	}
	public Debug(OutputStream os, int errlvl) {
		this(os);
		_errlvl = errlvl;
	}
	/** sets the stream to use for logging output.
	 * @param os
	 */
	public Debug(Writer os) {
		_file = null;
		_out = new BufferedOutputStream(new WriterOutputStream(os));
	}
	public Debug(Writer os, int errlvl) {
		this(os);
		_errlvl = errlvl;
	}
	/**
	 * closes the stream
	 */
	public void close() {
		try {
			_out.close();
		} catch(IOException ex) {}
	}
	/**
	 * @param prefix
	 * @param e the exception
	 * @return true on success
	 */
	public boolean log(String prefix, Exception e) {
		try {
			_out.write((((_showTime)?(getTime()+"\t"):"")+prefix+"\t"+e.getMessage()+"\r\n").getBytes());
			if (_errorDepth==errFULL) {
				StringWriter SW = new StringWriter();
				e.printStackTrace(new PrintWriter(SW));
				_out.write((SW.toString()+"\r\n").getBytes());
				System.err.println(SW.toString());
				_out.flush();
			}
		}
		catch (IOException ex) {ex.printStackTrace();return false;}
		return true;
	}
	public boolean log(String msg) {
		try {
			_out.write((((_showTime)?(getTime()+"\t"):"")+msg).getBytes());
			_out.flush();
		}
		catch (IOException ex) {ex.printStackTrace();return false;}
		return true;
	}
	public boolean log(Object msg) {
		try {
			_out.write((((_showTime)?(getTime()+"\t"):"")+msg).getBytes());
			_out.flush();
		}
		catch (IOException ex) {ex.printStackTrace();return false;}
		return true;
	}
	public boolean log(Exception e) {
		try {
			_out.write((((_showTime)?(getTime()+"\t"):"")+e.getMessage()+"\r\n").getBytes());
			if (_errorDepth==errFULL) {
				StringWriter SW = new StringWriter();
				e.printStackTrace(new PrintWriter(SW));
				_out.write((SW.toString()+"\r\n").getBytes());
				System.err.println(SW.toString());
				_out.flush();
			}
		}
		catch (IOException ex) {ex.printStackTrace();return false;}
		return true;
	}
	public boolean log(String prefix, Object msg) {
		msg = prefix+"\t"+msg+"\r\n";
		return log(msg);
	}
	public boolean log(String prefix, String msg) {
		msg = prefix+"\t"+msg+"\r\n";
		return log(msg);
	}
	public boolean logDebug(String caller, String msg) {
		if ((_errlvl&lvlDEBUG) != 0) {
			return log("DEBUG:\t["+caller+"]", msg);
		}
		return true;
	}
	public boolean logDebug(String caller, Properties msg) {
		boolean success = true;
		if ((_errlvl&lvlDEBUG) != 0) {
			String[] props = msg.toString().split(", ");
			for (int i = 0; i < props.length; i++)
			success &= log("DEBUG:\t["+caller+"]", props[i]);
		}
		return success;
	}
	public boolean logError(String caller, Exception e) {
		if ((_errlvl&lvlERROR) != 0) {
			return log("ERROR:\t["+caller+"]", e);
		}
		return true;
	}
	public boolean logError(String caller, String e) {
		if ((_errlvl&lvlERROR) != 0) {
			return log("ERROR:\t["+caller+"]", e);
		}
		return true;
	}
	public boolean logWarning(String caller, String msg) {
		if ((_errlvl&lvlWARN) != 0) {
			return log("WARNING:\t["+caller+"]", msg);
		}
		return true;
	}
	public boolean logWarning(String caller, Exception e) {
		if ((_errlvl&lvlWARN) != 0) {
			return log("WARNING:\t["+caller+"]", e);
		}
		return true;
	}
	public boolean logInfo(String caller, String msg) {
		if ((_errlvl&lvlINFO) != 0) {
			return log("INFO:\t["+caller+"]", msg);
		}
		return true;
	}
	public boolean logInfo(String caller, Properties msg) {
		boolean success = true;
		if ((_errlvl&lvlINFO) != 0) {
			String[] props = msg.toString().split(", ");
			for (int i = 0; i < props.length; i++)
			success &= log("INFO:\t["+caller+"]", props[i]);
		}
		return success;
	}
	public boolean logInfo(String caller, Object msg) {
		if ((_errlvl&lvlINFO) != 0) {
			return log("INFO:\t["+caller+"]", msg);
		}
		return true;
	}
	/**
	 * @param lvl 
	 */
	public void setErrorLevel(int lvl) {
		if (lvl>=lvlERROR || lvl <= lvlDEBUG)
			_errlvl = lvl;
	}
	public int getErrorDepth() {
		return _errorDepth;
	}
	/** 
	 * @param lvl {@link #errSHORT} or {@link #errFULL}
	 */
	public void setErrorDepth(int lvl) {
		if (lvl>=errFULL || lvl<=errSHORT) {
			_errorDepth = lvl;
		}
	}
	public void setShowTime(boolean t) {
		_showTime = t;
	}
	public boolean getShowTime() {
		return _showTime;
	}
	public void finalize() throws Throwable {
		try {
			_out.close();
		} catch(IOException ex) {}
		super.finalize();
	}
	public void logDebug(String caller, Vector<String> list) {
		StringBuilder buf = new StringBuilder(15*list.size());
		String item;
		for (int i = 0; i < list.size(); i++) {
			item = list.get(i);
			buf.append(item);
			if (i != list.size()-1)
				buf.append(", ");
		}
		logDebug(caller, buf.toString());
	}
}