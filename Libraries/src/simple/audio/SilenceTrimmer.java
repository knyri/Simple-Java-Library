/**
 *
 */
package simple.audio;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.tritonus.share.sampled.FloatSampleBuffer;

import simple.util.logging.Log;
import simple.util.logging.LogFactory;

/**
 * Attempts to trim the silence from the start of an audio stream
 * @author Ken
 *
 */
public final class SilenceTrimmer extends WrapperAIS{
	private static final Log log=LogFactory.getLogFor(SilenceTrimmer.class);
	private boolean readIntro=false;
	private final FloatSampleBuffer sampleBuffer;
	private final float minVolume;
	public SilenceTrimmer(AudioFormat audioFormat,AudioInputStream audioInputStream,int threshold){
		super(audioFormat,audioInputStream,AudioSystem.NOT_SPECIFIED);
		minVolume=Util.dbToGain(threshold);
		sampleBuffer=new FloatSampleBuffer();
		//log.debug("silence threshold",minVolume);
	}
	private int sampleOffset=0,sampleCount,channelCount,sampleOffsetChannel;
	@Override
	public int read(byte[] b,int off,int len) throws IOException{
		int read=stream.read(b,off,len);
		if(read==-1)return -1;
		if(!readIntro){
			//log.debug("processing intro - read",read);
			//Search for the smallest silence offset
			sampleBuffer.initFromByteArray(b,off,read,stream.getFormat());
			sampleCount=sampleBuffer.getSampleCount();
			try{
			for(channelCount=sampleBuffer.getChannelCount();channelCount>0;channelCount--){
				//log.debug("channel",channelCount);
				float[] samples=sampleBuffer.getChannel(channelCount-1);
				for(sampleOffsetChannel=0;sampleOffsetChannel<sampleCount;sampleOffsetChannel++){
					if(samples[sampleOffsetChannel]>minVolume){
						if(sampleOffsetChannel<sampleOffset || channelCount==1)sampleOffset=sampleOffsetChannel;
						break;
					}
				}
				//log.debug("channelOffset",sampleOffsetChannel);
			}
			read= sampleBuffer.convertToByteArray(sampleOffset,sampleCount-sampleOffset,b,0,getFormat());
			//log.debug("processing intro - read returned - sampleOffset - sampleCount",read+" - "+sampleOffset+" - "+sampleCount);
			}catch(Exception e){
				log.error(e);
				return -1;
			}
			readIntro=true;
		}
		return read;
	}

}
