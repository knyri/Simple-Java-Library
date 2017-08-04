package simple.beanstalk;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import simple.io.ByteBuffer;

/**
 * Implements all in the following doc as of April 3, 2016
 * https://github.com/kr/beanstalkd/blob/master/doc/protocol.txt
 */
public class BeanstalkClient implements AutoCloseable{
	private final InetSocketAddress address;
	private Socket con= null;
	private InputStream input;
	private OutputStream output;
	private final ByteBuffer readBuf= new ByteBuffer(1024);
	private final StringBuilder response= new StringBuilder();

	/**
	 * Connects to and sets up the client according to the config object
	 * @param config
	 * @throws IOException
	 * @throws BeanstalkProtocolException
	 * @throws BeanstalkNotFoundException
	 * @throws BeanstalkDisconnectedException
	 * @throws BeanstalkServerException
	 */
	public BeanstalkClient(BeanstalkClientConfig config) throws IOException, BeanstalkProtocolException, BeanstalkNotFoundException, BeanstalkDisconnectedException, BeanstalkServerException{
		address= config.getAddress();
		this.connect();

		for(String tube: config.watching()){
			this.watch(tube);
		}
		if(!config.ignoresDefault()){
			this.ignore("deault");
		}
		this.use(config.using());
	}
	@Override
	public void finalize(){
		if(con != null){
			try {
				this.close();
			} catch (IOException e) {}
		}
	}
	@Override
	public void close() throws IOException{
		output.write("quit\r\n".getBytes());
		con.close();
	}

	/**
	 * Shortcut for ("127.0.0.1", 11300)
	 */
	public BeanstalkClient(){
		this("127.0.0.1", 11300);
	}

	/**
	 * Uses the default port of 11300
	 * @param host Hostname or IP address of the server to connect to
	 */
	public BeanstalkClient(String host){
		this(host, 11300);
	}
	/**
	 * @param host Hostname or IP address of the server to connect to
	 * @param port Port number to connect on
	 */
	public BeanstalkClient(String host, int port) {
		address= new InetSocketAddress(host, port);
	}

	/**
	 * Connects to the beanstalk server
	 * @throws IOException
	 */
	public void connect() throws IOException {
		if(con == null || con.isClosed()){
			con= new Socket(address.getAddress(), address.getPort());
			output= con.getOutputStream();
			input= new BufferedInputStream(con.getInputStream());
		}
	}

