/**
 *
 */
package simple.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * Convenience methods for Readers.
 * @author Ken
 *
 */
public final class ReadWriterFactory {
	/**
	 * @param file
	 * @return a FileReader wrapped in a BufferedInputStream
	 * @throws FileNotFoundException
	 */
	public static BufferedReader getBufferedReader(final File file) throws FileNotFoundException {
		return new BufferedReader(new FileReader(file));
	}
	/**
	 * Wraps a Reader in a BufferedReader.
	 * @param rd
	 * @return Reader wrapped in a BufferedReader. If <var>rd</var> is already
	 * 			a BufferedReader then it is casted and returned.
	 */
	public static BufferedReader getBufferedReader(final Reader rd) {
		if(rd instanceof java.io.BufferedReader)
			return (BufferedReader)rd;
		return new BufferedReader(rd);
	}
	/**
	 * Reads all available data from reader.
	 * @param reader Source
	 * @return Everything contained in reader as a string.
	 * @throws IOException
	 * @deprecated
	 * @see RWUtil#readFully(Reader)
	 */
	public static String readFully(final Reader reader) throws IOException {
		return RWUtil.readFully(reader);
	}
	/**
	 * @param reader
	 * @param buf
	 * @throws IOException
	 * @deprecated
	 * @see RWUtil#readInto(Reader, StringBuilder)
	 */
	public static void readInto(final Reader reader, final StringBuilder buf) throws IOException {
		RWUtil.readInto(reader, buf);
	}
	/**
	 * Reads until <code>end</code> is reached. The returned String includes the end character.<br>
	 * As a result of this behaviour, an empty string means that the end has been reached.
	 * @param in Reader to read from.
	 * @param end Character to stop at.
	 * @return String of read characters.
	 * @throws IOException
	 * @deprecated
	 * @see RWUtil#readUntil(Reader, char)
	 */
	public static String readUntil(final Reader in, final char end) throws IOException {
		return RWUtil.readUntil(in, end);
	}
	/**
	 * Reads until <code>end</code> is reached. The returned String includes the end character.
	 * @param in Reader to read from.
	 * @param end Character to stop at.
	 * @return String of read characters.
	 * @throws IOException
	 * @deprecated
	 * @see RWUtil#readUntil(Reader, int)
	 */
	public static String readUntil(final Reader in,final int limit) throws IOException{
		return RWUtil.readUntil(in, limit);
	}
	/** Reads until <code>end</code> is found or the end of the stream is reached.
	 * @param rd
	 * @param end
	 * @param throwEOF Weather or not it should throw an EOFException if EOF is reached.
	 * @return The content read including the <code>end</code> string. Returns null if the first character read is EOF.
	 * @throws IOException
	 * @throws EOFException
	 * @deprecated
	 * @see RWUtil#readUntil(Reader, String, boolean)
	 */
	public static String readUntil(final Reader rd, final String end, final boolean throwEOF) throws IOException, EOFException {
		return RWUtil.readUntil(rd, end, throwEOF);
	}
	/** Reads until <code>end</code> is found or the end of the stream is reached.
	 * @param rd
	 * @param end
	 * @return The content read including the <code>end</code> string. Returns null if the first character read is EOF.
	 * @throws IOException
	 * @deprecated
	 * @see RWUtil#readUntil(Reader, String)
	 */
	public static String readUntil(final Reader rd, final String end) throws IOException {
		return RWUtil.readUntil(rd,end,false);
	}
	/**Reads until the first non-whitespace character is found.
	 * @param rd
	 * @return The non-whitespace character or -1
	 * @throws IOException
	 * @deprecated
	 * @see RWUtil#skipWhitespace(Reader)
	 */
	public static int skipWhitespace(final Reader rd) throws IOException {
		return RWUtil.skipWhitespace(rd);
	}
	/**
	 * @param file
	 * @return a FileWriter wrapped in a BufferedInputStream
	 * @throws FileNotFoundException
	 */
	public static BufferedWriter getBufferedWriter(final File file) throws IOException {
		return new BufferedWriter(new FileWriter(file));
	}
	/**
	 * Wraps a Writer in a BufferedWriter.
	 * @param wr
	 * @return Writer wrapped in a BufferedWriter. If <var>wr</var> is already
	 * 			a BufferedWriter then it is casted and returned.
	 */
	public static BufferedWriter getBufferedWriter(final Writer wr) {
		if(wr instanceof java.io.BufferedWriter)
			return (BufferedWriter)wr;
		return new BufferedWriter(wr);
	}
}
