package simple.beanstalk;

/**
 * Something wasn't found
 */
public class BeanstalkNotFoundException extends BeanstalkException {

	private static final long serialVersionUID = 1L;

	public BeanstalkNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	public BeanstalkNotFoundException(String message) {
		super(message);
	}

	public BeanstalkNotFoundException(Throwable cause) {
		super(cause);
	}
}
