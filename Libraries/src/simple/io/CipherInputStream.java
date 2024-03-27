package simple.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

/**
 * Wraps a Cipher
 */
public class CipherInputStream extends InputStream {
	private final Cipher c;
	private final InputStream i;
	private byte[] buf;
	private int pos= 0;
	private boolean eof= false;
	private final int bufsize;
	public CipherInputStream(Cipher c, File f, int bufferSize) throws IOException {
		this.c= c;
		this.i= new BufferedInputStream(new FileInputStream(f), bufferSize);
		this.bufsize= bufferSize;
		fillBuffer();
	}
	public CipherInputStream(Cipher c, File f) throws IOException {
		this(c, f, 1024*16);
	}
	private void fillBuffer() throws IOException {
		byte[] ib= new byte[bufsize];
		int read= i.read(ib);
		if(read == -1) {
			try{
				// at EoF, do final decode
				buf= c.doFinal();
				eof= true;
			}catch(IllegalBlockSizeException | BadPaddingException e){
				throw new IOException(e);
			}
		}else {
			buf= c.update(ib, 0, read);
		}
	}
	@Override
	public void close() throws IOException {
		i.close();
	}
	@Override
	public int available() throws IOException {
		return eof && pos == buf.length ? 0 : buf.length - pos;
	}
	@Override
	public int read() throws IOException{
		if(pos == buf.length) {
			if(eof) {
				return -1;
			}
			pos= 0;
			fillBuffer();
		}
		return buf[pos++];
	}
}