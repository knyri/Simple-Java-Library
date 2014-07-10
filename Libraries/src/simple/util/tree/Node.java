/**
 *
 */
package simple.util.tree;

import java.util.ArrayList;
import java.util.Iterator;


/** Simple node where all child nodes must contain the same content. Node names
 * are determined by calling the content's toString method.
 * <br>Created: Oct 12, 2010
 * @author Kenneth Pierce
 * @param <T> Type of the content.
 */
public class Node<T> implements Iterable<Node<T>> {
	/** Holder for the node's content */
	private T content = null;
	/** The parent node */
	private Node<T> parent = null;
	/** Container of children */
	private final ArrayList<Node<T>> children = new ArrayList<Node<T>>();
	private final Object sync=new Object();
	/** Sync on this object when iterating over the child nodes.
	 * @return The sync object
	 */
	public final Object getChildLock(){
		return sync;
	}

	public T getContent() { return content; }
	public void setContent(T obj) { content = obj; }

	/** Calls {@linkplain java.lang.Object#toString()} on the content.
	 * @return The result of the content's toString() method.
	 */
	public String getName() { return content.toString(); }

	protected void setParent(Node<T> node) { parent = node; }
	/**Returns the parent node or null.
	 * @return The parent node or null
	 */
	public Node<T> getParent() { return parent; }
	/** Determines if the node has a parent.
	 * @return True if the parent is not null
	 */
	public boolean hasParent() { return parent!=null; }

	/** Determines if the node has any children.
	 * @return true if the node has children. False otherwise.
	 */
	public boolean hasChild() { return !children.isEmpty(); }
	public Node<T> getChildAt(int index) { return children.get(index); }
	/** Removes the node and sets it's parent to null.
	 * @param node The node to be removed
	 * @return result of {@link java.util.Vector#remove(Object)}
	 */
	public boolean removeChild(Node<T> node) {
		synchronized(sync){
			if(!children.remove(node))return false;
			node.setParent(null);
			return true;
		}
	}
	public void removeAllChildren(){
		synchronized(sync){
			children.clear();
		}
	}
	/** Removes and returns the child at the specified index. Sets the child's parent to null.
	 * @param index Index of the child in the list.
	 * @return The removed child.
	 * @see java.util.Vector#remove(int)
	 */
	public Node<T> removeChildAt(int index) {
		synchronized(sync){
			Node<T> tmp = children.remove(index);
			tmp.setParent(null);
			return tmp;
		}
	}
	/** Adds the node to the child list and sets the node's parent to this.
	 * Does not check for a previous parent. Call remove child on the old parent
	 * first!
	 * @param node The node to be added.
	 * @return the result of {@link java.util.Vector#add(Object)}
	 */
	public boolean addChild(Node<T> node) {
		synchronized(sync){
			if(!children.add(node))return false;
			node.setParent(this);
			return true;
		}
	}
	/**Adds the node at the specified index and sets it's parent to this.
	 * @param node Node to be added
	 * @param index
	 * @see java.util.Vector#add(int, Object)
	 */
	public void addChildAt(Node<T> node, int index) {
		synchronized(sync){
			node.setParent(this);
			children.add(index, node);
		}
	}

	@Override
	public Iterator<Node<T>> iterator() { return children.iterator(); }
}
