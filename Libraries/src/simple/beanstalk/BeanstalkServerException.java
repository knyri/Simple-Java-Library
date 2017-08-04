package simple.beanstalk;

/**
 * The server threw an error
 */
public class BeanstalkServerException extends BeanstalkException {

	private static final long serialVersionUID = 1L;

	public BeanstalkServerException(String message, Throwable cause) {
		super(message, cause);
	}

	public BeanstalkServerException(String message) {
		super(message);
	}

	public BeanstalkServerException(Throwable cause) {
		super(cause);
	}

}
