/**
 *
 */
package simple.net.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;

import simple.io.FileUtil;
import simple.net.http.clientparams.ClientParam;
import simple.util.logging.Log;
import simple.util.logging.LogFactory;
import simple.util.logging.LogLevel;
/**
 * <hr>
 * <br>
 * Created: Oct 16, 2011
 *
 * @author Kenneth Pierce
 */
public final class Client{
	private static final Log log=LogFactory.getLogFor(Client.class);
	// Needed anymore?
	//private final HashMap<String,DefaultHttpClient> cache=new HashMap<String,DefaultHttpClient>();
	private final CookieStore cookies;
	private final HttpHost proxy;
	private final CloseableHttpClient client;
	public static final Header[] defaults=new Header[]{
		new BasicHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"),
		new BasicHeader("Accept-Charset","ISO-8859-1,utf-8;q=0.7,*;q=0.3"),
		new BasicHeader("Accept-Encoding","gzip,deflate"),
		new BasicHeader("Accept-Language","en-US,en;q=0.8")
	};
	public static enum PostDataType {
		UrlEncoded("application/x-www-form-urlencoded"),
		FormData("multipart/form-data");
		public final String contentType;
		PostDataType(String contentType){
			this.contentType= contentType;
		}
	}
	public void addCookie(Cookie cookie){
		cookies.addCookie(cookie);
	}
	public void addCookies(Cookie[] cookies){
		for(Cookie cookie: cookies){
			this.cookies.addCookie(cookie);
		}
	}
	public CookieStore getCookieStore(){
		return cookies;
	}
	public void saveCookies(File destination) throws FileNotFoundException, IOException{
		if(!FileUtil.createFile(destination)){
			throw new IOException("Failed to create the cookie store file.");
		}
		try(ObjectOutputStream oos= new ObjectOutputStream(new FileOutputStream(destination))){
			oos.writeObject(cookies);
		}
	}
	public static CookieStore loadCookies(File source) throws FileNotFoundException, IOException, ClassNotFoundException{
		try(ObjectInputStream ois= new ObjectInputStream(new FileInputStream(source))){
			return (CookieStore)ois.readObject();
		}
	}

	/**
	 * Fetches the URI
	 * @param uri The item to fetch
	 * @return The response
	 * @throws ClientProtocolException  in case of an http protocol error
	 * @throws IOException in case of a problem or the connection was aborted
	 */
	public CloseableHttpResponse get(String uri) throws ClientProtocolException, IOException{
		return get(uri,null,null);
	}
	/**
	 * Fetches the URI
	 * @param uri The item to fetch
	 * @param headers The headers to set
	 * @return The response
	 * @throws ClientProtocolException  in case of an http protocol error
	 * @throws IOException in case of a problem or the connection was aborted
	 */
	public CloseableHttpResponse get(String uri, Header[] headers) throws ClientProtocolException, IOException{
		return get(uri,headers,null);
	}

	/**
	 * Fetches the URI
	 * @param uri URI to fetch
	 * @param headers request headers to set
	 * @param context Execution context
	 * @return The response
	 * @throws ClientProtocolException  in case of an http protocol error
	 * @throws IOException in case of a problem or the connection was aborted
	 */
	public CloseableHttpResponse get(String uri, Header[] headers, HttpContext context) throws ClientProtocolException, IOException{
		HttpGet req=new HttpGet(uri);

		if(headers!=null){
			req.setHeaders(headers);
		}

		CloseableHttpResponse response;
		if(context==null){
			response= client.execute(req);
		}else{
			response= client.execute(req,context);
		}

		if(response.getStatusLine().getStatusCode()==301 || response.getStatusLine().getStatusCode()==302){// redirection
			Header location=response.getFirstHeader("Location");
			log.debug("Redirect("+response.getStatusLine().getStatusCode()+")",uri+" --TO-- "+location);
			if(location!=null){
				return get(location.getValue(),headers,context);
			}
		}
		log.debug("Response",response);
		return response;
	}


