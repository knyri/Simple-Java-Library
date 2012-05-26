/**
 * 
 */
package simple.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Properties;
import java.util.Map.Entry;

/**Allows static writing to a common output stream for an entire program.
 * <br>Created: Feb 8, 2010
 * @author Kenneth Pierce
 * @deprecated
 * @see simple.util.logging.Log
 */
public final class StaticDebug {
	public static PrintWriter _out = new PrintWriter(System.out);
	public static boolean _showTime = true;
	public static final int errFULL = 0, errSHORT = 1;
	public static final int lvlINFO = 1, lvlWARN = 2, lvlERROR = 4, lvlDEBUG = 8;
	public static final int ERROR_DEBUG = lvlERROR+lvlDEBUG,
			INFO_ERROR = lvlINFO+lvlERROR,
			WARN_ERROR = lvlWARN+lvlERROR,
			INFO_WARN_ERROR = lvlINFO+lvlWARN+lvlERROR,
			WARN_ERROR_DEBUG = WARN_ERROR+lvlDEBUG,
			INFO_WARN_ERROR_DEBUG = lvlERROR + lvlWARN + lvlINFO + lvlDEBUG;
	public static int _errorDepth = errFULL,
				_errlvl = INFO_WARN_ERROR_DEBUG;
	public static final String getTime() {
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR), minute = c.get(Calendar.MINUTE), second = c.get(Calendar.SECOND);
		return ((hour<10?"0":"")+hour + ":" + (minute<10?"0":"")+minute  + ":" + (second<10?"0":"")+second);
	}
	public static final String getTimeStamp() {
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR), minute = c.get(Calendar.MINUTE), second = c.get(Calendar.SECOND);
		return ((hour<10?"0":"") + hour + (minute<10?"0":"") + minute  + (second<10?"0":"") + second);
	}
	public static final String getDateStamp() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR), month = c.get(Calendar.MONTH), day = c.get(Calendar.DAY_OF_MONTH);
		return (Integer.toString(year) + (month<10?"0":"")+Integer.toString(month) + (day<10?"0":"")+Integer.toString(day));
	}
	public static boolean log(String prefix, Exception e) {
		_out.print(((_showTime)?(getTime()+" "):"")+prefix+"\t"+e.getMessage()+"\r\n");
		if (_errorDepth==errFULL) {
			StringWriter SW = new StringWriter();
			e.printStackTrace(new PrintWriter(SW));
			_out.print(SW.toString()+"\r\n");
		}
		_out.flush();
		return true;
	}
	public static boolean log(String prefix, String msg, Exception e) {
		_out.print(((_showTime)?(getTime()+" "):"")+prefix+"\t"+msg+":"+e.getMessage()+"\r\n");
		if (_errorDepth==errFULL) {
			StringWriter SW = new StringWriter();
			e.printStackTrace(new PrintWriter(SW));
			_out.print(SW.toString()+"\r\n");
		}
		_out.flush();
		return true;
	}
	public static boolean log(String prefix, String e) {
		_out.print(((_showTime)?(getTime()+" "):"")+prefix+"\t"+e+"\r\n");
		_out.flush();
		return true;
	}
	public static boolean log (String prefix, Properties e) {
		prefix = ((_showTime)?(getTime()+" "):"")+prefix+"\t";
		for (Entry<Object, Object> en : e.entrySet())
			_out.println(prefix+en.getKey()+":"+en.getValue());
		_out.flush();
		return true;
	}
	public static boolean log (String prefix, Object e) {
		_out.print(((_showTime)?(getTime()+" "):"")+prefix+"\t"+e+"\r\n");
		_out.flush();
		return true;
	}
	public static boolean info(String caller, Exception e) {
		if ((_errlvl & lvlINFO) == lvlINFO)
			return log("[INFO]\t"+caller, e);
		return true;
	}
	public static boolean warning(String caller, Exception e) {
		if ((_errlvl & lvlWARN) == lvlWARN)
			return log("[WARN]\t"+caller, e);
		return true;
	}
	public static boolean error(String caller, Exception e) {
		if ((_errlvl & lvlERROR) == lvlERROR)
			return log("[ERRO]\t"+caller, e);
		return true;
	}
	public static boolean debug(String caller, Exception e) {
		if ((_errlvl & lvlDEBUG) == lvlDEBUG)
			return log("[DEBU]\t"+caller, e);
		return true;
	}
	public static boolean info(String caller, String msg, Exception e) {
		if ((_errlvl & lvlINFO) == lvlINFO)
			return log("[INFO]\t"+caller, msg, e);
		return true;
	}
	public static boolean warning(String caller, String msg, Exception e) {
		if ((_errlvl & lvlWARN) == lvlWARN)
			return log("[WARN]\t"+caller, msg, e);
		return true;
	}
	public static boolean error(String caller, String msg, Exception e) {
		if ((_errlvl & lvlERROR) == lvlERROR)
			return log("[ERRO]\t"+caller, msg, e);
		return true;
	}
	public static boolean debug(String caller, String msg, Exception e) {
		if ((_errlvl & lvlDEBUG) == lvlDEBUG)
			return log("[DEBU]\t"+caller, msg, e);
		return true;
	}
	//=============================================================
	public static boolean info(Class<?> c, String caller, Exception e) {
		if ((_errlvl & lvlINFO) == lvlINFO)
			return log("[INFO]\t"+c.getCanonicalName()+":"+caller, e);
		return true;
	}
	public static boolean warning(Class<?> c, String caller, Exception e) {
		if ((_errlvl & lvlWARN) == lvlWARN)
			return log("[WARN]\t"+c.getCanonicalName()+":"+caller, e);
		return true;
	}
	public static boolean error(Class<?> c, String caller, Exception e) {
		if ((_errlvl & lvlERROR) == lvlERROR)
			return log("[ERRO]\t"+c.getCanonicalName()+":"+caller, e);
		return true;
	}
	public static boolean debug(Class<?> c, String caller, Exception e) {
		if ((_errlvl & lvlDEBUG) == lvlDEBUG)
			return log("[DEBU]\t"+c.getCanonicalName()+":"+caller, e);
		return true;
	}
	//-------------------------------------------------------------
	public static boolean info(String caller, String e) {
		if ((_errlvl & lvlINFO) == lvlINFO)
			return log("[INFO]\t"+caller, e);
		return true;
	}
	public static boolean warning(String caller, String e) {
		if ((_errlvl & lvlWARN) == lvlWARN)
			return log("[WARN]\t"+caller, e);
		return true;
	}
	public static boolean error(String caller, String e) {
		if ((_errlvl & lvlERROR) == lvlERROR)
			return log("[ERRO]\t"+caller, e);
		return true;
	}
	public static boolean debug(String caller, String e) {
		if ((_errlvl & lvlDEBUG) == lvlDEBUG)
			return log("[DEBU]\t"+caller, e);
		return true;
	}
	
	public static boolean info(String caller, Properties e) {
		if ((_errlvl & lvlINFO) == lvlINFO)
			return log("[INFO]\t"+caller, e);
		return true;
	}
	public static boolean warning(String caller, Properties e) {
		if ((_errlvl & lvlWARN) == lvlWARN)
			return log("[WARN]\t"+caller, e);
		return true;
	}
	public static boolean error(String caller, Properties e) {
		if ((_errlvl & lvlERROR) == lvlERROR)
			return log("[ERRO]\t"+caller, e);
		return true;
	}
	public static boolean debug(String caller, Properties e) {
		if ((_errlvl & lvlDEBUG) == lvlDEBUG)
			return log("[DEBU]\t"+caller, e);
		return true;
	}
	
	public static boolean info(String caller, Object e) {
		if ((_errlvl & lvlINFO) == lvlINFO)
			return log("[INFO]\t"+caller, e);
		return true;
	}
	public static boolean warning(String caller, Object e) {
		if ((_errlvl & lvlWARN) == lvlWARN)
			return log("[WARN]\t"+caller, e);
		return true;
	}
	public static boolean error(String caller, Object e) {
		if ((_errlvl & lvlERROR) == lvlERROR)
			return log("[ERRO]\t"+caller, e);
		return true;
	}
	public static boolean debug(String caller, Object e) {
		if ((_errlvl & lvlDEBUG) == lvlDEBUG)
			return log("[DEBU]\t"+caller, e);
		return true;
	}
}
