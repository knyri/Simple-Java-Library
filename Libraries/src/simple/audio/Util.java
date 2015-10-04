package simple.audio;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.tritonus.sampled.convert.PCM2PCMConversionProvider;
import org.tritonus.sampled.convert.SampleRateConversionProvider;

import simple.util.logging.Log;
import simple.util.logging.LogFactory;

public final class Util{
	private static final Log log=LogFactory.getLogFor(Util.class);
	public static final float[] dbToGain={1.0f,0.8912509f,0.7943282f,0.70794576f,0.63095737f,0.56234133f,0.5011872f,0.4466836f,0.39810717f,0.3548134f,0.31622776f,0.2818383f,0.25118864f,0.22387213f,0.19952624f,0.17782794f,0.15848932f,0.14125374f,0.12589255f,0.11220185f,0.1f,0.089125104f,0.07943282f,0.07079458f,0.063095726f,0.056234132f,0.05011873f,0.044668358f,0.03981072f,0.035481334f,0.031622775f,0.028183833f,0.025118863f,0.022387212f,0.019952621f,0.017782794f,0.015848933f,0.014125375f,0.012589254f,0.011220183f,0.01f,0.00891251f,0.007943284f,0.0070794565f,0.0063095726f,0.0056234132f,0.005011873f,0.004466837f,0.003981071f,0.0035481334f,0.0031622776f,0.0028183833f,0.0025118869f,0.0022387207f,0.001995262f,0.0017782794f,0.0015848933f,0.0014125379f,0.0012589252f,0.0011220183f,0.001f,8.91251E-4f,7.943284E-4f,7.079456E-4f,6.3095725E-4f,5.623413E-4f,5.011873E-4f,4.466837E-4f,3.9810708E-4f,3.5481335E-4f,3.1622776E-4f,2.8183832E-4f,2.511887E-4f,2.2387206E-4f,1.9952621E-4f,1.7782794E-4f,1.5848933E-4f,1.4125378E-4f,1.2589252E-4f,1.1220184E-4f,1.0E-4f,8.912505E-5f,7.9432844E-5f,7.0794566E-5f,6.3095766E-5f,5.6234134E-5f,5.0118702E-5f,4.466837E-5f,3.9810708E-5f,3.5481353E-5f,3.1622778E-5f,2.8183817E-5f,2.511887E-5f,2.2387207E-5f,1.9952631E-5f};
	public static final SampleRateConversionProvider SFCP=new SampleRateConversionProvider();
	public static final PCM2PCMConversionProvider P2PCP=new PCM2PCMConversionProvider();
	/**Converts from decibels to gain. Uses the absolute value so -94 is the same as 94.
	 * @param db Volume in decibels. Must be in the range -94 to 94.
	 * @return The volume in gain (0.0 - 1.0)
	 */
	public static float dbToGain(int db){
		if(db<0)db=-db;
		if(db>94)throw new IllegalArgumentException("dB must be between 0 and -94.");
		return dbToGain[db];
		//return (float)Math.pow(10,db/20f);
	}
	public static double gainToDb(double volume){
		double ret=0;
		if(volume==0)		ret=-94;
		else if(volume>0)	ret=(20*Math.log10(volume));
		else				ret=-(20*Math.log10(-volume));
		//log.debug("gain - dB",volume+" - "+ret);
		return ret;
	}
	public static long milli2Frames(long lLengthInMilliseconds,float fFrameRate){
		return (long)(lLengthInMilliseconds*fFrameRate/1000);
	}
	public static long milli2Frames(long lLengthInMilliseconds,AudioFormat format){
		if(format.getFrameRate()==AudioSystem.NOT_SPECIFIED)return AudioSystem.NOT_SPECIFIED;
		return (long)(lLengthInMilliseconds*format.getFrameRate()/1000);
	}
	/**Calculates the stream length in bytes for the data excluding header information.
	 * @param format audio format
	 * @param milli length in milliseconds
	 * @return The stream length in bytes minus header information
	 */
	public static long milli2Bytes(AudioFormat format,long milli){
		return (long)(format.getFrameRate()*format.getFrameSize()*(milli/1000f));
	}
	/**Calculates the stream length in bytes for the data excluding header information.
	 * @param format audio format
	 * @param bytes length of the stream in bytes
	 * @return The stream length in bytes minus header information
	 */
	public static long bytes2Milli(AudioFormat format,long bytes){
		return (long)((bytes/format.getFrameSize())/format.getFrameRate());
	}
	/**Calculates the length of the stream in bytes.
	 * NOTE: This does <strong>not</strong> check for {@linkplain AudioSystem#NOT_SPECIFIED} and may return gibberish if the
	 * frame length or frame size are set to this value.
	 * @param format audio format
	 * @param frames length in frames
	 * @return The length in bytes
	 */
	public static long frames2Bytes(AudioFormat format,long frames){
		return format.getFrameSize()*frames;
	}
	/**Calculates the length of the stream in milliseconds.
	 * NOTE: This does <strong>not</strong> check for {@linkplain AudioSystem#NOT_SPECIFIED} and may return gibberish if the
	 * frame length or frame rate are set to this value.
	 * @param af audio format
	 * @param frameLength length in frames
	 * @return The length of the stream in milliseconds.
	 */
	public static long frames2milli(AudioFormat af,long frameLength){
		return (long)((frameLength/af.getFrameRate())*1000);
	}
	/** The length of the stream in samples.
	 * @param af audio format
	 * @param frameLength length in frames
	 * @return Length of the stream in samples
	 */
	public static long frames2samples(AudioFormat af,long frameLength){
		return frameLength*(af.getFrameSize()/(af.getSampleSizeInBits()/8));
	}
	/** Number of samples per frame.
	 * @param af audio format
	 * @return samples per frame
	 */
	public static int getSamplesPerFrame(AudioFormat af){
		return af.getFrameSize()/(af.getSampleSizeInBits()/8);
	}
	public static int getSampleSize(AudioFormat af){
		return af.getSampleSizeInBits()/8;
		//return af.getFrameSize()/af.getChannels();
	}
	/**Calculates the length of the stream in seconds.
	 * NOTE: This does <strong>not</strong> check for {@linkplain AudioSystem#NOT_SPECIFIED} and may return gibberish if the
	 * frame length or frame rate are set to this value.
	 * @param ais the audio input stream
	 * @return The length of the stream in milliseconds.
	 */
	public static long getMilliLength(AudioInputStream ais){
		AudioFormat aisf=ais.getFormat();
		return (long)((ais.getFrameLength()/aisf.getFrameRate())*1000);
	}
	/**Calculates the length of the stream in bytes.
	 * NOTE: This does <strong>not</strong> check for {@linkplain AudioSystem#NOT_SPECIFIED} and may return gibberish if the
	 * frame length or frame size are set to this value.
	 * @param ais the audio input stream
	 * @return The length of the stream in bytes.
	 */
	public static long getByteLength(AudioInputStream ais){
		return frames2Bytes(ais.getFormat(),ais.getFrameLength());
	}
	/** The length of the stream in samples.
	 * @param ais the audio input stream
	 * @return Length of the stream in samples
	 */
	public static long getSampleLength(AudioInputStream ais){
		return frames2samples(ais.getFormat(),ais.getFrameLength());
	}
	/** Number of samples per frame.
	 * @param ais the audio input stream
	 * @return samples per frame
	 */
	public static int getSamplesPerFrame(AudioInputStream ais){
		return getSamplesPerFrame(ais.getFormat());
	}
	public static boolean changePCMFormatTo(AudioFormat to,File from) throws IOException, UnsupportedAudioFileException{
			try(AudioInputStream ostream=AudioSystem.getAudioInputStream(from)){
				AudioInputStream stream= null;
				if(ostream.getFormat().getSampleRate()==to.getSampleRate() && ostream.getFormat().getChannels()==to.getChannels()){
					return true;
				}
		log.debug("changePCMFormatTo() File [From] [To]",from+" ["+ostream.getFormat()+"] ["+to+"]");
				File tmp=File.createTempFile("changeSampleRateTemp",".wav");
				if(ostream.getFormat().getChannels()!=to.getChannels()){
					AudioFormat old=ostream.getFormat();
					if(ostream.getFormat().getChannels()<to.getChannels()){
						stream=P2PCP.getAudioInputStream(new AudioFormat(old.getEncoding(),old.getSampleRate(),old.getSampleSizeInBits(),to.getChannels(),
								to.getFrameSize(),old.getFrameRate(),old.isBigEndian()),ostream);
					}else{
						stream=new MonoAIS(new AudioFormat(old.getEncoding(),old.getSampleRate(),old.getSampleSizeInBits(),1,
								old.getFrameSize()/old.getChannels(),old.getFrameRate(),old.isBigEndian()),ostream);
					}
				}else{
					stream= ostream;
				}
				if(stream.getFormat().getSampleRate()!=to.getSampleRate())
					stream=SFCP.getAudioInputStream(to,stream);
				try{
					AudioSystem.write(stream,Type.WAVE,tmp);
				}finally{
					stream.close();
				}
				if(!from.delete())
					log.warning("changePCMFormatTo() Could not delete ["+from+"]");
				if(tmp.renameTo(from))
					return true;
				else
					log.error("changePCMFormatTo() Could not rename ["+tmp+"] to ["+from+"]");
			}
		return false;
	}
	public static boolean trimSilence(File file) throws IOException, UnsupportedAudioFileException{
	log.debug("trimSilence() File",file);
		File tmp=File.createTempFile("trimSilenceTemp",".wav");
		try(AudioInputStream stream=AudioSystem.getAudioInputStream(file)){
			try(AudioInputStream trimstream=new SilenceTrimmer(stream.getFormat(),stream,40)){
				AudioSystem.write(stream,Type.WAVE,tmp);
				if(!file.delete())
					log.warning("trimSilence() Could not delete ["+file+"]");
				if(tmp.renameTo(file))
					return true;
				else
					log.error("trimSilence() Could not rename ["+tmp+"] to ["+file+"]");
			}
		}
		return false;
	}
}