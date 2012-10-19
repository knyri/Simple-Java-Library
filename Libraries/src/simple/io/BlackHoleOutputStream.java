/**
 *
 */
package simple.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Discards everything given to it. Great for finishing off an InputStream.
 * @author Ken
 *
 */
public class BlackHoleOutputStream extends OutputStream{
	@Override
	public void write(int arg0) throws IOException{}
	@Override
	public void close() throws IOException{}
	@Override
	public void flush() throws IOException{}
	@Override
	public void write(byte[] b,int off,int len) throws IOException{}
	@Override
	public void write(byte[] b) throws IOException{}
}
