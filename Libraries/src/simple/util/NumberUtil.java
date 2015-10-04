/**
 *
 */
package simple.util;

/**
 * Could be smaller, but trying to keep the up-converts to a minimum.
 * @author Kenneth Pierce
 */
public final class NumberUtil{
	private NumberUtil(){}
	/**
	 * Convenience for n greater than s and  n less than e
	 * @param n number being compared
	 * @param s start
	 * @param e end
	 * @return
	 */
	public static boolean between(long n, long s, long e){
		return n > s && n < e;
	}
	/**
	 * Convenience for n greater than s and  n less than e
	 * @param n number being compared
	 * @param s start
	 * @param e end
	 * @return
	 */
	public static boolean between(int n, int s, int e){
		return n > s && n < e;
	}
	/**
	 * Convenience for n greater than s and  n less than e
	 * @param n number being compared
	 * @param s start
	 * @param e end
	 * @return
	 */
	public static boolean between(short n, short s, short e){
		return n > s && n < e;
	}
	/**
	 * Convenience for n greater than s and  n less than e
	 * @param n number being compared
	 * @param s start
	 * @param e end
	 * @return
	 */
	public static boolean between(byte n, byte s, byte e){
		return n > s && n < e;
	}
	/**
	 * Convenience for n greater than s and  n less than e
	 * @param n number being compared
	 * @param s start
	 * @param e end
	 * @return
	 */
	public static boolean between(int n, short s, short e){
		return n > s && n < e;
	}
	/**
	 * Convenience for n greater than s and  n less than e
	 * @param n number being compared
	 * @param s start
	 * @param e end
	 * @return
	 */
	public static boolean between(int n, byte s, byte e){
		return n > s && n < e;
	}
	/**
	 * Convenience for n greater than s and  n less than e
	 * @param n number being compared
	 * @param s start
	 * @param e end
	 * @return
	 */
	public static boolean between(short n, int s, int e){
		return n > s && n < e;
	}
	/**
	 * Convenience for n greater than s and  n less than e
	 * @param n number being compared
	 * @param s start
	 * @param e end
	 * @return
	 */
	public static boolean between(byte n, int s, int e){
		return n > s && n < e;
	}
	/**
	 * Convenience for n greater than s and  n less than e
	 * @param n number being compared
	 * @param s start
	 * @param e end
	 * @return
	 */
	public static boolean between(double n, double s, double e){
		return n > s && n < e;
	}
	/**
	 * Convenience for n greater than s and  n less than e
	 * @param n number being compared
	 * @param s start
	 * @param e end
	 * @return
	 */
	public static boolean between(float n, float s, float e){
		return n > s && n < e;
	}
}
