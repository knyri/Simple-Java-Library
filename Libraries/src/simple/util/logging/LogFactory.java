/**
 *
 */
package simple.util.logging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

import simple.io.FileUtil;

/**Factory for getting and setting logs for specific classes.<br>
 * Getting a log: {@link #getLogFor(Class)}, {@link #getLogFor(Object)}<br>
 * Standard way of getting a log: <br>
 * Setting a log: <code>private static final {@link simple.util.logging.Log Log} log = LogFactory.getLogFor(&lt;your class&gt;.class)</code>
 * <ul>
 * <li>{@link #setGlobalLogFile(File, boolean)}</li>
 * <li>{@link #setGlobalLogStream(PrintStream)}</li>
 * <li>{@link #setLogFileFor(Class, File, boolean)}</li>
 * <li>{@link #setLogStreamFor(Class, PrintStream)}</li>
 * <li>{@link #setLogFor(Class, Log)}</li>
 * </ul>
 * You can change the log for a class after it has been gotten. Messages
 * logged before the change will not be transferred.
 * <hr>
 * Created: Oct 21, 2010
 * @author Kenneth Pierce
 * @see simple.util.logging.Log
 */
public final class LogFactory {
	private static ErrorCodeResolver ecr = new ErrorCodeResolver() {
		@Override
		public String getErrorString(final int code) {
			return null;
		}};
		private static HashMap<Class<?>, Log> logCache = new HashMap<Class<?>, Log>();
		//private static final Log _log = new Log(LogFactory.class);
		private static PrintStream globalStream = System.out;
		private static byte logOptions = (byte)0xFF;
		private static boolean printTime=false,printDate=false;
		public synchronized static final void setPrint(final LogLevel level, final boolean print) {
			if (print) {
				logOptions |= level.getValue();
			} else {
				logOptions &= ~level.getValue();
			}
			for(final Log log:logCache.values()) {
				log.setPrint(level, print);
			}
		}
		public static final String getTimeStamp() {
			Calendar t=Calendar.getInstance(TimeZone.getDefault());
			return String.format("%1$tH%1$tM%1$tS",t);
		}
		public static final String getDateStamp() {
			Calendar t=Calendar.getInstance(TimeZone.getDefault());
			return String.format("%1$tY%1$tm%1$td",t);
		}
		public static final void setPrintTimeStamp(boolean print){
			printTime=print;
			for (final Log log : logCache.values()) {
				log.setPrintTime(print);
			}
		}
		public static final void setPrintDateStamp(boolean print){
			printDate=print;
			for (final Log log : logCache.values()) {
				log.setPrintDate(print);
			}
		}
		/**Sets the default stream for all logs created by this factory.
		 * Also updates existing logs.
		 * @param stream
		 */
		public synchronized static void setGlobalLogStream(final PrintStream stream) {
			globalStream = stream;
			//_log.setStream(stream);
			for (final Log log : logCache.values()) {
				log.setStream(stream);
			}
		}
		/**Sets the output stream for the log associated with the class.
		 * Creates a log if one does not exist.
		 * @param clazz
		 * @param stream
		 */
		public static void setLogStreamFor(final Class<?> clazz, final PrintStream stream) {
			getLogFor(clazz).setStream(stream);
		}
		/**Sets the global output file for all log streams created by this factory.
		 * Also updates existing logs.
		 * @param file target file
		 * @param append append to the file or clear it
		 * @throws IOException if file creation fails.
		 * @see java.io.File#createNewFile()
		 */
		public synchronized static void setGlobalLogFile(final File file, final boolean append) throws IOException {
			FileUtil.createFile(file);
			setGlobalLogStream(new PrintStream(new FileOutputStream(file, append)));
		}
		/**Sets the output file for the specified class.
		 * @param clazz target class log
		 * @param file target file
		 * @param append append to the file or clear it
		 * @throws IOException if file creation fails
		 * @see java.io.File#createNewFile()
		 */
		public static void setLogFileFor(final Class<?> clazz, final File file, final boolean append) throws IOException {
			FileUtil.createFile(file);
			setLogStreamFor(clazz, new PrintStream(new FileOutputStream(file, append)));
		}
		public static PrintStream getGlobalLogStream() {
			return globalStream;
		}
		/**This should be called before instantiating any classes that use this
		 * factory for it's logs if you want to specify a log for it to use.
		 * @param clazz
		 * @param log
		 */
		public static final synchronized void setLogFor(final Class<?> clazz, final Log log) {
			logCache.remove(clazz);
			logCache.put(clazz, log);
			//_log.information("setLogFor: "+clazz.getCanonicalName());
		}
		public static final synchronized Log getLogFor(final Class<?> clazz) {
			Log log = logCache.get(clazz);
			if (log==null) {
				log = new Log(globalStream,clazz);
				for (final LogLevel ll:LogLevel.values()) {
					log.setPrint(ll, (logOptions&ll.getValue())==ll.getValue());
				}
				log.setPrintTime(printTime);
				log.setPrintDate(printDate);
				setLogFor(clazz, log);
			}
			//_log.information("getLogFor: "+clazz.getCanonicalName());
			return log;
		}
		/**For anonymous classes.
		 * @param obj
		 * @return
		 */
		public static final synchronized Log getLogFor(final Object obj) {
			return getLogFor(obj.getClass());
		}
		/**Gets the currently assigned error code resolver.
		 * @return The error code resolver
		 */
		public static final ErrorCodeResolver getECR() {
			return ecr;
		}
		/**Sets the error code resolver.
		 * @param ecr The new error code resolver
		 */
		public static final void setECR(final ErrorCodeResolver ecr) {
			LogFactory.ecr = ecr;
		}
		/**Gets the String associated with the code from the error code resolver.
		 * Short for <code>LogFactory.getECR().getErrorString(code)</code>.
		 * @param code The error code.
		 * @return The error string associated with the error code.
		 */
		public static final String getErrorString(final int code) {
			return ecr.getErrorString(code);
		}
}
