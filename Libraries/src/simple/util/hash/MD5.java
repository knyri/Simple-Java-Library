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
		MD5State state=new MD5State();
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
		final byte[] buf=new byte[2048];
		while(in.read(buf) != -1)
			state.update(buf,buf.length);
		in.close();
		return state.finish();
	}
	public static final String hash(String str){
		MD5State state=new MD5State();
		byte[] buf=str.getBytes();
		state.update(buf,buf.length);
		return state.finish();
	}

	/**
	 * #define FF(a, b, c, d, x, s, ac) { \
 		(a) += F ((b), (c), (d)) + (x) + (UINT4)(ac); \
 		(a) = ROTATE_LEFT ((a), (s)); \
 		(a) += (b); \
  }
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param ibuf
	 * @param s
	 * @param ac
	 * @return
	 */
	private static final int R1 (int a, int b, int c, int d, int ibuf, int s, int ac) {
		a = a + ( (b&c) | ( (~b)&d ) ) + ibuf + ac;
		return b + ( (a << s) | (a >>> (32-s)) );
	}
	private static final int R2 (int a, int b, int c, int d, int ibuf, int s, int ac) {
		a = a + ( (b&d) | (c&(~d)) ) + ibuf + ac;
		return b + ( (a << s) | (a >>> (32-s)) );
	}
	private static final int R3(int a, int b, int c, int d, int ibuf, int s, int ac) {
		a = a + ( (b ^ c) ^ d ) + ibuf + ac;
		return b + ( (a << s) | (a >>> (32-s)) );
	}
	private static final int R4(int a, int b, int c, int d, int ibuf, int s, int l) {
		a = a + ( c ^ (b | (~d)) ) + ibuf + l;
		return b + ( (a << s) | (a >>> (32-s)) );
	}
	private final static class MD5State{
		private static final byte[] padding= new byte[] {
			  (byte)0x80, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
			};
		private long blocksProcessed=0;
		/*
		 * "words" are low byte order (reversed)
		 * context->state[0] = 0x67452301;
			context->state[1] = 0xefcdab89;
			context->state[2] = 0x98badcfe;
			context->state[3] = 0x10325476;
			word A: 01 23 45 67
			word B: 89 ab cd ef
	 		word C: fe dc ba 98
			word D: 76 54 32 10

		 */
		private final int state[] = new int[] {
				0x67452301,
				0xefcdab89,
				0x98badcfe,
				0x10325476
			};

		/**
		 * Leftover bits from an update
		 */
		private final byte part[] = new byte[64];
		private int partIndex=0;

		private String hash=null;
		public void update(final byte[] buf, final int length){
			int added = 0;
			if(partIndex > 0){
				//length to be added to the part buffer
				added = Math.min(length, 64-partIndex);
				System.arraycopy(buf,0,part,partIndex,added);
				if (partIndex + added == 64)
					partIndex=0;
				else{
					// do nothing else until we have 64 bytes
					partIndex+=added;
					return;
				}
				transform(part,0);
			}
			// length minus the bytes added to the buffer
			int leftover = (length - added) % 64;
			if(leftover > 0){
				// keep low order bytes for the next update
				System.arraycopy(buf, length-leftover ,part,0,leftover);
				partIndex=leftover;
			}
			for(int i=added;i<(length-63);i+=64){
				transform(buf, i);
			}
		}
		private void transform(byte[] buf,int offset){
			blocksProcessed++;
			int
				a=state[0],
				b=state[1],
				c=state[2],
				d=state[3];
			final int ibuf[]=new int[16];
			Util.decode( ibuf, buf, offset );
			log.debug("ibuf",ibuf);

			a = R1(a, b, c, d, ibuf[ 0], 7,  0xd76aa478);
			d = R1(d, a, b, c, ibuf[ 1], 12,  0xe8c7b756);
			c = R1(c, d, a, b, ibuf[ 2], 17,  0x242070db);
			b = R1(b, c, d, a, ibuf[ 3], 22,  0xc1bdceee);

			a = R1(a, b, c, d, ibuf[ 4],  7,  0xf57c0faf);
			d = R1(d, a, b, c, ibuf[ 5], 12,  0x4787c62a);
			c = R1(c, d, a, b, ibuf[ 6], 17,  0xa8304613);
			b = R1(b, c, d, a, ibuf[ 7], 22,  0xfd469501);

			a = R1(a, b, c, d, ibuf[ 8],  7,  0x698098d8);
			d = R1(d, a, b, c, ibuf[ 9], 12, 0x8b44f7af);
			c = R1(c, d, a, b, ibuf[10], 17, 0xffff5bb1);
			b = R1(b, c, d, a, ibuf[11], 22, 0x895cd7be);

			a = R1(a, b, c, d, ibuf[12],  7, 0x6b901122);
			d = R1(d, a, b, c, ibuf[13], 12, 0xfd987193);
			c = R1(c, d, a, b, ibuf[14], 17, 0xa679438e);
			b = R1(b, c, d, a, ibuf[15], 22, 0x49b40821);

			/* ************
			 * Stage 2
			 * ************/
			a = R2(a, b, c, d, ibuf[ 1],  5, 0xf61e2562);
			d = R2(d, a, b, c, ibuf[ 6],  9, 0xc040b340);
			c = R2(c, d, a, b, ibuf[11], 14, 0x265e5a51);
			b = R2(b, c, d, a, ibuf[ 0], 20, 0xe9b6c7aa);

			a = R2(a, b, c, d, ibuf[ 5],  5, 0xd62f105d);
			d = R2(d, a, b, c, ibuf[10],  9, 0x2441453);
			c = R2(c, d, a, b, ibuf[15], 14, 0xd8a1e681);
			b = R2(b, c, d, a, ibuf[ 4], 20, 0xe7d3fbc8);

			a = R2(a, b, c, d, ibuf[ 9],  5, 0x21e1cde6);
			d = R2(d, a, b, c, ibuf[14],  9, 0xc33707d6);
			c = R2(c, d, a, b, ibuf[ 3], 14, 0xf4d50d87);
			b = R2(b, c, d, a, ibuf[ 8], 20, 0x455a14ed);

			a = R2(a, b, c, d, ibuf[13],  5, 0xa9e3e905);
			d = R2(d, a, b, c, ibuf[ 2],  9, 0xfcefa3f8);
			c = R2(c, d, a, b, ibuf[ 7], 14, 0x676f02d9);
			b = R2(b, c, d, a, ibuf[12], 20, 0x8d2a4c8a);

			/* ***********
			 * Stage 3
			 * ***********/
			a = R3(a, b, c, d, ibuf[ 5],  4, 0xfffa3942);
			d = R3(d, a, b, c, ibuf[ 8], 11, 0x8771f681);
			c = R3(c, d, a, b, ibuf[11], 16, 0x6d9d6122);
			b = R3(b, c, d, a, ibuf[14], 23, 0xfde5380c);

			a = R3(a, b, c, d, ibuf[ 1],  4, 0xa4beea44);
			d = R3(d, a, b, c, ibuf[ 4], 11, 0x4bdecfa9);
			c = R3(c, d, a, b, ibuf[ 7], 16, 0xf6bb4b60);
			b = R3(b, c, d, a, ibuf[10], 23, 0xbebfbc70);

			a = R3(a, b, c, d, ibuf[13],  4, 0x289b7ec6);
			d = R3(d, a, b, c, ibuf[ 0], 11, 0xeaa127fa);
			c = R3(c, d, a, b, ibuf[ 3], 16, 0xd4ef3085);
			b = R3(b, c, d, a, ibuf[ 6], 23, 0x4881d05);

			a = R3(a, b, c, d, ibuf[ 9],  4, 0xd9d4d039);
			d = R3(d, a, b, c, ibuf[12], 11, 0xe6db99e5);
			c = R3(c, d, a, b, ibuf[15], 16, 0x1fa27cf8);
			b = R3(b, c, d, a, ibuf[ 2], 23, 0xc4ac5665);

			/* ***********
			 * Stage 4
			 * ***********/
			a = R4(a, b, c, d, ibuf[ 0],  6,  0xf4292244);
			d = R4(d, a, b, c, ibuf[ 7], 10,  0x432aff97);
			c = R4(c, d, a, b, ibuf[14], 15,  0xab9423a7);
			b = R4(b, c, d, a, ibuf[ 5], 21,  0xfc93a039);

			a = R4(a, b, c, d, ibuf[12],  6,  0x655b59c3);
			d = R4(d, a, b, c, ibuf[ 3], 10,  0x8f0ccc92);
			c = R4(c, d, a, b, ibuf[10], 15,  0xffeff47d);
			b = R4(b, c, d, a, ibuf[ 1], 21,  0x85845dd1);

			a = R4(a, b, c, d, ibuf[ 8],  6,  0x6fa87e4f);
			d = R4(d, a, b, c, ibuf[15], 10,  0xfe2ce6e0);
			c = R4(c, d, a, b, ibuf[ 6], 15,  0xa3014314);
			b = R4(b, c, d, a, ibuf[13], 21,  0x4e0811a1);

			a = R4(a, b, c, d, ibuf[ 4],  6,  0xf7537e82);
			d = R4(d, a, b, c, ibuf[11], 10,  0xbd3af235);
			c = R4(c, d, a, b, ibuf[ 2], 15,  0x2ad7d2bb);
			b = R4(b, c, d, a, ibuf[ 9], 21,  0xeb86d391);

			state[0]+= a;
			state[1]+= b;
			state[2]+= c;
			state[3]+= d;
		}
		public String finish(){
			if(hash!=null) return hash;
			/*
			 * Multiply blocks by 64
			 * Add the bytes in the buffer
			 * Multiply by 8
			 */
			long bitsProcessed= ( (blocksProcessed << 6) + partIndex) << 3;

			// pad to 448 bits (56 bytes)
			update(padding, (partIndex < 56) ? (56 - partIndex) : (120 - partIndex) );

			//add the length
			byte buf[] = new byte[8];
			for(byte i=0;i<8;i++){
				buf[i]=(byte)bitsProcessed;
				bitsProcessed = bitsProcessed >> 8;
			}
			update(buf,8);

			hash=HexUtil.getHex(new byte[] {
					(byte)state[0],
					(byte)(state[0]>>>8),
					(byte)(state[0]>>>16),
					(byte)(state[0]>>>24),
					(byte)state[1],
					(byte)(state[1]>>>8),
					(byte)(state[1]>>>16),
					(byte)(state[1]>>>24),
					(byte)state[2],
					(byte)(state[2]>>>8),
					(byte)(state[2]>>>16),
					(byte)(state[2]>>>24),
					(byte)state[3],
					(byte)(state[3]>>>8),
					(byte)(state[3]>>>16),
					(byte)(state[3]>>>24),
					});
			return hash;
		}
	}
	public static void main(String[] arg) throws IOException {
		log.debug( hash("") ); //d41d8cd98f00b204e9800998ecf8427e
		log.debug( hash("a") ); //0cc175b9c0f1b6a831c399e269772661
		log.debug( hash("abc") ); //900150983cd24fb0d6963f7d28e17f72

		System.exit(0);
	}
}
