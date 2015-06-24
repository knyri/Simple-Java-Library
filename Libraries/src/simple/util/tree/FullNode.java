package simple.util.tree;

import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import simple.CIString;

/**Node that has it all.<br>
 * <ul>
 * <li>case insensitive name</li>
 * <li>properties</li>
 * <li>node depth</li>
 * <li>children</li>
 * <li>content</li>
 * </ul>
 * <hr>
 * other dependencies:<br>
 * simple.CIString
 * @author Kenneth Pierce
 * @version 1.2
 * @param <T> Type for node content.
 * @param <K> Type for property Keys.
 * @param <V> Type for property Values.
 */
/**
 * @author Ken
 *
 * @param <T> Content type
 * @param <K> Property key type
 * @param <V> Property value type
 */
public class FullNode<T, K, V> {
	/**
	 * Vector of Nodes to hold any children.
	 */
	protected final LinkedList<FullNode<T,K,V>> children = new LinkedList<FullNode<T,K,V>>();
	/**
	 * HashMap to hold properties for this node.
	 */
	protected HashMap<K,V> properties = new HashMap<K,V>();
	private FullNode<T,K,V> parent = null;
	private CIString nName = null;
	private T content = null;
	private final Object sync=new Object();
	//private static final Log log = LogFactory.getLogFor(FullNode.class);
	/**
	 * depth: how deep in the tree<br>
	 * siblingIndex: index used when calling {@link #getChild(int)} on this node's parent
	 */
	private int depth=0,siblingIndex=-1;
	protected FullNode() {}
	/**
	 * Creates a Node and sets its name.
	 * @param name Name of the node.
	 */
	public FullNode(String name) {
		setName(name);
	}
	public FullNode(String name, FullNode<T,K,V> parent, T content) {
		setName(name);
		setParent(parent);
		setContent(content);
	}
	public FullNode(String name, T content) {
		setName(name);
		setContent(content);
	}
	/**
	 * Creates a Node and sets its name and parent.
	 * @param name Name of the node.
	 * @param parent Parent of the node.
	 */
	public FullNode(String name, FullNode<T,K,V> parent) {
		this(name);
		setParent(parent);
	}
	/**
	 * Sets the contents of the node.
	 * @param object Object to set as the contents.
	 * @return this
	 */
	public final FullNode<T,K,V> setContent(T object){content = object; return this;}
	/**
	 * Contents of the Node. May be null.
	 * @return The contents of the node.
	 */
	public final T getContent() {return content;}

	/**
	 * Sets the name of the node.
	 * @param name String to set as the Node's name.
	 * @return this
	 */
	public final FullNode<T,K,V> setName(String name) {nName=new CIString(name);return this;}
	public final FullNode<T,K,V> setName(CIString name) {nName=name;return this;}
	/**
	 * @return The name of this node.
	 */
	public final CIString getName() {return nName;}

