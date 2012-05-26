package simple.io;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Extends upon StringWriter to add writeln().
 * <br>Created: 2005
 * @author Kenneth Pierce
 */
public class StringWriterExt extends StringWriter {
	/** value of System.getProperty("line.separator","") */
	final static char[] NL = System.getProperty("line.separator","").toCharArray();
	/** value of System.getProperty("line.separator","") */
	final static String sNL = System.getProperty("line.separator","");
	public void writeln(CharSequence s) {
		write(s.toString());
		writeln();
	}
	public void writeln() {
		write(sNL);
	}
	public void writeln(char[] s) throws IOException {
		write(s);
		writeln();
	}
	public void writeln(char[] s, int offset, int length) {
		write(s, offset, length);
		writeln();
	}
	public void writeln(char[] s, int offset) {
		writeln(s, offset, s.length);
	}
}
