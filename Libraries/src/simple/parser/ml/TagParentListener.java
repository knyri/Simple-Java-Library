package simple.parser.ml;

public interface TagParentListener{
	public void newParentTag(Tag child, Tag oldP, Tag newP);
}