	/**
	 * Runs a command
	 * @param cmd The full command
	 * @return The first token of the response
	 * @throws IOException
	 * @throws BeanstalkDisconnectedException
	 * @throws BeanstalkServerException
	 * @throws BeanstalkProtocolException
	 */
	private final String doCommand(byte[] cmd) throws IOException, BeanstalkDisconnectedException, BeanstalkServerException, BeanstalkProtocolException{
		output.write(cmd);
		fillBuffer();
		String response= getToken();

		switch(response){
		case "OUT_OF_MEMORY":
			throw new BeanstalkServerException("Server ran out of memory while processing this request.");
		case "INTERNAL_ERROR":
			throw new BeanstalkServerException("Server encountered an internal error while processing this request.");
		case "BAD_FORMAT":
			throw new BeanstalkProtocolException("Command is formatted incorrectly.");
		case "UNKNOWN_COMMAND":
			throw new BeanstalkProtocolException("The server didn't recognize the command.");
		}

		return response;
	}
	/**
	 * Runs the command and returns the response
	 * @param cmd Command to run without the trailing CR LF
	 * @return The first token of the response
	 * @throws IOException
	 * @throws BeanstalkDisconnectedException
	 * @throws BeanstalkServerException
	 * @throws BeanstalkProtocolException
	 */
	private String doCommand(String cmd) throws IOException, BeanstalkDisconnectedException, BeanstalkServerException, BeanstalkProtocolException{
		return doCommand((cmd+"\r\n").getBytes());
	}
	/**
	 * Runs a command that passes data
	 * @param cmd Command to run
	 * @param data Data to supply
	 * @return The first token of the response
	 * @throws BeanstalkDisconnectedException
	 * @throws IOException
	 * @throws BeanstalkServerException
	 * @throws BeanstalkProtocolException
	 */
	private String doCommand(String cmd, byte[] data) throws BeanstalkDisconnectedException, IOException, BeanstalkServerException, BeanstalkProtocolException{
		byte[] cmdBytes= (' ' + Integer.toString(data.length, 10) + "\r\n").getBytes();
		byte[] fullCmd= new byte[cmdBytes.length + data.length + 2];
		System.arraycopy(cmdBytes, 0, fullCmd, 0, cmdBytes.length);
		System.arraycopy(data, 0, fullCmd, cmdBytes.length, data.length);
		fullCmd[fullCmd.length - 1]= '\n';
		fullCmd[fullCmd.length - 2]= '\r';
		return doCommand(fullCmd);
	}
	/**
	 * Attempts to fill the byte buffer.
	 * Blocks until at least one byte is read.
	 * @throws IOException
	 * @throws BeanstalkDisconnectedException
	 */
	private void fillBuffer() throws IOException, BeanstalkDisconnectedException{
		if(!readBuf.isEmpty()){
			return;
		}
		while(input.available() == 0){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {}
		}
		int ibyte;
		while(input.available() > 0){
			ibyte= input.read();
			if(ibyte == -1){
				throw new BeanstalkDisconnectedException("Encountered unexpected EOF");
			}
			readBuf.put((byte)ibyte);
		}

	}
	/**
	 * Reads until a space or CR LF
	 * @return
	 * @throws IOException
	 * @throws BeanstalkDisconnectedException
	 */
	protected final String getToken() throws IOException, BeanstalkDisconnectedException {
		response.setLength(0);
		byte curByte;
		while(true){
			fillBuffer();

			while(!readBuf.isEmpty()){
				curByte= readBuf.get();
				switch(curByte){
				case '\n':
					if(response.charAt(response.length() - 1) == '\r'){
						response.setLength(response.length() - 1);
						return response.toString();
					}
				break;
				case ' ':
					return response.toString();
				}
				response.append((char)curByte);
			}
		}
	}
	protected final byte[] readData() throws BeanstalkDisconnectedException, IOException{
		int length= Integer.parseInt(getToken(), 10);
		int read= 0;
		byte[] data= new byte[length];

		while(read != length){
			fillBuffer();
			while(!readBuf.isEmpty() && read != length){
				data[read++]= readBuf.get();
			}
		}

		// discard the remaining \r\n
		fillBuffer();
		readBuf.get();
		readBuf.get();

		return data;
	}
	/**
	 * Attempts to read a YAML list
	 * @return
	 * @throws BeanstalkDisconnectedException
	 * @throws IOException
	 */
	protected List<String> readList() throws BeanstalkDisconnectedException, IOException{
		List<String> list= new ArrayList<String>();
		String line;
		try(LineNumberReader lines= new LineNumberReader(new InputStreamReader(new ByteArrayInputStream(readData())))){
			line= lines.readLine();
			if(line != null && line.length() > 0){
				list.add(line.substring(2));
			}
		}
		return list;
	}
	protected static final Pattern colonSplit= Pattern.compile(":");
	/**
	 * Attempts to read a YAML dictionary
	 * @return
	 * @throws BeanstalkDisconnectedException
	 * @throws IOException
	 */
	protected Map<String, String> readMap() throws BeanstalkDisconnectedException, IOException{
		Map<String, String> map= new HashMap<String, String>();
		String line;
		String[] keyValue;
		try(LineNumberReader lines= new LineNumberReader(new InputStreamReader(new ByteArrayInputStream(readData())))){
			line= lines.readLine();
			if(line != null && line.length() > 0){
				keyValue= colonSplit.split(line);
				map.put(keyValue[0].trim(), keyValue[1].trim());
			}
		}
		return map;
	}
	/**
	 * Attempts to read a beanstalk job
	 * @return a beanstalk job
	 * @throws BeanstalkDisconnectedException
	 * @throws IOException
	 */
	protected final BeanstalkJob readJob() throws BeanstalkDisconnectedException, IOException{
		int id= Integer.parseInt(getToken(), 10);
		return new BeanstalkJob(id, readData(), this);
	}
	/**
	 * Blocks until a job is ready
	 * @return A job from the beanstalk queue
	 * @throws IOException
	 * @throws BeanstalkException
	 * @throws BeanstalkProtocolException
	 * @throws BeanstalkDisconnectedException
	 * @throws BeanstalkServerException
	 */
	public final BeanstalkJob reserve() throws IOException, BeanstalkException{
		return reserve(-1);
	}
	/**
	 * Blocks a set amount of time for a job
	 * @param timeout Time to wait in seconds. -1 waits indefinitely.
	 * @return A beanstalk job
	 * @throws IOException
	 * @throws BeanstalkException
	 * @throws BeanstalkTimeoutException If the timeout is reached
	 * @throws BeanstalkProtocolException
	 * @throws BeanstalkDisconnectedException
	 * @throws BeanstalkServerException
	 */
	public BeanstalkJob reserve(int timeout) throws IOException, BeanstalkException{
		String response;
		if(timeout == -1){
			response= doCommand("reserve");
		}else{
			response= doCommand("reserve-with-timeout "+Integer.toString(timeout, 10));
		}
		switch(response){
		case "TIMED_OUT":
			throw new BeanstalkTimeoutException("Time out wating for a job");
		case "DEADLINE_SOON":
			throw new BeanstalkException("DEADLINE_SOON");
		case "RESERVED":
			break;
		default:
			throw new BeanstalkProtocolException("Unknown response: '" + response + "'");
		}

		return readJob();
	}

