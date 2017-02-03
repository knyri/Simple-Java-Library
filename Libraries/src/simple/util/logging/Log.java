/**
 *
 */
package simple.util.logging;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

/** The preferred method for getting a log is to use {@link LogFactory#getLogFor(Class)}.<br>
 * This ensures that only one log will be used per class and each log outputs to
 * the same stream. Unless you don't want this behavior.<br>
 * Note: all log functions return true when an error has occurred.
 * <hr>
 * <br>Created: Oct 21, 2010
 * @author Kenneth Pierce
 */
public final class Log implements AutoCloseable{
	private ErrorCodeResolver ecr= LogFactory.DEFAULTECR;
	private PrintWriter _out;
	private final String _cName;
	private boolean
		_printTime = false,
		_printDate=false;

	private final Object writeSync=new Object();

	public static final byte
		// Why am I duplicating this? Speed?
		LDEBUG = LogLevel.DEBUG.getValue(),
		LERROR = LogLevel.ERROR.getValue(),
		LWARNING = LogLevel.WARNING.getValue(),
		LINFORMATION = LogLevel.INFORMATION.getValue();

	private final HashMap<String, Byte> section = new HashMap<String, Byte>();
	/**<dl>
	 * <dt>debug</dt><dd>0x1</dd>
	 * <dt>error</dt><dd>0x2</dd>
	 * <dt>warning</dt><dd>0x4</dd>
	 * <dt>information</dt><dd>0x8</dd>
	 * </dl>
	 */
	private byte options= (byte)0xFF;

	public Log(Class<?> clazz) {
		this(System.out, clazz);
	}

	public Log(OutputStream os, Class<?> clazz) {
		_cName = clazz.getCanonicalName();
		_out = new PrintWriter(new OutputStreamWriter(os));
	}

	public Log(Writer os, Class<?> clazz) {
		_cName = clazz.getCanonicalName();
		if (os instanceof PrintWriter)
			_out = (PrintWriter)os;
		else
			_out = new PrintWriter(os);
	}

	public void setPrintTime(boolean b) {
		_printTime = b;
	}
	public boolean getPrintTime() {
		return _printTime;
	}

	public void setPrintDate(boolean b) {
		_printDate = b;
	}
	public boolean getPrintDate() {
		return _printDate;
	}

	public void setPrint(LogLevel level, boolean print) {
		if (print){
			options |= level.getValue();
		}else{
			options &= ~level.getValue();
		}
	}
	public final boolean getPrint(LogLevel level) {
		return isSet(options, level);
	}

	private static final boolean isSet(byte options, LogLevel level){
		// Praying the JIT inlines the crap out of this method
		return (options & level.getValue()) == level.getValue();
	}

	public void setPrintDebug      (boolean b) {setPrint(LogLevel.DEBUG,b);}
	public void setPrintError      (boolean b) {setPrint(LogLevel.ERROR,b);}
	public void setPrintWarning    (boolean b) {setPrint(LogLevel.WARNING,b);}
	public void setPrintInformation(boolean b) {setPrint(LogLevel.INFORMATION,b);}
	public synchronized void setSection(String section, LogLevel level, boolean print) {
		Byte options= this.section.remove(section);
		if (options == null){
			options= 0;
		}

		if (print){
			options= (byte) (options.byteValue() | level.getValue());
		}else if (isSet(options, level)){
			options=(byte) (options.byteValue() & ~level.getValue());
		}
		this.section.put(section, options);
	}

	public void setStream(OutputStream os) {
		_out= new PrintWriter(new OutputStreamWriter(os));
	}
	public void setStream(Writer os) {
		if (os instanceof PrintWriter){
			_out= (PrintWriter)os;
		}else{
			_out= new PrintWriter(os);
		}
	}

	public final boolean println() {
		_out.println();
		return _out.checkError();
	}
	public final boolean println(Object msg){
		_out.println(msg);
		return _out.checkError();
	}
	public final boolean print(Object msg){
		_out.print(msg);
		return _out.checkError();
	}

	public final boolean log(LogLevel type, Object          msg){return _log(type,msg,options);}
	public final boolean log(LogLevel type, Throwable       msg){return _log(type,msg,options);}
	public final boolean log(LogLevel type, Dictionary<?,?> msg){return _log(type,msg,options);}
	public final boolean log(LogLevel type, Iterable<?> msg){return _log(type,msg,',',options);}
	public final boolean log(LogLevel type, Object[]    msg){return _log(type,msg,',',options);}
	public final boolean log(LogLevel type, byte[]      msg){return _log(type,msg,',',options);}
	public final boolean log(LogLevel type, short[]     msg){return _log(type,msg,',',options);}
	public final boolean log(LogLevel type, int[]       msg){return _log(type,msg,',',options);}
	public final boolean log(LogLevel type, long[]      msg){return _log(type,msg,',',options);}
	public final boolean log(LogLevel type, float[]     msg){return _log(type,msg,',',options);}
	public final boolean log(LogLevel type, double[]    msg){return _log(type,msg,',',options);}

