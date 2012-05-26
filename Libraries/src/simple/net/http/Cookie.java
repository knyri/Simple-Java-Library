/**
 *
 */
package simple.net.http;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Vector;

import simple.CIString;
import simple.util.App;
import simple.util.do_str;
import simple.util.logging.Log;
import simple.util.logging.LogFactory;

/**
 * <hr>
 * <br>Created: Mar 14, 2011
 * @author Kenneth Pierce
 */
public class Cookie {
	private static final Log log = LogFactory.getLogFor(Cookie.class);
	private int flags = 0;
	private CIString name = null, value=null, domain=null, path=null;
	private Date expires=null;
	private String comment=null, version="1";
	public static final int F_SECURE = 2;
	public Cookie() {}
	public boolean isFlagSet(final int flag) {
		return App.isSet(flags, flag);
	}
	public void setFlag(final int flag) {
		flags |= flag;
	}
	public void unsetFlag(final int flag) {
		flags ^= flag;
	}
	public static Vector<Cookie> parse(String cookie) {
		//max-age: in seconds
		//commentURL must be in quotes
		//port list must be in quotes, comma separated
		//cookies are comma-separated(note for cookie handler)
		int beg = 0, end = 0,semi=0,comma=0;
		String name,value;
		Cookie cTmp;
		if (cookie.startsWith("Set-Cookie:")) {
			cookie = cookie.substring(11);
		}
		final Vector<Cookie> cookies = new Vector<Cookie>();
		final DateFormat df = DateFormat.getDateInstance();
		while (end!=-1 && end!=cookie.length()) {
			beg = end;
			beg = do_str.skipWhitespace(cookie, beg);
			if (beg==end) break;
			comma = cookie.indexOf(',',comma+1);
			if (comma== -1) comma = cookie.length();
			cTmp = new Cookie();
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
					} catch (final ParseException e) {
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
					} catch (final NumberFormatException e) {
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
		return null;
	}
	public void setName(final String name) {
		this.name = new CIString(name);
	}
	public void setValue(final String value) {
		this.value = new CIString(value);
	}
	public String getName() {
		return name.toString();
	}
	public String getValue() {
		return value.toString();
	}
	public void setDomain(final String domain) {
		if (!(domain.charAt(0)=='.'))
			this.domain = new CIString('.'+domain);
		else
			this.domain = new CIString(domain);
	}
	public String getDomain() {
		return domain.toString();
	}
	public void setPath(final String path) {
			this.path = new CIString(path);
	}
	public String getPath() {
		return path.toString();
	}
	public void setExpires(final Date expiry) {
		this.expires = expiry;
	}
	public Date getExpires() {
		return expires;
	}
	public void setMaxAge(final int seconds) {
		//TODO: get current date(GMT) add these seconds convert to string (Wed, 13-Mar-2013 07:21:20 GMT)
	}
	public void setComment(final String comment) {
		this.comment = comment;
	}
	public String getComment() {
		return comment;
	}

	public void setVersion(final String version) {
		this.version = version;
	}
	public String getVersion() {
		return version;
	}
	@Override
	public String toString() {
		return toResponseString();
	}
	public String toRequestString() {
		final StringBuilder ret = new StringBuilder("$Version="+version+";"+name+"=\""+value+'"');
		if (path!=null) { ret.append(";$Path=\""+path+'"'); }
		if (domain!=null) { ret.append(";$Domain="+domain+'"'); }
		return ret.toString();
	}
	public String toResponseString() {
		final StringBuilder ret = new StringBuilder(name+"=\""+value+'"');
		if (comment!=null) { ret.append(";Comment=\""+comment+'"'); }
		if (domain!=null) { ret.append(";Domain=\""+domain+'"'); }
		if (expires!=null) { ret.append(";expires=\""+expires+'"'); }
		if (path!=null) { ret.append(";Path=\""+path+'"'); }
		if ((flags&F_SECURE) == F_SECURE) { ret.append(";Secure"); }
		if (version!=null) { ret.append(";Version=\""+version+'"'); }
		return ret.toString();
	}
}
