import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

/**
 * This program runs various sorts and gathers timing information on them.
 *
 * @author Marlon Mendez-Yanez <br>
 *         Created May 7, 2013.
 */
public class SortRunner {
	private static Random rand = new Random(17); // uses a fixed seed for debugging. Remove the parameter later.

	/**
	 * Starts here.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		int size = (int) Math.pow(10, 6);

		/**
		 * Each integer will have the range from [0, maxValue). If this is significantly
		 * higher than size, you will have small likelihood of getting duplicates.
		 */
		int maxValue = Integer.MAX_VALUE;

		// Test 1: Array of random values.
		int[] randomIntArray = getRandomIntArray(size, maxValue);
		runAllSortsForOneArray(randomIntArray);

		/**
		 * Tests 2-4 Generate the three other types of arrays (shuffled, almost sorted,
		 * almost reverse sorted) and run the sorts on those as well.
		 */

		// Test 2: Array of shuffled unique values.
		int[] shuffled = getUniqueElementArray(size);
		runAllSortsForOneArray(shuffled);

		// Test 3: Array of almost sorted values.
		int[] almostSorted = getRandomIntArray(size, maxValue);
		Arrays.sort(almostSorted);
		getAlmostSortedArray(almostSorted);
		runAllSortsForOneArray(almostSorted);

		// Test 4: Array of almost reverse sorted values.
		int[] reverseAlmostSorted = new int[size];
		for (int i = 0; i < size; i++)
			reverseAlmostSorted[i] = almostSorted[size - 1 - i];