	public final boolean log(LogLevel type, Object[] msg, char sep){return _log(type,msg,sep,options);}
	public final boolean log(LogLevel type, byte[]   msg, char sep){return _log(type,msg,sep,options);}
	public final boolean log(LogLevel type, short[]  msg, char sep){return _log(type,msg,sep,options);}
	public final boolean log(LogLevel type, int[]    msg, char sep){return _log(type,msg,sep,options);}
	public final boolean log(LogLevel type, long[]   msg, char sep){return _log(type,msg,sep,options);}
	public final boolean log(LogLevel type, float[]  msg, char sep){return _log(type,msg,sep,options);}
	public final boolean log(LogLevel type, double[] msg, char sep){return _log(type,msg,sep,options);}
/*
 * Reference Prefix
 */
	public final boolean log(LogLevel type, String ref, Throwable   msg){return _log(type,ref,msg,options);}
	public final boolean log(LogLevel type, String ref, Iterable<?> msg){return _log(type,ref,msg,',',options);}
	public final boolean log(LogLevel type, String ref, Object      msg){return _log(type,ref,msg,options);}
	public final boolean log(LogLevel type, String ref, Object[]    msg){return _log(type,ref,msg,',',options);}
	public final boolean log(LogLevel type, String ref, byte[]      msg){return _log(type,ref,msg,',',options);}
	public final boolean log(LogLevel type, String ref, short[]     msg){return _log(type,ref,msg,',',options);}
	public final boolean log(LogLevel type, String ref, int[]       msg){return _log(type,ref,msg,',',options);}
	public final boolean log(LogLevel type, String ref, long[]      msg){return _log(type,ref,msg,',',options);}
	public final boolean log(LogLevel type, String ref, float[]     msg){return _log(type,ref,msg,',',options);}
	public final boolean log(LogLevel type, String ref, double[]    msg){return _log(type,ref,msg,',',options);}

	public final boolean log(LogLevel type, String ref, Iterable<?> msg, char sep){return _log(type,ref,msg,sep,options);}
	public final boolean log(LogLevel type, String ref, Object[]    msg, char sep){return _log(type,ref,msg,sep,options);}
	public final boolean log(LogLevel type, String ref, byte[]      msg, char sep){return _log(type,ref,msg,sep,options);}
	public final boolean log(LogLevel type, String ref, short[]     msg, char sep){return _log(type,ref,msg,sep,options);}
	public final boolean log(LogLevel type, String ref, int[]       msg, char sep){return _log(type,ref,msg,sep,options);}
	public final boolean log(LogLevel type, String ref, long[]      msg, char sep){return _log(type,ref,msg,sep,options);}
	public final boolean log(LogLevel type, String ref, float[]     msg, char sep){return _log(type,ref,msg,sep,options);}
	public final boolean log(LogLevel type, String ref, double[]    msg, char sep){return _log(type,ref,msg,sep,options);}
	//Sectioned loggers
	private Byte getSectionOptions(String section){
		Byte options= this.section.get(section);
		if(options == null){
			options= this.options;
		}
		return options;
	}
	public final boolean log(String section, LogLevel type, Object msg) {
		return _log(type, msg, getSectionOptions(section));
	}
	public final boolean log(String section, LogLevel type, Throwable msg) {
		return _log(type, msg, getSectionOptions(section));
	}
	public final boolean log(String section, LogLevel type, Dictionary<?,?> msg) {
		return _log(type, msg, getSectionOptions(section));
	}
	public final boolean log(String section, LogLevel type, String ref, Object msg) {
		return _log(type, ref, msg, getSectionOptions(section));
	}
	public final boolean log(String section, LogLevel type, String ref, Throwable msg) {
		return _log(type, ref, msg, getSectionOptions(section));
	}
	public final boolean log(String section, LogLevel type, Iterable<?> msg) {
		return _log(type, msg, ',', getSectionOptions(section));
	}
	public final boolean log(String section, LogLevel type, String ref, Iterable<?> msg) {
		return _log(type, ref, msg, ',', getSectionOptions(section));
	}
	public final boolean log(String section, LogLevel type, Object[] msg) {
		return _log(type, msg, ',', getSectionOptions(section));
	}
	public final boolean log(String section, LogLevel type, String ref, Object[] msg) {
		return _log(type, ref, msg, ',', getSectionOptions(section));
	}


