package simple.parser.css;

import simple.CIString;
import simple.util.CIHashtable;

public class CssProperties{
	private final CIHashtable<Object> props= new CIHashtable<Object>();
	public CssProperties(){
		// TODO Auto-generated constructor stub
	}
	public String setStringProperty(CIString property, String value){
		return (String)props.put(property,value);
	}

}
