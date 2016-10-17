package simple.parser.ml;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import simple.CIString;
import simple.util.do_str;

/**
 * Iterable - Iterates over children
 * <hr>
 * Other dependencies:<br>
 * simple.CIString<br>
 * simple.util.tree.FullNode
 * <br>Created: Oct 16, 2010
 * @author Kenneth Pierce
 */
public final class Tag implements Iterable<Tag> {
	public static final CIString
		CDATA = new CIString("CDATA"),
		META = new CIString("DOCTYPE"),
		SGMLCDATA = new CIString("SGMLCDATA"),
		HTMLCOMM = new CIString("HCOM");
	protected final LinkedList<Tag> children = new LinkedList<Tag>();
	protected HashMap<CIString,String> properties = new HashMap<CIString,String>();
	private Tag parent = null;
	private CIString nName = null;
	private String content = null;
	/**
	 * Used to sync add/remove child operations
	 */
	private final Object sync=new Object();
	/**
	 * How far in the tree this Tag is.
	 * 0 is root.
	 */
	private int depth=0;



	private LinkedList<TagParentListener> parentListeners;
	private LinkedList<TagChildListener> childListeners;
	private boolean selfClosing = false;
	//private static final Log log = LogFactory.getLogFor(Tag.class);
	public Tag() {
		this(CIString.EMPTY, null, null, false);
	}

	public Tag(final String name, final Tag parent,	final String content) {
		this(new CIString(name), parent, content, false);
	}
	public Tag(final String name, final Tag parent) {
		this(new CIString(name), parent, null, false);
	}
	public Tag(final String name, final String content) {
		this(new CIString(name), null, content, false);
	}
	public Tag(final String name) {
		this(new CIString(name), null, null, false);
	}
	public Tag(final String name, final boolean selfClosing) {
		this(new CIString(name), null, null, selfClosing);
	}
	public Tag(final String name, final String content, final boolean selfClosing) {
		this(new CIString(name), null, content, selfClosing);
	}
	public Tag(final String name, final Tag parent, final boolean selfClosing) {
		this(new CIString(name), parent, null, selfClosing);
	}
	public Tag(final String name, final Tag parent, final String content, final boolean selfClosing) {
		this(new CIString(name), parent, content, selfClosing);
	}
	public Tag(final boolean selfClosing) {
		this(CIString.EMPTY, null, null, selfClosing);
	}
	public Tag(final CIString name, final Tag parent, final String content) {
		this(name, parent, content, false);
	}

	public Tag(final CIString name, final Tag parent) {
		this(name, parent, null, false);
	}
	public Tag(final CIString name, final String content) {
		this(name, null, content, false);
	}
	public Tag(final CIString name) {
		this(name, null, null, false);
	}
	public Tag(final CIString name, final boolean selfClosing) {
		this(name, null, null, selfClosing);
	}
	public Tag(final CIString name, final String content, final boolean selfClosing) {
		this(name, null, content, selfClosing);
	}
	public Tag(final CIString name, final Tag parent, final boolean selfClosing) {
		this(name, parent, null, selfClosing);
	}
	public Tag(final CIString name, final Tag parent, final String content, final boolean selfClosing) {
		setName(name);
		setParent(parent);
		setContent(content);
		this.selfClosing = selfClosing;
	}

	public void addParentListener(TagParentListener l){
		if(parentListeners == null){
			parentListeners= new LinkedList<>();
		}
		parentListeners.add(l);
	}
	public void addChildListener(TagChildListener l){
		if(childListeners == null){
			childListeners= new LinkedList<>();
		}
		childListeners.add(l);
	}
	private void fireNewParent(Tag oldP, Tag newP){
		if(parentListeners != null){
			for(TagParentListener p: parentListeners){
				p.newParentTag(this, oldP, newP);
			}
		}
	}
	private void fireNewChild(Tag child){
		if(childListeners != null){
			for(TagChildListener p: childListeners){
				p.childTagAdded(this, child);
			}
		}
	}
	private void fireChildRemoved(Tag child){
		if(childListeners != null){
			for(TagChildListener p: childListeners){
				p.childTagRemoved(this, child);
			}
		}
	}