	/**
	 * Removes a job from the server
	 * @param jobId
	 * @throws IOException
	 * @throws BeanstalkNotFoundException If the job wasn't found
	 * @throws BeanstalkProtocolException
	 * @throws BeanstalkDisconnectedException
	 * @throws BeanstalkServerException
	 */
	public void delete(int jobId) throws IOException, BeanstalkNotFoundException, BeanstalkProtocolException, BeanstalkDisconnectedException, BeanstalkServerException{
		String response= doCommand("delete "+ Integer.toString(jobId, 10));
		if("NOT_FOUND".equals(response)){
			throw new BeanstalkNotFoundException("Job " + jobId + " not found");
		}
		if(!"DELETED".equals(response)){
			throw new BeanstalkProtocolException("Unexpected response: '" + response + "'");
		}
	}
	/**
	 * Puts a reserved job back in the ready queue
	 * @param jobId Job to put back
	 * @param priority Priority of the job
	 * @param delay Seconds to wait before letting a client reserve it
	 * @return A put response indicating if the job was put in the delayed/ready queue or was buried due to memory constraints
	 * @throws BeanstalkDisconnectedException
	 * @throws IOException
	 * @throws BeanstalkNotFoundException
	 * @throws BeanstalkProtocolException
	 * @throws BeanstalkServerException
	 */
	public PutResponse release(int jobId, int priority, int delay) throws BeanstalkDisconnectedException, IOException, BeanstalkNotFoundException, BeanstalkProtocolException, BeanstalkServerException{
		String response= doCommand("release " + Integer.toString(jobId, 10) + ' ' + Integer.toString(priority, 10) + ' ' + Integer.toString(delay, 10));
		PutResponse resp;
		switch(response){
		case "RELEASED":
			resp= new PutResponse(PutResponse.Type.INSERTED, jobId);
			break;
		case "NOT_FOUND":
			throw new BeanstalkNotFoundException("Job " + jobId + " not found");
		case "BURIED":
			resp= new PutResponse(PutResponse.Type.BURIED, jobId);
		default:
			throw new BeanstalkProtocolException("Unexpected response: '" + response + "'");
		}
		return resp;
	}
	/**
	 * Put the job in the buried queue
	 * @param jobId
	 * @param priority
	 * @throws BeanstalkDisconnectedException
	 * @throws IOException
	 * @throws BeanstalkNotFoundException
	 * @throws BeanstalkProtocolException
	 * @throws BeanstalkServerException
	 */
	public void bury(int jobId, int priority) throws BeanstalkDisconnectedException, IOException, BeanstalkNotFoundException, BeanstalkProtocolException, BeanstalkServerException{
		String response= doCommand("bury "+ Integer.toString(jobId, 10) + ' ' + Integer.toString(priority, 10));
		if("NOT_FOUND".equals(response)){
			throw new BeanstalkNotFoundException("Job " + jobId + " not found");
		}
		if(!"BURIED".equals(response)){
			throw new BeanstalkProtocolException("Unexpected response: '" + response + "'");
		}
	}
	/**
	 * Touches the job. Extending the time to run.
	 * @param jobId
	 * @throws IOException
	 * @throws BeanstalkNotFoundException
	 * @throws BeanstalkProtocolException
	 * @throws BeanstalkDisconnectedException
	 * @throws BeanstalkServerException
	 */
	public void touch(int jobId) throws IOException, BeanstalkNotFoundException, BeanstalkProtocolException, BeanstalkDisconnectedException, BeanstalkServerException{
		String response= doCommand("touch "+ Integer.toString(jobId, 10));
		if("NOT_FOUND".equals(response)){
			throw new BeanstalkNotFoundException("Job " + jobId + " not found");
		}
		if(!"TOUCHED".equals(response)){
			throw new BeanstalkProtocolException("Unexpected response: '" + response + "'");
		}
	}
	/**
	 * Adds the tube to the watch list.
	 * @param tube
	 * @throws BeanstalkDisconnectedException
	 * @throws IOException
	 * @throws BeanstalkProtocolException
	 * @throws BeanstalkServerException
	 */
	public void watch(String tube) throws BeanstalkDisconnectedException, IOException, BeanstalkProtocolException, BeanstalkServerException{
		String response= doCommand("watch "+ tube);
		if(!"WATCHING".equals(response)){
			throw new BeanstalkProtocolException("Unexpected response: '" + response + "'");
		}
		// discard count
		getToken();
	}
	/**
	 * Removes the tube from the watch list
	 * @param tube
	 * @return false if the tube is the only one it is currently watching
	 * @throws BeanstalkDisconnectedException
	 * @throws IOException
	 * @throws BeanstalkProtocolException
	 * @throws BeanstalkServerException
	 */
	public boolean ignore(String tube) throws BeanstalkDisconnectedException, IOException, BeanstalkProtocolException, BeanstalkServerException{
		String response= doCommand("ignore "+ tube);
		switch(response){
		case "WATCHING":
			// discard count
			getToken();
			return true;
		case "NOT_IGNORED":
			return false;
		default:
			throw new BeanstalkProtocolException("Unexpected response: '" + response + "'");
		}
	}
	/**
	 * Gets the job without removing it from the queue
	 * @param jobId
	 * @return
	 * @throws BeanstalkDisconnectedException
	 * @throws IOException
	 * @throws BeanstalkNotFoundException
	 * @throws BeanstalkProtocolException
	 * @throws BeanstalkServerException
	 */
	public BeanstalkJob peek(int jobId) throws BeanstalkDisconnectedException, IOException, BeanstalkNotFoundException, BeanstalkProtocolException, BeanstalkServerException{
		String response= doCommand("peek "+ Integer.toString(jobId, 10));
		if("NOT_FOUND".equals(response)){
			throw new BeanstalkNotFoundException("Job "+ jobId +" not found");
		}
		if(!"FOUND".equals(response)){
			throw new BeanstalkProtocolException("Unexpected response: '" + response + "'");
		}
		return readJob();
	}
	/**
	 * Get's the first ready job without removing it
	 * @return A beanstalk job
	 * @throws BeanstalkDisconnectedException
	 * @throws IOException
	 * @throws BeanstalkNotFoundException if no jobs are in the ready queue
	 * @throws BeanstalkProtocolException
	 * @throws BeanstalkServerException
	 */
	public BeanstalkJob peekReady() throws BeanstalkDisconnectedException, IOException, BeanstalkNotFoundException, BeanstalkProtocolException, BeanstalkServerException{
		String response= doCommand("peek-ready");
		if("NOT_FOUND".equals(response)){
			throw new BeanstalkNotFoundException("No jobs in the ready queue");
		}
		if(!"FOUND".equals(response)){
			throw new BeanstalkProtocolException("Unexpected response: '" + response + "'");
		}
		return readJob();
	}
	/**
	 * Get's the first delayed job without removing it
	 * @return A beanstalk job
	 * @throws BeanstalkDisconnectedException
	 * @throws IOException
	 * @throws BeanstalkNotFoundException If no jobs are in the delayed queue
	 * @throws BeanstalkProtocolException
	 * @throws BeanstalkServerException
	 */
	public BeanstalkJob peekDelayed() throws BeanstalkDisconnectedException, IOException, BeanstalkNotFoundException, BeanstalkProtocolException, BeanstalkServerException{
		String response= doCommand("peek-delayed");
		if("NOT_FOUND".equals(response)){
			throw new BeanstalkNotFoundException("No jobs in the delayed queue");
		}
		if(!"FOUND".equals(response)){
			throw new BeanstalkProtocolException("Unexpected response: '" + response + "'");
		}
		return readJob();
	}
	/**
	 * Get's the first buried job without removing it
	 * @return A beanstalk job
	 * @throws BeanstalkDisconnectedException
	 * @throws IOException
	 * @throws BeanstalkNotFoundException If no jobs are in the buried queue
	 * @throws BeanstalkProtocolException
	 * @throws BeanstalkServerException
	 */
	public BeanstalkJob peekBuried() throws BeanstalkDisconnectedException, IOException, BeanstalkNotFoundException, BeanstalkProtocolException, BeanstalkServerException{
		String response= doCommand("peek-buried");
		if("NOT_FOUND".equals(response)){
			throw new BeanstalkNotFoundException("No jobs in the buried queue");
		}
		if(!"FOUND".equals(response)){
			throw new BeanstalkProtocolException("Unexpected response: '" + response + "'");
		}
		return readJob();
	}

