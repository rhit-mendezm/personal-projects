import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Random;

import org.junit.AfterClass;
import org.junit.Test;

/*
 * NOTE: These tests assume that BinaryHeap uses a generic array of type T and therefore 
 * has a constructor that takes a class type as a parameter.
 * 
 * @author Matt Boutell. Created May 12, 2013.
 * @author Nate Chenette. Edited Oct 22, 2019.
 */

public class AllBinaryHeapTest {
	private static int points = 0;

	@Test
	public void testInsertion() {
		BinaryHeap<Integer> heap = new BinaryHeap<Integer>(Integer.class);
		heap.insert(2);
		heap.insert(5);
		heap.insert(13);
		assertEquals("[null, 2, 5, 13]", heap.toString());
		heap.insert(29);
		heap.insert(7);
		heap.insert(67);
		assertEquals("[null, 2, 5, 13, 29, 7, 67]", heap.toString());
		heap.insert(11);
		heap.insert(11);
		heap.insert(19);
		heap.insert(5);
		assertEquals("[null, 2, 5, 11, 11, 5, 67, 13, 29, 19, 7]", heap.toString());

		points += 1;
	}

	@Test
	public void testBubbleRight() {
		BinaryHeap<Integer> heap = new BinaryHeap<Integer>(Integer.class);
		heap.insert(4);
		heap.insert(6);
		heap.insert(3);
		assertEquals("[null, 3, 6, 4]", heap.toString());

		heap.insert(18);
		assertEquals("[null, 3, 6, 4, 18]", heap.toString());
		heap.insert(2);
		heap.insert(1);
		heap.insert(0);

		assertEquals("[null, 0, 3, 1, 18, 6, 4, 2]", heap.toString());
		points += 1;
	}

	@Test
	public void testBubbleLeft() {
		BinaryHeap<Integer> heap = new BinaryHeap<Integer>(Integer.class);
		heap.insert(12);
		heap.insert(5);
		heap.insert(7);
		heap.insert(6);
		assertEquals("[null, 5, 6, 7, 12]", heap.toString());

		heap.insert(15);
		heap.insert(4);
		heap.insert(9);
		assertEquals("[null, 4, 6, 5, 12, 15, 7, 9]", heap.toString());
		points += 1;
	}

	/**
	 * Tests insert for the small already sorted array and that delete min works
	 * until all items are gone.
	 * 
	 */
	@Test
	public void testDeleteAllMinAlreadyOrdered() {
		BinaryHeap<Integer> heap = new BinaryHeap<Integer>(Integer.class);
		heap.insert(3);
		heap.insert(4);
		heap.insert(5);
		int min = heap.deleteMin();
		assertEquals(3, min);
		assertEquals("[null, 4, 5]", heap.toString());
		points += 1;

		int min2 = heap.deleteMin();
		assertEquals(4, min2);
		assertEquals("[null, 5]", heap.toString());
		points += 1;

		int min3 = heap.deleteMin();
		assertEquals(5, min3);
		points += 1;

		assertEquals("[null]", heap.toString());
		points += 1;
	}

	/**
	 * Tests delete min for a simple array that is out of order
	 * 
	 */
	@Test
	public void testDeleteAllMinSimpleOutofOrder() {
		BinaryHeap<Integer> heap = new BinaryHeap<Integer>(Integer.class);
		heap.insert(1);
		heap.insert(3);
		heap.insert(7);
		heap.insert(2);
		int min = heap.deleteMin();
		assertEquals(1, min);
		assertEquals("[null, 2, 3, 7]", heap.toString());
		points += 1;
	}

	/**
	 * Tests delete min for a slightly larger array that is out of order.
	 * 
	 */
	@Test
	public void testDeleteOutOfOrder() {
		BinaryHeap<Integer> heap = new BinaryHeap<Integer>(Integer.class);
		heap.insert(6);
		heap.insert(4);
		heap.insert(8);
		heap.insert(1);
		heap.insert(5);
		heap.insert(3);
		heap.insert(2);
		heap.insert(7);
		int min = heap.deleteMin();
		assertEquals(1, min);
		assertEquals("[null, 2, 4, 3, 6, 5, 8, 7]", heap.toString());
		points += 1;
	}

