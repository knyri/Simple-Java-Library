package simple.net.http.clientparams;

public abstract class ClientParam{
	protected final String name,value;
	public ClientParam(String name, String value){
		this.name=name;
		this.value=value;
	}
	public String getName(){return name;}
	public abstract String getValue();
}
