/**
 *
 */
package simple.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

/**
 * Used? 2015-1-1
 * <br>Created: Jun 19, 2008
 * @author Kenneth Pierce
 */
public final class do_net {
	private do_net() {};
	/**
	 * Converts the input stream to the appropriate inflater if needed.
	 * @param con
	 * @return an input stream
	 * @throws IOException
	 */
	public static InputStream getInputStream(HttpURLConnection con) throws IOException {
		String encoding = con.getHeaderField("Content-Encoding");
		if (encoding == null){
			return new BufferedInputStream(con.getInputStream());
		}
		if ("gzip".equals(encoding) || "x-gzip".equals(encoding)){
			return new GZIPInputStream(new BufferedInputStream(con
					.getInputStream()));
		}
		if ("deflate".equals(encoding)){
			return new InflaterInputStream(new BufferedInputStream(
					con.getInputStream()));
		}
		return new BufferedInputStream(con.getInputStream());
	}
}
