/**
 *
 */
package simple.net;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import HTTPClient.CIHashtable;
import HTTPClient.HTTPConnection;
import HTTPClient.HTTPResponse;
import HTTPClient.ModuleException;
import HTTPClient.NVPair;
import HTTPClient.ProtocolNotSuppException;

/**
 * The name is a bit misleading. This class only eases the process of catching
 * and handling errors while performing operations. One day I may expand it to
 * meet its name. <br>
 * Relies heavily on the HTTPClient package.
 * <br>
 * Created: Jun 19, 2008
 *
 * @author Kenneth Pierce
 */
public class HttpConnectionController {
	/**
	 * Stores previously created connections for reuse.
	 */
	private final static Hashtable<String, HTTPConnection> cache = new Hashtable<String, HTTPConnection>();
	private final Object waiter = new Object();
	private final Object sync = new Object();
	private HTTPConnection con;
	private HTTPResponse resp;
	private final Uri uri;
	private final Vector<HttpConnectionListener> listeners = new Vector<HttpConnectionListener>();
	private final CIHashtable request = new CIHashtable();
	// private Map<String, List<String>> headers;
	private int maxConnectRetry = 3, maxTimeoutRetry = 3,
	retryConnectDelay = 15, retryTimeoutDelay = 15, contentlength = -1;

	private boolean keepAlive = false, resumeable = false, connect = false;
	private String contenttype = null;
	private HttpConnectionEvent lastError = null;
	private int responseCode = 0;
	private int method = METHOD_GET;
	public static final int METHOD_GET = 0, METHOD_HEAD = 1;
	/** Sets the method to get or head. Post is not supported at this time.
	 * @param method {@link #METHOD_GET} or {@link #METHOD_HEAD}
	 */
	public void setMethod(final int method) {
		this.method = method;
	}
	public HttpConnectionController(final URL url) throws ProtocolNotSuppException {
		this.uri = new Uri(url);
		final String host = url.getHost().toLowerCase();
		con = cache.get(host);
		if (con==null) {
			con = new HTTPConnection(url);
			con.setTimeout(30000);
			cache.put(host, con);
		}
	}

	public void addHttpConnectionListener(final HttpConnectionListener listener) {
		listeners.add(listener);
	}

	public void removeHttpConnectionListener(final HttpConnectionListener listener) {
		listeners.remove(listener);
	}

	/** Sends a message to the listeners.
	 * @param msg
	 */
	private final void sendMessage(final String msg) {
		final HttpConnectionEvent e = new HttpConnectionEvent(this, msg);
		setLastError(e);
		for (final HttpConnectionListener lis : listeners) {
			lis.handleHttpStatusUpdate(e);
		}
	}

	/** Sends a message to the listeners.
	 * @param msg
	 * @param responseCode
	 */
	private final void sendMessage(final String msg, final int responseCode) {
		final HttpConnectionEvent e = new HttpConnectionEvent(this, msg, responseCode);
		setLastError(e);
		for (final HttpConnectionListener lis : listeners) {
			lis.handleHttpStatusUpdate(e);
		}
	}

	/** Sends a message to the listeners.
	 * @param msg
	 * @param ex
	 * @param responseCode
	 */
	private final void sendError(final String msg, final Exception ex, final int responseCode) {
		final HttpConnectionEvent e = new HttpConnectionEvent(this, msg, ex,
				responseCode);
		setLastError(e);
		for (final HttpConnectionListener lis : listeners) {
			lis.handleHttpError(e);
		}
	}

	/**
	 * Disconnects from the server.
	 */
	public final void disconnect() {
		synchronized (waiter) {
			waiter.notifyAll();
		}
		con.stop();
		try {
			resp.getInputStream().close();
		} catch (final Exception e) {}
		connect = false;
	}

	/**
	 * @return the underlying HTTPConnection
	 */
	public HTTPConnection getConnection() {
		return con;
	}

