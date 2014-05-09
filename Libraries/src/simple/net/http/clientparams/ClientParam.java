package simple.net.http.clientparams;

import org.apache.http.NameValuePair;

public abstract class ClientParam implements NameValuePair{
	protected final String name,value;
	public ClientParam(String name, String value){
		this.name=name;
		this.value=value;
	}
	@Override
	public String getName(){return name;}
	@Override
	public abstract String getValue();
	@Override
	public String toString(){
		return "[name="+name+" value="+value+"]";
	}
}
