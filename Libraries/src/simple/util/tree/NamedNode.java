/**
 *
 */
package simple.util.tree;

import java.util.HashMap;

/**Node with a name, content, and children.
 * <br>Created: Oct 13, 2010
 * @author Kenneth Pierce
 * @param T Type of the content
 */
public class NamedNode<T> {
	private T content = null;
	private final String name;
	private NamedNode<T> parent = null;
	private final HashMap<String, NamedNode<T>> children = new HashMap<String, NamedNode<T>>();
	private final Object sync=new Object();
	public NamedNode(String name) {
		this.name = name;
	}
	public String getName() { return name; }

	public void setParent(NamedNode<T> nParent) { parent = nParent; }
	public NamedNode<T> getParent() { return parent; }

	public void setContent(T nContent) { content = nContent; }
	public T getContent() { return content; }

	public void addChild(NamedNode<T> node) {
		synchronized(sync){
			node.setParent(this);
			children.put(node.getName(), node);
		}
	}
	public NamedNode<T> getChild(String name) {
		return children.get(name);
	}
	public NamedNode<T> removeChild(String name) {
		synchronized(sync){
			NamedNode<T> tmp = children.remove(name);
			tmp.setParent(null);
			return tmp;
		}
	}
	public void removeAllChildren(){
		synchronized(sync){
			children.clear();
		}
	}
}
