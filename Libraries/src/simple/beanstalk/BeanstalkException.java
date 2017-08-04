package simple.beanstalk;

/**
 * A generic exception from this package
 */
public class BeanstalkException extends Exception{

	public BeanstalkException(String message, Throwable cause) {
		super(message, cause);
	}

	public BeanstalkException(String message) {
		super(message);
	}

	public BeanstalkException(Throwable cause) {
		super(cause);
	}

	private static final long serialVersionUID = 1L;



}
