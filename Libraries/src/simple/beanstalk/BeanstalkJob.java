package simple.beanstalk;

import java.io.IOException;
import java.util.Map;

/**
 * Convenience class for working with Beanstalk jobs
 */
public class BeanstalkJob {

	private final int id;
	private final byte[] data;
	private final BeanstalkClient con;
	public BeanstalkJob(int id, byte[] data, BeanstalkClient con) {
		this.id= id;
		this.con= con;
		this.data= data;
	}
	public int getId(){
		return id;
	}
	public byte[] getData(){
		return data;
	}
	/**
	 * Releases the job with the same priority and a 60 second delay
	 * @throws BeanstalkDisconnectedException
	 * @throws BeanstalkNotFoundException
	 * @throws BeanstalkProtocolException
	 * @throws BeanstalkServerException
	 * @throws IOException
	 */
	public void release() throws BeanstalkDisconnectedException, BeanstalkNotFoundException, BeanstalkProtocolException, BeanstalkServerException, IOException{
		release(getPriority(), 60);
	}
	public void release(int priority, int delay) throws BeanstalkDisconnectedException, BeanstalkNotFoundException, BeanstalkProtocolException, BeanstalkServerException, IOException{
		con.release(id, priority, delay);
	}
	public int getPriority() throws BeanstalkProtocolException, BeanstalkNotFoundException, BeanstalkDisconnectedException, BeanstalkServerException, IOException{
		return Integer.parseInt(con.statsJob(id).get("pri"), 10);
	}
	public void delete() throws BeanstalkNotFoundException, BeanstalkProtocolException, BeanstalkDisconnectedException, BeanstalkServerException, IOException{
		con.delete(id);
	}
	public void bury() throws BeanstalkNotFoundException, BeanstalkProtocolException, BeanstalkDisconnectedException, BeanstalkServerException, IOException{
		bury(getPriority());
	}
	public void bury(int priority) throws BeanstalkNotFoundException, BeanstalkDisconnectedException, BeanstalkProtocolException, BeanstalkServerException, IOException{
		con.bury(id, priority);
	}
	public Map<String, String> getStats() throws BeanstalkNotFoundException, BeanstalkProtocolException, BeanstalkDisconnectedException, BeanstalkServerException, IOException{
		return con.statsJob(id);
	}
	public void touch() throws BeanstalkNotFoundException, BeanstalkProtocolException, BeanstalkDisconnectedException, BeanstalkServerException, IOException{
		con.touch(id);
	}
}
