/**
 *
 */
package simple.parser.ml;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;
import java.util.HashMap;

import simple.CIString;
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
		final StringBuilder buf = new StringBuilder(500);

		Parser(Reader in, ParserConstants pconst, boolean buildCache){
			bin= ReadWriterFactory.getBufferedReader(in);
			this.pconst= pconst;
			this.buildCache= buildCache;
		}
		boolean fillBuffer() throws IOException{
			if (buf.length()==0) {
				char c;
				int retc;
				if(cur == null){
					retc = RWUtil.skipWhitespace(bin);
					if (retc==-1){
						return false;
					}
					c= (char)retc;
					buf.append(c);
				}else{
					while(true){
						c= (char)(retc= bin.read());
						if(retc == -1){
							return false;
						}
						if(!do_str.isWhiteSpace(c)){
							cur.addChild(new Tag(Tag.CDATA,buf.toString(),true));
							buf.setLength(0);
							buf.append(c);
							break;
						}
						buf.append(c);
					}
				}
			}
			return true;
		}
		/**
		 * @return Last character in the buffer
		 */
		char lastChar(){
			return buf.charAt(buf.length() - 1);
		}
		char charAt(int idx){
			return buf.charAt(idx);
		}
		boolean endsWith(String str){
			return buf.indexOf(str, buf.length() - str.length()) != -1;
		}
		boolean startsWith(String str){
			return buf.lastIndexOf(str, 0) == 0;
		}
		void addCharData(String data){
//			log.debug("CDATA",data);
			if(cur != null){
				if(cur.getChild(cur.childCount()-1).getName().equals(Tag.CDATA)){
					// Current's last child is CDATA. Append this text to it
					cur.getChild(cur.childCount()-1).setContent(cur.getChild(cur.childCount()-1).getContent() + data);
				}else{
					cur.addChild(new Tag(Tag.CDATA, data, true));
				}
			}else{
				page.addTag(new Tag(Tag.CDATA, data, true), null);
			}
		}
		/**
		 * Meta and comments. Anything that not CDATA or a normal tag
		 * @param tag
		 */
		void addSpecialTag(Tag tag){
			if (cur == null) {
				// no current tag, add to page
				page.addTag(tag, null);
				if(buildCache){
					page.addTagToCache(tag);
					tag.addParentListener(page);
				}
			} else {
				cur.addChild(tag);
				if(buildCache){
					page.addTagToCache(tag);
					tag.addParentListener(page);
				}
			}
		}
		void addTag(Tag tag){
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
//log.debug("optional ender","ended: "+cur.getName()+" current: "+(cur.getParent()!=null?cur.getParent().getName():"[NONE]"));
					cur = cur.getParent();
				}
				if(cur == null){
					page.addTag(tag, null);
				}else{
					cur.addChild(tag);
//log.debug(cur.toString());
				}
				if(buildCache){
					page.addTagToCache(tag);
					tag.addParentListener(page);
				}
			}
		}
		final Page page = new Page();
		Page parse() throws IOException, ParseException {
			char c;
			String tmp;
			while(true){
				pos.reset();
//				log.debug("---- LOOP ----");
				if(!fillBuffer()){
					break;
				}
				c= charAt(0);
				if(c != '<'){
					// No tag, CDATA
					// Read until a tag
					buf.append(RWUtil.readUntil(bin, '<'));
					boolean append= false;
					if (lastChar()=='<') {
						append= true;
						buf.deleteCharAt(buf.length()-1);
					}
					addCharData(buf.toString());
					buf.setLength(0);
					if(append){
						buf.append('<');
					}
					continue;
				}
				buf.append(RWUtil.readUntilAny(bin, "><"));
//				log.debug(buf);
				/*
				 *  charAt(1) == '!' - Don't do it if it's a special tag!
				 */
				if(lastChar() == '<' && charAt(1) != '!'){
//log.debug("ending <",buf);
					// Unescaped <, treat as CDATA
					// <3 cause this bug to be found
					buf.deleteCharAt(buf.length() - 1);
					addCharData(buf.toString());
					buf.setLength(0);
					buf.append('<');
					continue;
				}
				// Check for << or < followed by whitespace
				if(charAt(1) == '<'){
					int index= 2, end= buf.length();
					while(index < end && charAt(index) == '<'){index++;}
					index--;
					addCharData(buf.substring(0, index));
					buf.delete(0, index);
					continue;
				}
				if(do_str.isWhiteSpace(charAt(1))){
//log.debug("<<",buf);
					//un-escaped <
					int index= 2, end= buf.length();
					while(index < end && charAt(index) != '<'){index++;}
					index--;
					addCharData(buf.substring(0,index));
					buf.delete(0, index);
					continue;
//					log.debug("After",buf);
				}
				/*
				 * Comment
				 */
				if (buf.length()>3 && startsWith("<!--")) {
					if (!endsWith("-->")){
						buf.append(RWUtil.readUntil(bin, "-->"));
					}

					// log.debug("HTML comment", buf);
					addSpecialTag(new Tag(Tag.HTMLCOMM, buf.toString(), true));
					buf.setLength(0);
					continue;
				}
				// rare case where < is the last character?
				if(buf.length() < 2){
					log.debug("When does this happen?!?",buf);
					break;
				}
				/*
				 * This thing
				 * <? ?>
				 */
				if(charAt(1)=='?'){
					if (!endsWith("?>")){
						buf.append(RWUtil.readUntil(bin,"?>"));
					}
					addSpecialTag(new Tag(Tag.META, buf.toString(), true));
					buf.setLength(0);
					continue;
				}
				/*
				 * DOCTYPE or CDATA
				 */
				if (charAt(1)=='!'){
					// <!DOCTYPE
					if (buf.length()>9){
						//TODO: Make this more generalized for <! tags
						final String ttmp= buf.substring(0,9).toUpperCase();
						if(ttmp.startsWith("<!CDATA")){//NOTE: SGML CDATA
							if (!buf.substring(buf.length()-3).equals("]]>")) {
								buf.append(RWUtil.readUntil(bin,"]]>"));
							}
							// log.debug("SGML CDATA");
							addSpecialTag(new Tag(Tag.SGMLCDATA, buf.toString(), true));
							buf.setLength(0);
							continue;
						}else if(ttmp.equals("<!DOCTYPE")){
							// Checking for <!DOCTYPE dmodule [
							int idx= 9;
							//skip whitespace
							while(idx < buf.length() && Character.isWhitespace(charAt(idx))) idx++;
							if(idx < buf.length() && Character.isAlphabetic(charAt(idx))){
								//skip name
								while(idx < buf.length() && Character.isAlphabetic(charAt(idx))) idx++;
								if(idx < buf.length() && ( Character.isWhitespace(charAt(idx)) || charAt(idx) == '[' )){
									//skip whitespace
									while(idx < buf.length() && Character.isWhitespace(charAt(idx))) idx++;
									if(idx < buf.length() && charAt(idx) == '[' && !endsWith("]>")){
										buf.append(RWUtil.readUntil(bin,"]>"));
									}
								}
							}
// log.debug("DOCTYPE", buf);
							addSpecialTag(new Tag(Tag.META,buf.toString(),true));
							buf.setLength(0);
							continue;
						}
					}
				}
				/*
				 * Found possible end tag </
				 */
				if (charAt(pos.start+1) == '/') {
					pos.end = buf.indexOf(">",pos.start);
					if (!pos.validEnd() || do_str.isWhiteSpace(charAt(pos.end)))
						throw new ParseException("Missing matching '>' for '<' at index "+pos.start+" of "+buf, pos.start);
					/*
					 * Name of the end tag
					 */
					final String name= buf.substring(pos.start+2, pos.end).trim();
					if (cur != null) {
						//end the tags whose end tags are optional
						while (!cur.getName().equals(name) && pconst.isOptionalEnder(cur.getName())) {
							// end tag does not match current and current's end tag is optional
// log.debug("optional ender","ended: '"+cur.getName()+"'");
							cur= cur.getParent();
						}
// log.debug("current: '"+cur.getName()+"'; name: '"+name+"'");
						if (cur.getName().equals(name)) {
							cur= cur.getParent();
// log.debug("ended: '"+name+"' current: '"+(cur!=null?cur.getName():"[NONE]")+"'");
						} else {
							tag= cur;
							boolean found = false;
							while (tag.hasParent()) {
								/*
								 * check the upper tags(cause: self-closing tag without special
								 * closure or empty body followed by closing tag)
								 */
								tag= tag.getParent();
								if (tag.getName().equals(name)) {
									found= true;
									do {
										log.warning("lazy self-closing tag found: '"+cur.getName()+"'");
										cur.setSelfClosing(true);//this will copy all children to parent
										cur= cur.getParent();
									} while (cur != tag);
									cur= cur.getParent();
									break;
								}
							}
							if (!found){
								log.warning("Missing opening tag for closing tag "
										+buf.subSequence(pos.start, pos.end+1)
										+" found at "+pos+". Current tag: "+cur.getName());
							}
						}
					} else {
						log.warning("Missing opening tag for closing tag "
								+buf.subSequence(pos.start, pos.end+1)
								+" found at "+pos+".(At document root)");
					}
					buf.setLength(0);
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
				while (charAt(pos.end) != '>') {
					switch(charAt(pos.end)){
					// NOTE: we are guaranteed not to be in a quotable section here
					case '"':
					case '\'':
						pos.end++;
						continue;
					}
					if(charAt(pos.end) == '='){
						// quotable section, read it as such.
	//skip whitespace
						pos.end++;
						while(do_str.isWhiteSpace(charAt(pos.end))){
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
						switch(charAt(pos.end)){
						case '"':
							pos.end++;
							while(charAt(pos.end) != '"'){
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
							while(charAt(pos.end)!='\''){
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
							while(!do_str.isWhiteSpace(charAt(pos.end)) && charAt(pos.end) != '>'){
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
						if(charAt(pos.end)=='>'){
							break;
						}
					}//if(charAt(pos.end)=='=')
					pos.end++;
					if (pos.end == buf.length()) {
						tmp = RWUtil.readUntil(bin, '>');
						if (tmp.isEmpty()) {
							throw new ParseException(pos+" Reached end of file while parsing. Cause: "+buf, pos.start);
						}
						buf.append(tmp);
					}
				}// end while (charAt(pos.end)!='>')
				}catch(final StringIndexOutOfBoundsException e){
					// log.debug("BUFFER DUMP",buf);
					int start=pos.end-100,end=pos.end+100;
					if(start<0)start=0;
					if(end>buf.length())end=buf.length();
					StringIndexOutOfBoundsException e2=new StringIndexOutOfBoundsException(pos.toString()+":"+buf.substring(start,end));
					e2.initCause(e);
					throw e2;
				}
				// parse the tag
				tag = createTag(buf, pos);
// log.debug("Start tag","'"+tag.getName()+"' | "+pos);

				// --- ADD CHILD TAG ---
				addTag(tag);
				// --- CHILD TAG ADDED ---



				if (pconst.isPcdataTag(tag.getName())) {
					//check for tags filled with gibberish to us
					try {
						buf.setLength(0);
						buf.append(RWUtil.readUntil(bin, "</"+tag.getName()+">", true));
					} catch (final EOFException e) {
						throw new ParseException("Missing end tag for PCDATA tag "+tag.getName()+" found at "+pos,pos.start);
					}
					try{
						Tag tmptag= new Tag(Tag.CDATA, buf.substring(0, Math.max(do_str.CI.lastIndexOf(buf,"</"+tag.getName()),0)).trim(),true);
						tag.addChild(tmptag);
						if(buildCache){
							page.addTagToCache(tmptag);
							tmptag.addParentListener(page);
						}
					}catch(final StringIndexOutOfBoundsException e){
						final StringIndexOutOfBoundsException e2= new StringIndexOutOfBoundsException(buf.toString());
						e2.initCause(e);
						throw e2;
					}
					// log.debug("PCDATA tag");
					buf.setLength(0);
					//add the data and continue(skips the adding of sub tags)
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

				buf.setLength(0);
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
	private static Tag createTag(final CharSequence src, final DoubleParsePosition limits) throws ParseException {
		final DoubleParsePosition pos = limits.clone();
//		log.debug("Create Tag:"+src.subSequence(limits.start, limits.end+1));
		if (src.charAt(pos.start+1)=='!' || src.charAt(pos.start+1)=='?')
			return new Tag(getCiStr("META"), src.subSequence(pos.start, pos.end+1).toString(), true);
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
//		log.debug(pos.toString() + "--" + limits.toString());
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
				if (src.charAt(pos.end)=='=') {//distinguish between name=value and name
					pos.start = pos.end = pos.end+1;
					if(do_str.isWhiteSpace(src.charAt(pos.end))){
						pos.start= pos.end= Math.min(do_str.skipWhitespace(src, pos.end + 1), limits.end);
					}
					if (src.charAt(pos.start)=='\'') {
						do {
							pos.end = do_str.indexOf(src, '\'', pos.start+1, limits.end);
						} while (pos.validEnd() && src.charAt(pos.end-1)=='\\');
						if (!pos.validEnd()) throw new ParseException(pos+" Missing matching single quote in:'"+src.subSequence(limits.start, limits.end+1)+"' for "+attrn, pos.start);
						pos.start++;
					} else if (src.charAt(pos.start)=='"') {
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
