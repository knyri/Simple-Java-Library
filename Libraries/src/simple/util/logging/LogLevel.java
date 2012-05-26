/**
 * 
 */
package simple.util.logging;

/**
 * <br>Created: Nov 2, 2010
 * @author Kenneth Pierce
 */
public enum LogLevel {
	DEBUG((byte)1),
	ERROR((byte)2),
	WARNING((byte)4),
	INFORMATION((byte)8);
	private final byte value;
	LogLevel(byte val) {
		value = val;
	}
	public byte getValue() {return value;}
}
