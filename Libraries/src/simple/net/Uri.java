/**
 *
 */
package simple.net;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import simple.CIString;
import simple.io.DoubleParsePosition;
import simple.util.do_str;
import simple.util.logging.Log;
import simple.util.logging.LogFactory;

/** More accurately parses the parts of a URI. This will even parse partial
 * URI's with no problem.<br>
 * The example URIs that will be used:<br>
 * http://train.rail.com/georgia/augusta.php?id=1654#schedule<br>
 * urn:isbn:0-395-36341-1<br>
 * <br>Created: Nov 5, 2010
 * @author Kenneth Pierce
 */
public final class Uri {
	private static final Log log = LogFactory.getLogFor(Uri.class);
	public static final int SCHEME = 1,
	HOST = 2,
	PORT = 4,
	PATH = 8,
	FILE = 16,
	QUERY = 32,
	FRAGMENT = 64;
	private static String defaultScheme="";
	/*
	 * unreserved  = ALPHA / DIGIT / "-" / "." / "_" / "~"
	 * reserved    = general-delimiters / sub-delimiters
	 * general-delimiters  = ":" / "/" / "?" / "#" / "[" / "]" / "@"
	 * sub-delimiters  = "!" / "$" / "&" / "'" / "(" / ")"
	 *         / "*" / "+" / "," / ";" / "="
	 */
	private static final String reserved="&=?:/+#[]@!$'()*,;` ";
	/** The scheme used.<br>
	 *  http<br>
	 *  javascript
	 */
	private final String scheme;
	/** The domain.<br>
	 * rail.com<br>
	 * N/A
	 */
	private final String domain;
	/** The host.<br>
	 * train.rail.com<br>
	 * N/A
	 */
	private final String host;
	/** The path.<br>
	 * /georgia/<br>
	 * :isbn:
	 */
	private final String path;
	/** The file.<br>
	 * augusta.php<br>
	 * 0-395-36341-1
	 */
	private final String file;
	/** The query without leading '?'.<br>
	 * id=1654<br>
	 * none defined
	 */
	private final String query;
	/** The fragment without leading '#'. (#fragment)<br>
	 * #schedule<br>
	 * none defined
	 */
	private final String fragment;
	/** The URI used to create this. */
	private final String originalUri;
	/** The port specified or -1 if none. */
	private final int port;
	/** The type of URI this object represents. */
	private final UriType type;
	private volatile boolean qparsed=false;
	/** A breakdown of the query. */
	private final HashMap<CIString, String> params = new HashMap<CIString, String>();
	private final int hash;

	/**
	 * Sets the default scheme to use when a URI starting with '//'
	 * is encountered. The default default scheme is blank.
	 * Updating the default scheme is not retroactive.
	 * @param scheme The default scheme
	 */
	public static void setDefaultScheme(String scheme){
		defaultScheme=scheme.toLowerCase();
	}
	/**
	 * @return The default scheme
	 */
	public static String getDefaultScheme(){return defaultScheme;}

