/**
 * 
 */
package simple.math;

/**
 * <br>Created: Jun 13, 2010
 * @author Kenneth Pierce
 */
public final class Mass {
	public static final int GRAM_PER_TONNE = 1000000, GRAIN_PER_POUND = 7000;
	public static final byte DRAM_PER_OUNCE = 16, OUNCE_PER_POUND = 16;
	public static final double GRAM_PER_POUND = 453.59237;
	public static final double gramToPound(double gram) {
		return gram/GRAM_PER_POUND;
	}
	public static final double poundToGram(double pound) {
		return pound*GRAM_PER_POUND;
	}
	public static final double hundredweightToPound_US(double hw) {
		return hw*100;
	}
	public static final double hundredweightToPound_UK(double hw) {
		return hw*112;
	}
	public static final double poundToHundredweight_US(double pound) {
		return pound/100;
	}
	public static final double poundToHundredweight_UK(double pound) {
		return pound/112;
	}
	public static final double poundToTon(double pound) {
		return pound/2000;
	}
	public static final double tonToPound(double ton) {
		return ton*2000;
	}
	public static final double grainToPound(double grain) {
		return grain/GRAIN_PER_POUND;
	}
	public static final double poundToGrain(double pound) {
		return pound*GRAIN_PER_POUND;
	}
	public static final double ounceToPound(double ounce) {
		return ounce/OUNCE_PER_POUND;
	}
	public static final double poundToOunce(double pound) {
		return pound*OUNCE_PER_POUND;
	}
	public static final double dramToOunce(double dram) {
		return dram/DRAM_PER_OUNCE;
	}
	public static final double ounceToDram(double ounce) {
		return ounce*DRAM_PER_OUNCE;
	}
	
	/**Grams to metric tons.
	 * @param gram
	 * @return Metric Tons
	 */
	public static final double gramToTonne(double gram) {
		return gram/(double)GRAM_PER_TONNE;
	}
	/**Metric tons to grams.
	 * @param tonne
	 * @return Grams
	 */
	public static final double tonneToGram(double tonne) {
		return tonne*GRAM_PER_TONNE;
	}
}
