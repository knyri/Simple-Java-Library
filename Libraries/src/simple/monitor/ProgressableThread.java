package simple.monitor;

/** Thread that implements the progressable interface.
 * <br>Created: Jun 26, 2006
 * @author Kenneth Pierce
 */
public interface ProgressableThread extends Runnable, Progressable {
	/**
	 * Blocks until the object finishes and returns an exit code.
	 * @return An "exit value". Values and meanings depend on implementer.
	 * As a general rule, an exit value of 0 should mean that it finished
	 * normally.
	 */
	public int waitFor();
	/**
	 * Returns a friendly error message using the exit value returned
	 * by the waitFor() method.
	 * @param exitCode Exit code to look up
	 * @return Should return a friendly error message about the exit code.
	 */
	public String getError(int exitCode);
}
