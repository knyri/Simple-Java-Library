package simple.net.http.clientparams;

public class StringParam extends ClientParam{

	public StringParam(String name,String value){
		super(name,value);
	}
	@Override
	public String getValue(){
		return value;
	}

}
