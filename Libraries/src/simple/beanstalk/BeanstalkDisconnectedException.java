package simple.beanstalk;

/**
 * Lost connection with the server.
 */
public class BeanstalkDisconnectedException extends BeanstalkException {

	private static final long serialVersionUID = 1L;

	public BeanstalkDisconnectedException(String message, Throwable cause) {
		super(message, cause);
	}

	public BeanstalkDisconnectedException(String message) {
		super(message);
	}

	public BeanstalkDisconnectedException(Throwable cause) {
		super(cause);
	}

}
