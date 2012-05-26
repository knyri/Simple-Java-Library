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
	public static final boolean isSet(long options,byte mask){
		return (options&mask)==mask;
	}
}
