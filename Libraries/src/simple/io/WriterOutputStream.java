/**
 * 
 */
package simple.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/** An OutputStream that prints to a Writer.
 * <br>Created: Feb 5, 2010
 * @author Kenneth Pierce
 */
public class WriterOutputStream extends OutputStream {
	protected final Writer _writer;
	protected final String _encoding;
	private byte[] _buf = new byte[1];
	public WriterOutputStream(Writer writer, String encoding) {
		_writer = writer;
		_encoding = encoding;
	}
	public WriterOutputStream(Writer writer) {
		this(writer, null);
	}
	public void close() throws IOException {
		_writer.close();
	}
	 public void flush() throws IOException {
		 _writer.flush();
	}
	public void write(byte[] b) throws IOException {
		if (_encoding==null)
			_writer.write(new String(b));
		else
			_writer.write(new String(b,_encoding));
	}
	public void write(byte[] b, int off, int len) throws IOException {
		if (_encoding==null)
			_writer.write(new String(b,off,len));
		else
			_writer.write(new String(b,off,len,_encoding));
	}
	public synchronized void write(int b) throws IOException {
		_buf[0]=(byte)b;
		write(_buf);
	}
}
