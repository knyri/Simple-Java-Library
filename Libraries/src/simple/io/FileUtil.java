/**
 *
 */
package simple.io;

import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**All close(...) methods now log a warning message.
 * @since 4-19-2012
 * <hr>
 * <br>Created: Sep 10, 2011
 * @author Kenneth Pierce
 */
public final class FileUtil{
	private FileUtil(){}
	static final BlackHoleOutputStream voidos=new BlackHoleOutputStream();
	static final BlackHoleWriter voidw = new BlackHoleWriter();
	public static final void discard(InputStream in) throws IOException{
		copy(in,voidos,4096);
	}
	public static final void discard(Reader in) throws IOException{
		copy(in,voidw,4096);
	}
	/**
	 * Convenience method for <code>formatSize(bytes, 2)</code>.
	 * @param bytes Number of bytes.
	 * @return The number of bytes in condensed form. (B,KB,MB,GB,TB)
	 */
	public static final String formatSize(final long bytes) {
		return FileUtil.formatSize(bytes, 2);
	}
	/**
	 * @param bytes Number of bytes.
	 * @param precision Max number of spaces after the decimal point to show. Max precision is 10.
	 * @return The number of bytes in condensed form. (B,KB,MB,GB,TB)
	 */
	public static final String formatSize(final long bytes, double precision) {
		if (precision > 10)
			throw new Error("Precision is too high. Max supported is 10.");
		final StringBuffer buf = new StringBuffer(10);
		precision = Math.pow(10, precision);
		if (bytes >= 1024) {
			if (bytes >= 1048576) {
				if (bytes >= 1073741824) {
					if (bytes >= 1099511627776l) {
						buf.append(FileUtil.doPrecision((bytes/1099511627776d),precision));
						buf.append(" TB");
					} else {
						buf.append(FileUtil.doPrecision((bytes/1073741824d),precision));
						buf.append(" GB");
					}
				} else {// < 1GB
					buf.append(FileUtil.doPrecision((bytes/1048576d),precision));
					buf.append(" MB");
				}
			} else {// < 1MB
				buf.append(FileUtil.doPrecision((bytes/1024f),precision));
				buf.append(" KB");
			}
		} else {// < 1KB
			buf.append(bytes);
			buf.append(" B ");
		}
		return buf.toString();
	}
	private static final double doPrecision(final double number, double precision) {
		if (precision<0)
			return number;
		if (precision==0)
			return (long)number;
		precision *= 10;
		return ((long)(number*precision))/precision;
	}
	/**
	 * Flushes and closes the stream without throwing an exception
	 * @param os the thing to close
	 */
	public static void close(final OutputStream os) {
		if(os != null) {
			try {os.close(); } catch(final Exception e) {}
		}
	}
	/**
	 * Closes the stream without throwing an exception
	 * @param is the thing to close
	 */
	public static void close(final InputStream is) {
		if(is != null) {
			try {is.close();} catch(final Exception e) {}
		}
	}
	/**
	 * Closes the stream without throwing an exception
	 * @param rd the thing to close
	 */
	public static void close(final Reader rd) {
		if(rd != null) {
			try{rd.close();}catch(final Exception e){}
		}
	}
	/**
	 * Flushes and closes the stream without throwing an exception
	 * @param wr the thing to close
	 */
	public static void close(final Writer wr) {
		if(wr != null) {
			try {wr.close();}catch(final Exception e){}
		}
	}
	/**Closes the object ignoring any errors made.
	 * @param f the thing to close
	 */
	public static final void close(final Closeable f){
		try{f.close();}catch(final Exception e){}
	}
	/**
	 * @param file the file
	 * @return The parent directory
	 */
	public static final File cdup(File file){
		String path=file.getPath();
		int i=path.lastIndexOf(File.separatorChar);
		if(i==-1)return file;
		return new File(path.substring(0,i));
	}
	/** Ensures the directory tree exists and attempts to create it.
	 * @param file the file
	 * @return true if success
	 */
	public static final boolean ensureDir(File file){
		File file2=cdup(file);
		if(file2.exists()||file.equals(file2))return true;
		return file2.mkdirs();
	}
	/**
	 * Creates the file and any necessary directories.
	 * @param file the file
	 * @return True if the file has been created or already exists. False if the creation of the directories or file failed.
	 * @throws IOException See {@linkplain java.io.File#createNewFile()}
	 */
	public static final boolean createFile(File file) throws IOException{
		if(file.exists())return true;
		if(!ensureDir(file))return false;
		return file.createNewFile();
	}
	/**
	 * @param file the file
	 * @return the file without the extension
	 * @since 4-19-2012
	 */
	public static String stripExtension(File file){
		String fileStr= file.toString();
		int idx=fileStr.lastIndexOf(File.separatorChar);
		if(idx==-1)
			idx=fileStr.lastIndexOf('.');
		else{
			String fname=fileStr.substring(idx);
			idx=fname.lastIndexOf('.');
			if(idx==-1)return fileStr;
			return fileStr.substring(0,fileStr.length()-fname.length()+idx);
		}
		if(idx==-1)return fileStr;
		return fileStr.substring(0,idx);
	}
	/**
	 * Copies from input directly to output.
	 * @param input Source
	 * @param output Destination
	 * @param bufferSize Size of byte chunks to be copied at once.
	 * @throws IOException
	 */
	public static void copy(final Reader input, final Writer output, final int bufferSize ) throws IOException {
		final char buffer[] = new char[bufferSize];
		int n = 0;
		while( (n=input.read(buffer)) != -1) {
			output.write(buffer, 0, n);
		}
		output.flush();
	}
	/**
	 * Copies from input directly to output.
	 * @param input Source
	 * @param output Destination
	 * @param bufferSize Size of byte chunks to be copied at once.
	 * @param numBytes number of bytes to copy
	 * @throws IOException
	 */
	public static void copy(final Reader input, final Writer output, final int bufferSize, long numBytes) throws IOException {
		final char buffer[] = new char[bufferSize];
		int n = 0;
		while( numBytes > 0 ) {
			if (bufferSize > numBytes) {
				n = input.read(buffer, 0, (int)numBytes);
			} else {
				n = input.read(buffer, 0, bufferSize);
			}
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
	public static void copy(final InputStream input, final OutputStream output, final int bufferSize ) throws IOException {
		final byte buffer[] = new byte[bufferSize];
		int n = 0;
		while( (n=input.read(buffer)) != -1) {
			output.write(buffer, 0, n);
		}
		output.flush();
	}
	/**
	 * Copies from input directly to output.
	 * @param input Source
	 * @param output Destination
	 * @param buffer Buffer to use.
	 * @throws IOException
	 */
	public static void copy(final InputStream input, final OutputStream output, final byte[] buffer ) throws IOException {
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
	/**
	 * @param b1 the first array
	 * @param b2 the second array
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
	 * @param b1 the first array
	 * @param b2 the second array
	 * @param offset start index
	 * @param length number of bytes to compare
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
	 * @param f1 the first file
	 * @param f2 the second file
	 * @return True if both files are exactly identical.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static boolean compareBytes(File f1, File f2) throws FileNotFoundException, IOException {
		if (f1!=null&&f2!=null&&f1.exists()&&f2.exists())
			if (f1.length()==f2.length()) {
				int read = 0;
				FileInputStream in1 = new FileInputStream(f1),
						in2 = new FileInputStream(f2);
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
	 * @param f1 the first file
	 * @param f2 the second file
	 * @param buffSize read buffer size
	 * @return True if both files are exactly identical.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static boolean compareBytes(File f1, File f2, int buffSize) throws FileNotFoundException, IOException {
		if (f1!=null&&f2!=null&&f1.exists()&&f2.exists())
			if (f1.length()==f2.length()) {
				int read = 0;
				FileInputStream in1 = new FileInputStream(f1),
						in2 = new FileInputStream(f2);
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
	/**Adds all the files to a giant LinkedList. Can add them recursively.
	 * NOTE: will not add directories. Might add symbolic links.
	 * @param start Directory to start in.
	 * @param recursive Whether or not to add files of sub-directories.
	 * @return A LinkedList containing all the files.
	 */
	public static List<File> getFiles(File start, boolean recursive) {
		if (!start.isDirectory()) {return null;}
		List<File> tmp = new LinkedList<File>();
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
		}else{
			return Collections.emptyList();
		}
		return tmp;
	}
	/**Adds all the files to a giant LinkedList. Can add them recursively.
	 * NOTE: will not add directories. Might add symbolic links.
	 * @param start Directory to start in.
	 * @param filter file filter
	 * @param recursive Whether or not to add files of sub-directories.
	 * @return A LinkedList containing all the files.
	 */
	public static List<File> getFiles(File start, FileFilter filter, boolean recursive) {
		if (!start.isDirectory()) {return null;}
		File[] files= start.listFiles();
		List<File> tmp;
		if (files!=null) {
			tmp = new ArrayList<File>((int)(files.length*1.3));
			for (File cur : files) {
				if (cur.isDirectory()) {
					if (recursive) {
						tmp.addAll(getFiles(cur, filter, true));
					}
				} else if(filter.accept(cur)) {
					tmp.add(cur);
				}
			}
			return tmp;
		}
		return Collections.emptyList();
	}
}
