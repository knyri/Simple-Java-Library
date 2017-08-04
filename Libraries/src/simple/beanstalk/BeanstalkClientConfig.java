package simple.beanstalk;

import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;

/**
 * Config to use for a connection
 */
public class BeanstalkClientConfig {

	private final List<String> watching= new LinkedList<String>();
	private boolean ignoreDefault= false;
	private final InetSocketAddress address;
	private String using= "default";

	/**
	 * @param host Host to connect to
	 */
	public BeanstalkClientConfig(String host){
		this(host, 11300);
	}
	public BeanstalkClientConfig(String host, int port) {
		address= new InetSocketAddress(host, port);
	}
	/**
	 * Connections will ignore the default tube
	 */
	public void ignoreDefault(){
		this.ignoreDefault= true;
	}
	/**
	 * Connections will use this tube
	 * @param tube Name of the tube to use
	 */
	public void use(String tube){
		this.using= tube;
	}
	/**
	 * Adds tubes to the watch list.
	 * @param tubes Tubes to watch
	 */
	public void watch(String... tubes){
		for(String tube: tubes){
			watching.add(tube);
		}
	}

	/**
	 * Will the connections ignore the default tube
	 * @return
	 */
	public boolean ignoresDefault(){
		return ignoreDefault;
	}
	/**
	 * What tube will the connections use
	 * @return
	 */
	public String using(){
		return using;
	}
	/**
	 * What tubes the connections will watch
	 * @return
	 */
	public List<String> watching(){
		return watching;
	}
	/**
	 * What server the connections will connect to
	 * @return
	 */
	public InetSocketAddress getAddress(){
		return address;
	}

}