	/**
	 * Tests insert and delete all min for an array that is out of order.
	 * 
	 */
	@Test
	public void testInsertAndDeleteOutOfOrder2() {
		BinaryHeap<Integer> heap = new BinaryHeap<Integer>(Integer.class);
		heap.insert(1);
		heap.insert(5);
		heap.insert(4);
		heap.insert(7);
		heap.insert(2);
		heap.insert(3);
		assertEquals("[null, 1, 2, 3, 7, 5, 4]", heap.toString());
		int min = heap.deleteMin();
		assertEquals(1, min);
		assertEquals("[null, 2, 4, 3, 7, 5]", heap.toString());
		int min2 = heap.deleteMin();
		assertEquals(2, min2);
		assertEquals("[null, 3, 4, 5, 7]", heap.toString());
		int min3 = heap.deleteMin();
		assertEquals(3, min3);
		assertEquals("[null, 4, 7, 5]", heap.toString());
		int min4 = heap.deleteMin();
		assertEquals(4, min4);
		assertEquals("[null, 5, 7]", heap.toString());

		points += 1;
	}

	/**
	 * Tests delete all mins from the array example from class.
	 * 
	 */
	@Test
	public void testDeleteAllMins() {
		BinaryHeap<Integer> heap = new BinaryHeap<Integer>(Integer.class);
		heap.insert(6);
		heap.insert(4);
		heap.insert(8);
		heap.insert(1);
		heap.insert(5);
		heap.insert(3);
		heap.insert(2);
		heap.insert(7);
		int min = heap.deleteMin();
		assertEquals(1, min);
		assertEquals("[null, 2, 4, 3, 6, 5, 8, 7]", heap.toString());
		int min2 = heap.deleteMin();
		assertEquals(2, min2);
		assertEquals("[null, 3, 4, 7, 6, 5, 8]", heap.toString());
		int min3 = heap.deleteMin();
		assertEquals(3, min3);
		assertEquals("[null, 4, 5, 7, 6, 8]", heap.toString());
		int min4 = heap.deleteMin();
		assertEquals(4, min4);
		assertEquals("[null, 5, 6, 7, 8]", heap.toString());
		int min5 = heap.deleteMin();
		assertEquals(5, min5);
		assertEquals("[null, 6, 8, 7]", heap.toString());
		int min6 = heap.deleteMin();
		assertEquals(6, min6);
		assertEquals("[null, 7, 8]", heap.toString());
		int min7 = heap.deleteMin();
		assertEquals(7, min7);
		assertEquals("[null, 8]", heap.toString());
		int min8 = heap.deleteMin();
		assertEquals(8, min8);
		assertEquals("[null]", heap.toString());

		points += 1;
	}

	@Test
	public void testDelete1() {
		BinaryHeap<Integer> heap = new BinaryHeap<Integer>(Integer.class);
		heap.insert(2);
		heap.deleteMin();
		assertEquals("[null]", heap.toString());
		points += 1;

		heap.insert(2);
		heap.insert(5);
		heap.insert(13);
		assertEquals("[null, 2, 5, 13]", heap.toString());
		points += 1;

		heap.insert(29);
		heap.insert(7);
		heap.insert(67);
		assertEquals("[null, 2, 5, 13, 29, 7, 67]", heap.toString());
		points += 1;

		heap.deleteMin();
		heap.deleteMin();
		assertEquals(Integer.valueOf(7), heap.deleteMin());

		points += 1;
	}

	/**
	 * More in-depth tests for delete than simple cases
	 * 
	 */
	@Test
	public void testDeleteToNullLetters() {
		BinaryHeap<String> heapStr = new BinaryHeap<String>(String.class);
		heapStr.insert("A");
		heapStr.insert("B");
		heapStr.insert("C");
		heapStr.insert("D");
		heapStr.insert("E");
		heapStr.insert("F");
		heapStr.insert("G");
		heapStr.insert("H");
		heapStr.insert("I");
		assertEquals("A", heapStr.deleteMin());
		assertEquals("B", heapStr.deleteMin());
		assertEquals("C", heapStr.deleteMin());
		assertEquals("D", heapStr.deleteMin());
		assertEquals("E", heapStr.deleteMin());
		assertEquals("F", heapStr.deleteMin());
		assertEquals("G", heapStr.deleteMin());
		assertEquals("H", heapStr.deleteMin());
		assertEquals("I", heapStr.deleteMin());
		assertNull("Expected: null", heapStr.deleteMin());

		points += 1;

		heapStr = new BinaryHeap<String>(String.class);
		heapStr.insert("B");
		heapStr.insert("C");
		heapStr.insert("H");
		heapStr.insert("D");
		heapStr.insert("I");
		heapStr.insert("Z");
		heapStr.insert("G");
		heapStr.insert("E");
		heapStr.insert("A");
		assertEquals("A", heapStr.deleteMin());
		assertEquals("B", heapStr.deleteMin());
		assertEquals("C", heapStr.deleteMin());
		assertEquals("D", heapStr.deleteMin());
		assertEquals("E", heapStr.deleteMin());
		assertEquals("G", heapStr.deleteMin());
		assertEquals("H", heapStr.deleteMin());
		assertEquals("I", heapStr.deleteMin());
		assertEquals("Z", heapStr.deleteMin());
		assertNull("Expected: null", heapStr.deleteMin());

		points += 1;
	}

