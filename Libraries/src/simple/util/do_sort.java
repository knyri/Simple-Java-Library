package simple.util;

import java.io.File;

/**
 * <br>Created: Feb 13, 2008
 * @author Kenneth Pierce
 */
public final class do_sort {
	/*public static void main (String[] args) {
		int size = 50;
		int[] data = new int[size];
		for (int i = 0; i<size;i++)
			data[i] = (int)(Math.random()*100);
		insertionSort(data);
		do_array.print(data);
	}*/
	public static void swap(final int data[], final int e0, final int e1) {
		final int tmp = data[e0];
		data[e0] = data[e1];
		data[e1] = tmp;
	}
	public static void swap(final short data[], final int e0, final int e1) {
		final short tmp = data[e0];
		data[e0] = data[e1];
		data[e1] = tmp;
	}
	public static void swap(final long data[], final int e0, final int e1) {
		final long tmp = data[e0];
		data[e0] = data[e1];
		data[e1] = tmp;
	}
	public static void swap(final double data[], final int e0, final int e1) {
		final double tmp = data[e0];
		data[e0] = data[e1];
		data[e1] = tmp;
	}
	public static void swap(final float data[], final int e0, final int e1) {
		final float tmp = data[e0];
		data[e0] = data[e1];
		data[e1] = tmp;
	}
	public static void swap(final Object data[], final int e0, final int e1) {
		final Object tmp = data[e0];
		data[e0] = data[e1];
		data[e1] = tmp;
	}
	public static void selectionSort(final int data[]) {
		int min, scan;
		for(int j=0; j<data.length-1; j++) {
			min=j;
			for(scan=j+1; scan<data.length; scan++)
				if(data[scan] < data[min])
					min=scan;
			// Swap
			swap(data, min, j);
		}
	}

	public static void insertionSort(final int data[]) {
		/* Start at 1st element and shift elements down
		 * until a value lower than key is found and
		 * shift that one doen until a lower is found
		 * and shit that one down...
		 */
		int key, pos;
		for(int j=1; j<data.length; j++) {
			key=data[j];
			pos=j;
			// shifting
			while(pos>0 && data[pos-1] > key) {
				data[pos]=data[pos-1];
				pos--;
			}
			data[pos]=key;
		}
	}

