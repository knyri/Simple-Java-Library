/**
 * 
 */
package simple.ml;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.ParseException;

import simple.net.Uri;
import simple.util.logging.*;

/**Tests {@link InlineLooseParser}
 * <br>Created: Oct 21, 2010
 * @author Kenneth Pierce
 */
public class ParserTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException, URISyntaxException {
		System.out.println(new Uri("http://www.jaja.com").hashCode());
		System.out.println(new Uri("http://www.jaja.com").hashCode());
		System.out.println(new Uri("http://www.jaja.com#56").hashCode());
		LogFactory.setGlobalLogFile(new File("parserteser.log"), false);
		Log log = LogFactory.getLogFor(ParserTester.class);
		StringBuilder src = new StringBuilder(600);
		try {
			//do_io.readInto(do_io.openInputResource("mimeTypes.conf.xml", null), src);
			simple.net.http.HttpFactory.readPage(
					new java.net.URL("http://www.megatokyo.com/rss/megatokyo.xml")
					//new java.net.URL("http://www.miscarchives.com")
					//new java.net.URL("http://www.google.com")
					//new java.net.URL("http://youporn.com/watch/486595/trick-or-treat-happy-halloween-pumpkin-fucking/?from=halloween")
					, null, src);/* */
			log.information(src);
		} catch (MalformedURLException e) {
			log.error(e);
			System.exit(1);
		} catch (IOException e) {
			log.error(e);
			System.exit(1);
		}
		Page page = null;
		try {
			page = InlineLooseParser.parse(src);
		} catch (ParseException e) {
			log.error(e);
			System.exit(0);
		}
		if (page != null)
			System.out.println(page.toString());
		System.exit(0);
	}

}
