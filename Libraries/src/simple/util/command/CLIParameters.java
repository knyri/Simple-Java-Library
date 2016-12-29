package simple.util.command;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Parses passed args based on this format:
 * Automatically stops parameter and flag parsing when none of these forms are seen.
 * 
 * Params:
 * -param value
 * --param=value
 * --param
 * 		unsets the parameter
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
	private final HashMap<String, String> params= new HashMap<>();
	private final HashSet<Character> flags= new HashSet<>();
	private final boolean caseSensitive;
	private int argsParsed;
	public CLIParameters(String[] args, boolean caseSensitiveFlags){
		caseSensitive= caseSensitiveFlags;
		argsParsed= args.length;
		out:
		for(int i= 0, len= args.length; i < len; i++){
			switch(args[i].charAt(0)){
			case '/':
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
						argsParsed= i;
						break out;
					}
					String[] kv= args[i].split("=", 2);
					if(kv.length == 1){
						params.remove(kv[0]);
					}else{
						params.put(kv[0], kv[1]);
					}
				}else{
					// -key value
					params.put(args[i], args[++i]);
				}
			break;
			default:
				argsParsed= i;
				break out;
			}
		}
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
	
	public String getValue(String key){
		return params.get(key);
	}
}
