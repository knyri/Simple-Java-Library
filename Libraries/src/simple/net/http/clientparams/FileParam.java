package simple.net.http.clientparams;

public class FileParam extends ClientParam{
	private final String content_type;
	/**
	 * Use '/' as the path separator.
	 * @param name
	 * @param value
	 * @param content_type
	 */
	public FileParam(String name,String value,String content_type){
		super(name,value);
		this.content_type=content_type;
	}

	public String getContentType(){
		return content_type;
	}
	@Override
	public String getValue(){
		return value;
	}

}
