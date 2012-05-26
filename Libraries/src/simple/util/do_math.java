package simple.util;

/**
 * <br>Created: 2006
 * @author Kenneth Pierce
 */
public final class do_math {
	private static final char[] HEX = new char[] {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
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
	public static byte convertHexChar (char hex) {
		switch (hex) {
		case '0':
			return 0;
		case '1':
			return 1;
		case '2':
			return 2;
		case '3':
			return 3;
		case '4':
			return 4;
		case '5':
			return 5;
		case '6':
			return 6;
		case '7':
			return 7;
		case '8':
			return 8;
		case '9':
			return 9;
		case 'A':
		case 'a':
			return 10;
		case 'B':
		case 'b':
			return 11;
		case 'C':
		case 'c':
			return 12;
		case 'D':
		case 'd':
			return 13;
		case 'E':
		case 'e':
			return 14;
		case 'F':
		case 'f':
			return 15;
		}
		return 0;
	}
	public static byte fromHexByte (String hex) {
		if (hex.length() != 2)
			throw new IllegalArgumentException("Wrong length. Must be 2 characters long.");
		return (byte)(convertHexChar(hex.charAt(1)) + (convertHexChar(hex.charAt(0)) << 4));
	}
	public static short fromHexShort(String hex) {
		if (hex.length() != 4)
			throw new IllegalArgumentException("Wrong length. Must be 4 characters long.");
		return (short)(convertHexChar(hex.charAt(3))
				+ (convertHexChar(hex.charAt(2)) << 4)
				+ (convertHexChar(hex.charAt(1)) << 8)
				+ (convertHexChar(hex.charAt(0)) << 12));
	}
	public static int fromHexInt(String hex) {
		if (hex.length() != 8)
			throw new IllegalArgumentException("Wrong length. Must be 8 characters long.");
		return (convertHexChar(hex.charAt(7))
				+ (convertHexChar(hex.charAt(6)) << 4)
				+ (convertHexChar(hex.charAt(5)) << 8)
				+ (convertHexChar(hex.charAt(4)) << 12)
				+ (convertHexChar(hex.charAt(3)) << 16)
				+ (convertHexChar(hex.charAt(2)) << 20)
				+ (convertHexChar(hex.charAt(1)) << 24)
				+ (convertHexChar(hex.charAt(0)) << 28));
	}
	public static long fromHexLong(String hex) {
		if (hex.length() != 16)
			throw new IllegalArgumentException("Wrong length. Must be 16 characters long.");
		return (convertHexChar(hex.charAt(15))
				+ (convertHexChar(hex.charAt(14)) << 4)
				+ (convertHexChar(hex.charAt(13)) << 8)
				+ (convertHexChar(hex.charAt(12)) << 12)
				+ (convertHexChar(hex.charAt(11)) << 16)
				+ (convertHexChar(hex.charAt(10)) << 20)
				+ (convertHexChar(hex.charAt(9)) << 24)
				+ ((long)convertHexChar(hex.charAt(8)) << 28)
				+ ((long)convertHexChar(hex.charAt(7)) << 32)
				+ ((long)convertHexChar(hex.charAt(6)) << 36)
				+ ((long)convertHexChar(hex.charAt(5)) << 40)
				+ ((long)convertHexChar(hex.charAt(4)) << 44)
				+ ((long)convertHexChar(hex.charAt(3)) << 48)
				+ ((long)convertHexChar(hex.charAt(2)) << 52)
				+ ((long)convertHexChar(hex.charAt(1)) << 56)
				+ ((long)convertHexChar(hex.charAt(0)) << 60));
	}
	public static byte[] fromHex(String hex) {
		byte[] result = new byte[hex.length()/2];
		int it2 = 0;
		int i =0;
		for (; i < result.length-1; i++) {
			it2 = i*2;
			result[i] = fromHexByte(hex.substring(it2,it2+2));
		}
		return result;
	}
	public static String toHex(byte num) {
		char[] res = new char[2];
		res[1] = HEX[num&15];
		res[0] = HEX[num>>>4];
		return new String(res);
	}
	public static String toHex(short num) {
		byte len = 0;
		if ((0xFF00 & num) != 0) {//short
			len = 4;
		} else {
			if ((0xFF & num) != 0 ) {//byte
				len = 2;
			} else {
				return null;
			}
		}
		char[] res = new char[len];
		byte shift = 0;
		for (int i = len-1; i >= 0; i--) {
			res[i] = HEX[(int) ((num>>>shift) & 0x0F)];
			shift += 4;
		}
		return new String(res);
	}
	public static String toHex(int num) {
		byte len = 0;
		if ((0xFF000000l & num) != 0) {//int
			len = 8;
		} else {
			if ((0xFF0000 & num) != 0) {
				len = 6;
			} else {
				if ((0xFF00 & num) != 0) {//short
					len = 4;
				} else {
					if ((0xFF & num) != 0) {//byte
						len = 2;
					} else {
						return null;
					}
				}
			}
		}
		char[] res = new char[len];
		byte shift = 0;
		for (int i = len-1; i >= 0; i--) {
			res[i] = HEX[(int) ((num>>>shift) & 0x0F)];
			shift += 4;
		}
		return new String(res);
	}
	public static String toHex(long num) {
		byte len = 0;
		if ((0xFF00000000000000l & num) != 0) {//long
			len = 16;
		} else {
			if ((0xFF000000000000l & num) != 0) {
				len = 14;
			} else {
				if ((0xFF0000000000l & num) != 0) {
					len = 12;
				} else {
					if ((0xFF00000000l & num) != 0) {
						len = 10;
					} else {
						if ((0xFF000000l & num) != 0) {//int
							len = 8;
						} else {
							if ((0xFF0000 & num) != 0) {
								len = 6;
							} else {
								if ((0xFF00 & num) != 0) {//short
									len = 4;
								} else {
									if ((0xFF & num) != 0) {//byte
										len = 2;
									} else {
										return null;
									}
								}
							}
						}
					}
				}
			}
		}
		char[] res = new char[len];
		byte shift = 0;
		for (int i = len-1; i >= 0; i--) {
			res[i] = HEX[(int) ((num>>>shift) & 0x0F)];
			shift += 4;
		}
		return new String(res);
	}
	public static String toHex(byte[] b) {
		StringBuilder res = new StringBuilder(b.length*2);
		for (int i = 0; i < b.length; i++) {
			res.append(HEX[(int) ((b[i] >>> 4) & 0x0F)]);
			res.append(HEX[b[i] & 0x0F]);
		}
		return new String(res);
	}
}
