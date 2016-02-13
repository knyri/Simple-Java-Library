package simple.parser.ml;

/**
 * Listens for changes in the children.
 * @author Ken Pierce
 */
public interface TagChildListener{
	public void childTagAdded(Tag parent, Tag child);
	public void childTagRemoved(Tag parent, Tag child);
}
