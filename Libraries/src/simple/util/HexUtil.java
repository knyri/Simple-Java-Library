package simple.util;

/**
 * <br>Created: 2006
 * @author Kenneth Pierce
 */
public final class HexUtil {
	//#############-----------####################
	//############# HEX STUFF ####################
	//#############-----------####################
	private static final char[][] hex=
		new char[][] {
			new char[]{'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'},
			new char[]{'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'}
		};
	/**
	 * @param c Some character.
	 * @return Returns a hex representation of <var>c</var>
	 */
	public static String getHex(char c) {
		return new String(new char[] {getHexTop(c),getHexBottom(c)});
	}
	/**
	 * Converts a CharSequence into a String of hex.
	 * @param cs
	 * @return A hex String of the characters in the provided CharSequence.
	 */
	public static String getHex(CharSequence cs) {
		StringBuffer buf = new StringBuffer(cs.length()*2);
		for (int i = 0;i<cs.length();i++) {
			buf.append(getHex(cs.charAt(i)));
		}
		return buf.toString();
	}
	/**
	 * Same as getHex but each hex has <var>pre</var> before it.
	 * @param pre String to place in front of each hex.
	 * @param cs
	 * @return A hex String of the characters in the provided CharSequence.
	 */
	public static String getHex(String pre, CharSequence cs) {
		StringBuffer buf = new StringBuffer(cs.length()*(2+pre.length()));
		for (int i = 0;i<cs.length();i++) {
			buf.append(pre+getHex(cs.charAt(i)));
		}
		return buf.toString();
	}
	private static int getValue(char c){
		switch(c){
		case'0':
			return 0;
		case'1':
			return 1;
		case'2':
			return 2;
		case'3':
			return 3;
		case'4':
			return 4;
		case'5':
			return 5;
		case'6':
			return 6;
		case'7':
			return 7;
		case'8':
			return 8;
		case'9':
			return 9;
		case'A':
		case'a':
			return 10;
		case'B':
		case'b':
			return 11;
		case'C':
		case'c':
			return 12;
		case'D':
		case'd':
			return 13;
		case'E':
		case'e':
			return 14;
		case'F':
		case'f':
			return 15;
		}
		return -1;
	}
	public static String getHex(byte[] b) {
		StringBuffer buf = new StringBuffer(b.length);
		for(int i= 0; i < b.length; i++) {
			buf.append(getHexTop(b[i]));
			buf.append(getHexBottom(b[i]));
		}
		return buf.toString();
	}
	public static String getHex(byte[] b, char sep) {
		StringBuffer buf = new StringBuffer(b.length);
		for(int i= 0; i < b.length; i++) {
			buf.append(sep);
			buf.append(getHexTop(b[i]));
			buf.append(getHexBottom(b[i]));
		}
//		buf.deleteCharAt(buf.length()-1);
		return buf.toString();
	}
	public static String getHexLC(byte[] b) {
		StringBuffer buf = new StringBuffer(b.length);
		for(int i= 0; i < b.length; i++) {
			buf.append(getHexTopLC(b[i]));
			buf.append(getHexBottomLC(b[i]));
		}
		return buf.toString();
	}
	public static String getHexLC(byte[] b, char sep) {
		StringBuffer buf = new StringBuffer(b.length);
		for(int i= 0; i < b.length; i++) {
			buf.append(sep);
			buf.append(getHexTopLC(b[i]));
			buf.append(getHexBottomLC(b[i]));
		}
//		buf.deleteCharAt(buf.length()-1);
		return buf.toString();
	}
	/**
	 * Takes a String of 2 hex characters and converts it into integer form.
	 * @param hex String to be converted.
	 * @return Int form of the first 2 characters.
	 */
	public static int getIntFromHex(String hex) {
		int number = 0;
		number = getValue(hex.charAt(0))<<4;
		number |= getValue(hex.charAt(1));
		return number;
	}
	/**
	 * Takes a hex string and returns a String of the characters they represent.
	 * @param pre Prefix.(e.g. <code>toStringFromHex("%", "%34");</code>)
	 * @param hex String to convert.
	 * @return A String of characters that the hex string represents.
	 */
	public static String toString(String pre, String hex) {
		hex = hex.replaceAll("\\Q"+pre+"\\E","");
		return toString(hex);
	}
	/**
	 * @param hex String containing the hex characters.
	 * @return A string of characters that the hex string represents.
	 */
	public static String toString(String hex) {
		StringBuffer buf = new StringBuffer(hex.length());
		for (int i = 0;i<hex.length()-1;i+=2) {
			buf.append((char)getIntFromHex(hex.substring(i,Math.min(hex.length(),i+2))));
		}
		return buf.toString();
	}
	/**
	 * Upper Case
	 * @param b
	 * @return Hex character for the bottom 4 bits of <var>b</var>.
	 */
	public static char getHexBottom(int b) {return hex[0][b&15];}
	/**
	 * Upper Case
	 * @param b
	 * @return The top 4 bits of <var>b</var>.
	 */
	public static char getHexTop(int b) {return getHexBottom(b>>>4);}
	/**
	 * Lower case
	 * @param b
	 * @return Hex character for the bottom 4 bits of <var>b</var>.
	 */
	public static char getHexBottomLC(int b) {return hex[1][b&15];}
	/**
	 * Lower case
	 * @param b
	 * @return The top 4 bits of <var>b</var>.
	 */
	public static char getHexTopLC(int b) {return getHexBottomLC(b>>>4);}
	public static byte[] fromHex(CharSequence hex){
		if(hex.length()%2!=0)hex=hex.toString()+"0";
		byte[] ret=new byte[hex.length()/2];
		int retIdx = 0;
		for(int i=0;i<hex.length();i+=2){
			ret[retIdx++]=(byte)((getValue(hex.charAt(i))<<4)|getValue(hex.charAt(i+1)));
		}
		return ret;
	}
	public static byte[] getRandomByteArray(int size) {
		byte[] array = new byte[size];
		for (;size>0;size--) {
			array[size-1] = (byte)(Math.random()*0xff);
		}
		return array;
	}
}
