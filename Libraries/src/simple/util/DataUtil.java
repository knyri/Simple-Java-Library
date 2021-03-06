/**
 *
 */
package simple.util;

import java.util.Arrays;

/**
 * @author Ken
 *
 * Note to self: Endianess is determined by the FIRST byte written
 */
public final class DataUtil{

	/** BigEndian (0x00ff is 255)
	 * @author Ken
	 *
	 */
	public static class BE{
		public static byte[] toByteArray(byte b){
			return new byte[]{b};
		}
		public static byte[] toByteArray(short b){
			return toByteArray(b, Short.SIZE);
		}
		public static byte[] toByteArray(int b){
			return toByteArray(b, Integer.SIZE);
		}
		public static byte[] toByteArray(long b){
			return toByteArray(b, Long.SIZE);
		}
		/**
		 * @param val
		 * @param valSize Size of val in bits. MUST be even.
		 * @return
		 */
		public static byte[] toByteArray(long val, int valSize){
			byte[] ret= new byte[valSize/2];
			for(int i= ret.length - 1, shift= 0; i >= 0; i--, shift+= 8){
				ret[i]= (byte)((val >> shift) & 0xff);
			}
			return ret;
		}
		public static byte[] toByteArray(float b){
			return toByteArray(Float.floatToIntBits(b));
		}
		public static byte[] toByteArray(double b){
			return toByteArray(Double.doubleToLongBits(b));
		}
		/**
		 * Preserves NaN values
		 * @param b
		 * @return
		 */
		public static byte[] toByteArrayRaw(float b){
			return toByteArray(Float.floatToRawIntBits(b));
		}
		/**
		 * Preserves NaN values
		 * @param b
		 * @return
		 */
		public static byte[] toByteArrayRaw(double b){
			return toByteArray(Double.doubleToRawLongBits(b));
		}
		public static long getLong(byte[] b){
			return getLong(b, 0);
		}
		public static long getUnsignedInt(byte[] b){
			return getUnsignedInt(b, 0);
		}
		public static int getInt(byte[] b){
			return getInt(b, 0);
		}
		public static short getShort(byte[] b){
			return (short)getUnsignedShort(b, 0);
		}
		public static int getUnsignedShort(byte[] b){
			return getUnsignedShort(b, 0);
		}
		public static float getFloat(byte[] b){
			return Float.intBitsToFloat(getInt(b, 0));
		}
		public static double getDouble(byte[] b){
			return Double.longBitsToDouble(getLong(b, 0));
		}

		// =================================================

		public static long getLong(byte[] b, int offset){
			int end= Math.min(b.length - offset, Long.SIZE/2) + offset;
			long ret= 0;
			for (; offset < end; offset++) {
				ret = (ret << 8) | (b[offset] & 0xFF);
			}
			return ret;
		}
		public static long getUnsignedInt(byte[] b, int offset){
			int end= Math.min(b.length - offset,Integer.SIZE/2) + offset;
			long ret= 0;
			for (; offset < end; offset++) {
				ret = (ret << 8) | (b[offset] & 0xFF);
			}
			return ret;
		}
		public static int getInt(byte[] b, int offset){
			int end= Math.min(b.length - offset,Integer.SIZE/2) + offset;
			int ret= 0;
			for (; offset < end; offset++) {
				ret = (ret << 8) | (b[offset] & 0xFF);
			}
			return ret;
		}
		public static short getShort(byte[] b, int offset){
			return (short)getUnsignedShort(b, offset);
		}
		public static int getUnsignedShort(byte[] b, int offset){
			int end= Math.min(b.length - offset,Short.SIZE/2) + offset;
			int ret= 0;
			for (; offset < end; offset++) {
				ret = (ret << 8) | (b[offset] & 0xFF);
			}
			return ret;
		}
		public static float getFloat(byte[] b, int offset){
			return Float.intBitsToFloat(getInt(b, offset));
		}
		public static double getDouble(byte[] b, int offset){
			return Double.longBitsToDouble(getLong(b, offset));
		}
	}
	/**LittleEndian (0x00ff is -256 if read as a short and 65280 if as an int or long)
	 * @author Ken
	 *
	 */
	public static class LE{
		public static byte[] toByteArray(byte b){
			return new byte[]{b};
		}
		public static byte[] toByteArray(short b){
			return toByteArray(b, Short.SIZE);
		}
		public static byte[] toByteArray(int b){
			return toByteArray(b, Integer.SIZE);
		}
		public static byte[] toByteArray(long b){
			return toByteArray(b, Long.SIZE);
		}
		/**
		 * @param val
		 * @param valSize Size of val in bits. MUST be even.
		 * @return
		 */
		public static byte[] toByteArray(long val, int valSize){
			byte[] ret= new byte[valSize/2];
			for(int i= valSize/2 - 1, shift= valSize - 8; i >= 0; i--, shift-= 8){
				ret[i]= (byte)((val >> shift) & 0xff);
			}
			return ret;
		}
		public static byte[] toByteArray(float b){
			return toByteArray(Float.floatToIntBits(b));
		}
		public static byte[] toByteArray(double b){
			return toByteArray(Double.doubleToLongBits(b));
		}
		/**
		 * Preserves NaN values
		 * @param b
		 * @return
		 */
		public static byte[] toByteArrayRaw(float b){
			return toByteArray(Float.floatToRawIntBits(b));
		}
		/**
		 * Preserves NaN values
		 * @param b
		 * @return
		 */
		public static byte[] toByteArrayRaw(double b){
			return toByteArray(Double.doubleToRawLongBits(b));
		}

