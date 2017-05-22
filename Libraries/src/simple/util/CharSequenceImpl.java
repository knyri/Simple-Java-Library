package simple.util;

public class CharSequenceImpl implements CharSequence {
	private final char[] sequence;
	public CharSequenceImpl(char[] source) {
		sequence= source;
	}

	@Override
	public char charAt(int index) {
		return sequence[index];
	}

	@Override
	public int length() {
		return sequence.length;
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		char[] dest= new char[start - end];
		System.arraycopy(sequence, start, dest, 0, end);
		return new CharSequenceImpl(dest);
	}

	@Override
	public String toString(){
		return new String(sequence);
	}
}
