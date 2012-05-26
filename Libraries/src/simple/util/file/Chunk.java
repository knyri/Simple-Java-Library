/**
 * 
 */
package simple.util.file;

/**A chunk of a file. Since files can be fragmented.
 * <hr>
 * <br>Created: Jan 12, 2011
 * @author Kenneth Pierce
 */
public interface Chunk {
	/**Returns the start of the chunk.
	 * @return The start position.
	 */
	public long getStart();
	/**Returns the length of the chunk.
	 * @return Length of the chunk.
	 */
	public long getLength();
}