	/**
	 * Moves up to <var>limit</var> from the buried queue in to the ready queue.
	 * If there are no jobs in the buried queue it will move jobs from the delayed queue.
	 * @param limit
	 * @return The number of jobs moved
	 * @throws BeanstalkDisconnectedException
	 * @throws IOException
	 * @throws BeanstalkProtocolException
	 * @throws BeanstalkServerException
	 */
	public int kick(int limit) throws BeanstalkDisconnectedException, IOException, BeanstalkProtocolException, BeanstalkServerException{
		String response= doCommand("kick "+ Integer.toString(limit, 10));
		if(!"KICKED".equals(response)){
			throw new BeanstalkProtocolException("Unexpected response: '" + response + "'");
		}
		return Integer.parseInt(getToken(), 10);
	}
	/**
	 * Moves a job from the buried or delayed queue to the ready queue
	 * @param jobId
	 * @throws BeanstalkDisconnectedException
	 * @throws IOException
	 * @throws BeanstalkNotFoundException
	 * @throws BeanstalkProtocolException
	 * @throws BeanstalkServerException
	 */
	public void kickJob(int jobId) throws BeanstalkDisconnectedException, IOException, BeanstalkNotFoundException, BeanstalkProtocolException, BeanstalkServerException{
		String response= doCommand("kick-job "+ Integer.toString(jobId, 10));
		if("NOT_FOUND".equals(response)){
			throw new BeanstalkNotFoundException("Job "+ jobId +" not found");
		}
		if(!"KICKED".equals(response)){
			throw new BeanstalkProtocolException("Unexpected response: '" + response + "'");
		}
	}

