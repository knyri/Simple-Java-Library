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
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;

import simple.io.FileUtil;
import simple.net.Uri;
import simple.net.http.clientparams.ClientParam;
import simple.util.logging.Log;
import simple.util.logging.LogFactory;
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
	private BasicCookieStore cookies=new BasicCookieStore();
	private final HttpHost proxy;
	private final CloseableHttpClient client;
	public static final Header[] defaults=new Header[]{
		new BasicHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
		,new BasicHeader("Accept-Charset","ISO-8859-1,utf-8;q=0.7,*;q=0.3")
		,new BasicHeader("Accept-Encoding","gzip,deflate")
		,new BasicHeader("Accept-Language","en-US,en;q=0.8")
	};
	public static final String FORMAT_URLENCODED="application/x-www-form-urlencoded",FORMAT_FORMDATA="multipart/form-data";
	public void addCookie(org.apache.http.cookie.Cookie cookie){
		cookies.addCookie(cookie);
	}
	public void addCookies(org.apache.http.cookie.Cookie[] cookie){
		cookies.addCookies(cookie);
	}
	public BasicCookieStore getCookieStore(){
		return cookies;
	}
	public void saveCookies(File destination) throws FileNotFoundException, IOException{
		FileUtil.createFile(destination);
		ObjectOutputStream oos= new ObjectOutputStream(new FileOutputStream(destination));
		oos.writeObject(cookies);
		oos.close();
	}
	public void loadCookies(File source) throws FileNotFoundException, IOException, ClassNotFoundException{
		ObjectInputStream ois= new ObjectInputStream(new FileInputStream(source));
		cookies= (BasicCookieStore)ois.readObject();
		ois.close();
	}
	public HttpResponse get(Uri uri,Header[] headers) throws ClientProtocolException, IOException{
		return get(uri,headers,null);
	}
	public HttpResponse get(Uri uri,Header[] headers,HttpContext context) throws ClientProtocolException, IOException{
		HttpGet req=new HttpGet(uri.toString());
		req.setHeaders(defaults);
		HttpResponse response;
		if(headers!=null){
			for(Header header:headers)
				req.setHeader(header);
		}
		if(context==null)
			response=client.execute(req);
		else
			response=client.execute(req,context);
		log.debug("Request",req.getRequestLine().toString());
		log.debug("Request Headers",req.getAllHeaders());
		if(response.getStatusLine().getStatusCode()==301 || response.getStatusLine().getStatusCode()==302){// redirection
			Header location=response.getFirstHeader("Location");
			log.debug("Redirect",uri+" --TO-- "+location);
			if(location!=null) return get(new Uri(response.getFirstHeader("Location").getValue()),headers,context);
		}
		log.debug("Response",response);
		return response;
	}
	public HttpResponse get(Uri uri) throws ClientProtocolException, IOException{
		return get(uri,null,null);
	}
	public HttpResponse post(Uri uri,Header[] headers,ClientParam[] data,String format,HttpContext context) throws ClientProtocolException, IOException{
		HttpPost req=new HttpPost(uri.toString());
		HttpResponse response;
		req.setHeaders(defaults);
		// I assume this is proper enough
		String boundary=Integer.toHexString(uri.hashCode());
		if(headers!=null){
			for(Header header:headers)
				req.setHeader(header);
		}
		if(data!=null){
			if(FORMAT_URLENCODED.equals(format)){
				req.setHeader("Content-Type",format);
				req.setEntity(new UrlEncodedFormEntity(Arrays.asList(data)));
			}else if(FORMAT_FORMDATA.equals(format)){
				req.setHeader("Content-Type",format+"; boundary="+boundary);
				req.setEntity(new MultipartFormEntity(data,boundary));
			}
		}
		if(context==null)
			response=client.execute(req);
		else
			response=client.execute(req,context);
		log.debug("Request",req.getRequestLine().toString());
		log.debug("Request",req.getAllHeaders());
		if(response.getStatusLine().getStatusCode()==301 || response.getStatusLine().getStatusCode()==302){
			// redirection
			Header location=response.getFirstHeader("Location");
			log.debug("Redirect",uri+" --TO-- "+location);
			if(location!=null) return post(new Uri(response.getFirstHeader("Location").getValue()),headers,data,format,context);
		}
		log.debug("Response",response);
		return response;
	}
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
	public Client(String ProxyHost,int ProxyPort){
		proxy = new HttpHost(ProxyHost,ProxyPort);
		client=init().build();
	}
	public Client(){
		proxy=null;
		client=init().build();
	}
	/**
	 * Initializes the connection builder
	 */
	private HttpClientBuilder init(){
		HttpClientBuilder conBuilder= HttpClientBuilder.create();
		// Let the server know we want compressed if available
		conBuilder.addInterceptorFirst(
			new HttpRequestInterceptor(){
				@Override
				public void process(final HttpRequest request,final HttpContext context) throws HttpException,IOException{
					if(!request.containsHeader("Accept-Encoding")){
						request.addHeader("Accept-Encoding","gzip,deflate");
					}
				}
			});
		// Expand if compressed
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