	/**Sets the parent of this node. If it already has a parent then it calls
	 * <code>removeChild(this)</code> on its parent before setting its parent
	 * to the specified Node. Also adds itself to the new parent if
	 * <code><var>node</var>.hasChild(this)!=true</code>.<br>
	 * This function sets the depth.
	 * @param pnode Node to be set as the parent of this Node.
	 * @return The old parent or null if pnode is already the parent
	 */
	public final FullNode<T,K,V> setParent(FullNode<T,K,V> pnode) {
		if (parent==pnode) return null;
		FullNode<T,K,V> oldp= parent;
		//log.debug("moving "+getName()+" from "+getParent()+" to "+(node==null?null:node.getName()));
		if (hasParent())
			parent.removeChild(this);

		if (pnode!=null) {
			if (!pnode.hasChild(this))
				pnode.addChild(this);

			depth= pnode.getDepth()+1;
		} else {
			depth= 0;
		}
		parent=pnode;
		return oldp;
	}
	/**
	 * @return The result of <code>getParent()!=null</code>.
	 */
	public final boolean hasParent() {return parent != null;}
	/**
	 * @return The parent of this node.
	 */
	public final FullNode<T,K,V> getParent() {return parent;}
	public final FullNode<T,K,V> getParent(int levels) {
		FullNode<T,K,V> ret= parent;
		while(ret != null && --levels > 0)
			ret= ret.getParent();
		return ret;
	}
	/**
	 * Checks if it has a parent and returns the result.
	 * Logical opposite of {@link #hasParent()}
	 * @return True if it has no parent.
	 */
	public final boolean isRoot() {return parent==null;}
	/**
	 * Checks if it has any children.
	 * @return True if it has one or more children.
	 */
	public final boolean hasChild() {return !children.isEmpty();}
	/**
	 * @param child Node to test if it is a child.
	 * @return True if the specified Node is a child of this node.
	 */
	public final boolean hasChild(FullNode<T,K,V> child) {
		return children.contains(child);
	}
	/**
	 * @param parent Possible parent node.
	 * @return The result of <code><var>parent</var>.hasChild(this)</code>.
	 */
	public final boolean isChildOf(FullNode<T,K,V> parent) {
		return parent.hasChild(this);
	}
	public final void addChildren(Collection<? extends FullNode<T,K,V>> nodes){
		for(FullNode<T,K,V> n: nodes){
			addChild(n);
		}
	}
	/**
	 * Adds a child Node and sets this as the node's parent. Does nothing if <code><var>node</var>==null</code>.
	 * @param cnode Node to be added as a child.
	 * @return this
	 */
	public final FullNode<T,K,V> addChild(FullNode<T,K,V> cnode) {
		if (cnode==null) return this;
		synchronized(sync){
			children.add(cnode);
			cnode.setSiblingIndex(children.size()-1);
			cnode.setParent(this);
		}
		return this;
	}
	/**
	 * Adds a child Node and sets this as the node's parent. Does nothing if <code><var>node</var>==null</code>.
	 * @param cnode Node to be added as a child.
	 * @return this
	 */
	public final FullNode<T,K,V> insertChild(FullNode<T,K,V> cnode, int index) {
		if (cnode==null) return this;
		if (index >= children.size() || index < 0) return this;
		synchronized(sync){
			children.add(index, cnode);
			cnode.setSiblingIndex(index);
			cnode.setParent(this);
			for(;index<children.size();index++)
				children.get(index).setSiblingIndex(index);
		}
		return this;
	}
	/**
	 * Gets a child at the index specified.
	 * @param index Index of the child wanted.
	 * @return Child Node at specified index or null if it has no children or the index is
	 * out of the valid range(0&gt;= index &gt; childCount()).
	 */
	public final FullNode<T,K,V> getChild(int index) {
		if (index >= children.size() || index < 0) return null;
		return children.get(index);
	}
	/**
	 * @return The number of children this node has.
	 */
	public final int childCount() {return children.size();}

	/**
	 * Removes the specified Node from the list of children if it has any.
	 * @param child Node to be removed.
	 * @return this
	 */
	public FullNode<T,K,V> removeChild(FullNode<T,K,V> child) {
		synchronized(sync){
			return removeChild(children.indexOf(child));
		}
	}
	/**
	 * Removes the child Node at the specified index if getChild(index)!=null.
	 * @param index Index of Node to be removed.
	 * @return this
	 */
	public final FullNode<T,K,V> removeChild(int index) {
		if (index < children.size() && index >= 0){
			synchronized(sync){
				children.remove(index).setSiblingIndex(-1).setParent(null);
				for(;index<children.size();index++)
					children.get(index).setSiblingIndex(index);
			}
		}
		return this;
	}
	/**
	 * Removes all children.
	 * @return this
	 */
	public final FullNode<T,K,V> removeAllChildren(){
		synchronized(sync){
			for(int index=0;index<children.size();index++)
				children.pop().setSiblingIndex(-1).setParent(null);
		}
		return this;
	}
	public final FullNode<T,K,V> setProperties(Map<K,V> props){
		properties.putAll(properties);
		return this;
	}
	/**
	 * @param key The key for the value.
	 * @return The value of the property.
	 */
	public final V getProperty(K key) {
		return properties.get(key);
	}
	/**
	 * Adds the key/value pair to the properties. If the key already exists then
	 * the value for that key is replaced with the one provided.
	 * @param key Key for the value.
	 * @param value Value to be set for the property.
	 * @return This node
	 */
	public final FullNode<T,K,V> setProperty(K key, V value) {
		properties.put(key, value);
		return this;
	}
	/**Removes the key/value pair.
	 * @param key
	 * @return The value associated with the key.
	 */
	public final V removeProperty(K key) {
		return properties.remove(key);
	}
	/**
	 * @param key
	 * @return True if the properties table contains the specified key.
	 */
	public final boolean hasProperty(K key) {
		return properties.containsKey(key);
	}
	@Override
	public String toString() {
		return getName().toString();
	}
	/**Get which level of the tree the element is on. 0 based.
	 * @return The depth of this node
	 */
	public final int getDepth() {
		return depth;
	}
	public final void print(PrintStream out){
		Util.print(this,out);
	}
	/**
	 * @param index
	 * @return this
	 */
	protected FullNode<T,K,V> setSiblingIndex(int index){
		this.siblingIndex=index;
		return this;
	}
	/** index used when calling {@link #getChild(int)} on this node's parent
	 * @return the index
	 */
	public int getSiblingIndex(){
		return this.siblingIndex;
	}
}
