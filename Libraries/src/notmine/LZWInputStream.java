/**
 * 
 */
package notmine;

import java.io.InputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

/**Replaces LZW.
 * <br>Created: Sep 7, 2008
 * @author Kenneth Pierce
 */
public class LZWInputStream extends InflaterInputStream {

	/**
	 * @param arg0
	 */
	public LZWInputStream(InputStream arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public LZWInputStream(InputStream arg0, Inflater arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	public LZWInputStream(InputStream arg0, Inflater arg1, int arg2) {
		super(arg0, arg1, arg2);
	}

}
