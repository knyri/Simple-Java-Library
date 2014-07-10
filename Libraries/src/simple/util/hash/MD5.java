/**
 *
 */
package simple.util.hash;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

//import simple.util.BitArray;
import simple.util.HexUtil;
import simple.util.logging.Log;
import simple.util.logging.LogFactory;

/**Work in progress. It will produce a consistent hash, but not the correct MD5 one.
 * <hr>
 * <br>Created: Dec 13, 2010
 * @author Kenneth Pierce
 */
public final class MD5 {
	private static final Log log = LogFactory.getLogFor(MD5.class);
	/**Generates a MD5 hash of a file.
	 * @param file File to generate the hash for
	 * @return The MD5 hash
	 * @throws java.io.FileNotFoundException
	 * @throws IOException
	 */
	public static final String hash(File file) throws IOException {
		/*
		 * word = 32 bits
		 * byte = 8 bits
		 */
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));

		int state[] = new int[4];
		state[3] = 0x01234567;
		state[2] = 0x89abcdef;
		state[1] = 0xfedcba98;
		state[0] = 0x76543210;
			//hash pieces
		byte rbuf[] = new byte[64];
		long size = file.length();
		int added = (int) (size%512);//padding bits at the end
		if (added<448) {//pad to 448
			added = 448-added;
		} else if (added>=448) {//must add at least one bit
			added = 512-added+448;
		}
		int paddingRemaining = added/8;
		boolean addedPadding = false;
		long iterations = ((size+added+64)-(size+added)%512)/16;
		int read = 0;
		log.debug("iterations", iterations);
		log.debug("size",size);
		log.debug("added",added);
		log.debug("padding remaining",paddingRemaining);
		for (;iterations > 0; iterations--) {
			log.debug("-- loop --");
			read = in.read(rbuf);//read
			log.debug(read+" blocks read");
			if (read < 64) {
				if (read == -1) read = 0;
				if (!addedPadding) {
					log.debug("added first padding");
					rbuf[read] = Byte.MIN_VALUE;
					read++;
					addedPadding = true;
					paddingRemaining--;
					log.debug("padding remaining",paddingRemaining);
				}
				for (;read<64 && paddingRemaining > 0;read++) {
					rbuf[read] = 0;
					paddingRemaining--;
				}
				log.debug(read+" blocks read");
				log.debug("padding remaining",paddingRemaining);
				if (paddingRemaining == 0 && read < 64) {//append size
					log.debug("adding size");
					rbuf[read++] = (byte)(size);
					rbuf[read++] = (byte)(size >> 8);
					rbuf[read++] = (byte)(size >> 16);
					rbuf[read++] = (byte)(size >> 24);
					rbuf[read++] = (byte)(size >> 32);
					rbuf[read++] = (byte)(size >> 48);
					rbuf[read++] = (byte)(size >> 40);
					rbuf[read++] = (byte)(size >> 56);
					//rbuf[read++] = (byte)(size >> 64);
					log.debug(read+" blocks read");
				}
			}
			transform(state, rbuf);
		}
		in.close();
		return HexUtil.getHex(new byte[] {(byte)(state[0]>>24), (byte)(state[0]>>16), (byte)(state[0]>>8), (byte)state[0],
				(byte)(state[1]>>24), (byte)(state[1]>>16), (byte)(state[1]>>8), (byte)state[1],
				(byte)(state[2]>>24), (byte)(state[2]>>16), (byte)(state[2]>>8), (byte)state[2],
				(byte)(state[3]>>24), (byte)(state[3]>>16), (byte)(state[3]>>8), (byte)state[3]});
	}
	private static final void transform(int state[], byte buf[]) {
		//store for addition after operations
		int a = state[0], b = state[1], c = state[2], d = state[3], blocks[] = new int[16];
		Util.decode(blocks, buf);
		/*
		 * FLOW:stage 1
		 */
		a = R1(a,b,c,d,blocks[ 0],7, 0xd76aa478);
		d = R1(d,a,b,c,blocks[ 1],12, 0xe8c7b756);
		c = R1(c,d,a,b,blocks[ 2],17, 0x242070db);
		b = R1(b,c,d,a,blocks[ 3],22, 0xc1bdceee);

		a = R1(a,b,c,d,blocks[ 4], 7, 0xf57c0faf);
		d = R1(d,a,b,c,blocks[ 5],12, 0x4787c62a);
		c = R1(c,d,a,b,blocks[ 6],17, 0xa8304613);
		b = R1(b,c,d,a,blocks[ 7],22, 0xfd469501);

		a = R1(a,b,c,d,blocks[ 8], 7, 0x698098d8);
		d = R1(d,a,b,c,blocks[ 9],12,0x8b44f7af);
		c = R1(c,d,a,b,blocks[10],17,0xffff5bb1);
		b = R1(b,c,d,a,blocks[11],22,0x895cd7be);

		a = R1(a,b,c,d,blocks[12], 7,0x6b901122);
		d = R1(d,a,b,c,blocks[13],12,0xfd987193);
		c = R1(c,d,a,b,blocks[14],17,0xa679438e);
		b = R1(b,c,d,a,blocks[15],22,0x49b40821);

		/*
		 * FLOW:stage 2
		 */
		a = R2(a,b,c,d,blocks[ 1], 5,0xf61e2562);
		d = R2(d,a,b,c,blocks[ 6], 9,0xc040b340);
		c = R2(c,d,a,b,blocks[11],14,0x265e5a51);
		b = R2(b,c,d,a,blocks[ 0],20,0xe9b6c7aa);

		a = R2(a,b,c,d,blocks[ 5], 5,0xd62f105d);
		d = R2(d,a,b,c,blocks[10], 9,0x2441453);
		c = R2(c,d,a,b,blocks[15],14,0xd8a1e681);
		b = R2(b,c,d,a,blocks[ 4],20,0xe7d3fbc8);

		a = R2(a,b,c,d,blocks[ 9], 5,0x21e1cde6);
		d = R2(d,a,b,c,blocks[14], 9,0xc33707d6);
		c = R2(c,d,a,b,blocks[ 3],14,0xf4d50d87);
		b = R2(b,c,d,a,blocks[ 8],20,0x455a14ed);

		a = R2(a,b,c,d,blocks[13], 5,0xa9e3e905);
		d = R2(d,a,b,c,blocks[ 2], 9,0xfcefa3f8);
		c = R2(c,d,a,b,blocks[ 7],14,0x676f02d9);
		b = R2(b,c,d,a,blocks[12],20,0x8d2a4c8a);
		/*
		 * FLOW:stage 3
		 */
		a = R3(a,b,c,d,blocks[ 5], 4,0xfffa3942);
		d = R3(d,a,b,c,blocks[ 8],11,0x8771f681);
		c = R3(c,d,a,b,blocks[11],16,0x6d9d6122);
		b = R3(b,c,d,a,blocks[14],23,0xfde5380c);

		a = R3(a,b,c,d,blocks[ 1], 4,0xa4beea44);
		d = R3(d,a,b,c,blocks[ 4],11,0x4bdecfa9);
		c = R3(c,d,a,b,blocks[ 7],16,0xf6bb4b60);
		b = R3(b,c,d,a,blocks[10],23,0xbebfbc70);

		a = R3(a,b,c,d,blocks[13], 4,0x289b7ec6);
		d = R3(d,a,b,c,blocks[ 0],11,0xeaa127fa);
		c = R3(c,d,a,b,blocks[ 3],16,0xd4ef3085);
		b = R3(b,c,d,a,blocks[ 6],23,0x4881d05);

		a = R3(a,b,c,d,blocks[ 9], 4,0xd9d4d039);
		d = R3(d,a,b,c,blocks[12],11,0xe6db99e5);
		c = R3(c,d,a,b,blocks[15],16,0x1fa27cf8);
		b = R3(b,c,d,a,blocks[ 2],23,0xc4ac5665);
		/*
		 * FLOW:stage 4
		 */
		a = R4(a,b,c,d,blocks[ 0], 6, 0xf4292244);
		d = R4(d,a,b,c,blocks[ 7],10, 0x432aff97);
		c = R4(c,d,a,b,blocks[14],15, 0xab9423a7);
		b = R4(b,c,d,a,blocks[ 5],21, 0xfc93a039);

		a = R4(a,b,c,d,blocks[12], 6, 0x655b59c3);
		d = R4(d,a,b,c,blocks[ 3],10, 0x8f0ccc92);
		c = R4(c,d,a,b,blocks[10],15, 0xffeff47d);
		b = R4(b,c,d,a,blocks[ 1],21, 0x85845dd1);

		a = R4(a,b,c,d,blocks[ 8], 6, 0x6fa87e4f);
		d = R4(d,a,b,c,blocks[15],10, 0xfe2ce6e0);
		c = R4(c,d,a,b,blocks[ 6],15, 0xa3014314);
		b = R4(b,c,d,a,blocks[13],21, 0x4e0811a1);

		a = R4(a,b,c,d,blocks[ 4], 6, 0xf7537e82);
		d = R4(d,a,b,c,blocks[11],10, 0xbd3af235);
		c = R4(c,d,a,b,blocks[ 2],15, 0x2ad7d2bb);
		b = R4(b,c,d,a,blocks[ 9],21, 0xeb86d391);
		//add
		state[0] += a;
		state[1] += b;
		state[2] += c;
		state[3] += d;
	}
	private static final int F(int x, int y, int z) {
		return x&y | (~x)&z;
	}
	private static final int G(int x, int y, int z) {
		return x&z | y&(~z);
	}
	private static final int H(int x, int y, int z) {
		return x ^ y ^ z;
	}
	private static final int I(int x, int y, int z) {
		return y ^ (x | (~z));
	}
	private static final int R1 (int a, int b, int c, int d, int k, int s, int i) {
		a = a + F(b,c,d) + k + i;
		return b + Util.rotateLeft(a, s);
	}
	private static final int R2 (int a, int b, int c, int d, int k, int s, int i) {
		a = a + G(b,c,d) + k + i;
		return b + Util.rotateLeft(a, s);
		//return b + Util.rotationalLeftShift((a + G(b,c,d) + blocks[k] + T[i]), s);
	}
	private static final int R3 (int a, int b, int c, int d, int k, int s, int i) {
		a = a + H(b,c,d) + k + i;
		return b + Util.rotateLeft(a, s);
		//return b + Util.rotationalLeftShift((a + H(b,c,d) + blocks[k] + T[i]), s);
	}
	private static final int R4 (int a, int b, int c, int d, int k, int s, int i) {
		a = a + I(b,c,d) + k + i;
		return b + Util.rotateLeft(a, s);
		//return b + Util.rotationalLeftShift((a + I(b,c,d) + blocks[k] + T[i]), s);
	}
	public static void main(String[] arg) throws IOException {
		System.out.println(MD5.hash(new File("D:\\test.txt")));//d41d8cd98f00b204e9800998ecf8427e
		/*System.out.println(Integer.MAX_VALUE);
		System.out.println(Integer.MAX_VALUE+1);
		System.out.println(Integer.MAX_VALUE+Integer.MIN_VALUE);
		BitArray arr = new BitArray(Integer.SIZE);
		//arr.setBits(Integer.MIN_VALUE+60);
		arr.setBits((0xFF<<8)+0xff);
		System.out.println(arr.toString());
		arr = new BitArray(Integer.SIZE);
		arr.setBits(~(Integer.MIN_VALUE+60));
		System.out.println(arr.toString());
		arr = new BitArray(8);
		arr.setBits(0x44);
		System.out.println(arr.toString());
		arr = new BitArray(8);
		arr.setBits(Util.rotateLeft((byte)0x44, 3));
		System.out.println(arr.toString());*/
	}
}
