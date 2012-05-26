package simple.util.command;

import java.util.Vector;

/**
 * Advanced version of Command. Will eventually be able to handle optional arguments,
 * required 'one of' arguments, ect.<br>
 * Not Fully Implemented.
 * <br>Created: 2006
 * @author Kenneth Pierce
 * @version 0
 */
public class FullCommand extends Command {
	Vector<Vector<Parameter>> params = new Vector<Vector<Parameter>>();
	public FullCommand(final String CMD) {
		super(CMD);
		ParseCommand();
	}
	private void ParseCommand() {
		final String cmd = getCmd();
		int nest = 0;
		StringBuilder buf = new StringBuilder();
		final Vector<Parameter> paramSet = new Vector<Parameter>();
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
					paramSet.addElement(new Parameter(new String(buf), true));
					buf = new StringBuilder();
				break;
				case '|':
					paramSet.addElement(new Parameter(new String(buf), (nest>0)));
					buf = new StringBuilder();
				break;
				default:
					buf.append(cmd.charAt(i));
			}
		}
	}
}