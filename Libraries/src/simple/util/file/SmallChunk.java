/**
 * 
 */
package simple.util.file;

/**
 * <hr>
 * <br>Created: Jan 12, 2011
 * @author Kenneth Pierce
 */
public class SmallChunk implements Chunk {
	private final long start;
	private final short length;
	public SmallChunk(long start, short length) {
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
	 * @return The length of the chunk
	 * @see simple.util.file.Chunk#getLength()
	 */
	public long getLength() {
		return length;
	}

}
