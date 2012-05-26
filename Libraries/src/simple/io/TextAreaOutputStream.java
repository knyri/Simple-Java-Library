package simple.io;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;

/**An output stream that prints to a JTextArea.
 * <br>Created: 2007
 * @author Kenneth Pierce
 */
public class TextAreaOutputStream extends OutputStream {
	private final JTextArea out;
	private byte[] chars = new byte[1];
	public TextAreaOutputStream(JTextArea out) {
		this.out = out;
	}
	@Override
	public void write(int b) throws IOException {
//		chars[1] = (byte)((b >> 8) & 255);
		chars[0] = (byte)(b & 255);
		out.append(new String(chars));
	}
}
