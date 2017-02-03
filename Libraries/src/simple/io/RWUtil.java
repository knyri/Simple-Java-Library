package simple.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Arrays;

public final class RWUtil {

	/**
	 * Attempts to fill output
	 * @param reader
	 * @param output
	 * @return The number of characters read
	 * @throws IOException
	 */
	public static int readFully(Reader reader, char[] output) throws IOException{
		int read= 0, len= output.length, offset= 0;
		do{
			read= reader.read(output, offset, len - offset);
			if(read == -1){
				break;
			}
			offset+= read;
		}while(offset < len);
		return offset;
	}
	/**
	 * Reads all available data from reader.
	 * @param reader Source
	 * @return Everything contained in reader as a string.
	 * @throws IOException
	 */
	public static String readFully(final Reader reader) throws IOException {
		final StringWriter writer = new StringWriter();
		FileUtil.copy(reader, writer, 1024);
		return writer.toString();
	}
	public static void readInto(final Reader reader, final StringBuilder buf) throws IOException {
		final char[] buffer = new char[1024];
		int length = 0;
		while ((length=reader.read(buffer))!=-1) {
			buf.append(buffer, 0, length);
		}
	}
	/**
	 * Reads until <code>end</code> is reached. The returned String includes the end character.<br>
	 * As a result of this behaviour, an empty string means that the end has been reached.
	 * @param in Reader to read from.
	 * @param end Character to stop at.
	 * @return String of read characters.
	 * @throws IOException
	 */
	public static String readUntil(final Reader in, final char end) throws IOException {
		final StringBuilder buf = new StringBuilder(255);
		final char[] cbuf = new char[1];
		while((in.read(cbuf)!=-1)){
			buf.append(cbuf[0]);
			if (cbuf[0]==end) {
				break;
			}
		}
		return buf.toString();
	}
	/**
	 * Reads until <code>end</code> is reached. The returned String includes the end character.<br>
	 * As a result of this behaviour, an empty string means that the end has been reached.
	 * @param in Reader to read from.
	 * @param end Character to stop at.
	 * @return String of read characters.
	 * @throws IOException
	 */
	public static String readUntilAny(final Reader in, final String end) throws IOException {
		final StringBuilder buf = new StringBuilder(255);
		final char[] cbuf = new char[1];
		while((in.read(cbuf)!=-1)){
			buf.append(cbuf[0]);
			if (end.indexOf(cbuf[0]) != -1) {
				break;
			}
		}
		return buf.toString();
	}
	/**
	 * Reads until <code>end</code> is reached. The returned String includes the end character.
	 * @param in Reader to read from.
	 * @param limit Maximum number of bytes to read
	 * @return String of read characters.
	 * @throws IOException
	 */
	public static String readUntil(final Reader in,final int limit) throws IOException{
		final char[] cbuf = new char[limit];
		final int read=in.read(cbuf);
		if(read==-1){
			return null;
		} else {
			return new String(Arrays.copyOfRange(cbuf, 0, read));
		}
	}
	/** Reads until <code>end</code> is found or the end of the stream is reached.
	 * @param rd
	 * @param end
	 * @param throwEOF Weather or not it should throw an EOFException if EOF is reached.
	 * @return The content read including the <code>end</code> string. Returns null if the first character read is EOF.
	 * @throws IOException
	 * @throws EOFException
	 */
	public static String readUntil(final Reader rd, final String end, final boolean throwEOF) throws IOException, EOFException {
		final StringBuilder buf = new StringBuilder(100);
		final char[] c = new char[1];
		int read = 0;
		while((read=rd.read(c))!=-1) {
			buf.append(c[0]);
			if (end.length() <= buf.length())
				if (buf.substring(buf.length()-end.length()).equalsIgnoreCase(end)) {
					break;
				}
		}
		if (read==-1 && throwEOF) throw new EOFException("End of file reached before '"+end+"' was found.");
		if (buf.length()==0) return null;
		return buf.toString();
	}
	/** Reads until <code>end</code> is found or the end of the stream is reached.
	 * @param rd
	 * @param end
	 * @return The content read including the <code>end</code> string. Returns null if the first character read is EOF.
	 * @throws IOException
	 */
	public static String readUntil(final Reader rd, final String end) throws IOException {
		return readUntil(rd,end,false);
	}
	/**Reads until the first non-whitespace character is found.
	 * @param rd
	 * @return The non-whitespace character or -1
	 * @throws IOException
	 */
	public static int skipWhitespace(final Reader rd) throws IOException {
		int c = rd.read();
		if (c==-1) return c;
		do {
			if (!Character.isWhitespace((char)c)) {
				break;
			}
			c = rd.read();
		} while(c!=-1);
		return c;
	}
	/**
	 * Reads until <code>end</code> is reached. The returned String includes the end character.<br>
	 * As a result of this behaviour, an empty string means that the end has been reached.<br>
	 * Ignores <var>end</var> if inside <var>quote</var>.
	 * @param in Reader to read from.
	 * @param end Character to stop at.
	 * @param quote quote character
	 * @return String of read characters.
	 * @throws IOException
	 */
	public static String readUntil(final Reader in, final char end, char quote) throws IOException {
		final StringBuilder buf = new StringBuilder(255);
		final char[] cbuf = new char[1];
		boolean quoted= false;
		while((in.read(cbuf)!=-1)){
			buf.append(cbuf[0]);
			if (cbuf[0]==end && !quoted) {
				break;
			} else if (cbuf[0] == quote){
				quoted = !quoted;
			}
		}
		return buf.toString();
	}