	//Error Code Resolver code
	public final void setECR(ErrorCodeResolver ecr){
		this.ecr= ecr;
	}
	public final ErrorCodeResolver getECR(){
		return ecr;
	}
	/**
	 * @param type
	 * @param msg can be null. Will pull the ECR assigned to this Log.
	 * @param code
	 * @return true on error
	 */
	public final boolean log(LogLevel type, ErrorCodeResolver msg, int code) {
		if (isSet(options, type)) {
			if (msg == null){
				msg= ecr;
			}
			_out.print(_getPreMessage(type));
			_out.print(_cName);
			_out.print(": (");
			_out.print(code);
			_out.print(")");
			_out.println(msg.getErrorString(code));
		}
		return _out.checkError();
	}
	/**Shortcut for <code>log(LogLevel.ERROR, ErrorCodeResolver, errorCode)</code>
	 * @param errorCode
	 * @return true on error
	 */
	public final boolean log(int errorCode) {
		return log(LogLevel.ERROR, ecr, errorCode);
	}
	//workhorse loggers
	private final boolean _log(LogLevel type, Object msg,byte options) {
		if(isSet(options, type))return _out.checkError();
		synchronized(writeSync){
			_out.print(_getPreMessage(type));
			_out.print(_cName);
			_out.print(": ");
			_out.println(msg);
		}
		return _out.checkError();
	}
	private final boolean _log(LogLevel type, Throwable msg,byte options) {
		if(isSet(options, type))return _out.checkError();
		synchronized(writeSync){
			_out.println(_getPreMessage(type));
			_out.print(_cName);
			_out.print(": ");
			_out.println(msg);
			msg.printStackTrace(_out);
		}
		return _out.checkError();
	}
	private final boolean _log(LogLevel type, Dictionary<?,?> msg,byte options) {
		if(isSet(options, type))return _out.checkError();
		synchronized(writeSync){
			_out.print(_getPreMessage(type));
			_out.print(_cName);
			_out.println(": Property listing:");
			final Enumeration<?> keys = msg.keys();
			Object key;
			while(keys.hasMoreElements()) {
				key= keys.nextElement();
				_out.print('[');
				_out.print(key);
				_out.print('=');
				_out.print(msg.get(key));
				_out.println(']');
			}
		}
		return _out.checkError();
	}
	private final boolean _log(LogLevel type, String ref, Object msg,byte options) {
		if(isSet(options, type))return _out.checkError();
		synchronized(writeSync){
			_out.print(_getPreMessage(type));
			_out.print(_cName);
			_out.print(": ");
			_out.print(ref);
			_out.print(": ");
			_out.println(msg);
		}
		return _out.checkError();
	}
	private final boolean _log(LogLevel type, String msg, Throwable e,byte options) {
		if(isSet(options, type))return _out.checkError();
		synchronized(writeSync){
			_out.print(_getPreMessage(type));
			_out.print(_cName);
			_out.print(": ");
			_out.println(msg);
			e.printStackTrace(_out);
		}
		return _out.checkError();
	}
	private final boolean _log(LogLevel type, Iterable<?> msg,char sep,byte options) {
		if(isSet(options, type))return _out.checkError();
		synchronized(writeSync){
			_out.print(_getPreMessage(type));
			_out.print(_cName);
			_out.print(": {");
			final Iterator<?> iter = msg.iterator();
			if(iter.hasNext()){
				_out.print(iter.next());
				while (iter.hasNext()) {
					_out.print(sep);
					_out.print(iter.next());
				}
			}
			_out.println('}');
		}
		return _out.checkError();
	}
	private final boolean _log(LogLevel type, String ref, Iterable<?> msg,char sep,byte options) {
		if(isSet(options, type))return _out.checkError();
		synchronized(writeSync){
			_out.print(_getPreMessage(type));
			_out.print(_cName);
			_out.print(": ");
			_out.print(ref);
			_out.print(" : {");
			final Iterator<?> iter = msg.iterator();
			if(iter.hasNext()){
				_out.print(iter.next());
				while (iter.hasNext()) {
					_out.print(sep);
					_out.print(iter.next());
				}
			}
			_out.println('}');
		}
		return _out.checkError();
	}
	private final boolean _log(LogLevel type, byte[] msg,char sep,byte options) {
		if(isSet(options, type))return _out.checkError();
		synchronized(writeSync){
			_out.print(_getPreMessage(type));
			_out.print(_cName);
			_out.print(": byte[]{");
			if (msg.length>0) {
				_out.print(msg[0]);
				for (int i = 1; i < msg.length; i++) {
					_out.print(sep);
					_out.print(msg[i]);
				}
			}
			_out.println('}');
		}
		return _out.checkError();
	}
	private final boolean _log(LogLevel type, int[] msg,char sep,byte options) {
		if(isSet(options, type))return _out.checkError();
		synchronized(writeSync){
			_out.print(_getPreMessage(type));
			_out.print(_cName);
			_out.print(": int[]{");
			if (msg.length>0) {
				_out.print(msg[0]);
				for (int i = 1; i < msg.length; i++) {
					_out.print(sep);
					_out.print(msg[i]);
				}
			}
			_out.println('}');
		}
		return _out.checkError();
	}
	private final boolean _log(LogLevel type, short[] msg,char sep,byte options) {
		if(isSet(options, type))return _out.checkError();
		synchronized(writeSync){
			_out.print(_getPreMessage(type));
			_out.print(_cName);
			_out.print(": short[]{");
			if (msg.length>0) {
				_out.print(msg[0]);
				for (int i = 1; i < msg.length; i++) {
					_out.print(sep);
					_out.print(msg[i]);
				}
			}
			_out.println('}');
		}
		return _out.checkError();
	}
	private final boolean _log(LogLevel type, long[] msg,char sep,byte options) {
		if(isSet(options, type))return _out.checkError();
		synchronized(writeSync){
			_out.print(_getPreMessage(type));
			_out.print(_cName);
			_out.print(": long[]{");
			if (msg.length>0) {
				_out.print(msg[0]);
				for (int i = 1; i < msg.length; i++) {
					_out.print(sep);
					_out.print(msg[i]);
				}
			}
			_out.println('}');
		}
		return _out.checkError();
	}
	private final boolean _log(LogLevel type, float[] msg,char sep,byte options) {
		if(isSet(options, type))return _out.checkError();
		synchronized(writeSync){
			_out.print(_getPreMessage(type));
			_out.print(_cName);
			_out.print(": float[]{");
			if (msg.length>0) {
				_out.print(msg[0]);
				for (int i = 1; i < msg.length; i++) {
					_out.print(sep);
					_out.print(msg[i]);
				}
			}
			_out.println('}');
		}
		return _out.checkError();
	}
	private final boolean _log(LogLevel type, double[] msg,char sep,byte options) {
		if(isSet(options, type))return _out.checkError();
		synchronized(writeSync){
			_out.print(_getPreMessage(type));
			_out.print(_cName);
			_out.print(": double[]{");
			if (msg.length>0) {
				_out.print(msg[0]);
				for (int i = 1; i < msg.length; i++) {
					_out.print(sep);
					_out.print(msg[i]);
				}
			}
			_out.println('}');
		}
		return _out.checkError();
	}
	private final boolean _log(LogLevel type, Object[] msg,char sep,byte options) {
		if(isSet(options, type))return _out.checkError();
		synchronized(writeSync){
			_out.print(_getPreMessage(type));
			_out.print(_cName);
			_out.print(": Object[]{");
			if (msg.length>0) {
				_out.print(msg[0]);
				for (int i = 1; i < msg.length; i++) {
					_out.print(sep);
					_out.print(msg[i]);
				}
			}
			_out.println('}');
		}
		return _out.checkError();
	}
	private final boolean _log(LogLevel type, String ref, Object[] msg,char sep,byte options) {
		if(isSet(options, type))return _out.checkError();
		synchronized(writeSync){
			_out.print(_getPreMessage(type));
			_out.print(_cName);
			_out.print(": ");
			_out.print(ref);
			_out.print(" : Object[]{");
			if (msg.length>0) {
				_out.print(msg[0]);
				for (int i = 1; i < msg.length; i++) {
					_out.print(sep);
					_out.print(msg[i]);
				}
			}
			_out.println('}');
		}
		return _out.checkError();
	}
	private final boolean _log(LogLevel type, String ref, byte[] msg,char sep,byte options) {
		if(isSet(options, type))return _out.checkError();
		synchronized(writeSync){
			_out.print(_getPreMessage(type));
			_out.print(_cName);
			_out.print(": ");
			_out.print(ref);
			_out.print(" : byte[]{");
			if (msg.length>0) {
				_out.print(msg[0]);
				for (int i = 1; i < msg.length; i++) {
					_out.print(sep);
					_out.print(msg[i]);
				}
			}
			_out.println('}');
		}
		return _out.checkError();
	}
	private final boolean _log(LogLevel type, String ref, int[] msg,char sep,byte options) {
		if(isSet(options, type))return _out.checkError();
		synchronized(writeSync){
			_out.print(_getPreMessage(type));
			_out.print(_cName);
			_out.print(": ");
			_out.print(ref);
			_out.print(" : int[]{");
			if (msg.length>0) {
				_out.print(msg[0]);
				for (int i = 1; i < msg.length; i++) {
					_out.print(sep);
					_out.print(msg[i]);
				}
			}
			_out.println('}');
		}
		return _out.checkError();
	}
	private final boolean _log(LogLevel type, String ref, short[] msg,char sep,byte options) {
		if(isSet(options, type))return _out.checkError();
		synchronized(writeSync){
			_out.print(_getPreMessage(type));
			_out.print(_cName);
			_out.print(": ");
			_out.print(ref);
			_out.print(" : short[]{");
			if (msg.length>0) {
				_out.print(msg[0]);
				for (int i = 1; i < msg.length; i++) {
					_out.print(sep);
					_out.print(msg[i]);
				}
			}
			_out.println('}');
		}
		return _out.checkError();
	}
	private final boolean _log(LogLevel type, String ref, long[] msg,char sep,byte options) {
		if(isSet(options, type))return _out.checkError();
		synchronized(writeSync){
			_out.print(_getPreMessage(type));
			_out.print(_cName);
			_out.print(": ");
			_out.print(ref);
			_out.print(" : long[]{");
			if (msg.length>0) {
				_out.print(msg[0]);
				for (int i = 1; i < msg.length; i++) {
					_out.print(sep);
					_out.print(msg[i]);
				}
			}
			_out.println('}');
		}
		return _out.checkError();
	}
	private final boolean _log(LogLevel type, String ref, float[] msg,char sep,byte options) {
		if(isSet(options, type))return _out.checkError();
		synchronized(writeSync){
			_out.print(_getPreMessage(type));
			_out.print(_cName);
			_out.print(": ");
			_out.print(ref);
			_out.print(" : float[]{");
			if (msg.length>0) {
				_out.print(msg[0]);
				for (int i = 1; i < msg.length; i++) {
					_out.print(sep);
					_out.print(msg[i]);
				}
			}
			_out.println('}');
		}
		return _out.checkError();
	}
	private final boolean _log(LogLevel type, String ref, double[] msg,char sep,byte options) {
		if(isSet(options, type))return _out.checkError();
		synchronized(writeSync){
			_out.print(_getPreMessage(type));
			_out.print(_cName);
			_out.print(": ");
			_out.print(ref);
			_out.print(" : double[]{");
			if (msg.length>0) {
				_out.print(msg[0]);
				for (int i = 1; i < msg.length; i++) {
					_out.print(sep);
					_out.print(msg[i]);
				}
			}
			_out.println('}');
		}
		return _out.checkError();
	}
	private final String _getPreMessage(LogLevel type){
		return type + ": " + ( (_printDate) ? (LogFactory.getDateStamp()+" ") : "" ) + ( (_printTime) ? (LogFactory.getTimeStamp()+" ") : "");
	}
	/* **************************************
	 * *************************** NOTE:DEBUG
	 * **************************************
	 */
	public final boolean debugSect(String section, Object    msg){return log(section,LogLevel.DEBUG, msg);}
	public final boolean debugSect(String section, Object[]  msg){return log(section,LogLevel.DEBUG, msg);}
	public final boolean debugSect(String section, Throwable msg){return log(section,LogLevel.DEBUG, msg);}
	public final boolean debugSect(String section, Dictionary<?,?> msg){return log(section,LogLevel.DEBUG, msg);}
	public final boolean debugSect(String section, String ref, Object    msg){return log(section,LogLevel.DEBUG, ref, msg);}
	public final boolean debugSect(String section, String msg, Throwable e)  {return log(section,LogLevel.DEBUG, msg, e);}
	public final boolean debug(Object      msg){return log(LogLevel.DEBUG, msg);}
	public final boolean debug(Iterable<?> msg){return log(LogLevel.DEBUG, msg);}
	public final boolean debug(Throwable msg){return log(LogLevel.DEBUG, msg);}
	public final boolean debug(Dictionary<?,?> msg)        {return log(LogLevel.DEBUG, msg);}
	public final boolean debug(Object[] msg){return log(LogLevel.DEBUG, msg);}
	public final boolean debug(byte[]   msg){return log(LogLevel.DEBUG, msg);}
	public final boolean debug(int[]    msg){return log(LogLevel.DEBUG, msg);}
	public final boolean debug(short[]  msg){return log(LogLevel.DEBUG, msg);}
	public final boolean debug(float[]  msg){return log(LogLevel.DEBUG, msg);}
	public final boolean debug(long[]   msg){return log(LogLevel.DEBUG, msg);}
	public final boolean debug(double[] msg){return log(LogLevel.DEBUG, msg);}
	public final boolean debug(Object[] msg, char sep){return log(LogLevel.DEBUG, msg, sep);}
	public final boolean debug(byte[]   msg, char sep){return log(LogLevel.DEBUG, msg, sep);}
	public final boolean debug(int[]    msg, char sep){return log(LogLevel.DEBUG, msg, sep);}
	public final boolean debug(short[]  msg, char sep){return log(LogLevel.DEBUG, msg, sep);}
	public final boolean debug(float[]  msg, char sep){return log(LogLevel.DEBUG, msg, sep);}
	public final boolean debug(long[]   msg, char sep){return log(LogLevel.DEBUG, msg, sep);}
	public final boolean debug(double[] msg, char sep){return log(LogLevel.DEBUG, msg, sep);}

