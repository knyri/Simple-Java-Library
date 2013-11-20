package simple.audio;


import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

public class TrimmerAIS extends WrapperAIS{

	private final AudioInputStream stream;
	private final long startByte,endByte;
	private long t_bytesRead=0;
	private final long frame_length;

	public TrimmerAIS(AudioFormat audioFormat,AudioInputStream audioInputStream,long startMilli,long endMilli){
		super(audioFormat,audioInputStream,Util.milli2Frames(endMilli-startMilli,audioFormat));
		stream=audioInputStream;
		//calculate where to start and where to end
		startByte=Util.milli2Bytes(stream.getFormat(),startMilli);
		endByte=Util.milli2Bytes(stream.getFormat(),endMilli);
		frame_length=Util.milli2Frames(endMilli-startMilli,stream.getFormat());
	}

	@Override
	public int read(byte[] abData,int nOffset,int nLength) throws IOException{
		int bytesRead=0;
		if(t_bytesRead<startByte){//skip to start byte
			do{
				bytesRead=(int)skip(startByte-t_bytesRead);
				t_bytesRead+=bytesRead;
			}while(t_bytesRead<startByte);
		}
		if(t_bytesRead>=endByte)//end reached. signal EOF
			return -1;

		bytesRead=stream.read(abData,0,nLength);
		if(bytesRead==-1)
			return -1;
		else if(bytesRead==0)
			return 0;

		t_bytesRead+=bytesRead;
		if(t_bytesRead>=endByte)// "trim" the extra by altering the number of bytes read
			bytesRead=(int)(bytesRead-(t_bytesRead-endByte));

		return bytesRead;
	}

	@Override
	public long getFrameLength(){
		return frame_length;
	}

	@Override
	public int available() throws IOException{
		return (int)Math.min(stream.available(),endByte-startByte-t_bytesRead);
	}
}
