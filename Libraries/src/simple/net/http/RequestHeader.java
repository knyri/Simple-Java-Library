package simple.net.http;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * Parses an HTTP header.<br>
 * unfinished.
 * <br>Created: 2005
 * @author KP
 */
public class RequestHeader {
	HashMap<String, String> props = new HashMap<String, String>();
	HashMap<String, String> params = new HashMap<String, String>();
	String[] head;
	/**
	 *
	 */
	public RequestHeader(){}
	public String getMethod() {
		return head[0];
	}
	public String getFullResource() {
		return head[1];
	}
	public String getResource() {
		if (head[1].indexOf("?")>-1)
			return head[1].substring(0,head[1].indexOf("?"));
		else
			return head[1];
	}
	public String getVersion() {
		return head[2];
	}
	public String getProperty(final String name) {
		return props.get(name);
	}
	public Enumeration<String> getPropertyNames() {
		return Collections.enumeration(props.keySet());
	}
	public Enumeration<String> getPropertyValues() {
		return Collections.enumeration(props.values());
	}
	public String getParameter(final String name) {
		return params.get(name);
	}
	public Enumeration<String> getParameterNames() {
		return Collections.enumeration(params.keySet());
	}
	public Enumeration<String> getParameterValues() {
		return Collections.enumeration(params.values());
	}
	public boolean isValid() {
		return (head.length==3);
	}
	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		buf.append(getMethod()+" "+getFullResource()+" "+getVersion()+"\r\n");
		Enumeration<String> pn = getPropertyNames();
		String tmp;
		while (pn.hasMoreElements()) {
			tmp = pn.nextElement();
			buf.append(tmp+": "+getProperty(tmp)+"\r\n");
		}
		if (getMethod().equals("POST")) {
			pn = getParameterNames();
			buf.append("\r\n?");
			boolean and = false;
			while (pn.hasMoreElements()) {
				tmp = pn.nextElement();
				buf.append(((and?"&":"")+tmp+"="+getParameter(tmp)));
				and = true;
			}
		}
		buf.append("\r\n");
		return buf.toString();
	}
}