	@Test
	public void testDeleteToNullNumbers() {
		BinaryHeap<Integer> heap = new BinaryHeap<Integer>(Integer.class);
		heap.insert(10);
		heap.insert(9);
		heap.insert(8);
		heap.insert(7);
		heap.insert(6);
		heap.insert(5);
		heap.insert(4);
		heap.insert(3);
		heap.insert(2);
		heap.insert(1);

		assertEquals((Integer) 1, heap.deleteMin());
		assertEquals((Integer) 2, heap.deleteMin());
		assertEquals((Integer) 3, heap.deleteMin());
		assertEquals((Integer) 4, heap.deleteMin());
		assertEquals((Integer) 5, heap.deleteMin());
		assertEquals((Integer) 6, heap.deleteMin());
		assertEquals((Integer) 7, heap.deleteMin());
		assertEquals((Integer) 8, heap.deleteMin());
		assertEquals((Integer) 9, heap.deleteMin());
		assertEquals((Integer) 10, heap.deleteMin());
		assertNull("Expected: null", heap.deleteMin());

		points += 1;
	}

	@Test
	public void testMultipleDeletes() {
		BinaryHeap<Integer> heap = new BinaryHeap<Integer>(Integer.class);
		heap.insert(3);
		heap.insert(16);
		heap.insert(20);
		heap.insert(24);
		heap.insert(35);
		heap.insert(21);
		heap.insert(40);
		assertEquals("[null, 3, 16, 20, 24, 35, 21, 40]", heap.toString());

		points += 1;

		heap.deleteMin();
		assertEquals("[null, 16, 24, 20, 40, 35, 21]", heap.toString());

		points += 1;

		heap.deleteMin();
		heap.deleteMin();
		heap.deleteMin();
		assertEquals("[null, 24, 40, 35]", heap.toString());

		points += 1;
	}

	@Test
	public void testDeleteWithWords() {
		BinaryHeap<String> heap = new BinaryHeap<String>(String.class);
		heap.insert("ice");
		heap.insert("wont");
		heap.insert("hello");
		heap.insert("work");
		heap.insert("queen");
		assertEquals("[null, hello, queen, ice, work, wont]", heap.toString());

		points += 1;

		heap.deleteMin();
		assertEquals("[null, ice, queen, wont, work]", heap.toString());

		points += 1;

		heap.insert("act");
		heap.insert("race");
		heap.deleteMin();
		assertEquals("[null, ice, queen, race, work, wont]", heap.toString());

		points += 1;
	}

	/**
	 * Tests that call insert AND Delete.
	 * 
	 */
	@Test
	public void testInsertAndDelete() {
		BinaryHeap<String> heapStr = new BinaryHeap<String>(String.class);
		assertEquals("[null]", heapStr.toString());
		heapStr.insert("A");
		assertEquals("[null, A]", heapStr.toString());
		heapStr.deleteMin();
		heapStr.insert("B");
		assertEquals("[null, B]", heapStr.toString());
		heapStr.insert("C");
		assertEquals("[null, B, C]", heapStr.toString());
		heapStr.insert("D");
		assertEquals("[null, B, C, D]", heapStr.toString());
		heapStr.deleteMin();
		heapStr.insert("E");
		assertEquals("[null, C, D, E]", heapStr.toString());
		heapStr.insert("F");
		heapStr.deleteMin();
		assertEquals("[null, D, F, E]", heapStr.toString());
		heapStr.insert("G");
		assertEquals("[null, D, F, E, G]", heapStr.toString());
		heapStr.insert("H");
		assertEquals("[null, D, F, E, G, H]", heapStr.toString());
		heapStr.insert("I");
		heapStr.deleteMin();
		assertEquals("[null, E, F, I, G, H]", heapStr.toString());
		heapStr.deleteMin();
		assertEquals("[null, F, G, I, H]", heapStr.toString());
		heapStr.deleteMin();
		heapStr.deleteMin();
		heapStr.deleteMin();
		heapStr.deleteMin();
		assertEquals("[null]", heapStr.toString());
		heapStr.deleteMin();
		assertEquals("[null]", heapStr.toString());

		points += 1;
	}

