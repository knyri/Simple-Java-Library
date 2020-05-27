/**
 *
 */
package simple.parser.ml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;
import java.util.HashMap;

import simple.CIString;
import simple.io.CharBuffer;
import simple.io.DoubleParsePosition;
import simple.io.RWUtil;
import simple.io.ReadWriterFactory;
import simple.util.do_str;
import simple.util.logging.Log;
import simple.util.logging.LogFactory;
import simple.util.logging.LogLevel;

/**Parses ML without recursion. Will allow tags without end tags that are not
 * self-closing. Some may be interpreted as self-closing and have the children
 * tags moved to the parent. To define a self closing tag add it to
 * {@link ParserConstants}.<br>
 * Other dependents:<br>
 * simple.io.DoubleParsePosition<br>
 * simple.util.do_str<br>
 * simple.util.logging.Log<br>
 * simple.util.logging.LogFactory<br>
 * <br>Created: Nov 4, 2010
 * @author Kenneth Pierce
 */
public class InlineLooseParser {
	protected static final Log log = LogFactory.getLogFor(InlineLooseParser.class);
//	static {
//		log.setPrintDebug(true);
//	}
	public static Page parse(final CharSequence src) throws ParseException, IOException {
		return parse(src, new ParserConstants(), true);
	}
	/** See {@link #parse(CharSequence, ParserConstants)} for important notes.
	 * @param src The text.
	 * @param buildCache wether or not to build the tag cache
	 * @return A {@link simple.parser.ml.Page Page} object that represents the source.
	 * @throws ParseException
	 * @throws IOException
	 */
	public static Page parse(final CharSequence src, boolean buildCache) throws ParseException, IOException {
		return parse(src, new ParserConstants(), buildCache);
	}
	public static Page parse(final CharSequence src, final ParserConstants pconst) throws ParseException, IOException {
		return parse(new StringReader(src.toString()),pconst, true);
	}
	/** Parses the text.
	 * NOTE: the plain text of a tag is added as a sub-tag with the name "CDATA".
	 * You can also use Tag.CDATA for the reference.
	 * This is done since the CDATA could be split by a sub-tag.(e.g. [p] text [span]text[/span] text [/p])
	 * @param src The text.
	 * @param pconst Options specific to this source's format.
	 * @param buildCache wether or not to build the tag cache
	 * @return A {@link simple.parser.ml.Page Page} object that represents the source.
	 * @see simple.parser.ml.ParserConstants
	 * @throws ParseException
	 * @throws IOException
	 */
	public static Page parse(final CharSequence src, final ParserConstants pconst, boolean buildCache) throws ParseException, IOException {
		return parse(new StringReader(src.toString()),pconst, buildCache);
	}
	/** See {@link #parse(Reader, ParserConstants)} for important notes.
	 * @param in The Reader.
	 * @return A {@link simple.parser.ml.Page Page} object that represents the source.
	 * @throws ParseException
	 * @throws IOException
	 */
	public static Page parse(final Reader in) throws ParseException, IOException {
		return parse(in, new ParserConstants(),true);
	}
	/** See {@link #parse(Reader, ParserConstants)} for important notes.
	 * @param in The Reader.
	 * @param buildCache wether or not to build the tag cache
	 * @return A {@link simple.parser.ml.Page Page} object that represents the source.
	 * @throws ParseException
	 * @throws IOException
	 */
	public static Page parse(final Reader in, boolean buildCache) throws ParseException, IOException {
		return parse(in, new ParserConstants(), buildCache);
	}
	public static Page parse(final Reader in, final ParserConstants pconst) throws IOException, ParseException {
		return parse(in, pconst, true);
	}
	private static HashMap<String, CIString> cisCache= new HashMap<String, CIString>();
	private static CIString getCiStr(String str){
		if(!cisCache.containsKey(str)){
			CIString ret= new CIString(str);
			cisCache.put(str, ret);
			return ret;
		}
		return cisCache.get(str);
	}
	private final static class Parser {
		final BufferedReader bin;
		final ParserConstants pconst;
		final boolean buildCache;

		final DoubleParsePosition pos = new DoubleParsePosition();
		Tag cur = null;//tag currently adding to. defaults to page if cur==null
		Tag tag = null;//tag currently creating
		final CharBuffer buf;

