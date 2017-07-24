package simple.parser.ini;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class IniSection{

	private final String name;
	private final HashMap<String, String> props= new HashMap<>();

	public IniSection(String name) {
		this.name= name;
	}

	public void putAll(Map<String, String> properties){
		for(Map.Entry<String, String> prop: properties.entrySet()){
			// to apply the null check
			put(prop.getKey(), prop.getValue());
		}
	}
	public void putAll(IniSection section){
		props.putAll(section.props);
	}

	/**
	 * Puts the values if the key isn't already defined.
	 * Nice for applying default values
	 * @param properties
	 */
	public void putIfAbsent(Map<String, String> properties){
		for(Map.Entry<String, String> prop: properties.entrySet()){
			// to apply the null check
			putIfAbsent(prop.getKey(), prop.getValue());
		}
	}
	/**
	 * Puts the values if the key isn't already defined.
	 * Nice for applying default values
	 * @param section
	 */
	public void putIfAbsent(IniSection section){
		for(Map.Entry<String, String> prop: section.props.entrySet()){
			props.putIfAbsent(prop.getKey(), prop.getValue());
		}
	}


	public String putIfAbsent(String key, String value){
		if(value == null){
			throw new IllegalArgumentException("value cannot be null");
		}
		return props.putIfAbsent(key, value);
	}
	public String put(String key, String value){
		if(value == null){
			throw new IllegalArgumentException("value cannot be null");
		}
		return props.put(key, value);
	}

	/**
	 * @param key
	 * @return The value or null
	 */
	public String get(String key){
		return props.get(key);
	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return The value or the default
	 */
	public String get(String key, String defaultValue){
		return props.getOrDefault(key, defaultValue);
	}

	/**
	 * @param key
	 * @return Value of the removed key or null
	 */
	public String remove(String key){
		return props.remove(key);
	}

	/**
	 * @return Name of the section
	 */
	public String getName(){
		return name;
	}

	/**
	 * Removes all keys and values
	 */
	public void clear(){
		props.clear();
	}


	/**
	 * @return
	 */
	public Set<Map.Entry<String, String>> entrySet(){
		return props.entrySet();
	}


}
