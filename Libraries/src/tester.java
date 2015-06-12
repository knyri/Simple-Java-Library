import java.io.IOException;

import simple.NumberIterator;
import simple.io.StaticPrinter;
import simple.util.do_math;

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
		String regex="(^hotspot$)|(^hotspot .*)|(.* hotspot$)|(.* hotspot .*)";
		System.out.println(" hotspot ".matches(regex));
		System.out.println("hotspot ".matches(regex));
		System.out.println(" hotspot".matches(regex));
		System.out.println("hotspot".matches(regex));
		System.out.println("asd hotspot asd".matches(regex));
		do_mathRound();
	}
	static void do_mathRound(){
		System.out.println(do_math.round(1.002, 2));
		System.out.println(do_math.round(10.002, 2));
		System.out.println(do_math.round(1.005, 2));
		System.out.println(do_math.round(1.995, 2));
		System.out.println(do_math.round(1.9949, 2));
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
