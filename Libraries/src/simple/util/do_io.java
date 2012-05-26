package simple.util;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.filechooser.FileFilter;

import simple.util.logging.LogFactory;
/**
 * Does various IO things.
 * <hr>
 * Other dependencies:<br>
 * simple.util.do_str<br>
 * simple.util.logging.*
 * <br>Created: 2004
 * @author Kenneth Pierce
 * @deprecated {@link simple.util.App},{@link simple.io.FilterFactory},{@link simple.io.StreamFactory},{@link simple.io.ReadWriterFactory}
 */
public final class do_io {
	//private static final int num_threads = Runtime.getRuntime().availableProcessors();
	public static int _jobBufferSize = 8192;
	static class WriterJob implements Runnable {
		final Writer _out;
		final Reader _in;
		final int _bufferSize;
		public WriterJob(Reader in, Writer out) {
			_in = in;
			_out = out;
			_bufferSize = do_io._jobBufferSize;
		}
		public void run() {
			try {
				do_io.copy(_in, _out, _bufferSize);
			} catch (IOException e) {
				LogFactory.getLogFor(WriterJob.class).warning(e);
			}
		}
	}
	static class StreamJob implements Runnable {
		final OutputStream _out;
		final InputStream _in;
		final int _bufferSize;
		public StreamJob(InputStream in, OutputStream out) {
			_in = in;
			_out = out;
			_bufferSize = _jobBufferSize;
		}
		public void run() {
			try {
				copy(_in, _out, _bufferSize);
			} catch (IOException e) {
				LogFactory.getLogFor(StreamJob.class).warning(e);
			}
		}
	}
	/**
	 * @param dir
	 * @param ext
	 * @return
	 * @deprecated {@link simple.io.FilterFactory}
	 */
	public static File[] listFiles(String dir, String ext) {
		File file = new File(dir);
		return file.listFiles(createFilenameFilter(ext));
	}
	static Hashtable<String,FileFilter> ffcache = new Hashtable<String,FileFilter>();
	static Hashtable<String,FilenameFilter>fnfcache = new Hashtable<String,FilenameFilter>();
	/**
	 * @param list
	 * @return
	 * @deprecated {@link simple.io.FilterFactory}
	 */
	public static FilenameFilter createFilenameFilter(String list) {
		final String key = list.toLowerCase();
		FilenameFilter ff = fnfcache.get(key);
		if (ff==null)
			ff = new FilenameFilter() {
					final String flist = key;
					public boolean accept(File dir,String file) {
						String ext = file.substring(file.lastIndexOf('.')+1);
						return flist.contains(ext);
					}
				};
			else
				fnfcache.put(key, ff);
		return ff;
	}
	/**
	 * @param list
	 * @return
	 * @deprecated {@link simple.io.FilterFactory}
	 */
	public static FileFilter createFileFilter(String list) {
		final String key = list.toLowerCase();
		FileFilter ff = ffcache.get(key);
		if (ff==null)
			ff = new FileFilter() {
					final String flist = key;
					@Override
					public boolean accept(File file) {
						String ext = file.toString();
						ext = ext.substring(ext.lastIndexOf('.')+1);
						return flist.contains(ext);
					}
		
					@Override
					public String getDescription() {
						return flist;
					}
				};
			else
				ffcache.put(key, ff);
		return ff;
	}
	
