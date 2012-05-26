/**
 * 
 */
package simple.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Vector;

/** Prints various iterable and non-iterable things to a stream.
 * <br>Created: Jul 9, 2010
 * @author Kenneth Pierce
 */
public final class StaticPrinter {
	public static void print(Iterator<?> iter, char sep, OutputStream out, Charset cset) throws IOException {
		if (!iter.hasNext()) return;
		out.write(iter.next().toString().getBytes(cset));
		while(iter.hasNext())
			out.write((sep+iter.next().toString()).getBytes(cset));
		out.flush();
	}
	public static void print(Iterator<?> iter, char sep, OutputStream out) throws IOException {
		if (!iter.hasNext()) return;
		out.write(iter.next().toString().getBytes());
		while(iter.hasNext())
			out.write((sep+iter.next().toString()).getBytes());
		out.flush();
	}
	public static void print(Iterator<?> iter, char sep, Writer out) throws IOException {
		if (!iter.hasNext()) return;
		out.write(iter.next().toString());
		while(iter.hasNext())
			out.write((sep+iter.next().toString()));
		out.flush();
	}
	//===============================================================================================
	public static void print(Vector<?> iter, char sep, OutputStream out, Charset cset) throws IOException {
		print(iter.iterator(), sep, out, cset);
	}
	public static void print(Vector<?> iter, char sep, OutputStream out) throws IOException {
		print(iter.iterator(), sep, out);
	}
	public static void print(Vector<?> iter, char sep, Writer out) throws IOException {
		print(iter.iterator(), sep, out);
	}
}
