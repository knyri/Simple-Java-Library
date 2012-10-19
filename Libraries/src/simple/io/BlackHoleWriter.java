/**
 *
 */
package simple.io;

import java.io.IOException;
import java.io.Writer;

/**
 * Discards everything given to it. Great for finishing off a Reader.
 *
 * @author Ken
 *
 */
public class BlackHoleWriter extends Writer{
	public BlackHoleWriter(Object arg0){}
	@Override
	public void close() throws IOException{}
	@Override
	public void flush() throws IOException{}
	@Override
	public void write(char[] arg0,int arg1,int arg2) throws IOException{}
	@Override
	public Writer append(char c) throws IOException{return this;}
	@Override
	public Writer append(CharSequence csq,int start,int end) throws IOException{return this;}
	@Override
	public Writer append(CharSequence csq) throws IOException{return this;}
	@Override
	public void write(char[] cbuf) throws IOException{}
	@Override
	public void write(int c) throws IOException{}
	@Override
	public void write(String str,int off,int len) throws IOException{}
	@Override
	public void write(String str) throws IOException{}
}