		runAllSortsForOneArray(reverseAlmostSorted);
	}

	/**
	 * Runs all the specified sorts on the given array and outputs timing results on
	 * each.
	 *
	 * @param array
	 */
	private static void runAllSortsForOneArray(int[] array) {
		long startTime, elapsedTime;
		boolean isSorted = false;

		int[] sortedIntsUsingDefaultSort = array.clone();
		Integer[] sortedIntegersUsingDefaultSort = copyToIntegerArray(array);
		Integer[] sortedIntegersUsingHeapSort = sortedIntegersUsingDefaultSort.clone();
		Integer[] sortedIntegersUsingTreeSort = sortedIntegersUsingDefaultSort.clone();
		int[] sortedIntsUsingQuickSort = array.clone();

		int size = array.length;

		/**
		 * Q: What is the default sort for ints? Read the javadoc.
		 * 
		 * A: Dual-pivot sort is default for ints.
		 */
		startTime = System.currentTimeMillis();
		Arrays.sort(sortedIntsUsingDefaultSort);
		elapsedTime = (System.currentTimeMillis() - startTime);
		isSorted = verifySort(sortedIntsUsingDefaultSort);
		displayResults("int", "Dual-Pivot sort", elapsedTime, size, isSorted);

		/**
		 * Q: What is the default sort for Integers (which are objects that wrap ints)?
		 * 
		 * A: Merge sort is default for Integers.
		 */
		startTime = System.currentTimeMillis();
		Arrays.sort(sortedIntegersUsingDefaultSort);
		elapsedTime = (System.currentTimeMillis() - startTime);
		isSorted = verifySort(sortedIntegersUsingDefaultSort);
		displayResults("Integer", "Mergesort", elapsedTime, size, isSorted);

		/**
		 * Sort using the following methods, and time and verify each like done above.
		 */

		// TreeSet sort
		startTime = System.currentTimeMillis();
		sortUsingTreeMap(sortedIntegersUsingTreeSort);
		elapsedTime = (System.currentTimeMillis() - startTime);
		isSorted = verifySort(sortedIntegersUsingTreeSort);
		displayResults("Integer", "TreeSet sort", elapsedTime, size, isSorted);

		// Quicksort
		startTime = System.currentTimeMillis();
		Quicksort.quicksort(sortedIntsUsingQuickSort, 0, size - 1);
		elapsedTime = (System.currentTimeMillis() - startTime);
		isSorted = verifySort(sortedIntsUsingQuickSort);
		displayResults("int", "Quicksort", elapsedTime, size, isSorted);

		// Heap sort
		startTime = System.currentTimeMillis();
		BinaryHeap.sort(sortedIntegersUsingHeapSort, Integer.class);
		elapsedTime = (System.currentTimeMillis() - startTime);
		isSorted = verifySort(sortedIntegersUsingHeapSort);
		displayResults("Integer", "Heap sort", elapsedTime, size, isSorted);
	}

	private static void displayResults(String typeName, String sortName, long elapsedTime, int size, boolean isSorted) {
		if (isSorted)
			System.out.printf("Sorted %.1e %ss using %s in %d milliseconds\n", (double) size, typeName, sortName,
					elapsedTime);
		else
			System.out.println("ARRAY NOT SORTED");

	}

	/**
	 * Checks in O(n) time if this array is sorted.
	 *
	 * @param a An array to check to see if it is sorted.
	 */
	private static boolean verifySort(int[] a) {
		for (int i = 1; i < a.length; i++)
			if (a[i] < a[i - 1])
				return false;

		return true;
	}

	/**
	 * Checks in O(n) time if this array is sorted.
	 *
	 * @param a An array to check to see if it is sorted.
	 */
	private static boolean verifySort(Integer[] a) {
		for (int i = 1; i < a.length; i++)
			if (a[i].compareTo(a[i - 1]) < 0)
				return false;

		return true;
	}

	/**
	 * Copies from an int array to an Integer array.
	 *
	 * @param randomIntArray
	 * @return A clone of the primitive int array, but with Integer objects.
	 */
	private static Integer[] copyToIntegerArray(int[] ints) {
		Integer[] integers = new Integer[ints.length];
		for (int i = 0; i < ints.length; i++)
			integers[i] = ints[i];

		return integers;
	}

	/**
	 * Creates and returns an array of random ints of the given size.
	 *
	 * @return An array of random ints.
	 */
	private static int[] getRandomIntArray(int size, int maxValue) {
		int[] a = new int[size];
		for (int i = 0; i < size; i++)
			a[i] = rand.nextInt(maxValue);

		return a;
	}

	/**
	 * Creates a shuffled random array.
	 *
	 * @param size
	 * @return An array of the ints from 0 to size-1, all shuffled
	 */
	private static int[] getUniqueElementArray(int size) {
		ArrayList<Integer> toShuffle = new ArrayList<>();
		for (int i = 0; i < size; i++)
			toShuffle.add(i);

		Collections.shuffle(toShuffle);

		int[] shuffled = new int[size];
		for (int i = 0; i < size; i++)
			shuffled[i] = toShuffle.get(i);

		return shuffled;
	}

	/**
	 * Creates an almost sorted array. Performs 0.01*n swaps on sorted in random
	 * positions to slightly shuffle sorted where n is the length of sorted.
	 * 
	 * @param sorted
	 */
	private static void getAlmostSortedArray(int[] sorted) {
		int length = sorted.length;
		int numSwaps = (int) (length * 0.01);

		for (int i = 0; i < numSwaps; i++) {
			int randomFromPosition = rand.nextInt(0, length);
			int randomToPosition = rand.nextInt(0, length);

			int temp = sorted[randomFromPosition];
			sorted[randomFromPosition] = sorted[randomToPosition];
			sorted[randomToPosition] = temp;
		}
	}

	/**
	 * Sorts the array using a TreeMap.
	 * 
	 * @param array
	 */
	private static void sortUsingTreeMap(Integer[] array) {
		TreeMap<Integer, Integer> map = new TreeMap<>();
		int length = array.length;

		for (int i = 0; i < length; i++) {
			int count = 1;
			int current = array[i];

			if (map.containsKey(current))
				count = map.get(current) + 1;

			map.put(current, count);
		}

		Set<Integer> inOrderKeys = map.keySet();
		int j = 0;
		for (Integer current : inOrderKeys) {
			int repeats = map.get(current);
			for (int k = 0; k < repeats; k++) {
				array[j] = current;
				j++;
			}
		}
	}

}