	/**
	 * Makes a list of random numbers, and checks if its sorted.
	 * 
	 */
	@Test
	public void testRandomNumbersSort() {
		for (int j = 1; j < 100; j++) {
			Integer[] numbers = new Integer[10 * j];
			for (int i = 0; i < 10 * j; i++) {
				numbers[i] = (int) (10 * j * Math.random());
			}

			Integer[] sortedNumbers = numbers.clone();
			Arrays.sort(sortedNumbers);
			BinaryHeap.sort(numbers, Integer.class);
			String definitelySorted = new String();
			for (Integer i : sortedNumbers) {
				definitelySorted = definitelySorted + i + ", ";
			}
			String hopefullySorted = new String();
			for (Integer i : numbers) {
				hopefullySorted = hopefullySorted + i + ", ";
			}
			assertEquals(definitelySorted, hopefullySorted);
		}

		points += 1;
	}

	/**
	 * Sorts a pre-sorted list
	 * 
	 */
	@Test
	public void testInOrderSort() {
		Integer[] numbers = new Integer[50];
		for (int i = 1; i <= 50; i++) {
			numbers[i - 1] = i;
		}
		Integer[] sortedNumbers = numbers.clone();
		String definitelySorted = new String();
		for (Integer i : sortedNumbers) {
			definitelySorted = definitelySorted + i + ", ";
		}
		BinaryHeap.sort(numbers, Integer.class);
		String hopefullySorted = new String();
		for (Integer i : numbers) {
			hopefullySorted = hopefullySorted + i + ", ";
		}
		assertEquals(definitelySorted, hopefullySorted);

		points += 1;
	}

	/**
	 * Test the sort method with integer and string array.
	 * 
	 */
	@Test
	public void testSort() {
		Integer[] intArray = new Integer[] { 87, 46, 82, 61, 46, 29, 19, 62, 22, 4, 7, 67, 75, 78, 89, 54, 15, 90, 86,
				44, 16, 10, 6, 71, 99, 21, 83, 64, 44, 57, 33, 23, 98, 13, 81, 75, 71, 39, 32, 50, 37, 42, 31, 73, 25,
				12, 28, 12, 69, 11, 24, 58, 84, 50, 41, 96, 37, 95, 4, 24, 37, 43, 96, 4, 99, 69, 81, 5, 71, 1, 50, 84,
				72, 98, 42, 60, 78, 23, 19, 14, 98, 95, 11, 69, 25, 87, 36, 96, 73, 29, 78, 95, 15, 24, 99, 31, 42, 3,
				82, 63, 26 };

		BinaryHeap.sort(intArray, Integer.class);

		for (int i = 0; i < intArray.length - 1; i++) {
			assertTrue("Expected: true", intArray[i].compareTo(intArray[i + 1]) <= 0);
		}
		points += 1;

		String[] strArray = new String[] { "in", "the", "second", "step", "a", "sorted", "array", "is", "created", "by",
				"repeatedly", "removing", "the", "largest", "element", "from", "the", "heap", "and", "inserting", "it",
				"into", "the", "array", "the", "heap", "is", "reconstructed", "after", "each", "removal", "once", "all",
				"objects", "have", "been", "removed", "from", "the", "heap", "we", "have", "a", "sorted", "array",
				"the", "direction", "of", "the", "sorted", "elements", "can", "be", "varied", "by", "choosing", "a",
				"min", "heap", "or", "max", "heap", "in", "step", "one" };

		BinaryHeap.sort(strArray, String.class);

		for (int i = 0; i < strArray.length - 1; i++) {
			assertTrue("Expected: true", strArray[i].compareTo(strArray[i + 1]) <= 0);
		}

		points += 1;
	}