	public static void bubbleSort(final int data[]) {
		/* Start from end and move to start.
		 * Start from start and move to the end
		 * moving the largest element down as we go.
		 */
		for (int i = data.length-1; i>0; i--) {
			for(int j = 0; j<i; j++) {
				if (data[j] > data[j+1]) {
					swap(data, j+1, j);
				}
			}
		}
	}
	public static void bubbleSort(final long data[]) {
		/* Start from end and move to start.
		 * Start from start and move to the end
		 * moving the largest element down as we go.
		 */
		for (int i = data.length-1; i>0; i--) {
			for(int j = 0; j<i; j++) {
				if (data[j] > data[j+1]) {
					swap(data, j+1, j);
				}
			}
		}
	}
	public static void bubbleSort(final short data[]) {
		/* Start from end and move to start.
		 * Start from start and move to the end
		 * moving the largest element down as we go.
		 */
		for (int i = data.length-1; i>0; i--) {
			for(int j = 0; j<i; j++) {
				if (data[j] > data[j+1]) {
					swap(data, j+1, j);
				}
			}
		}
	}
	public static void bubbleSort(final double data[]) {
		/* Start from end and move to start.
		 * Start from start and move to the end
		 * moving the largest element down as we go.
		 */
		for (int i = data.length-1; i>0; i--) {
			for(int j = 0; j<i; j++) {
				if (data[j] > data[j+1]) {
					swap(data, j+1, j);
				}
			}
		}
	}
	public static void bubbleSort(final float data[]) {
		/* Start from end and move to start.
		 * Start from start and move to the end
		 * moving the largest element down as we go.
		 */
		for (int i = data.length-1; i>0; i--) {
			for(int j = 0; j<i; j++) {
				if (data[j] > data[j+1]) {
					swap(data, j+1, j);
				}
			}
		}
	}
	public static void bubbleSort(final String data[]) {
		/* Start from end and move to start.
		 * Start from start and move to the end
		 * moving the largest element down as we go.
		 */
		for (int i = data.length-1; i>0; i--) {
			for(int j = 0; j<i; j++) {
				if (data[j].compareTo(data[j+1])>0) {
					swap(data, j+1, j);
				}
			}
		}
	}
//	============ start qsort ===========
	public static void quickSort(final int data[]) {
		quickSort(data, 0, data.length-1);
	}
	public static void quickSort(final long data[]) {
		quickSort(data, 0, data.length-1);
	}
	public static void quickSort(final short data[]) {
		quickSort(data, 0, data.length-1);
	}
	public static void quickSort(final double data[]) {
		quickSort(data, 0, data.length-1);
	}
	public static void quickSort(final float data[]) {
		quickSort(data, 0, data.length-1);
	}
	public static void quickSort(final String data[]) {
		quickSort(data, 0, data.length-1);
	}
	public static void quickSort(final int data[], final int left, final int right) {
		//System.out.println("Qsort: "+qsort++);
		if(left<right) {
			int l = left,
				r = right;
			final int mid = data[(left + right)/2];
			// Keep going until we've hit the middle
			while(l<=r) {
				while(data[l] < mid && l<right)
					++l;
				while(data[r] > mid && r>left)
					--r;
				if(l<=r) {
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
			if (left < r)
				quickSort(data, left, r);

			// quick sort right part if l has not reached the right end
			if (l<right)
				quickSort(data, l, right);
		}
	}
	public static void quickSort(final long data[], final int left, final int right) {
		//System.out.println("Qsort: "+qsort++);
		if(left<right) {
			int l = left,
				r = right;
			final long mid = data[(left + right)/2];
			// Keep going until we've hit the middle
			while(l<=r) {
				while(data[l] < mid && l<right)
					++l;
				while(data[r] > mid && r>left)
					--r;
				if(l<=r) {
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
			if (left < r)
				quickSort(data, left, r);

			// quick sort right part if l has not reached the right end
			if (l<right)
				quickSort(data, l, right);
		}
	}
	public static void quickSort(final short data[], final int left, final int right) {
		//System.out.println("Qsort: "+qsort++);
		if(left<right) {
			int l = left,
				r = right;
			final short mid = data[(left + right)/2];
			// Keep going until we've hit the middle
			while(l<=r) {
				while(data[l] < mid && l<right)
					++l;
				while(data[r] > mid && r>left)
					--r;
				if(l<=r) {
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
			if (left < r)
				quickSort(data, left, r);

			// quick sort right part if l has not reached the right end
			if (l<right)
				quickSort(data, l, right);
		}
	}
	public static void quickSort(final float data[], final int left, final int right) {
		//System.out.println("Qsort: "+qsort++);
		if(left<right) {
			int l = left,
				r = right;
			final float mid = data[(left + right)/2];
			// Keep going until we've hit the middle
			while(l<=r) {
				while(data[l] < mid && l<right)
					++l;
				while(data[r] > mid && r>left)
					--r;
				if(l<=r) {
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
			if (left < r)
				quickSort(data, left, r);

			// quick sort right part if l has not reached the right end
			if (l<right)
				quickSort(data, l, right);
		}
	}
	public static void quickSort(final double data[], final int left, final int right) {
		//System.out.println("Qsort: "+qsort++);
		if(left<right) {
			int l = left,
				r = right;
			final double mid = data[(left + right)/2];
			// Keep going until we've hit the middle
			while(l<=r) {
				while(data[l] < mid && l<right)
					++l;
				while(data[r] > mid && r>left)
					--r;
				if(l<=r) {
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
			if (left < r)
				quickSort(data, left, r);

			// quick sort right part if l has not reached the right end
			if (l<right)
				quickSort(data, l, right);
		}
	}
	public static void quickSort(final String data[], final int left, final int right) {
		//System.out.println("Qsort: "+qsort++);
		if(left<right) {
			int l = left,
				r = right;
			final String mid = data[(left + right)/2];
			// Keep going until we've hit the middle
			while(l<=r) {
				while(data[l].compareTo(mid)<0 && l<right)
					++l;
				while(data[r].compareTo(mid)>0 && r>left)
					--r;
				if(l<=r) {
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
			if (left < r)
				quickSort(data, left, r);

			// quick sort right part if l has not reached the right end
			if (l<right)
				quickSort(data, l, right);
		}
	}
	public static void quickSortFileBySize(final File data[], final int left, final int right) {
		//System.out.println("Qsort: "+qsort++);
		if(left<right) {
			int l = left,
				r = right;
			final File mid = data[(left + right)/2];
			// Keep going until we've hit the middle
			while(l<=r) {
				while(data[l].length() < mid.length() && l<right)
					++l;
				while(data[r].length() > mid.length() && r>left)
					--r;
				if(l<=r) {
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
			if (left < r)
				quickSortFileBySize(data, left, r);

			// quick sort right part if l has not reached the right end
			if (l<right)
				quickSortFileBySize(data, l, right);
		}
	}
//	========== end qsort ===============
//	========== start msort =============
	public static void mergeSort(final int data[]) {
		/* passing this tmp space increases speed slightly
		 * (no allocation for all the sub arrays)
		 */
		final int[] tmp = new int[data.length];
		mergeSort(data, 0, data.length-1, tmp);
	}
	public static void mergeSort(final int data[], final int min, final int max, final int[] temp) {
		if(min < max) {
			final int mid = (min+max)/2;
			//sort left half recursively
			mergeSort(data, min, mid, temp);
			//sort right half recursively
			mergeSort(data, mid+1, max, temp);
			int left = min,
				right= mid+1,
				k;
			//int[] temp = new int [max-min+1];
			for (k = min; k<max; k++){
				if (left<=mid && (right>max || data[right]>data[left])) {
					temp[k] = data[left];
					left++;
				} else {
					temp[k] = data[right];
					right++;
				}
			}
			for(k = min; k<max; k++) {
				data[k] = temp[k];
			}
		}
	}
	public static void mergeSortFileBySize(final File data[]) {
		/* passing this tmp space increases speed slightly
		 * (no allocation for all the sub arrays)
		 */
		final File[] tmp = new File[data.length];
		mergeSortFileBySize(data, 0, data.length-1, tmp);
	}
	public static void mergeSortFileBySize(final File data[], final int min, final int max, final File[] temp) {
		if(min < max) {
			final int mid = (min+max)/2;
			//sort left half recursively
			mergeSortFileBySize(data, min, mid, temp);
			//sort right half recursively
			mergeSortFileBySize(data, mid+1, max, temp);
			int left = min,
				right= mid+1,
				k;
			//int[] temp = new int [max-min+1];
			for (k = min; k<max; k++){
				if (left<=mid && (right>max || data[right].length()>data[left].length())) {
					temp[k] = data[left];
					left++;
				} else {
					temp[k] = data[right];
					right++;
				}
			}
			for(k = min; k<max; k++) {
				data[k] = temp[k];
			}
		}
	}
//	========= end msort ===============
}
