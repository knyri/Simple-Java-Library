/**
 * 
 */
package simple.util.constants;

/**
 * <hr>
 * <br>Created: May 3, 2011
 * @author Kenneth Pierce
 */
public enum TF {
	TRUE(true),
	FALSE(false);
	private final boolean val;
	TF(boolean value) {
		val = value;
	}
	public boolean getValue(){
		return val;
	}
}
