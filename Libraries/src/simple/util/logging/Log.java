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
public final class Log {
	private ErrorCodeResolver ecr=LogFactory.DEFAULTECR;
	private PrintWriter _out;
	private final String _cName;
	private boolean _printTime = false,
		_printDate=false;
	private final Object writeSync=new Object();
	public static final byte LDEBUG = LogLevel.DEBUG.getValue(),
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
	private byte options = (byte)0xFF;
	public Log(final Class<?> clazz) {
		this(System.out, clazz);
	}
	public Log(final OutputStream os, final Class<?> clazz) {
		_cName = clazz.getCanonicalName();
		_out = new PrintWriter(new OutputStreamWriter(os));
	}
	public Log(final Writer os, final Class<?> clazz) {
		_cName = clazz.getCanonicalName();
		if (os instanceof PrintWriter)
			_out = (PrintWriter)os;
		else
			_out = new PrintWriter(os);
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
		if (print)
			options |= level.getValue();
		else
			options &= ~level.getValue();
	}
	public boolean getPrint(final LogLevel level) {
		return (options & level.getValue()) == level.getValue();
	}
	public void setPrintDebug(final boolean b) {setPrint(LogLevel.DEBUG,b);}
	public void setPrintError(final boolean b) {setPrint(LogLevel.ERROR,b);}
	public void setPrintWarning(final boolean b) {setPrint(LogLevel.WARNING,b);}
	public void setPrintInformation(final boolean b) {setPrint(LogLevel.INFORMATION,b);}
	public synchronized void setSection(final String section, final LogLevel level, final boolean print) {
		Byte options = this.section.remove(section);
		if (options==null)
			options=0;
		if (print)
			options =(byte) (options.byteValue()|level.getValue());
		else if ((options&level.getValue())==level.getValue())
			options=(byte) (options.byteValue()&~level.getValue());
		this.section.put(section, options);
	}
	public void setStream(final OutputStream os) {
		_out = new PrintWriter(new OutputStreamWriter(os));
	}
	public void setStream(final Writer os) {
		if (os instanceof PrintWriter)
			_out = (PrintWriter)os;
		else
			_out = new PrintWriter(os);
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
	public final boolean log(final LogLevel type, final Object msg){return _log(type,msg,options);}
	public final boolean log(final LogLevel type, final Exception msg){return _log(type,msg,options);}
	public final boolean log(final LogLevel type, final Dictionary<?,?> msg){return _log(type,msg,options);}
	public final boolean log(final LogLevel type, final String msg, final Exception e){return _log(type,msg,e,options);}
	public final boolean log(final LogLevel type, final Iterable<?> msg){return _log(type,msg,',',options);}
	public final boolean log(final LogLevel type, final Object[] msg){return _log(type,msg,',',options);}
	public final boolean log(final LogLevel type, final byte[] msg){return _log(type,msg,',',options);}
	public final boolean log(final LogLevel type, final short[] msg){return _log(type,msg,',',options);}
	public final boolean log(final LogLevel type, final int[] msg){return _log(type,msg,',',options);}
	public final boolean log(final LogLevel type, final long[] msg){return _log(type,msg,',',options);}
	public final boolean log(final LogLevel type, final float[] msg){return _log(type,msg,',',options);}
	public final boolean log(final LogLevel type, final double[] msg){return _log(type,msg,',',options);}

	public final boolean log(final LogLevel type, final Object[] msg,char sep){return _log(type,msg,sep,options);}
	public final boolean log(final LogLevel type, final byte[] msg,char sep){return _log(type,msg,sep,options);}
	public final boolean log(final LogLevel type, final short[] msg,char sep){return _log(type,msg,sep,options);}
	public final boolean log(final LogLevel type, final int[] msg,char sep){return _log(type,msg,sep,options);}
	public final boolean log(final LogLevel type, final long[] msg,char sep){return _log(type,msg,sep,options);}
	public final boolean log(final LogLevel type, final float[] msg,char sep){return _log(type,msg,sep,options);}
	public final boolean log(final LogLevel type, final double[] msg,char sep){return _log(type,msg,sep,options);}
/*
 * Reference Prefix
 */
	public final boolean log(final LogLevel type, final String ref, final Iterable<?> msg){return _log(type,ref,msg,',',options);}
	public final boolean log(final LogLevel type, final String ref, final Object msg){return _log(type,ref,msg,options);}
	public final boolean log(final LogLevel type, final String ref, final Object[] msg){return _log(type,ref,msg,',',options);}
	public final boolean log(final LogLevel type, final String ref, final byte[] msg){return _log(type,ref,msg,',',options);}
	public final boolean log(final LogLevel type, final String ref, final short[] msg){return _log(type,ref,msg,',',options);}
	public final boolean log(final LogLevel type, final String ref, final int[] msg){return _log(type,ref,msg,',',options);}
	public final boolean log(final LogLevel type, final String ref, final long[] msg){return _log(type,ref,msg,',',options);}
	public final boolean log(final LogLevel type, final String ref, final float[] msg){return _log(type,ref,msg,',',options);}
	public final boolean log(final LogLevel type, final String ref, final double[] msg){return _log(type,ref,msg,',',options);}

	public final boolean log(final LogLevel type, final String ref, final Iterable<?> msg,char sep){return _log(type,ref,msg,sep,options);}
	public final boolean log(final LogLevel type, final String ref, final Object[] msg,char sep){return _log(type,ref,msg,sep,options);}
	public final boolean log(final LogLevel type, final String ref, final byte[] msg,char sep){return _log(type,ref,msg,sep,options);}
	public final boolean log(final LogLevel type, final String ref, final short[] msg,char sep){return _log(type,ref,msg,sep,options);}
	public final boolean log(final LogLevel type, final String ref, final int[] msg,char sep){return _log(type,ref,msg,sep,options);}
	public final boolean log(final LogLevel type, final String ref, final long[] msg,char sep){return _log(type,ref,msg,sep,options);}
	public final boolean log(final LogLevel type, final String ref, final float[] msg,char sep){return _log(type,ref,msg,sep,options);}
	public final boolean log(final LogLevel type, final String ref, final double[] msg,char sep){return _log(type,ref,msg,sep,options);}
	//Sectioned loggers
	public final boolean log(final String section,final LogLevel type, final Object msg) {
		Byte options = this.section.get(section);
		if(options==null)	options=this.options;
		return _log(type,msg,options);
	}
	public final boolean log(final String section,final LogLevel type, final Exception msg) {
		Byte options = this.section.get(section);
		if(options==null)	options=this.options;
		return _log(type,msg,options);
	}
	public final boolean log(final String section,final LogLevel type, final Dictionary<?,?> msg) {
		Byte options = this.section.get(section);
		if(options==null)	options=this.options;
		return _log(type,msg,options);
	}
	public final boolean log(final String section,final LogLevel type, final String ref, final Object msg) {
		Byte options = this.section.get(section);
		if(options==null)	options=this.options;
		return _log(type,ref,msg,options);
	}
	public final boolean log(final String section,final LogLevel type, final String msg, final Exception e) {
		Byte options = this.section.get(section);
		if(options==null)	options=this.options;
		return _log(type,msg,e,options);
	}
	public final boolean log(final String section,final LogLevel type, final Iterable<?> msg) {
		Byte options = this.section.get(section);
		if(options==null)	options=this.options;
		return _log(type,msg,',',options);
	}
	public final boolean log(final String section,final LogLevel type, final String ref, final Iterable<?> msg) {
		Byte options = this.section.get(section);
		if(options==null)	options=this.options;
		return _log(type,ref,msg,',',options);
	}
	public final boolean log(final String section,final LogLevel type, final Object[] msg) {
		Byte options = this.section.get(section);
		if(options==null)	options=this.options;
		return _log(type,msg,',',options);
	}
	public final boolean log(final String section,final LogLevel type, final String ref, final Object[] msg) {
		Byte options = this.section.get(section);
		if(options==null)	options=this.options;
		return _log(type,ref,msg,',',options);
	}
	//Error Code Resolver code
	public final void setECR(ErrorCodeResolver ecr){
		this.ecr=ecr;
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
	public final boolean log(final LogLevel type, ErrorCodeResolver msg, final int code) {
		if ((options&type.getValue()) == type.getValue()) {
			if (msg == null)
				msg = ecr;
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
	public final boolean log(final int errorCode) {
		return log(LogLevel.ERROR, ecr, errorCode);
	}
	//workhorse loggers
	private final boolean _log(final LogLevel type, final Object msg,byte options) {
		if((options&type.getValue()) != type.getValue())return _out.checkError();
		synchronized(writeSync){
		_out.print(_getPreMessage(type));
		_out.print(_cName);
		_out.print(": ");
		_out.println(msg);
		}
		return _out.checkError();
	}
	private final boolean _log(final LogLevel type, final Exception msg,byte options) {
		if((options&type.getValue()) != type.getValue())return _out.checkError();
		synchronized(writeSync){
		_out.println(_getPreMessage(type));
		_out.print(_cName);
		_out.print(": ");
		_out.println(msg);
		msg.printStackTrace(_out);
		}
		return _out.checkError();
	}
	private final boolean _log(final LogLevel type, final Dictionary<?,?> msg,byte options) {
		if((options&type.getValue()) != type.getValue())return _out.checkError();
		synchronized(writeSync){
		_out.print(_getPreMessage(type));
		_out.print(_cName);
		_out.println(": Property listing:");
		final Enumeration<?> keys = msg.keys();
		Object key;
		while(keys.hasMoreElements()) {
			key = keys.nextElement();
			_out.print('[');
			_out.print(key);
			_out.print('=');
			_out.print(msg.get(key));
			_out.println(']');
		}
		}
		return _out.checkError();
	}
	private final boolean _log(final LogLevel type, final String ref, final Object msg,byte options) {
		if((options&type.getValue()) != type.getValue())return _out.checkError();
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
	private final boolean _log(final LogLevel type, final String msg, final Exception e,byte options) {
		if((options&type.getValue()) != type.getValue())return _out.checkError();
		synchronized(writeSync){
		_out.print(_getPreMessage(type));
		_out.print(_cName);
		_out.print(": ");
		_out.println(msg);
		e.printStackTrace(_out);
		}
		return _out.checkError();
	}
	private final boolean _log(final LogLevel type, final Iterable<?> msg,char sep,byte options) {
		if((options&type.getValue()) != type.getValue())return _out.checkError();
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
	private final boolean _log(final LogLevel type, final String ref, final Iterable<?> msg,char sep,byte options) {
		if((options&type.getValue()) != type.getValue())return _out.checkError();
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
	private final boolean _log(final LogLevel type, final byte[] msg,char sep,byte options) {
		if((options&type.getValue()) != type.getValue())return _out.checkError();
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
	private final boolean _log(final LogLevel type, final int[] msg,char sep,byte options) {
		if((options&type.getValue()) != type.getValue())return _out.checkError();
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
	private final boolean _log(final LogLevel type, final short[] msg,char sep,byte options) {
		if((options&type.getValue()) != type.getValue())return _out.checkError();
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
	private final boolean _log(final LogLevel type, final long[] msg,char sep,byte options) {
		if((options&type.getValue()) != type.getValue())return _out.checkError();
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
	private final boolean _log(final LogLevel type, final float[] msg,char sep,byte options) {
		if((options&type.getValue()) != type.getValue())return _out.checkError();
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
	private final boolean _log(final LogLevel type, final double[] msg,char sep,byte options) {
		if((options&type.getValue()) != type.getValue())return _out.checkError();
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
	private final boolean _log(final LogLevel type, final Object[] msg,char sep,byte options) {
		if((options&type.getValue()) != type.getValue())return _out.checkError();
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
	private final boolean _log(final LogLevel type, final String ref, final Object[] msg,char sep,byte options) {
		if((options&type.getValue()) != type.getValue())return _out.checkError();
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
	private final boolean _log(final LogLevel type, final String ref, final byte[] msg,char sep,byte options) {
		if((options&type.getValue()) != type.getValue())return _out.checkError();
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
	private final boolean _log(final LogLevel type, final String ref, final int[] msg,char sep,byte options) {
		if((options&type.getValue()) != type.getValue())return _out.checkError();
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
	private final boolean _log(final LogLevel type, final String ref, final short[] msg,char sep,byte options) {
		if((options&type.getValue()) != type.getValue())return _out.checkError();
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
	private final boolean _log(final LogLevel type, final String ref, final long[] msg,char sep,byte options) {
		if((options&type.getValue()) != type.getValue())return _out.checkError();
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
	private final boolean _log(final LogLevel type, final String ref, final float[] msg,char sep,byte options) {
		if((options&type.getValue()) != type.getValue())return _out.checkError();
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
	private final boolean _log(final LogLevel type, final String ref, final double[] msg,char sep,byte options) {
		if((options&type.getValue()) != type.getValue())return _out.checkError();
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
	private final String _getPreMessage(final LogLevel type){
		return type+": "+((_printDate)?(LogFactory.getDateStamp()+" "):"")+((_printTime)?(LogFactory.getTimeStamp()+" "):"");
	}
	/* **************************************
	 * *************************** NOTE:DEBUG
	 * **************************************
	 */
	public final boolean debugSect(final String section,final Object msg){return log(section,LogLevel.DEBUG, msg);}
	public final boolean debugSect(final String section,final Object[] msg){return log(section,LogLevel.DEBUG, msg);}
	public final boolean debugSect(final String section,final Exception msg){return log(section,LogLevel.DEBUG, msg);}
	public final boolean debugSect(final String section,final Dictionary<?,?> msg){return log(section,LogLevel.DEBUG, msg);}
	public final boolean debugSect(final String section,final String ref, final Object msg){return log(section,LogLevel.DEBUG, ref, msg);}
	public final boolean debugSect(final String section,final String msg, final Exception e){return log(section,LogLevel.DEBUG, msg, e);}
	public final boolean debug(final Object msg)	{return log(LogLevel.DEBUG, msg);}
	public final boolean debug(final Iterable<? extends Object> msg)	{return log(LogLevel.DEBUG, msg);}
	public final boolean debug(final Object[] msg)	{return log(LogLevel.DEBUG, msg);}
	public final boolean debug(final byte[] msg)	{return log(LogLevel.DEBUG, msg);}
	public final boolean debug(final int[] msg)		{return log(LogLevel.DEBUG, msg);}
	public final boolean debug(final short[] msg)	{return log(LogLevel.DEBUG, msg);}
	public final boolean debug(final float[] msg)	{return log(LogLevel.DEBUG, msg);}
	public final boolean debug(final long[] msg)	{return log(LogLevel.DEBUG, msg);}
	public final boolean debug(final double[] msg)	{return log(LogLevel.DEBUG, msg);}
	public final boolean debug(final Object[] msg,char sep)	{return log(LogLevel.DEBUG, msg,sep);}
	public final boolean debug(final byte[] msg,char sep)	{return log(LogLevel.DEBUG, msg,sep);}
	public final boolean debug(final int[] msg,char sep)		{return log(LogLevel.DEBUG, msg,sep);}
	public final boolean debug(final short[] msg,char sep)	{return log(LogLevel.DEBUG, msg,sep);}
	public final boolean debug(final float[] msg,char sep)	{return log(LogLevel.DEBUG, msg,sep);}
	public final boolean debug(final long[] msg,char sep)	{return log(LogLevel.DEBUG, msg,sep);}
	public final boolean debug(final double[] msg,char sep)	{return log(LogLevel.DEBUG, msg,sep);}
	public final boolean debug(final Exception msg)	{return log(LogLevel.DEBUG, msg);}
	public final boolean debug(final Dictionary<?,?> msg)	{return log(LogLevel.DEBUG, msg);}
	public final boolean debug(final String ref,final Object msg)	{return log(LogLevel.DEBUG, ref, msg);}
	public final boolean debug(String ref, final Iterable<? extends Object> msg)	{return log(LogLevel.DEBUG, ref,msg);}
	public final boolean debug(final String msg,final Exception e)	{return log(LogLevel.DEBUG, msg, e);}
	public final boolean debug(final String ref,final Object[] msg){return log(LogLevel.DEBUG, ref, msg,',');}
	public final boolean debug(final String ref,final byte[] msg)	{return log(LogLevel.DEBUG, ref, msg,',');}
	public final boolean debug(final String ref,final int[] msg)	{return log(LogLevel.DEBUG, ref, msg,',');}
	public final boolean debug(final String ref,final short[] msg)	{return log(LogLevel.DEBUG, ref, msg,',');}
	public final boolean debug(final String ref,final float[] msg)	{return log(LogLevel.DEBUG, ref, msg,',');}
	public final boolean debug(final String ref,final long[] msg)	{return log(LogLevel.DEBUG, ref, msg,',');}
	public final boolean debug(final String ref,final double[] msg)	{return log(LogLevel.DEBUG, ref, msg,',');}
	public final boolean debug(final String ref,final Object[] msg,char sep){return log(LogLevel.DEBUG, ref, msg,sep);}
	public final boolean debug(final String ref,final byte[] msg,char sep)	{return log(LogLevel.DEBUG, ref, msg,sep);}
	public final boolean debug(final String ref,final int[] msg,char sep)	{return log(LogLevel.DEBUG, ref, msg,sep);}
	public final boolean debug(final String ref,final short[] msg,char sep)	{return log(LogLevel.DEBUG, ref, msg,sep);}
	public final boolean debug(final String ref,final float[] msg,char sep)	{return log(LogLevel.DEBUG, ref, msg,sep);}
	public final boolean debug(final String ref,final long[] msg,char sep)	{return log(LogLevel.DEBUG, ref, msg,sep);}
	public final boolean debug(final String ref,final double[] msg,char sep)	{return log(LogLevel.DEBUG, ref, msg,sep);}

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
	public final boolean error(final Object msg)	{return log(LogLevel.ERROR, msg);}
	public final boolean error(final Object[] msg)	{return log(LogLevel.ERROR, msg);}
	public final boolean error(final byte[] msg)	{return log(LogLevel.ERROR, msg);}
	public final boolean error(final int[] msg)		{return log(LogLevel.ERROR, msg);}
	public final boolean error(final short[] msg)	{return log(LogLevel.ERROR, msg);}
	public final boolean error(final float[] msg)	{return log(LogLevel.ERROR, msg);}
	public final boolean error(final long[] msg)	{return log(LogLevel.ERROR, msg);}
	public final boolean error(final double[] msg)	{return log(LogLevel.ERROR, msg);}
	public final boolean error(final Object[] msg,char sep)	{return log(LogLevel.ERROR, msg,sep);}
	public final boolean error(final byte[] msg,char sep)	{return log(LogLevel.ERROR, msg,sep);}
	public final boolean error(final int[] msg,char sep)		{return log(LogLevel.ERROR, msg,sep);}
	public final boolean error(final short[] msg,char sep)	{return log(LogLevel.ERROR, msg,sep);}
	public final boolean error(final float[] msg,char sep)	{return log(LogLevel.ERROR, msg,sep);}
	public final boolean error(final long[] msg,char sep)	{return log(LogLevel.ERROR, msg,sep);}
	public final boolean error(final double[] msg,char sep)	{return log(LogLevel.ERROR, msg,sep);}
	public final boolean error(final Exception msg)	{return log(LogLevel.ERROR, msg);}
	public final boolean error(final Iterable<? extends Object> msg)	{return log(LogLevel.ERROR, msg);}
	public final boolean error(final Dictionary<?,?> msg)	{return log(LogLevel.ERROR, msg);}
	public final boolean error(final String ref,final Object msg)	{return log(LogLevel.ERROR, ref, msg);}
	public final boolean error(final String msg,final Exception e)	{return log(LogLevel.ERROR, msg, e);}
	public final boolean error(final String ref,final Object[] msg){return log(LogLevel.ERROR, ref, msg,',');}
	public final boolean error(final String ref,final byte[] msg)	{return log(LogLevel.ERROR, ref, msg,',');}
	public final boolean error(final String ref,final int[] msg)	{return log(LogLevel.ERROR, ref, msg,',');}
	public final boolean error(final String ref,final short[] msg)	{return log(LogLevel.ERROR, ref, msg,',');}
	public final boolean error(final String ref,final float[] msg)	{return log(LogLevel.ERROR, ref, msg,',');}
	public final boolean error(final String ref,final long[] msg)	{return log(LogLevel.ERROR, ref, msg,',');}
	public final boolean error(final String ref,final double[] msg)	{return log(LogLevel.ERROR, ref, msg,',');}
	public final boolean error(final String ref,final Object[] msg,char sep){return log(LogLevel.ERROR, ref, msg,sep);}
	public final boolean error(final String ref,final byte[] msg,char sep)	{return log(LogLevel.ERROR, ref, msg,sep);}
	public final boolean error(final String ref,final int[] msg,char sep)	{return log(LogLevel.ERROR, ref, msg,sep);}
	public final boolean error(final String ref,final short[] msg,char sep)	{return log(LogLevel.ERROR, ref, msg,sep);}
	public final boolean error(final String ref,final float[] msg,char sep)	{return log(LogLevel.ERROR, ref, msg,sep);}
	public final boolean error(final String ref,final long[] msg,char sep)	{return log(LogLevel.ERROR, ref, msg,sep);}
	public final boolean error(final String ref,final double[] msg,char sep)	{return log(LogLevel.ERROR, ref, msg,sep);}
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
	public final boolean warning(final Object msg)	{return log(LogLevel.WARNING, msg);}
	public final boolean warning(final Object[] msg)	{return log(LogLevel.WARNING, msg);}
	public final boolean warning(final byte[] msg)	{return log(LogLevel.WARNING, msg);}
	public final boolean warning(final int[] msg)		{return log(LogLevel.WARNING, msg);}
	public final boolean warning(final short[] msg)	{return log(LogLevel.WARNING, msg);}
	public final boolean warning(final float[] msg)	{return log(LogLevel.WARNING, msg);}
	public final boolean warning(final long[] msg)	{return log(LogLevel.WARNING, msg);}
	public final boolean warning(final double[] msg)	{return log(LogLevel.WARNING, msg);}
	public final boolean warning(final Object[] msg,char sep)	{return log(LogLevel.WARNING, msg,sep);}
	public final boolean warning(final byte[] msg,char sep)	{return log(LogLevel.WARNING, msg,sep);}
	public final boolean warning(final int[] msg,char sep)		{return log(LogLevel.WARNING, msg,sep);}
	public final boolean warning(final short[] msg,char sep)	{return log(LogLevel.WARNING, msg,sep);}
	public final boolean warning(final float[] msg,char sep)	{return log(LogLevel.WARNING, msg,sep);}
	public final boolean warning(final long[] msg,char sep)	{return log(LogLevel.WARNING, msg,sep);}
	public final boolean warning(final double[] msg,char sep)	{return log(LogLevel.WARNING, msg,sep);}
	public final boolean warning(final Exception msg)	{return log(LogLevel.WARNING, msg);}
	public final boolean warning(final Iterable<? extends Object> msg)	{return log(LogLevel.WARNING, msg);}
	public final boolean warning(String ref,final Iterable<? extends Object> msg)	{return log(LogLevel.WARNING, ref,msg);}
	public final boolean warning(final Dictionary<?,?> msg)	{return log(LogLevel.WARNING, msg);}
	public final boolean warning(final String ref,final Object msg)	{return log(LogLevel.WARNING, ref, msg);}
	public final boolean warning(final String msg,final Exception e)	{return log(LogLevel.WARNING, msg, e);}
	public final boolean warning(final String ref,final Object[] msg){return log(LogLevel.WARNING, ref, msg,',');}
	public final boolean warning(final String ref,final byte[] msg)	{return log(LogLevel.WARNING, ref, msg,',');}
	public final boolean warning(final String ref,final int[] msg)	{return log(LogLevel.WARNING, ref, msg,',');}
	public final boolean warning(final String ref,final short[] msg)	{return log(LogLevel.WARNING, ref, msg,',');}
	public final boolean warning(final String ref,final float[] msg)	{return log(LogLevel.WARNING, ref, msg,',');}
	public final boolean warning(final String ref,final long[] msg)	{return log(LogLevel.WARNING, ref, msg,',');}
	public final boolean warning(final String ref,final double[] msg)	{return log(LogLevel.WARNING, ref, msg,',');}
	public final boolean warning(final String ref,final Object[] msg,char sep){return log(LogLevel.WARNING, ref, msg,sep);}
	public final boolean warning(final String ref,final byte[] msg,char sep)	{return log(LogLevel.WARNING, ref, msg,sep);}
	public final boolean warning(final String ref,final int[] msg,char sep)	{return log(LogLevel.WARNING, ref, msg,sep);}
	public final boolean warning(final String ref,final short[] msg,char sep)	{return log(LogLevel.WARNING, ref, msg,sep);}
	public final boolean warning(final String ref,final float[] msg,char sep)	{return log(LogLevel.WARNING, ref, msg,sep);}
	public final boolean warning(final String ref,final long[] msg,char sep)	{return log(LogLevel.WARNING, ref, msg,sep);}
	public final boolean warning(final String ref,final double[] msg,char sep)	{return log(LogLevel.WARNING, ref, msg,sep);}
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
	public final boolean information(final Object msg)	{return log(LogLevel.INFORMATION, msg);}
	public final boolean information(final Object[] msg)	{return log(LogLevel.INFORMATION, msg);}
	public final boolean information(final byte[] msg)	{return log(LogLevel.INFORMATION, msg);}
	public final boolean information(final int[] msg)		{return log(LogLevel.INFORMATION, msg);}
	public final boolean information(final short[] msg)	{return log(LogLevel.INFORMATION, msg);}
	public final boolean information(final float[] msg)	{return log(LogLevel.INFORMATION, msg);}
	public final boolean information(final long[] msg)	{return log(LogLevel.INFORMATION, msg);}
	public final boolean information(final double[] msg)	{return log(LogLevel.INFORMATION, msg);}
	public final boolean information(final Object[] msg,char sep)	{return log(LogLevel.INFORMATION, msg,sep);}
	public final boolean information(final byte[] msg,char sep)	{return log(LogLevel.INFORMATION, msg,sep);}
	public final boolean information(final int[] msg,char sep)		{return log(LogLevel.INFORMATION, msg,sep);}
	public final boolean information(final short[] msg,char sep)	{return log(LogLevel.INFORMATION, msg,sep);}
	public final boolean information(final float[] msg,char sep)	{return log(LogLevel.INFORMATION, msg,sep);}
	public final boolean information(final long[] msg,char sep)	{return log(LogLevel.INFORMATION, msg,sep);}
	public final boolean information(final double[] msg,char sep)	{return log(LogLevel.INFORMATION, msg,sep);}
	public final boolean information(final Exception msg)	{return log(LogLevel.INFORMATION, msg);}
	public final boolean information(final Dictionary<?,?> msg)	{return log(LogLevel.INFORMATION, msg);}
	public final boolean information(final Iterable<? extends Object> msg)	{return log(LogLevel.INFORMATION, msg);}
	public final boolean information(String ref,final Iterable<? extends Object> msg)	{return log(LogLevel.INFORMATION, ref,msg);}
	public final boolean information(final String ref,final Object msg)	{return log(LogLevel.INFORMATION, ref, msg);}
	public final boolean information(final String msg,final Exception e)	{return log(LogLevel.INFORMATION, msg, e);}
	public final boolean information(final String ref,final Object[] msg){return log(LogLevel.INFORMATION, ref, msg,',');}
	public final boolean information(final String ref,final byte[] msg)	{return log(LogLevel.INFORMATION, ref, msg,',');}
	public final boolean information(final String ref,final int[] msg)	{return log(LogLevel.INFORMATION, ref, msg,',');}
	public final boolean information(final String ref,final short[] msg)	{return log(LogLevel.INFORMATION, ref, msg,',');}
	public final boolean information(final String ref,final float[] msg)	{return log(LogLevel.INFORMATION, ref, msg,',');}
	public final boolean information(final String ref,final long[] msg)	{return log(LogLevel.INFORMATION, ref, msg,',');}
	public final boolean information(final String ref,final double[] msg)	{return log(LogLevel.INFORMATION, ref, msg,',');}
	public final boolean information(final String ref,final Object[] msg,char sep){return log(LogLevel.INFORMATION, ref, msg,sep);}
	public final boolean information(final String ref,final byte[] msg,char sep)	{return log(LogLevel.INFORMATION, ref, msg,sep);}
	public final boolean information(final String ref,final int[] msg,char sep)	{return log(LogLevel.INFORMATION, ref, msg,sep);}
	public final boolean information(final String ref,final short[] msg,char sep)	{return log(LogLevel.INFORMATION, ref, msg,sep);}
	public final boolean information(final String ref,final float[] msg,char sep)	{return log(LogLevel.INFORMATION, ref, msg,sep);}
	public final boolean information(final String ref,final long[] msg,char sep)	{return log(LogLevel.INFORMATION, ref, msg,sep);}
	public final boolean information(final String ref,final double[] msg,char sep)	{return log(LogLevel.INFORMATION, ref, msg,sep);}
}
