package editortrees;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.AfterClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Complete test cases for Milestone 1
 * 
 * Test cases are scored out of a possible 50 points
 * 
 * @author Jimmy Theis
 * 
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EditTreeMilestone1Test {
	// I numbered the unit tests in this file. Fixing the method order forces the
	// unit tests to run in this numerical order, which is roughly the suggested
	// order of implementing things.

	private static double m1points = 0;
	private static final int MAX_MILESTONE1 = 25;
	private static final int MAX_DESIRED_MILESTONE1 = 50;
	private static double m1weight = (double) MAX_DESIRED_MILESTONE1 / MAX_MILESTONE1;

	// Premature optimization isn't good.
	// Tests named with "Simple" are there to give some immediate feedback.
	// They don't require any balancing so don't use balance code or rotations
	//
	// More intense tests that require rotations and balancing are in
	// milestone 2, where you will optimize add().
	// (and efficiency is worth a TON of points on milestones 2 and 3).

	@Test
	public void test101EmptySimple() {
		EditTree t = new EditTree();
		assertEquals("", t.toString());
		m1points += m1weight;
	}

	@Test
	public void test102OneCharacterConstructorSimple() {
		EditTree t = new EditTree('x');
		assertEquals("x", t.toString());
		m1points += m1weight;
	}

	@Test
	public void test103AppendToBeNewRootSimple() {
		EditTree t = new EditTree();
		t.add('c');
		assertEquals("c", t.toString());
		m1points += m1weight;
	}

	@Test
	public void test104AppendNoRotationsNeededSimple() {
		EditTree t = new EditTree();
		t.add('c');
		t.add('d');
		assertEquals("cd", t.toString());
		m1points += m1weight;
	}

	@Test
	public void test105AppendManyUsingPositionOnlySimple() {
	    EditTree t = new EditTree(); 
	    t.add('j');
	    t.add('i');
	    t.add('h');
	    t.add('g');
	    t.add('f');
	    t.add('e');
	    t.add('d');
	    t.add('c');
	    t.add('b');
	    t.add('a');
	    assertEquals("jihgfedcba", t.toString());
	    m1points += m1weight;
	}

	@Test
	public void test106AddNoRotationsNeededSimple() {
		EditTree t = new EditTree();
		t.add('b', 0);
		t.add('a', 0);
		t.add('c', 2);
		assertEquals("abc", t.toString());
		m1points += m1weight;
	}

	@Test
	public void test107AddNoRotationsNeededSimple2() {
		EditTree t = new EditTree();
		t.add('d', 0);
		t.add('b', 0);
		t.add('f', 2);
		t.add('a', 0);
		t.add('c', 2);
		t.add('e', 4);
		t.add('g', 6);
		assertEquals("abcdefg", t.toString());
		m1points += m1weight;
	}

	// These tests check to see if you are keeping track of ranks properly in the
	// add() methods.
	@Test
	public void test112OneCharacterConstructorSimple() {
		EditTree t = new EditTree('x');
		assertEquals("[x0]", t.toRankString());
		m1points += m1weight;
	}

	@Test
	public void test113AppendToBeNewRootSimple() {
		EditTree t = new EditTree();
		t.add('c');
		assertEquals("[c0]", t.toRankString());
		m1points += m1weight;
	}

	@Test
	public void test114AppendNoRotationsNeededSimple() {
		EditTree t = new EditTree();
		t.add('c');
		t.add('d');
		assertEquals("[c0, d0]", t.toRankString());
		m1points += m1weight;
	}

	@Test
	public void test115AppendManyUsingPositionOnlySimple() {
		EditTree t = new EditTree();
		t.add('a');
		t.add('b');
		t.add('c');
		t.add('d');
		t.add('e');
		t.add('f');
		t.add('g');
		t.add('h');
		t.add('i');
		t.add('j');
		assertEquals("abcdefghij", t.toString());
		m1points += m1weight;
	}

	@Test
	public void test116AddNoRotationsNeededSimple() {
		EditTree t = new EditTree();
		t.add('b', 0);
		t.add('a', 0);
		t.add('c', 2);
		assertEquals("[b1, a0, c0]", t.toRankString());
		m1points += m1weight;
	}

	@Test
	public void test117AddNoRotationsNeededSimple2() {
		EditTree t = new EditTree();
		t.add('d', 0);
		t.add('b', 0);
		t.add('f', 2);
		t.add('a', 0);
		t.add('c', 2);
		t.add('e', 4);
		t.add('g', 6);
		assertEquals("[d3, b1, a0, c0, f1, e0, g0]", t.toRankString());
		m1points += m1weight;

	}

	@Test
	public void test118ThrowsAddIndexExceptions() {
		EditTree t1 = new EditTree();
		try {
			t1.add('a', 1);
			fail("Did not throw IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
		}

		EditTree t2 = new EditTree();
		try {
			t2.add('a', -1);
			fail("Did not throw IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			m1points += m1weight;
		}

		EditTree t3 = new EditTree();
		t3.add('b');
		t3.add('a', 0);
		t3.add('c');
		try {
			t3.add('d', 4);
			fail("Did not throw IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
		}

		EditTree t4 = new EditTree();
		t4.add('b');
		t4.add('a', 0);
		t4.add('c');
		try {
			t4.add('d', -1);
		} catch (IndexOutOfBoundsException e) {
			m1points += m1weight;
		}
	}

	@Test
	public void test120SimpleGets() {
		EditTree t = new EditTree();
		t.add('d', 0);
		t.add('b', 0);
		t.add('f', 2);
		t.add('a', 0);
		t.add('c', 2);
		t.add('e', 4);
		t.add('g', 6);
		assertEquals("[d3, b1, a0, c0, f1, e0, g0]", t.toRankString());
		// Testing each position from the top of the tree down.
		assertEquals('d', t.get(3));
		assertEquals('b', t.get(1));
		assertEquals('f', t.get(5));
		assertEquals('a', t.get(0));
		assertEquals('c', t.get(2));
		assertEquals('e', t.get(4));
		assertEquals('g', t.get(6));
		m1points += m1weight;
	}

	// The remaining tests for get() use this method as a quicker way to make
	// sure get() works on every position in the tree.
	public void assertStringByChar(String expected, EditTree t) {
		for (int i = 0; i < expected.length(); i++) {
			assertEquals(expected.charAt(i), t.get(i));
		}
	}

	@Test
	public void test121GetRoot() {
		EditTree t = new EditTree();
		t.add('a');
		assertStringByChar("a", t);

		m1points += m1weight;
	}

	@Test
	public void test122GetTwoLevelFull() {
		EditTree t = new EditTree();
		t.add('b');
		t.add('a', 0);
		t.add('c'); // Two full levels

		assertStringByChar("abc", t);

		m1points += m1weight;
	}

	@Test
	public void test123GetThreeLevelFull() {
		EditTree t = new EditTree();
		t.add('d');
		t.add('b', 0);
		t.add('f');
		t.add('a', 0);
		t.add('c', 2);
		t.add('e', 4);
		t.add('g'); // Three full levels

		assertStringByChar("abcdefg", t);

		m1points += m1weight;
	}

	@Test
	public void test124GetTwoLevelJagged() {
		EditTree t1 = new EditTree();
		t1.add('a');
		t1.add('b');

		assertStringByChar("ab", t1);

		EditTree t2 = new EditTree();
		t2.add('b');
		t2.add('a', 0);

		assertStringByChar("ab", t2);

		m1points += m1weight;
	}

	@Test
	public void test125GetThreeLevelJagged() {
		EditTree t1 = new EditTree();

		t1.add('c');
		t1.add('b', 0);
		t1.add('d');
		t1.add('a', 0);

		assertStringByChar("abcd", t1);

		EditTree t2 = new EditTree();

		t2.add('c');
		t2.add('a', 0);
		t2.add('d');
		t2.add('b', 1);

		assertStringByChar("abcd", t2);

		EditTree t3 = new EditTree();

		t3.add('b');
		t3.add('a', 0);
		t3.add('d');
		t3.add('c', 2);

		assertStringByChar("abcd", t3);

		EditTree t4 = new EditTree();

		t4.add('b');
		t4.add('a', 0);
		t4.add('c');
		t4.add('d');

		assertStringByChar("abcd", t4);

		m1points += m1weight;
	}

	@Test
	public void test126ThrowsGetIndexExceptions() {
		EditTree t1 = new EditTree();
		try {
			t1.get(0);
			fail("Did not throw IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
		}

		EditTree t2 = new EditTree();
		try {
			t2.get(-1);
			fail("Did not throw IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
		}

		EditTree t3 = new EditTree();
		t3.add('a');
		try {
			t3.get(1);
			fail("Did not throw IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
		}

		EditTree t4 = new EditTree();
		t4.add('a');
		try {
			t4.get(-1);
			fail("Did not throw IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
		}

		EditTree t5 = new EditTree();
		t5.add('b');
		t5.add('a', 0);
		t5.add('c');
		try {
			t5.get(3);
		} catch (IndexOutOfBoundsException e) {
		}

		EditTree t6 = new EditTree();
		t6.add('b');
		t6.add('a', 0);
		t6.add('c');
		try {
			t6.get(-1);
			fail("Did not throw IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
		}

		EditTree t7 = new EditTree();
		t7.add('d');
		t7.add('b', 0);
		t7.add('f');
		t7.add('a', 0);
		t7.add('c', 2);
		t7.add('e', 4);
		t7.add('g');
		try {
			t7.get(7);
			fail("Did not throw IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
		}

		EditTree t8 = new EditTree();
		t8.add('d');
		t8.add('b', 0);
		t8.add('f');
		t8.add('a', 0);
		t8.add('c', 2);
		t8.add('e', 4);
		t8.add('g');
		try {
			t8.get(-1);
			fail("Did not throw IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
		}

		m1points += m1weight * 2;
	}

	// Verifying ranks
	@Test
	public void test130TestRanksMatchLeftSubtreeSize() {
		EditTree t = new EditTree();
		t.add('d', 0);
		t.add('b', 0);
		t.add('f', 2);
		t.add('a', 0);
		t.add('c', 2);
		t.add('e', 4);
		t.add('g', 6);
		t.add('x', 3);
//		t.show();
//		while (true) {
//			/* spin until user closes graphic window */
//		}
		assertTrue("Expected: true", t.ranksMatchLeftSubtreeSize());
		// Make a rank incorrect:
		int tempLeftRank = t.root.left.rank;
		t.root.left.rank = 0;
		assertFalse("Expected: false", t.ranksMatchLeftSubtreeSize());
		// Restore it.
		t.root.left.rank = tempLeftRank;
		// Make another rank incorrect:
		int tempRightRank = t.root.right.rank;
		t.root.right.rank = 0;
		assertFalse("Expected: false", t.ranksMatchLeftSubtreeSize());
		// Restore it.
		t.root.right.rank = tempRightRank;
		m1points += 2 * m1weight;
	}

	// This shows how to use the graphical debugger. There are no points
	// for it, it's just to help you.
	// Uncomment the last 2 lines to see the graphics window.
	// You can comment them out once you are have tested it.
	@Test
	public void test140TestDisplayableTree() {
		EditTree t = new EditTree();
		t.add('d', 0);
		t.add('b', 0);
		t.add('f', 2);
		t.add('a', 0);
		t.add('c', 2);
		t.add('e', 4);
		t.add('g', 6);
		assertEquals("[d3, b1, a0, c0, f1, e0, g0]", t.toRankString());

		// TODO: you can comment out the last 2 lines of this test. We put them here to
		// force you to read the README.txt file in the debug package and consider if
		// you want to use the graphical debugger or not.
		// If so (which past students recommend), then take 5 minutes and do what the
		// README says to make this error go away.
		// Then comment out the next 2 lines now and any time
		// you aren't using the graphical debugger.
		t.show();
		while (true) {
			/* spin until user closes graphic window */
		}
	}

	@AfterClass
	public static void printSummary() {
		System.out.print("\n ===============     ");
		System.out.print("Milestone 1: ");
		System.out.printf("%4.1f / %d ", m1points, MAX_DESIRED_MILESTONE1);
		System.out.println("     ===============");
	}
}