		public static long getLong(byte[] b){
			return getLong(b, 0);
		}
		public static long getUnsignedInt(byte[] b){
			return getUnsignedInt(b, 0);
		}
		public static int getInt(byte[] b){
			return getInt(b, 0);
		}
		public static short getShort(byte[] b){
			return (short)getUnsignedShort(b, 0);
		}
		public static int getUnsignedShort(byte[] b){
			return getUnsignedShort(b, 0);
		}
		public static float getFloat(byte[] b){
			return Float.intBitsToFloat(getInt(b, 0));
		}
		public static double getDouble(byte[] b){
			return Double.longBitsToDouble(getLong(b, 0));
		}

		public static long getLong(byte[] b, int offset){
			int end;
			if(b.length - offset > Long.SIZE/2){
				end= Long.SIZE/2 + offset;
				b= Arrays.copyOfRange(b,0,end);
			}else{
				end= b.length;
			}
			b= do_array.reverse(b);
			long ret= 0;
			for (int i = 0; i < end; i++) {
				ret = (ret << 8) | (b[i] & 0xFF);
			}
			return ret;
		}
		public static int getInt(byte[] b, int offset){
			int end;
			if(b.length - offset > Integer.SIZE/2){
				end= Integer.SIZE/2 + offset;
				b= Arrays.copyOfRange(b,0,end);
			}else{
				end= b.length;
			}
			b= do_array.reverse(b);
			int ret= 0;
			for (int i = 0; i < end; i++) {
				ret = (ret << 8) | (b[i] & 0xFF);
			}
			return ret;
		}
		public static long getUnsignedInt(byte[] b, int offset){
			int end;
			if(b.length - offset > Integer.SIZE/2){
				end= Integer.SIZE/2 + offset;
				b= Arrays.copyOfRange(b,0,end);
			}else{
				end= b.length;
			}
			b= do_array.reverse(b);
			long ret= 0;
			for (int i = 0; i < end; i++) {
				ret = (ret << 8) | (b[i] & 0xFF);
			}
			return ret;
		}
		public static short getShort(byte[] b, int offset){
			return (short)getUnsignedShort(b, offset);
		}
		public static int getUnsignedShort(byte[] b, int offset){
			int end;
			if(b.length - offset > Short.SIZE/2){
				end= Short.SIZE/2 + offset;
				b= Arrays.copyOfRange(b,0,end);
			}else{
				end= b.length;
			}
			b= do_array.reverse(b);
			int ret= 0;
			for (int i = 0; i < end; i++) {
				ret = (ret << 8) | (b[i] & 0xFF);
			}
			return ret;
		}
		public static float getFloat(byte[] b, int offset){
			return Float.intBitsToFloat(getInt(b, offset));
		}
		public static double getDouble(byte[] b, int offset){
			return Double.longBitsToDouble(getLong(b, offset));
		}
	}
//	public static void main(String...strings){
//		System.out.println(LE.getUnsignedShort(LE.toByteArray((short)0xffff)));// (65535) pass
//		System.out.println(LE.getUnsignedShort(LE.toByteArray((short)0xff00)));// (65280) pass
//		System.out.println(LE.getShort(LE.toByteArray((short)0xff00)));// (-256) pass
//	}
}
