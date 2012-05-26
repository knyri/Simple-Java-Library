package simple.util.regex;

import java.util.regex.*;

/**
 * Provides a simpler to use Pattern.<br>
 * Main difference: a pattern of "*" will now match what ".*" can match.
 * <br>Created: 2004
 * @author KP
 */
public class SimplePattern {
	static char NULL = (char)0;
	public static Pattern compile(String regex) {
		return Pattern.compile(parse(regex));
	}
	public static boolean matches(String regex, String test) {
		return Pattern.matches(parse(regex), test);
	}
	private static String parse(String regex) {
		StringBuffer buf = new StringBuffer(regex.length()+15);
		char[] ca = regex.toCharArray();
		char c = NULL;
		char prev = NULL;
		char next = NULL;
		int depth = 0;
		for (int i =0;i<ca.length;i++) {
			c = ca[i];
			next = i==ca.length-1?NULL:ca[i+1];
			switch(c) {
			case '[':
				buf.append(c);
				depth++;
				break;
			case ']':
				buf.append(c);
				depth--;
				break;
			case '*':
				if (prev!=']') {
					buf.append('.');
				}
				buf.append(c);
				break;
			case '\\':
				buf.append(c);
				buf.append(next);
				i++;
				break;
			case '.':
				buf.append('\\');
				//buf.append('\\');
				buf.append(c);
				break;
			case '?':
				buf.append('\\');
				//buf.append('\\');
				buf.append(c);
				break;
			default:
				buf.append(c);
			}
				
			prev = ca[i];
		}
		return buf.toString();
	}
}
