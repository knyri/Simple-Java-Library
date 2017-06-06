package simple.io;

import java.io.IOException;
import java.io.Writer;

/**
 * A Writer wrapper that returns itself for call chaining
 * @author Ken
 *
 */
public class ChainableWriter implements AutoCloseable {

	private final Writer writer;
	public ChainableWriter(Writer writer) {
		this.writer= writer;
	}

	@Override
	public void close() throws IOException {
		writer.close();
	}

	public void flush() throws IOException {
		writer.flush();
	}

	public ChainableWriter write(char[] cbuf, int off, int len) throws IOException {
		writer.write(cbuf, off, len);
		return this;
	}

	public ChainableWriter write(String str) throws IOException {
		writer.write(str);
		return this;
	}

	public ChainableWriter write(String str, int off, int len) throws IOException {
		writer.write(str, off, len);
		return this;
	}

	public ChainableWriter write(char[] cbuf) throws IOException {
		writer.write(cbuf);
		return this;
	}

	public ChainableWriter write(int c) throws IOException {
		writer.write(c);
		return this;
	}

	public ChainableWriter append(char c) throws IOException {
		writer.append(c);
		return this;
	}
	public ChainableWriter append(CharSequence csq, int off, int len) throws IOException {
		writer.append(csq, off, len);
		return this;
	}

	public ChainableWriter write(CharSequence csq) throws IOException {
		writer.append(csq);
		return this;
	}
}
