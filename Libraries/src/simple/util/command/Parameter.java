/**
 *
 */
package simple.util.command;

/**
 * <hr>
 * <br>Created: Sep 11, 2011
 * @author Kenneth Pierce
 */
public class Parameter{
	private boolean optional;
	private String tag;
	public Parameter(final String tag, final boolean optional) {
		setOptional(optional);
		setTag(tag);
	}
	public void setOptional(final boolean setting) {	optional = setting;	}
	public boolean isOptional() {	return optional;	}
	public void setTag(final String Tag) {	tag = Tag;	}
	public String getTag() {	return tag;	}
}
