/**
 *
 */
package simple.util;

import java.util.Arrays;

/**
 * @author Ken
 *
 */
public final class DataUtil{

	/** BigEndian (0x00ff is 255)
	 * @author Ken
	 *
	 */
	public static class BE{
		public static long getLong(byte[] b){
			byte end=(byte)Math.min(b.length,8);
			long ret=0;
			for (byte i = 0; i < end; i++) {
				ret = (ret << 8) | (b[i] & 0xFF);
			}
			return ret;
		}
		public static int getInt(byte[] b){
			byte end=(byte)Math.min(b.length,4);
			int ret=0;
			for (byte i = 0; i < end; i++) {
				ret = (ret << 8) | (b[i] & 0xFF);
			}
			return ret;
		}
		public static short getShort(byte[] b){
			byte end=(byte)Math.min(b.length,2);
			int ret=0;
			for (byte i = 0; i < end; i++) {
				ret = (ret << 8) | (b[i] & 0xFF);
			}
			return (short)ret;
		}
		public static float getFloat(byte[] b){
			return Float.intBitsToFloat(getInt(b));
		}
		public static double getDouble(byte[] b){
			return Double.longBitsToDouble(getLong(b));
		}
	}
	/**LittleEndian (0x00ff is -256 if read as a short and 65280 if not)
	 * @author Ken
	 *
	 */
	public static class LE{
		public static long getLong(byte[] b){
			if(b.length>8)Arrays.copyOfRange(b,0,8);
			b=do_array.reverse(b);
			long ret=0;
			for (byte i = 0; i < 8; i++) {
				ret = (ret << 8) | (b[i] & 0xFF);
			}
			return ret;
		}
		public static int getInt(byte[] b){
			if(b.length>4)Arrays.copyOfRange(b,0,4);
			b=do_array.reverse(b);
			int ret=0;
			for (byte i = 0; i < 4; i++) {
				ret = (ret << 8) | (b[i] & 0xFF);
			}
			return ret;
		}
		public static short getShort(byte[] b){
			if(b.length>2)Arrays.copyOfRange(b,0,2);
			b=do_array.reverse(b);
			int ret=0;
			for (byte i = 0; i < 2; i++) {
				ret = (ret << 8) | (b[i] & 0xFF);
			}
			return (short)ret;
		}
		public static float getFloat(byte[] b){
			return Float.intBitsToFloat(getInt(b));
		}
		public static double getDouble(byte[] b){
			return Double.longBitsToDouble(getLong(b));
		}
	}
}
