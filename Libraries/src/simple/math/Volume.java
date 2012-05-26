/**
 * 
 */
package simple.math;

/**
 * <br>Created: Jun 13, 2010
 * @author Kenneth Pierce
 */
public final class Volume {
	public static final class Liquid {
		public static final byte TEASPOON_PER_OUNCE = 2, TEASPOON_PER_TABLESPOON_US = 3,
		TEASPOON_PER_TABLESPOON_UK = 4, DASH_PER_PINCH = 2;
		public static final float DROP_PER_PINCH = 9.5f;
		public static final double pinchToDash(double pinch) {
			return pinch/DASH_PER_PINCH;
		}
		public static final double dashToPinch(double dash) {
			return dash*DASH_PER_PINCH;
		}
		public static final double dropToPinch(double drop) {
			return drop/DROP_PER_PINCH;
		}
		public static final double pinchToDrop(double pinch) {
			return pinch*DROP_PER_PINCH;
		}
		public static final double ounceToTeaspoon(double ounce) {
			return ounce*TEASPOON_PER_OUNCE;
		}
		public static final double teaspoonToOunce(double teaspoon) {
			return teaspoon/TEASPOON_PER_OUNCE;
		}
		public static final double tablespoonToTeaspoon_US(double tablespoon) {
			return tablespoon*TEASPOON_PER_TABLESPOON_US;
		}
		public static final double teaspoonToTablespoon_US(double teaspoon) {
			return teaspoon/TEASPOON_PER_TABLESPOON_US;
		}
		public static final double tablespoonToTeaspoon_UK(double tablespoon) {
			return tablespoon*TEASPOON_PER_TABLESPOON_UK;
		}
		public static final double teaspoonToTablespoon_UK(double teaspoon) {
			return teaspoon/TEASPOON_PER_TABLESPOON_UK;
		}
		public static final double USteaspoonToUKteaspoon(double us) {
			return us*1.066;
		}
		public static final double UKteaspoonToUSteaspoon(double uk) {
			return uk/1.066;
		}
		public static final double UStablespoonToUKtablespoon(double us) {
			return us*.8;
		}
		public static final double UKtablespoonToUStablespoon(double uk) {
			return uk/.8;
		}
	}
}