	/**
	 * Switches the active tube to <var>tube</var>
	 * @param tube Tube that future command should act on
	 * @throws BeanstalkDisconnectedException
	 * @throws IOException
	 * @throws BeanstalkProtocolException
	 * @throws BeanstalkServerException
	 */
	public void use(String tube) throws BeanstalkDisconnectedException, IOException, BeanstalkProtocolException, BeanstalkServerException{
		String response= doCommand("use "+ tube);
		if(!"USING".equals(response)){
			throw new BeanstalkProtocolException("Unexpected response: '" + response + "'");
		}
		// discard tube name
		getToken();
	}
	/**
	 * Gets the tube that this client is using
	 * @return
	 * @throws BeanstalkDisconnectedException
	 * @throws IOException
	 * @throws BeanstalkProtocolException
	 * @throws BeanstalkServerException
	 */
	public String listTubeUsed() throws BeanstalkDisconnectedException, IOException, BeanstalkProtocolException, BeanstalkServerException{
		String response= doCommand("list-tube-used");
		if(!"USING".equals(response)){
			throw new BeanstalkProtocolException("Unexpected response: '" + response + "'");
		}

		return getToken();
	}
	/**
	 * Puts the job in to the used tube
	 * @param priority
	 * @param delay
	 * @param ttr
	 * @param data
	 * @return
	 * @throws BeanstalkDisconnectedException
	 * @throws IOException
	 * @throws BeanstalkProtocolException
	 * @throws BeanstalkServerException
	 */
	public PutResponse put(int priority, int delay, int ttr, byte[] data) throws BeanstalkDisconnectedException, IOException, BeanstalkProtocolException, BeanstalkServerException{
		String response= doCommand("put "+ Integer.toString(priority, 10) + ' ' + Integer.toString(delay, 10) + ' ' + Integer.toString(ttr, 10), data);
		switch(response){
		case "INSERTED":
			return new PutResponse(PutResponse.Type.INSERTED, Integer.parseInt(getToken(), 10));
		case "BURIED":
			return new PutResponse(PutResponse.Type.BURIED, Integer.parseInt(getToken(), 10));
		case "EXPECTED_CRLF":
			throw new BeanstalkProtocolException("Missing trailing CRLF after data.");
		case "JOB_TOO_BIG":
			throw new BeanstalkProtocolException("Job data too large.");
		case "DRAINING":
			throw new BeanstalkProtocolException("Server is in drain mode and is not accepting new jobs.");
		default:
			throw new BeanstalkProtocolException("Unexpected response: '" + response + "'");
		}
	}

