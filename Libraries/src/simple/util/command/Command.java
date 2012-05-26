package simple.util.command;

import java.util.Vector;

import simple.util.do_str;

/**
 * Created to aid command driven applications.
 * <hr>
 * Depends on {@link simple.util.do_str}
 * <br>Created: 2004
 * @author Kenneth Pierce
 * @version .7
 */
public class Command {
	private Vector<String> args = null;
	private String CMD;
	/**
	 * @param cmd Root command.(E.G. cd, copy, del, deltree, etc.)
	 */
	public Command(String cmd) {
		CMD = cmd;
	}
	/**
	 * Parses the user's input.
	 * Supports spaces, commas, semicolons, single quotes, and double quotes.
	 * Recognizes \" in double quotes as an escape sequence and \' in single quotes
	 * as an escape sequence. Are treated as literals outside their respective
	 * quotes. You do not need to use \\ to get \ in a quote.
	 * @param command Raw user input.
	 */
	public void parse(String command) {
		int start = do_str.count_sameIgnoreCase(command,CMD);
		if (start==command.length()) {
			args = null;
			return;
		}
		if (start < 0) {start = 0;}
		args = new Vector<String>();
		char[] cmdC = command.substring(start).trim().toCharArray();
		StringBuilder arg = new StringBuilder();
		boolean sq = false,
				dq = false;
		for(int i = 0;i<cmdC.length;i++) {
			if (dq || sq) {
				if (dq) {
					if (cmdC[i]=='"') {
						dq = false;
						args.addElement(arg.toString());
						arg.delete(0, arg.length());
					} else if (cmdC[i]=='\\' && cmdC[i+1]=='"') {
						i++;
						arg.append(cmdC[i]);
					}
				} else if (sq) {
					if (cmdC[i]=='\'') {
						sq = false;
						args.addElement(arg.toString());
						arg.delete(0, arg.length());
					} else if (cmdC[i]=='\\' && cmdC[i+1]=='\'') {
						i++;
						arg.append(cmdC[i]);
					}
				} else {
					arg.append(cmdC[i]);
				}
			} else if (cmdC[i] == '"') {
				dq = true;
			} else if (cmdC[i] == '\'') {
				sq = true;
			} else if (cmdC[i] == ' ' || cmdC[i] == ',' || cmdC[i] == ';') {
				args.addElement(arg.toString());
				arg.delete(0, arg.length());
				while(cmdC[i+1] == ' ' || cmdC[i+1] == '\t' || cmdC[i+1] == '\n' || cmdC[i+1] == '\r') i++;
			} else {
				arg.append(cmdC[i]);
			}
		}
		args.addElement(arg.toString());
		args.trimToSize();
	}
	/**
	 * Takes the arguments and formats them according to <var>to</var>.<br>
	 * For example:<pre>
	 * Command c = new Command("cmd");
	 * c.parse("cmd arga argb 'argc d'");
	 * System.out.println(c.format("%s %S, %s"));
	 * </pre>
	 * Will output: "arga ARGB, argc d"
	 * 
	 * @param to String containing the format to use.
	 * @return Formatted version.
	 */
	public String format(String to) {
		if (args==null) {return to;}
		for (int i = 0;i<args.size();i++) {
			to = to.replaceFirst("%S",args.elementAt(i).toUpperCase());
			to = to.replaceFirst("%s",args.elementAt(i));
		}
		return to;
	}
	/**
	 * @return The number of arguments.
	 */
	public int argCount() {
		if (args==null)
			return 0;
		else
			return args.size();
	}
	/**
	 * @param i Index of argument.
	 * @return Argument at index <var>i</var>
	 */
	public String getArg(int i) {
		if (argCount()==0) {return null;}
		return args.elementAt(i);
	}
	/**
	 * @return A String array of all the arguments.
	 */
	public String[] getArgs() {
		if (argCount()==0)
			return new String[0];
		else
			return args.toArray(new String[0]);
	}
	/**
	 * @return The command set when instantiated.
	 */
	public String getCmd() {
		return CMD;
	}
}