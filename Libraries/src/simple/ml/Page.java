/**
 * 
 */
package simple.ml;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import simple.CIString;
import simple.util.do_array;
import simple.util.logging.Log;
import simple.util.logging.LogFactory;

/**tag.tag.tag;number<br>
 * tag.tag.tag#attribute<br>
 * <b>Do not add multiple roots of the same name!</b> If you need to do so, make
 * a dummy root node and add them there.
 * <hr>
 * Other dependencies:
 * simple.CIString<br>
 * simple.util.do_array<br>
 * simple.util.logging.*<br>
 * simple.ml.Tag
 * <br>Created: Oct 16, 2010
 * @author Kenneth Pierce
 */
public class Page implements Iterable<Tag> {
	private static final Log log = LogFactory.getLogFor(Page.class);
	private final Hashtable<String,Tag> roots = new Hashtable<String,Tag>();
	/** Cache of all tags by their entity name(not the full canonical name) */
	private final Hashtable<CIString, Vector<Tag>> cache = new Hashtable<CIString, Vector<Tag>>();
	public Page() {}
	/**Adds a tag to the page.
	 * @param tag Tag to be added.
	 * @param where Qualified position to add the tag or null to add a new root tag.
	 */
	public void addTag(final Tag tag, final String where) {
		if (where==null || where.isEmpty()) {roots.put(tag.getName().toString(), tag); return;}
		getTag(where).addChild(tag);
	}
	/**Finds a tag using it's fully qualified name.<br>
	 * We have a document with this structure:<br>
	 * html
	 * <dl>
	 * <dt>body</dt>
	 * <dd>p</dd>
	 * <dd>p</dd>
	 * </dl>
	 * The fully qualified name of the 2nd 'p' element is 'html.body.p;1'
	 * @param where The fully qualified name of the element.
	 * @return The tag represented by <code>where</code> or <code>null</code>.
	 */
	public Tag getTag(final String where) {
		if (where==null || where.isEmpty()) return null;
		final String[] path = where.split("\\.");
		Tag t = null;
		if (path.length==1)
			return roots.get(path[0]);
		else {
			t = roots.get(path[0]);
			for(int i = 1;i<path.length;i++) {
				t = t.getChild(path[i]);
			}
		}
		return t;
	}
	/** Finds and returns all the tags with this name.
	 * @param name Name of the tags wanted.
	 * @return A Vector containing the tags.
	 */
	public Vector<Tag> getTags(final String name) {
		return cache.get(name);
	}
	/** Rebuilds the tag cache. Should be called after a full parsing of the
	 * page */
	public void rebuildCache() {
		log.information("building tag cache");
		cache.clear();
		Vector<Tag> tmp;
		for (final Tag tag : do_array.iterable(roots.elements())) {
			tmp = cache.get(tag.getName());
			if (tmp==null) {
				tmp = new Vector<Tag>();
				cache.put(tag.getName(), tmp);
			}
			tmp.add(tag);
			//log.debug(tag.toStringTagOnly());
			addToCache(tag);
		}
	}
	/**Adds the children of this tag to the cache recursively.
	 * @param tag
	 */
	private void addToCache(final Tag tag) {
		if (tag.isSelfClosing()) return;
		if (!tag.hasChild()) return;
		Vector<Tag> tmp;
		for (final Tag child : tag) {
			tmp = cache.get(child.getName());
			if (tmp==null) {
				tmp = new Vector<Tag>();
				cache.put(child.getName(), tmp);
			}
			//log.debug(tag.toStringTagOnly());
			tmp.add(child);
			addToCache(child);
		}
	}
	/**Recreates an equivalent to the original page. This will not be an exact
	 * duplication.
	 * @return Source equivalent to the original.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder buf = new StringBuilder(300);
		for (final Tag tag : roots.values()) {
			buf.append(tag.toString());
			buf.append('\n');
		}
		return buf.toString();
	}
	public String formattedSource() {
		final StringBuilder buf = new StringBuilder(300);
		for (final Tag tag : roots.values()) {
			buf.append(tag.toStringFormatted(0));
			buf.append('\n');
		}
		return buf.toString();
	}
	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<Tag> iterator() {
		final Vector<Tag> tags = new Vector<Tag>(50,50);
		for(final Vector<Tag> taglist: do_array.iterable(cache.elements())) {
			tags.addAll(taglist);
		}
		return tags.iterator();
	}
}