	/**
	 * More in-depth tests for sort than simple cases
	 * 
	 */
	@Test
	public void testSort2() {
		String[] backwardsAlpha = new String[] { "f", "e", "d", "c", "b", "a" };
		BinaryHeap.sort(backwardsAlpha, String.class);
		assertEquals("[a, b, c, d, e, f]", Arrays.toString(backwardsAlpha));
		points += 1;

		String[] randomAlpha = new String[] { "d", "c", "f", "a", "e", "b" };
		BinaryHeap.sort(randomAlpha, String.class);
		assertEquals("[a, b, c, d, e, f]", Arrays.toString(randomAlpha));
		points += 1;

		String[] Alpha = new String[] { "a", "b", "c", "d", "e", "f" };
		BinaryHeap.sort(Alpha, String.class);
		assertEquals("[a, b, c, d, e, f]", Arrays.toString(Alpha));
		points += 1;

		Integer[] integers = new Integer[] { 1, 2, 3, 4, 5, 6 };
		BinaryHeap.sort(integers, Integer.class);
		assertEquals("[1, 2, 3, 4, 5, 6]", Arrays.toString(integers));
		points += 1;

		integers = new Integer[] { 6, 5, 4, 3, 2, 1 };
		BinaryHeap.sort(integers, Integer.class);
		assertEquals("[1, 2, 3, 4, 5, 6]", Arrays.toString(integers));

		points += 1;

		integers = new Integer[] { 5, 2, 6, 4, 1, 3 };
		BinaryHeap.sort(integers, Integer.class);
		assertEquals("[1, 2, 3, 4, 5, 6]", Arrays.toString(integers));

		points += 1;
	}

	/**
	 * Sorts a descending list
	 * 
	 */
	@Test
	public void testReverseSort() {
		Integer[] numbers = new Integer[50];
		for (int i = 1; i <= 50; i++) {
			numbers[i - 1] = 51 - i;
		}
		Integer[] sortedNumbers = numbers.clone();
		Arrays.sort(sortedNumbers);
		String definitelySorted = new String();
		for (Integer i : sortedNumbers) {
			definitelySorted = definitelySorted + i + ", ";
		}
		BinaryHeap.sort(numbers, Integer.class);
		String hopefullySorted = new String();
		for (Integer i : numbers) {
			hopefullySorted = hopefullySorted + i + ", ";
		}
		assertEquals(definitelySorted, hopefullySorted);

		points += 1;
	}

	/**
	 * Tests sort for a simple array that is out of order on insertion.
	 * 
	 */
	@Test
	public void testSimpleOutOfOrderInsert() {
		BinaryHeap<Integer> heap = new BinaryHeap<Integer>(Integer.class);
		heap.insert(1);
		heap.insert(3);
		heap.insert(7);
		heap.insert(2);
		assertEquals("[null, 1, 2, 7, 3]", heap.toString());

		points += 1;
	}

	/**
	 * Tests sort for the array example from class.
	 * 
	 */
	@Test
	public void testOutOfOrderInsert() {
		BinaryHeap<Integer> heap = new BinaryHeap<Integer>(Integer.class);
		heap.insert(6);
		heap.insert(4);
		heap.insert(8);
		heap.insert(1);
		heap.insert(5);
		heap.insert(3);
		heap.insert(2);
		heap.insert(7);
		assertEquals("[null, 1, 4, 2, 6, 5, 8, 3, 7]", heap.toString());

		points += 1;
	}

	/**
	 * Tests the sort for a larger array.
	 * 
	 */
	@Test
	public void testBiggerSort() {
		Integer[] nums = new Integer[] { 1, 4, 6, 7, 8, 9, 0, 22, 5 };
		BinaryHeap.sort(nums, Integer.class);
		assertEquals("[0, 1, 4, 5, 6, 7, 8, 9, 22]", Arrays.toString(nums));

		points += 1;
	}

	@Test
	public void sortRandom1000000() {
		Integer[] arr3 = new Integer[1000000];
		Random rand = new Random();
		for (int i = 0; i < 1000000; i++) {
			arr3[i] = rand.nextInt(Integer.MAX_VALUE);
		}
		BinaryHeap.sort(arr3, Integer.class);
		assertTrue(verifySort(arr3));

		points += 1;
	}

	@AfterClass
	public static void testNothing() {
		System.out.println("Points: " + points * .75 + " of 30");
	}

	/**
	 * Checks in O(n) time if this array is sorted.
	 * 
	 * @param a An array to check to see if it is sorted.
	 */
	private static boolean verifySort(Integer[] a) {
		for (int i = 1; i < a.length; i++) {
			if (a[i - 1] > a[i]) {
				return false;
			}
		}
		return true;
	}

}