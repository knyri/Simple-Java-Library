/**
 *
 */
package simple.parser.ml;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import simple.CIString;
import simple.util.FixedSizeArrayList;
import simple.util.ImmutableList;
import simple.util.logging.Log;
import simple.util.logging.LogFactory;

/**tag.tag.tag;number<br>
 * tag.tag.tag#attribute<br>
 * Not thread safe.
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
public class Page implements Iterable<Tag>, TagParentListener {
	private static final Log log = LogFactory.getLogFor(Page.class);
	private static final List<Tag> EMPTY= new FixedSizeArrayList<Tag>(new Tag[0]);
	private final LinkedList<Tag> roots = new LinkedList<Tag>();
	private final ImmutableList<Tag> publicRoots= new ImmutableList<Tag>(roots);
	/** Cache of all tags by their entity name(not the full canonical name) */
	private final HashMap<CIString, List<Tag>> cache = new HashMap<CIString, List<Tag>>();
	public Page() {}
	/**
	 * Based on the cache.
	 * @return Number of tags in this page
	 */
	public final long getTagCount(){
		long count=0;
		for(List<Tag> tags: cache.values()){
			count+= tags.size();
		}
		return count;
	}
	/**
	 * Time consuming count as it iterates over all tags.
	 * @return the true number of tags
	 */
	public final long getTrueTagCount(){
		long count= publicRoots.size();
		for(Tag root: publicRoots){
			if(root.hasChild()){
				count += countTags(root);
			}
		}
		return count;
	}
	private static final long countTags(Tag tag){
		long count= tag.childCount();
		for(Tag child: tag){
			count+= countTags(child);
		}
		return count;
	}
	/**
	 * Gets the list of root elements.
	 * @return An immutable list of the root elements
	 */
	public final ImmutableList<Tag> getRoots(){
		return publicRoots;
	}
	/**Adds a tag to the page.
	 * @param tag Tag to be added.
	 * @param where Qualified position to add the tag or null to add a new root tag.
	 */
	public void addTag(final Tag tag, final String where) {
		if (where==null || where.isEmpty()) {roots.add(tag); return;}
		getTag(where).addChild(tag);
	}
	/**
	 * Finds a tag using it's fully qualified name.<br>
	 * Does not depend on the tag cache.<br>
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
			return getRoot(path[0]);
		else {
			t = getRoot(path[0]);
			for(int i = 1;i<path.length;i++) {
				t = t.getChild(path[i]);
			}
		}
		return t;
	}
	public final Tag getRoot(final String name) {
		final String[] fname = name.split(";");
		int count = 0;
		if (fname.length==2)
			count = Integer.parseInt(fname[1]);

		for (final Tag node: roots) {
			if (node.getName().equals(fname[0])) {
				if (count>0) {
					count--;
				} else return node;
			}
		}
		return null;
	}
	/**
	 * Finds and returns all the tags with this name.<br>
	 * Will be inaccurate if the cache is not rebuilt after
	 * the page is modified.
	 * @param name Name of the tags wanted.
	 * @return A Vector containing the tags.
	 */
	public List<Tag> getTags(final String name) {
		return cache.get(new CIString(name));
	}
	/**
	 * Finds and returns all the tags with this name.<br>
	 * Will be inaccurate if the cache is not rebuilt after
	 * the page is modified.
	 * @param name Name of the tags wanted.
	 * @return A List containing the tags.
	 */
	public List<Tag> getTags(final CIString name) {
		List<Tag> ret= cache.get(name);
		return ret == null ? EMPTY : ret;
	}
	void addTagToCache(Tag t){
		List<Tag> tmp= cache.get(t.getName());
		if (tmp==null) {
			tmp = Collections.synchronizedList(new LinkedList<Tag>());
			cache.put(t.getName(), tmp);
		}
		synchronized(tmp){
			tmp.add(t);
		}
	}
	void removeTagFromCache(Tag t){
		//TODO: bother with children?
		List<Tag> tmp= cache.get(t.getName());
		if(tmp!=null){
			synchronized(tmp){
				tmp.remove(t);
			}
		}
	}
	/**
	 * Rebuilds the tag cache. Should be called after a full parsing of the
	 * page or after removing/adding children.
	 */
	public void rebuildCache() {
		log.debug("building tag cache");
		cache.clear();
		List<Tag> tmp;
		for (final Tag tag : roots) {
			tmp = cache.get(tag.getName());
			if (tmp==null) {
				tmp = new LinkedList<Tag>();
				cache.put(tag.getName(), tmp);
			}
			tmp.add(tag);
			//log.debug(tag.toStringTagOnly());
			addToCache(tag);
		}
		log.debug("tag cache done");
	}
	/**Adds the children of this tag to the cache recursively.
	 * @param tag tag to add
	 */
	private void addToCache(final Tag tag) {
		if (tag.isSelfClosing() || !tag.hasChild()) return;
		List<Tag> tmp;
		for (final Tag child : tag) {
			tmp = cache.get(child.getName());
			if (tmp==null) {
				tmp = new LinkedList<Tag>();
				cache.put(child.getName(), tmp);
			}
			//log.debug(tag.toStringTagOnly());
			tmp.add(child);
			addToCache(child);
		}
	}
	/**
	 * Recreates an equivalent to the original page. This will not be an exact
	 * duplication.
	 * @return Source equivalent to the original.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder buf = new StringBuilder(2048);
		for (final Tag tag : roots) {
			buf.append(tag.toString());
			buf.append('\n');
		}
		return buf.toString();
	}
	/**
	 * Generates the pretty print of the page.
	 * @return the formatted source
	 * @deprecated Use a {@link PageFormatter}
	 */
	@Deprecated
	public String formattedSource() {
		final StringBuilder buf = new StringBuilder(2048);
		for (final Tag tag : roots) {
			buf.append(tag.toStringFormatted(0));
			buf.append('\n');
		}
		return buf.toString();
	}
	@Override
	public Iterator<Tag> iterator() {
		return new Iterator<Tag>() {
			private Iterator<Tag> iter = roots.iterator();
			// Storage for the depth first tree
			private final LinkedList<Iterator<Tag>> iterators= new LinkedList<Iterator<Tag>>();
			Tag current;
			@Override
			public boolean hasNext() {
				if(current != null && current.hasChild()){
					// Move down the tree
					iterators.addLast(iter);
					iter= current.iterator();
				}
				while(!iter.hasNext() && !iterators.isEmpty()){
					// move up the tree
					iter= iterators.removeLast();
				}
				return iter.hasNext();
			}
			@Override
			public Tag next() {
				return current= iter.next();
			}
			@Override
			public void remove() {iter.remove();}
		};
	}
	@Override
	public void newParentTag(Tag child, Tag oldP, Tag newP){
		if(oldP == null){
			addTagToCache(child);
		}else if(newP == null){
			removeTagFromCache(child);
		}
	}
}
