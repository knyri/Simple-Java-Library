package simple.HTML;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Hashtable;
import java.util.Vector;

import simple.CIString;
import simple.io.ReadWriterFactory;
import simple.ml.InlineLooseParser;
import simple.ml.Page;
import simple.ml.Tag;
import simple.net.Uri;
import simple.util.logging.Log;
import simple.util.logging.LogFactory;

/**
 * This class reads a simple XML file containing MIME Type information and
 * provides this information statically.<br>
 * File read: mimeTypes.conf.xml in the base package.<br>
 * depends on {@link simple.CIString},
{@link simple.ml.InlineLooseParser},
{@link simple.ml.Page},
{@link simple.ml.Tag},
{@link simple.net.Uri},
{@link simple.io.ReadWriterFactory},
{@link simple.util.logging.Log},
{@link simple.util.logging.LogFactory}
 * <br>Created: Jul 25, 2006
 * @author Kenneth Pierce
 */
public final class MimeTypes {
	private static final Vector<String> EMPTYVECTOR = new Vector<String>(1,0);
	private static final Hashtable<String, Vector<String>> video = new Hashtable<String, Vector<String>>();
	private static final Hashtable<String, Vector<String>> audio = new Hashtable<String, Vector<String>>();
	private static final Hashtable<String, Vector<String>> image = new Hashtable<String, Vector<String>>();
	private static final Hashtable<String, Vector<String>> other = new Hashtable<String, Vector<String>>();
	private static final Hashtable<String, Vector<String>> archive = new Hashtable<String, Vector<String>>();
	private static final Hashtable<String, Vector<String>> document = new Hashtable<String, Vector<String>>();
	private static final Hashtable<String, Vector<String>> extTomime = new Hashtable<String, Vector<String>>();
	private static final CIString mime = new CIString("mime"), ext = new CIString("ext");
	private static boolean loaded = false;
	private static final Log log = LogFactory.getLogFor(MimeTypes.class);
	static {
		System.out.println("Loading MimeTypes...");
		if (!loaded) {
			loaded=true;
			EMPTYVECTOR.add("");
			loaded = loadTypes();
			log.information("loaded mime types");
		}
		System.out.println("MimeTypes Loaded");
	}
	public MimeTypes() {
		if (!loaded)
			loaded = loadTypes();
	}
	public static boolean isLoaded() {
		return loaded;
	}
	public static String getMimeType(final Uri e) throws IOException {
		final Vector<String> list = getMime(e);
		if (list.firstElement().isEmpty()) {
			log.debug("Server connection for MIME", e);
			final HttpURLConnection con = (HttpURLConnection)e.openConnection();
			String type;
			con.setRequestMethod("HEAD");
			con.connect();
			type = con.getContentType();
			con.disconnect();
			return type;
		}
		return list.firstElement();
	}
	public static String getMimeType(final Uri e, final String referer) throws IOException {
		final Vector<String> list = getMime(e);
		if (list.firstElement().isEmpty()) {
			log.debug("Server connection for MIME", e);
			final HttpURLConnection con = (HttpURLConnection)e.openConnection();
			String type;
			con.setRequestProperty("Referer", referer);
			con.setRequestMethod("HEAD");
			con.connect();
			type = con.getContentType();
			con.disconnect();
			return type;
		}
		return list.firstElement();
	}
	protected static Vector<String> toVector(final String[] list) {
		final Vector<String> tmp = new Vector<String>();
		for (final String item : list) {
			tmp.add(item);
		}
		return tmp;
	}
	protected static boolean loadTypes() {
		try {
			EMPTYVECTOR.add("");
			final Page page = InlineLooseParser.parse(
					ReadWriterFactory.readFully(new InputStreamReader(
							MimeTypes.class.getClassLoader().getResource("mimeTypes.conf.xml").openStream())));
			log.debug(page);
			add(page.getTag("mime.image;0"), image);
			add(page.getTag("mime.video;0"), video);
			add(page.getTag("mime.audio;0"), audio);
			add(page.getTag("mime.other;0"), other);
			add(page.getTag("mime.archive;0"), archive);
			add(page.getTag("mime.document;0"), document);
			//mime.print();
			//System.out.println(MimeTypes.image.toString());
			//System.out.println(MimeTypes.extTomime.toString());
		} catch (final Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	protected static void add(final Tag tags, final Hashtable<String, Vector<String>> list) {
		String mimetmp;
		String[] exttmp;
		Vector<String> vectmp;
		for (final Tag tag : tags) {
			mimetmp = tag.getProperty(mime).toLowerCase().intern();
			exttmp = tag.getProperty(ext).toLowerCase().split(" ");
			for (byte j = 0; j < exttmp.length; j++) {
				exttmp[j] = exttmp[j].intern();
				if (extTomime.containsKey(exttmp[j])) {
					vectmp = extTomime.get(exttmp[j]);
					vectmp.add(mimetmp);
				} else {
					vectmp = new Vector<String>();
					vectmp.add(mimetmp);
					extTomime.put(exttmp[j], vectmp);
				}
				//log.log(mimetmp+": "+exttmp[j]);
			}
			list.put(mimetmp, toVector(exttmp));
		}
	}
	public static boolean isMimeVideo(String mime) {
		if (mime==null) return false;
		mime = mime.toLowerCase();
		if (mime.startsWith("video"))
			return true;
		else
			return video.containsKey(mime);
	}
	public static boolean isMimeAudio(String mime) {
		if (mime==null) return false;
		mime = mime.toLowerCase();
		if (mime.startsWith("audio"))
			return true;
		else
			return audio.containsKey(mime);
	}
	public static boolean isMimeImage(String mime) {
		if (mime==null) return false;
		mime = mime.toLowerCase();
		if (mime.startsWith("image"))
			return true;
		else
			return image.containsKey(mime);
	}
	public static boolean isMimeOther(String mime) {
		if (mime==null) return false;
		mime = mime.toLowerCase();
		if (mime.startsWith("application") || mime.startsWith("text") ||
				mime.startsWith("drawing") || mime.startsWith("model"))
			return true;
		else
			return other.containsKey(mime);
	}
	public static boolean isMimeDocument(String mime) {
		if (mime==null) return false;
		mime = mime.toLowerCase();
		return document.containsKey(mime);
	}
	public static boolean isMimeArchive(String mime) {
		if (mime==null) return false;
		mime = mime.toLowerCase();
		return archive.containsKey(mime);
	}
	@SuppressWarnings("unchecked")
	public static Vector<String> getExt(final String mime) {
		Vector<String> ext = null;
		ext = image.get(mime);
		if (ext!=null)
			return ext;
		ext = video.get(mime);
		if (ext!=null)
			return ext;
		ext = audio.get(mime);
		if (ext!=null)
			return ext;
		ext = other.get(mime);
		if (ext!=null)
			return ext;
		ext = document.get(mime);
		if (ext!=null)
			return ext;
		ext = archive.get(mime);
		if (ext!=null)
			return ext;
		return (Vector<String>) EMPTYVECTOR.clone();
	}
	/**Get's the MIME type from the raw header value. Some strange servers
	 * include extra after the mime.
	 * @param rawContent
	 * @return The MIME type.
	 */
	public static String getMimeFromContentType(final String rawContent) {
		final String[] tmp = rawContent.split(";");
		for (int i = 0; i < tmp.length; i++) {
			tmp[i] = tmp[i].trim().toLowerCase();
			if (tmp[i].startsWith("text") || tmp[i].startsWith("image") || tmp[i].startsWith("audio") ||
					tmp[i].startsWith("video") || tmp[i].startsWith("application") || tmp[i].startsWith("drawing") ||
					tmp[i].startsWith("model")
				) return tmp[i];
		}
		return null;
	}
	/**Checks the URL to see what it's MIME is. Does not connect to the server.
	 * @param url The URL to check.
	 * @return A Vector of MIME types. The Vector may be empty.
	 */
	@SuppressWarnings("unchecked")
	public static Vector<String> getMime(final Uri url) {
		String ext = url.getFile();
		ext = ext.substring(ext.lastIndexOf(".")+1);
		final Vector<String> tmp = extTomime.get(ext.toLowerCase());
		if (tmp == null)
			return (Vector<String>) EMPTYVECTOR.clone();
		return tmp;
	}
	/** Returns a vector of matching MIME types.
	 * @param ext The extension or file name.
	 * @return A vector of matching MIME types or an empty vector if none were found.
	 */
	@SuppressWarnings("unchecked")
	public static Vector<String> getMime(String ext) {
		final int ind = ext.indexOf('.');
		if (ind > -1) {
			ext = ext.substring(ind+1);
		}
		final Vector<String> tmp = extTomime.get(ext.toLowerCase());
		if (tmp == null)
			return (Vector<String>) EMPTYVECTOR.clone();
		return tmp;
	}
	public static void main(final String[] args) {
		new MimeTypes();
	}
	private static String dumpHashtable(final Hashtable<String, Vector<String>> table) {
		final StringBuilder buf = new StringBuilder(500);
		for (final String ext : table.keySet()) {
			buf.append(ext);
			buf.append('\t');
			buf.append('[');
			for (final String mime : table.get(ext)) {
				buf.append(mime);
				buf.append(';');
			}
			buf.replace(buf.length()-1, buf.length(), "]\n");
		}
		return buf.toString();
	}
	public static String dumpMIMES() {
		return dumpHashtable(MimeTypes.extTomime);
	}
	public static String dumpArchiveMimes() {
		return dumpHashtable(MimeTypes.archive);
	}
	public static String dumpAudioMimes() {
		return dumpHashtable(MimeTypes.audio);
	}
	public static String dumpDocumentMimes() {
		return dumpHashtable(MimeTypes.document);
	}
	public static String dumpImageMimes() {
		return dumpHashtable(MimeTypes.image);
	}
	public static String dumpOtherMimes() {
		return dumpHashtable(MimeTypes.other);
	}
	public static String dumpVideoMimes() {
		return dumpHashtable(MimeTypes.video);
	}
}
