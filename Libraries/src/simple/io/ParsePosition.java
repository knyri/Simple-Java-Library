/**
 * 
 */
package simple.io;

/**Holder for the current position while parsing.
 * <br>Created: Mar 31, 2010
 * @author Kenneth Pierce
 */
public final class ParsePosition {
	/** position in the file */
	private long filePosition = 0;
	/** position on the current line */
	private long linePosition = 0;
	/** current line */
	private int lineCount = 1;
	public final void incrementPosition() {
		filePosition++;
		linePosition++;
	}
	public final void incrementPosition(long amount) {
		filePosition += amount;
		linePosition += amount;
	}
	/** resets the line position and increments the line count */
	public final void newLine() {
		linePosition = 0;
		lineCount++;
	}
	/**
	 * @return the filePosition
	 */
	public final long getFilePosition() {
		return filePosition;
	}
	/**
	 * @param filePosition the filePosition to set
	 */
	public final void setFilePosition(long filePosition) {
		this.filePosition = filePosition;
	}
	/**
	 * @return the linePosition
	 */
	public final long getLinePosition() {
		return linePosition;
	}
	/**
	 * @param linePosition the linePosition to set
	 */
	public final void setLinePosition(int linePosition) {
		this.linePosition = linePosition;
	}
	/**
	 * @return the lineCount
	 */
	public final int getLineCount() {
		return lineCount;
	}
	/**
	 * @param lineCount the lineCount to set
	 */
	public final void setLineCount(int lineCount) {
		this.lineCount = lineCount;
	}
	public String toString() {
		return "FPos:"+getFilePosition()+",Line:"+getLineCount()+",LPos:"+getLinePosition();
	}
}
