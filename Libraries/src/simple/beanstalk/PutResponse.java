package simple.beanstalk;

public final class PutResponse {
	public static enum Type{
		/**
		 * The job was successfully added to the tube
		 */
		INSERTED,
		/**
		 * The job was successfully added to the tube in the BURIED state.
		 * This can happen when the tube is full, paused, or the server is shutting down.
		 */
		BURIED
	}

	private final Type type;
	private final int jobId;
	public PutResponse(Type type, int jobId) {
		this.type= type;
		this.jobId= jobId;
	}
	public Type getType(){
		return type;
	}
	public int getJobId(){
		return jobId;
	}

}
