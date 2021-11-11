package simple.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Output the same data to multiple streams
 */
public class MultiOutputStream extends OutputStream{
	private final OutputStream[] outs;
	public MultiOutputStream(OutputStream ... dest){
		outs= dest;
	}
	@Override
	public void write(int arg0) throws IOException{
		for(OutputStream out: outs){
			out.write(arg0);
		}
	}
	@Override
	public void close() throws IOException{
		for(OutputStream out: outs){
			out.close();
		}
	}
	@Override
	public void flush() throws IOException{
		for(OutputStream out: outs){
			out.flush();
		}
	}
	@Override
	public void write(byte[] b, int off, int len) throws IOException{
		for(OutputStream out: outs){
			out.write(b, off, len);
		}
	}
	@Override
	public void write(byte[] b) throws IOException{
		for(OutputStream out: outs){
			out.write(b);
		}
	}
}