	public HttpConnectionEvent getLastError() {
		synchronized (sync) {
			return lastError;
		}
	}

	/** sets the most recent error
	 * @param e
	 */
	private void setLastError(final HttpConnectionEvent e) {
		synchronized (sync) {
			lastError = e;
			sync.notifyAll();
		}
	}

	/** Attempts to connect to the server. Usually not needed since
	 * all function requiring a connection automatically call this.
	 * @return true on success.
	 */
	public boolean connect() {
		if (connect)
			return true;
		int retry = 0;
		int errorCode;
		try {
			do {
				try {
					sendMessage("Connecting...("+retry + "/" + maxConnectRetry+")", -1);
					if (request.isEmpty()) {
						if (!uri.getQuery().isEmpty()) {
							switch (method) {
							case METHOD_GET:
								resp = con.Get(uri.getPath() + uri.getFile()+"?"+uri.getQuery());
								break;
							case METHOD_HEAD:
								resp = con.Head(uri.getPath() + uri.getFile()+"?"+uri.getQuery());
								break;
							}
						} else {
							switch (method) {
							case METHOD_GET:
								resp = con.Get(uri.getPath() + uri.getFile());
								break;
							case METHOD_HEAD:
								resp = con.Head(uri.getPath() + uri.getFile());
								break;
							}
						}
					} else {
						final NVPair[] req = new NVPair[request.size()];
						final Iterator<Map.Entry<CharSequence, String>> entrys = request.entrySet().iterator();
						Map.Entry<CharSequence, String> entry;
						for (int i = 0; i < req.length; i++) {
							entry = entrys.next();
							req[i] = new NVPair(entry.getKey().toString(), entry.getValue());
						}
						if (!uri.getQuery().isEmpty()) {
							switch(method) {
							case METHOD_GET:
								resp = con.Get(uri.getPath() + uri.getFile()+"?"+uri.getQuery(), (String)null, req);
								break;
							case METHOD_HEAD:
								resp = con.Head(uri.getPath() + uri.getFile()+"?"+uri.getQuery(), (String)null, req);
								break;
							}
						} else {
							switch (method) {
							case METHOD_GET:
								resp = con.Get(uri.getPath() + uri.getFile(), (String)null, req);
								break;
							case METHOD_HEAD:
								resp = con.Head(uri.getPath() + uri.getFile(), (String)null, req);
								break;
							}
						}
					}
					// headers = con.getHeaderFields();
					//sendMessage(resp.getEffectiveURI().toString(), -1);
					//sendMessage(request.toString(), -1);
					//sendMessage(resp.toString(), -1);
					connect = true;
					contentlength = resp.getHeaderAsInt("Content-Length");
					contenttype = resp.getHeader("Content-Type");
					if ("bytes".equals(resp.getHeader("Accept-Ranges"))) {
						resumeable = true;
					} else {
						resumeable = false;
					}
					if ("close".equals(resp.getHeader("Connection"))) {
						keepAlive = false;
					} else {
						keepAlive = true;
					}
					responseCode = resp.getStatusCode();
					return true;
				} catch (final SocketTimeoutException e) {
					retry++;
					sendMessage("Timed out connecting. Waiting "
							+ retryConnectDelay + " seconds. Try " + retry
							+ " of " + maxConnectRetry, 200);
					synchronized (waiter) {
						try {
							waiter.wait(retryConnectDelay * 1000);
						} catch (final InterruptedException e1) {
							sendError("Unhandled error while waiting.", e1, -1);
							break;
						}
					}
				} catch (final ConnectException e) {
					retry++;
					sendError(e.getMessage() + ". Waiting 20 seconds. Try "
							+ retry + " of " + maxConnectRetry, e, 200);
					synchronized (waiter) {
						try {
							waiter.wait(retryConnectDelay * 2000);
						} catch (final InterruptedException e1) {
							sendError("Unhandled error while waiting.", e1, -1);
							break;
						}
					}
				} catch (final UnknownHostException e) {
					sendError("Host could not be resolved. "+uri, e, -1);
					return false;
				} catch (final IOException e) {
					if (resp != null ) {
						try {
							errorCode = resp.getStatusCode();
						} catch (final ModuleException e2) {
							errorCode = -1;
							sendError("Unhandled error while connecting.", e2,
									errorCode);
						}
						if (errorCode == HttpURLConnection.HTTP_INTERNAL_ERROR) {
							retry++;
							sendMessage("Server error while connecting. Waiting "
									+ retryConnectDelay + " seconds. Try " + retry
									+ " of " + maxConnectRetry, errorCode);
							synchronized (waiter) {
								try {
									waiter.wait(retryConnectDelay * 1000);
								} catch (final InterruptedException e1) {
									sendError("Unhandled error while waiting.", e1,
											-1);
									break;
								}
							}
						} else {
							sendError("Unhandled error while connecting.", e,
									errorCode);
							break;
						}
					} else {
						sendError("Unhandled fatal error", e, -1);
					}
				} catch (final ModuleException e) {
					sendError("Unhandled error while connecting.", e, -1);
				}
			} while (retry < maxConnectRetry);
		} catch (final IOException e) {
			// should never reach
			sendError("Unhandled error while connecting", e, -1);
			e.printStackTrace();
		} catch (final Exception e) {
			sendError("Unhandled error while connecting.", e, -1);
		}
		return false;
	}
	/**
	 * @return The effective URI for the original URI.
	 * @throws IOException If the connection failed
	 * @throws ModuleException if any module encounters an exception
	 */
	public String getEffectiveURI() throws IOException, ModuleException {
		if (!connect())
			throw new IOException("Could not get header: connection failed");
		return resp.getEffectiveURI().toString();
	}
	/**Get's the HTTP response code.<br>
	 * NOTE: connects if not already
	 * @return The HTTP response code
	 * @throws IOException if the connection failed
	 */
	public int getResponseCode() throws IOException {
		if (!connect)
			if (!connect())
				throw new IOException("Could not get header: connection failed");
		return responseCode;
	}

