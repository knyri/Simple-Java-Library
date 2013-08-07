import java.io.IOException;

import simple.NumberIterator;
import simple.io.StaticPrinter;

/**
 *
 */

/**
 * <br>Created: Jul 9, 2010
 * @author Kenneth Pierce
 */
public final class tester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NumberIterator iter = new NumberIterator("[011...45,3]");
		System.out.println(iter.toString());
		while (iter.hasNext())
			System.out.println(iter.next());
		try {
			StaticPrinter.print(iter, ',', System.out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		{
			String s = "http://96a3db6c.linkbucks.com/url/http://anonib.com/123cp/src/127825369331.jpg";
			System.out.println(s.substring(s.indexOf("url/")+4));
		}
	}

}
