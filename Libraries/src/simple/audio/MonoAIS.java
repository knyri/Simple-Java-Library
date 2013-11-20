package simple.audio;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

import org.tritonus.share.sampled.FloatSampleBuffer;

import simple.util.logging.LogFactory;

/**
 * Down-mixes a multi-channel audio streamto one channel.
 * @author Ken
 *
 */
public class MonoAIS extends AudioInputStream{
	private static final simple.util.logging.Log log = LogFactory.getLogFor(MonoAIS.class);
	private final AudioInputStream stream;
	private byte[] tempBuffer=new byte[4096];
	private final FloatSampleBuffer mixBuffer;
	@Override
	public int read(byte[] b,int off,int len) throws IOException{
		int needRead=(len/getFormat().getFrameSize())*stream.getFormat().getFrameSize();
		if(tempBuffer==null||tempBuffer.length<needRead){
			tempBuffer=new byte[needRead];
		}

		// read from the source stream
		int bytesRead=stream.read(tempBuffer,0,needRead);
		if(bytesRead==-1)return -1;
		mixBuffer.initFromByteArray(tempBuffer,0,bytesRead,stream.getFormat());
		mixBuffer.mixDownChannels();
		byte[] btmp=mixBuffer.convertToByteArray(getFormat());
		System.arraycopy(btmp,0,b,0,btmp.length);
		return btmp.length;
	}

	public MonoAIS(AudioFormat format,AudioInputStream stream){
		super(new ByteArrayInputStream(new byte[0]),format,stream.getFrameLength());
		if(format.getChannels()!=1) throw new IllegalArgumentException("Can only downmix to 1 channel.");
		this.stream=stream;
		mixBuffer=new FloatSampleBuffer(format.getChannels(),0,format.getSampleRate());
		log.debug("stream format",stream.getFormat());
		log.debug("format",format);
	}

}