	public final boolean debug(String ref, Object      msg){return log(LogLevel.DEBUG, ref, msg);}
	public final boolean debug(String ref, Iterable<?> msg){return log(LogLevel.DEBUG, ref,msg);}
	public final boolean debug(String ref, Throwable   msg){return log(LogLevel.DEBUG, ref, msg);}
	public final boolean debug(String ref, Object[] msg){return log(LogLevel.DEBUG, ref, msg, ',');}
	public final boolean debug(String ref, byte[]   msg){return log(LogLevel.DEBUG, ref, msg, ',');}
	public final boolean debug(String ref, int[]    msg){return log(LogLevel.DEBUG, ref, msg, ',');}
	public final boolean debug(String ref, short[]  msg){return log(LogLevel.DEBUG, ref, msg, ',');}
	public final boolean debug(String ref, float[]  msg){return log(LogLevel.DEBUG, ref, msg, ',');}
	public final boolean debug(String ref, long[]   msg){return log(LogLevel.DEBUG, ref, msg, ',');}
	public final boolean debug(String ref, double[] msg){return log(LogLevel.DEBUG, ref, msg, ',');}
	public final boolean debug(String ref, Object[] msg, char sep){return log(LogLevel.DEBUG, ref, msg, sep);}
	public final boolean debug(String ref, byte[]   msg, char sep){return log(LogLevel.DEBUG, ref, msg, sep);}
	public final boolean debug(String ref, int[]    msg, char sep){return log(LogLevel.DEBUG, ref, msg, sep);}
	public final boolean debug(String ref, short[]  msg, char sep){return log(LogLevel.DEBUG, ref, msg, sep);}
	public final boolean debug(String ref, float[]  msg, char sep){return log(LogLevel.DEBUG, ref, msg, sep);}
	public final boolean debug(String ref, long[]   msg, char sep){return log(LogLevel.DEBUG, ref, msg, sep);}
	public final boolean debug(String ref, double[] msg, char sep){return log(LogLevel.DEBUG, ref, msg, sep);}

