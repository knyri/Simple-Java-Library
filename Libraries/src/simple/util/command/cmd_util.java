/**
 *
 */
package simple.util.command;

import java.util.ArrayList;
import java.util.List;

/**
 * <br>Created: Nov 23, 2010
 * @author Kenneth Pierce
 */
public final class cmd_util {
	/**Parses the arguments. Spaces are separators. Will unquote arguments
	 * and unescape sub-quotes.
	 * @param raw The arguments
	 * @return A vector containing the arguments in left to right order
	 */
	public static List<String> parseArguments(CharSequence raw) {
		boolean sq = false, dq = false;
		StringBuilder buf = new StringBuilder(75);
		ArrayList<String> args = new ArrayList<String>();
		for (int i = 0; i < raw.length(); i++) {
			if (raw.charAt(i) == '\\') {
				/* if the next character is a quote and the quote flag is set,
				 * increment to the quote
				 */
				switch(raw.charAt(i+1)) {
				case '"':
					if (dq) i++;
					break;
				case '\'':
					if (sq) i++;
					break;
				}
				buf.append(raw.charAt(i));
			} else if (raw.charAt(i) == '"' && !sq) {
				if (dq) {
					dq = false;
					args.add(buf.toString());//quote ended, add argument
					buf.setLength(0);//clear the buffer
					if (raw.charAt(i+1)==' ') i++;//increment if the next char is an argument separator
				} else dq = true;
			} else if (raw.charAt(i) == '\'' && !dq) {
				if (sq) {
					sq = false;
					args.add(buf.toString());//quote ended, add argument
					buf.setLength(0);//clear the buffer
					if (raw.charAt(i+1)==' ') i++;//increment if the next char is an argument separator
				} else sq = true;
			} else if (!dq && !sq && Character.isWhitespace(raw.charAt(i))){
				args.add(buf.toString());
				buf.setLength(0);
			} else {
				buf.append(raw.charAt(i));
			}
		}
		if (buf.length()>0) args.add(buf.toString());
		args.trimToSize();
		return args;
	}
	/*private static boolean isQuote(char c) {
		return (c == '"' || c == '\'');
	}*/
}
