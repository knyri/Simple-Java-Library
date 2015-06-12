package simple.util;

/**
 * <br>Created: 2006
 * @author Kenneth Pierce
 */
public final class do_math {
	/** Rounds half up.
	 * Does not yet do an exhaustive search. Meaning 1.445
	 * rounded to 1 place equals 1.4 instead of 1.5
	 * @param d
	 * @param places
	 * @return
	 */
	public static String round(double d, int places){
		if(d == 0.0)return "0";
		StringBuilder ret=new StringBuilder(Double.toString(d));
		if(ret.length()<places+3)return ret.toString();
		int dot= ret.indexOf(".");
		if(dot==-1 || dot+places+2 > ret.length()) return ret.toString();
		if(ret.charAt(dot+places+1) > '4'){
loop:
			for(int i= dot+places; i != -1; i--){
				switch(ret.charAt(i)){
				case '9':
					places--;
				break;
				case '.':
				break;
				default:
					ret.setCharAt(i, (char)(ret.charAt(i)+1));
				break loop;
				}
			}
		}else{
			while(ret.charAt(dot+places) == '0') places--;
		}
		if(places>0)
			ret.setLength(dot+places+1);
		else
			ret.setLength(dot);
		return ret.toString();
	}
	public static long sum(int[] x) {
		long sum = 0;
		for(int i = 0;i<x.length;i++) {
			sum += x[i];
		}
		return sum;
	}
	public static long sum(short[] x) {
		long sum = 0;
		for(int i = 0;i<x.length;i++) {
			sum += x[i];
		}
		return sum;
	}
	public static long sum(char[] x) {
		long sum = 0;
		for(int i = 0;i<x.length;i++) {
			sum += x[i];
		}
		return sum;
	}
	public static double sum(float[] x) {
		double sum = 0;
		for(int i = 0;i<x.length;i++) {
			sum += x[i];
		}
		return sum;
	}
	public static double sum(double[] x) {
		double sum = 0;
		for(int i = 0;i<x.length;i++) {
			sum += x[i];
		}
		return sum;
	}
	public static long sum(long[] x) {
		long sum = 0;
		for(int i = 0;i<x.length;i++) {
			sum += x[i];
		}
		return sum;
	}
	public static long factorial(long n) {
		long ans = 1;
		for(; n>1 ; n--)
			ans *= n;
		return ans;
	}
	private do_math(){}
}
