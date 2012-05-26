/**
 * 
 */
package simple.net;

/** For use with HttpConnectionController.
 * <br>Created: Jun 19, 2008
 * @author Kenneth Pierce
 * @see simple.net.HttpConnectionController
 * @see simple.net.HttpConnectionEvent
 */
public interface HttpConnectionListener {
	public void handleHttpError(HttpConnectionEvent e);
	public void handleHttpStatusUpdate(HttpConnectionEvent e);
}
