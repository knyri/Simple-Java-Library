/**
 *
 */
package simple.math;

/**Requires Metric.
 * <br>Created: Jun 13, 2010
 * @author Kenneth Pierce
 */
public final class Length {
	public static final int INCH_PER_FOOT = 12, FOOT_PER_MILE = 5280, FOOT_PER_YARD = 3;
	public static final float CM_PER_IN = 2.54f;
	public static final double inchToYard(double inch) {
		return footToYard(inchToFoot(inch));
	}
	public static final double yardToInch(double yard) {
		return footToInch(yardToFoot(yard));
	}
	public static final double yardToFoot(double yard) {
		return yard*FOOT_PER_YARD;
	}
	public static final double footToYard(double feet) {
		return feet/FOOT_PER_YARD;
	}
	public static final double inchToFoot(double inch) {
		return inch/INCH_PER_FOOT;
	}
	public static final double footToInch(double feet) {
		return feet*INCH_PER_FOOT;
	}
	public static final double footToMile(double feet) {
		return feet/FOOT_PER_MILE;
	}
	public static final double mileToFoot(double miles) {
		return miles*FOOT_PER_MILE;
	}
	public static final double inchToCentimeter(double inches) {
		return inches*CM_PER_IN;
	}
	public static final double centimeterToInch(double centimeters) {
		return centimeters/CM_PER_IN;
	}
	public static final double kilometerToMile(double kilometers) {
		return footToMile(inchToFoot(centimeterToInch(Metric.toCenti(Metric.kiloTo(kilometers)))));
	}
	public static final double mileToKilometer(double miles) {
		return Metric.toKilo(Metric.centiTo(inchToCentimeter(footToInch(mileToFoot(miles)))));
	}
}
