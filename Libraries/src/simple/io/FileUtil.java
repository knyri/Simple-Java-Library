/**
 *
 */
package simple.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * Convenience methods
 * <hr>
 * <br>Created: Sep 10, 2011
 * @author Kenneth Pierce
 */
public final class FileUtil{
	private FileUtil(){}
	static final BlackHoleOutputStream voidos= new BlackHoleOutputStream();
	static final BlackHoleWriter voidw= new BlackHoleWriter();
	/**
	 * Fully reads a file
	 *
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String readFully(File file) throws IOException{
		try(FileReader in= new FileReader(file)){
			return RWUtil.readFully(in);
		}
	}
	/**
	 * Fully reads a file with the specified character set
	 *
	 * @param file
	 * @param cs
	 * @return
	 * @throws IOException
	 */
	public static String readFully(File file, Charset cs) throws IOException {
		return RWUtil.readFully(file, cs);
	}
	public static void write(File file, String data) throws IOException {
		try(Writer out= ReadWriterFactory.getBufferedWriter(file)){
			out.write(data);
			out.flush();
		}
	}
	/**
	 * Appends the string data to the file
	 *
	 * @param file
	 * @param cs
	 * @param data
	 * @throws IOException
	 */
	public static void write(File file, Charset cs, String data) throws IOException {
		try(Writer out= ReadWriterFactory.getBufferedWriter(file, cs)){
			out.write(data);
			out.flush();
		}
	}
	/**
	 * Sums the size of all the files in the directory
	 */
	private static final class PathSummer implements FileVisitor<Path>{
		private IOException error= null;
		private AtomicLong size= new AtomicLong();
		public long getSize() {
			return size.get();
		}
		public IOException getError() {
			return error;
		}
		public boolean hasError() {
			return error != null;
		}
		@Override
		public FileVisitResult postVisitDirectory(Path p, IOException e) throws IOException{
			if(e != null) {
				error= e;
				return FileVisitResult.TERMINATE;
			}
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path arg0, BasicFileAttributes arg1) throws IOException{
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path p, BasicFileAttributes arg1) throws IOException{
			size.addAndGet(Files.size(p));
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path p, IOException e) throws IOException{
			if(e != null) {
				error= e;
				return FileVisitResult.TERMINATE;
			}
			return FileVisitResult.CONTINUE;
		}

	}
	/**
	 * Walks the path to get the fill size
	 * @param p
	 * @return
	 * @throws IOException
	 */
	public static long size(Path p) throws IOException {
		if(p == null || !Files.exists(p)) {
			return 0;
		}
		if(Files.isDirectory(p)) {
			PathSummer size= new PathSummer();
			Files.walkFileTree(p, size);
			if(size.hasError()) {
				throw size.getError();
			}
			return size.getSize();
		}else{
			return Files.size(p);
		}
	}
	/**
	 * discards everything read
	 * @param in
	 * @throws IOException
	 */
	public static void discard(InputStream in) throws IOException{
		copy(in, voidos, 4096);
	}
	/**
	 * discards everything read
	 * @param in
	 * @throws IOException
	 */
	public static void discard(Reader in) throws IOException{
		copy(in, voidw, 4096);
	}
	/**
	 * Convenience method for <code>formatSize(bytes, 2)</code>.
	 * @param bytes Number of bytes.
	 * @return The number of bytes in condensed form. (B,KB,MB,GB,TB)
	 */
	public static String formatSize(final long bytes) {
		return FileUtil.formatSize(bytes, 2);
	}
	/**
	 * @param bytes Number of bytes.
	 * @param precision Max number of spaces after the decimal point to show. Max precision is 10.
	 * @return The number of bytes in condensed form. (B,KB,MB,GB,TB)
	 */
	public static String formatSize(final long bytes, double precision) {
		if (precision > 10){
			throw new Error("Precision is too high. Max supported is 10.");
		}
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
	private static double doPrecision(final double number, double precision) {
		if (precision < 0){
			return number;
		}
		if (precision==0){
			return (long)number;
		}
		precision *= 10;
		return ((long)(number*precision))/precision;
	}
	/**
	 * Closes all the things without throwing an error. Can be null.
	 * @param ac The things to close
	 */
	public static void close(AutoCloseable... ac){
		if(ac != null){
			for(AutoCloseable a : ac){
				if(a == null){
					continue;
				}
				try {a.close();} catch (Exception e) {}
			}
		}
	}
	/**
	 * Closes all the things without throwing an error. Can be null.
	 * @param ac The things to close
	 */
	public static void close(Collection<? extends AutoCloseable> ac){
		if(ac != null){
			for(AutoCloseable a : ac){
				if(a == null){
					continue;
				}
				try {a.close();} catch (Exception e) {}
			}
		}
	}
	/**
	 * @param file the file
	 * @return The parent directory
	 */
	public static File cdup(File file){
		String path= file.getPath();
		int i= path.lastIndexOf(File.separatorChar);
		if(i==-1){
			return file;
		}
		return new File(path.substring(0,i));
	}
	/** Ensures the directory tree exists and attempts to create it.
	 * @param file the file
	 * @return true if success
	 */
	public static boolean ensureDir(File file){
		File file2= cdup(file);
		if(file2.exists() || file.equals(file2)){
			return true;
		}
		return file2.mkdirs();
	}
	/**
	 * Creates the file and any necessary directories.
	 * @param file the file
	 * @return True if the file has been created or already exists. False if the creation of the directories or file failed.
	 * @throws IOException See {@linkplain java.io.File#createNewFile()}
	 */
	public static boolean createFile(File file) throws IOException{
		return file.exists() || ( ensureDir(file) && file.createNewFile() );
	}
	/**
	 * @param file the file
	 * @return the file without the extension
	 * @since 4-19-2012
	 */
	public static String stripExtension(File file){
		return stripExtension(file.toString());
	}
	public static String stripExtension(String file){
		int idx= file.lastIndexOf(File.separatorChar);
		if(idx == -1){
			idx=file.lastIndexOf('.');
		}else{
			String fname=file.substring(idx);
			int dot= fname.lastIndexOf('.');
			if(dot == -1 || dot < idx){
				return file;
			}
			return file.substring(0, file.length() - fname.length() + dot);
		}
		if(idx == -1){
			return file;
		}
		return file.substring(0, idx);
	}
	/**
	 * Copies from input directly to output.
	 * @param input Source
	 * @param output Destination
	 * @param bufferSize Size of byte chunks to be copied at once.
	 * @throws IOException
	 */
	public static void copy(final Reader input, final Writer output, final int bufferSize ) throws IOException {
		final char[] buffer= new char[bufferSize];
		int n= 0;
		while( (n= input.read(buffer)) != -1) {
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
		final char[] buffer= new char[bufferSize];
		int n= 0;
		while(numBytes > 0) {
			if(bufferSize > numBytes) {
				n= input.read(buffer, 0, (int)numBytes);
			} else {
				n= input.read(buffer, 0, bufferSize);
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
		final byte[] buffer= new byte[bufferSize];
		int n= 0;
		while( (n= input.read(buffer)) != -1) {
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
		int n= 0;
		while( (n= input.read(buffer)) != -1) {
			output.write(buffer, 0, n);
		}
		output.flush();
	}
	/**
	 * @param input
	 * @param output
	 * @param bufferSize
	 * @param numBytes Number of bytes to copy
	 * @throws IOException
	 */
	public static void copy(final InputStream input, final OutputStream output, final int bufferSize, long numBytes) throws IOException {
		final byte[] buffer= new byte[bufferSize];
		int n= 0;
		while (numBytes > 0) {
			if (bufferSize > numBytes){
				n= input.read(buffer, 0, (int)numBytes);
			}else{
				n= input.read(buffer, 0, bufferSize);
			}
			output.write(buffer, 0, n);
			numBytes -= n;
		}
		output.flush();
	}
	/**
	 * @param b1 the first array
	 * @param b2 the second array
	 * @return True if the elements of both are identical.
	 */
	public static boolean compareBytes(byte[] b1, byte[] b2) {
		if (b1.length == b2.length) {
			for (int i= 0; i < b1.length; i++) {
				if (b1[i] != b2[i]){
					return false;
				}
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
		if (offset > b1.length || offset > b2.length) {
			return false;
		}
		// Adjust length to not go past the end
		if (offset+length > b1.length){
			length= b1.length - offset;
		}
		if (offset+length > b2.length){
			length= b2.length - offset;
		}

		int max= offset + length;
		for (; offset < max; offset++) {
			if (b1[offset] != b2[offset]){
				return false;
			}
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
	public static boolean compareBytes(File f1, File f2) throws FileNotFoundException, IOException {
		if (f1==null || f2==null || !f1.exists() || !f2.exists() || f1.length() != f2.length()){
			return false;
		}
		int read= 0;
		try(
			FileInputStream in1= new FileInputStream(f1);
			FileInputStream in2 = new FileInputStream(f2)
		){
			byte[] b1= new byte[1024];
			byte[] b2= new byte[1024];
			read= in1.read(b1);
			in2.read(b2);
			while (read != -1) {
				if (!compareBytes(b1,b2,0,read)){
					return false;
				}
				read= in1.read(b1);
				in2.read(b2);
			}
		}
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
	public static boolean compareBytes(File f1, File f2, int buffSize) throws FileNotFoundException, IOException {
		if (f1 == null || f2 == null || !f1.exists() || !f2.exists() || f1.length() != f2.length()){
			return false;
		}
		int read1= 0,
			read2= 0;
		try(
			InputStream in1= new BufferedInputStream(new FileInputStream(f1), buffSize);
			InputStream in2 = new BufferedInputStream(new FileInputStream(f2), buffSize)
		){
			for (read1= in1.read(), read2= in2.read(); read2 != -1 && read1  != -1; read1= in1.read(), read2= in2.read()) {
				if (read1 != read2){
					return false;
				}
			}
		}
		return read2 == -1 && read1  == -1;
	}
	/**Adds all the files to a giant LinkedList. Can add them recursively.
	 * NOTE: will not add directories. Might add symbolic links.
	 * @param dir Directory to start in.
	 * @param recursive Whether or not to add files of sub-directories.
	 * @return A LinkedList containing all the files.
	 */
	public static List<File> getFiles(File dir, boolean recursive) {
		if (!dir.isDirectory()){
			return Collections.emptyList();
		}
		File[] files= dir.listFiles();
		if(files == null){
			return Collections.emptyList();
		}
		List<File> tmp= new LinkedList<File>();
		for (File cur : files) {
			if (!cur.isDirectory()) {
				tmp.add(cur);
			} else if (recursive) {
				tmp.addAll(getFiles(cur, true));
			}
		}
		return tmp;
	}
	/**Adds all the files to a giant LinkedList. Can add them recursively.
	 * NOTE: will not add directories. Might add symbolic links.
	 * @param dir Directory to start in.
	 * @param filter file filter
	 * @param recursive Whether or not to add files of sub-directories.
	 * @return A LinkedList containing all the files.
	 */
	public static List<File> getFiles(File dir, FileFilter filter, boolean recursive) {
		if (!dir.isDirectory()) {
			return Collections.emptyList();
		}
		File[] files= dir.listFiles();
		if(files == null){
			return Collections.emptyList();
		}
		List<File> tmp= new ArrayList<File>((int)(files.length*1.3));
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
	/**
	 * deleteDir walker
	 */
	private static final class DeleteTree implements FileVisitor<Path>{
		private IOException error;
		public IOException getError() {
			return error;
		}
		public boolean hasError() {
			return error != null;
		}
		@Override
		public FileVisitResult postVisitDirectory(Path p, IOException e) throws IOException{
			if(e != null) {
				error= e;
				return FileVisitResult.TERMINATE;
			}
			Files.delete(p);
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path p, BasicFileAttributes bfa) throws IOException{
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path p, BasicFileAttributes bfa) throws IOException{
			Files.delete(p);
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path p, IOException e) throws IOException{
			if(e != null) {
				error= e;
				return FileVisitResult.TERMINATE;
			}
			return FileVisitResult.CONTINUE;
		}
	}
	/**
	 * Recursively deletes a directory
	 *
	 * @param dir
	 * @throws IOException
	 */
	public static void deleteDir(Path dir) throws IOException{
		if(dir == null || !Files.exists(dir)) {
			return;
		}
		DeleteTree res= new DeleteTree();
		Files.walkFileTree(dir, res);
		if(res.hasError()) {
			throw res.getError();
		}
	}
	/**
	 * Recursively deletes a directory
	 *
	 * @param dir
	 * @return
	 */
	public static boolean deleteDir(File dir){
		if(dir == null || !dir.exists()) {
			return true;
		}
		for(File file : dir.listFiles()){
			if(file.isDirectory()){
				if(!deleteDir(file)){
					return false;
				}
			}else{
				if(!file.delete()){
					return false;
				}
			}
		}
		return dir.delete();
	}
	/**
	 * Copies files. Throws an error on any creation error
	 * @param to Destination directory
	 * @param from File(s) to copy
	 * @throws IOException
	 */
	public static void copyFiles(File to, File... from) throws IOException{
		copyFiles(to, new byte[2048*5], from);
	}
	/**
	 * Copies files
	 * @param to Destination directory
	 * @param buf Copy buffer
	 * @param from Files(s) to copy
	 * @throws IOException
	 */
	public static void copyFiles(File to, byte[] buf, File... from) throws IOException{
		File tmp;
		for(File file: from){
			tmp= new File(to, file.getName());
			if(file.isDirectory()){
				if(!tmp.mkdir()){
					throw new IOException("Failed to create the directory " + tmp.getAbsolutePath());
				}
				copyFiles(tmp, file.listFiles());
			}else{
				if(!tmp.createNewFile()){
					throw new IOException("Failed to create the file " + tmp.getAbsolutePath());
				}
				FileUtil.copyFile(file, tmp, buf);
			}
		}
	}
	/**
	 * Copies a file
	 *
	 * @param from
	 * @param to
	 * @param buf Byte buffer to use when copying
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void copyFile(File from, File to, byte[] buf) throws FileNotFoundException, IOException{
		try(
			InputStream is= new FileInputStream(from);
			OutputStream os= new FileOutputStream(to);
		){
			FileUtil.copy(is, os, buf);
		}
	}
	/**
	 * Copies a file using a 10KB buffer
	 *
	 * @param from
	 * @param to
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void copyFile(File from, File to) throws FileNotFoundException, IOException{
		copyFile(from, to, new byte[2048*5]);
	}
	/**
	 * Run the consumer on all files in the directory
	 *
	 * @param dir
	 * @param consumer
	 * @throws IOException
	 */
	public static void consumeFiles(Path dir, Consumer<Path> consumer) throws IOException{
		try(DirectoryStream<Path> files= Files.newDirectoryStream(dir)){
			for(Path cur: files){
				consumer.accept(cur);
			}
		}
	}
}
