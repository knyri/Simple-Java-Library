/**
 *
 */
package simple.ml;

import java.util.Iterator;

import simple.CIString;
import simple.util.do_str;
import simple.util.tree.FullNode;

/**<hr>
 * Other dependencies:<br>
 * simple.CIString<br>
 * simple.util.tree.FullNode
 * <br>Created: Oct 16, 2010
 * @author Kenneth Pierce
 */
public final class Tag extends FullNode<String, CIString, String> implements Iterable<Tag> {
	public static final String CDATA = "CDATA", META = "DOCTYPE", SGMLCDATA = "SGMLCDATA", HTMLCOMM = "HCOM";
	private boolean selfClosing = false;
	//private static final Log log = LogFactory.getLogFor(Tag.class);
	public Tag() {
		this(false);
	}

	public Tag(final String name, final Tag parent,	final String content) {
		this(name, parent, content, false);
	}

	public Tag(final String name, final Tag parent) {
		this(name, parent, null, false);
	}

	public Tag(final String name, final String content) {
		this(name, null, content, false);
	}

	public Tag(final String name) {
		this(name, null, null, false);
	}
	public Tag(final String name, final boolean selfClosing) {
		this(name, null, null, selfClosing);
	}
	public Tag(final String name, final String content, final boolean selfClosing) {
		this(name, null, content, selfClosing);
	}
	public Tag(final String name, final Tag parent, final boolean selfClosing) {
		this(name, parent, null, selfClosing);
	}
	public Tag(final String name, final Tag parent, final String content, final boolean selfClosing) {
		super(name, parent, content);
		this.selfClosing = selfClosing;
	}
	public Tag(final boolean selfClosing) {
		this("", null, null, selfClosing);
	}

	public final Tag getParentTag(){
		return (Tag) getParent();
	}

