/**
 *
 */
package simple.gui;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/** Attempt at taming JTrees
 * <br>Created: Nov 29, 2009
 * @author Kenneth Pierce
 */
public class SimpleJTreeModel implements TreeModel {
	private final Root root;
	final Vector<TreeModelListener> listeners = new Vector<TreeModelListener>();
	public SimpleJTreeModel() {
		root = new Root("Root", "root");
	}
	public SimpleJTreeModel(final String rootName, final String rootAction) {
		root = new Root(rootName, rootAction);
	}
	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#addTreeModelListener(javax.swing.event.TreeModelListener)
	 */
	public void addTreeModelListener(final TreeModelListener tml) {
		listeners.add(tml);
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
	 */
	public Node getChild(final Object parent, final int index) {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
	 */
	public int getChildCount(final Object parent) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)
	 */
	public int getIndexOfChild(final Object parent, final Object child) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getRoot()
	 */
	public Root getRoot() {
		return root;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
	 */
	public boolean isLeaf(final Object node) {
		if (node instanceof BasicNode)
			return ((BasicNode)node).isLeaf;
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#removeTreeModelListener(javax.swing.event.TreeModelListener)
	 */
	public void removeTreeModelListener(final TreeModelListener tml) {
		listeners.remove(tml);
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath, java.lang.Object)
	 */
	public void valueForPathChanged(final TreePath path, final Object newValue) {
	}
	private static class BasicNode implements TreeNode {
		final String name, action;
		final boolean isRoot, isLeaf;
		final Vector<BasicNode> children;
		BasicNode parent = null;
		public BasicNode(final String name, final String action, final boolean isRoot, final boolean isLeaf) {
			this.name = name;
			this.action = action;
			this.isRoot = isRoot;
			this.isLeaf = isLeaf;
			if (isLeaf)
				children = null;
			else
				children = new Vector<BasicNode>();
		}
		protected void setParent(final BasicNode parent) {
			this.parent = parent;
		}
		public BasicNode removeChild(final int index) {
			if (getAllowsChildren())
				return children.remove(index);
			return null;
		}
		public boolean removeChild(final BasicNode child) {
			if (getAllowsChildren())
				return children.remove(child);
			return true;
		}
		public boolean addChild(final BasicNode child) {
			if (getAllowsChildren())
				return children.add(child);
			return false;
		}

		@Override
		public String toString() {
			return name;
		}
		/* (non-Javadoc)
		 * @see javax.swing.tree.TreeNode#children()
		 */
		public Enumeration<BasicNode> children() {
			if (getAllowsChildren())
				return children.elements();
			return null;
		}
		/* (non-Javadoc)
		 * @see javax.swing.tree.TreeNode#getAllowsChildren()
		 */
		public boolean getAllowsChildren() {
			return !isLeaf;
		}
		/* (non-Javadoc)
		 * @see javax.swing.tree.TreeNode#getChildAt(int)
		 */
		public TreeNode getChildAt(final int index) {
			if (getAllowsChildren())
				return children.elementAt(index);
			return null;
		}
		/* (non-Javadoc)
		 * @see javax.swing.tree.TreeNode#getChildCount()
		 */
		public int getChildCount() {
			if (getAllowsChildren())
				return children.size();
			return 0;
		}
		/* (non-Javadoc)
		 * @see javax.swing.tree.TreeNode#getIndex(javax.swing.tree.TreeNode)
		 */
		public int getIndex(final TreeNode child) {
			if (getAllowsChildren())
				return children.indexOf(child);
			return 0;
		}
		/* (non-Javadoc)
		 * @see javax.swing.tree.TreeNode#getParent()
		 */
		public TreeNode getParent() {
			return parent;
		}
		/* (non-Javadoc)
		 * @see javax.swing.tree.TreeNode#isLeaf()
		 */
		public boolean isLeaf() {
			return isLeaf;
		}
	}
	public static class Root extends BasicNode {
		public Root(final String name, final String action) {
			super(name, action, true, false);
		}
	}
	public static class Node extends BasicNode {
		public Node(final String name, final String action) {
			super(name, action, false, false);
		}
		public Node(final Node parent, final String name, final String action) {
			this(name, action);
			setParent(parent);
		}
		protected Node(final String name, final String action, final boolean isRoot, final boolean isLeaf) {
			super (name, action, isRoot, isLeaf);
		}
	}
	public static class Leaf extends Node {
		public Leaf(final String name, final String action) {
			super(name, action, false, true);
		}
		public Leaf(final Node parent, final String name, final String action) {
			this(name, action);
			setParent(parent);
		}
	}
}
