package simple.monitor;

import javax.swing.JProgressBar;
/** Interface for anything that can use a progress bar.
 * <br>Created: Jun 26, 2006
 * @author Kenneth Pierce
 */
public interface Progressable {
	/**
	 * Sets the progress bar that this object will use.
	 * @param pbar the progress bar to use
	 */
	public void setProgressBar(JProgressBar pbar);
	/**
	 * Return the progress bar that this object is using.
	 * If none was set prior to its calling a default will be made.
	 * @return The progress bar associated with this progressable
	 */
	public JProgressBar getProgressBar();
	/**
	 * Returns the number that needs to be reached
	 * in order to finish. This number can change over time.
	 * @return The maximum number for this object.
	 */
	public int getMaximum();
	/**
	 * Returns the number to start at. This number may change
	 * over time, though it generally shouldn't.
	 * @return The starting number for this object.
	 */
	public int getMinimum();
	/**
	 * Returns the current value. This should not be below
	 * the minimum or above the maximum.
	 * @return The current number for this object.
	 */
	public int getCurrentValue();
	/**
	 * Returns a message about the current operation. This
	 * may change over time.
	 * @return A message about the current operation.
	 */
	public String getMessage();
	/**
	 * Returns whether this operation is finished.
	 * @return True if the operation is finished.
	 */
	public boolean isDone();
	/**
	 * Returns whether this operation was canceled.<br>
	 * This should return true if the user canceled the operation.
	 * @return True if the operation was canceled.
	 */
	public boolean isCanceled();
	/**
	 * Returns whether the operation was aborted.<br>
	 * Should return true if the operation was canceled
	 * because of an error.
	 * @return True if the operation was aborted.
	 */
	public boolean isAborted();
	/**
	 * Returns a String containing an error message if one
	 * occured. I.e. isAborted() or isCanceled() returns true.
	 * @return An error message if an error occured. If none occured
	 * then it should return an empty String.
	 */
	public String getError();
}
