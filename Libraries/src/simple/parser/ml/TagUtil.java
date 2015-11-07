/**
 *
 */
package simple.parser.ml;

import java.util.LinkedList;

import simple.CIString;

/**
 * 10/10/2012
 * @author Ken
 *
 */
public final class TagUtil{
	public static final Tag findTagDeep(Tag parent, String tagname){
		return findTagDeep(parent,new CIString(tagname));
	}
	public static final Tag findTagDeep(Tag parent, CIString tagname){
		Tag found=null;
		for(Tag tag:parent){
			if(tag.getName().equals(tagname)){
				found=tag;
				break;
			}
			if(tag.hasChild()){
				found=findTagDeep(tag,tagname);
				if(found!=null)break;
			}
		}
		return found;
	}
	public static final LinkedList<Tag> findTagsDeep(Tag parent, CIString tagname){
		LinkedList<Tag> found=new LinkedList<Tag>();
		for(Tag tag:parent){
			if(tag.getName().equals(tagname)){
				found.add(tag);
			}
			if(tag.hasChild()){
				found.addAll(findTagsDeep(tag,tagname));
			}
		}
		return found;
	}
	public static final LinkedList<Tag> findTagsDeep(Tag parent, String tagname){
		return findTagsDeep(parent,new CIString(tagname));
	}
}
