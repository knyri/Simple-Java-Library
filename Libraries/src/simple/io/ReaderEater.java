package simple.io;

import java.io.IOException;
import java.io.Reader;

public class ReaderEater implements Runnable {

	private final Reader food;
	public ReaderEater(Reader readerToEat) {
		food= readerToEat;
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