	public Uri(final URI uri) {
		this(uri.toString());
	}
	public Uri(final URL url) {
		this(url.toExternalForm());
	}
	public Uri(final String uri){
		this(uri,defaultScheme);
	}
	/**
	 * @param uri The URI
	 * @param defaultScheme Used if the URI starts with "//". Is to address a new fad on the net(2012-07-08).
	 */
	public Uri(String uri,String defaultScheme){
		if(uri.startsWith("//") && !defaultScheme.isEmpty()){
			uri=defaultScheme+":"+uri;
		}
		hash = new CIString(uri).hashCode();
		originalUri = uri;
		final DoubleParsePosition pos = new DoubleParsePosition();
		final int frag_index = do_str.defaultValue(uri.lastIndexOf('#'), uri.length());
		final int query_index= do_str.defaultValue(uri.indexOf('?'), uri.length());
		final int path_index = do_str.defaultValue(uri.indexOf('/'), uri.length());
		/* URI         = scheme ":" hierarchical-part [ "?" query ] [ "#" fragment ]
		 * hierarchical-part   = "//" authority path-absolute-empty
		 * / path-absolute
		 * / path-root-less
		 * / path-empty
		 */
		try {
			pos.end = uri.indexOf(':');
			if (!pos.validEnd()) {//semicolon found.
				scheme = "";
			} else if (pos.end > frag_index || pos.end > query_index || pos.end > path_index) {
				//is before everything
				scheme = "";
				pos.end = -1;
			} else {
				scheme = uri.substring(pos.start, pos.end);
			}
			//XXX:scheme set
			pos.resetStart();
			pos.incStart();
			if (pos.start == uri.length()) {
				type = UriType.URN;
				host = "";
				domain = "";
				port = -1;
				path = "";
				file = "";
				query = "";
				fragment = "";
				return;
			}
			if (!scheme.isEmpty() && uri.charAt(pos.start)!='/') {//NOTE:name-space
				type = UriType.URN;
				host = "";
				domain = "";
				file = "";
				port = -1;
				//XXX: path query fragment
				pos.end = do_str.indexOfAny(uri, "?#");//query, fragment
				if (pos.validEnd()) {//query or fragment found(pos.validEnd()==true)
					if(uri.charAt(pos.start)!='/')
						path = "/"+uri.substring(pos.start, pos.end);
					else
						path = uri.substring(pos.start, pos.end);
					pos.resetStart();
				} else {//name-space;no-query;no-fragment
					if(uri.charAt(pos.start)!='/')
						path = "/"+uri.substring(pos.start);
					else
						path = uri.substring(pos.start);
					query = "";
					fragment = "";//NOTE: path query fragment [return]
					return;
				}
				//XXX: query fragment
				if (uri.charAt(pos.end)=='#') {//fragment;no-query
					query = "";
					fragment = uri.substring(pos.end+1);
					return;
				} else {//query and/or fragment
					pos.resetStart();
					pos.incStart();
					pos.end = uri.indexOf('#', pos.start);
					if (pos.validEnd()) {//query;fragment
						query = uri.substring(pos.start, pos.end);
						fragment = uri.substring(pos.end+1);
					} else {//query;no-fragment
						query = uri.substring(pos.start);
						fragment = "";
					}
				}//end fragment/query
				return;
			}//XXX:end name-space [URN]
			type = UriType.URL;
			if (!scheme.isEmpty()) {
				if(uri.length()!=pos.start+1)
					if(uri.charAt(pos.start+1)=='/')//Had a case where someone goofed and put http:/example.com
						pos.start += 2;
					else
						pos.incStart();
			}
			pos.resetEnd();
			// at char past 'scheme://'
			if (uri.charAt(pos.start)=='[') {//NOTE:ipv6 host set
				pos.end = uri.indexOf(']', pos.start);
				if (!pos.validEnd()) throw new IllegalArgumentException("missing matching ] for ipv6 address");
				host = uri.substring(pos.start+1, pos.end);
				domain = "";
				pos.incEnd();
				if(pos.end<uri.length())
					pos.decEnd();
			} else {//ipv4 or hostname
				pos.end = do_str.indexOfAny(uri,":/",pos.start);
						//uri.indexOf('/', pos.start);//port,path,query,fragment

				if (scheme.isEmpty()) {//no host set
					//log.debug("NO host");
					host = domain = "";
				} else {
					//log.debug("host");
					if (pos.validEnd()) {//NOTE: set host
						host = uri.substring(pos.start, pos.end);
					} else {
						host = uri.substring(pos.start);
					}
					pos.markEnd();//save end position
					pos.end = uri.lastIndexOf('.', pos.end);//find first '.'
					if (!pos.validEnd()) {//NOTE: set domain
						domain = host;
					} else {
						pos.end = uri.lastIndexOf('.', pos.end-1);//find 2nd '.'
						if (!pos.validEnd()) {//no second, set domain to host
							domain = host;
						} else {
							domain = uri.substring(pos.end+1, pos.getMarkEnd());
						}
					}
					pos.toMarkEnd();//restore end position
					if (!pos.validEnd()) {//no character indicating these were set was found
						//log.debug("Nothing else. Done.");
						port = -1;
						path = "";
						file = "";
						query = "";
						fragment = "";
						return;
					}
				}
			}
			pos.resetStart();
			/*
			 * Checking for port ':'
			 * path '/'
			 * query '?' or
			 * fragment '#'
			 */
			if(pos.start==-1)pos.start=0;
			if (uri.charAt(pos.start)==':') {//NOTE: found port
				//log.debug("port");
				pos.incStart();
				pos.end = do_str.indexOfAny(uri, "/?#", pos.start);//path,query,fragment
				if (!pos.validEnd()) {
					//log.debug("Nothing else. done");
					port = Integer.parseInt(uri.substring(pos.start));
					path = "";
					file = "";
					query = "";
					fragment = "";
					return;
				}else{
					port = Integer.parseInt(uri.substring(pos.start, pos.end));
				}
				pos.resetStart();
			} else {//NOTE: no port found
				//log.debug("NO port");
				port = -1;
			}
			//XXX: path file query fragment
			if (uri.charAt(pos.start)!='?' && uri.charAt(pos.start)!='#') {//NOTE:check for path
				pos.end = do_str.indexOfAny(uri, "?#", pos.start);
				pos.markEnd();
				if (!pos.validEnd()) {//no query or fragment
					pos.end = uri.lastIndexOf('/');
					if (pos.isEqual() || !pos.validEnd()) {//no path
						path = "/";
						if(uri.charAt(pos.start)!='/')
							file = uri.substring(pos.start);
						else
							file = uri.substring(pos.start+1);
					} else {
						if(uri.charAt(pos.start)!='/')
							path = "/"+uri.substring(pos.start, pos.end+1);
						else
							path = uri.substring(pos.start, pos.end+1);
						//pos.resetStart(); pos.incStart();
						//pos.toMarkEnd();
						file = uri.substring(pos.end+1);
					}
					query = "";
					fragment = "";
					//log.debug("NO query/fragment. done");
					return;
				} else {//query and/or fragment
					//log.debug("query or fragment");
					//marked end = end of all
					pos.end = uri.lastIndexOf('/', pos.end);
					if (pos.start == pos.end || pos.end==-1) {
						path = "/";
						if(uri.charAt(pos.start)!='/')
							file = uri.substring(pos.start,pos.getMarkEnd());
						else
							file = uri.substring(pos.start+1,pos.getMarkEnd());
					} else {
						if(uri.charAt(pos.start)!='/')
							path = "/"+uri.substring(pos.start, pos.end+1);
						else
							path = uri.substring(pos.start, pos.end+1);
						file = uri.substring(pos.end+1, pos.getMarkEnd());
					}
				}
				pos.toMarkEnd();//reset end;
				pos.resetStart();//set start to the start of the query/fragment
			} else {//NOTE: no path or file
				path = "";
				file = "";
			}
			//XXX: query fragment
			if (uri.charAt(pos.start)=='?' && uri.length()>1) {
				pos.end = uri.indexOf('#', pos.start);
				if (!pos.validEnd()) {//no fragment
					//log.debug("Query, NO fragment; done");
					query = uri.substring(pos.start+1);
					fragment = "";
					return;
				} else {
					query = uri.substring(pos.start+1, pos.end);
				}
				pos.resetStart();
			} else {
				query = "";
			}
			if (uri.charAt(pos.start)=='#' && uri.length()>1) {
				//log.debug("fragment");
				fragment = uri.substring(pos.start+1);
			} else {
				//log.debug("NO fragment");
				fragment = "";
			}
		} catch (final StringIndexOutOfBoundsException e) {
			log.error(uri+" :: "+pos,e);
			throw e;
		}
		//log.debug("done");
	}
	/**Trims the fragment off the URI. Useful for keeping a list of pages
	 * already read. Query is kept since this can affect the page's content.
	 * @return The URI minus the fragment.
	 */
	public final String trimFragment() {
		switch(this.type) {
		case URL:
			return this.getScheme()+"://"+this.getHost()+this.getPath()+this.getFile()+(getQuery().isEmpty()?"":"?"+getQuery());
		case URN:
			return this.getScheme()+":"+this.getPath()+(getQuery().isEmpty()?"":"?"+getQuery());
		default:
			return "";
		}
	}
	/**Trims the fragment and query off the URI. Useful for keeping a list of pages
	 * already read where the query has no effect.
	 * @return The URI minus the fragment.
	 */
	public final String trimTrailing() {
		switch(this.type) {
		case URL:
			return this.getScheme()+"://"+this.getHost()+this.getPath()+this.getFile();
		case URN:
			return this.getScheme()+":"+this.getPath();
		default:
			return "";
		}
	}

