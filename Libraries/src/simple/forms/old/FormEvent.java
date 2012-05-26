package simple.forms.old;

/**
 * Object that is passed to a FormListener.
 * <br>Created: 2004
 * @author Kenneth Pierce
 * @deprecated
 */
public class FormEvent {
	public static int RESET = 0;
	public static int SUBMIT = 1;
	public static int ERROR = -1;
	private Form source;
	private int command;
	private String problem;
	public FormEvent(Form source, int command, String problem) {
		this.source = source;
		this.command = command;
		this.problem = problem;
	}
	public Form getSource() {
		return source;
	}
	public int getCommand() {
		return command;
	}
	public String getProblem() {
		return problem;
	}
}