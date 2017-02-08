package simple.util;

import java.io.File;
import java.util.Comparator;
import java.util.List;

/**
 * Not sure if I use these. Created as an assignment for a database management class.
 * <br>Created: Feb 13, 2008
 * @author Kenneth Pierce
 */
public final class do_sort {
	/*public static void main (String[] args) {
		int size= 50;
		int[] data= new int[size];
		for (int i= 0; i<size;i++)
			data[i]= (int)(Math.random()*100);
		insertionSort(data);
		do_array.print(data);
	}*/
	public static void swap(final int data[], final int e0, final int e1) {
		final int tmp= data[e0];
		data[e0]= data[e1];
		data[e1]= tmp;
	}
	public static void swap(final short data[], final int e0, final int e1) {
		final short tmp= data[e0];
		data[e0]= data[e1];
		data[e1]= tmp;
	}
	public static void swap(final long data[], final int e0, final int e1) {
		final long tmp= data[e0];
		data[e0]= data[e1];
		data[e1]= tmp;
	}
	public static void swap(final double data[], final int e0, final int e1) {
		final double tmp= data[e0];
		data[e0]= data[e1];
		data[e1]= tmp;
	}
	public static void swap(final float data[], final int e0, final int e1) {
		final float tmp= data[e0];
		data[e0]= data[e1];
		data[e1]= tmp;
	}
	public static void swap(final Object data[], final int e0, final int e1) {
		final Object tmp= data[e0];
		data[e0]= data[e1];
		data[e1]= tmp;
	}
	public static <T> void selectionSort(final T[] data, Comparator<T> comparator){
		int min, scan;
		for(int j= 0, len= data.length; j < len; j++) {
			min= j;
			for(scan= j + 1; scan<data.length; scan++){
				if(comparator.compare(data[scan], data[min]) < 0){
					min= scan;
				}
			}
			// Swap
			swap(data, min, j);
		}
	}

	public static <T> void insertionSort(final T[] data, Comparator<T> comparator) {
		/* Start at 1st element and shift elements down
		 * until a value lower than key is found and
		 * shift that one down until a lower is found
		 * and shift that one down...
		 */
		int pos;
		T key;
		for(int j= 1, len= data.length; j < len; j++) {
			key= data[j];
			pos= j;
			// shifting
			while(pos > 0 && comparator.compare(data[pos-1], key) > 0) {
				data[pos]= data[pos - 1];
				pos--;
			}
			data[pos]= key;
		}
	}

