/**
 *
 */
package simple.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**Contains useful functions pertaining to just about anything
 * that is related to or backed by an array.
 * (no dependents)
 * <br>Created: Dec 3, 2007
 * @author Kenneth Pierce
 */
public final class do_array {
	private do_array() {}
	public static <E> int indexOf(E[] a, E v){
		for(int i=0; i<a.length; i++){
			if(a[i].equals(v)){
				return i;
			}
		}
		return -1;
	}
	public static <E> int lastIndexOf(E[] a, E v){
		for(int i=a.length-1; i>-1; i++){
			if(a[i].equals(v)){
				return i;
			}
		}
		return -1;
	}
	/**Converts an Enumeration into something use-able by a for-loop.
	 * @param <E>
	 * @param e The enumeration to convert
	 * @return An iterable class that can be used by for-loops.
	 */
	public static <E> Iterable<E> iterable(final Enumeration<E> e) {
		return new Iterable<E>() {
			@Override
			public Iterator<E> iterator() {
				return new Iterator<E>() {
					@Override
					public boolean hasNext() {
						return e.hasMoreElements();
					}
					@Override
					public E next() {
						return e.nextElement();
					}
					@Override
					public void remove() {}
				};
			}
		};
	}
	/** Converts an Enumeration into an Iterator.
	 * @param <E>
	 * @param e
	 * @return An Enumeration wrapped in an Iterator
	 */
	public static <E> Iterator<E> iterator(final Enumeration<E> e) {
		return new Iterator<E>() {
			final Enumeration<E> _e = e;
			@Override
			public boolean hasNext() {
				return _e.hasMoreElements();
			}
			@Override
			public E next() {
				return _e.nextElement();
			}
			@Override
			public void remove() {}
		};
	}
	/**
	 * Creates a typed Iterator for the supplied array.
	 * @param <E>
	 * @param e
	 * @return An iterator for the array.
	 */
	public static <E> Iterator<E> iterator(final E[] e) {
		return new Iterator<E>() {
			final E[] elements = e;
			int pos = 0;
			@Override
			public boolean hasNext() {
				if (pos==elements.length) return false;
				return true;
			}
			@Override
			public E next() {
				return elements[pos++];
			}
			@Override
			public void remove() {}
		};
	}
	/**
	 * Adds the array to the end of the vector.
	 * @param <E>
	 * @param vec Vector to add the elements to
	 * @param add Elements to add.
	 * @return The vector with the added elements.
	 */
	public static <E> Vector<E> addToVector(Vector<E> vec, E[] add) {
		Collections.addAll(vec,add);
		return vec;
	}
	/**
	 * Creates a typed enumeration for the supplied array.
	 * @param <E>
	 * @param e
	 * @return An Enumeration for the array.
	 */
	public static <E> Enumeration<E> enumeration(final E[] e) {
		return Collections.enumeration(Arrays.asList(e));
	}
	/**
	 * Takes an array and creates a typed Vector containing it.
	 * @param <E>
	 * @param list Array to be added.
	 * @return The resulting vector
	 */
	public static <E> Vector<E> toVector(E[] list) {
		return addToVector(new Vector<E>(), list);
	}
	/**
	 * Convenience reference to {@link java.util.Arrays#asList(Object[])}.
	 * @param <E>
	 * @param list List to be converted
	 * @return A List containing the array.
	 */
	public static <E> List<E> asList(E[] list) {
		return Arrays.asList(list);
	}
	/**Prints the array as a comma separated list.
	 * @param arr
	 */
	public static void print(Object[] arr) {
		System.out.println(arr[0]);
		for (int i = 1;i<arr.length-1;i++) {
			System.out.print(", "+arr[i]);
		}
	}
	/**Prints the array as a comma separated list.
	 * @param arr
	 */
	public static void print(int[] arr) {
		System.out.println(arr[0]);
		for (int i = 1;i<arr.length-1;i++) {
			System.out.print(", "+arr[i]);
		}
	}
	/**Prints the array as a comma separated list.
	 * @param arr
	 */
	public static void print(byte[] arr) {
		System.out.println(arr[0]);
		for (int i = 1;i<arr.length-1;i++) {
			System.out.print(", "+arr[i]);
		}
	}
	/**Prints a subset of the array.
	 * @param arr
	 * @param start
	 * @param length
	 */
	public static void print(Object[] arr, int start, int length) {
		System.out.println(arr[start]);
		for (int i = start+1;i<length;i++) {
			System.out.print(arr[start+i]+", ");
		}
	}
	/**Reverses the elements in the list
	 * @param <E>
	 * @param list
	 */
	public static <E> void reverse(E[] list) {
		E tmp;
		int mid = list.length/2;
		for (int i = 0; i < mid; i++) {
			tmp = list[i];
			list[i] = list[list.length-(i+1)];
			list[list.length-(i+1)] = tmp;
		}
	}
	/**Reverses the elements in the list
	 * @param list
	 */
	public static byte[] reverse(byte[] list) {
		byte tmp;
		int mid = list.length/2;
		for (int i = 0; i < mid; i++) {
			tmp = list[i];
			list[i] = list[list.length-(i+1)];
			list[list.length-(i+1)] = tmp;
		}
		return list;
	}
	/**Reverses the elements in the list
	 * @param list
	 */
	public static int[] reverse(int[] list) {
		int tmp;
		int mid = list.length/2;
		for (int i = 0; i < mid; i++) {
			tmp = list[i];
			list[i] = list[list.length-(i+1)];
			list[list.length-(i+1)] = tmp;
		}
		return list;
	}
	/**Reverses the elements in the list
	 * @param list
	 */
	public static short[] reverse(short[] list) {
		short tmp;
		int mid = list.length/2;
		for (int i = 0; i < mid; i++) {
			tmp = list[i];
			list[i] = list[list.length-(i+1)];
			list[list.length-(i+1)] = tmp;
		}
		return list;
	}
	/**Reverses the elements in the list
	 * @param list
	 */
	public static long[] reverse(long[] list) {
		long tmp;
		int mid = list.length/2;
		for (int i = 0; i < mid; i++) {
			tmp = list[i];
			list[i] = list[list.length-(i+1)];
			list[list.length-(i+1)] = tmp;
		}
		return list;
	}
	/**Reverses the elements in the list
	 * @param list
	 */
	public static char[] reverse(char[] list) {
		char tmp;
		int mid = list.length/2;
		for (int i = 0; i < mid; i++) {
			tmp = list[i];
			list[i] = list[list.length-(i+1)];
			list[list.length-(i+1)] = tmp;
		}
		return list;
	}
	/**Reverses the elements in the list
	 * @param list
	 */
	public static float[] reverse(float[] list) {
		float tmp;
		int mid = list.length/2;
		for (int i = 0; i < mid; i++) {
			tmp = list[i];
			list[i] = list[list.length-(i+1)];
			list[list.length-(i+1)] = tmp;
		}
		return list;
	}
	/**Reverses the elements in the list
	 * @param list
	 */
	public static double[] reverse(double[] list) {
		double tmp;
		int mid = list.length/2;
		for (int i = 0; i < mid; i++) {
			tmp = list[i];
			list[i] = list[list.length-(i+1)];
			list[list.length-(i+1)] = tmp;
		}
		return list;
	}
	@SuppressWarnings("unchecked")
	public static <E> E[] combine(E[] a,E[] b){
		Object[] c=new Object[a.length+b.length];
		System.arraycopy(a,0,c,0,a.length);
		System.arraycopy(b,0,c,a.length,b.length);
		return (E[])c;
	}
	public static byte[] grow(byte[] ary,int amount){
		byte[] tmp=new byte[ary.length+amount];
		System.arraycopy(ary,0,tmp,0,ary.length);
		return tmp;
	}
	public static short[] grow(short[] ary,int amount){
		short[] tmp=new short[ary.length+amount];
		System.arraycopy(ary,0,tmp,0,ary.length);
		return tmp;
	}
	public static int[] grow(int[] ary,int amount){
		int[] tmp=new int[ary.length+amount];
		System.arraycopy(ary,0,tmp,0,ary.length);
		return tmp;
	}
	public static long[] grow(long[] ary,int amount){
		long[] tmp=new long[ary.length+amount];
		System.arraycopy(ary,0,tmp,0,ary.length);
		return tmp;
	}
	public static double[] grow(double[] ary,int amount){
		double[] tmp=new double[ary.length+amount];
		System.arraycopy(ary,0,tmp,0,ary.length);
		return tmp;
	}
	public static float[] grow(float[] ary,int amount){
		float[] tmp=new float[ary.length+amount];
		System.arraycopy(ary,0,tmp,0,ary.length);
		return tmp;
	}
}
