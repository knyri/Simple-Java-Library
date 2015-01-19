/**
 *
 */
package simple.net.http;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import simple.util.do_str;
import simple.util.logging.Log;
import simple.util.logging.LogFactory;

/**
 * <hr>
 * <br>Created: Mar 14, 2011
 * @author Kenneth Pierce
 */
public class Cookie2 extends Cookie {
	private static final Log log = LogFactory.getLogFor(Cookie2.class);
	private String commenturl;
	private String port;
	public static final int F_DISCARD = 1, F_HTTPONLY = 4;
	public Cookie2() {}
	public static List<Cookie> parse(String cookie) {
		//max-age: in seconds
		//commentURL must be in quotes
		//port list must be in quotes, comma separated
		//cookies are comma-separated(note for cookie handler)
		int beg = 0, end = 0,semi=0,comma=0;
		String name,value;
		Cookie2 cTmp;
		if (cookie.startsWith("Set-Cookie2:")) {
			cookie = cookie.substring(11);
		}
		List<Cookie> cookies = new LinkedList<Cookie>();
		DateFormat df = DateFormat.getDateInstance();
		while (end!=-1 && end!=cookie.length()) {
			beg = end;
			beg = do_str.skipWhitespace(cookie, beg);
			if (beg==end) break;
			comma = cookie.indexOf(',',comma+1);
			if (comma== -1) comma = cookie.length();
			cTmp = new Cookie2();
			while (end < comma) {
				semi = cookie.indexOf(';',beg+1);
				if (semi>comma) semi = comma;
				else if (semi==-1) semi = cookie.length();//semi is always the absolute end

				end = cookie.indexOf('=');
				if (end==-1) {
					name = cookie.trim();
					value = null;
				} else {
					name = cookie.substring(beg, end).trim();
					beg = do_str.skipWhitespace(cookie, end+1);
					if (semi < comma) end = semi; else end = comma;
					if (name.equalsIgnoreCase("expires") && (beg-end)<6) {//space,quote,4 letter day
						comma = cookie.indexOf(',', end+1);
						if (comma==-1) comma = cookie.length();
						if (comma<semi) semi = comma;
						end = semi;
					}
					if (cookie.charAt(beg)=='"') {//adjust for quotes
						beg++;
						if (cookie.charAt(end)!='"') end--;//will either be ',' or ';' or '"'
					}
					value = cookie.substring(beg, end);
				}
				if (name.equalsIgnoreCase("expires")) {
					try {
						cTmp.setExpires(df.parse(value));
					} catch (ParseException e) {
						log.error(e);
						//e.printStackTrace();
					}
				} else if (name.equalsIgnoreCase("secure")) {
					cTmp.setFlag(Cookie.F_SECURE);
				} else if (name.equalsIgnoreCase("comment")) {
					cTmp.setComment(value);
				} else if (name.equalsIgnoreCase("domain")) {
					if (value.charAt(0)!='.') value = '.'+value;
					cTmp.setDomain(value);
				} else if (name.equalsIgnoreCase("path")) {
					cTmp.setPath(value);
				} else if (name.equalsIgnoreCase("version")) {
					cTmp.setVersion(value);
				} else if (name.equalsIgnoreCase("max-age")) {
					try {
						cTmp.setExpires(new Date(System.currentTimeMillis() + Integer.parseInt(value)*1000L));
					} catch (NumberFormatException e) {
						log.error(e);
					}
				} else {
					cTmp.setName(name);
					cTmp.setValue(value);
				}
			}//while end < comma
			cookies.add(cTmp);
			beg = end = semi+1;
		}//end while
		return cookies;
	}
	public void setCommentUrl(String commenturl) {
		this.commenturl = commenturl;
	}
	public String getCommentUrl() {
		return commenturl;
	}
	public void setPort(String portlist) {
		this.port = portlist;
	}
	public String getPort() {
		return port;
	}
	@Override
	public String toString() {
		return toResponseString();
	}
	@Override
	public String toRequestString() {
		StringBuilder ret = new StringBuilder("$Version="+getVersion()+";"+getName()+"=\""+getValue()+'"');
		if (getPath()!=null) { ret.append(";$Path=\""+getPath()+'"'); }
		if (getDomain()!=null) { ret.append(";$Domain="+getDomain()+'"'); }
		if (getPort()!=null) { ret.append("$Port=\""+getPort()+'"'); }
		return ret.toString();
	}
	@Override
	public String toResponseString() {
		StringBuilder ret = new StringBuilder(getName()+"=\""+getValue()+'"');
		if (getComment()!=null) { ret.append(";Comment=\""+getComment()+'"'); }
		if (getCommentUrl()!=null) { ret.append(";CommentURL=\""+getCommentUrl()+'"'); }
		if (isFlagSet(F_DISCARD)) { ret.append(";Discard"); }
		if (getDomain()!=null) { ret.append(";Domain=\""+getDomain()+'"'); }
		if (getExpires()!=null) { ret.append(";expires=\""+getExpires()+'"'); }
		if (getPath()!=null) { ret.append(";Path=\""+getPath()+'"'); }
		if (getPort()!=null) { ret.append(";Port=\""+getPort()+'"'); }
		if (isFlagSet(F_SECURE)) { ret.append(";Secure"); }
		if (getVersion()!=null) { ret.append(";Version=\""+getVersion()+'"'); }
		if (isFlagSet(F_HTTPONLY)) { ret.append(";httponly"); }
		return ret.toString();
	}
}
