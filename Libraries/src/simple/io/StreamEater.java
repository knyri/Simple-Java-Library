package simple.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * Discards everything from the stream
 *
 */
public class StreamEater implements Runnable {
	private final InputStream food;
	public StreamEater(InputStream streamToEat) {
		food= streamToEat;
	}

	@Override
	public void run() {
		try {
			FileUtil.discard(food);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
