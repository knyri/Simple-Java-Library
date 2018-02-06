/**
 *
 */
package simple.parser.ml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.util.HashSet;

import simple.CIString;
import simple.collections.MVHashtable;

/**
 * <br>Created: Oct 23, 2010
 * @author Kenneth Pierce
 */
public final class ParserConstants {
//	private static final Log log = LogFactory.getLogFor(ParserConstants.class);
	private final HashSet<CIString> PCDATA = new HashSet<CIString>();
	private final HashSet<CIString> SELFCLOSER = new HashSet<CIString>();
	private final HashSet<CIString> OPTIONALEND = new HashSet<CIString>();
	private final MVHashtable<CIString, CIString> OPTIONALENDEND = new MVHashtable<CIString, CIString>();
	private static final CIString TAGA = new CIString("tag");
	private static final ParserConstants PCONST = new ParserConstants();
	static {
		PCONST.addSelfCloser("pcdata");
		PCONST.addSelfCloser("selfcloser");
		PCONST.addSelfCloser("end");
	}
	/** Loads the configuration file.
	 * @param file file to get the config from
	 * @throws IOException
	 * @throws ParseException
	 */
	public void load(final File file) throws IOException, ParseException {
		try(Reader r= new FileReader(file)){
			load(r);
		}
	}
	/**Loads the configuration file.
	 * @param in stream to get the config from
	 * @throws IOException
	 * @throws ParseException
	 */
	public void load(final InputStream in) throws IOException, ParseException {
		try(Reader r= new InputStreamReader(in)){
			load(r);
		}
	}
	/**Loads the configuration file.
	 * @param in reader to get the config from
	 * @throws IOException
	 * @throws ParseException
	 */
	public void load(final Reader in) throws IOException, ParseException {
		final char[] buf = new char[512];
		final StringBuilder src = new StringBuilder(500);
		int read = 0;
		for (;read!=-1;read=in.read(buf)) {
			src.append(buf, 0, read);
		}
		in.close();
		final Page page = InlineLooseParser.parse(src,PCONST, false);
		String name;
		for(final Tag tag : page) {
			if (tag.getName().equals("pcdata")) {
				addPcdataTag(tag.getProperty(TAGA));
			} else if (tag.getName().equals("selfcloser")) {
				addSelfCloser(tag.getProperty(TAGA));
			} else if (tag.getName().equals("optionalend")) {
				name = tag.getProperty(TAGA);
				addOptionalEnder(name);
				for (int i = 0; i < tag.childCount(); i++) {
					addOptionalEnderEnd(name, tag.getChild(i).getProperty(TAGA));
				}
			}
		}
	}
	/**Adds this tag as a tag that may or may not have a closing tag.
	 * @param tag the tag name
	 */
	public void addOptionalEnder(final String tag) {
		addOptionalEnder(new CIString(tag));
	}
	/**Adds this tag as a tag that may or may not have a closing tag.
	 * @param tag the tag name
	 */
	public void addOptionalEnder(final CIString tag) {
//		log.debug("Add Optional Ender",tag);
		OPTIONALEND.add(tag.intern());
	}
	/**
	 * Convenience method for addOptionalEnder(otag); addOptionalEnderEnd(otag, etags);
	 * Adds this tag as a tag that will close the optional end tag when it opens.
	 * For example an opening "li" tag will close the previous "li" tag.
	 * @param otag The optional tag name
	 * @param etag The tag name that ends this tag
	 */
	public void addOptionalEnder(CIString otag, final CIString... etags) {
//		log.debug("Add Option ender end",etag+" ends "+otag);
		otag= otag.intern();
		OPTIONALEND.add(otag);
		for(CIString etag: etags){
			OPTIONALENDEND.add(otag, etag.intern());
		}
	}
	/**Adds this tag as a tag that will close the optional end tag when it opens.
	 * For example an opening "li" tag will close the previous "li" tag.
	 * @param otag The optional tag name
	 * @param etag The tag name that ends this tag
	 */
	public void addOptionalEnderEnd(CIString otag, final CIString... etags) {
//		log.debug("Add Option ender end",etag+" ends "+otag);
		otag= otag.intern();
		for(CIString etag: etags){
			OPTIONALENDEND.add(otag, etag.intern());
		}
	}
	/**Adds this tag as a tag that will close the optional end tag when it opens.
	 * For example an opening li tag will close the previous li tag.
	 * @param otag The optional tag name
	 * @param etag The tag name that ends this tag
	 */
	public void addOptionalEnderEnd(final String otag, final String etag) {
		addOptionalEnderEnd(new CIString(otag), new CIString(etag));
	}
	/**Defines this tag as never having content or sub-tags. Tags like "br" and "hr" belong here.
	 * @param tag The tag name
	 */
	public void addSelfCloser(final CIString tag) {
//		log.debug("Add self closer",tag);
		SELFCLOSER.add(tag.intern());
	}
	/**Defines this tag as never having content or sub-tags. Tags like "br" and "hr" belong here.
	 * @param tag The tag name
	 */
	public void addSelfCloser(final String tag) {
		addSelfCloser(new CIString(tag));
	}

	/**Sets this tag as only containing PCDATA. Tags like "style" and "script" belong here.
	 * @param tag name of the tag
	 */
	public void addPcdataTag(final String tag) {
		addPcdataTag(new CIString(tag));
	}
	/**Sets this tag as only containing PCDATA. Tags like "style" and "script" belong here.
	 * @param tag name of the tag
	 */
	public void addPcdataTag(final CIString tag) {
//		log.debug("Add PCDATA tag",tag);
		PCDATA.add(tag.intern());
	}
	/**Checks to see if the end tag is optional for this tag.
	 * @param tag the tag name
	 * @return true if the end tag is optional for this tag.
	 */
	public boolean isOptionalEnder(final String tag) {
		return isOptionalEnder(new CIString(tag));
	}
	/**Checks to see if the end tag is optional for this tag.
	 * @param tag the tag name
	 * @return true if the end tag is optional for this tag.
	 */
	public boolean isOptionalEnder(final CIString tag) {
		return OPTIONALEND.contains(tag);
	}
	/**Checks to see if etag serves as an ending tag for otag.
	 * @param otag The optional tag name
	 * @param etag The ending tag name
	 * @return True if the opening tag for etag serves as a closing tag for otag.
	 */
	public boolean isOptionalEnderEnd(final CIString otag, final CIString etag) {
		return OPTIONALENDEND.contains(otag, etag);
	}
	/**Checks to see if the tag contains PCDATA.
	 * @param tag the tag name
	 * @return true if the contents of tag are to be treated as PCDATA
	 */
	public boolean isPcdataTag(final String tag) {
		return isPcdataTag(new CIString(tag));
	}
	/**Checks to see if the tag contains PCDATA.
	 * @param tag the tag name
	 * @return true if the contents of tag are to be treated as PCDATA
	 */
	public boolean isPcdataTag(final CIString tag) {
		return PCDATA.contains(tag);
	}
	/**Checks to see if tag is a self-closer.
	 * @param tag the tag name
	 * @return true if the tag closes itself.
	 */
	public boolean isSelfCloser(final String tag) {
		return isSelfCloser(new CIString(tag));
	}
	/**Checks to see if tag is a self-closer.
	 * @param tag the tag name
	 * @return true if the tag closes itself.
	 */
	public boolean isSelfCloser(final CIString tag) {
		return SELFCLOSER.contains(tag);
	}
}
