package simple.net.http;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * Assists creation of an HTTP header.
 * <br>Created: 2005
 * @author KP
 */
public class ResponseHeader {
	HashMap<String,String> props = new HashMap<String,String>();
	String[] head=new String[]{"","",""};
	ResponseHeader(){}
	public ResponseHeader(final String header) {
		final String[] prop=header.split("\r\n");
		int start=0,end=0;
		start=end=prop[0].indexOf(' ');
		start++;
		head[0]=prop[0].substring(0, end);
		end=prop[0].indexOf(' ',end+1);
		head[1] = prop[0].substring(start,end);
		start = end+1;
		head[2] = prop[0].substring(end);
		int tmp = 0;
		for (int i=1;i<prop.length;i++) {
			tmp = prop[i].indexOf(":");
			if (tmp==-1) {
				break;
			}
			//System.out.println(prop[i].substring(0,tmp)+":"+prop[i].substring(tmp+2));
			props.put(prop[i].substring(0,tmp), prop[i].substring(tmp+2));
		}
	}
	public String getHttpVersion() {
		return head[0];
	}
	public String getCode() {
		return head[1];
	}
	public String getMessage() {
		return head[2];
	}
	public String getProperty(final String name) {
		return props.get(name);
	}/*
	public void setMethod(String value) {
		head[0] = value;
	}
	public void setCode(String value) {
		head[1]=value;
	}
	public void setMessage(String value) {
		head[2]=value;
	}
	public void setProperty(String name, String value) {
		props.remove(name);
		props.put(name,value);
	}
	public void unsetProperty(String name) {
		props.remove(name);
	}*/
	public Enumeration<String> getPropertyNames() {
		return Collections.enumeration(props.keySet());
	}
	public Enumeration<String> getPropertyValues() {
		return Collections.enumeration(props.values());
	}
	public boolean isValid() {
		return true;//TODO: implement this
	}
	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		final Enumeration<String> name = getPropertyNames();
		final Enumeration<String> value = getPropertyValues();
		buf.append(head[0]+" "+head[1]+" "+head[2]+"\r\n");
		while (name.hasMoreElements()) {
			buf.append((name.nextElement())+": "+(value.nextElement())+"\r\n");
		}
		buf.append("\r\n");
		if (!getCode().equals("200")) {
			buf.append(getCode()+" "+getMessage()+"<br>");
		}
		return (buf.toString());
	}
}
