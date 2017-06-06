package simple.parser.ini;

import java.util.HashMap;
import java.util.Set;

public class IniSection{

	private final String name;
	private final HashMap<String, String> props= new HashMap<>();

	public IniSection(String name) {
		this.name= name;
	}

	public String put(String key, String value){
		if(value == null){
			throw new NullPointerException("value cannot be null");
		}
		return props.put(key, value);
	}
	public String get(String key){
		return props.get(key);
	}
	public String get(String key, String defaultValue){
		return props.getOrDefault(key, defaultValue);
	}
	public String remove(String key){
		return props.remove(key);
	}

	public String getName(){
		return name;
	}

	public void clear(){
		props.clear();
	}


	public Set<HashMap.Entry<String, String>> entrySet(){
		return props.entrySet();
	}


}
