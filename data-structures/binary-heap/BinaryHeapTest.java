import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

/**
 * Tests binary heaps.
 * 
 * @author Matt Boutell. Created May 7, 2013.
 * @author Nate Chenette. Edited 2019, 2020.
 */
public class BinaryHeapTest {

	/**
	 * Simple test method for insert, delete, toString, and sort. Enforces the
	 * method signatures.
	 */
	@Test
	public void testSimple() {
		BinaryHeap<Integer> heap = new BinaryHeap<Integer>(Integer.class);

		// deleteMin returns null if it has no elements.
		assertNull(heap.deleteMin());
		heap.insert(21);
		assertEquals("[null, 21]", heap.toString());
		assertEquals(Integer.valueOf(21), heap.deleteMin());
		heap.insert(30);
		heap.insert(15);
		heap.insert(45);
		assertEquals(Integer.valueOf(15), heap.deleteMin());
		assertEquals(Integer.valueOf(30), heap.deleteMin());
		assertEquals(Integer.valueOf(45), heap.deleteMin());
		assertEquals("[null]", heap.toString());

		String[] csLegends = new String[] { "Edsger Dijkstra", "Grace Hopper", "Donald Knuth", "Ada Lovelace",
				"Claude Shannon", "Alan Turing" };
		BinaryHeap.sort(csLegends, String.class);
		assertEquals("[Ada Lovelace, Alan Turing, Claude Shannon, Donald Knuth, Edsger Dijkstra, Grace Hopper]",
				Arrays.toString(csLegends));
	} // testSimple

	/**
	 * Test method for insert, deleteMin, toString, and sort on Integers.
	 */
	@Test
	public void testIntegers() {
		BinaryHeap<Integer> heap = new BinaryHeap<Integer>(Integer.class);

		assertNull(heap.deleteMin());
		heap.insert(4);
		assertEquals("[null, 4]", heap.toString());
		assertEquals(Integer.valueOf(4), heap.deleteMin());
		heap.insert(8);
		heap.insert(12);
		heap.insert(400);
		heap.insert(1);
		assertEquals(Integer.valueOf(1), heap.deleteMin());
		assertEquals(Integer.valueOf(8), heap.deleteMin());
		assertEquals(Integer.valueOf(12), heap.deleteMin());
		assertEquals(Integer.valueOf(400), heap.deleteMin());
		assertEquals("[null]", heap.toString());

		Integer[] ints = new Integer[] { 4, 5, 2, 3, 1, 0, 9 };
		BinaryHeap.sort(ints, Integer.class);
		assertEquals("[0, 1, 2, 3, 4, 5, 9]", Arrays.toString(ints));
	} // testIntegers

	/**
	 * Test method for insert, deleteMin, toString, and sort on Characters.
	 */
	@Test
	public void testCharacters() {
		BinaryHeap<Character> heap = new BinaryHeap<Character>(Character.class);

		assertNull(heap.deleteMin());
		heap.insert('b');
		assertEquals("[null, b]", heap.toString());
		assertEquals(Character.valueOf('b'), heap.deleteMin());
		heap.insert('a');
		heap.insert('c');
		heap.insert('x');
		heap.insert('y');
		heap.insert('z');
		assertEquals(Character.valueOf('a'), heap.deleteMin());
		assertEquals(Character.valueOf('c'), heap.deleteMin());
		assertEquals(Character.valueOf('x'), heap.deleteMin());
		assertEquals(Character.valueOf('y'), heap.deleteMin());
		assertEquals(Character.valueOf('z'), heap.deleteMin());

		Character[] chars = new Character[] { 'y', 'o', 'e', 'a', 'i', 'u' };
		BinaryHeap.sort(chars, Character.class);
		assertEquals("[a, e, i, o, u, y]", Arrays.toString(chars));
	} // testCharacters

	/**
	 * Test method for insert, deleteMin, toString, and sort on Integers with
	 * duplicates in the heap.
	 */
	@Test
	public void testIntegersDuplicates() {
		BinaryHeap<Integer> heap = new BinaryHeap<Integer>(Integer.class);

		assertNull(heap.deleteMin());
		heap.insert(30);
		assertEquals("[null, 30]", heap.toString());
		heap.insert(30); // first duplicate
		assertEquals("[null, 30, 30]", heap.toString());
		heap.insert(300);
		heap.insert(20);
		heap.insert(5);
		heap.insert(0);
		heap.insert(5); // second duplicate
		assertEquals(Integer.valueOf(0), heap.deleteMin());
		assertEquals(Integer.valueOf(5), heap.deleteMin());
		assertEquals(Integer.valueOf(5), heap.deleteMin());
		assertEquals(Integer.valueOf(20), heap.deleteMin());
		assertEquals(Integer.valueOf(30), heap.deleteMin());
		assertEquals(Integer.valueOf(30), heap.deleteMin());
		assertEquals(Integer.valueOf(300), heap.deleteMin());
		assertEquals("[null]", heap.toString());

		// two duplicates: 33 and 91
		Integer[] ints = new Integer[] { 33, 21, 45, 33, 5, 90, 91, 98, 70, 91 };
		BinaryHeap.sort(ints, Integer.class);
		assertEquals("[5, 21, 33, 33, 45, 70, 90, 91, 91, 98]", Arrays.toString(ints));
	} // testDuplicates

} // BinaryHeapTest
