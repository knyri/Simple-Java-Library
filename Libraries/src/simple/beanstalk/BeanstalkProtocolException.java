package simple.beanstalk;

/**
 * Something didn't match the protocol spec.
 */
public class BeanstalkProtocolException extends BeanstalkException {

	private static final long serialVersionUID = 1L;

	public BeanstalkProtocolException(String message, Throwable cause) {
		super(message, cause);
	}

	public BeanstalkProtocolException(String message) {
		super(message);
	}

	public BeanstalkProtocolException(Throwable cause) {
		super(cause);
	}

}
