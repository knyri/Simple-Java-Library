import java.io.IOException;
import java.util.Properties;

import simple.NumberIterator;
import simple.io.StaticPrinter;
import simple.util.do_math;
import simple.util.logging.Log;
import simple.util.logging.LogFactory;

/**
 *
 */

/**
 * <br>Created: Jul 9, 2010
 * @author Kenneth Pierce
 */
public final class tester {
	private static final Log log= LogFactory.getLogFor(tester.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		log.setPrintDate(false);
		log.setPrintTime(false);
		Properties env=System.getProperties();
//		Map<String, String> env = System.getenv();
        for (Object envName : env.keySet()) {
            System.out.format("%s=%s%n",
                              envName,
                              env.get(envName));
        }
		System.exit(0);
	}
	/**
	 * Best use
	 * append(char).append(str_literal)
	 * append(str).append(str_literal)
	 * append(str_literal + str_literal) : joined at compile time
	 * Avoid
	 * append(char + str_literal)
	 * append(str + str_literal)
	 * append(char).append(str_literal.toCharArray()) : toCharArray makes a copy of the internal array
	 */
	static void concatenationSpeed(){
		log.information("This tests the speed of multiple calls or string concatenation\nTimes are in milliseconds");
		final int runs= 5000000;
		log.information("Number of runs: "+runs);
		//Try to keep the JIT optimizer from optimizing
		final char[] letters= new char[128];
		final String[] letters_str= new String[128];

		for(int i= 0; i < 128; i++){
			letters[i]= (char)i;
			letters_str[i]= new String(new char[]{letters[i]});
		}
		//Make sure allocation isn't a factor
		StringBuilder buf = new StringBuilder(runs*4);
		long start;

		log.information("append(char).append(str_literal)");
		start= System.currentTimeMillis();
		for(int i = 0; i < runs; i++){
			buf.append(letters[i%128]).append("te");
		}
		log.information((System.currentTimeMillis() - start));
		buf.setLength(0);

		log.information("append(char + str_literal)");
		start= System.currentTimeMillis();
		for(int i = 0; i < runs; i++){
			buf.append(letters[i%128] + "te");
		}
		log.information((System.currentTimeMillis() - start));
		buf.setLength(0);

		log.information("append(char).append(str_literal.toCharArray())");
		start= System.currentTimeMillis();
		for(int i = 0; i < runs; i++){
			buf.append(letters[i%128]).append("te".toCharArray());
		}
		log.information((System.currentTimeMillis() - start));
		buf.setLength(0);

		log.information("append(str + str_literal)");
		start= System.currentTimeMillis();
		for(int i = 0; i < runs; i++){
			buf.append(letters_str[i%128] + "te");
		}
		log.information((System.currentTimeMillis() - start));
		buf.setLength(0);

		log.information("append(str).append(str_literal)");
		start= System.currentTimeMillis();
		for(int i = 0; i < runs; i++){
			buf.append(letters_str[i%128]).append("te");
		}
		log.information((System.currentTimeMillis() - start));
		buf.setLength(0);

		log.information("append(str_literal + str_literal)");
		start= System.currentTimeMillis();
		for(int i = 0; i < runs; i++){
			buf.append("]" + "te");
		}
		log.information((System.currentTimeMillis() - start));
		buf.setLength(0);
	}
	static void do_mathRound(){
		System.out.println(do_math.round(1.002, 2));
		System.out.println(do_math.round(10.002, 2));
		System.out.println(do_math.round(1.005, 2));
		System.out.println(do_math.round(1.995, 2));
		System.out.println(do_math.round(1.9949, 2));
		System.out.println(do_math.round(0, 2));
	}
	static void numberIterator(){
		NumberIterator iter = new NumberIterator("[011...45,3]");
		System.out.println(iter.toString());
		while (iter.hasNext())
			System.out.println(iter.next());
		try {
			StaticPrinter.print(iter, ',', System.out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
