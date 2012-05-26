package simple.util;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import simple.net.Uri;

/**
 * Depends on simple.util.HexUtil, simple.util.do_str
 * <br>Created: 2005
 * @author Kenneth Pierce
 */
public class UrlUtil {
	//#############################------------------#####################
	//#############################  URL STUFF       #####################
	//#############################------------------#####################
	private static Pattern escapeChars = Pattern.compile("[\" |\\[\\]]+");
	private static Pattern unescape = Pattern.compile("%[0-9A-F]{2}+");
	/**
	 * Escapes all non-word characters into %xx where xx is the hex of that
	 * character.
	 * @param e String to be escaped.
	 * @return The escaped version of this string.
	 */
	public static String URLescape(String e) {
		StringBuffer buf = new StringBuffer(e.length()*3);
		buf.append(e);
		Matcher m = escapeChars.matcher(e);
		int offset = 0;
		while (m.find()) {
			buf.replace(m.start()+offset,m.end()+offset,HexUtil.getHex("%",m.group()));
			offset+=m.group().length()*2;
		}
		return buf.toString();
	}
	/**
	 * Unescapes all sequences that match %xx where xx is hex.
	 * @param e String to be unescaped.
	 * @return The unescaped version of this string.
	 */
	public static String URLunescape(String e) {
		StringBuffer buf = new StringBuffer(e.length());
		buf.append(e);
		Matcher m = unescape.matcher(e);
		int offset = 0;
		while (m.find()) {
			buf.replace(m.start()+offset,m.end()+offset,HexUtil.toString("%",m.group()));
			offset-=m.group().length()-1;
		}
		return buf.toString();
	}
	//private static final String unsafeChars = "/\\\":?*|<>";
	public static String URLescape2(String e) {
		StringBuffer buf = new StringBuffer(e.length()*3);
		byte[] bbuf;
		buf.append(e);
		int offset = 0;
		char tmp;
		for (int i = 0; i < e.length(); i++) {
			tmp = e.charAt(i);
			if (tmp > 159 || (!Character.isLetterOrDigit(tmp) && (!Character.isDefined(tmp) || Character.isWhitespace(tmp) || Character.isISOControl(tmp)))) {
				try {
					bbuf = e.substring(i, i+1).getBytes("UTF-8");
					//System.out.println("char:"+tmp+" index:"+i+" blen:"+bbuf.length);
					//System.out.println("buf(i,i+1):"+buf.substring(i+offset, i+offset+1));
					buf.replace(i+offset, i+offset+1, HexUtil.getHex(bbuf, '%'));
					offset += bbuf.length*3-1;
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
			}
		}
		return buf.toString();
	}
	private static final Hashtable<String, Character> HtmlAscii = new Hashtable<String, Character>();
	static {
		HtmlAscii.put("nbsp", (char)160);
		HtmlAscii.put("iexcl", (char)161);
		HtmlAscii.put("cent", (char)162);
		HtmlAscii.put("pound", (char)163);
		HtmlAscii.put("curren", (char)164);
		HtmlAscii.put("yen", (char)065);
		HtmlAscii.put("brvbar", (char)166);
		HtmlAscii.put("sect", (char)167);
		HtmlAscii.put("uml", (char)168);
		HtmlAscii.put("copy", (char)169);
		HtmlAscii.put("ordf", (char)170);
		HtmlAscii.put("laquo", (char)171);
		HtmlAscii.put("not", (char)172);
		HtmlAscii.put("shy", (char)173);
		HtmlAscii.put("reg", (char)174);
		HtmlAscii.put("macr", (char)175);
		HtmlAscii.put("deg", (char)176);
		HtmlAscii.put("plusmn", (char)177);
		HtmlAscii.put("sup2", (char)178);
		HtmlAscii.put("sup3", (char)179);
		HtmlAscii.put("acute", (char)180);
		HtmlAscii.put("micro", (char)181);
		HtmlAscii.put("para", (char)182);
		HtmlAscii.put("middot", (char)183);
		HtmlAscii.put("cedil", (char)184);
		HtmlAscii.put("sup1", (char)185);
		HtmlAscii.put("ordm", (char)186);
		HtmlAscii.put("raquo", (char)187);
		HtmlAscii.put("frac14", (char)188);
		HtmlAscii.put("frac12", (char)189);
		HtmlAscii.put("frac34", (char)190);
		HtmlAscii.put("iquest", (char)191);
		HtmlAscii.put("Agrave", (char)192);
		HtmlAscii.put("Aacute", (char)193);
		HtmlAscii.put("Acirc", (char)194);
		HtmlAscii.put("Atilde", (char)195);
		HtmlAscii.put("Auml", (char)196);
		HtmlAscii.put("Aring", (char)197);
		HtmlAscii.put("AElig", (char)198);
		HtmlAscii.put("Ccedil", (char)199);
		HtmlAscii.put("Egrave", (char)200);
		HtmlAscii.put("Eacute", (char)201);
		HtmlAscii.put("Ecirc", (char)202);
		HtmlAscii.put("Euml", (char)203);
		HtmlAscii.put("Igrave", (char)204);
		HtmlAscii.put("Iacute", (char)205);
		HtmlAscii.put("Icirc", (char)206);
		HtmlAscii.put("Iuml", (char)207);
		HtmlAscii.put("ETH", (char)208);
		HtmlAscii.put("Ntilde", (char)209);
		HtmlAscii.put("Ograve", (char)210);
		HtmlAscii.put("Oacute", (char)211);
		HtmlAscii.put("Ocirc", (char)212);
		HtmlAscii.put("Otilde", (char)213);
		HtmlAscii.put("Ouml", (char)214);
		HtmlAscii.put("times", (char)215);
		HtmlAscii.put("Oslash", (char)216);
		HtmlAscii.put("Ugrave", (char)217);
		HtmlAscii.put("Uacute", (char)218);
		HtmlAscii.put("Ucirc", (char)219);
		HtmlAscii.put("Uuml", (char)220);
		HtmlAscii.put("Yacute", (char)221);
		HtmlAscii.put("THORN", (char)222);
		HtmlAscii.put("szlig", (char)223);
		HtmlAscii.put("agrave", (char)224);
		HtmlAscii.put("aacute", (char)225);
		HtmlAscii.put("acirc", (char)226);
		HtmlAscii.put("atilde", (char)227);
		HtmlAscii.put("auml", (char)228);
		HtmlAscii.put("aring", (char)229);
		HtmlAscii.put("aelig", (char)230);
		HtmlAscii.put("ccedil", (char)231);
		HtmlAscii.put("egrave", (char)232);
		HtmlAscii.put("eacute", (char)233);
		HtmlAscii.put("ecirc", (char)234);
		HtmlAscii.put("euml", (char)235);
		HtmlAscii.put("igrave", (char)236);
		HtmlAscii.put("iacute", (char)237);
		HtmlAscii.put("icirc", (char)238);
		HtmlAscii.put("iuml", (char)239);
		HtmlAscii.put("eth", (char)240);
		HtmlAscii.put("ntilde", (char)241);
		HtmlAscii.put("ograve", (char)242);
		HtmlAscii.put("oacute", (char)243);
		HtmlAscii.put("ocirc", (char)244);
		HtmlAscii.put("otilde", (char)245);
		HtmlAscii.put("ouml", (char)246);
		HtmlAscii.put("divide", (char)247);
		HtmlAscii.put("oslash", (char)248);
		HtmlAscii.put("ugrave", (char)249);
		HtmlAscii.put("uacute", (char)250);
		HtmlAscii.put("ucirc", (char)251);
		HtmlAscii.put("uuml", (char)252);
		HtmlAscii.put("yacute", (char)253);
		HtmlAscii.put("thorn", (char)254);
		HtmlAscii.put("yuml", (char)255);
		HtmlAscii.put("amp", '&');
		HtmlAscii.put("quot", '"');
		HtmlAscii.put("gt", '>');
		HtmlAscii.put("lt", '<');
	}
	/**
	 * @param ascii Can either be in raw form (&#000; &yadda;) or parsed.
	 * @return The character that this code represents.
	 */
	public static char htmlAsciiToChar(String ascii) {
		if (ascii.charAt(0)=='&') {
			if (ascii.charAt(1)=='#') {
				if (ascii.charAt(ascii.length()-1)==';') {
					return (char)Integer.parseInt(ascii.substring(2, ascii.length()-1));
				} else {
					return (char)Integer.parseInt(ascii.substring(2));
				}
			} else {
				if (ascii.charAt(ascii.length()-1)==';') {
					return HtmlAscii.get(ascii.substring(1, ascii.length()-1));
				} else {
					return HtmlAscii.get(ascii.substring(1));
				}
			}
		} else if (ascii.charAt(0)=='#') {
			if (ascii.charAt(ascii.length()-1)==';') {
				return (char)Integer.parseInt(ascii.substring(1, ascii.length()-1));
			} else {
				return (char)Integer.parseInt(ascii.substring(1));
			}
		} else {
			if (ascii.charAt(ascii.length()-1)==';') {
				if (Character.isDigit(ascii.charAt(0)))
					return (char)Integer.parseInt(ascii.substring(0, ascii.length()-1));
				else
					return HtmlAscii.get(ascii.substring(0, ascii.length()-1));
			} else {
				if (Character.isDigit(ascii.charAt(0)))
					return (char)Integer.parseInt(ascii);
				else
					return HtmlAscii.get(ascii);
			}
		}
	}
	/** Finds the url hidden in the tag. Matches <tag src=... > <tag src="..." > <tag src='...' >
	 * @param s
	 * @param offset
	 * @return A String containing the source attribute or null if none was found.
	 */
	public static final String extractURL(CharSequence s, int offset) {
		offset -= 20;
		if (offset < 0) {	offset = 0;	}
		// loop until src is found
		while (offset < (s.length()-3)
				&& (s.charAt(offset) != 's' || s.charAt(offset) != 'S')
				&& (s.charAt(offset+1) != 'r' || s.charAt(offset+1) != 'R')
				&& (s.charAt(offset+2) != 'c' || s.charAt(offset+2) != 'C'))
			{	offset++;	}
		if (offset == s.length()) {	return null;	} //nothing found
		StringBuilder url = new StringBuilder(30);
		offset = do_str.skipWhitespace(s, offset);
		switch (s.charAt(offset)) {
		case '"':
			while (s.charAt(offset) != '"') {	url.append(s.charAt(offset++));	}
			break;
		case '\'':
			while (s.charAt(offset) != '\'') {	url.append(s.charAt(offset++));	}
			break;
		default:
			while (s.charAt(offset) != ' ') {	url.append(s.charAt(offset++));	}
		}
		
		return url.toString();
	}
	/** Creates a URL from the provided information.
	 * @param link href from the source
	 * @param page page on which the href was found.
	 * @param basehref base href(can be null)
	 * @return A complete URL.
	 */
	public static final String createURL(Uri link, Uri page, String basehref) {
		String ret = null;
		if (link.getScheme().isEmpty()) {
			if (link.getHost().isEmpty()) {
				if (link.getPath().isEmpty()) {
					ret = page.getScheme() + "://" + page.getHost() + link;
				} else {
					if (basehref != null) {
						ret = basehref + link;
					} else {
						ret = page.getScheme()+ "://" + page.getHost() + page.getPath() + link;
					}
				}
			} else {
				ret = page.getScheme() + "://" + link;
			}
		}
		return ret;
	}
}