	/* **************************************
	 * *************************** NOTE:ERROR
	 * **************************************
	 */
	public final boolean errorSect(String section, Object msg) {
		return log(section,LogLevel.ERROR, msg);
	}
	public final boolean errorSect(String section, Object[] msg) {
		return log(section,LogLevel.ERROR, msg);
	}
	public final boolean errorSect(String section, Throwable msg) {
		return log(section,LogLevel.ERROR, msg);
	}
	public final boolean errorSect(String section, String msg, Throwable e) {
		return log(section,LogLevel.ERROR, msg, e);
	}
	public final boolean errorSect(String section, String ref, Object msg) {
		return log(section,LogLevel.ERROR, ref, msg);
	}
	public final boolean errorSect(String section, String ref, Object[] msg) {
		return log(section,LogLevel.ERROR, ref, msg);
	}
	public final boolean error(Object   msg){return log(LogLevel.ERROR, msg);}
	public final boolean error(Object[] msg){return log(LogLevel.ERROR, msg);}
	public final boolean error(byte[]   msg){return log(LogLevel.ERROR, msg);}
	public final boolean error(int[]    msg){return log(LogLevel.ERROR, msg);}
	public final boolean error(short[]  msg){return log(LogLevel.ERROR, msg);}
	public final boolean error(float[]  msg){return log(LogLevel.ERROR, msg);}
	public final boolean error(long[]   msg){return log(LogLevel.ERROR, msg);}
	public final boolean error(double[] msg){return log(LogLevel.ERROR, msg);}
	public final boolean error(Object[] msg, char sep){return log(LogLevel.ERROR, msg,sep);}
	public final boolean error(byte[]   msg, char sep){return log(LogLevel.ERROR, msg,sep);}
	public final boolean error(int[]    msg, char sep){return log(LogLevel.ERROR, msg,sep);}
	public final boolean error(short[]  msg, char sep){return log(LogLevel.ERROR, msg,sep);}
	public final boolean error(float[]  msg, char sep){return log(LogLevel.ERROR, msg,sep);}
	public final boolean error(long[]   msg, char sep){return log(LogLevel.ERROR, msg,sep);}
	public final boolean error(double[] msg, char sep){return log(LogLevel.ERROR, msg,sep);}
	public final boolean error(Throwable       msg){return log(LogLevel.ERROR, msg);}
	public final boolean error(Iterable<?>     msg){return log(LogLevel.ERROR, msg);}
	public final boolean error(Dictionary<?,?> msg){return log(LogLevel.ERROR, msg);}

