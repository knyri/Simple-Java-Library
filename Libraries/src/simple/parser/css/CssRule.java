package simple.parser.css;

import simple.parser.ml.Tag;

public abstract class CssRule{
	private final String rule;
	public CssRule(String rule){
		this.rule= rule;
	}
	public abstract boolean test(Tag tag);

	@Override
	public String toString(){
		return rule;
	}
	public static final CssRule create(String rule, CssProperties props){
		// TODO create the rules man
		return null;
	}
}
