package simple.audio;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/**
 * Eases creation of classes that wrap an underlying AudioInputStream by passing the common
 * function to the stream. Call the only constructor in your constructor to take advantage.<br>
 * Created: 2012
 * @author Ken
 *
 */
public class WrapperAIS extends AudioInputStream{
	protected final AudioInputStream stream;
	public WrapperAIS(AudioFormat audioFormat,AudioInputStream audioInputStream){
		super(new ByteArrayInputStream(new byte[0]),audioFormat,AudioSystem.NOT_SPECIFIED);
		stream=audioInputStream;
	}
	public WrapperAIS(AudioFormat audioFormat,AudioInputStream audioInputStream,long length){
		super(new ByteArrayInputStream(new byte[0]),audioFormat,length);
		stream=audioInputStream;
	}
	public long getFrameLength(){
		return stream.getFrameLength();
	}
	public int read() throws IOException{
		byte[] samples=new byte[1];
		int ret=read(samples);
		if(ret!=1){
			return -1;
		}
		return samples[0];
	}
	public long skip(long lLength) throws IOException{
		return stream.skip(lLength);
	}
	public int available() throws IOException{
		return stream.available();
	}
	public void mark(int nReadLimit){
		stream.mark(nReadLimit);
	}
	public boolean markSupported(){
		return stream.markSupported();
	}
	public void reset() throws IOException{
		stream.reset();
	}
	@Override
	public void close() throws IOException{
		stream.close();
	}
	@Override
	public AudioFormat getFormat(){
		return super.getFormat();
	}
	@Override
	public int read(byte[] b,int off,int len) throws IOException{
		return stream.read(b,off,len);
	}
	@Override
	public int read(byte[] b) throws IOException{
		return read(b,0,b.length);
	}
	
}
