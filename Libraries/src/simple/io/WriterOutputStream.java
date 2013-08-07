/**
 *
 */
package simple.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;

/** An OutputStream that prints to a Writer.
 * <br>Created: Feb 5, 2010
 * @author Kenneth Pierce
 */
public class WriterOutputStream extends OutputStream {
	protected final Writer _writer;
	protected final Charset _encoding;
	public WriterOutputStream(Writer writer, String encoding) {
		_writer = writer;
		if(encoding!=null)
			_encoding = Charset.forName(encoding);
		else
			_encoding=Charset.defaultCharset();
	}
	public WriterOutputStream(Writer writer) {
		this(writer, null);
	}
	@Override
	public void close() throws IOException {
		_writer.close();
	}
	 @Override
	public void flush() throws IOException {
		 _writer.flush();
	}
	@Override
	public void write(byte[] b) throws IOException {
		_writer.write(new String(b,_encoding));
	}
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		_writer.write(new String(b,off,len,_encoding));
	}
	@Override
	public synchronized void write(int b) throws IOException {
		_writer.write(b);
	}
}
