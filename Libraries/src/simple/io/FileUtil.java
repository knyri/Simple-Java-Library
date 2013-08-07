/**
 *
 */
package simple.io;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**All close(...) methods now log a warning message.
 * @since 4-19-2012
 * <hr>
 * <br>Created: Sep 10, 2011
 * @author Kenneth Pierce
 */
public final class FileUtil{
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
	 * @param os
	 */
	public static void close(final OutputStream os) {
		if(os != null) {
			try {os.flush(); os.close(); } catch(final Exception e) {}
		}
	}
	/**
	 * Closes the stream without throwing an exception
	 * @param is
	 */
	public static void close(final InputStream is) {
		if(is != null) {
			try {is.close();} catch(final Exception e) {}
		}
	}
	/**
	 * Closes the stream without throwing an exception
	 * @param rd
	 */
	public static void close(final Reader rd) {
		if(rd != null) {
			try{rd.close();}catch(final Exception e){}
		}
	}
	/**
	 * Flushes and closes the stream without throwing an exception
	 * @param wr
	 */
	public static void close(final Writer wr) {
		if(wr != null) {
			try {wr.flush();wr.close();}catch(final Exception e){}
		}
	}
	/**Closes the object ignoring any errors made.
	 * @param f
	 */
	public static final void close(final Closeable f){
		try{f.close();}catch(final Exception e){}
	}
	/**
	 * @param file
	 * @return The parent directory
	 */
	public static final File cdup(File file){
		String path=file.getPath();
		int i=path.lastIndexOf(File.separatorChar);
		if(i==-1)return file;
		return new File(path.substring(0,i));
	}
	/** Ensures the directory tree exists and attempts to create it.
	 * @param file
	 * @return
	 */
	public static final boolean ensureDir(File file){
		File file2=cdup(file);
		if(file2.exists()||file.equals(file2))return true;
		return file2.mkdirs();
	}
	/**
	 * Creates the file and any necessary directories.
	 * @param file
	 * @return True if the file has been created or already exists. False if the creation of the directories or file failed.
	 * @throws IOException See {@linkplain java.io.File#createNewFile()}
	 */
	public static final boolean createFile(File file) throws IOException{
		if(file.exists())return true;
		if(!ensureDir(file))return false;
		return file.createNewFile();
	}
	/**
	 * @param file
	 * @return
	 * @since 4-19-2012
	 */
	public static String stripExtension(File file){
		int idx=file.toString().lastIndexOf('/');
		if(idx==-1)
			idx=file.toString().lastIndexOf('.');
		else{
			String fname=file.toString().substring(idx);
			idx=fname.lastIndexOf('.');
			if(idx==-1)return file.toString();
			return file.toString().substring(0,file.toString().length()-fname.length()+idx);
		}
		if(idx==-1)return file.toString();
		return file.toString().substring(0,idx);
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
}
