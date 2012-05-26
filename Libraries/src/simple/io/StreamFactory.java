/**
 *
 */
package simple.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import simple.util.logging.LogFactory;

/**convenience methods for InputStream and OutputStream
 * @author Ken
 *
 */
public class StreamFactory {
	public static int _jobBufferSize = 8192;
	static class StreamJob implements Runnable {
		final OutputStream _out;
		final InputStream _in;
		final int _bufferSize;
		public StreamJob(final InputStream in, final OutputStream out) {
			_in = in;
			_out = out;
			_bufferSize = _jobBufferSize;
		}
		public void run() {
			try {
				copy(_in, _out, _bufferSize);
			} catch (final IOException e) {
				LogFactory.getLogFor(StreamJob.class).warning(e);
			}
		}
	}
	/**
	 * Copies from input directly to output.
	 * @param input Source
	 * @param output Destination
	 * @param bufferSize Size of byte chunks the be copied at once.
	 * @throws IOException
	 */
	public static void copy(final InputStream input, final OutputStream output, final int bufferSize ) throws IOException {
		final byte buffer[] = new byte[bufferSize];
		int n = 0;
		while( (n=input.read(buffer)) != -1) {
			output.write(buffer, 0, n);
		}
		output.flush();
	}
	public static void copy(final InputStream input, final OutputStream output, final int bufferSize, long numBytes ) throws IOException {
		final byte buffer[] = new byte[bufferSize];
		int n = 0;
		while (numBytes > 0) {
			if (bufferSize > numBytes)
				n = input.read(buffer, 0, (int)numBytes);
			else
				n = input.read(buffer, 0, bufferSize);
			output.write(buffer, 0, n);
			numBytes -= n;
		}
		output.flush();
	}
	public static void copyThread(final InputStream input, final OutputStream output) {
		new Thread(new StreamJob(input, output)).start();
	}
	/**
	 * @param file
	 * @return a FileInputStream wrapped in a BufferedInputStream
	 * @throws FileNotFoundException
	 */
	public static BufferedInputStream getBufferedInputStream(final File file) throws FileNotFoundException {
		return new BufferedInputStream(new FileInputStream(file));
	}
	/**
	 * Takes an InputStream and wraps it in a BufferedInputStream.
	 * @param in
	 * @return InputStream wrapped in a BufferedInputStream. If <var>in</var> is already
	 * 			a BufferedInputStream then it is casted and returned.
	 */
	public static BufferedInputStream getBufferedInputStream(final InputStream in) {
		if (in instanceof BufferedInputStream)
			return (BufferedInputStream)in;
		return new BufferedInputStream(in);
	}
	/**
	 * Reads until <code>end</code> is reached. The returned array includes the end byte.
	 * @param in InputStream to read from.
	 * @param end Byte to stop at.
	 * @return An array of the bytes read.
	 * @throws IOException
	 */
	public static byte[] readUntil(final InputStream in, final byte end) throws IOException {
		final Vector<Byte> buf = new Vector<Byte>();
		byte[] bbuf = new byte[1];
		while((in.read(bbuf)!=-1)){
			buf.addElement(new Byte(bbuf[0]));
			if (bbuf[0]==end) {
				break;
			}
		}
		bbuf = new byte[buf.size()];
		for (int i = 0;i<buf.size(); i++) {
			bbuf[i] = buf.elementAt(i).byteValue();
		}
		return bbuf;
	}
	/**Reads until the first non-whitespace character is found.
	 * @param rd
	 * @return The non-whitespace character or -1
	 * @throws IOException
	 */
	public static char skipWhitespace(final InputStream rd) throws IOException {
		int c = rd.read();
		if (c==-1) return (char)c;
		do {
			if (!Character.isWhitespace(c)) break;
			c = rd.read();
		} while(c==-1);
		return (char)c;
	}

	/**
	 * @param file
	 * @return A FileOutputStream wrapped in a BufferedOutputStream.
	 * @throws FileNotFoundException If the file is a directory or if it cannot be created if it does not exist.
	 */
	public static BufferedOutputStream getBufferedOutputStream(final File file) throws FileNotFoundException {
		return new BufferedOutputStream(new FileOutputStream(file));
	}
	/**
	 * Wraps an OutputStream in a BufferedOutputStream.
	 * @param out
	 * @return OutputStream wrapped in a BufferedOutputStream. If <var>out</var> is already
	 * 			a BufferedOutputStream then it is casted and returned.
	 */
	public static BufferedOutputStream getBufferedOutputStream(final OutputStream out) {
		if (out instanceof BufferedOutputStream)
			return (BufferedOutputStream)out;
		return new BufferedOutputStream(out);
	}
}
