/**
 *
 */
package simple.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.ImageIcon;

/**Provides convenience methods for applications.<br>
 * For exception-less methods, the exception will be logged using
 * {@link simple.util.logging.Log}.
 * @since 5/2011
 * @author Ken
 *
 */
public final class App {
	/**
	 * Name separator in file paths
	 * File.separator
	 * @return File.separator
	 */
	public static String fileSeparator(){
		return File.separator;
	}
	/**
	 * Path separator in path lists
	 * File.pathSeparator
	 * @return File.pathSeparator
	 */
	public static String pathSeparator(){
		return File.pathSeparator;
	}
	/**
	 * Name of the default character encoding used when dealing with files
	 * @return System.getProperty("file.encoding")
	 */
	public static String defaultCharacterEncoding(){
		return System.getProperty("file.encoding");
	}
	/**
	 * The default line ending
	 * @return System.getProperty("line.separator")
	 */
	public static String defaultLineEnding(){
		return System.getProperty("line.separator");
	}
	/**
	 * The user's home directory; if they have one
	 * @return System.getProperty("user.home")
	 */
	public static String homeDirectory(){
		return System.getProperty("user.home");
	}
	/**
	 * The default tmp directory use when making temporary files
	 * @return System.getProperty("java.io.tmpdir")
	 */
	public static String tempDirectory(){
		return System.getProperty("java.io.tmpdir");
	}
	/**
	 * The directory that the application started in
	 * @return System.getProperty("user.dir")
	 */
	public static String startingDirectory(){
		return System.getProperty("user.dir");
	}
	/**
	 * The username of the current user
	 * @return System.getProperty("user.name")
	 */
	public static String username(){
		return System.getProperty("user.name");
	}
	/**
	 * Static true/false strings for use with storing true/false values in a
	 * Properties or other object.
	 */
	public static final String
		TRUE="true",
		FALSE="false";
	/**Turns the boolean into a static internalized string.
	 * @param tf The boolean
	 * @return {@link #TRUE} or {@link #FALSE}
	 */
	public static final String TF(final boolean tf){
		return tf ? TRUE : FALSE;
	}
	/**Turns the string into a boolean.
	 * Does a <code>tf.equalsIgnoreCase({@link #TRUE})</code>
	 * @param tf Turns the string into a boolean
	 * @return true or false
	 */
	public static final boolean TF(final String tf){
		return tf.equalsIgnoreCase(TRUE);
	}
	public static boolean isEmpty(String str){
		return str == null || str.isEmpty();
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
	 * @throws FileNotFoundException Occurs if the loader returns a null. Avoids a nasty nondescript NullPointerException.
	 * @throws URISyntaxException
	 */
	public static final File getResourceAsFile(final String location, final Class<?> loader) throws FileNotFoundException, URISyntaxException {
		final URL tmp= loader.getResource(location);
		if (tmp == null){
			final File f= new File(location);
			if (!f.exists()){
				throw new FileNotFoundException(location+" could not be found.");
			}else{
				return f;
			}
		}
		return new File(tmp.toURI());
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
	 * @return A URL object referencing the URL returned from {@link java.lang.Class#getResource(String)}.
	 * @throws FileNotFoundException Occurs if the loader returns a null
	 */
	public static final URL getResource(final String location, final Class<?> loader) throws FileNotFoundException {
		URL file= loader.getResource(location);
		if (file == null){
			final File f= new File(location);
			if(!f.exists()){
				throw new FileNotFoundException(location+" could not be found.");
			}
			try{
				file= f.toURI().toURL();
			}catch(final MalformedURLException e){
				FileNotFoundException fse= new FileNotFoundException(location + " could not be found.");
				fse.initCause(e);
				throw fse;
			}
		}
		return file;
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
	 * @return A URL object referencing the URL returned from {@link java.lang.Class#getResource(String)}.
	 * @throws FileNotFoundException Occurs if the loader returns a null
	 */
	public static final InputStream getResourceAsStream(final String location, final Class<?> loader) throws FileNotFoundException {
		final InputStream file= loader.getResourceAsStream(location);
		if (file == null){
			throw new FileNotFoundException(location + " could not be found.");
		}
		return file;
	}
	public static final ImageIcon createImageIcon(final String path, final String description, final Class<?> loader) throws FileNotFoundException {
		final java.net.URL imgURL= getResource(path, loader);
		return new ImageIcon(imgURL, description);
	}
	public static final boolean playSound(final String file){
		return playSound(new File(file));
	}
	/**Plays the specified sound.
	 * @param file File to be played.
	 * @return true on success. False if an error occurred.
	 */
	public static final boolean playSound(final File file){
		try(
			Clip clip= AudioSystem.getClip();
			AudioInputStream ais= AudioSystem.getAudioInputStream(file)
		){
			clip.open(ais);
			clip.start();
		}catch(final Exception e){
			return false;
		}
		return true;
	}
	/**Plays the specified sound.
	 * Any errors are sent to the error log.
	 * @param file File to be played.
	 * @param gain How much to increase or decrease the volume(in dB)
	 * @return true on success. False if an error occurred.
	 */
	public static final boolean playSound(final File file, float gain){
		try(
			Clip clip= AudioSystem.getClip();
			AudioInputStream ais= AudioSystem.getAudioInputStream(file)
		){
			clip.open(ais);
			if(gain!=0){
				final FloatControl gainControl= (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
				if(gain > gainControl.getMaximum()){
					gain= gainControl.getMaximum();
				}else
				if(gain < gainControl.getMinimum()){
					gain= gainControl.getMinimum();
				}
				gainControl.setValue(gain);
			}
			clip.start();
		}catch(final Exception e){
			return false;
		}
		return true;
	}
	/**
	 * Tests to see if the on bits in <var>option</var> are also on in <var>options</var>.
	 * @param options Options the user set.
	 * @param option Option to be tested.
	 * @return The result of <code>(options&amp;option)==option</code>
	 */
	public static boolean isSet(final int options, final int option) {
		return (options & option) == option;
	}
	/**
	 * Tests to see if the on bits in <var>option</var> are also on in <var>options</var>.
	 * @param options Options the user set.
	 * @param option Option to be tested.
	 * @return The result of <code>(options&amp;option)==option</code>
	 */
	public static boolean isSet(final long options, final long option) {
		return ( options & option) == option;
	}
	public static enum LineEnding {
		MAC("\r"),
		UNIX("\n"),
		WINDOWS("\r\n");
		private final String ending;
		LineEnding(String ending) {
			this.ending = ending;
		}
		public boolean isEnd(String ending) {
			return this.ending.equals(ending);
		}
		public String getEnd() {
			return ending;
		}
		/**
		 * @param line
		 * @return The LineEnding or null
		 */
		public static final LineEnding getLineEndingType(String line) {
			if(line == null || line.isEmpty()){
				return null;
			}
			int i= line.length() - 1;
			for (; i>0; i--) {
				if (line.charAt(i) != '\r' && line.charAt(i) != '\n'){
					break;
				}
			}
			String ending= line.substring(i);
			for (LineEnding t: LineEnding.values()) {
				if (t.isEnd(ending)){
					return t;
				}
			}
			return null;
		}
	};
	/**
	 * Moved to LineEnding
	 * @param line
	 * @return The LineEnding or null
	 * @deprecated
	 */
	@Deprecated
	public static final LineEnding getLineEndingType(String line) {
		return LineEnding.getLineEndingType(line);
	}
	public static void print(Object str){
		System.out.print(str);
	}
	public static void println(Object str){
		System.out.println(str);
	}
	public static void println(){
		System.out.println();
	}
}
