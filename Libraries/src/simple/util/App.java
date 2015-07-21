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

import simple.util.logging.Log;
import simple.util.logging.LogFactory;

/**Provides convenience methods for applications.<br>
 * For exception-less methods, the exception will be logged using
 * {@link simple.util.logging.Log}.
 * @since 5/2011
 * @author Ken
 *
 */
public final class App {
	private static final Log log=LogFactory.getLogFor(App.class);
	/**
	 * Static true/false strings for use with storing true/false values in a
	 * Properties or other object.
	 */
	public static final String TRUE="true",FALSE="false";
	/**Turns the boolean into a static internalized string.
	 * @param tf The boolean
	 * @return {@link #TRUE} or {@link #FALSE}
	 * @see {@link #TF(String)}
	 */
	public static final String TF(final boolean tf){
		if(tf)
			return TRUE;
		else
			return FALSE;
	}
	/**Turns the string into a boolean.
	 * Does a <code>tf.equalsIgnoreCase({@link #TRUE})</code>
	 * @param tf Turns the string into a boolean
	 * @return true or false
	 * @see {@link #TF(boolean)}
	 */
	public static final boolean TF(final String tf){
		return tf.equalsIgnoreCase(TRUE);
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
		final URL tmp = loader.getClassLoader().getResource(location);
		if (tmp==null){
			final File f = new File(location);
			if (!f.exists())
				throw new FileNotFoundException(location+" could not be found.");
			else
				return f;
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
		URL file = loader.getClassLoader().getResource(location);
		if (file==null){
			final File f=new File(location);
			if(!f.exists())
				throw new FileNotFoundException(location+" could not be found.");
			try{
				file=f.toURI().toURL();
			}catch(final MalformedURLException e){throw new FileNotFoundException(location+" could not be found.");}
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
		final InputStream file = loader.getClassLoader().getResourceAsStream(location);
		if (file==null){
			throw new FileNotFoundException(location+" could not be found.");
		}
		return file;
	}
	public static final ImageIcon createImageIcon(final String path, final String description,final Class<?> loader) throws FileNotFoundException {
		final java.net.URL imgURL = getResource(path,loader);
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
		try(Clip clip=AudioSystem.getClip()){
			try(AudioInputStream ais = AudioSystem.getAudioInputStream(file)){
				clip.open(ais);
			}
			clip.start();
		}catch(final Exception e){
			log.error(e);
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
		try(Clip clip=AudioSystem.getClip()){
			try(AudioInputStream ais = AudioSystem.getAudioInputStream(file)){
				clip.open(ais);
			}
			if(gain!=0){
				final FloatControl gainControl=(FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
				if(gain>gainControl.getMaximum())gain=gainControl.getMaximum();
				if(gain<gainControl.getMinimum())gain=gainControl.getMinimum();
				gainControl.setValue(gain);
			}
			clip.start();
		}catch(final Exception e){
			log.error(e);
			return false;
		}
		return true;
	}
	/**
	 * Tests to see if the on bits in <var>option</var> are also on in <var>options</var>.
	 * @param options Options the user set.
	 * @param option Option to be tested.
	 * @return The result of <code>(options&option)==option</code>
	 */
	public static boolean isSet(final int options, final int option) {
		return (options&option)==option;
	}
	/**
	 * Tests to see if the on bits in <var>option</var> are also on in <var>options</var>.
	 * @param options Options the user set.
	 * @param option Option to be tested.
	 * @return The result of <code>(options&option)==option</code>
	 */
	public static boolean isSet(final long options, final long option) {
		return (options&option)==option;
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
	};
	/**
	 * @param line
	 * @return The LineEnding or null
	 */
	public static final LineEnding getLineEndingType(String line) {
		int i = line.length()-1;
		for (;i>0;i--) {
			if (line.charAt(i)!='\r' && line.charAt(i)!='\n') break;
		}
		String ending = line.substring(i);
		for (LineEnding t : LineEnding.values()) {
			if (t.isEnd(ending)) return t;
		}
		return null;
	}
}