	/**
	 * Posts text data to the server
	 * @param uri URI to fetch
	 * @param headers Nullable. Headers to set
	 * @param data Data to sent
	 * @param context Nullable. Execution context
	 * @return The response
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public CloseableHttpResponse post(String uri,Header[] headers,String data,HttpContext context) throws ClientProtocolException, IOException{
		return post(uri, headers, data, null, context);
	}
	/**
	 * Posts text data to the server
	 * @param uri URI to fetch
	 * @param headers Nullable. Headers to set
	 * @param data Data to sent
	 * @param charset Nullable. Character encoding for data
	 * @param context Nullable. Execution context
	 * @return The response
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public CloseableHttpResponse post(String uri,Header[] headers,String data, Charset charset,HttpContext context) throws ClientProtocolException, IOException{
		HttpPost req=new HttpPost(uri);

		if(headers!=null){
			req.setHeaders(headers);
		}
		if(data!=null){
			if(charset==null)
				req.setEntity(new StringEntity(data));
			else
				req.setEntity(new StringEntity(data, charset));
		}

		CloseableHttpResponse response;
		if(context==null){
			response= client.execute(req);
		}else{
			response= client.execute(req,context);
		}

		if(response.getStatusLine().getStatusCode()==301 || response.getStatusLine().getStatusCode()==302){
			// redirection
			Header location=response.getFirstHeader("Location");
			log.debug("Redirect("+response.getStatusLine().getStatusCode()+")",uri+" --TO-- "+location);
			if(location!=null){
				return post(location.getValue(),headers,data,charset,context);
			}
		}
		log.debug("Response",response);
		return response;
	}

	/**
	 * Posts a series of name value pairs to the server
	 * @param uri URI to post the data to
	 * @param headers Nullable. Request headers
	 * @param data Data to post
	 * @param format Format the data should be sent in
	 * @param context Nullable. Execution context
	 * @return The response
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public CloseableHttpResponse post(String uri,Header[] headers,ClientParam[] data,PostDataType format,HttpContext context) throws ClientProtocolException, IOException{
		HttpPost req=new HttpPost(uri);

		if(headers!=null){
			req.setHeaders(headers);
		}

		if(data!=null){
			switch(format){
			case FormData:
				// I assume this is proper enough
				String boundary=Integer.toHexString(uri.hashCode());
				req.setHeader("Content-Type",format.contentType+"; boundary="+boundary);
				req.setEntity(new MultipartFormEntity(data,boundary));
				break;
			case UrlEncoded:
				req.setHeader("Content-Type",format.contentType);
				req.setEntity(new UrlEncodedFormEntity(Arrays.asList(data)));
				break;
			default:
				throw new ClientProtocolException("Unknown PostDataType: "+format);
			}
		}

		CloseableHttpResponse response;
		if(context==null){
			response= client.execute(req);
		}else{
			response= client.execute(req,context);
		}

		if(response.getStatusLine().getStatusCode()==301 || response.getStatusLine().getStatusCode()==302){
			Header location=response.getFirstHeader("Location");
			log.debug("Redirect("+response.getStatusLine().getStatusCode()+")",uri+" --TO-- "+location);
			if(location!=null){
				return post(location.getValue(),headers,data,format,context);
			}
		}
		log.debug("Response",response);
		return response;
	}
	/**
	 * Retries 5 times.
	 */
	private static final HttpRequestRetryHandler RetryHandler=new HttpRequestRetryHandler(){
		@Override
		public boolean retryRequest(IOException exception,int executionCount,HttpContext context){
			if(executionCount>=5){
				// Do not retry if over max retry count
				return false;
			}
			if(exception instanceof NoHttpResponseException){
				// Retry if the server dropped connection on us
				return true;
			}
			if(exception instanceof SSLHandshakeException){
				// Do not retry on SSL handshake exception
				return false;
			}
			HttpRequest request=(HttpRequest)context.getAttribute(HttpCoreContext.HTTP_REQUEST);
			boolean idempotent=!(request instanceof HttpEntityEnclosingRequest);
			if(idempotent){
				// Retry if the request is considered idempotent
				return true;
			}
			return false;
		}
	};
	public Client(HttpHost proxy, CookieStore cookies){
		this.proxy= proxy;
		if(cookies != null){
			this.cookies= cookies;
		}else{
			this.cookies= new BasicCookieStore();
		}
		client= init().build();
	}
	public Client(String ProxyHost,int ProxyPort){
		this(new HttpHost(ProxyHost,ProxyPort), null);
	}
	public Client(){
		this(null, null);
	}
	/**
	 * Initializes the connection builder
	 */
	private HttpClientBuilder init(){
		HttpClientBuilder conBuilder= HttpClientBuilder.create();
		conBuilder
			.setDefaultHeaders(Arrays.asList(defaults))
			.setUserAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/4.4.6.1000 Chrome/30.0.1599.101 Safari/537.36")
			.setDefaultCookieStore(cookies);

		if(log.getPrint(LogLevel.DEBUG)){
			conBuilder.addInterceptorFirst(
				new HttpRequestInterceptor(){
					@Override
					public void process(final HttpRequest request,final HttpContext context) throws HttpException,IOException{
						log.debug(request);
					}
				});
		}
		// Expand if compressed
		/* 2015-11-13 Seems the new HttpClient expands automatically... Keeping in case the ever changes.
		conBuilder.addInterceptorFirst(new HttpResponseInterceptor(){
				@Override
				public void process(final HttpResponse response,final HttpContext context) throws HttpException,IOException{
					HttpEntity entity=response.getEntity();
					Header ceheader=entity.getContentEncoding();
					if(ceheader!=null){
						HeaderElement[] codecs=ceheader.getElements();
						log.debug("Codecs",codecs);
						for(int i=0;i<codecs.length;i++){
							if(codecs[i].getName().equalsIgnoreCase("gzip")){
								response.setEntity(new GzipDecompressingEntity(response.getEntity()));
								log.debug("Content-Encoding","GZIP");
								return;
							}else if(codecs[i].getName().equalsIgnoreCase("deflate")){
								response.setEntity(new DeflaterDecompressingEntity(response.getEntity()));
								log.debug("Content-Encoding","DEFLATE");
								return;
							}
						}
					}
				}
			});
		/**/
		// set the proxy host
		if(proxy != null)
			conBuilder.setProxy(proxy);
		conBuilder.setRetryHandler(RetryHandler);

		return conBuilder;
	}
	public void addCookies(List<Cookie> cookies2) {
		for(Cookie cookie:cookies2){
			addCookie(cookie);
		}
	}
}
class GzipDecompressingEntity extends HttpEntityWrapper{
	public GzipDecompressingEntity(final HttpEntity entity){
		super(entity);
	}
	@Override
	public InputStream getContent() throws IOException,IllegalStateException{
		// the wrapped entity's getContent() decides about repeatability
		InputStream wrappedin=wrappedEntity.getContent();
		return new GZIPInputStream(wrappedin);
	}
	@Override
	public long getContentLength(){
		// length of ungzipped content is not known
		return -1;
	}
}
class DeflaterDecompressingEntity extends HttpEntityWrapper{
	public DeflaterDecompressingEntity(final HttpEntity entity){
		super(entity);
	}
	@Override
	public InputStream getContent() throws IOException,IllegalStateException{
		// the wrapped entity's getContent() decides about repeatability
		InputStream wrappedin=wrappedEntity.getContent();
		return new InflaterInputStream(wrappedin);
	}
	@Override
	public long getContentLength(){
		// length of unzipped content is not known
		return -1;
	}
}