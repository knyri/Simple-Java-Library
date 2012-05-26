/**
 * 
 */
package simple.util.file;

import java.util.Vector;

/**
 * <hr>
 * <br>Created: Jan 12, 2011
 * @author Kenneth Pierce
 */
public class FileMarker {
	private final Vector<Chunk> chunks = new Vector<Chunk>();
	private final String name;
	private long size;
	public FileMarker(String name) {
		this.name = name;
	}
	/**Adds a chunk to the end of the file. Adjusts the file size.
	 * @param chunk Chunk to be added.
	 */
	public synchronized void addChunk(Chunk chunk) {
		if (chunks.add(chunk))
			size += chunk.getLength();
	}
	/**Adds a chunk at the specified index. Adjusts the file size.
	 * @param chunk Chunk to be added.
	 * @param index Index to add the chunk.
	 */
	public synchronized void addChunkAt(Chunk chunk, int index) {
		chunks.add(index, chunk);
		size += chunk.getLength();
	}
	/**Removes the chunk from the file. Adjusts the file size.
	 * @param chunk Chunk to be removed.
	 * @return True if it was found and removed. False otherwise.
	 */
	public synchronized boolean removeChunk(Chunk chunk) {
		if (chunks.remove(chunk)) {
			size -= chunk.getLength();
			return true;
		}
		return false;
	}
	/**Removes a chunk from the file. Adjusts the file size.
	 * @param index Index of the chunk.
	 * @return The removed chunk or null if there wasn't a chunk there.
	 */
	public synchronized Chunk removeChunk(int index) {
		Chunk tmp = chunks.remove(index);
		if (tmp!=null) {
			size -= tmp.getLength();
		}
		return tmp;
	}
	/**The name of the file.
	 * @return The name of the file.
	 */
	public String getName() { return name; }
	/**The size of the file.
	 * @return The size of the file.
	 */
	public long getSize() { return size; }
	/**The number of file chunks.
	 * @return The number of file chunks.
	 */
	public int getChunkCount() { return chunks.size(); }
	public String toString() {
		StringBuilder ret = new StringBuilder(500);
		ret.append(name + "{");
		Chunk tmp = chunks.firstElement();
		ret.append(tmp.getStart() + "," + tmp.getLength());
		for (int i = 1; i < chunks.size(); i++) {
			tmp = chunks.elementAt(i);
			ret.append(";" + tmp.getStart() + "," + tmp.getLength());
		}
		ret.append('}');
		return ret.toString();
	}
}
