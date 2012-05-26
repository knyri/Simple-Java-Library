package simple.util;

import java.util.regex.*;

/**
 * This class is still in its infancy. It will grow as my needs expand.<br>
 * <br>Created: 2008
 * @author KP
 */
public final class NetUtils {
	public static String HTTP_HEADER_MOZILLA = "GET / HTTP/1.1\n"+
	"Accept: */*\n"+
	"Accept-Language: en-us\n"+
	"Accept-Encoding: deflate\n"+
	"User-Agent: Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; Maxthon; .NET CLR 1.1.4322; .NET CLR 2.0.50727)\n"+
	"Host: 127.0.0.1:8000\n"+
	"Connection: Close\n\n";
	public static boolean isValidURL(String url) {
		return Pattern.matches("http://[[\\w\\-]*\\.]*[\\w]{3}.*", url);
	}
}