	public static <T> void bubbleSort(final T[] data, Comparator<T> comparator) {
		/* Start from end and move to start.
		 * Start from start and move to the end
		 * moving the largest element down as we go.
		 */
		for (int i= data.length - 1; i > 0; i--) {
			for(int j= 0; j < i; j++) {
				if (comparator.compare(data[j], data[j + 1]) > 0) {
					swap(data, j + 1, j);
				}
			}
		}
	}
	public static void bubbleSort(final String data[]) {
		/* Start from end and move to start.
		 * Start from start and move to the end
		 * moving the largest element down as we go.
		 */
		for (int i= data.length-1; i>0; i--) {
			for(int j= 0; j<i; j++) {
				if (data[j].compareTo(data[j+1])>0) {
					swap(data, j+1, j);
				}
			}
		}
	}
//	============ start qsort===========
	public static <T> void swap(List<T> data, int a, int b){
		T aT= data.get(a);
		data.set(a, data.get(b));
		data.set(b, aT);
	}
	public static <T> void quickSort(List<T> data, Comparator<T> comparer){
		quickSort(data, comparer, 0, data.size()-1);
	}
	public static <T> void quickSort(List<T> data, Comparator<T> comparer, int left, int right){
		if (left >= right){
			return;
		}
		int l= left,
			r= right;
		final T mid= data.get((left + right)/2);
		// Keep going until we've hit the middle
		while(l<=r) {
			while(comparer.compare(data.get(l), mid) < 0 && l < right){
				++l;
			}
			while(comparer.compare(data.get(r), mid) > 0 && r > left){
				--r;
			}
			if(l <= r) {
				swap(data, r, l);
				/* Keeps it from hitting an infinite loop
				 * also keeps it from sorting a large number
				 * of size 2 arrays which slows it down
				 * considerably and can cause stack overflows.
				 */
				++l;
				--r;
			}
		}
		// quick-sort left part if r has not reached the left end
		if (left < r){
			quickSort(data, comparer, left, r);
		}

		// quick sort right part if l has not reached the right end
		if (l < right){
			quickSort(data, comparer, l, right);
		}
	}
	public static <T> void quickSort(T[] data, Comparator<T> comparer){
		quickSort(data, comparer, 0, data.length-1);
	}
	public static <T> void quickSort(T[] data, Comparator<T> comparer, int left, int right){
		if (left >= right){
			return;
		}
		int l= left,
			r= right;
		final T mid= data[(left + right)/2];
		// Keep going until we've hit the middle
		while(l<=r) {
			while(comparer.compare(data[l], mid) < 0 && l < right){
				++l;
			}
			while(comparer.compare(data[r], mid) > 0 && r > left){
				--r;
			}
			if(l <= r) {
				swap(data, r, l);
				/* Keeps it from hitting an infinite loop
				 * also keeps it from sorting a large number
				 * of size 2 arrays which slows it down
				 * considerably and can cause stack overflows.
				 */
				++l;
				--r;
			}
		}
		// quick-sort left part if r has not reached the left end
		if (left < r){
			quickSort(data, comparer, left, r);
		}

		// quick sort right part if l has not reached the right end
		if (l < right){
			quickSort(data, comparer, l, right);
		}
	}
	public static void quickSort(final Integer data[]) {
		quickSort(data, IntegerComparator.get(), 0, data.length-1);
	}
	public static void quickSort(final Long data[]) {
		quickSort(data, LongComparator.get(), 0, data.length-1);
	}
	public static void quickSort(final Short data[]) {
		quickSort(data, ShortComparator.get(), 0, data.length-1);
	}
	public static void quickSort(final Double data[]) {
		quickSort(data, DoubleComparator.get(), 0, data.length-1);
	}
	public static void quickSort(final Float data[]) {
		quickSort(data, FloatComparator.get(), 0, data.length-1);
	}
	public static void quickSort(final String data[]) {
		quickSort(data, StringComparator.get(), 0, data.length-1);
	}
	private static final Object initSync= new Object();
	public static final class StringComparator implements Comparator<String>{
		private static StringComparator c= null;
		public static StringComparator get(){
			if(c == null){
				synchronized(initSync){
					if(c == null){
						c= new StringComparator();
					}
				}
			}
			return c;
		}
		@Override
		public int compare(String o1, String o2) {
			return o1.compareTo(o2);
		}
	}
	public static final class IntegerComparator implements Comparator<Integer>{
		private static IntegerComparator c= null;
		public static IntegerComparator get(){
			if(c == null){
				synchronized(initSync){
					if(c == null){
						c= new IntegerComparator();
					}
				}
			}
			return c;
		}
		@Override
		public int compare(Integer o1, Integer o2) {
			return o1 - o2;
		}
	}
	public static final class ByteComparator implements Comparator<Byte>{
		private static ByteComparator c= null;
		public static ByteComparator get(){
			if(c == null){
				synchronized(initSync){
					if(c == null){
						c= new ByteComparator();
					}
				}
			}
			return c;
		}
		@Override
		public int compare(Byte o1, Byte o2) {
			return o1 - o2;
		}
	}
	public static final class ShortComparator implements Comparator<Short>{
		private static ShortComparator c= null;
		public static ShortComparator get(){
			if(c == null){
				synchronized(initSync){
					if(c == null){
						c= new ShortComparator();
					}
				}
			}
			return c;
		}
		@Override
		public int compare(Short o1, Short o2) {
			return o1 - o2;
		}
	}
	public static final class LongComparator implements Comparator<Long>{
		private static LongComparator c= null;
		public static LongComparator get(){
			if(c == null){
				synchronized(initSync){
					if(c == null){
						c= new LongComparator();
					}
				}
			}
			return c;
		}
		@Override
		public int compare(Long o1, Long o2) {
			if(o1 < o2){
				return -1;
			}
			if(o1 > o2){
				return 1;
			}
			return 0;
		}
	}
	public static final class DoubleComparator implements Comparator<Double>{
		private static DoubleComparator c= null;
		public static DoubleComparator get(){
			if(c == null){
				synchronized(initSync){
					if(c == null){
						c= new DoubleComparator();
					}
				}
			}
			return c;
		}
		@Override
		public int compare(Double o1, Double o2) {
			if(o1 < o2){
				return -1;
			}
			if(o1 > o2){
				return 1;
			}
			return 0;
		}
	}
	public static final class FloatComparator implements Comparator<Float>{
		private static FloatComparator c= null;
		public static FloatComparator get(){
			if(c == null){
				synchronized(initSync){
					if(c == null){
						c= new FloatComparator();
					}
				}
			}
			return c;
		}
		@Override
		public int compare(Float o1, Float o2) {
			if(o1 < o2){
				return -1;
			}
			if(o1 > o2){
				return 1;
			}
			return 0;
		}
	}