	/**Reads until the first non-whitespace character is found.
	 * @param rd
	 * @return The non-whitespace character or -1
	 * @throws IOException
	 */
	public static int skipWhitespace(Reader rd) throws IOException {
		int c = rd.read();
		if (c==-1) return c;
		do {
			if (!Character.isWhitespace((char)c)) break;
			c = rd.read();
		} while(c!=-1);
		return c;
	}
	/**Reads until the first non-whitespace character is found.
	 * @param rd
	 * @return The non-whitespace character or -1
	 * @throws IOException
	 */
	public static char skipWhitespace(InputStream rd) throws IOException {
		int c = rd.read();
		if (c==-1) return (char)c;
		do {
			if (!Character.isWhitespace(c)) break;
			c = rd.read();
		} while(c==-1);
		return (char)c;
	}
	/** Reads until <code>end</code> is found or the end of the stream is reached.
	 * @param rd
	 * @param end
	 * @param throwEOF Weather or not it should throw an EOFException if EOF is reached.
	 * @return The content read including the <code>end</code> string. Returns null if the first character read is EOF.
	 * @throws IOException
	 * @throws EOFException
	 */
	public static String readUntil(Reader rd, String end, boolean throwEOF) throws IOException, EOFException {
		StringBuilder buf = new StringBuilder(100);
		char[] c = new char[1];
		int read = 0;
		while((read=rd.read(c))!=-1) {
			buf.append(c[0]);
			if (end.length() <= buf.length())
				if (buf.substring(buf.length()-end.length()).equals(end))
					break;
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
	public static String readUntil(Reader rd, String end) throws IOException {
		return readUntil(rd,end,false);
	}
	/**
	 * @param file
	 * @return a FileInputStream wrapped in a BufferedInputStream
	 * @throws FileNotFoundException
	 */
	public static BufferedInputStream getBufferedInputStream(File file) throws FileNotFoundException {
		return new BufferedInputStream(new FileInputStream(file));
	}
	/**
	 * @param file
	 * @return A FileOutputStream wrapped in a BufferedOutputStream.
	 * @throws FileNotFoundException If the file is a directory or if it cannot be created if it does not exist.
	 */
	public static BufferedOutputStream getBufferedOutputStream(File file) throws FileNotFoundException {
		return new BufferedOutputStream(new FileOutputStream(file));
	}
	/**
	 * Takes an InputStream and wraps it in a BufferedInputStream.
	 * @param in
	 * @return InputStream wrapped in a BufferedInputStream. If <var>in</var> is already
	 * 			a BufferedInputStream then it is casted and returned.
	 */
	public static BufferedInputStream getBufferedInputStream(InputStream in) {
		if (in instanceof BufferedInputStream)
			return (BufferedInputStream)in;
		return new BufferedInputStream(in);
	}
	/**
	 * Wraps an OutputStream in a BufferedOutputStream.
	 * @param out
	 * @return OutputStream wrapped in a BufferedOutputStream. If <var>out</var> is already
	 * 			a BufferedOutputStream then it is casted and returned.
	 */
	public static BufferedOutputStream getBufferedOutputStream(OutputStream out) {
		if (out instanceof BufferedOutputStream)
			return (BufferedOutputStream)out;
		return new BufferedOutputStream(out);
	}
	/**
	 * @param file
	 * @return a FileReader wrapped in a BufferedInputStream
	 * @throws FileNotFoundException
	 */
	public static BufferedReader getBufferedReader(File file) throws FileNotFoundException {
		return new BufferedReader(new FileReader(file));
	}
	/**
	 * @param file
	 * @return a FileWriter wrapped in a BufferedInputStream
	 * @throws FileNotFoundException
	 */
	public static BufferedWriter getBufferedWriter(File file) throws IOException {
		return new BufferedWriter(new FileWriter(file));
	}
	/**
	 * Wraps a Reader in a BufferedReader.
	 * @param rd
	 * @return Reader wrapped in a BufferedReader. If <var>rd</var> is already
	 * 			a BufferedReader then it is casted and returned.
	 */
	public static BufferedReader getBufferedReader(Reader rd) {
		if(rd instanceof java.io.BufferedReader)
			return (BufferedReader)rd;
		return new BufferedReader(rd);
	}
	/**
	 * Wraps a Writer in a BufferedWriter.
	 * @param wr
	 * @return Writer wrapped in a BufferedWriter. If <var>wr</var> is already
	 * 			a BufferedWriter then it is casted and returned.
	 */
	public static BufferedWriter getBufferedWriter(Writer wr) {
		if(wr instanceof java.io.BufferedWriter)
			return (BufferedWriter)wr;
		return new BufferedWriter(wr);
	}
	/**
	 * Closes the stream without throwing an exception
	 * @param file
	 */
	public static void close(RandomAccessFile file) {
		if(file != null) {
			try { file.close(); } catch(Exception ex) {}
		}
	}
	/**
	 * Closes the stream without throwing an exception
	 * @param is
	 */
	public static void close(InputStream is) {
		if(is != null) {
			try { is.close(); } catch(Exception ex) {}
		}
	}
	/**
	 * Flushes and closes the stream without throwing an exception
	 * @param os
	 */
	public static void close(OutputStream os) {
		if(os != null) {
			try {os.flush(); os.close(); } catch(Exception ex) {}
		}
	}
	/**
	 * Closes the stream without throwing an exception
	 * @param rd
	 */
	public static void close(Reader rd) {
		if(rd != null) {
			try { rd.close(); } catch(Exception ex) {}
		}
	}

	/**
	 * Flushes and closes the stream without throwing an exception
	 * @param wr
	 */
	public static void close(Writer wr) {
		if(wr != null) {
			try {wr.flush(); wr.close(); } catch(Exception ex) {}
		}
	}
	/**
	 * Copies from input directly to output.
	 * @param input Source
	 * @param output Destination
	 * @param bufferSize Size of byte chunks to be copied at once.
	 * @throws IOException
	 */
	public static void copy(Reader input, Writer output, int bufferSize ) throws IOException {
		char buffer[] = new char[bufferSize];
		int n = 0;
		while( (n=input.read(buffer)) != -1) {
			output.write(buffer, 0, n);
		}
		output.flush();
	}
	public static void copyThread(Reader input, Writer output) {
		new Thread(new WriterJob(input, output)).start();
	}
	public static void copy(Reader input, Writer output, int bufferSize, long numBytes) throws IOException {
		char buffer[] = new char[bufferSize];
		int n = 0;
		while( numBytes > 0 ) {
			if (bufferSize > numBytes)
				n = input.read(buffer, 0, (int)numBytes);
			else
				n = input.read(buffer, 0, bufferSize);
			output.write(buffer, 0, n);
			numBytes -= n;
		}
		output.flush();
	}
	/**
	 * Copies from input directly to output.
	 * @param input Source
	 * @param output Destination
	 * @param bufferSize Size of byte chunks the be copied at once.
	 * @throws IOException
	 */
	public static void copy(InputStream input, OutputStream output, int bufferSize ) throws IOException {
		byte buffer[] = new byte[bufferSize];
		int n = 0;
		while( (n=input.read(buffer)) != -1) {
			output.write(buffer, 0, n);
		}
		output.flush();
	}
	public static void copyThread(InputStream input, OutputStream output) {
		new Thread(new StreamJob(input, output)).start();
	}
	public static void copy(InputStream input, OutputStream output, int bufferSize, long numBytes ) throws IOException {
		byte buffer[] = new byte[bufferSize];
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
	/**
	 * Reads all available data from reader.
	 * @param reader Source
	 * @return Everything contained in reader as a string.
	 * @throws IOException
	 */
	public static String readFully(Reader reader) throws IOException {
		StringWriter writer = new StringWriter();
		copy(reader, writer, 1024);
		return writer.toString();
	}
	/**
	 * Reads all available data from input.
	 * @param input Source
	 * @return Everything contained in input as a string.
	 * @throws IOException
	 */
	public static String readFully(InputStream input) throws IOException {
		StringWriter writer = new StringWriter();
		InputStreamReader reader = new InputStreamReader(input);
		copy(reader, writer, 1024);
		return writer.toString();
	}
	public static void readInto(Reader reader, StringBuilder buf) throws IOException {
		char[] buffer = new char[1024];
		int length = 0;
		while ((length=reader.read(buffer))!=-1) {
			buf.append(buffer, 0, length);
		}
	}
	/**
	 * Reads until <code>end</code> is reached. The returned String includes the end character.
	 * @param in Reader to read from.
	 * @param end Character to stop at.
	 * @return String of read characters.
	 * @throws IOException
	 */
	public static String readUntil(Reader in, char end) throws IOException {
		StringBuffer buf = new StringBuffer(255);
		char[] cbuf = new char[1];
		while((in.read(cbuf)!=-1)){
			buf.append(cbuf[0]);
			if (cbuf[0]==end) {
				break;
			}
		}
		return buf.toString();
	}
	/**
	 * Reads until <code>end</code> is reached. The returned array includes the end byte.
	 * @param in InputStream to read from.
	 * @param end Byte to stop at.
	 * @return An array of the bytes read.
	 * @throws IOException
	 */
	public static byte[] readUntil(InputStream in, byte end) throws IOException {
		Vector<Byte> buf = new Vector<Byte>();
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

	/**
	 * @param b1
	 * @param b2
	 * @return True of the elements of both are identical.
	 */
	public static boolean compareBytes(byte[] b1, byte[] b2) {
		if (b1.length == b2.length) {
			for (int i = 0; i<b1.length; i++) {
				if (b1[i]!=b2[i]) return false;
			}
		} else {
			return false;
		}
		return true;
	}
	/**
	 * @param b1
	 * @param b2
	 * @param offset
	 * @param length
	 * @return true if the range is identical
	 */
	public static boolean compareBytes(byte[] b1, byte[] b2, int offset, int length) {
		if (offset < b1.length && offset < b2.length) {
			if (offset+length > b1.length)
				length = b1.length - offset;
			if (offset+length > b2.length)
				length = b2.length - offset;
			int max = offset+length;
			for (; offset<max; offset++) {
				if (b1[offset]!=b2[offset]) return false;
			}
		} else {
			return false;
		}
		return true;
	}
	/**
	 * Tests two files to see if they are identical on the bit level.
	 * @param f1
	 * @param f2
	 * @return True if both files are exactly identical.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static boolean compareBytes(File f1, File f2) throws FileNotFoundException, IOException {
		if (f1!=null&&f2!=null&&f1.exists()&&f2.exists())
			if (f1.length()==f2.length()) {
				int read = 0;
				FileInputStream in1 = new FileInputStream(f1);
				FileInputStream in2 = new FileInputStream(f2);
				byte[] b1 = new byte[1024];
				byte[] b2 = new byte[1024];
				read = in1.read(b1);
				in2.read(b2);
				for (;read != -1;) {
					if (!compareBytes(b1,b2,0,read)) return false;
					read = in1.read(b1);
					in2.read(b2);
				}
				close(in1);
				close(in2);
			} else {
				return false;
			}
		else
			return false;
		return true;
	}
	/**
	 * Tests two files to see if they are identical on the bit level.
	 * @param f1
	 * @param f2
	 * @param buffSize
	 * @return True if both files are exactly identical.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static boolean compareBytes(File f1, File f2, int buffSize) throws FileNotFoundException, IOException {
		if (f1!=null&&f2!=null&&f1.exists()&&f2.exists())
			if (f1.length()==f2.length()) {
				int read = 0;
				FileInputStream in1 = new FileInputStream(f1);
				FileInputStream in2 = new FileInputStream(f2);
				byte[] b1 = new byte[buffSize];
				byte[] b2 = new byte[buffSize];
				read = in1.read(b1);
				in2.read(b2);
				for (;read != -1;) {
					if (!compareBytes(b1,b2,0,read)) return false;
					read = in1.read(b1);
					in2.read(b2);
				}
				close(in1);
				close(in2);
			} else {
				return false;
			}
		else
			return false;
		return true;
	}
	/**
	 * Sorts the files alphabetically by path and filename.
	 * Relys on {@link simple.util.do_str}.
	 * @param list Files to be sorted
	 * @return The sorted list.
	 */
	public static File[] sort(File[] list) {
		File tmp = null;
		boolean change = true;
		while (change) {
			change = false;
			for (int i = 0;i<list.length-1;i++) {
				//System.out.println(list[i]+" "+list[i+1]+" = "+compare(list[i], list[i+1]));
				if (do_str.compare(list[i].toString(), list[i+1].toString())<0) {
					tmp = list[i];
					list[i] = list[i+1];
					list[i+1] = tmp;
					change = true;
				}
			}
		}
		return list;
	}

	/**
	 * Reads until a newline or EOF is reached.<br>
	 * Trims trailing carriage return if found.
	 * @param in
	 * @param buf
	 * @return String containing the read line.
	 * @throws IOException
	 */
	public static int readLine(Reader in, StringBuffer buf) throws IOException {
		int i = 0;
		char[] c = new char[1];
		while ((i=in.read(c))!=-1) {
			if (c[0]=='\n') break;
			buf.append(c);
		}
		if (buf.length()>0)
			if (buf.charAt(buf.length()-1)=='\r') {
				buf.deleteCharAt(buf.length()-1);
			}
		return i;
	}
	/**Adds all the files to a giant vector. Can add them recursively.
	 * NOTE: will not add directories. Might add symbolic links.
	 * @param start Directory to start in.
	 * @param recursive Whether or not to add files of sub-directories.
	 * @return A vector containing all the files.
	 */
	public static Vector<File> getFiles(File start, boolean recursive) {
		if (!start.isDirectory()) {return null;}
		Vector<File> tmp = new Vector<File>();
		if (start.listFiles()!=null) {
			for (File cur : start.listFiles()) {
				if (cur.isDirectory()) {
					if (recursive) {
						tmp.addAll(getFiles(cur, true));
					}
				} else {
					tmp.add(cur);
				}
			}
		}
		return tmp;
	}
	/**
	 * Attempts to create a File object that references the location of the file.<br>
	 * It will use the <code>do_io</code> class as the loading class.
	 * NOTE: This is very prone to fail if the URL returned is not a normal file.
	 * The file is considered abnormal if the protocol of the returned URL is not
	 * 'file'.
	 * @see java.lang.Class#getResource(String)
	 * @param location Location of the file relative to the "start in" folder.
	 * @return A File object referencing the URL returned from {@link java.lang.Class#getResource(String)}.
	 * @throws URISyntaxException Happens if the returned URL cannot be transformed into a valid URI.
	 * This should never occur.
	 * @deprecated {@link simple.util.App}
	 */
	public static File getResourceAsFile(String location) throws URISyntaxException, FileNotFoundException {
		return getResourceAsFile(location, do_io.class);
	}
	/**
	 * Attempts to create a File object that references the location of the file.<br>
	 * It will use the <code>loader</code> class as the loading class.
	 * NOTE: This is very prone to fail if the URL returned is not a normal file.
	 * The file is considered abnormal if the protocol of the returned URL is not
	 * 'file'.
	 * @see java.lang.Class#getResource(String)
	 * @param location Location of the file relative to the "start in" folder.
	 * @param loader Class to use to load the file.
	 * @return A File object referencing the URL returned from {@link java.lang.Class#getResource(String)}.
	 * @throws URISyntaxException Happens if the returned URL cannot be transformed into a valid URI.
	 * @throws FileNotFoundException Occurs if the loader returns a null. Avoids a nasty nondescript NullPointerException.
	 * @deprecated {@link simple.util.App}
	 */
	public static File getResourceAsFile(String location, Class<?> loader) throws FileNotFoundException {
		URL tmp = loader.getClassLoader().getResource(location);
		if (tmp==null) throw new FileNotFoundException("The file "+location+" could not be found.");
		return new File(tmp.toString());
	}
	/**
	 * @param location
	 * @param loader
	 * @return
	 * @throws IOException
	 * @deprecated {@link simple.util.App}
	 */
	public static BufferedInputStream getResourceForReading(String location, Class<?> loader) throws IOException {
		URL tmp = loader.getClassLoader().getResource(location);
		if (tmp==null) throw new FileNotFoundException("The file "+location+" could not be found.");
		return new BufferedInputStream(tmp.openStream());
	}
	/**Convenience method that opens a resource for writing.
	 * @param location
	 * @param loader may be null
	 * @return A PrintStream to the resource
	 * @throws FileNotFoundException
	 * @throws URISyntaxException
	 * @deprecated {@link simple.util.App}
	 */
	public static final PrintStream openOutputResource(String location, Class<?> loader) throws FileNotFoundException, URISyntaxException {
		if (loader!=null)
			return openOutputFile(getResourceAsFile(location, loader));
		else
			return openOutputFile(getResourceAsFile(location));
	}
	/**Convenience method that opens a resource for reading.
	 * @param location
	 * @param loader may be null
	 * @return A FileReader to the resource
	 * @throws FileNotFoundException
	 * @throws URISyntaxException
	 * @deprecated use {@link App#getResourceAsFile(String, Class)}
	 */
	public static final FileReader openInputResource(String location, Class<?> loader) throws FileNotFoundException, URISyntaxException {
		if (loader!=null)
			return openInputFile(getResourceAsFile(location, loader));
		else
			return openInputFile(getResourceAsFile(location));
	}
	/**Convenience method.
	 * @param file
	 * @return A PrintStream to the resource
	 * @throws FileNotFoundException
	 * @deprecated {@link simple.util.App}
	 */
	public static final PrintStream openOutputFile(File file) throws FileNotFoundException {
		return new PrintStream(new FileOutputStream(file));
	}
	/**Convenience method.
	 * @param file
	 * @return A FileReader to the resource
	 * @throws FileNotFoundException
	 * @deprecated {@link simple.util.App}
	 */
	public static final FileReader openInputFile(File file) throws FileNotFoundException {
		return new FileReader(file);
	}
	public static String toString(Reader in) throws IOException {
		StringWriter out = new StringWriter();
		copy(in, out, 256);
		close(in);
		close(out);
		return out.toString();
	}
	public static String formatSize(long size) {
		StringBuffer buf = new StringBuffer(10);
		if (size >= 1024) {
			if (size >= 1048576) {
				if (size >= 1073741824) {
					buf.append(((long)((size/1073741824d)*100))/100.0);
					buf.append(" GB");
				} else {// < 1GB
					buf.append(((long)((size/1048576d)*100))/100.0);
					buf.append(" MB");
				}
			} else {// < 1MB
				buf.append(((long)((size/1024f)*100))/100.0);
				buf.append(" KB");
			}
		} else {// < 1KB
			buf.append(size);
			buf.append(" B ");
		}
		return buf.toString();
	}
	/**
	 * @param ext
	 * @param desc
	 * @return
	 * @deprecated {@link simple.io.FilterFactory}
	 */
	public static FileFilter createFileFilter(final String ext, final String desc) {
		return new javax.swing.filechooser.FileFilter() {
			final String extention = ext.intern();
			final String description = desc.intern();
			public boolean accept(File file) {return file.getName().endsWith(extention) || file.isDirectory();}
			@Override
			public String getDescription() {return description;}
		};
	}
	/**
	 * @param dir
	 * @param ext
	 * @return
	 * @deprecated {@link simple.io.FilterFactory}
	 */
	public static FilenameFilter createFilenameFilter(final File dir, final String ext) {
		return new FilenameFilter() {
			final File directory = dir;
			final String extention = ext;
			public boolean accept(File dir, String name) {
				if (directory != null) {
					if (dir.equals(directory))
						if (name.endsWith(extention)) return true;
				} else if (name.endsWith(extention)) return true;
				return false;
			}
		};
	}
}