		Parser(Reader in, ParserConstants pconst, boolean buildCache){
			bin= ReadWriterFactory.getBufferedReader(in);
			buf= new CharBuffer(bin);
			this.pconst= pconst;
			this.buildCache= buildCache;
		}

		void addTag(Tag tag){
			log.debug("Add Tag", tag.toString());
			if (cur != null) {
				if(buildCache){
					page.addTagToCache(tag);
					tag.addParentListener(page);
				}
				cur.addChild(tag);
			} else {
				page.addTag(tag, null);
			}
		}
		Page page;
		Page parse() throws IOException, ParseException {
			page= new Page();
			String tmp;
			if(!buf.fillUntil('<')){
				return page;
			}
			while (true) {
				pos.reset();
				log.debug("---- LOOP ----", buf.toString());
				buf.markStart(buf.find('<'));
				if(buf.getMarkStart() == -1){
					log.debug("EOF at start of loop");
					addTag(new Tag(Tag.CDATA, buf.toString(), true));
					break;
				}
				if(buf.getMarkStart() > 0){
					log.debug("CDATA at start of loop");
					do{
						buf.moveTo(buf.getMarkStart());
						buf.markStart(buf.findAny("><", 1));
						if(buf.getMarkStart() == -1){
							log.debug("EOF at start of loop; looks like an unfinished tag");
							addTag(new Tag(Tag.CDATA, buf.toString(), true));
							buf.clear();
							break;
						}
					}while(buf.lastChar() == '<');
					addTag(new Tag(Tag.CDATA, buf.readToString(), true));
					buf.clearRead();
					continue;
				}else{
					buf.skip();
					if(buf.findAny("><") == -1){
						log.debug("EOF at start of loop; looks like an unfinished tag");
						addTag(new Tag(Tag.CDATA, buf.toString(), true));
						buf.clear();
						break;
					}
				}
				/*
				 *  buf.charAt(1) != '!' - Don't do it if it's a special tag!
				 */
//				if(buf.lastChar() == '<' && buf.current() != '!'){
//					// Unescaped <, treat as CDATA
//					// <3 cause this bug to be found
//					buf.fillUntil('<');
//					buf.moveTo(buf.length());
//					if (buf.lastChar() == '<') {
//						buf.rewind();
//					}
//					// log.debug("CDATA", buf);
//					addTag(new Tag(Tag.CDATA, buf.readToString(), true));
//					buf.clearRead();
//					continue;
//				}
				// Check for << or < followed by whitespace
				if(buf.current() == '<' || do_str.isWhiteSpace(buf.current())){
					log.debug("<<",buf);
					//un-escaped <
					while(buf.current() == '<' || do_str.isWhiteSpace(buf.current())){
						if(!buf.skip()){
							if(!buf.fillUntil('<')){
								log.debug("EOF");
								addTag(new Tag(Tag.CDATA, buf.toString(), true));
								break;
							}
						}
					}
					addTag(new Tag(Tag.CDATA, buf.readToString(), true));
					buf.clearRead();
					continue;
				}
				/*
				 * Comment
				 */
				if (buf.length() > 3 && buf.startsWith("<!--")) {
					int idx= buf.find("-->");
					if(idx == -1){
						// EOF while searching
						addTag(new Tag(Tag.CDATA, buf.toString(), true));
						buf.clear();
						break;
					}

					log.debug("HTML comment", buf);
					addTag(new Tag(Tag.HTMLCOMM, buf.toString(), true));
					buf.clear();
					continue;
				}
				// rare case where < is the last character?
				if(buf.length() < 2){
					// log.debug("When does this happen?!?",buf);
					break;
				}
				/*
				 * This thing
				 * <? ?>
				 */
				if(buf.current() == '?'){
					int idx= buf.find("?>");
					if(idx == -1){
						// EOF while searching
						addTag(new Tag(Tag.CDATA, buf.toString(), true));
						buf.clear();
						break;
					}
					buf.moveTo(idx + 2);

					addTag(new Tag(Tag.META, buf.readToString(), true));
					buf.clearRead();
					continue;
				}
				/*
				 * DOCTYPE or CDATA
				 */
				if (buf.next() == '!'){
					log.debug("Meta Tag");
					// <!DOCTYPE
					if (buf.length() > 9){
						int idx= buf.findAny("[>");
						if(idx == -1){
							// EOF while searching
							addTag(new Tag(Tag.CDATA, buf.toString(), true));
							buf.clear();
							break;
						}
						if(buf.charAt(idx) == '>'){
							buf.moveTo(idx);
							addTag(new Tag(Tag.META, buf.readToString(),true));
							buf.clearRead();
							continue;
						}
						buf.fillUntil("]>");
						addTag(new Tag(Tag.META,buf.toString(),true));
						buf.clear();
						continue;
					}
				}
				/*
				 * Found possible end tag </
				 */
				if (buf.current() == '/') {
					buf.markStart(2);
					buf.markEnd(buf.find('>'));
					if (buf.getMarkEnd() == -1){
						throw new ParseException("Missing matching '>' for '<' at index "+buf.getMarkStart()+" of "+buf, buf.getMarkStart());
					}
					// include '>' in the read section
					buf.moveTo(buf.getMarkEnd() + 1);
					/*
					 * Name of the end tag
					 */
					final String name = buf.getMarked().trim();
					if (cur == null) {
						log.warning("Missing opening tag for closing tag "+name+" found at "+pos+".(At document root)");
					}else{
						//end the tags whose end tags are optional
						while (!cur.getName().equals(name) && pconst.isOptionalEnder(cur.getName())) {
							// end tag does not match current and current's end tag is optional
							log.debug("optional ender","ended: '"+cur.getName()+"'");
							cur = cur.getParent();
						}
						log.debug("current: '"+cur.getName()+"'; name: '"+name+"'");
						if (cur.getName().equals(name)) {
							cur = cur.getParent();
							log.debug("ended: '"+name+"' current: '"+(cur != null ? cur.getName() : "[NONE]")+"'");
						} else {
							tag = cur;
							boolean found = false;
							while (tag.hasParent()) {
								/*
								 * check the upper tags(cause: self-closing tag without special
								 * closure or empty body followed by closing tag)
								 */
								tag = tag.getParent();
								if (tag.getName().equals(name)) {
									found = true;
									do {
										log.warning("lazy self-closing tag found: '"+cur.getName()+"'");
										cur.setSelfClosing(true);//this will copy all children to parent
										cur = cur.getParent();
									} while (cur != tag);
									cur = cur.getParent();
									break;
								}
							}
							if (!found){
								log.warning("Missing opening tag for closing tag "+name+" found at "+pos+". Current tag: "+cur.getName());
							}
						}
					}
					buf.clearRead();
					continue;
				}//end handle end tag
				try{
				/*
				 * search for ending >
				 * This is more complicated than it should be because I
				 * had a case where the alt attr of an img tag contained
				 * a > and it screwed the parser
				 * Fixes other wacky things like:
				 * <input name="name" value="ss"' ... >
				 * <td colspan=11'>
				 */
				while (buf.charAt(pos.end) != '>') {
					switch(buf.charAt(pos.end)){
					// NOTE: we are guaranteed not to be in a quotable section here
					case '"':
					case '\'':
						pos.end++;
						continue;
					}
					if(buf.charAt(pos.end) == '='){
						// quotable section, read it as such.
	//skip whitespace
						pos.end++;
						while(do_str.isWhiteSpace(buf.charAt(pos.end))){
							pos.end++;
							if (pos.end == buf.length()) {
								tmp = RWUtil.readUntil(bin, '>');
								if (tmp.isEmpty()) {
									throw new ParseException(pos+" Reached end of file while parsing. Cause: "+buf, pos.start);
								}
								buf.append(tmp);
							}
						}
						//read value
						switch(buf.charAt(pos.end)){
						case '"':
							pos.end++;
							while(buf.charAt(pos.end) != '"'){
								// Find matching quote
								pos.end++;
								if (pos.end == buf.length()) {
									// Buffer empty, fill it
									tmp = RWUtil.readUntil(bin, '>');
									if (tmp.isEmpty()) {
										throw new ParseException(pos+" Reached end of file while parsing. Cause: "+buf, pos.start);
									}
									buf.append(tmp);
								}
							}
							break;
						case '\'':
							pos.end++;
							while(buf.charAt(pos.end) != '\''){
								// Find matching quote
								pos.end++;
								if (pos.end == buf.length()) {
									// Buffer empty, fill it
									tmp = RWUtil.readUntil(bin, '>');
									if (tmp.isEmpty()) {
										throw new ParseException(pos+" Reached end of file while parsing. Cause: "+buf, pos.start);
									}
									buf.append(tmp);
								}
							}
							break;
						default:
							while(!do_str.isWhiteSpace(buf.charAt(pos.end)) && buf.charAt(pos.end) != '>'){
								pos.end++;
								if (pos.end == buf.length()) {
									tmp = RWUtil.readUntil(bin, '>');
									if (tmp.isEmpty()) {
										throw new ParseException(pos+" Reached end of file while parsing. Cause: "+buf, pos.start);
									}
									buf.append(tmp);
								}
							}
						}//switch
						if(buf.charAt(pos.end)=='>'){
							break;
						}
					}//if(buf.charAt(pos.end)=='=')
					pos.end++;
					if (pos.end == buf.length()) {
						tmp = RWUtil.readUntil(bin, '>');
						if (tmp.isEmpty()) {
							throw new ParseException(pos+" Reached end of file while parsing. Cause: "+buf, pos.start);
						}
						buf.append(tmp);
					}
				}// end while (buf.charAt(pos.end)!='>')
				}catch(final StringIndexOutOfBoundsException e){
					// log.debug("BUFFER DUMP",buf);
					int start= pos.end - 100,
							end= pos.end + 100;
					if(start < 0){
						start=0;
					}
					if(end > buf.length()){
						end=buf.length();
					}
					buf.markStart(start);
					buf.markEnd(end);
					StringIndexOutOfBoundsException e2=new StringIndexOutOfBoundsException(pos.toString()+":"+buf.getMarked());
					e2.initCause(e);
					throw e2;
				}
				// parse the tag
				tag = createTag(buf, pos);
				buf.moveTo(pos.end + 1);
				log.debug("Start tag","'"+tag.getName()+"' | "+pos);

				// --- ADD CHILD TAG ---
				if (cur == null) {

					// no current tag, add to page
					page.addTag(tag, null);
					if(buildCache){
						page.addTagToCache(tag);
						tag.addParentListener(page);
					}
				} else {
					if (pconst.isOptionalEnder(cur.getName()) && pconst.isOptionalEnderEnd(cur.getName(),tag.getName())) {
						// Current tag has an optional end and the new tag ends it
 log.debug("optional ender","ended: "+cur.getName()+" current: "+(cur.getParent()!=null?cur.getParent().getName():"[NONE]"));
						cur = cur.getParent();
					}
					if(cur == null){
						page.addTag(tag, null);
					}else{
						cur.addChild(tag);
					}
					if(buildCache){
						page.addTagToCache(tag);
						tag.addParentListener(page);
					}
				}
				// --- CHILD TAG ADDED ---



				if (pconst.isPcdataTag(tag.getName())) {
					//check for tags filled with gibberish to us
					buf.clearRead();
					buf.markStart();
					buf.markEnd(buf.find("</"+tag.getName()+">"));
					if(buf.getMarkEnd() == -1){
						throw new ParseException("Missing end tag for PCDATA tag "+tag.getName()+" found at "+pos,pos.start);
					}
					Tag tmptag= new Tag(Tag.CDATA, buf.getMarked(),true);
					tag.addChild(tmptag);
					if(buildCache){
						page.addTagToCache(tmptag);
						tmptag.addParentListener(page);
					}
					buf.clear();
					continue;
				}
				// Should we add child tags to this one?
				if (!tag.isSelfClosing()){
					if(pconst.isSelfCloser(tag.getName())) {
// log.debug("pre-defined self closer");
						tag.setSelfClosing(true);
					}else{
						// Set the current tag to this tag
						cur = tag;
					}
				}

				buf.clear();
				// log.debug("current: "+cur.getName());
			}
			// END PARSE LOOP
	//don't waste time in the loop if it won't be displayed
			if (cur != null && log.getPrint(LogLevel.WARNING)) {
				do {
					log.warning("Missing end tag for " + cur.getName()+"|"+pos);
					cur = cur.getParent();
				} while (cur != null);
			}

			return page;
		}
	}
	public static Page parse(final Reader in, final ParserConstants pconst, final boolean buildCache) throws IOException, ParseException {
		return new Parser(in, pconst, buildCache).parse();
	}
	/**Creates a Tag based on the raw data passed to it. Fills in attributes and the self-closing flag.
	 * TODO: inline this function
	 * @param src The complete source
	 * @param limits The object that represents the location of &lt; and &gt;
	 * @return The created tag.
	 * @throws ParseException when certain elements are missing
	 */
	private static Tag createTag(final CharBuffer src, final DoubleParsePosition limits) throws ParseException {
		final DoubleParsePosition pos = limits.clone();
		 log.debug("Create Tag:"+src.subSequence(limits.start, limits.end+1));
		if (src.charAt(pos.start+1)=='!' || src.charAt(pos.start+1)=='?'){
			return new Tag(getCiStr("META"), src.subSequence(pos.start, pos.end+1).toString(), true);
		}
		final Tag tag = new Tag(src.charAt(limits.end-1)=='/');
		pos.end = do_str.indexOf(src, ' ', pos.start, limits.end);
		if (!pos.validEnd()) {//no space so no attributes.
			if (tag.isSelfClosing()){
				tag.setName(getCiStr(src.subSequence(limits.start+1, limits.end-1).toString().trim()));
			}else{
				tag.setName(getCiStr(src.subSequence(limits.start+1, limits.end).toString().trim()));
			}

			return tag;
		}
		tag.setName(getCiStr(src.subSequence(limits.start+1, pos.end).toString().trim()));
		String attrn = "";
		while (true) {
			pos.start = do_str.skipWhitespace(src, pos.end + 1);
			pos.end = do_str.indexOfAny(src, "= ", pos.start, limits.end);
			if (!pos.validEnd())
				break;
			if(do_str.isWhiteSpace(src.charAt(pos.end))){
				pos.end= Math.min(do_str.skipWhitespace(src, pos.end + 1), limits.end);
			}

			attrn = src.subSequence(pos.start, pos.end).toString().trim();
			try {
				if (src.charAt(pos.end) == '=') {//distinguish between name=value and name
					pos.start = pos.end = pos.end+1;
					if(do_str.isWhiteSpace(src.charAt(pos.end))){
						pos.start= pos.end= Math.min(do_str.skipWhitespace(src, pos.end + 1), limits.end);
					}
					if (src.charAt(pos.start) == '\'') {
						pos.end = do_str.indexOf(src, '\'', pos.start+1, limits.end);
//						do {
//						} while (pos.validEnd() && src.charAt(pos.end-1)=='\\');
						if (!pos.validEnd()) throw new ParseException(pos+" Missing matching single quote in:'"+src.subSequence(limits.start, limits.end+1)+"' for "+attrn, pos.start);
						pos.start++;
					} else if (src.charAt(pos.start) == '"') {
						pos.end = do_str.indexOf(src, '"', pos.start+1, limits.end);
						/*do {
						} while (pos.validEnd() && src.charAt(pos.end-1)=='\\');*/
						if (!pos.validEnd()) throw new ParseException(pos+" Missing matching double quote in:'"+src.subSequence(limits.start, limits.end+1)+"' for "+attrn, pos.start);
						pos.start++;
					} else {
						pos.end = do_str.indexOfAny(src, " >", pos.start, limits.end);// space, or >
						if (!pos.validEnd()) throw new ParseException(pos+" Missing matching end of tag('>') in:'"+src.subSequence(limits.start, limits.end+1)+"' for "+attrn, pos.start);
						if(src.charAt(pos.end)=='>')
							if(src.charAt(pos.end-1)=='/'){
								pos.end--;
								if (!pos.validEnd()) throw new ParseException(pos+" Missing matching end of tag('>') in:'"+src.subSequence(limits.start, limits.end+1)+"' for "+attrn, pos.start);
							}
					}
					//// log.debug(pos+attrn+"="+src.subSequence(pos.start, pos.end));
					tag.setProperty(getCiStr(attrn), src.subSequence(pos.start, pos.end).toString());
				} else {//attribute with no value
					tag.setProperty(getCiStr(attrn), attrn);
					//// log.debug(pos+attrn+"="+src.subSequence(pos.start, pos.end));
				}
			} catch (final StringIndexOutOfBoundsException e) {
				log.error(pos.toString(),e);
			}
		}
		return tag;
	}
}
