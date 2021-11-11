package simple.io;

import java.io.IOException;
import java.io.Writer;

/**
 * Output the same data to multiple writers
 */
public class MultiWriter extends Writer{
	private final Writer[] outs;
	public MultiWriter(Writer ...dest){
		outs= dest;
	}
	@Override
	public void close() throws IOException{
		for(Writer out: outs){
			out.close();
		}
	}

	@Override
	public void flush() throws IOException{
		for(Writer out: outs){
			out.flush();
		}
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException{
		for(Writer out: outs){
			out.write(cbuf, off, len);
		}
	}

}