	/**
	 * Gets the job stats for a job.
	 * From the docs:
	 * The stats-job data is a YAML file representing a single dictionary of strings to scalars. It contains these keys:
	 * - "id" is the job id
	 * - "tube" is the name of the tube that contains this job
	 * - "state" is "ready" or "delayed" or "reserved" or "buried"
	 * - "pri" is the priority value set by the put, release, or bury commands.
	 * - "age" is the time in seconds since the put command that created this job.
	 * - "delay" is the integer number of seconds to wait before putting this job
	 *      in the ready queue.
	 * - "ttr" -- time to run -- is the integer number of seconds a worker is allowed
	 *      to run this job.
	 * - "time-left" is the number of seconds left until the server puts this
	 *      job into the ready queue. This number is only meaningful if the job is
	 *      reserved or delayed. If the job is reserved and this amount of time
	 *      elapses before its state changes, it is considered to have timed out.
	 * - "file" is the number of the earliest binlog file containing this job.
	 *      If -b wasn't used, this will be 0.
	 * - "reserves" is the number of times this job has been reserved.
	 * - "timeouts" is the number of times this job has timed out during a reservation.
	 * - "releases" is the number of times a client has released this job from a reservation.
	 * - "buries" is the number of times this job has been buried.
	 * - "kicks" is the number of times this job has been kicked.
	 * @param jobId
	 * @return
	 * @throws BeanstalkProtocolException
	 * @throws BeanstalkNotFoundException
	 * @throws BeanstalkDisconnectedException
	 * @throws BeanstalkServerException
	 * @throws IOException
	 */
	public Map<String, String> statsJob(int jobId) throws BeanstalkProtocolException, BeanstalkNotFoundException, BeanstalkDisconnectedException, BeanstalkServerException, IOException{
		String response= doCommand("stats-job " + Integer.toString(jobId, 10));
		if("NOT_FOUND".equals(response)){
			throw new BeanstalkNotFoundException("Job "+ jobId +" not found");
		}
		if(!"OK".equals(response)){
			throw new BeanstalkProtocolException("Unexpected response: '" + response + "'");
		}
		return readMap();
	}


