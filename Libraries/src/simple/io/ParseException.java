package simple.io;

/**For use with ParsePosition
 * <br>Created: 2008
 * @author Kenneth Pierce
 */
public class ParseException extends Exception {
	private static final long serialVersionUID = 2074747710527437564L;
	private final ParsePosition PP;
	public ParseException(String reason) {
		super(reason);
		PP = null;
	}
	public ParseException(ParsePosition pp, String reason) {
		super(reason);
		PP = pp;
	}
	public String getMessage() {
		if (PP==null)
			return super.getMessage();
		else
			return "["+PP.toString()+"]"+super.getMessage();
	}
	public String getLocalizedMessage() {
		if (PP==null)
			return super.getLocalizedMessage();
		else
			return "["+PP.toString()+"]"+super.getLocalizedMessage();
	}
}
