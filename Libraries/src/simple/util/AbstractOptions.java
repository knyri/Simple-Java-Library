/**
 *
 */
package simple.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

/**
 * How to use: Set the default options, {@link #DEFAULTS}, using either the
 * instance block or the static block. <br>
 * Created: Oct 21, 2009
 *
 * @author Kenneth Pierce
 */
public abstract class AbstractOptions {
	protected final static Properties DEFAULTS = new Properties();

	public final static String TRUE = "true", FALSE = "false";

	protected Properties options;

	/**
	 * Instantiates {@link #options} and sets the default values from {@link #DEFAULTS}.
	 */
	public AbstractOptions() {
		options = new Properties(DEFAULTS);
	}

	/**
	 * Should be used to store boolean data.
	 *
	 * @param bool
	 * @return {@link #TRUE} or {@link #FALSE}
	 */
	protected final static String getTF(boolean bool) {
		return bool ? TRUE : FALSE;
	}

	/**
	 * Should be used to convert stored boolean data
	 * back into a boolean.
	 *
	 * @param bool
	 * @return <code>true</code> if {@link #TRUE}.equals(<var>bool</var>)
	 */
	protected final static boolean getTF(String bool) {
		return TRUE.equals(bool);
	}

	public final String setProperty(String key, String value) {
		return (String) options.setProperty(key, value);
	}

	public final String getProperty(String key) {
		return options.getProperty(key);
	}

	/**
	 * Saves {@link #options} to <var>file</var>
	 *
	 * @param file
	 * @throws IOException
	 *             On write error or if the file was not found.
	 */
	protected final void save(String file) throws IOException {
		try(PrintWriter out = new PrintWriter(new File(file))){
			options.store(out, "");
		}
	}

	/**
	 * Loads {@link #options} from <var>file</var>
	 *
	 * @param file
	 * @throws IOException
	 *             On read error or if the file was not found.
	 */
	protected final void load(String file) throws IOException {
		try(FileReader in = new FileReader(new File(file))){
			options.load(in);
		}
	}
}
