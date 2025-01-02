package simple.formats.xlsx;

public class XlsxException extends Exception{

	private static final long serialVersionUID= 1L;

	public XlsxException(String message){
		super(message);
	}

	public XlsxException(Throwable cause){
		super(cause);
	}

	public XlsxException(String message, Throwable cause){
		super(message, cause);
	}

	public XlsxException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace){
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
