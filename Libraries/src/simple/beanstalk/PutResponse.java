package simple.beanstalk;

public final class PutResponse {
	public static enum Type{
		INSERTED,
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
