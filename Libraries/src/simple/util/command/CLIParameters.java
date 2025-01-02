package simple.util.command;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import simple.io.ParseException;

/**
 * Parses passed args based on this format:
 * Automatically stops parameter and flag parsing when none of these forms are seen.
 *
 * Params:
 * -param value
 *    Sets the value
 * --param=value
 *    Sets the value
 * --param
 *    Sets the value to 1 (boolean parameter/switch)
 * --param=
 *    unsets the parameter
 *
 * Flags:
 * /f
 * 		sets one flag
 * /-f
 * 		unsets one flag
 * /fjk
 * 		sets many flags
 * /-fjk
 * 		unsets many flags
 *
 * --
 * 		Stops parameter and flag parsing
 *
 */
public class CLIParameters {
	// An unmodifiable version are set to these
	private final Map<String, String> params;
	private final Set<Character> flags;
	private final Set<String> bigFlags;
	private final boolean caseSensitive;
	private final String[] args;
	private int argsParsed= 0;
	public Set<String> getSwitches(){
		return bigFlags;
	}
	public Set<Character> getFlags(){
		return flags;
	}
	public Map<String, String> getParams(){
		return params;
	}
	public CLIParameters(String[] args, boolean caseSensitiveFlags) throws ParseException {
		this(args, caseSensitiveFlags, null, null, null);
	}
	public CLIParameters(String[] args, boolean caseSensitiveFlags, Map<String, String> defaultParams, Set<Character> defaultFlags, Set<String> defaultSwitches) throws ParseException{
		caseSensitive= caseSensitiveFlags;
		HashMap<String, String> params= new HashMap<>();
		HashSet<Character> flags= new HashSet<>();
		HashSet<String> bigFlags= new HashSet<>();
		if(defaultParams != null) {
			params.putAll(defaultParams);
		}
		if(defaultFlags != null) {
			flags.addAll(defaultFlags);
		}
		if(defaultSwitches != null) {
			bigFlags.addAll(defaultSwitches);
		}
		out:
		for(int i= 0, len= args.length; i < len; i++){
			if(args[i].length() == 0 || args[i].equals("-")){
				continue;
			}
			switch(args[i].charAt(0)){
			case '/':
				if(args[i].lastIndexOf('/') != 0) {
					argsParsed= i;
					// it's a path
					break out;
				}
				// flags
				// e.g. /fjk will set flags f, j, and k
				// /-fjk will unset flags f, j, and k
				if(caseSensitive){
					if(args[i].charAt(1) == '-'){
						for(Character c : args[i].substring(2).toCharArray()){
							flags.remove(c);
						}
					}else{
						for(Character c : args[i].substring(1).toCharArray()){
							flags.add(c);
						}
					}
				} else {
					if(args[i].charAt(1) == '-'){
						for(Character c : args[i].substring(2).toLowerCase().toCharArray()){
							flags.remove(c);
						}
					}else{
						for(Character c : args[i].substring(1).toLowerCase().toCharArray()){
							flags.add(c);
						}
					}
				}
			break;
			case '-':
				if(args[i].charAt(1) == '-'){
					// --key=value
					if(args[i].length() == 2){
						// "--" The stop processing indicator
						argsParsed= i + 1;
						break out;
					}
					String[] kv= args[i].split("=", 2);
					if(kv.length == 1){
						if(kv[0].endsWith("=")) {
							params.remove(kv[0].substring(2));
							bigFlags.remove(caseSensitive ? kv[0].substring(2) : kv[0].substring(2).toLowerCase());
						}else{
							bigFlags.add(caseSensitive ? kv[0].substring(2) : kv[0].substring(2).toLowerCase());
						}
					}else{
						params.put(kv[0].substring(2), kv[1]);
					}
				}else{
					// -key value
					try{
						params.put(args[i].substring(1), args[++i]);
					}catch(IndexOutOfBoundsException e){
						throw new ParseException("Expected a value after " + args[i - 1]);
					}
				}
			break;
			default:
				argsParsed= i;
				break out;
			}
		}
		if(argsParsed == args.length){
			this.args= new String[0];
		}else{
			this.args= new String[args.length - argsParsed];
			System.arraycopy(args, argsParsed, this.args, 0, this.args.length);
		}
		this.bigFlags= Collections.unmodifiableSet(bigFlags);
		this.flags= Collections.unmodifiableSet(flags);
		this.params= Collections.unmodifiableMap(params);
	}

	public String getArg(int i){
		return args[i];
	}
	public int getArgCount(){
		return args.length;
	}

	/**
	 * Returns
	 *
	 * @param param
	 * @return
	 */
	public boolean isSet(String param) {
		return bigFlags.contains(caseSensitive ? param : param.toLowerCase());
	}

	/**
	 * @return The number of arguments parsed
	 */
	public int argsParsed(){
		return argsParsed;
	}

	public boolean hasFlag(Character flag){
		if(caseSensitive){
			return flags.contains(flag);
		}else{
			return flags.contains(Character.toLowerCase(flag));
		}
	}

	public String getValue(String key, String def){
		// longer form used for java 7 compatibility
		if(params.get(key) == null){
			return def;
		}
		return params.get(key);
	}
	public String getValue(String key){
		return params.get(key);
	}
	/*public static void main(String[] a){
		CLIParameters p= new CLIParameters(new String[]{"/f","/l","--","arg"}, true);
		System.out.println(p.getArg(0));
	}*/
}
