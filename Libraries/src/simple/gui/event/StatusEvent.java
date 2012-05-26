package simple.gui.event;

import java.util.Date;

/** Event for updating the status message.
 * <br>Created: 2007
 * @author Kenneth Pierce
 */
public class StatusEvent {
	private final Object t;
	private final int os;
	private final int ns;
	private final String ms;
	private final String ds;
	private final long time;
	private final int type;
	private boolean consumed = false;
	public StatusEvent(Object target, int type, int oldStatus, int newStatus, String message, String display, long date) {
		t = target;
		os = oldStatus;
		ns = newStatus;
		ms = message;
		ds = display;
		time = date;
		this.type = type;
	}
	public StatusEvent(Object target, int type, int oldStatus, int newStatus, String message, String display) {
		this(target, type, oldStatus, newStatus, message, display, System.currentTimeMillis());
	}
	public Object getTarget() {
		return t;
	}
	public int getOldStatus() {
		return os;
	}
	public int getNewStatus() {
		return ns;
	}
	public String getMessage() {
		return ms;
	}
	public String getDisplay() {
		return ds;
	}
	public long getTime() {
		return time;
	}
	public int getType() {
		return type;
	}
	public void consume() {
		consumed = true;
	}
	public boolean isConsumed() {
		return consumed;
	}
	public String toString() {
		StringBuffer buf = new StringBuffer(150);
		buf.append("Target:      "+t+"\n");
		buf.append("Old Status:  "+os+"\n");
		buf.append("New Status:  "+ns+"\n");
		buf.append("Type:        "+type+"\n");
		buf.append("Message:     "+ms+"\n");
		buf.append("Display Str: "+ds+"\n");
		buf.append("Date:        "+new Date(time)+"\n");
		return buf.toString();
	}
}