	/**
	 * From the docs:
	 * The stats-tube data is a YAML file representing a single dictionary of strings to scalars. It contains these keys:
	 * - "name" is the tube's name.
	 * - "current-jobs-urgent" is the number of ready jobs with priority < 1024 in this tube.
	 * - "current-jobs-ready" is the number of jobs in the ready queue in this tube.
	 * - "current-jobs-reserved" is the number of jobs reserved by all clients in this tube.
	 * - "current-jobs-delayed" is the number of delayed jobs in this tube.
	 * - "current-jobs-buried" is the number of buried jobs in this tube.
	 * - "total-jobs" is the cumulative count of jobs created in this tube in the current beanstalkd process.
	 * - "current-using" is the number of open connections that are currently using this tube.
	 * - "current-waiting" is the number of open connections that have issued a reserve command while watching this tube but not yet received a response.
	 * - "current-watching" is the number of open connections that are currently watching this tube.
	 * - "pause" is the number of seconds the tube has been paused for.
	 * - "cmd-delete" is the cumulative number of delete commands for this tube
	 * - "cmd-pause-tube" is the cumulative number of pause-tube commands for this tube.
	 * - "pause-time-left" is the number of seconds until the tube is un-paused.
	 * @param tube
	 * @return
	 * @throws BeanstalkProtocolException
	 * @throws BeanstalkNotFoundException
	 * @throws BeanstalkDisconnectedException
	 * @throws BeanstalkServerException
	 * @throws IOException
	 */
	public Map<String, String> statsTube(String tube) throws BeanstalkProtocolException, BeanstalkNotFoundException, BeanstalkDisconnectedException, BeanstalkServerException, IOException{
		String response= doCommand("stats-tube " + tube);
		if("NOT_FOUND".equals(response)){
			throw new BeanstalkNotFoundException("Tube " + tube + " not found");
		}
		if(!"OK".equals(response)){
			throw new BeanstalkProtocolException("Unexpected response: '" + response + "'");
		}
		return readMap();
	}

	/**
	 * The stats data for the system is a YAML file representing a single dictionary
	 * of strings to scalars. Entries described as "cumulative" are reset when the
	 * beanstalkd process starts; they are not stored on disk with the	* -b flag.
	 * - "current-jobs-urgent" is the number of ready jobs with priority < 1024.
	 * - "current-jobs-ready" is the number of jobs in the ready queue.
	 * - "current-jobs-reserved" is the number of jobs reserved by all clients.
	 * - "current-jobs-delayed" is the number of delayed jobs.
	 * - "current-jobs-buried" is the number of buried jobs.
	 * - "cmd-put" is the cumulative number of put commands.
	 * - "cmd-peek" is the cumulative number of peek commands.
	 * - "cmd-peek-ready" is the cumulative number of peek-ready commands.
	 * - "cmd-peek-delayed" is the cumulative number of peek-delayed commands.
	 * - "cmd-peek-buried" is the cumulative number of peek-buried commands.
	 * - "cmd-reserve" is the cumulative number of reserve commands.
	 * - "cmd-use" is the cumulative number of use commands.
	 * - "cmd-watch" is the cumulative number of watch commands.
	 * - "cmd-ignore" is the cumulative number of ignore commands.
	 * - "cmd-delete" is the cumulative number of delete commands.
	 * - "cmd-release" is the cumulative number of release commands.
	 * - "cmd-bury" is the cumulative number of bury commands.
	 * - "cmd-kick" is the cumulative number of kick commands.
	 * - "cmd-stats" is the cumulative number of stats commands.
	 * - "cmd-stats-job" is the cumulative number of stats-job commands.
	 * - "cmd-stats-tube" is the cumulative number of stats-tube commands.
	 * - "cmd-list-tubes" is the cumulative number of list-tubes commands.
	 * - "cmd-list-tube-used" is the cumulative number of list-tube-used commands.
	 * - "cmd-list-tubes-watched" is the cumulative number of list-tubes-watched commands.
	 * - "cmd-pause-tube" is the cumulative number of pause-tube commands.
	 * - "job-timeouts" is the cumulative count of times a job has timed out.
	 * - "total-jobs" is the cumulative count of jobs created.
	 * - "max-job-size" is the maximum number of bytes in a job.
	 * - "current-tubes" is the number of currently-existing tubes.
	 * - "current-connections" is the number of currently open connections.
	 * - "current-producers" is the number of open connections that have each issued at least one put command.
	 * - "current-workers" is the number of open connections that have each issued at least one reserve command.
	 * - "current-waiting" is the number of open connections that have issued a reserve command but not yet received a response.
	 * - "total-connections" is the cumulative count of connections.
	 * - "pid" is the process id of the server.
	 * - "version" is the version string of the server.
	 * - "rusage-utime" is the cumulative user CPU time of this process in seconds and microseconds.
	 * - "rusage-stime" is the cumulative system CPU time of this process in seconds and microseconds.
	 * - "uptime" is the number of seconds since this server process started running.
	 * - "binlog-oldest-index" is the index of the oldest binlog file needed to store the current jobs.
	 * - "binlog-current-index" is the index of the current binlog file being written to. If binlog is not active this value will be 0.
	 * - "binlog-max-size" is the maximum size in bytes a binlog file is allowed to get before a new binlog file is opened.
	 * - "binlog-records-written" is the cumulative number of records written to the binlog.
	 * - "binlog-records-migrated" is the cumulative number of records written as part of compaction.
	 * - "id" is a random id string for this server process, generated when each beanstalkd process starts.
	 * - "hostname" the hostname of the machine as determined by uname.
	 * @return
	 * @throws BeanstalkProtocolException
	 * @throws BeanstalkNotFoundException
	 * @throws BeanstalkDisconnectedException
	 * @throws BeanstalkServerException
	 * @throws IOException
	 */
	public Map<String, String> stats() throws BeanstalkProtocolException, BeanstalkNotFoundException, BeanstalkDisconnectedException, BeanstalkServerException, IOException{
		String response= doCommand("stats");

		if(!"OK".equals(response)){
			throw new BeanstalkProtocolException("Unexpected response: '" + response + "'");
		}
		return readMap();
	}