	/**Get's the contents length from the header.<br>
	 * NOTE: connects if not already
	 * @return The content length
	 * @throws IOException if the connection failed
	 */
	public int getContentLength() throws IOException {
		if (!connect)
			if (!connect())
				throw new IOException("Could not get header: could not connect");
		return contentlength;
	}

	/**Gets the value mapped to the response header<br>
	 * NOTE: connects if not already
	 * @param key response header name
	 * @return the mapped value or null
	 * @throws IOException if the connection failed
	 * @throws ModuleException if any module encounters an error
	 */
	public String getResponseHeader(final String key) throws IOException,
	ModuleException {
		if (!connect)
			if (!connect())
				throw new IOException("Could not get header: could not connect");
		return resp.getHeader(key);
	}

	/**Gets the mapped value as an integer<br>
	 * NOTE: connects if not already
	 * @param key header key
	 * @return the mapped value
	 * @throws IOException if connection fails
	 * @throws NumberFormatException if the header is not a number
	 * @throws ModuleException if any module encounters an error
	 */
	public int getResponseHeaderAsInt(final String key) throws IOException,
	NumberFormatException, ModuleException {
		if (!connect)
			if (!connect())
				throw new IOException("Could not get header: could not connect");
		return resp.getHeaderAsInt(key);
	}

	/**<br>
	 * NOTE: connects if not already
	 * @return an Enumeration of the header keys.
	 * @throws IOException on connect failure
	 * @throws ModuleException
	 */
	public Enumeration<CharSequence> getResponseHeaderKeys() throws IOException,
	ModuleException {
		if (!connect)
			if (!connect())
				throw new IOException(
				"Could not get headers: could not connect");
		return resp.listHeaders();
	}

	public void setRequestProperty(final String name, final String value) {
		request.put(name, value);
	}

	public Enumeration<CharSequence> getRequestHeaderKeys() {
		return request.keys();
	}

