/**
 * 
 */
package simple.util.logging;

/**Interface for retrieving error strings from error codes. 
 * <hr>
 * <br>Created: Nov 15, 2010
 * @author Kenneth Pierce
 */
public interface ErrorCodeResolver {
	public String getErrorString(int code);
}
