package simple.net.http;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Parses an HTTP header.<br>
 * unfinished.
 * <br>Created: 2005
 * @author KP
 */
public class RequestHeader {
	Hashtable<String, String> props = new Hashtable<String, String>();
	Hashtable<String, String> params = new Hashtable<String, String>();
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
		return props.keys();
	}
	public Enumeration<String> getPropertyValues() {
		return props.elements();
	}
	public String getParameter(final String name) {
		return params.get(name);
	}
	public Enumeration<String> getParameterNames() {
		return params.keys();
	}
	public Enumeration<String> getParameterValues() {
		return params.elements();
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