	public String getRequestHeader(final String key) {
		return request.get(key);
	}
	public NVPair[] getRequestHeaderDefaults() {
		return con.getDefaultHeaders();
	}
	public String getRequestHeaders() {
		return request.toString();
	}
	public String getResponseHeaders() {
		return resp.toString();
	}
	/**Gets the content MIME type from the header.<br>
	 * NOTE: connects if not already.
	 * @return The content MIME type
	 * @throws IOException If the connection failed
	 */
	public String getContentType() throws IOException {
		if (!connect)
			if (!connect())
				throw new IOException("Could not get header: could not connect");
		return contenttype;
	}

	/**
	 * Returns a buffered InputStream to the site. Will automatically convert to
	 * a GZIPInputStream or DeflaterInputStream as needed. Cannot handle
	 * "compress" yet.
	 *
	 * @return a buffered input stream from the server.
	 */
	public InputStream getInputStream() {
		if (!connect)
			if (!connect())
				return null;
		int retry = 0;
		int errorCode;
		String encoding;
		try {
			do {
				sendMessage("Getting input stream...", -1);
				try {
					encoding = resp.getHeader("Content-Encoding");
					if (encoding == null)
						return new BufferedInputStream(resp.getInputStream());
					if ("gzip".equals(encoding) || "x-gzip".equals(encoding))
						return new GZIPInputStream(new BufferedInputStream(resp
								.getInputStream()));
					if ("deflate".equals(encoding))
						return new InflaterInputStream(new BufferedInputStream(
								resp.getInputStream()));
					return new BufferedInputStream(resp.getInputStream());
				} catch (final SocketTimeoutException e) {
					retry++;
					sendMessage(
							"Timed out connecting for input stream. Waiting "
							+ retryConnectDelay + " seconds. Try "
							+ retry + " of " + maxConnectRetry, 200);
					synchronized (waiter) {
						try {
							waiter.wait(retryConnectDelay * 1000);
						} catch (final InterruptedException e1) {
							sendError("Unhandled error while waiting.", e1, -1);
							break;
						}
					}
				} catch (final ConnectException e) {
					retry++;
					sendError(
							e.getMessage()
							+ " while getting input stream. Waiting 20 seconds. Try "
							+ retry + " of " + maxConnectRetry, e, 200);
					synchronized (waiter) {
						try {
							waiter.wait(retryConnectDelay * 2000);
						} catch (final InterruptedException e1) {
							sendError("Unhandled error while waiting.", e1, -1);
							break;
						}
					}
				} catch (final IOException e) {
					try {
						errorCode = resp.getStatusCode();
					} catch (final ModuleException e2) {
						errorCode = -1;
						sendError("Unhandled error while connecting.", e2, -1);
						e2.printStackTrace();
					}
					if (errorCode == HttpURLConnection.HTTP_INTERNAL_ERROR) {
						retry++;
						sendMessage(
								"Server error while connecting for input stream. Waiting "
								+ retryConnectDelay + " seconds. Try "
								+ retry + " of " + maxConnectRetry,
								errorCode);
						synchronized (waiter) {
							try {
								waiter.wait(retryConnectDelay * 1000);
							} catch (final InterruptedException e1) {
								sendError("Unhandled error while waiting.", e1,
										-1);
								break;
							}
						}
					} else {
						sendError(
								"Unhandled error while connecting for input stream.",
								e, errorCode);
						break;
					}
				} catch (final ModuleException e) {
					sendError("Unhandled error while connecting.", e, -1);
				}
			} while (retry < maxConnectRetry);
		} catch (final IOException e) {
			// should never reach
			sendError("Unhandled error while connecting for input stream.", e,
					-1);
			e.printStackTrace();
		}
		return null;
	}