	/**
	 * @return the type
	 */
	public final UriType getType() {
		return type;
	}
	/**
	 * http, ftp, https, etc
	 * @return the scheme
	 */
	public final String getScheme() {
		return scheme;
	}
	/**
	 * example.com
	 * @return the domain
	 */
	public final String getDomain() {
		return domain;
	}
	/**
	 * www.example.com
	 * @return the host
	 */
	public final String getHost() {
		return host;
	}
	/** The path with leading and trailing '/'
	 * @return the path
	 */
	public final String getPath() {
		return path;
	}
	/** The query without leading '?'.
	 * @return the query
	 */
	public final String getQuery() {
		return query;
	}
	/** The fragment without leading '#'.
	 * @return the fragment
	 */
	public final String getFragment() {
		return fragment;
	}
	/**
	 * @return the file
	 */
	public final String getFile() {
		return file;
	}
	/**
	 * @return the originalUri
	 */
	public final String getOriginalUri() {
		return originalUri;
	}
	/**
	 * @return the port
	 */
	public final int getPort() {
		return port;
	}
	/**
	 * @return the query parameters
	 */
	public final HashMap<CIString, String> getParams() {
		return params;
	}
	public final URL toURL() throws MalformedURLException {
		return new URL(this.originalUri);
	}
	public final URI toURI() throws URISyntaxException {
		return new URI(this.originalUri);
	}
	private static final void parseParams(final HashMap<CIString, String> store, final String query){
		final DoubleParsePosition pos = new DoubleParsePosition();
		String[] tmp = null;
		while((pos.end = query.indexOf("&", pos.start))!=-1) {
			tmp = do_str.substring(query, pos).split("=");
			if (tmp.length == 2) {
				store.put(new CIString(tmp[0]), tmp[1]);
			} else if (tmp.length == 1) {
				store.put(new CIString(tmp[0]), "");
			}
			pos.resetStart(); pos.incStart();
		}
		tmp = query.substring(pos.start).split("=");
		if (tmp.length == 2) {
			store.put(new CIString(tmp[0]), tmp[1]);
		} else {
			store.put(new CIString(tmp[0]), "");
		}
	}
	public final void parseQuery(){
		boolean parsed=qparsed;
		if(parsed)return;
		synchronized(params){
			parsed=qparsed;
			if(parsed)return;
			parseParams(params,query);
			qparsed=true;
		}
	}
	public final boolean isQueryParsed(){return qparsed;}
	/**Convenience method for {@link #getQuery(CIString)}.
	 * @param name Name of the parameter.
	 * @return The value of the parameter or null if it was not defined.
	 */
	public String getQuery(final String name) {
		return getQuery(new CIString(name));
	}
	/** Gets the value associated with the name.
	 * @param name Name of the parameter
	 * @return The value of the parameter or null if it was not defined.
	 */
	public String getQuery(final CIString name) {
		return params.get(name);
	}
	/** Convenience method for {@link #getQuery(CIString, String)}.
	 * @param name Name of the parameter.
	 * @param def Default value.
	 * @return The value of the parameter or the default value if it was not defined.
	 * @deprecated
	 */
	@Deprecated
	public String getQuery(final String name, final String def) {
		return getQuery(new CIString(name), def);
	}
	/** Gets the value of the parameter. Returns the default value if it was not
	 * defined.
	 * @param name Name of the parameter
	 * @param def Default value
	 * @return The value of the parameter or the default if it was not defined.
	 */
	public String getQuery(final CIString name, final String def) {
		final String o=params.get(name);
		if (o==null)
			return def;
		return o;
	}
	/**Gets the query names.
	 * @return An enumeration of the query names.
	 */
	public Set<CIString> getQueryKeys() {
		return params.keySet();
	}
	/**Gets the query values.
	 * @return A collection of the query values.
	 */
	public Collection<String> getQueryValues() {
		return params.values();
	}
	public String getQueryEscaped(){
		if(query.isEmpty())return "";
		parseQuery();
		final StringBuilder ret=new StringBuilder(query.length());
		final Iterator<Map.Entry<CIString,String>>iter=params.entrySet().iterator();
		Map.Entry<CIString,String>en=iter.next();
		ret.append(en.getKey()+"="+escapeSmart(en.getValue()));
		while(iter.hasNext()){
			en=iter.next();
			ret.append("&"+en.getKey()+"="+escapeSmart(en.getValue()));
		}
		return ret.toString();
	}
	/** Builds the uri with the options given.
	 * @param options Any combination of SCHEME, HOST, PORT, PATH, FILE, QUERY, FRAGMENT
	 * @return The custom uri
	 */
	public String buildCustom(final int options) {
		final StringBuilder ret = new StringBuilder();
		if ((options&SCHEME)==SCHEME) {
			ret.append(getScheme()+":");
			if (this.getType()==UriType.URL){
				ret.append("//");
			}
		}
		if ((options&HOST)==HOST){
			ret.append(getHost());
		}
		if ((options&PORT)==PORT){
			if (port!=-1){
				ret.append(":"+getPort());
			}
		}
		if ((options&PATH)==PATH){
			ret.append(getPath());
		}
		if ((options & FILE) == FILE) {
			ret.append(getFile());
		}
		if ((options & QUERY) == QUERY) {
			if (!query.isEmpty()) {
				ret.append("?"+getQuery());
			}
		}
		if ((options & FRAGMENT) == FRAGMENT) {
			if (!fragment.isEmpty()) {
				ret.append("#"+getFragment());
			}
		}
		return ret.toString();
	}
	/** Builds the uri with the options given.
	 * @param options Any combination of SCHEME, HOST, PORT, PATH, FILE, QUERY, FRAGMENT
	 * @return The custom uri
	 */
	public String buildCustomEscaped(final int options) {
		final StringBuilder ret = new StringBuilder();
		if ((options&SCHEME)==SCHEME) {
			ret.append(getScheme()+":");
			if (this.getType()==UriType.URL){
				ret.append("//");
			}
		}
		if ((options&HOST)==HOST){
			ret.append(escape(getHost()));
		}
		if ((options&PORT)==PORT){
			if (port!=-1){
				ret.append(":"+getPort());
			}
		}
		if ((options&PATH)==PATH){
			ret.append(escapePath(getPath()));
		}
		if ((options&FILE)==FILE){
			ret.append(escapeSmart(getFile()));
		}
		if ((options&QUERY)==QUERY){
			if (!query.isEmpty()){
				ret.append("?"+getQueryEscaped());
			}
		}
		if ((options&FRAGMENT)==FRAGMENT){
			if (!fragment.isEmpty()){
				ret.append("#"+escape(getFragment()));
			}
		}
		return ret.toString();
	}
	/** The original URI used to create this Uri.
	 * @return the original URI.
	 */
	@Override
	public String toString() {
		return originalUri;
	}
	public URLConnection openConnection() throws MalformedURLException, IOException {
		return new URL(this.getOriginalUri()).openConnection();
	}
	@Override
	public int hashCode() {
		return hash;
	}
	@Override
	public boolean equals(final Object o) {
		if (o instanceof Uri)
			return this.originalUri.equals(((Uri)o).originalUri);
		else
			return false;
	}
	public final String escape(){
		final StringBuilder ret=new StringBuilder(256);
		if(!scheme.isEmpty()){
			ret.append(scheme+":");
			if(type==UriType.URL) {
				ret.append("//");
			}
		}
		if(!host.isEmpty()) {
			ret.append(escape(host));
		}
		if(port!=-1) {
			ret.append(":"+port);
		}
		if(!path.isEmpty()) {
			ret.append(escapePath(path));
		}
		if(!file.isEmpty()) {
			ret.append(escapeSmart(file));
		}
		if(!query.isEmpty()){
			ret.append('?');
			final Iterator<Map.Entry<CIString,String>>iter=params.entrySet().iterator();
			Map.Entry<CIString,String>en=iter.next();
			ret.append(en.getKey()+"="+escapeSmart(en.getValue()));
			while(iter.hasNext()){
				en=iter.next();
				ret.append("&"+en.getKey()+"="+escapeSmart(en.getValue()));
			}
		}
		if(!fragment.isEmpty()){ret.append("#"+escapeSmart(fragment));}
		return ret.toString();
	}
	public static final String escapePath(final String path){
		final StringBuilder ret=new StringBuilder(path.length()*2);
		int ind=0;
		char chr=0;
		for(int i=0;i<path.length();i++){
			chr=path.charAt(i);
			if(chr=='/'){
				ret.append('/');
			}else if(chr=='%'){
				chr=path.charAt(i+1);
				if(chr>='0'&&chr<='f'||chr>='A'&&chr<='F'){
					chr=path.charAt(i+2);
					if(chr>='0'&&chr<='f'||chr>='A'&&chr<='F'){
						ret.append(path.substring(i, i+3));
						i+=2;
					}else{ret.append('%');ret.append(Integer.toHexString('%'));}
				}else{ret.append('%');ret.append(Integer.toHexString('%'));}
			}else{
				ind=reserved.indexOf(chr);
				if(ind!=-1){
					ret.append('%');
					ret.append(Integer.toHexString(path.charAt(i)));
				}else{ret.append(path.charAt(i));}
			}
		}
		return ret.toString();
	}
	/** Escapes all reserved characters.
	 * Checks for valid escape sequences to avoid escaping an escape character.
	 * If the escape sequence is invalid then the escape is escaped.
	 * @param part The string to escape
	 * @return the escaped string
	 */
	public static final String escapeSmart(final String part){
		final StringBuilder ret=new StringBuilder(part.length()*2);
		int ind=0;
		char chr=0;
		for(int i=0;i<part.length();i++){
			chr=part.charAt(i);
			if(chr=='%'){
				chr=part.charAt(i+1);
				if(chr>='0'&&chr<='f'||chr>='A'&&chr<='F'){
					chr=part.charAt(i+2);
					if(chr>='0'&&chr<='f'||chr>='A'&&chr<='F'){
						ret.append(part.substring(i, i+3));
						i+=2;
					}else{ret.append('%');ret.append(Integer.toHexString('%'));}
				}else{ret.append('%');ret.append(Integer.toHexString('%'));}
			}else{
				ind=reserved.indexOf(chr);
				if(ind!=-1){
					ret.append('%');
					ret.append(Integer.toHexString(part.charAt(i)));
				}else{ret.append(part.charAt(i));}
			}
		}
		return ret.toString();
	}
	/** Blindly escapes all reserved characters.
	 * @param part the string to escape
	 * @return the escaped string
	 */
	public static final String escape(final String part){
		final StringBuilder ret=new StringBuilder(part.length()*2);
		int ind=0;
		char chr=0;
		for(int i=0;i<part.length();i++){
			chr=part.charAt(i);
			ind=reserved.indexOf(chr);
			if(ind!=-1){
				ret.append('%');
				ret.append(Integer.toHexString(chr));
			}else{ret.append(chr);}
		}
		return ret.toString();
	}
	/** Unescapes an escaped string.
	 * @param part the string to unescape
	 * @return the unescaped string
	 */
	public static final String unescape(final String part){
		final StringBuilder ret=new StringBuilder(part.length());
		char chr=0;
		for(int i=0;i<part.length();i++){
			chr=part.charAt(i);
			if(chr=='%'){
				chr=part.charAt(i+1);
				if(chr>='0'&&chr<='f'||chr>='A'&&chr<='F'){
					chr=part.charAt(i+2);
					if(chr>='0'&&chr<='f'||chr>='A'&&chr<='F'){
						ret.append((char)Integer.parseInt(part.substring(i+1, i+3), 16));
						i+=2;
					}else{ret.append(chr);}
				}else{ret.append(chr);}
			}else{ret.append(chr);}
		}
		return ret.toString();
	}
	/*
	public static void main(final String[] arg) {
		dump(new Uri("//thepiratebay.org/user/THE.DOCTOR.1963/0/3","http"));
		dump(new Uri("//images.4chan.org/hc/src/1341547348739.jpg"));
		dump(new Uri("http://thepiratebay.org/user/THE.DOCTOR.1963/0/3"));
		dump(new Uri("http://host.com/sub1/d2plxsb"));
		dump(new Uri("http://host.com/sub1/sub2/d2plxsb"));
		dump(new Uri("http://host.com/sub1/sub2/sub3/d2plxsb"));
		dump(new Uri("http://host.com/sub1/sub2/d2plxsb.ext"));

		dump(new Uri("D:\\_Movies\\[KAA]_Chrono_Crusade_01-24.DVD(complete)\\Chrono_Cusade_01[v2].DVD(AAC.H264)[KAA][06C15EBB].mkv"));
		dump(new Uri("http://darkiron.deviantart.com/gallery/#/d2plxsb"));
		dump(new Uri("http://darkiron.deviantart.com"));
		dump(new Uri("/gallery/#/d2plxsb"));
		dump(new Uri("gallery.php?g=poopa#d2plxsb"));
		dump(new Uri("urn:gallery:booboo?k=90#/d2plxsb"));
		dump(new Uri("index.php?page=post&s=list&tags=parent:654648"));
	}
	*/
	public static void dump(Uri p){
		System.out.println("baseURL:"+p.getOriginalUri());
		System.out.println("type:   "+p.getType());
		System.out.println("scheme: "+p.getScheme());
		System.out.println("host:   "+p.getHost());
		System.out.println("domain: "+p.getDomain());
		System.out.println("port:   "+p.getPort());
		System.out.println("path:   "+p.getPath());
		System.out.println("file:   "+p.getFile());
		System.out.println("query:  "+p.getQuery());
		System.out.println("anchor: "+p.getFragment());
	}/**/

}
