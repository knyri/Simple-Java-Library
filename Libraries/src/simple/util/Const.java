/**
 *
 */
package simple.util;

/**
 * <hr>
 * <br>Created: Apr 23, 2011
 * @author Kenneth Pierce
 */
public final class Const {
	public static enum LineEnding {
		MAC("\r"),
		UNIX("\n"),
		LINUX("\n"),
		WINDOWS("\r\n");
		private final String ending;
		LineEnding(String ending) {
			this.ending = ending;
		}
		public boolean isEnd(String ending) {
			return this.ending.equals(ending);
		}
		public String getEnd() {
			return ending;
		}
	};
	public static final LineEnding getLineEndingType(String line) {
		int i = line.length()-1;
		for (;i>0;i--) {
			if (line.charAt(i)!='\r' && line.charAt(i)!='\n') break;
		}
		String ending = line.substring(i);
		for (LineEnding t : LineEnding.values()) {
			if (t.isEnd(ending)) return t;
		}
		return null;
	}
	public static final String TRUE="true",FALSE="false";
	public static boolean tf(String tf){
		return TRUE.equals(tf.toLowerCase());
	}
	public static String tf(boolean tf){
		return tf?TRUE:FALSE;
	}
}