	/** Reads the input into the specified stream.
	 * @param os
	 * @return true on success.
	 */
	@SuppressWarnings("resource")
	public boolean readInto(final OutputStream os) {
		if (!connect)
			if (!connect())
				return false;
		InputStream in = null;
		final byte[] b = new byte[1024];
		int length = 0;
		for (int retry = 0; retry < maxTimeoutRetry; retry++) {
			if (in != null) {
				try {
					in.close();
				} catch (final IOException e2) {
					e2.printStackTrace();
				}
			}
			in = getInputStream();
			sendMessage("Reading...", -1);
			try {
				while ((length = in.read(b)) > 0) {
					os.write(b, 0, length);
				}
				return true;
			} catch (final SocketTimeoutException e) {
				sendMessage("Timed out reading. Waiting " + retryTimeoutDelay
						+ " seconds. Try " + retry + " of " + maxTimeoutRetry);
				synchronized (waiter) {
					try {
						waiter.wait(retryTimeoutDelay * 1000);
					} catch (final InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			} catch (final IOException e) {
				sendError("Error while reading.", e, -1);
				break;
			}
		}
		return false;
	}

	@SuppressWarnings("resource")
	public boolean readInto(final Writer wr) {
		if (!connect)
			if (!connect())
				return false;
		InputStream in = null;
		final byte[] b = new byte[1024];
		short bCount = 0;
		int length = 0;
		for (int retry = 0; retry < maxTimeoutRetry; retry++) {
			if (in != null) {
				try {
					in.close();
				} catch (final IOException e2) {
					e2.printStackTrace();
				}
			}
			in = getInputStream();
			try {
				while ((length = in.read(b)) > 0) {
					for (bCount = 0; bCount < length; bCount++) {
						wr.write((char) b[bCount]);
					}
				}
				return true;
			} catch (final SocketTimeoutException e) {
				sendMessage("Timed out reading. Waiting " + retryTimeoutDelay
						+ " seconds. Try " + retry + " of " + maxTimeoutRetry);
				synchronized (waiter) {
					try {
						waiter.wait(retryTimeoutDelay * 1000);
					} catch (final InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			} catch (final IOException e) {
				sendError("Error while reading.", e, -1);
				break;
			}
		}
		return false;
	}

	/**
	 * @return the max number of times to retry
	 */
	public int getMaxConnectRetry() {
		return maxConnectRetry;
	}

	/**
	 * @param maxConnectRetry
	 *            the max number of times to retry
	 */
	public void setMaxConnectRetry(final int maxConnectRetry) {
		this.maxConnectRetry = maxConnectRetry;
	}

	/**
	 * @return the max number of times to retry
	 */
	public int getMaxTimeoutRetry() {
		return maxTimeoutRetry;
	}

	/**
	 * @param maxTimeoutRetry
	 *            the max number of times to retry
	 */
	public void setMaxTimeoutRetry(final int maxTimeoutRetry) {
		this.maxTimeoutRetry = maxTimeoutRetry;
	}

	/**
	 * @return the number of seconds to wait before retrying
	 */
	public int getRetryConnectDelay() {
		return retryConnectDelay;
	}

	/**
	 * @param retryConnectDelay
	 *            the number of seconds to wait before retrying
	 */
	public void setRetryConnectDelay(final int retryConnectDelay) {
		this.retryConnectDelay = retryConnectDelay;
	}

	/**
	 * @return the number of seconds to wait before retrying
	 */
	public int getRetryTimeoutDelay() {
		return retryTimeoutDelay;
	}

	/**
	 * @param retryTimeoutDelay
	 *            the number of seconds to wait before retrying
	 */
	public void setRetryTimeoutDelay(final int retryTimeoutDelay) {
		this.retryTimeoutDelay = retryTimeoutDelay;
	}

	/**
	 * @return True if the connection will remain open until the server timeout
	 *         is reached or is manually closed.
	 */
	public boolean isKeepAlive() {
		return keepAlive;
	}

	/**
	 * @return True if the download is resumable. This may not be accurate.
	 */
	public boolean isResumeable() {
		return resumeable;
	}
}
