/* µCOMP:javac %sµ
   µEXEC:cmd /K java -classpath .. simple.BitArrayµ
 */
package simple.util;

/**
 * This handles bits of various sizes in a boolean array.<br>
 * The array size is set when made and cannot grow or shrink.
 * <br>Created: 2004
 * @author Kenneth Pierce
 * @version 1.0
 */
public final class BitArray {
  private final boolean[] bits;

  /**
   * Creates a BitArray of size 8.
   */
  public BitArray() {
	  bits = new boolean[8];
  }
  /**
   * Creates a BitArray with a size equal to the sum of the sizes of the BitArrays in the array.<br>
   * Think of it as the constructor for sub arrays.
   * The value of the array works like so:<br>
   * b[0].toString() = 00011; b[1].toString() = 100<br>
   * The new BitArray.toString() = 00011100
   * @param b
   */
  public BitArray(BitArray[] b) {
	  int size = 0;
	  for (int i = 0;i<b.length;i++) {
		  size+=b[i].getSize();
	  }
	  bits = new boolean[size];
	  size=0;
	  for (int i = b.length-1;i>-1;i--) {
		  setBits(b[i].getValue(), size);
		  size+=b[i].getSize();
	  }
  }
  /**
   * Creates a new BitArray with <var>size</var> bits.
   * 
   * @param size The number of bits to be used.
   */
  public BitArray(int size) {
    bits = new boolean[size];
  }
  /**
   * Creates a new BitArray with <var>size</var> bits and sets the value
   * to <var>value</var>.
   * 
   * @param size The number of bits to be used.
   * @param value The initial value.
   */
  public BitArray(int size, long value) {
    bits = new boolean[size];
    for (int i = 0;i<bits.length;i++) {
      bits[i] = tf(value>>>i&1);
    }
  }
  public BitArray(byte[] b) {
	  bits = new boolean[b.length*8];
	  int index = 0;
	  int j = 0;
	  for (int i = 0;i<b.length; i++) {
		  for (j=0;j<8;j++) {
			  if (((b[i]>>j)&0x01) == 0x01) {
				  setBitAt(index++);
			  }
		  }
	  }
  }
  /**
 * @return number of bits in array.
 */
public int getSize() {
	  return bits.length;
  }
  /**
 * @return The integral value.
 */
public long getValue() {
	  long v = 0;
	  for(int i = 0;i<getSize();i++) {
		  if (bits[i]) {
			  v = v|(1<<i);
		  }
	  }
	  return v;
  }
  /*public static void main(String[] args) {
    BitArray ba0 = new BitArray(2, 3), ba1 = new BitArray(6, 8);
    BitArray ba = new BitArray(new BitArray[] {ba0, ba1});
    System.out.println(ba0.toString()+ba1.toString());
    System.out.println(ba.toString());
    System.out.println(ba.getValue());
    /*
    System.out.println(ba.toString());
    System.out.println(ba.getValue());
    ba.setBit(1);
    System.out.println(ba.getValue());
    ba.unsetBit(1024);
    System.out.println(ba.getValue());
    ba.setBit(64);
    System.out.println(ba.getValue());
    System.out.println(ba.toString(' '));
    ba.toggleBit(64);
    System.out.println(ba.toString(' '));
    System.out.println(ba.getValue());
    //*
  }//*/

  /**
 * @param i
 * @return True if <var>i</var>!=0
 */
private boolean tf(long i) {
    return i!=0;
  }
  /**
   * 
   * @param bit Bit to check. Must be one of 2^<sup>x</sup>.
   * @return True or False depending on <var>bit</var>.
   */
  public boolean isSet(int bit) {
    for (int i = 0;i<bits.length;i++) {
      if (tf(bit>>>i&1)) {return bits[i];}
    }
    return false;
  }
  /**
   * Sets bit at index <var>index</var> to true.
   * @param index
   */
  public void setBitAt(int index) {
    bits[index]=true;
  }
  /**
   * Sets bit at index <var>index</var> to false.
   * @param index
   */
  public void unsetBitAt(int index) {
    bits[index]=false;
  }
  /**
   * Toggles bit at index <var>index</var>.
   * @param index
   */
  public void toggleBitAt(int index) {
    bits[index]=!bits[index];
  }
  public void setBits(long bits, int start) {
	  for (int i = 0;i<(getSize()+start);i++) {
		  if (tf(bits>>>i&1)) {this.bits[i+start]=true;}
	  }
  }
  /**
   * Sets bit at <var>bit</var> to true.<br>
   * 
   * @param bit One of 2<sup>x</sup>.
   */
  public void setBit(long bit) {
    for (int i = 0;;i++) {
      if (tf(bit>>>i&1)) {bits[i]=true;break;}
    }
  }
  /**
   * Sets 1 bits found in <var>bits</var> to true.
   * 
   * @param bits Int with the bits to set in it.
   */
  public void setBits(long bits) {
	  for (int i = 0;i<this.bits.length;i++) {
	      if (tf(bits>>>i&1)) {this.bits[i]=true;}
	   }  
  }
  /**
   * Sets bit at <var>bit</var> to false.
   * 
   * @param bit One of 2<sup>x</sup>.
   */
  public void unsetBit(long bit) {
    for (int i = 0;;i++) {
      if (tf(bit>>>i&1)) {bits[i]=false;break;}
    }
  }
  public void unsetBits(long bits) {
	for (int i = 0;i<this.bits.length;i++) {
	  if (tf(bits>>>i&1)) {this.bits[i]=false;}
    }
  }
  /**
 * Sets all bits to true.
 */
public void setAll() {
	  for (int i = 0;i<bits.length;i++) {
		  bits[i] = true;
	  }
  }
  /**
 * Sets all bits to false.
 */
public void unsetAll() {
	  for (int i = 0;i<bits.length;i++) {
		  bits[i] = false;
	  }
  }
  /**
   * Inverts the current set of bits.
   */
  public void toggleAll() {
	  for (int i = 0;i<bits.length;i++) {
		  bits[i] = !bits[i];
	  }
  }
  /**
   * Toggles the bit at <var>bit</var>.<br>
   * For example:<br>
   * bit = 32 and the bit array = 31+16<br>
   * When this is called given 32 the bit array now equals 32+31+16.<br>
   * It stops when the first bit is encountered, so if you give it 7 it will stop at
   * 1.
   * @param bit Bit to be toggled.
   */
  public void toggleBit(long bit) {
    for (int i = 0;;i++) {
      if (tf(bit>>>i&1)) {bits[i]=!bits[i];break;}
    }
  }
  /**
   * @return A string of binary.
   */
  public String toString() {
    StringBuffer t = new StringBuffer(bits.length);
    for (int i = bits.length-1;i>=0;i--) {
      t.append(bits[i]?1:0);
    }
    return new String(t);
  }
  /**
   * 
   * @param bitSeperator Charater to place between each byte(8 bits).
   * @return A string of binary numbers.
   */
  public String toString(char bitSeperator) {
    StringBuffer t = new StringBuffer(bits.length);
    for (int i = bits.length-1;i>=0;i--) {
      t.append(bits[i]?1:0);
      if (i%8==0) {t.append(bitSeperator);}
    }
    return new String(t);
  }
}