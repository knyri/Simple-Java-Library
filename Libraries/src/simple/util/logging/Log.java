/**
 * 
 */
package simple.util.logging;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Writer;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import simple.io.WriterOutputStream;

/** The preferred method for getting a log is to use {@link LogFactory#getLogFor(Class)}.<br>
 * This ensures that only one log will be used per class and each log outputs to
 * the same stream. Unless you don't want this behavior.<br>
 * Note: all log functions return true when an error has occurred.
 * <hr>
 * Depends on {@link simple.io.WriterOutputStream}
 * <br>Created: Oct 21, 2010
 * @author Kenneth Pierce
 */
public final class Log {
	private PrintStream _out;
	private final String _cName;
	private boolean _printTime = false,
		_printDate=false;
	public static final byte LDEBUG = LogLevel.DEBUG.getValue(),
	LERROR = LogLevel.ERROR.getValue(),
	LWARNING = LogLevel.WARNING.getValue(),
	LINFORMATION = LogLevel.INFORMATION.getValue();
	private final Hashtable<String, Byte> section = new Hashtable<String, Byte>();
	/**<dl>
	 * <dt>debug</dt><dd>0x1</dd>
	 * <dt>error</dt><dd>0x2</dd>
	 * <dt>warning</dt><dd>0x4</dd>
	 * <dt>information</dt><dd>0x8</dd>
	 */
	private byte options = (byte)0xFF;
	public Log(final Class<?> clazz) {
		this(System.out, clazz);
	}
	public Log(final OutputStream os, final Class<?> clazz) {
		_cName = clazz.getCanonicalName();
		if (os instanceof PrintStream) {
			_out = (PrintStream)os;
		} else {
			_out = new PrintStream(os);
		}
	}
	public Log(final Writer os, final Class<?> clazz) {
		_cName = clazz.getCanonicalName();
		_out = new PrintStream(new WriterOutputStream(os));
	}
	public void setPrintTime(final boolean b) {
		_printTime = b;
	}
	public boolean getPrintTime() {
		return _printTime;
	}
	public void setPrintDate(final boolean b) {
		_printDate = b;
	}
	public boolean getPrintDate() {
		return _printDate;
	}
	public void setPrint(final LogLevel level, final boolean print) {
		if (print) {
			options |= level.getValue();
		} else {
			options &= ~level.getValue();
		}
	}
	public void setPrintDebug(final boolean b) {
		if (b) {
			options |= LDEBUG;
		} else {
			options &= ~LDEBUG;
		}
	}
	public void setPrintError(final boolean b) {
		if (b) {
			options |= LERROR;
		} else {
			options &= ~LERROR;
		}
	}
	public void setPrintWarning(final boolean b) {
		if (b) {
			options |= LWARNING;
		} else {
			options &= ~LWARNING;
		}
	}
	public void setPrintInformation(final boolean b) {
		if (b) {
			options |= LINFORMATION;
		} else {
			options &= ~LINFORMATION;
		}
	}
	public synchronized void setSection(final String section, final LogLevel level, final boolean print) {
		Byte options = this.section.remove(section);
		if (options==null) {
			options=0;
		}
		if (print) {
			options =(byte) (options.byteValue()|level.getValue());
		} else {
			if ((options&level.getValue())==level.getValue()) {
				options=(byte) (options.byteValue()&~level.getValue());
			}
		}
		this.section.put(section, options);
	}
	public void setStream(final OutputStream os) {
		if (os instanceof PrintStream) {
			_out = (PrintStream)os;
		} else {
			_out = new PrintStream(os);
		}
	}
	public void setStream(final Writer os) {
		_out = new PrintStream(new WriterOutputStream(os));
	}
	public final boolean println() {
		_out.println();
		return _out.checkError();
	}
	public final boolean println(final Object msg){
		_out.println(msg);
		return _out.checkError();
	}
	public final boolean print(final Object msg){
		_out.print(msg);
		return _out.checkError();
	}
	public final boolean log(final LogLevel type, final Object msg) {
		if ((options&type.getValue()) == type.getValue())
			return _log(type,msg);
		return _out.checkError();
	}
	public final boolean log(final LogLevel type, final Exception msg) {
		if ((options&type.getValue()) == type.getValue())
			return _log(type,msg);
		return _out.checkError();
	}
	public final boolean log(final LogLevel type, final Dictionary<?,?> msg) {
		if ((options&type.getValue()) == type.getValue())
			return _log(type,msg);
		return _out.checkError();
	}
	public final boolean log(final LogLevel type, final String msg, final Exception e) {
		if ((options&type.getValue()) == type.getValue())
			return _log(type,msg,e);
		return _out.checkError();
	}
	public final boolean log(final LogLevel type, final Iterable<?> msg) {
		if ((options&type.getValue()) == type.getValue())
			return _log(type,msg);
		return _out.checkError();
	}
	public final boolean log(final LogLevel type, final String ref, final Iterable<?> msg) {
		if ((options&type.getValue()) == type.getValue())
			return _log(type,ref,msg);
		return _out.checkError();
	}
	public final boolean log(final LogLevel type, final Object[] msg) {
		if ((options&type.getValue()) == type.getValue())
			return _log(type,msg);
		return _out.checkError();
	}
	public final boolean log(final LogLevel type, final byte[] msg) {
		if ((options&type.getValue()) == type.getValue())
			return _log(type,msg);
		return _out.checkError();
	}
	public final boolean log(final LogLevel type, final short[] msg) {
		if ((options&type.getValue()) == type.getValue())
			return _log(type,msg);
		return _out.checkError();
	}
	public final boolean log(final LogLevel type, final int[] msg) {
		if ((options&type.getValue()) == type.getValue())
			return _log(type,msg);
		return _out.checkError();
	}
	public final boolean log(final LogLevel type, final long[] msg) {
		if ((options&type.getValue()) == type.getValue())
			return _log(type,msg);
		return _out.checkError();
	}
	public final boolean log(final LogLevel type, final float[] msg) {
		if ((options&type.getValue()) == type.getValue())
			return _log(type,msg);
		return _out.checkError();
	}
	public final boolean log(final LogLevel type, final double[] msg) {
		if ((options&type.getValue()) == type.getValue())
			return _log(type,msg);
		return _out.checkError();
	}
	public final boolean log(final LogLevel type, final String ref, final Object msg) {
		if ((options&type.getValue()) == type.getValue())
			return _log(type,ref,msg);
		return _out.checkError();
	}
	public final boolean log(final LogLevel type, final String ref, final Object[] msg) {
		if ((options&type.getValue()) == type.getValue())
			return _log(type,ref,msg);
		return _out.checkError();
	}
	public final boolean log(final LogLevel type, final String ref, final byte[] msg) {
		if ((options&type.getValue()) == type.getValue())
			return _log(type,ref,msg);
		return _out.checkError();
	}
	public final boolean log(final LogLevel type, final String ref, final short[] msg) {
		if ((options&type.getValue()) == type.getValue())
			return _log(type,ref,msg);
		return _out.checkError();
	}
	public final boolean log(final LogLevel type, final String ref, final int[] msg) {
		if ((options&type.getValue()) == type.getValue())
			return _log(type,ref,msg);
		return _out.checkError();
	}
	public final boolean log(final LogLevel type, final String ref, final long[] msg) {
		if ((options&type.getValue()) == type.getValue())
			return _log(type,ref,msg);
		return _out.checkError();
	}
	public final boolean log(final LogLevel type, final String ref, final float[] msg) {
		if ((options&type.getValue()) == type.getValue())
			return _log(type,ref,msg);
		return _out.checkError();
	}
	public final boolean log(final LogLevel type, final String ref, final double[] msg) {
		if ((options&type.getValue()) == type.getValue())
			return _log(type,ref,msg);
		return _out.checkError();
	}
	//Sectioned loggers
	public final boolean log(final String section,final LogLevel type, final Object msg) {
		Byte options = this.section.get(section);
		if (options==null) {
			options=this.options;
		}
		if ((options&type.getValue()) == type.getValue())
			return _log(type,msg);
		return _out.checkError();
	}
	public final boolean log(final String section,final LogLevel type, final Exception msg) {
		Byte options = this.section.get(section);
		if (options==null) {
			options=this.options;
		}
		if ((options&type.getValue()) == type.getValue())
			return _log(type,msg);
		return _out.checkError();
	}
	public final boolean log(final String section,final LogLevel type, final Dictionary<?,?> msg) {
		Byte options = this.section.get(section);
		if (options==null) {
			options=this.options;
		}
		if ((options&type.getValue()) == type.getValue())
			return _log(type,msg);
		return _out.checkError();
	}
	public final boolean log(final String section,final LogLevel type, final String ref, final Object msg) {
		Byte options = this.section.get(section);
		if (options==null) {
			options=this.options;
		}
		if ((options&type.getValue()) == type.getValue())
			return _log(type,ref,msg);
		return _out.checkError();
	}
	public final boolean log(final String section,final LogLevel type, final String msg, final Exception e) {
		Byte options = this.section.get(section);
		if (options==null) {
			options=this.options;
		}
		if ((options&type.getValue()) == type.getValue())
			return _log(type,msg,e);
		return _out.checkError();
	}
	public final boolean log(final String section,final LogLevel type, final Iterable<?> msg) {
		Byte options = this.section.get(section);
		if (options==null) {
			options=this.options;
		}
		if ((options&type.getValue()) == type.getValue())
			return _log(type,msg);
		return _out.checkError();
	}
	public final boolean log(final String section,final LogLevel type, final String ref, final Iterable<?> msg) {
		Byte options = this.section.get(section);
		if (options==null) {
			options=this.options;
		}
		if ((options&type.getValue()) == type.getValue())
			return _log(type,ref,msg);
		return _out.checkError();
	}
	public final boolean log(final String section,final LogLevel type, final Object[] msg) {
		Byte options = this.section.get(section);
		if (options==null) {
			options=this.options;
		}
		if ((options&type.getValue()) == type.getValue())
			return _log(type,msg);
		return _out.checkError();
	}
	public final boolean log(final String section,final LogLevel type, final String ref, final Object[] msg) {
		Byte options = this.section.get(section);
		if (options==null) {
			options=this.options;
		}
		if ((options&type.getValue()) == type.getValue())
			return _log(type,ref,msg);
		return _out.checkError();
	}
	//Error Code Resolver code
	/**
	 * @param type
	 * @param msg can be null. Will pull the ECR assigned to LogFactory.
	 * @param code
	 * @return true on error
	 */
	public final boolean log(final LogLevel type, ErrorCodeResolver msg, final int code) {
		if ((options&type.getValue()) == type.getValue()) {
			if (msg == null) {
				msg = LogFactory.getECR();
			}
			_out.println(_getPreMessage(type)+_cName+": "+"("+code+")"+msg.getErrorString(code));
		}
		return _out.checkError();
	}
	/**Shortcut for <code>log(LogLevel.ERROR, LogFactory.getECR(), errorCode)</code>
	 * @param errorCode
	 * @return true on error
	 */
	public final boolean log(final int errorCode) {
		return log(LogLevel.ERROR, LogFactory.getECR(), errorCode);
	}
	//workhorse loggers
	private final boolean _log(final LogLevel type, final Object msg) {
		_out.println(_getPreMessage(type)+_cName+": "+msg);
		return _out.checkError();
	}
	private final boolean _log(final LogLevel type, final Exception msg) {
		_out.println(_getPreMessage(type)+_cName+": "+msg);
		msg.printStackTrace(_out);
		return _out.checkError();
	}
	private final boolean _log(final LogLevel type, final Dictionary<?,?> msg) {
		_out.println(_getPreMessage(type)+_cName+": "+"Property listing:");
		final Enumeration<?> keys = msg.keys();
		Object key;
		while(keys.hasMoreElements()) {
			key = keys.nextElement();
			_out.println("["+key+"="+msg.get(key)+"]");
		}
		return _out.checkError();
	}
	private final boolean _log(final LogLevel type, final String ref, final Object msg) {
		_out.println(_getPreMessage(type)+_cName+": "+ref+": "+msg);
		return _out.checkError();
	}
	private final boolean _log(final LogLevel type, final String msg, final Exception e) {
		_out.println(_getPreMessage(type)+_cName+": "+msg);
		e.printStackTrace(_out);
		return _out.checkError();
	}
	private final boolean _log(final LogLevel type, final Iterable<?> msg) {
		_out.print(_getPreMessage(type)+_cName+": "+"{");
		final Iterator<?> iter = msg.iterator();
		_out.print(iter.next());
		while (iter.hasNext()) {
			_out.print(",");
			_out.print(iter.next());
		}
		_out.println("}");
		return _out.checkError();
	}
	private final boolean _log(final LogLevel type, final String ref, final Iterable<?> msg) {
		_out.print(_getPreMessage(type)+_cName+": "+ref+" : {");
		final Iterator<?> iter = msg.iterator();
		_out.print(iter.next());
		while (iter.hasNext()) {
			_out.print(",");
			_out.print(iter.next());
		}
		_out.println("}");
		return _out.checkError();
	}
	private final boolean _log(final LogLevel type, final byte[] msg) {
		_out.print(_getPreMessage(type)+_cName+": "+"{");
		if (msg.length>0) {
			_out.print(msg[0]);
			for (int i = 1; i < msg.length; i++) {
				_out.print(",");
				_out.print(msg[i]);
			}
		}
		_out.println("}");
		return _out.checkError();
	}
	private final boolean _log(final LogLevel type, final int[] msg) {
		_out.print(_getPreMessage(type)+_cName+": "+"{");
		if (msg.length>0) {
			_out.print(msg[0]);
			for (int i = 1; i < msg.length; i++) {
				_out.print(",");
				_out.print(msg[i]);
			}
		}
		_out.println("}");
		return _out.checkError();
	}
	private final boolean _log(final LogLevel type, final short[] msg) {
		_out.print(_getPreMessage(type)+_cName+": "+"{");
		if (msg.length>0) {
			_out.print(msg[0]);
			for (int i = 1; i < msg.length; i++) {
				_out.print(",");
				_out.print(msg[i]);
			}
		}
		_out.println("}");
		return _out.checkError();
	}
	private final boolean _log(final LogLevel type, final long[] msg) {
		_out.print(_getPreMessage(type)+_cName+": "+"{");
		if (msg.length>0) {
			_out.print(msg[0]);
			for (int i = 1; i < msg.length; i++) {
				_out.print(",");
				_out.print(msg[i]);
			}
		}
		_out.println("}");
		return _out.checkError();
	}
	private final boolean _log(final LogLevel type, final float[] msg) {
		_out.print(_getPreMessage(type)+_cName+": "+"{");
		if (msg.length>0) {
			_out.print(msg[0]);
			for (int i = 1; i < msg.length; i++) {
				_out.print(",");
				_out.print(msg[i]);
			}
		}
		_out.println("}");
		return _out.checkError();
	}
	private final boolean _log(final LogLevel type, final double[] msg) {
		_out.print(_getPreMessage(type)+_cName+": "+"{");
		if (msg.length>0) {
			_out.print(msg[0]);
			for (int i = 1; i < msg.length; i++) {
				_out.print(",");
				_out.print(msg[i]);
			}
		}
		_out.println("}");
		return _out.checkError();
	}
	private final boolean _log(final LogLevel type, final Object[] msg) {
		_out.print(_getPreMessage(type)+_cName+": "+"{");
		if (msg.length>0) {
			_out.print(msg[0]);
			for (int i = 1; i < msg.length; i++) {
				_out.print(",");
				_out.print(msg[i]);
			}
		}
		_out.println("}");
		return _out.checkError();
	}
	private final boolean _log(final LogLevel type, final String ref, final Object[] msg) {
		_out.print(_getPreMessage(type)+_cName+": "+ref+" : {");
		if (msg.length>0) {
			_out.print(msg[0]);
			for (int i = 1; i < msg.length; i++) {
				_out.print(",");
				_out.print(msg[i]);
			}
		}
		_out.println("}");
		return _out.checkError();
	}
	private final boolean _log(final LogLevel type, final String ref, final byte[] msg) {
		_out.print(_getPreMessage(type)+_cName+": "+ref+" : {");
		if (msg.length>0) {
			_out.print(msg[0]);
			for (int i = 1; i < msg.length; i++) {
				_out.print(",");
				_out.print(msg[i]);
			}
		}
		_out.println("}");
		return _out.checkError();
	}
	private final boolean _log(final LogLevel type, final String ref, final int[] msg) {
		_out.print(_getPreMessage(type)+_cName+": "+ref+" : {");
		if (msg.length>0) {
			_out.print(msg[0]);
			for (int i = 1; i < msg.length; i++) {
				_out.print(",");
				_out.print(msg[i]);
			}
		}
		_out.println("}");
		return _out.checkError();
	}
	private final boolean _log(final LogLevel type, final String ref, final short[] msg) {
		_out.print(_getPreMessage(type)+_cName+": "+ref+" : {");
		if (msg.length>0) {
			_out.print(msg[0]);
			for (int i = 1; i < msg.length; i++) {
				_out.print(",");
				_out.print(msg[i]);
			}
		}
		_out.println("}");
		return _out.checkError();
	}
	private final boolean _log(final LogLevel type, final String ref, final long[] msg) {
		_out.print(_getPreMessage(type)+_cName+": "+ref+" : {");
		if (msg.length>0) {
			_out.print(msg[0]);
			for (int i = 1; i < msg.length; i++) {
				_out.print(",");
				_out.print(msg[i]);
			}
		}
		_out.println("}");
		return _out.checkError();
	}
	private final boolean _log(final LogLevel type, final String ref, final float[] msg) {
		_out.print(_getPreMessage(type)+_cName+": "+ref+" : {");
		if (msg.length>0) {
			_out.print(msg[0]);
			for (int i = 1; i < msg.length; i++) {
				_out.print(",");
				_out.print(msg[i]);
			}
		}
		_out.println("}");
		return _out.checkError();
	}
	private final boolean _log(final LogLevel type, final String ref, final double[] msg) {
		_out.print(_getPreMessage(type)+_cName+": "+ref+" : {");
		if (msg.length>0) {
			_out.print(msg[0]);
			for (int i = 1; i < msg.length; i++) {
				_out.print(",");
				_out.print(msg[i]);
			}
		}
		_out.println("}");
		return _out.checkError();
	}
	private final String _getPreMessage(final LogLevel type){
		return type+": "+((_printDate)?(LogFactory.getDateStamp()+" "):"")+((_printTime)?(LogFactory.getTimeStamp()+" "):"");
	}
	/* **************************************
	 * *************************** NOTE:DEBUG
	 * **************************************
	 */
	public final boolean debugSect(final String section,final Object msg) {
		return log(section,LogLevel.DEBUG, msg);
	}
	public final boolean debugSect(final String section,final Object[] msg) {
		return log(section,LogLevel.DEBUG, msg);
	}
	public final boolean debugSect(final String section,final Exception msg) {
		return log(section,LogLevel.DEBUG, msg);
	}
	public final boolean debugSect(final String section,final Dictionary<?,?> msg) {
		return log(section,LogLevel.DEBUG, msg);
	}
	public final boolean debugSect(final String section,final String ref, final Object msg) {
		return log(section,LogLevel.DEBUG, ref, msg);
	}
	public final boolean debugSect(final String section,final String msg, final Exception e) {
		return log(section,LogLevel.DEBUG, msg, e);
	}
	public final boolean debug(final Object msg) {
		return log(LogLevel.DEBUG, msg);
	}
	public final boolean debug(final Object[] msg) {
		return log(LogLevel.DEBUG, msg);
	}
	public final boolean debug(final byte[] msg) {
		return log(LogLevel.DEBUG, msg);
	}
	public final boolean debug(final int[] msg) {
		return log(LogLevel.DEBUG, msg);
	}
	public final boolean debug(final short[] msg) {
		return log(LogLevel.DEBUG, msg);
	}
	public final boolean debug(final float[] msg) {
		return log(LogLevel.DEBUG, msg);
	}
	public final boolean debug(final long[] msg) {
		return log(LogLevel.DEBUG, msg);
	}
	public final boolean debug(final double[] msg) {
		return log(LogLevel.DEBUG, msg);
	}
	public final boolean debug(final Exception msg) {
		return log(LogLevel.DEBUG, msg);
	}
	public final boolean debug(final Dictionary<?,?> msg) {
		return log(LogLevel.DEBUG, msg);
	}
	public final boolean debug(final String ref, final Object[] msg) {
		return log(LogLevel.DEBUG, ref, msg);
	}
	public final boolean debug(final String ref, final Object msg) {
		return log(LogLevel.DEBUG, ref, msg);
	}
	public final boolean debug(final String msg, final Exception e) {
		return log(LogLevel.DEBUG, msg, e);
	}
	public final boolean debug(final String ref,final byte[] msg) {
		return log(LogLevel.DEBUG, ref, msg);
	}
	public final boolean debug(final String ref,final int[] msg) {
		return log(LogLevel.DEBUG, ref, msg);
	}
	public final boolean debug(final String ref,final short[] msg) {
		return log(LogLevel.DEBUG, ref, msg);
	}
	public final boolean debug(final String ref,final float[] msg) {
		return log(LogLevel.DEBUG, ref, msg);
	}
	public final boolean debug(final String ref,final long[] msg) {
		return log(LogLevel.DEBUG, ref, msg);
	}
	public final boolean debug(final String ref,final double[] msg) {
		return log(LogLevel.DEBUG, ref, msg);
	}
	/* **************************************
	 * *************************** NOTE:ERROR
	 * **************************************
	 */
	public final boolean errorSect(final String section,final Object msg) {
		return log(section,LogLevel.ERROR, msg);
	}
	public final boolean errorSect(final String section,final Object[] msg) {
		return log(section,LogLevel.ERROR, msg);
	}
	public final boolean errorSect(final String section,final Exception msg) {
		return log(section,LogLevel.ERROR, msg);
	}
	public final boolean errorSect(final String section,final String msg, final Exception e) {
		return log(section,LogLevel.ERROR, msg, e);
	}
	public final boolean errorSect(final String section,final String ref, final Object msg) {
		return log(section,LogLevel.ERROR, ref, msg);
	}
	public final boolean errorSect(final String section,final String ref, final Object[] msg) {
		return log(section,LogLevel.ERROR, ref, msg);
	}
	public final boolean error(final Object msg) {
		return log(LogLevel.ERROR, msg);
	}
	public final boolean error(final Object[] msg) {
		return log(LogLevel.ERROR, msg);
	}
	public final boolean error(final Exception msg) {
		return log(LogLevel.ERROR, msg);
	}
	public final boolean error(final String msg, final Exception e) {
		return log(LogLevel.ERROR, msg, e);
	}
	public final boolean error(final String ref, final Object msg) {
		return log(LogLevel.ERROR, ref, msg);
	}
	public final boolean error(final String ref, final Object[] msg) {
		return log(LogLevel.ERROR, ref, msg);
	}
	/* **************************************
	 * ************************* NOTE:WARNING
	 * **************************************
	 */
	public final boolean warningSect(final String section,final Object msg) {
		return log(section,LogLevel.WARNING, msg);
	}
	public final boolean warningSect(final String section,final Object[] msg) {
		return log(section,LogLevel.WARNING, msg);
	}
	public final boolean warningSect(final String section,final Exception msg) {
		return log(section,LogLevel.WARNING, msg);
	}
	public final boolean warningSect(final String section,final String msg, final Exception e) {
		return log(section,LogLevel.WARNING, msg, e);
	}
	public final boolean warningSect(final String section,final String ref, final Object msg) {
		return log(section,LogLevel.WARNING, ref, msg);
	}
	public final boolean warningSect(final String section,final String ref, final Object[] msg) {
		return log(section,LogLevel.WARNING, ref, msg);
	}
	public final boolean warning(final Object msg) {
		return log(LogLevel.WARNING, msg);
	}
	public final boolean warning(final Object[] msg) {
		return log(LogLevel.WARNING, msg);
	}
	public final boolean warning(final Exception msg) {
		return log(LogLevel.WARNING, msg);
	}
	public final boolean warning(final String msg, final Exception e) {
		return log(LogLevel.WARNING, msg, e);
	}
	public final boolean warning(final String ref, final Object msg) {
		return log(LogLevel.WARNING, ref, msg);
	}
	public final boolean warning(final String ref, final Object[] msg) {
		return log(LogLevel.WARNING, ref, msg);
	}
	/* **************************************
	 * ********************* NOTE:INFORMATION
	 * **************************************
	 */
	public final boolean informationSect(final String section,final Object msg) {
		return log(section,LogLevel.INFORMATION, msg);
	}
	public final boolean informationSect(final String section,final Dictionary<?,?> msg) {
		return log(section,LogLevel.INFORMATION, msg);
	}
	public final boolean informationSect(final String section,final String ref, final Object[] msg) {
		return log(section,LogLevel.INFORMATION, ref, msg);
	}
	public final boolean informationSect(final String section,final String ref, final Object msg) {
		return log(section,LogLevel.INFORMATION, ref, msg);
	}
	public final boolean information(final Object msg) {
		return log(LogLevel.INFORMATION, msg);
	}
	public final boolean information(final Dictionary<?,?> msg) {
		return log(LogLevel.INFORMATION, msg);
	}
	public final boolean information(final String ref, final Object[] msg) {
		return log(LogLevel.INFORMATION, ref, msg);
	}
	public final boolean information(final String ref, final Object msg) {
		return log(LogLevel.INFORMATION, ref, msg);
	}
}
