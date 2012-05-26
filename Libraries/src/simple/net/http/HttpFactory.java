/**
 *
 */
package simple.net.http;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URL;

import simple.io.FileUtil;
import simple.net.HttpConnectionController;

/**Common HTTP functions.<br>
 * Other dependencies:<br>
 * HTTPClient.HttpURLConnection<br>
 * simple.net.HttpConnectionController<br>
 * simple.util.do_io<br>
 * <br>Created: Oct 21, 2010
 * @author Kenneth Pierce
 */
public final class HttpFactory {
	/**Fully reads a HTTP page into <code>store</code>. Will try 10 times to
	 * connect and will wait 10 seconds between each try. Defaults to the UTF-8
	 * charset.
	 * @param link URL pointing to the page.
	 * @param referer URL of the page that this link was found on or NULL.
	 * @param store Object in which to store the read data.
	 * @return <code>true</code> on success, <code>false</code> otherwise.
	 * @throws IOException
	 */
	public static final boolean readPage(final URL link, final URL referer, final StringBuilder store) throws IOException {
		int max_count,
			//used to see if we got all the content
			cur_count;
		InputStreamReader in = null;
		int retry = 0, start = 0;
		HttpConnectionController conCont = new HttpConnectionController(link);
		conCont.setMaxConnectRetry(10);
		conCont.setRetryConnectDelay(10);
		if (referer != null)
			conCont.setRequestProperty("Referer", referer.toString());

		if (!conCont.connect()) {
			switch (conCont.getLastError().getResponseCode()) {
			case -1:
				break;
			case 200:
			case java.net.HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
			case java.net.HttpURLConnection.HTTP_RESET:
			case java.net.HttpURLConnection.HTTP_INTERNAL_ERROR:
			}
			return false;
		}
		max_count = conCont.getContentLength();
		final char[] buff = new char[1024];
		//long oldSize = 0;
		for (retry = 0; retry < 3; retry++) {
			final InputStream inT = conCont.getInputStream();
			if (inT == null) {
				switch (conCont.getLastError().getResponseCode()) {
				case -1:
					break;
				case 200:
				case java.net.HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
				case java.net.HttpURLConnection.HTTP_RESET:
				case java.net.HttpURLConnection.HTTP_INTERNAL_ERROR:
				}
				return false;
			}
			in = new InputStreamReader(inT, "UTF-8");
			try {
				cur_count = 0;
				if (max_count > -1) {
					while ((start = in.read(buff)) != -1) {
						store.append(buff, 0, start);
						cur_count += start;
					}
				} else {
					while ((start = in.read(buff)) != -1) {
						store.append(buff, 0, start);
						cur_count += start;
					}
				}
				break;
			} catch (final SocketTimeoutException e) {
				e.printStackTrace();
				store.delete(0, store.length());
			} catch (final EOFException e) {
				e.printStackTrace();
				store.delete(0, store.length());
			} catch (final IOException e) {
				FileUtil.close(in);
				conCont.disconnect();
				e.printStackTrace();
				return false;
			}
			FileUtil.close(in);
			conCont.disconnect();
			if (retry < 2) {
				conCont = new HttpConnectionController(link);
				conCont.setMaxConnectRetry(10);
				conCont.setRetryConnectDelay(10);
			}
		}
		FileUtil.close(in);
		if (retry == 3) {
			switch (conCont.getLastError().getResponseCode()) {
			case -1:
				break;
			case java.net.HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
			case java.net.HttpURLConnection.HTTP_RESET:
			case java.net.HttpURLConnection.HTTP_INTERNAL_ERROR:
			case 200:
			}
			return false;
		}
		return true;
	}
}
