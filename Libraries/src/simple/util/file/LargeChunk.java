/**
 * 
 */
package simple.util.file;

/**
 * <hr>
 * <br>Created: Jan 12, 2011
 * @author Kenneth Pierce
 */
public class LargeChunk implements Chunk {

	private final long start;
	private final long length;
	public LargeChunk(long start, long length) {
		this.start = start;
		this.length = length;
	}
	/**
	 * @return the start position
	 * @see simple.util.file.Chunk#getStart()
	 */
	public long getStart() {
		return start;
	}

	/**
	 * @return the length of the chunk
	 * @see simple.util.file.Chunk#getLength()
	 */
	public long getLength() {
		return length;
	}
}