	public final boolean isSelfClosing() {
		return selfClosing;
	}
	/**Sets this tag's indicator for self closing.<br>
	 * If it is set to <code>true</code> and this tag has children then the
	 * children will be copied to the parent. If there is no parent then the
	 * children will be destroyed.
	 * @param b
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
	 * @param name
	 * @return The child or null if it was not found.
	 */
	public final Tag getChild(final String name) {
		final String[] fname = name.split(";");
		int count = 0;//default to the first tag found
		if (fname.length==2) {
			count = Integer.parseInt(fname[1]);
		}
		for (final FullNode<String, CIString, String> node: children) {//iterate through all the children
			if (node.getName().equals(fname[0])) {
				if (count>0) {
					count--;
				} else return (Tag)node;
			}
		}
		return null;
	}
	/**Alias for (Tag){@link #getChild(int)}.
	 * @param index
	 * @return The child at the index.
	 */
	public final Tag getChildAt(final int index) {
		return (Tag)getChild(index);
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
		return ((Tag)getParent()).indexOfForName(this);
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
		for (final Iterator<FullNode<String, CIString, String>> it = children.iterator();it.hasNext();) {
			tmp = (Tag)it.next();
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
			for (Tag tmp = (Tag) getParent(); tmp != null; tmp = (Tag) tmp.getParent()) {
				buf.insert(0, tmp.getName()+";"+tmp.indexOfForName()+".");
			}
		}
		return null;
	}
	public String toStringTagOnly() {
		if (getName().equals(CDATA)) return getContent();
		if (getName().equals(SGMLCDATA)) return "<!CDATA[["+getContent()+"]]>";
		if (getName().equals(HTMLCOMM)) return getContent()+"\n";
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
		if (getName().equals(HTMLCOMM)) return "\n"+getContent();
		final StringBuilder buf = new StringBuilder(300);
		buf.append("\n<");
		buf.append(getName());
		for (final CIString key : properties.keySet()) {
			buf.append(" "+key+"=\""+properties.get(key)+"\"");
		}
		if (isSelfClosing()) {
			buf.append("/>\n");
			return buf.toString();
		}
		buf.append('>');
		for(Tag t: this){
			buf.append(t.toString());
		}
		buf.append("</"+getName()+">");
		return buf.toString();
	}
	public String toStringFormatted(final int depth) {
		if (getName().equals(CDATA) || getName().equals(META)) return getContent()+"\n";
		if (getName().equals(SGMLCDATA)) return "<!CDATA[["+getContent()+"]]>";
		if (getName().equals(HTMLCOMM)) return getContent()+"\n";
		final StringBuilder buf = new StringBuilder(300);
		String tabs=do_str.repeat('\t', depth);
		buf.append(tabs);
		buf.append('<');
		buf.append(getName());
		for (final CIString key : properties.keySet()) {
			buf.append(" "+key+"=\""+properties.get(key)+"\"");
		}
		if (isSelfClosing()) {
			buf.append("/>\n");
			return buf.toString();
		}
		buf.append('>');
		if(hasChild())
			buf.append('\n');
		for(Tag t: this){
			buf.append(t.toStringFormatted(tabs+"\t"));
		}
		buf.append(tabs);
		buf.append("</"+getName()+">\n");
		return buf.toString();
	}
	protected String toStringFormatted(final String tabs){
		if (getName().equals(CDATA) || getName().equals(META)) return getContent()+"\n";
		if (getName().equals(SGMLCDATA)) return "<!CDATA[["+getContent()+"]]>";
		if (getName().equals(HTMLCOMM)) return getContent()+"\n";
		final StringBuilder buf = new StringBuilder(300);
		buf.append(tabs);
		buf.append('<');
		buf.append(getName());
		for (final CIString key : properties.keySet()) {
			buf.append(" "+key+"=\""+properties.get(key)+"\"");
		}
		if (isSelfClosing()) {
			buf.append("/>\n");
			return buf.toString();
		}
		buf.append('>');
		if(hasChild())
			buf.append('\n');
		for(Tag t: this){
			buf.append(t.toStringFormatted(tabs+"\t"));
		}
		buf.append(tabs);
		buf.append("</"+getName()+">\n");
		return buf.toString();
	}
	@Override
	public boolean equals(final Object o) {
		if (o == this) return true;
		if (o instanceof Tag) {
			final Tag tmp = (Tag)o;
			if (tmp.getName()!=getName() && getName()!=null && !getName().equals(tmp.getName()))
				return false;
			if (tmp.getContent()!=getContent() && getContent() != null && !getContent().equals(tmp.getContent()))
				return false;
			if (!tmp.children.containsAll(children))
				return false;
			if (!tmp.properties.equals(properties))
				return false;
			return true;
		}
		return false;
	}
	public Tag getLastChild(){
		return (Tag)children.peekLast();
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Tag> iterator() {
		return new Iterator<Tag>() {
			private final Iterator<FullNode<String, CIString, String>> iter = children.iterator();
			@Override
			public boolean hasNext() {
				return iter.hasNext();
			}
			@Override
			public Tag next() {
				return (Tag)iter.next();
			}
			@Override
			public void remove() {iter.remove();}
		};
	}

	/**
	 * @param key
	 * @return
	 * @deprecated Use {{@link #getProperty(CIString)}
	 */
	@Deprecated
	public String getProperty(final String key) {
		return super.getProperty(new CIString(key));
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 * @deprecated Use {{@link #setProperty(CIString, String)}
	 */
	@Deprecated
	public Tag setProperty(final String key, final String value) {
		return (Tag)super.setProperty(new CIString(key), value);
	}

	/**
	 * @param key
	 * @return
	 * @deprecated Use {{@link #removeProperty(CIString)}
	 */
	@Deprecated
	public String removeProperty(final String key) {
		return super.removeProperty(new CIString(key));
	}

	/**
	 * @param key
	 * @return
	 * @deprecated Use {{@link #hasProperty(CIString)}
	 */
	@Deprecated
	public boolean hasProperty(final String key) {
		return super.hasProperty(new CIString(key));
	}

	/**
	 * Do Not use for iteration.
	 * Convenience method: ((Tag)getParent()).getChildAt(getSiblingIndex()+1);
	 * @return The sibling or null
	 */
	public Tag getNextSibling(){
		if(getParent() != null)
			return ((Tag)getParent()).getChildAt(getSiblingIndex()+1);
		return null;
	}
	/**
	 * Do Not use for iteration.
	 * Convenience method: ((Tag)getParent()).getChildAt(getSiblingIndex()-1);
	 * @return The sibling or null
	 */
	public Tag getPrevSibling(){
		if(getSiblingIndex() > 0 && getParent() != null)
			return ((Tag)getParent()).getChildAt(getSiblingIndex()-1);
		return null;
	}
}
