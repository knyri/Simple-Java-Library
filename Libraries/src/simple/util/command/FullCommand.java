package simple.util.command;

import java.util.ArrayList;
import java.util.List;

/**
 * Advanced version of Command. Will eventually be able to handle optional arguments,
 * required 'one of' arguments, ect.<br>
 * Not Fully Implemented.
 * <br>Created: 2006
 * @author Kenneth Pierce
 * @version 0
 */
public class FullCommand extends Command {
	List<List<Parameter>> params = new ArrayList<List<Parameter>>();
	public FullCommand(final String CMD) {
		super(CMD);
		ParseCommand();
	}
	private void ParseCommand() {
		final String cmd = getCmd();
		int nest = 0;
		StringBuilder buf = new StringBuilder();
		final List<Parameter> paramSet = new ArrayList<Parameter>();
/*
[ ] Optional
 |  One of
*/
		for (int i = 0;i<cmd.length();i++) {
			switch(cmd.charAt(i)) {
				case '[':
					nest++;
				break;
				case ']':
					nest--;
					paramSet.add(new Parameter(buf.toString(), true));
					buf.setLength(0);
				break;
				case '|':
					paramSet.add(new Parameter(buf.toString(), (nest>0)));
					buf.setLength(0);
				break;
				default:
					buf.append(cmd.charAt(i));
			}
		}
	}
}