/**
 *
 */
package simple.util;

/**
 * <hr>
 * <br>Created: May 1, 2011
 * @author Kenneth Pierce
 */
public final class do_bit {
	public static final byte set(byte options,byte mask){
		return (byte)(options|mask);
	}
	public static final byte unset(byte options,byte mask){
		return (byte)(options&~mask);
	}
	public static final byte toggle(byte options,byte mask){
		return (byte)(options^mask);
	}
	public static final short set(short options,short mask){
		return (short)(options|mask);
	}
	public static final short unset(short options,short mask){
		return (short)(options&~mask);
	}
	public static final short toggle(short options,short mask){
		return (short)(options^mask);
	}
	public static final int set(int options,int mask){
		return (options|mask);
	}
	public static final int unset(int options,int mask){
		return (options&~mask);
	}
	public static final int toggle(int options,int mask){
		return (options^mask);
	}
	public static final long set(long options,long mask){
		return (options|mask);
	}
	public static final long unset(long options,long mask){
		return (options&~mask);
	}
	public static final long toggle(long options,long mask){
		return (options^mask);
	}
	public static final boolean isSet(long options,long mask){
		return (options&mask)==mask;
	}
	public static final String toString(byte b){
		char c[] = new char[8];
		for(byte i=0;i<8;i++)
			c[7-i] = (((b >> i)& 0x1) == 1)?'1':'0';
		return new String(c);
	}
	public static final String toString(int b){
		char c[] = new char[32];
		for(byte i=0;i<32;i++)
			c[31-i] = (((b >> i)& 0x1) == 1)?'1':'0';
		return new String(c);
	}
	public static final String toString(short b){
		char c[] = new char[16];
		for(byte i=0;i<16;i++)
			c[15-i] = (((b >> i)& 0x1) == 1)?'1':'0';
		return new String(c);
	}
	public static final String toString(long b){
		char c[] = new char[64];
		for(byte i=0;i<64;i++)
			c[63-i] = (((b >> i)& 0x1) == 1)?'1':'0';
		return new String(c);
	}
	private do_bit(){}
}