	public final boolean error(String ref, Object    msg){return log(LogLevel.ERROR, ref, msg);}
	public final boolean error(String ref, Throwable msg){return log(LogLevel.ERROR, ref, msg);}
	public final boolean error(String ref, Object[]  msg){return log(LogLevel.ERROR, ref, msg, ',');}
	public final boolean error(String ref, byte[]    msg){return log(LogLevel.ERROR, ref, msg, ',');}
	public final boolean error(String ref, int[]     msg){return log(LogLevel.ERROR, ref, msg, ',');}
	public final boolean error(String ref, short[]   msg){return log(LogLevel.ERROR, ref, msg, ',');}
	public final boolean error(String ref, float[]   msg){return log(LogLevel.ERROR, ref, msg, ',');}
	public final boolean error(String ref, long[]    msg){return log(LogLevel.ERROR, ref, msg, ',');}
	public final boolean error(String ref, double[]  msg){return log(LogLevel.ERROR, ref, msg, ',');}
	public final boolean error(String ref, Object[]  msg, char sep){return log(LogLevel.ERROR, ref, msg, sep);}
	public final boolean error(String ref, byte[]    msg, char sep){return log(LogLevel.ERROR, ref, msg, sep);}
	public final boolean error(String ref, int[]     msg, char sep){return log(LogLevel.ERROR, ref, msg, sep);}
	public final boolean error(String ref, short[]   msg, char sep){return log(LogLevel.ERROR, ref, msg, sep);}
	public final boolean error(String ref, float[]   msg, char sep){return log(LogLevel.ERROR, ref, msg, sep);}
	public final boolean error(String ref, long[]    msg, char sep){return log(LogLevel.ERROR, ref, msg, sep);}
	public final boolean error(String ref, double[]  msg, char sep){return log(LogLevel.ERROR, ref, msg, sep);}
	/* **************************************
	 * ************************* NOTE:WARNING
	 * **************************************
	 */
	public final boolean warningSect(String section, Object msg) {
		return log(section, LogLevel.WARNING, msg);
	}
	public final boolean warningSect(String section, Object[] msg) {
		return log(section, LogLevel.WARNING, msg);
	}
	public final boolean warningSect(String section, Throwable msg) {
		return log(section, LogLevel.WARNING, msg);
	}
	public final boolean warningSect(String section, String msg, Throwable e) {
		return log(section, LogLevel.WARNING, msg, e);
	}
	public final boolean warningSect(String section, String ref, Object msg) {
		return log(section, LogLevel.WARNING, ref, msg);
	}
	public final boolean warningSect(String section, String ref, Object[] msg) {
		return log(section, LogLevel.WARNING, ref, msg);
	}
	public final boolean warning(Object   msg)	{return log(LogLevel.WARNING, msg);}
	public final boolean warning(Object[] msg)	{return log(LogLevel.WARNING, msg);}
	public final boolean warning(byte[]   msg)	{return log(LogLevel.WARNING, msg);}
	public final boolean warning(int[]    msg)		{return log(LogLevel.WARNING, msg);}
	public final boolean warning(short[]  msg)	{return log(LogLevel.WARNING, msg);}
	public final boolean warning(float[]  msg)	{return log(LogLevel.WARNING, msg);}
	public final boolean warning(long[]   msg)	{return log(LogLevel.WARNING, msg);}
	public final boolean warning(double[] msg)	{return log(LogLevel.WARNING, msg);}
	public final boolean warning(Object[] msg, char sep){return log(LogLevel.WARNING, msg,sep);}
	public final boolean warning(byte[]   msg, char sep){return log(LogLevel.WARNING, msg,sep);}
	public final boolean warning(int[]    msg, char sep){return log(LogLevel.WARNING, msg,sep);}
	public final boolean warning(short[]  msg, char sep){return log(LogLevel.WARNING, msg,sep);}
	public final boolean warning(float[]  msg, char sep){return log(LogLevel.WARNING, msg,sep);}
	public final boolean warning(long[]   msg, char sep){return log(LogLevel.WARNING, msg,sep);}
	public final boolean warning(double[] msg, char sep){return log(LogLevel.WARNING, msg,sep);}
	public final boolean warning(Throwable       msg){return log(LogLevel.WARNING, msg);}
	public final boolean warning(Iterable<?>     msg){return log(LogLevel.WARNING, msg);}
	public final boolean warning(Dictionary<?,?> msg)	{return log(LogLevel.WARNING, msg);}
	public final boolean warning(String ref, Iterable<?> msg){return log(LogLevel.WARNING, ref,msg);}
	public final boolean warning(String ref, Object      msg){return log(LogLevel.WARNING, ref, msg);}
	public final boolean warning(String ref, Throwable   msg){return log(LogLevel.WARNING, ref, msg);}
	public final boolean warning(String ref, Object[]    msg){return log(LogLevel.WARNING, ref, msg,',');}
	public final boolean warning(String ref, byte[]      msg){return log(LogLevel.WARNING, ref, msg,',');}
	public final boolean warning(String ref, int[]       msg){return log(LogLevel.WARNING, ref, msg,',');}
	public final boolean warning(String ref, short[]     msg){return log(LogLevel.WARNING, ref, msg,',');}
	public final boolean warning(String ref, float[]     msg){return log(LogLevel.WARNING, ref, msg,',');}
	public final boolean warning(String ref, long[]      msg){return log(LogLevel.WARNING, ref, msg,',');}
	public final boolean warning(String ref, double[]    msg){return log(LogLevel.WARNING, ref, msg,',');}
	public final boolean warning(String ref, Object[]    msg, char sep){return log(LogLevel.WARNING, ref, msg,sep);}
	public final boolean warning(String ref, byte[]      msg, char sep){return log(LogLevel.WARNING, ref, msg,sep);}
	public final boolean warning(String ref, int[]       msg, char sep){return log(LogLevel.WARNING, ref, msg,sep);}
	public final boolean warning(String ref, short[]     msg, char sep){return log(LogLevel.WARNING, ref, msg,sep);}
	public final boolean warning(String ref, float[]     msg, char sep){return log(LogLevel.WARNING, ref, msg,sep);}
	public final boolean warning(String ref, long[]      msg, char sep){return log(LogLevel.WARNING, ref, msg,sep);}
	public final boolean warning(String ref, double[]    msg, char sep){return log(LogLevel.WARNING, ref, msg,sep);}
	/* **************************************
	 * ********************* NOTE:INFORMATION
	 * **************************************
	 */
	public final boolean informationSect(String section, Object msg) {
		return log(section, LogLevel.INFORMATION, msg);
	}
	public final boolean informationSect(String section, Dictionary<?,?> msg) {
		return log(section, LogLevel.INFORMATION, msg);
	}
	public final boolean informationSect(String section, String ref, Object[] msg) {
		return log(section, LogLevel.INFORMATION, ref, msg);
	}
	public final boolean informationSect(String section, String ref, Object msg) {
		return log(section, LogLevel.INFORMATION, ref, msg);
	}
	public final boolean information(Object   msg){return log(LogLevel.INFORMATION, msg);}
	public final boolean information(Object[] msg){return log(LogLevel.INFORMATION, msg);}
	public final boolean information(byte[]   msg){return log(LogLevel.INFORMATION, msg);}
	public final boolean information(int[]    msg){return log(LogLevel.INFORMATION, msg);}
	public final boolean information(short[]  msg){return log(LogLevel.INFORMATION, msg);}
	public final boolean information(float[]  msg){return log(LogLevel.INFORMATION, msg);}
	public final boolean information(long[]   msg){return log(LogLevel.INFORMATION, msg);}
	public final boolean information(double[] msg){return log(LogLevel.INFORMATION, msg);}
	public final boolean information(Object[] msg, char sep){return log(LogLevel.INFORMATION, msg,sep);}
	public final boolean information(byte[]   msg, char sep){return log(LogLevel.INFORMATION, msg,sep);}
	public final boolean information(int[]    msg, char sep){return log(LogLevel.INFORMATION, msg,sep);}
	public final boolean information(short[]  msg, char sep){return log(LogLevel.INFORMATION, msg,sep);}
	public final boolean information(float[]  msg, char sep){return log(LogLevel.INFORMATION, msg,sep);}
	public final boolean information(long[]   msg, char sep){return log(LogLevel.INFORMATION, msg,sep);}
	public final boolean information(double[] msg, char sep){return log(LogLevel.INFORMATION, msg,sep);}
	public final boolean information(Throwable       msg){return log(LogLevel.INFORMATION, msg);}
	public final boolean information(Dictionary<?,?> msg){return log(LogLevel.INFORMATION, msg);}
	public final boolean information(Iterable<?>     msg){return log(LogLevel.INFORMATION, msg);}
	public final boolean information(String ref, Iterable<?> msg){return log(LogLevel.INFORMATION, ref,msg);}
	public final boolean information(String ref, Object      msg){return log(LogLevel.INFORMATION, ref, msg);}
	public final boolean information(String ref, Throwable   msg){return log(LogLevel.INFORMATION, ref, msg);}
	public final boolean information(String ref, Object[]    msg){return log(LogLevel.INFORMATION, ref, msg,',');}
	public final boolean information(String ref, byte[]      msg){return log(LogLevel.INFORMATION, ref, msg,',');}
	public final boolean information(String ref, int[]       msg){return log(LogLevel.INFORMATION, ref, msg,',');}
	public final boolean information(String ref, short[]     msg){return log(LogLevel.INFORMATION, ref, msg,',');}
	public final boolean information(String ref, float[]     msg){return log(LogLevel.INFORMATION, ref, msg,',');}
	public final boolean information(String ref, long[]      msg){return log(LogLevel.INFORMATION, ref, msg,',');}
	public final boolean information(String ref, double[]    msg){return log(LogLevel.INFORMATION, ref, msg,',');}
	public final boolean information(String ref, Object[]    msg, char sep){return log(LogLevel.INFORMATION, ref, msg,sep);}
	public final boolean information(String ref, byte[]      msg, char sep){return log(LogLevel.INFORMATION, ref, msg,sep);}
	public final boolean information(String ref, int[]       msg, char sep){return log(LogLevel.INFORMATION, ref, msg,sep);}
	public final boolean information(String ref, short[]     msg, char sep){return log(LogLevel.INFORMATION, ref, msg,sep);}
	public final boolean information(String ref, float[]     msg, char sep){return log(LogLevel.INFORMATION, ref, msg,sep);}
	public final boolean information(String ref, long[]      msg, char sep){return log(LogLevel.INFORMATION, ref, msg,sep);}
	public final boolean information(String ref, double[]    msg, char sep){return log(LogLevel.INFORMATION, ref, msg,sep);}
	@Override
	public void close() throws Exception {
		_out.close();
	}
}