	public final boolean isSelfClosing() {
		return selfClosing;
	}
	/**Sets this tag's indicator for self closing.<br>
	 * If it is set to <code>true</code> and this tag has children then the
	 * children will be copied to the parent. If there is no parent then the
	 * children will be destroyed.
	 * @param b true or false
	 */
	public void setSelfClosing(final boolean b) {
		selfClosing = b;
		if (b) {
			if (hasParent()) {
				while (children.size() > 0) {
					getParent().addChild(children.remove(0));
				}
			}
			removeAllChildren();
		}
	}
	/** Finds the child by name. Children with the same name can be
	 * differentiated like so <i>name;0 name;1</i>.
	 * @param name path to the tag or tag name
	 * @return The child or null if it was not found.
	 */
	public final Tag getChild(final String name) {
		final String[] fname = name.split(";");
		int count = 0;//default to the first tag found
		if (fname.length==2) {
			count = Integer.parseInt(fname[1]);
		}
		for (final Tag node: children) {
			if (node.getName().equals(fname[0])) {
				if (count>0) {
					count--;
				} else{
					return node;
				}
			}
		}
		return null;
	}
	/**
	 * Finds the first tag with this name
	 * @param name
	 * @return
	 */
	public final Tag getChild(final CIString name) {
		for (final Tag node: children) {
			if (node.getName().equals(name)) {
				return node;
			}
		}
		return null;
	}
	/**
	 * Finds the nth occurrence of the tag. (0 based)
	 * @param name
	 * @param count
	 * @return
	 */
	public final Tag getChild(final CIString name, int count) {
		for (final Tag node: children) {
			if (node.getName().equals(name)) {
				if (count>0) {
					count--;
				} else{
					return node;
				}
			}
		}
		return null;
	}
	/**
	 * Searches this tag and all child tags for a tag
	 * with this name.
	 * @param name
	 * @return A list
	 */
	public final List<Tag> findTag(CIString name){
		List<Tag> ret= getChildren(name);
		for (final Tag node: children) {
			ret.addAll(node.findTag(name));
		}
		return ret;
	}
	/**
	 * Faster than the String counterpart
	 * @param tagName
	 * @return
	 */
	public final List<Tag> getChildren(CIString tagName){
		List<Tag> tags= new LinkedList<>();
		for (final Tag node: children) {
			if (node.getName().equals(tagName)) {
				tags.add(node);
			}
		}
		return tags;
	}
	public final List<Tag> getChildren(String tagName){
		List<Tag> tags= new LinkedList<>();
		for (final Tag node: children) {
			if (node.getName().equals(tagName)) {
				tags.add(node);
			}
		}
		return tags;
	}
	/**
	 * Gets the child at the index
	 * @param index Index of the wanted child
	 * @return The child at the index.
	 */
	public final Tag getChildAt(final int index) {
		return getChild(index);
	}
	public final int indexOf(final Tag tag) {
		return children.indexOf(tag);
	}
	/**Calls <code>indexOfForName(this)</code> on the parent Tag if one exists.
	 * If there is no parent tag then 0 is returned.
	 * @return The index of this tag or 0 if it has no parent. -1 is possible and
	 * 		should be a red flag that the using code circumvented either the
	 * 		addChild() or removeChild() methods.
	 * @see #indexOfForName(Tag)
	 */
	private final int indexOfForName() {
		if (!hasParent())
			return 0;
		return getParent().indexOfForName(this);
	}
	/**Finds the tag counting all the tags with the same name along the way. This
	 * count is returned.
	 * @param tag Tag to be found
	 * @return The number of tags with the same name that appear before <code>tag</code>
	 * 		or -1 if <code>tag</code> was not found.
	 */
	protected final int indexOfForName(final Tag tag) {
		Tag tmp;
		int index = 0;
		for (final Iterator<Tag> it = children.iterator();it.hasNext();) {
			tmp = it.next();
			if (tmp.equals(tag)) return index;
			if (tmp.getName().equals(tag.getName())) {
				index++;
			}
		}
		return -1;
	}
	/**Creates the fully qualified name associated with this tag.
	 * @return The fully qualified name associated with this tag.
	 */
	public final String getCanonicalName() {
		final StringBuilder buf = new StringBuilder(60);
		buf.append(getName());
		if (hasParent()) {
			buf.append(";"+indexOfForName());
			for (Tag tmp = getParent(); tmp != null; tmp = tmp.getParent()) {
				buf.insert(0, tmp.getName()+";"+tmp.indexOfForName()+".");
			}
		}
		return null;
	}
	/**
	 * A string of this tag. Does not include child tags.
	 * @return A string representing this tag
	 */
	public String toStringTagOnly() {
		if (getName().equals(CDATA)) return getContent();
		if (getName().equals(SGMLCDATA)) return "<!CDATA[["+getContent()+"]]>";
		if (getName().equals(HTMLCOMM)) return getContent();
		final StringBuilder buf = new StringBuilder(300);
		buf.append('<');
		buf.append(getName());
		for (final CIString key : properties.keySet()) {
			buf.append(" "+key+"=\""+properties.get(key)+"\"");
		}
		if (isSelfClosing()) {
			buf.append("/>");
			return buf.toString();
		}
		buf.append(">");
		return buf.toString();
	}
	@Override
	public String toString() {
		if (getName().equals(CDATA) || getName().equals(META)) return getContent();
		if (getName().equals(SGMLCDATA)) return "<!CDATA[["+getContent()+"]]>";
		if (getName().equals(HTMLCOMM)) return getContent();
		final StringBuilder buf = new StringBuilder(300);
		buf.append("<");
		buf.append(getName());
		for (final CIString key : properties.keySet()) {
			buf.append(" "+key+"=\""+properties.get(key)+"\"");
		}
		if (isSelfClosing()) {
			buf.append("/>");
			return buf.toString();
		}
		buf.append('>');
		for(Tag t: this){
			buf.append(t.toString());
		}
		buf.append("</"+getName()+">");
		return buf.toString();
	}
	/**
	 * @param depth
	 * @return
	 */
	public String toStringFormatted(final int depth) {
		if (getName().equals(CDATA)) return getContent();
		if (getName().equals(SGMLCDATA)) return "<!CDATA[["+getContent()+"]]>";
		if (getName().equals(HTMLCOMM)  || getName().equals(META)) return getContent()+'\n';
		return toStringFormatted(do_str.repeat('\t', depth));
	}
	/**
	 * @param tabs
	 * @return
	 */
	protected String toStringFormatted(final String tabs){
		if (getName().equals(CDATA)) return getContent();
		if (getName().equals(SGMLCDATA)) return "<!CDATA[["+getContent()+"]]>";
		if (getName().equals(HTMLCOMM) || getName().equals(META)) return getContent()+'\n';
		final StringBuilder buf = new StringBuilder(300);
		if(hasParent() && getParent().childCount() != 1)
			buf.append(tabs);
		buf
			.append('<')
			.append(getName());
		for (final CIString key : properties.keySet()) {
			buf
				.append(" ")
				.append(key)
				.append("=\"")
				.append(properties.get(key))
				.append("\"");
		}
		if (isSelfClosing()) {
			buf.append("/>");
			return buf.toString();
		}
		buf.append('>');
		if(hasChild()){
			if(childCount() == 1){
				buf.append(getChild(0).toStringFormatted(tabs+"\t"));
			}else{
				buf.append('\n');
				for(Tag t: this){
					buf
						.append(t.toStringFormatted(tabs+"\t"))
						.append('\n');
				}
				buf.append(tabs);
			}
		}
		buf
			.append("</")
			.append(getName())
			.append(">");
		return buf.toString();
	}
	@Override
	public boolean equals(final Object o) {
		if (o == this) return true;
		if (o instanceof Tag) {
			final Tag tmp = (Tag)o;
			if (tmp.getName()!=getName() && getName()!=null && !getName().equals(tmp.getName()))
				return false;
			if (tmp.getContent()!=getContent() && getContent() != null && !getContent().equals(tmp.getContent())){
				return false;
			}
			if (!tmp.properties.equals(properties)){
				return false;
			}
			if (children.size() != tmp.children.size() || !tmp.children.containsAll(children)){
				return false;
			}
			return true;
		}
		return false;
	}
	public Tag getLastChild(){
		return children.peekLast();
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Tag> iterator() {
		return new Iterator<Tag>() {
			private final Iterator<Tag> iter = children.iterator();
			@Override
			public boolean hasNext() {
				return iter.hasNext();
			}
			@Override
			public Tag next() {
				return iter.next();
			}
			@Override
			public void remove() {iter.remove();}
		};
	}

	/**
	 * @param key Property key
	 * @return property value
	 * @deprecated Use {{@link #getProperty(CIString)}
	 */
	@Deprecated
	public String getProperty(final String key) {
		return getProperty(new CIString(key));
	}

	/**
	 * Use is discouraged unless building a page from a file.
	 * @param key Property key
	 * @param value property value
	 * @return this
	 */
	public Tag setProperty(final String key, final String value) {
		return setProperty(new CIString(key), value);
	}

	/**
	 * @param key property key
	 * @return property value
	 * @deprecated Use {@link #removeProperty(simple.CIString)}
	 */
	@Deprecated
	public String removeProperty(final String key) {
		return removeProperty(new CIString(key));
	}

	/**
	 * @param key property key
	 * @return property value
	 * @deprecated Use {@link #hasProperty(simple.CIString)}
	 */
	@Deprecated
	public boolean hasProperty(final String key) {
		return hasProperty(new CIString(key));
	}


	/**
	 * Sets the contents of the node.
	 * @param object Object to set as the contents.
	 * @return this
	 */
	public final Tag setContent(String object){content = object; return this;}
	/**
	 * Contents of the Node. May be null.
	 * @return The contents of the node.
	 */
	public final String getContent() {return content;}
	/**
	 * Sets the name of the node.
	 * @param name String to set as the Node's name.
	 * @return this
	 */
	public final Tag setName(String name) {nName=new CIString(name);return this;}
	public final Tag setName(CIString name) {nName=name;return this;}
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
	public final Tag setParent(Tag pnode) {
		if (parent==pnode) return null;
		Tag oldp= parent;
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
		fireNewParent(oldp, pnode);
		return oldp;
	}
	/**
	 * @return The result of <code>getParent()!=null</code>.
	 */
	public final boolean hasParent() {return parent != null;}
	/**
	 * @return The parent of this node.
	 */
	public final Tag getParent() {return parent;}
	public final Tag getParent(int levels) {
		Tag ret= parent;
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
	public final boolean hasChild(Tag child) {
		return children.contains(child);
	}
	/**
	 * @param parent Possible parent node.
	 * @return The result of <code><var>parent</var>.hasChild(this)</code>.
	 */
	public final boolean isChildOf(Tag parent) {
		return parent.hasChild(this);
	}
	public final void addChildren(Collection<? extends Tag> nodes){
		for(Tag n: nodes){
			addChild(n);
		}
	}
	/**
	 * Adds a child Node and sets this as the node's parent. Does nothing if <code><var>node</var>==null</code>.
	 * @param cnode Node to be added as a child.
	 * @return this
	 */
	public final Tag addChild(Tag cnode) {
		if (cnode==null) return this;
		synchronized(sync){
			children.add(cnode);
			fireNewChild(cnode.setParent(this));
		}
		return this;
	}
	/**
	 * Adds a child Node and sets this as the node's parent. Does nothing if <code><var>node</var>==null</code>.
	 * @param cnode Node to be added as a child.
	 * @param index index to insert the child
	 * @return this
	 */
	public final Tag insertChild(Tag cnode, int index) {
		if (cnode==null) return this;
		if (index >= children.size() || index < 0) return this;
		synchronized(sync){
			children.add(index, cnode);
			fireNewChild(cnode.setParent(this));
		}
		return this;
	}
	/**
	 * Gets a child at the index specified.
	 * @param index Index of the child wanted.
	 * @return Child Node at specified index or null if it has no children or the index is
	 * out of the valid range(0&gt;= index &gt; childCount()).
	 */
	public final Tag getChild(int index) {
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
	public Tag removeChild(Tag child) {
		synchronized(sync){
			if(children.remove(child)){
				fireChildRemoved(child);
			}
		}
		return this;
	}
	/**
	 * Removes the child Node at the specified index if getChild(index)!=null.
	 * @param index Index of Node to be removed.
	 * @return this
	 */
	public final Tag removeChild(int index) {
		if (index < children.size() && index >= 0){
			synchronized(sync){
				fireChildRemoved(children.remove(index).setParent(null));
			}
		}
		return this;
	}
	/**
	 * Removes all children.
	 * @return this
	 */
	public final Tag removeAllChildren(){
		synchronized(sync){
			for(int index=0;index<children.size();index++){
				fireChildRemoved(children.pop().setParent(null));
			}
		}
		return this;
	}
	public final Map<CIString, String> getProperties(){
		return properties;
	}
	/**
	 * Adds these properties to this tag
	 * @param props Properties to set
	 * @return this
	 */
	public final Tag setProperties(Map<CIString,String> props){
		properties.putAll(props);
		return this;
	}
	/**
	 * @param key The key for the value.
	 * @return The value of the property.
	 */
	public final String getProperty(CIString key) {
		return properties.get(key);
	}
	/**
	 * Adds the key/value pair to the properties. If the key already exists then
	 * the value for that key is replaced with the one provided.
	 * @param key Key for the value.
	 * @param value Value to be set for the property.
	 * @return This node
	 */
	public final Tag setProperty(CIString key, String value) {
		properties.put(key, value);
		return this;
	}
	/**Removes the key/value pair.
	 * @param key the key
	 * @return The value associated with the key.
	 */
	public final String removeProperty(CIString key) {
		return properties.remove(key);
	}
	/**
	 * @param key the key
	 * @return True if the properties table contains the specified key.
	 */
	public final boolean hasProperty(CIString key) {
		return properties.containsKey(key);
	}

	/**Get which level of the tree the element is on. 0 based.
	 * @return The depth of this node
	 */
	public final int getDepth() {
		return depth;
	}
	public final Tag[] getChildren(){
		return children.toArray(new Tag[children.size()]);
	}
	/**
	 * Returns the text content of this tag and
	 * it's sub tags. Similar to HTML's Node.textContent.
	 * @return The text content of this tag and sub-tags
	 */
	public String getTextContent(){
		if(this.getName().equals(Tag.CDATA)){
			return this.getContent();
		}
		StringBuilder buf= new StringBuilder();
		for(Tag tag: children){
			tag.getText(buf);
		}
		return buf.toString();
	}
	private void getText(StringBuilder buf){
		if(this.getName().equals(Tag.CDATA)){
			buf.append(this.getContent());
		}else{
			for(Tag tag: children){
				tag.getText(buf);
			}
		}
	}
}
