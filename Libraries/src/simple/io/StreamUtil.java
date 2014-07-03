package simple.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public final class StreamUtil {
	/**
	 * Reads until <code>end</code> is reached. The returned array includes the end byte.
	 * @param in InputStream to read from.
	 * @param end Byte to stop at.
	 * @return An array of the bytes read.
	 * @throws IOException
	 */
	public static byte[] readUntil(final InputStream in, final byte end) throws IOException {
		final ArrayList<Byte> buf = new ArrayList<Byte>();
		byte[] bbuf = new byte[1];
		while((in.read(bbuf)!=-1)){
			buf.add(new Byte(bbuf[0]));
			if (bbuf[0]==end)
				break;
		}
		bbuf = new byte[buf.size()];
		for (int i = 0;i<buf.size(); i++)
			bbuf[i] = buf.get(i).byteValue();
		return bbuf;
	}
	/**
	 * Reads until <code>end</code> is reached. The returned array includes the end byte.
	 * @param in InputStream to read from.
	 * @param end Byte to stop at.
	 * @return An array of the bytes read.
	 * @throws IOException
	 */
	public static byte[] readUntil(final InputStream in, final byte[] end) throws IOException {
		final ArrayList<Byte> buf = new ArrayList<Byte>();
		byte[] bbuf = new byte[end.length];
		int off=1,read,stop=end.length-1,i=0;
		done:
		while((read=in.read())!=-1){
			buf.add(new Byte((byte)read));
			if((byte)read!=end[0])continue;
			while(off<end.length && (read=in.read())!=-1){
				if((byte)read!=end[off]){
					off=1;
					break;
				}
				off++;
				if(off==stop)break done;
			}
		}
		bbuf = new byte[buf.size()];
		for (i = 0;i<buf.size(); i++)
			bbuf[i] = buf.get(i).byteValue();
		return bbuf;
	}
	/**
	 * Reads until <code>end</code> is reached.
	 * @param in InputStream to read from.
	 * @param end Byte to stop at.
	 * @throws IOException
	 */
	public static void skipUntil(final InputStream in, final byte[] end) throws IOException {
		int off=1,read,stop=end.length-1;
		while((read=in.read())!=-1){
			if((byte)read!=end[0])continue;
			while(off<end.length && (read=in.read())!=-1){
				if((byte)read!=end[off]){
					off=1;
					break;
				}
				off++;
				if(off==stop)return;
			}
		}
	}
	/**Reads until the first non-whitespace character is found.
	 * @param rd
	 * @return The non-whitespace character or -1
	 * @throws IOException
	 */
	public static char skipWhitespace(final InputStream rd) throws IOException {
		int c = rd.read();
		if (c==-1) return (char)c;
		do {
			if (!Character.isWhitespace(c)) break;
			c = rd.read();
		} while(c==-1);
		return (char)c;
	}
}