	/**
	 * A list of all tubes on the server
	 * @return
	 * @throws BeanstalkProtocolException
	 * @throws BeanstalkNotFoundException
	 * @throws BeanstalkDisconnectedException
	 * @throws BeanstalkServerException
	 * @throws IOException
	 */
	public List<String> listTubes() throws BeanstalkProtocolException, BeanstalkNotFoundException, BeanstalkDisconnectedException, BeanstalkServerException, IOException{
		String response= doCommand("list-tubes");

		if(!"OK".equals(response)){
			throw new BeanstalkProtocolException("Unexpected response: '" + response + "'");
		}
		return readList();
	}

	/**
	 * A list of all tubes currently watched by this client
	 * @return
	 * @throws BeanstalkProtocolException
	 * @throws BeanstalkNotFoundException
	 * @throws BeanstalkDisconnectedException
	 * @throws BeanstalkServerException
	 * @throws IOException
	 */
	public List<String> listTubesWatched() throws BeanstalkProtocolException, BeanstalkNotFoundException, BeanstalkDisconnectedException, BeanstalkServerException, IOException{
		String response= doCommand("list-tubes-watched");

		if(!"OK".equals(response)){
			throw new BeanstalkProtocolException("Unexpected response: '" + response + "'");
		}
		return readList();
	}
	/**
	 * @param tube Tube to pause
	 * @param delay Number of seconds to pause the tube
	 * @throws BeanstalkDisconnectedException
	 * @throws IOException
	 * @throws BeanstalkProtocolException
	 * @throws BeanstalkServerException
	 * @throws BeanstalkNotFoundException
	 */
	public void pauseTube(String tube, int delay) throws BeanstalkDisconnectedException, IOException, BeanstalkProtocolException, BeanstalkServerException, BeanstalkNotFoundException{
		String response= doCommand("pause-tube "+ tube + ' ' + Integer.toString(delay, 10));
		if("NOT_FOUND".equals(response)){
			throw new BeanstalkNotFoundException("Tube "+ tube +" not found.");
		}
		if(!"PAUSED".equals(response)){
			throw new BeanstalkProtocolException("Unexpected response: '" + response + "'");
		}
	}
}
