/**
 *
 */
package simple.gui;

/**
 * <hr>
 * <br>Created: Dec 3, 2010
 * @author Kenneth Pierce
 */
public final class Util {
	/**
	 * Tests to see if the on bits in <var>option</var> are also on in <var>options</var>.
	 * @param options Options the user set.
	 * @param option Option to be tested.
	 * @return The result of <code>(options&amp;option)==option</code>
	 */
	public static boolean isSet(int options, int option) {
		return (options&option)==option;
	}
}
