/**
 * 
 */
package simple.util;

import java.util.Properties;

/**
 * <hr>
 * <br>Created: May 3, 2011
 * @author Kenneth Pierce
 */
public final class Options extends Properties {
	private static final long serialVersionUID = 1862292636191681990L;

	/**
	 * 
	 */
	public Options() {}

	/**
	 * @param arg0
	 * @see java.util.Properties#Properties(Properties)
	 */
	public Options(Properties defaults) {
		super(defaults);
	}

}