	public static String readUntilAny(final Reader in, final String end, char quote) throws IOException {
		final StringBuilder buf = new StringBuilder(255);
		final char[] cbuf = new char[1];
		boolean quoted= false;
		while((in.read(cbuf)!=-1)){
			buf.append(cbuf[0]);
			if (end.indexOf(cbuf[0]) != -1 && !quoted) {
				break;
			} else if (cbuf[0] == quote){
				quoted = !quoted;
			}
		}
		return buf.toString();
	}

	/** Reads until <code>end</code> is found or the end of the stream is reached.<br>
	 * Ignores <var>end</var> if inside <var>quote</var>.
	 * @param rd
	 * @param end
	 * @param throwEOF Weather or not it should throw an EOFException if EOF is reached.
	 * @return The content read including the <code>end</code> string. Returns null if the first character read is EOF.
	 * @throws IOException
	 * @throws EOFException
	 */
	public static String readUntil(final Reader rd, final String end, final boolean throwEOF, char quote) throws IOException, EOFException {
		final StringBuilder buf = new StringBuilder(100);
		final char[] c = new char[1];
		int read = 0;
		boolean quoted= false;
		while((read=rd.read(c))!=-1) {
			buf.append(c[0]);
			if (end.length() <= buf.length())
				if(c[0] == quote){
					quoted = !quoted;
				}else if (quoted && buf.substring(buf.length()-end.length()).equalsIgnoreCase(end)) {
					break;
				}
		}
		if (read==-1 && throwEOF) throw new EOFException("End of file reached before '"+end+"' was found.");
		if (buf.length()==0) return null;
		return buf.toString();
	}
	/**
	 * Reads until <code>end</code> is reached. The returned String will include the end character unless the
	 * end is reached before the end character is found.<br>
	 * As a result of this behaviour, an empty string means that the end has been reached.<br>
	 * Ignores <var>end</var> if inside " or '.
	 * @param in Reader to read from.
	 * @param end Character to stop at.
	 * @return String of read characters.
	 * @throws IOException
	 */
	public static String readUntilQuoted(final Reader in, final char end) throws IOException {
		final StringBuilder buf = new StringBuilder(255);
		final char[] cbuf = new char[1];
		boolean
			squoted= false,
			dquoted= false;
		while((in.read(cbuf)!=-1)){
			buf.append(cbuf[0]);
			if (cbuf[0]==end && !dquoted && !squoted) {
				break;
			} else if (cbuf[0] == '"' && !squoted){
				dquoted = !dquoted;
			} else if (cbuf[0] == '\'' && !dquoted){
				squoted = !squoted;
			}
		}
		return buf.toString();
	}

	/** Reads until <code>end</code> is found or the end of the stream is reached.<br>
	 * Ignores <var>end</var> if inside " or '.
	 * @param rd
	 * @param end
	 * @param throwEOF Weather or not it should throw an EOFException if EOF is reached.
	 * @return The content read including the <code>end</code> string. Returns null if the first character read is EOF.
	 * @throws IOException
	 * @throws EOFException
	 */
	public static String readUntilQuoted(final Reader rd, final String end, final boolean throwEOF) throws IOException, EOFException {
		final StringBuilder buf = new StringBuilder(100);
		final char[] c = new char[1];
		int read = 0;
		boolean
			squoted= false,
			dquoted= false;
		while((read=rd.read(c))!=-1) {
			buf.append(c[0]);
			if (end.length() <= buf.length())
				if (c[0] == '"' && !squoted){
					dquoted = !dquoted;
				} else if (c[0] == '\'' && !dquoted){
					squoted = !squoted;
				}else if (!dquoted && !squoted && buf.substring(buf.length()-end.length()).equalsIgnoreCase(end)) {
					break;
				}
		}
		if (read==-1 && throwEOF) throw new EOFException("End of file reached before '"+end+"' was found.");
		if (buf.length()==0) return null;
		return buf.toString();
	}
}
