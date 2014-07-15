package simple.gui;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

/**
 * This class is a basic implementation of the
 * javax.swing.tree.TreeNode interface.
 * <br>Created: ??
 * @author Kenneth Pierce
 */
public class TreeNodeImpl implements TreeNode {
	private List<TreeNode> children = new ArrayList<TreeNode>();
	private TreeNodeImpl parent = null;
	private boolean canAdopt = true;
	private String name = "";
	private Object userObject = null;
	/**
	 * Default Constructor. This tree node allows children
	 * and does not have a parent.
	 */
	public TreeNodeImpl() {
		super();
	}
	public TreeNodeImpl(String name) {
		this();
		setName(name);
	}
	public TreeNodeImpl(String name, TreeNodeImpl parent) {
		this(name);
		setParent(parent);
	}
	public TreeNodeImpl(TreeNodeImpl parent) {
		this();
		setParent(parent);
	}
	public TreeNodeImpl(TreeNodeImpl parent, boolean canAdopt) {
		this(parent);
		setAllowsChildren(canAdopt);
	}
	public TreeNodeImpl(boolean canAdopt) {
		this();
		setAllowsChildren(canAdopt);
	}
	@Override
	public TreeNode getChildAt(int childIndex) {
		if (childIndex >= children.size() || childIndex < 0)
			return null;
		return children.get(childIndex);
	}

	@Override
	public int getChildCount() {
		return children.size();
	}

	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public int getIndex(TreeNode node) {
		if (node == null) return -1;
		return children.indexOf(node);
	}

	@Override
	public boolean getAllowsChildren() {
		return canAdopt;
	}

	@Override
	public boolean isLeaf() {
		return (children.size()==0);
	}

	/* *******************************
	 * ***** Custom Methods **********
	 * *******************************
	 */
	public void setAllowsChildren(boolean b) {
		canAdopt = b;
	}
	public void setParent(TreeNodeImpl parent) {
		if (this.parent != null)
			this.parent.removeChild(this);
		this.parent = parent;
		if (parent != null)
			parent.addChild(this);
	}
	/**
	 * Adds a child to this TreeNode. Has no effect if it
	 * does not allow children.
	 * @param child
	 */
	public void addChild(TreeNode child) {
		if (canAdopt)
			children.add(child);
	}
	/**Adds a child to this TreeNode at the index. Has no effect
	 * if it does not allow children.
	 * @param child
	 * @param index
	 */
	public void insertChild(TreeNode child, int index) {
		if (canAdopt)
			children.add(index,child);
	}
	/**
	 * Removes a child from this TreeNode. Has no effect if it
	 * does not allow children.
	 * @param child
	 */
	public void removeChild(TreeNode child) {
		if (canAdopt)
			children.remove(child);
	}
	/**
	 * Removes the child at the index from this TreeNode. Has
	 * no effect if it does not allow children.
	 * @param index
	 */
	public void removeChild(int index) {
		if (canAdopt)
			children.remove(index);
	}
	public Object getUserObject() {
		return userObject;
	}
	public void setUserObject(Object object) {
		userObject = object;
	}
	public String getName() {
		return name;
	}
	public void setName(String newName) {
		name = newName;
	}
	/**
	 * Returns the node's name.
	 * @return Returns the same as getName()
	 */
	@Override
	public String toString() {
		return name;
	}
	@Override
	public Enumeration<TreeNode> children(){
		// TODO Auto-generated method stub
		return null;
	}
}
