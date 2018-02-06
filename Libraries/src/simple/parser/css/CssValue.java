package simple.parser.css;

import simple.CIString;
import simple.collections.CIHashtable;

public abstract class CssValue{
	private final String value;
	private final CIHashtable<Object> values= new CIHashtable<Object>();
	public CssValue(String raw){
		value= raw;
	}
/* ********
 * Setters
 * ********/
	public Object set(CIString k, Object v){
		return values.put(k, v);
	}
	public Integer setInteger(CIString k, Integer v){
		return (Integer)values.put(k,v);
	}
	public String setString(CIString k, String v){
		return (String)values.put(k, v);
	}
/* *******
 * Getters
 * *******/
	public Object get(CIString k){
		return values.get(k);
	}
	public Integer getInteger(CIString k){
		return (Integer)values.get(k);
	}
	public String getString(CIString k){
		return (String)values.get(k);
	}
/* ***************
 * Getters w/ defaults
 * ***************/
	public Object get(CIString k, Object d){
		Object ret= values.get(k);
		return ret == null ? d : ret;
	}
	public Integer getInteger(CIString k, Integer d){
		return (Integer)get(k,d);
	}
	public String getString(CIString k, String d){
		return (String)get(k, d);
	}
	@Override
	public String toString(){
		return value;
	}
}