	private static Comparator<File> fileComparer= null;
	public static void quickSortFileBySize(final File data[], final int left, final int right) {
		if(fileComparer == null){
			synchronized(initSync){
				if(fileComparer == null){
					fileComparer= new Comparator<File>(){

						@Override
						public int compare(File o1, File o2) {
							final long
								l1= o1.length(),
								l2= o2.length();
							if(l1 < l2){
								return -1;
							}
							if(l1 > l2){
								return 1;
							}
							return 0;
						}

					};
				}
			}
		}
		quickSort(data, fileComparer, left, right);
	}
//	========== end qsort===============
//	========== start msort=============
	public static void mergeSort(final int data[]) {
		/* passing this tmp space increases speed slightly
		 * (no allocation for all the sub arrays)
		 */
		final int[] tmp= new int[data.length];
		mergeSort(data, 0, data.length-1, tmp);
	}
	public static void mergeSort(final int data[], final int min, final int max, final int[] temp) {
		if(min < max) {
			final int mid= (min+max)/2;
			//sort left half recursively
			mergeSort(data, min, mid, temp);
			//sort right half recursively
			mergeSort(data, mid+1, max, temp);
			int left= min,
				right= mid+1,
				k;
			//int[] temp= new int [max-min+1];
			for (k= min; k<max; k++){
				if (left<=mid && (right>max || data[right]>data[left])) {
					temp[k]= data[left];
					left++;
				} else {
					temp[k]= data[right];
					right++;
				}
			}
			for(k= min; k<max; k++) {
				data[k]= temp[k];
			}
		}
	}
	public static void mergeSortFileBySize(final File data[]) {
		/* passing this tmp space increases speed slightly
		 * (no allocation for all the sub arrays)
		 */
		final File[] tmp= new File[data.length];
		mergeSortFileBySize(data, 0, data.length-1, tmp);
	}
	public static void mergeSortFileBySize(final File data[], final int min, final int max, final File[] temp) {
		if(min < max) {
			final int mid= (min+max)/2;
			//sort left half recursively
			mergeSortFileBySize(data, min, mid, temp);
			//sort right half recursively
			mergeSortFileBySize(data, mid+1, max, temp);
			int left= min,
				right= mid+1,
				k;
			//int[] temp= new int [max-min+1];
			for (k= min; k<max; k++){
				if (left<=mid && (right>max || data[right].length()>data[left].length())) {
					temp[k]= data[left];
					left++;
				} else {
					temp[k]= data[right];
					right++;
				}
			}
			for(k= min; k<max; k++) {
				data[k]= temp[k];
			}
		}
	}
//	========= end msort===============
	private do_sort(){}
}
