package simple.parser.ml;

/**
 * Listens for changes in the parent tag
 * @author Ken Pierce
 */
public interface TagParentListener{
	public void newParentTag(Tag child, Tag oldP, Tag newP);
}
