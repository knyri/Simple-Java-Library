package simple.parser.ini;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class IniSection implements Map<String, String>, Serializable{

	private static final long serialVersionUID= 1L;
	private final String name;
	private final HashMap<String, String> props= new HashMap<String, String>();

	public IniSection(String name) {
		this.name= name;
	}

	@Override
	public void putAll(Map<? extends String, ? extends String> properties){
		for(Map.Entry<? extends String, ? extends String> prop: properties.entrySet()){
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
	 * @param section Section to copy from
	 */
	public void putIfAbsent(IniSection section){
		for(Map.Entry<String, String> prop: section.props.entrySet()){
			props.putIfAbsent(prop.getKey(), prop.getValue());
		}
	}


	@Override
	public String putIfAbsent(String key, String value){
		if(value == null){
			throw new IllegalArgumentException("value cannot be null");
		}
		return props.putIfAbsent(key, value);
	}
	@Override
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
	@Override
	public String get(Object key){
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
	@Override
	public String remove(Object key){
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
	@Override
	public void clear(){
		props.clear();
	}


	/**
	 * @return
	 */
	@Override
	public Set<Map.Entry<String, String>> entrySet(){
		return props.entrySet();
	}

	@Override
	public boolean containsKey(Object key){
		return props.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value){
		return props.containsValue(value);
	}

	@Override
	public boolean isEmpty(){
		return props.isEmpty();
	}

	@Override
	public Set<String> keySet(){
		return props.keySet();
	}

	@Override
	public int size(){
		return props.size();
	}

	@Override
	public Collection<String> values(){
		return props.values();
	}

}
