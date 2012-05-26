package simple.io;

import java.io.*;

/** Links an OutputStream with an InputStream or a Writer with a Reader.
 * <br>Created: 2006
 * @author Kenneth Pierce
 */
public final class IOLink {
	private final OutputStream outs;
	private final InputStream ins;
	private final Writer outw;
	private final Reader inr;
	public IOLink(OutputStream out, InputStream in) {
		outs = out;
		ins = in;
		outw = null;
		inr = null;
	}
	public IOLink(Writer out, Reader in) {
		outw = out;
		inr = in;
		outs = null;
		ins = null;
	}
	/**Copies from the source to the destination. Blocks until finished.
	 * @throws IOException
	 */
	public synchronized void link() throws IOException {
		int read = 0;
		if (outs == null) {
			char[] buf = new char[255];
			while ((read = inr.read(buf))!=-1) {
				outw.write(buf, 0, read);
				outw.flush();
			}
		} else {
			byte[] buf = new byte[255];
			while ((read = ins.read(buf))!=-1) {
				outs.write(buf, 0, read);
				outs.flush();
			}
		}
	}
	/**
	 * Copies from the source to the destination in a new thread.
	 */
	public synchronized void linkAsynchronously() {
		new Thread(new Runnable() {
			public void run() {
				int read = 0;
				try {
					if (outs == null) {
						char[] buf = new char[255];
						while ((read = inr.read(buf))!=-1) {
							outw.write(buf, 0, read);
						}
					} else {
						byte[] buf = new byte[255];
						while ((read = ins.read(buf))!=-1) {
							outs.write(buf, 0, read);
						}
					}
				} catch (IOException e) {}
			}
		}).start();
	}
}
