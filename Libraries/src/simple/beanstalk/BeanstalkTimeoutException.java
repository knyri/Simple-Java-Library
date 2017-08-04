package simple.beanstalk;

/**
 * A time limit was exceeded
 */
public class BeanstalkTimeoutException extends BeanstalkException {

	private static final long serialVersionUID = 1L;

	public BeanstalkTimeoutException(String message, Throwable cause) {
		super(message, cause);
	}

	public BeanstalkTimeoutException(String message) {
		super(message);
	}

	public BeanstalkTimeoutException(Throwable cause) {
		super(cause);
	}

}
