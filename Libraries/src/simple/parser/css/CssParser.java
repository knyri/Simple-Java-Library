package simple.parser.css;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import simple.CIString;
import simple.io.DoubleParsePosition;
import simple.io.RWUtil;
import simple.io.ReadWriterFactory;
import simple.util.do_str;
import simple.util.logging.Log;
import simple.util.logging.LogFactory;

/**
 * Not yet coded. Right now it is a clone of InlineLooseParser.
 * Created: 2013-01-25
 * @author Ken
 *
 */
public class CssParser{
	protected static final Log log = LogFactory.getLogFor(CssParser.class);
	/** Parses the text.
	 * NOTE: the plain text of a tag is added as a sub-tag with the name "CDATA".
	 * You can also use Tag.CDATA for the reference.
	 * This is done since the CDATA could be split by a sub-tag.(e.g. [p] text [span]text[/span] text [/p])
	 * @param src The text.
	 * @return A {@link simple.parser.ml.Page Page} object that represents the source.
	 * @see simple.parser.ml.ParserConstants
	 * @throws ParseException
	 * @throws IOException
	 */
	public static CssDocument parse(final CharSequence src) throws ParseException, IOException {
		return parse(new StringReader(src.toString()));
	}
	/**
	 * @param in The Reader.
	 * @return A CssDocument object that represents the source.
	 * @throws ParseException
	 * @throws IOException
	 */
	public static CssDocument parse(final Reader in) throws ParseException, IOException {
		try(BufferedReader bin = ReadWriterFactory.getBufferedReader(in)){
			final CssDocument page = new CssDocument();
			final DoubleParsePosition pos = new DoubleParsePosition();
			final StringBuilder buf = new StringBuilder(500);
			char c;
			int retc;
			List<String> rules;
			HashMap<CIString, String> props= new HashMap<CIString, String>();
			CIString tmp;
			while (true) {
				pos.reset();
//log.debug("---- LOOP ----");
				if (buf.length()==0) {
					// Buffer is empty. And should be
					retc = RWUtil.skipWhitespace(bin);
					c= (char)retc;
					if (retc==-1) {
						// end of file
						break;
					}
					buf.append(c);
				} else {
					c = buf.charAt(0);
				}
				if (c == '{'){
					// Something odd
					buf.append(RWUtil.readUntilQuoted(bin, '}'));
					// TODO handle this case
					buf.setLength(0);
					continue;
				}
				// Read until next section
				buf.append(RWUtil.readUntilQuoted(bin, '{'));
				if (buf.charAt(buf.length()-1)=='{') {
					buf.deleteCharAt(buf.length()-1);
				}
				log.debug("buf",buf);
				rules= getRules(buf);
				buf.append(RWUtil.readUntil(bin, '}'));

			}
			log.debug(page);
			return page;
		}
	}
	private static final List<String> getRules(StringBuilder raw) throws IOException{
		List<String> ret= new LinkedList<String>();
		DoubleParsePosition pos= new DoubleParsePosition();
		pos.end= do_str.indexOfQuoted(raw,',',0);
		while(pos.validEnd()){
			ret.add(raw.substring(pos.start,pos.end));
			pos.start= pos.end + 1;
			pos.end= do_str.indexOfQuoted(raw,',',pos.start);
		}
		return ret;
	}

}
