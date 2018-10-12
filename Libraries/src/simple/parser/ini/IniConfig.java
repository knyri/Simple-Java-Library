package simple.parser.ini;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;

import simple.io.ParseException;

/**
 * Very basic INI reader/writer
 *
 * Supports sections (no sub-sections)
 * Is case sensitive
 * Does not escape or unescape values/keys
 * Does not preserve comments
 * Uses the platform dependent EOL sequence when writing
 *
 * When reading:
 * - multiple sections with the same name are combined
 * - the last value is used if there are keys with the same name in the same section
 *
 * @author Ken
 *
 */
public class IniConfig extends IniSection implements Iterable<IniSection>{

	private final HashMap<String, IniSection> sections= new HashMap<String, IniSection>();
	public IniConfig() {super("default");}

	public boolean sectionExists(String name){
		return sections.containsKey(name);
	}
	/**
	 * @param name
	 * @return The new section
	 */
	public IniSection addSection(String name){
		IniSection ret= new IniSection(name);
		sections.put(name, ret);
		return ret;
	}
	/**
	 * @param section
	 * @return The replaced section or null
	 */
	public IniSection addSection(IniSection section){
		return sections.put(section.getName(), section);
	}
	/**
	 * Adds the section or combines it with an existing section
	 * @param section
	 * @return The combined section
	 */
	public IniSection addOrCombine(IniSection section){
		IniSection ret= sections.get(section.getName());
		if(ret == null){
			sections.put(section.getName(), section);
			ret= section;
		}
		if(ret != section){
			ret.putAll(section);
		}
		return ret;
	}

	/**
	 * Adds or combines all sections and keys.
	 * @param config
	 */
	public void putAll(IniConfig config){
		putAll((IniSection)config);
		for(IniSection section: config){
			addOrCombine(section);
		}
	}

	/**
	 * Applies all key/value pairs to the default section and all sections in this config if they don't exist.
	 *
	 * Sections that don't exist are created and the keys/values copied.
	 *
	 * @param config
	 */
	public void putIfAbsent(IniConfig config){
		putIfAbsent((IniSection)config);
		for(IniSection section: config){
			getSection(section.getName()).putIfAbsent(section);
		}
	}


	/**
	 * Returns the section. The section is created if it doesn't exist.
	 * @param name
	 * @return The section
	 */
	public IniSection getSection(String name){
		IniSection ret= sections.get(name);
		if(ret == null){
			ret= new IniSection(name);
			sections.put(name, ret);
		}
		return ret;
	}
	/**
	 * @param name
	 * @return The removed section or null if the section didn't exist
	 */
	public IniSection removeSection(String name){
		return sections.remove(name);
	}

	@Override
	public Iterator<IniSection> iterator() {
		return sections.values().iterator();
	}

	public void clearSections(){
		sections.clear();
	}

	/**
	 * Parses the INI file and adds the values to this. Effectively combining them
	 * @param ini
	 * @throws IOException If a read error occurs
	 * @throws ParseException If the INI is improperly formatted
	 */
	public void load(Reader ini) throws IOException, ParseException{
		LineNumberReader iniLR= new LineNumberReader(ini);
		String line, trimmed;
		int splitIdx;
		IniSection section= this;
		while(null != (line= iniLR.readLine()) ){
			trimmed= line.trim();
			if(trimmed.isEmpty() || line.charAt(0) == ';'){
				continue;
			}
			if(line.charAt(0) == '['){
				line= line.trim();
				if(line.charAt(line.length() - 1) != ']'){
					throw new ParseException("Missing matching ']' on line " + iniLR.getLineNumber());
				}
				section= this.getSection(line.substring(1, line.length() - 1));
			}else{
				splitIdx= line.indexOf('=');
				if(-1 == splitIdx || (line.length() - 1) == splitIdx){
					section.put(line, "");
				}else{
					section.put(line.substring(0, splitIdx), line.substring(splitIdx + 1));
				}
			}
		}
	}

	public void save(Writer ini) throws IOException{
		String EOL= System.lineSeparator();
		for(HashMap.Entry<String, String> kv : this.entrySet()){
			ini.write(kv.getKey());
			ini.write('=');
			ini.write(kv.getValue());
			ini.write(EOL);
		}

		for(IniSection section: sections.values()){
			ini.write('[');
			ini.write(section.getName());
			ini.write(']');
			ini.write(EOL);

			for(HashMap.Entry<String, String> kv : section.entrySet()){
				ini.write(kv.getKey());
				ini.write('=');
				ini.write(kv.getValue());
				ini.write(EOL);
			}
		}
	}
	@Override
	public String toString(){
		StringWriter stringOut= new StringWriter(4096);
		try {
			this.save(stringOut);
		} catch(IOException e){
			// Will never happen
		}
		return stringOut.toString();
	}
/*	public static void main(String...args) throws FileNotFoundException, IOException, ParseException{
		IniConfig test= new IniConfig();
		try(Reader iniIn= new FileReader(new File("test.ini"))){
			test.load(iniIn);
		}
		try(Writer iniOut= new FileWriter(new File("testOut.ini"))){
			test.save(iniOut);
		}
	}*/
}
