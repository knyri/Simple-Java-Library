package simple.parser.ml;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import simple.parser.ml.impl.DefaultPageFormatter;

/**
 * Formatter for {@link Page}s.
 *
 * @author Kenneth Pierce
 * @see DefaultPageFormatter
 */
public abstract class PageFormatter {
	protected final String EOL;
	public PageFormatter(){
		this(System.getProperty("line.separator"));
	}
	/**
	 * @param eol End of line string
	 */
	public PageFormatter(String eol){
		EOL= eol;
	}

	/**
	 * @param page The page to format
	 * @return A string containing the formatted page
	 */
	public final String format(Page page) {
		StringWriter buf= new StringWriter(24000);
		try{
			writeTo(page, buf);
		}catch(IOException e){
			return null;
		}
		return buf.toString();
	}
	/**
	 * Writes the formatted page to the writer.
	 * Passes all the root tags to {@link #writeTo(Tag, Writer)}
	 * @param page Page to format
	 * @param out Stream to write the formatted page to
	 * @throws IOException If an error occurs while writing
	 */
	public final void writeTo(Page page, Writer out) throws IOException{
		for(Tag tag : page.getRoots()){
			writeTo(tag, out);
		}
	}

	/**
	 * @param tag The tag to start from
	 * @return The formatted version of this tag
	 */
	public final String format(Tag tag) {
		StringWriter buf= new StringWriter(24000);
		try{
			writeTo(tag, buf);
		}catch(IOException e){
			return null;
		}
		return buf.toString();
	}
	/**
	 * All other functions call this one.
	 * @param tag Tag to start from
	 * @param out Stream to write the formatted page to
	 * @throws IOException If an error occurs while writing
	 */
	public abstract void writeTo(Tag tag, Writer out) throws IOException;
}
