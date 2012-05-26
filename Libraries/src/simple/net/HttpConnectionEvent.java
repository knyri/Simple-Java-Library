package simple.net;

import java.util.EventObject;

/** For use with HttpConnectionController.
 * <br>Created: Jun 19, 2008
 * @author Kenneth Pierce
 * @see simple.net.HttpConnectionController
 * @see simple.net.HttpConnectionListener
 */
public class HttpConnectionEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	public static final int TYPE_INFO = 1, TYPE_ERROR = 2;
	private final int type;
	private final String message;
	private final Exception e;
	private final int errorCode;
	public HttpConnectionEvent (HttpConnectionController source, String message) {
		super(source);
		type = TYPE_INFO;
		this.message = message;
		e = null;
		errorCode = -1;
	}
	public HttpConnectionEvent (HttpConnectionController source, String message, int responseCode) {
		super(source);
		type = TYPE_INFO;
		this.message = message;
		e = null;
		errorCode = responseCode;
	}
	public HttpConnectionEvent (HttpConnectionController source, String message, Exception e) {
		super(source);
		type = TYPE_ERROR;
		this.e = e;
		this.message = message;
		errorCode = -1;
	}
	public HttpConnectionEvent (HttpConnectionController source, String message, Exception e, int responseCode) {
		super(source);
		type = TYPE_ERROR;
		this.e = e;
		this.message = message;
		errorCode = responseCode;
	}
	public int getType() {
		return type;
	}
	public int getResponseCode() {
		return errorCode;
	}
	public Exception getError() {
		return e;
	}
	public String getMessage() {
		return message;
	}
}
