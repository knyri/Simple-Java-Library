package simple.io;

import java.io.IOException;
import java.io.Writer;

import javax.swing.JTextArea;

/**A Writer that prints to a JTextArea.
 * <br>Created: 2007
 * @author Kenneth Pierce
 */
public class TextAreaWriter extends Writer {
	private final JTextArea out;
	public TextAreaWriter(JTextArea out) {
		this.out = out;
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		out.append(new String(cbuf, off, len));
	}
	@Override
	public void flush() throws IOException {}

	@Override
	public void close() throws IOException {}
}
