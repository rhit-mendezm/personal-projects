package editortrees;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Random;

import org.junit.AfterClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import editortrees.Node.Code;

/**
 * Tests for {@link editortrees.EditTree#concatenate(EditTree)}
 * 
 * @author Jimmy Theis
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EditTreeMilestone2Test {

	private static double m1points = 0;
	private static double m2points = 0;

	private static final int MAX_MILESTONE1 = 25;
	private static final int MAX_MILESTONE2 = 60;
	private static final int MAX_DESIRED_MILESTONE1 = 10;
	private static final int MAX_DESIRED_MILESTONE2 = 60;
	private static final int MAX_POINTS = MAX_DESIRED_MILESTONE1 + MAX_DESIRED_MILESTONE2;

	private static final double m1weight = (double) MAX_DESIRED_MILESTONE1 / MAX_MILESTONE1; // 0.2
	private static final double m2weight = (double) MAX_DESIRED_MILESTONE2 / MAX_MILESTONE2; // 0.5

	// MILESTONE 1 REPEATED
	// Free points as long as it doesn't get broken. Continuing to run
	// old unit tests is called "regression testing". We software engineers
	// love any way to help ensure high quality code!

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
		assertTrue("Expected: true", t.ranksMatchLeftSubtreeSize());
		// Make a rank incorrect:
		t.root.left.rank = 0;
		assertFalse("Expected: false", t.ranksMatchLeftSubtreeSize());
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
//		t.show();
//		while (true) { /* spin until user closes graphic window */ }
	}

	// Now MILESTONE 2, mostly more intense tests that require
	// rotations and balancing.
	// We start with testing the debug string format.

	@Test
	public void test200DebugStringFormat() {
		EditTree t = new EditTree();
		t.add('a');
		assertEquals("a", t.toString());
		assertEquals("[a0=]", t.toDebugString());
		m2points += m2weight;
	}

	@Test
	public void test201Empty() {
		EditTree t = new EditTree();
		assertEquals("", t.toString());
		assertEquals(-1, t.slowHeight());
		assertEquals("[]", t.toDebugString());
		assertEquals(0, t.totalRotationCount());
		m2points += m2weight;
	}

	@Test
	public void test202Root() {
		EditTree t = new EditTree();
		t.add('a');
		assertEquals("a", t.toString());
		assertEquals("[a0=]", t.toDebugString());
		assertEquals(0, t.slowHeight());
		assertEquals(0, t.totalRotationCount());
		m2points += m2weight;
	}

	@Test
	public void test203TwoLevelsNoRotations() {
		EditTree t = new EditTree();
		t.add('b');
		t.add('a', 0);
		t.add('c');
		assertEquals(0, t.totalRotationCount());
		assertEquals("abc", t.toString());
		assertEquals("[b1=, a0=, c0=]", t.toDebugString());
		assertEquals(1, t.slowHeight());
		m2points += m2weight;
	}

	@Test
	public void test204ThreeLevelsNoRotations() {
		EditTree t = new EditTree();
		t.add('d');
		t.add('b', 0);
		t.add('f');
		t.add('a', 0);
		t.add('c', 2);
		assertEquals(0, t.totalRotationCount());
		t.add('e', 4);
		assertEquals(0, t.totalRotationCount());
		t.add('g');
		assertEquals(0, t.totalRotationCount());
		assertEquals("abcdefg", t.toString());
		assertEquals(2, t.slowHeight());
		assertEquals("[d3=, b1=, a0=, c0=, f1=, e0=, g0=]", t.toDebugString());
		assertEquals(0, t.totalRotationCount());
		m2points += m2weight;
	}

	@Test
	public void test205FourLevelsNoRotations() {
		EditTree t = new EditTree();
		t.add('h');
		t.add('d', 0);
		t.add('l');
		t.add('b', 0);
		t.add('f', 2);
		t.add('j', 4);
		t.add('n');
		t.add('a', 0);
		t.add('c', 2);
		t.add('e', 4);
		t.add('g', 6);
		t.add('i', 8);
		t.add('k', 10);
		t.add('m', 12);
		t.add('o');
		assertEquals(0, t.totalRotationCount());
		assertEquals("abcdefghijklmno", t.toString());
		assertEquals("[h7=, d3=, b1=, a0=, c0=, f1=, e0=, g0=, l3=, j1=, i0=, k0=, n1=, m0=, o0=]", t.toDebugString());
		assertEquals(3, t.slowHeight());
		m2points += m2weight;
	}

	@Test
	public void test206InsertingIntoLastElement() {
		EditTree t = new EditTree();
		t.add('h', 0); // Insertion into last element
		t.add('d', 0);
		t.add('l', 2); // Insertion into last element
		t.add('b', 0);
		t.add('f', 2);
		t.add('j', 4);
		t.add('n', 6); // Insertion into last element
		t.add('a', 0);
		t.add('c', 2);
		t.add('e', 4);
		t.add('g', 6);
		t.add('i', 8);
		t.add('k', 10);
		t.add('m', 12);
		t.add('o', 14); // Insertion into last element
		assertEquals(0, t.totalRotationCount());
		assertEquals("abcdefghijklmno", t.toString());
		assertEquals("[h7=, d3=, b1=, a0=, c0=, f1=, e0=, g0=, l3=, j1=, i0=, k0=, n1=, m0=, o0=]", t.toDebugString());
		assertEquals(3, t.slowHeight());

		m2points += m2weight;
	}

	// The next ones require rotations.
	@Test
	public void test210SingleLeftRotationFirstLevel() {
		EditTree t = new EditTree();
		t.add('a');
		t.add('b');
		t.add('c'); // Rotation happens here
		assertEquals(1, t.totalRotationCount());
		assertEquals("abc", t.toString());
		assertEquals("[b1=, a0=, c0=]", t.toDebugString());
		assertEquals(1, t.slowHeight());
		m2points += m2weight;
	}

	@Test
	public void test211SingleLeftRotationSecondLevel() {
		// Cause a rotation on the right subtree
		EditTree t1 = new EditTree();
		t1.add('b');
		t1.add('a', 0);
		t1.add('c'); // Two full levels

		t1.add('d');
		t1.add('e'); // Rotation happens here
		assertEquals(1, t1.totalRotationCount());
		assertEquals("abcde", t1.toString());
		assertEquals("[b1\\, a0=, d1=, c0=, e0=]", t1.toDebugString());
		assertEquals(2, t1.slowHeight());

		m2points += m2weight;

		// Cause a rotation on the left subtree
		EditTree t2 = new EditTree();
		t2.add('d');
		t2.add('a', 0);
		t2.add('e'); // Two full levels

		t2.add('b', 1);
		t2.add('c', 2); // Rotation happens here
		assertEquals(1, t2.totalRotationCount());
		assertEquals("abcde", t2.toString());
		assertEquals("[d3/, b1=, a0=, c0=, e0=]", t2.toDebugString());
		assertEquals(2, t2.slowHeight());

		m2points += m2weight;
	}

	@Test
	public void test212SingleLeftRotationThirdLevel() {
		// Cause a rotation on the farthest rightmost from the third level
		EditTree t1 = new EditTree();
		t1.add('d');
		t1.add('b', 0);
		t1.add('f');
		t1.add('a', 0);
		t1.add('c', 2);
		t1.add('e', 4);
		t1.add('g'); // Three full levels
		assertEquals(0, t1.totalRotationCount());

		t1.add('h');
		t1.add('i'); // Rotation happens here
		assertEquals(1, t1.totalRotationCount());

		assertEquals("abcdefghi", t1.toString());
		assertEquals("[d3\\, b1=, a0=, c0=, f1\\, e0=, h1=, g0=, i0=]", t1.toDebugString());
		assertEquals(3, t1.slowHeight());

		// Cause a rotation on the leftmost branch of the right branch from the
		// third level
		EditTree t2 = new EditTree();
		t2.add('d');
		t2.add('b', 0);
		t2.add('h');
		t2.add('a', 0);
		t2.add('c', 2);
		t2.add('e', 4);
		t2.add('i'); // Three full levels

		t2.add('f', 5);
		t2.add('g', 6); // Rotation happens here
		assertEquals(1, t2.totalRotationCount());

		assertEquals("abcdefghi", t2.toString());
		assertEquals("[d3\\, b1=, a0=, c0=, h3/, f1=, e0=, g0=, i0=]", t2.toDebugString());
		assertEquals(3, t2.slowHeight());

		m2points += m2weight;

		// Cause a rotation on the rightmost branch of the left branch from the
		// third level
		EditTree t3 = new EditTree();
		t3.add('f');
		t3.add('b', 0);
		t3.add('h');
		t3.add('a', 0);
		t3.add('c', 2);
		t3.add('g', 4);
		t3.add('i'); // Three full levels

		t3.add('d', 3);
		t3.add('e', 4); // Rotation happens here
		assertEquals(1, t3.totalRotationCount());

		assertEquals("abcdefghi", t3.toString());
		assertEquals("[f5/, b1\\, a0=, d1=, c0=, e0=, h1=, g0=, i0=]", t3.toDebugString());
		assertEquals(3, t3.slowHeight());

		// Cause a rotation on the leftmost branch from the third level
		EditTree t4 = new EditTree();
		t4.add('f');
		t4.add('d', 0);
		t4.add('h');
		t4.add('a', 0);
		t4.add('e', 2);
		t4.add('g', 4);
		t4.add('i'); // Three full levels

		t4.add('b', 1);
		t4.add('c', 2); // Rotation happens here
		assertEquals(1, t4.totalRotationCount());
		assertEquals("abcdefghi", t4.toString());
		assertEquals("[f5/, d3/, b1=, a0=, c0=, e0=, h1=, g0=, i0=]", t4.toDebugString());
		assertEquals(3, t4.slowHeight());

		m2points += m2weight;
	}

	@Test
	public void test213SingleLeftRotationTwoLevelFromRoot() {
		EditTree t = new EditTree();
		t.add('b');
		t.add('a', 0);
		t.add('d'); // Two full levels

		t.add('c', 2);
		t.add('e');
		t.add('f'); // Rotation happens here
		assertEquals(1, t.totalRotationCount());
		assertEquals("abcdef", t.toString());
		assertEquals("[d3=, b1=, a0=, c0=, e0\\, f0=]", t.toDebugString());
		assertEquals(2, t.slowHeight());

		m2points += m2weight;
	}

	@Test
	public void test214SingleLeftRotationTwoLevelFromFirstLevel() {
		// Cause a rotation on the right subtree
		EditTree t1 = new EditTree();

		t1.add('d');
		t1.add('b', 0);
		t1.add('f');
		t1.add('a', 0);
		t1.add('c', 2);
		t1.add('e', 4);
		t1.add('h'); // Three full levels

		t1.add('g', 6);
		t1.add('i');
		assertEquals(0, t1.totalRotationCount());
		t1.add('j'); // Rotation happens here
		assertEquals(1, t1.totalRotationCount());
		assertEquals("abcdefghij", t1.toString());
		assertEquals("[d3\\, b1=, a0=, c0=, h3=, f1=, e0=, g0=, i0\\, j0=]", t1.toDebugString());
		assertEquals(3, t1.slowHeight());

		m2points += m2weight;

		// Cause a rotation on the left subtree
		EditTree t2 = new EditTree();

		t2.add('g');
		t2.add('b', 0);
		t2.add('i');
		t2.add('a', 0);
		t2.add('d', 2);
		t2.add('h', 4);
		t2.add('j'); // Three full layers

		t2.add('c', 2);
		t2.add('e', 4);
		t2.add('f', 5); // Rotation happens here
		assertEquals(1, t2.totalRotationCount());

		assertEquals("abcdefghij", t2.toString());
		assertEquals("[g6/, d3=, b1=, a0=, c0=, e0\\, f0=, i1=, h0=, j0=]", t2.toDebugString());
		assertEquals(3, t2.slowHeight());

		m2points += m2weight;
	}

	@Test
	public void test215SingleRightRotationFirstLevel() {
		EditTree t = new EditTree();
		t.add('c');
		t.add('b', 0);

		t.add('a', 0); // Rotation happens here
		assertEquals(1, t.totalRotationCount());

		assertEquals("abc", t.toString());
		assertEquals("[b1=, a0=, c0=]", t.toDebugString());
		assertEquals(1, t.slowHeight());

		m2points += m2weight;
	}

	@Test
	public void test216SingleRightRotationSecondLevel() {
		// Cause a rotation on the left subtree
		EditTree t1 = new EditTree();
		t1.add('d');
		t1.add('c', 0);
		t1.add('e'); // Two full levels

		t1.add('b', 0);
		t1.add('a', 0); // Rotation happens here
		assertEquals(1, t1.totalRotationCount());
		assertEquals("abcde", t1.toString());
		assertEquals("[d3/, b1=, a0=, c0=, e0=]", t1.toDebugString());
		assertEquals(2, t1.slowHeight());

		m2points += m2weight;

		// Cause a rotation on the right subtree
		EditTree t2 = new EditTree();
		t2.add('b');
		t2.add('a', 0);
		t2.add('e'); // Two full levels

		t2.add('d', 2);
		t2.add('c', 2); // Rotation happens here
		assertEquals(1, t1.totalRotationCount());
		assertEquals(1, t2.totalRotationCount());
		assertEquals("abcde", t2.toString());
		assertEquals("[b1\\, a0=, d1=, c0=, e0=]", t2.toDebugString());
		assertEquals(2, t2.slowHeight());

		m2points += m2weight;
	}

	@Test
	public void test217SingleRightRotationThirdLevel() {
		// Cause a rotation on the leftmost branch of the left subtree
		EditTree t1 = new EditTree();

		t1.add('f');
		t1.add('d', 0);
		t1.add('h');
		t1.add('c', 0);
		t1.add('e', 2);
		t1.add('g', 4);
		t1.add('i'); // Three full levels

		t1.add('b', 0);
		t1.add('a', 0); // Rotation happens here

		assertEquals("abcdefghi", t1.toString());
		assertEquals("[f5/, d3/, b1=, a0=, c0=, e0=, h1=, g0=, i0=]", t1.toDebugString());
		assertEquals(3, t1.slowHeight());

		// Cause a rotation on the rightmost branch of the left subtree
		EditTree t2 = new EditTree();

		t2.add('f');
		t2.add('b', 0);
		t2.add('h');
		t2.add('a', 0);
		t2.add('e', 2);
		t2.add('g', 4);
		t2.add('i'); // Three full levels

		t2.add('d', 2);
		t2.add('c', 2); // Rotation happens here

		assertEquals("abcdefghi", t2.toString());
		assertEquals("[f5/, b1\\, a0=, d1=, c0=, e0=, h1=, g0=, i0=]", t2.toDebugString());
		assertEquals(3, t2.slowHeight());

		m2points += m2weight;

		// Cause a rotation on the leftmost branch of the right subtree
		EditTree t3 = new EditTree();

		t3.add('d');
		t3.add('b', 0);
		t3.add('h');
		t3.add('a', 0);
		t3.add('c', 2);
		t3.add('g', 4);
		t3.add('i'); // Three full levels

		t3.add('f', 4);
		t3.add('e', 4); // Rotation happens here

		assertEquals("abcdefghi", t3.toString());
		assertEquals("[d3\\, b1=, a0=, c0=, h3/, f1=, e0=, g0=, i0=]", t3.toDebugString());
		assertEquals(3, t3.slowHeight());

		// Cause a rotation on the rightmost branch of the right subtree
		EditTree t4 = new EditTree();

		t4.add('d');
		t4.add('b', 0);
		t4.add('f');
		t4.add('a', 0);
		t4.add('c', 2);
		t4.add('e', 4);
		t4.add('i'); // Three full levels

		t4.add('h', 6);
		t4.add('g', 6); // Rotation happens here

		assertEquals("abcdefghi", t4.toString());
		assertEquals("[d3\\, b1=, a0=, c0=, f1\\, e0=, h1=, g0=, i0=]", t4.toDebugString());
		assertEquals(3, t4.slowHeight());

		m2points += m2weight;
	}

	@Test
	public void test218SingleRightRotationTwoLevelFromRoot() {
		EditTree t = new EditTree();

		t.add('e');
		t.add('c', 0);
		t.add('f'); // Two full levels

		t.add('b', 0);
		t.add('d', 2);
		t.add('a', 0); // Rotation happens here

		assertEquals("abcdef", t.toString());
		assertEquals("[c2=, b1/, a0=, e1=, d0=, f0=]", t.toDebugString());
		assertEquals(2, t.slowHeight());

		m2points += m2weight;
	}

	@Test
	public void test219SingleRightRotationTwoLevelFromFirstLevel() {
		// Cause a rotation on the left subtree
		EditTree t1 = new EditTree();

		t1.add('g');
		t1.add('e', 0);
		t1.add('i');
		t1.add('c', 0);
		t1.add('f', 2);
		t1.add('h', 4);
		t1.add('j'); // Three full levels

		t1.add('b', 0);
		t1.add('d', 2);
		t1.add('a', 0); // Rotation happens here

		assertEquals("abcdefghij", t1.toString());
		assertEquals("[g6/, c2=, b1/, a0=, e1=, d0=, f0=, i1=, h0=, j0=]", t1.toDebugString());
		assertEquals(3, t1.slowHeight());

		m2points += m2weight;

		// Cause a rotation on the right subtree
		EditTree t2 = new EditTree();

		t2.add('d');
		t2.add('b', 0);
		t2.add('i');
		t2.add('a', 0);
		t2.add('c', 2);
		t2.add('g', 4);
		t2.add('j'); // Three full levels

		t2.add('f', 4);
		t2.add('h', 6);
		t2.add('e', 4); // Rotation happens here

		assertEquals("abcdefghij", t2.toString());
		assertEquals("[d3\\, b1=, a0=, c0=, g2=, f1/, e0=, i1=, h0=, j0=]", t2.toDebugString());
		assertEquals(3, t2.slowHeight());

		m2points += m2weight;
	}

	@Test
	public void test230DoubleLeftRotationFirstLevel() {
		EditTree t = new EditTree();

		t.add('a');
		t.add('c');
		t.add('b', 1); // Rotation happens here
		assertEquals("abc", t.toString());
		assertEquals("[b1=, a0=, c0=]", t.toDebugString());
		assertEquals(1, t.slowHeight());

		m2points += m2weight;
	}

	@Test
	public void test231DoubleLeftRotationSecondLevel() {
		// Cause a rotation in the right subtree
		EditTree t1 = new EditTree();

		t1.add('b');
		t1.add('a', 0);
		t1.add('c'); // Two full levels

		t1.add('e');
		t1.add('d', 3); // Rotation happens here

		assertEquals("abcde", t1.toString());
		assertEquals("[b1\\, a0=, d1=, c0=, e0=]", t1.toDebugString());
		assertEquals(2, t1.slowHeight());

		m2points += m2weight;

		// Cause a rotation in the left subtree
		EditTree t2 = new EditTree();

		t2.add('d');
		t2.add('a', 0);
		t2.add('e'); // Two full levels

		t2.add('c', 1);
		t2.add('b', 1); // Rotation happens here

		assertEquals("abcde", t2.toString());
		assertEquals("[d3/, b1=, a0=, c0=, e0=]", t2.toDebugString());
		assertEquals(2, t2.slowHeight());

		m2points += m2weight;
	}

	@Test
	public void test232DoubleLeftRotationThirdLevel() {
		// Cause a rotation on the rightmost branch
		EditTree t1 = new EditTree();

		t1.add('d');
		t1.add('b', 0);
		t1.add('f');
		t1.add('a', 0);
		t1.add('c', 2);
		t1.add('e', 4);
		t1.add('g'); // Three full levels

		t1.add('i');
		t1.add('h', 7); // Rotation happens here

		assertEquals("abcdefghi", t1.toString());
		assertEquals("[d3\\, b1=, a0=, c0=, f1\\, e0=, h1=, g0=, i0=]", t1.toDebugString());
		assertEquals(3, t1.slowHeight());

		// Cause a rotation on the leftmost branch of the right subtree
		EditTree t2 = new EditTree();

		t2.add('d');
		t2.add('b', 0);
		t2.add('h');
		t2.add('a', 0);
		t2.add('c', 2);
		t2.add('e', 4);
		t2.add('i'); // Three full levels

		t2.add('g', 5);
		t2.add('f', 5); // Rotation happens here

		assertEquals("abcdefghi", t2.toString());
		assertEquals("[d3\\, b1=, a0=, c0=, h3/, f1=, e0=, g0=, i0=]", t2.toDebugString());
		assertEquals(3, t2.slowHeight());

		m2points += m2weight;

		// Cause a rotation on the rightmost branch of the left subtree
		EditTree t3 = new EditTree();

		t3.add('f');
		t3.add('b', 0);
		t3.add('h');
		t3.add('a', 0);
		t3.add('c', 2);
		t3.add('g', 4);
		t3.add('i'); // Three full levels

		t3.add('e', 3);
		t3.add('d', 3); // Rotation happens here

		assertEquals("abcdefghi", t3.toString());
		assertEquals("[f5/, b1\\, a0=, d1=, c0=, e0=, h1=, g0=, i0=]", t3.toDebugString());
		assertEquals(3, t3.slowHeight());

		// Cause a rotation on the leftmost branch
		EditTree t4 = new EditTree();

		t4.add('f');
		t4.add('d', 0);
		t4.add('h');
		t4.add('a', 0);
		t4.add('e', 2);
		t4.add('g', 4);
		t4.add('i'); // Three full levels

		t4.add('c', 1);
		t4.add('b', 1); // Rotation happens here

		assertEquals("abcdefghi", t4.toString());
		assertEquals("[f5/, d3/, b1=, a0=, c0=, e0=, h1=, g0=, i0=]", t4.toDebugString());
		assertEquals(3, t4.slowHeight());

		m2points += m2weight;
	}

	@Test
	public void test233DoubleLeftRotationTwoLevelFromFirstLevel() {
		// Cause a rotation on the right subtree
		EditTree t1 = new EditTree();

		t1.add('d');
		t1.add('b', 0);
		t1.add('f');
		t1.add('a', 0);
		t1.add('c', 2);
		t1.add('e', 4);
		t1.add('i'); // Three full levels

		t1.add('h', 6);
		t1.add('j');
		t1.add('g', 6); // Rotation happens here

		assertEquals("abcdefghij", t1.toString());
		assertEquals("[d3\\, b1=, a0=, c0=, h3=, f1=, e0=, g0=, i0\\, j0=]", t1.toDebugString());
		assertEquals(3, t1.slowHeight());

		m2points += m2weight;

		// Cause a rotation on the left subtree
		EditTree t2 = new EditTree();

		t2.add('g');
		t2.add('b', 0);
		t2.add('i');
		t2.add('a', 0);
		t2.add('e', 2);
		t2.add('h', 4);
		t2.add('j'); // Three full levels

		t2.add('d', 2);
		t2.add('f', 4);
		t2.add('c', 2); // Rotation happens here

		assertEquals("abcdefghij", t2.toString());
		assertEquals("[g6/, d3=, b1=, a0=, c0=, e0\\, f0=, i1=, h0=, j0=]", t2.toDebugString());
		assertEquals(3, t2.slowHeight());

		m2points += m2weight;
	}

	@Test
	public void test234DoubleRightRotationFirstLevel() {
		EditTree t = new EditTree();
		t.add('c');
		t.add('a', 0);
		t.add('b', 1); // Rotation happens here

		assertEquals("abc", t.toString());
		assertEquals("[b1=, a0=, c0=]", t.toDebugString());
		assertEquals(1, t.slowHeight());

		m2points += m2weight;
	}

	@Test
	public void test235DoubleRightRotationSecondLevel() {
		// Cause a rotation from the right subtree
		EditTree t1 = new EditTree();

		t1.add('d');
		t1.add('c', 0);
		t1.add('e'); // Two full levels

		t1.add('a', 0);
		t1.add('b', 1); // Rotation happens here

		assertEquals("abcde", t1.toString());
		assertEquals("[d3/, b1=, a0=, c0=, e0=]", t1.toDebugString());
		assertEquals(2, t1.slowHeight());

		m2points += m2weight;

		// Cause a rotation in the left subtree
		EditTree t2 = new EditTree();

		t2.add('b');
		t2.add('a', 0);
		t2.add('e'); // Two full levels

		t2.add('c', 2);
		t2.add('d', 3); // Rotation happens here

		assertEquals("abcde", t2.toString());
		assertEquals("[b1\\, a0=, d1=, c0=, e0=]", t2.toDebugString());
		assertEquals(2, t2.slowHeight());

		m2points += m2weight;
	}

	@Test
	public void test236DoubleRightRotationThirdLevel() {
		// Cause a rotation on the leftmost branch
		EditTree t1 = new EditTree();

		t1.add('f');
		t1.add('d', 0);
		t1.add('h');
		t1.add('c', 0);
		t1.add('e', 2);
		t1.add('g', 4);
		t1.add('i'); // Three full levels

		t1.add('a', 0);
		t1.add('b', 1); // Rotation happens here

		assertEquals("abcdefghi", t1.toString());
		assertEquals("[f5/, d3/, b1=, a0=, c0=, e0=, h1=, g0=, i0=]", t1.toDebugString());
		assertEquals(3, t1.slowHeight());

		// Cause a rotation on the rightmost branch of the left subtree
		EditTree t2 = new EditTree();

		t2.add('f');
		t2.add('b', 0);
		t2.add('h');
		t2.add('a', 0);
		t2.add('e', 2);
		t2.add('g', 4);
		t2.add('i'); // Three full levels

		t2.add('c', 2);
		t2.add('d', 3); // Rotation happens here

		assertEquals("abcdefghi", t2.toString());
		assertEquals("[f5/, b1\\, a0=, d1=, c0=, e0=, h1=, g0=, i0=]", t2.toDebugString());
		assertEquals(3, t2.slowHeight());

		m2points += m2weight;

		// Cause a rotation on the leftmost branch of the right subtree
		EditTree t3 = new EditTree();

		t3.add('d');
		t3.add('b', 0);
		t3.add('h');
		t3.add('a', 0);
		t3.add('c', 2);
		t3.add('g', 4);
		t3.add('i'); // Three full levels

		t3.add('e', 4);
		t3.add('f', 5); // Rotation happens here

		assertEquals("abcdefghi", t3.toString());
		assertEquals("[d3\\, b1=, a0=, c0=, h3/, f1=, e0=, g0=, i0=]", t3.toDebugString());
		assertEquals(3, t3.slowHeight());

		// Cause a rotation on the rightmost branch
		EditTree t4 = new EditTree();

		t4.add('d');
		t4.add('b', 0);
		t4.add('f');
		t4.add('a', 0);
		t4.add('c', 2);
		t4.add('e', 4);
		t4.add('i', 6); // Three full levels

		t4.add('g', 6);
		t4.add('h', 7); // Rotation happens here

		assertEquals("abcdefghi", t4.toString());
		assertEquals("[d3\\, b1=, a0=, c0=, f1\\, e0=, h1=, g0=, i0=]", t4.toDebugString());
		assertEquals(3, t4.slowHeight());

		m2points += m2weight;
	}

	@Test
	public void test237DoubleRightRotationTwoLevelFromRoot() {
		EditTree t = new EditTree();

		t.add('e');
		t.add('b', 0);
		t.add('f'); // Two full levels

		t.add('a', 0);
		t.add('c', 2);
		t.add('d', 3); // Rotation happens here

		assertEquals("abcdef", t.toString());
		assertEquals("[c2=, b1/, a0=, e1=, d0=, f0=]", t.toDebugString());
		assertEquals(2, t.slowHeight());

		m2points += m2weight;
	}

	@Test
	public void test238DoubleRightRotationTwoLevelFromFirstLevel() {
		EditTree t1 = new EditTree();

		t1.add('g');
		t1.add('e', 0);
		t1.add('i');
		t1.add('b', 0);
		t1.add('f', 2);
		t1.add('h', 4);
		t1.add('j'); // Three full levels

		t1.add('a', 0);
		t1.add('c', 2);
		t1.add('d', 3); // Rotation happens here

		assertEquals("abcdefghij", t1.toString());
		assertEquals("[g6/, c2=, b1/, a0=, e1=, d0=, f0=, i1=, h0=, j0=]", t1.toDebugString());
		assertEquals(3, t1.slowHeight());

		m2points += m2weight;

		EditTree t2 = new EditTree();

		t2.add('d');
		t2.add('b', 0);
		t2.add('i');
		t2.add('a', 0);
		t2.add('c', 2);
		t2.add('f', 4);
		t2.add('j'); // Three full levels

		t2.add('e', 4);
		t2.add('g', 6);
		t2.add('h', 7); // Rotation happens here

		assertEquals("abcdefghij", t2.toString());
		assertEquals("[d3\\, b1=, a0=, c0=, g2=, f1/, e0=, i1=, h0=, j0=]", t2.toDebugString());
		assertEquals(3, t2.slowHeight());

		m2points += m2weight;
	}

	@Test
	public void test250ManyRotations() {
		EditTree t = new EditTree();

		t.add('J');
		t.add('T');
		t.add('O', 1);
		assertEquals(2, t.totalRotationCount());
		t.add('L', 1);
		t.add('N', 2);
		assertEquals(3, t.totalRotationCount());
		t.add('M', 2);
		assertEquals(5, t.totalRotationCount());
		t.add('m');
		assertEquals(6, t.totalRotationCount());
		t.add('o');
		t.add('d', 6);
		t.add('g', 7);
		assertEquals(8, t.totalRotationCount());
		t.add('R', 5);
		assertEquals(10, t.totalRotationCount());
		t.add('b', 7);
		assertEquals(12, t.totalRotationCount());
		t.add('q');
		t.add('r');
		assertEquals(13, t.totalRotationCount());
		assertEquals(4, t.slowHeight());
		t.add('s');
		assertEquals(14, t.totalRotationCount());
		t.add('t');
		assertEquals(15, t.totalRotationCount());
		t.add('u');
		assertEquals(16, t.totalRotationCount());
		t.add('v');
		assertEquals(17, t.totalRotationCount());
		t.add('w');
		assertEquals(18, t.totalRotationCount());
		t.add('x');
		assertEquals(19, t.totalRotationCount());
		t.add('y');
		assertEquals(20, t.totalRotationCount());
		t.add('z');
		assertEquals(21, t.totalRotationCount());
		t.add('{');
		assertEquals(22, t.totalRotationCount());
		t.add('|');
		assertEquals(23, t.totalRotationCount());
		assertEquals(4, t.slowHeight());
		t.add('}');
		assertEquals(24, t.totalRotationCount());
		t.add('~');
		assertEquals(25, t.totalRotationCount());
		t.add('[');
		assertEquals(26, t.totalRotationCount());
		t.add(']');
		assertEquals(27, t.totalRotationCount());
		assertEquals(4, t.slowHeight());
		t.add('&');
		assertEquals(27, t.totalRotationCount());
		assertEquals(5, t.slowHeight());
		t.add('!');
		assertEquals(28, t.totalRotationCount());
		assertEquals(5, t.slowHeight());
		m2points += 5 * m2weight;
	}

	// Verifying that method to verify that balance codes are correct works.
	@Test
	public void test255TestBalanceCodesAreCorrect() {
		// Same as tree from testManyRotations
		EditTree t = new EditTree();
		t.add('J');
		t.add('T');
		t.add('O', 1);
		t.add('L', 1);
		t.add('N', 2);
		t.add('M', 2);
		t.add('m');
		t.add('o');
		t.add('d', 6);
		t.add('g', 7);
		t.add('R', 5);
		t.add('b', 7);
		t.add('q');
		t.add('r');
		t.add('s');
		t.add('t');
		t.add('u');
		t.add('v');
		t.add('w');
		t.add('x');
		t.add('y');
		t.add('z');
		t.add('{');
		t.add('|');
		t.add('}');
		t.add('~');
		t.add('[');
		t.add(']');
		t.add('&');
		t.add('!');
		assertTrue("Expected: true", t.balanceCodesAreCorrect());
		// Make a balance code incorrect. It should have been SAME before this:
		t.root.left.right.balance = Code.LEFT;
		assertFalse("Expected: false", t.balanceCodesAreCorrect());
		m2points += 2 * m2weight;
	}

	@Test
	public void test260GetAfterRotations() {
		EditTree t = new EditTree();
		t.add('a');
		t.add('b');
		t.add('c'); // Single left rotation
		assertEquals(1, t.totalRotationCount());

		assertStringByChar("abc", t);

		t.add('d', 0);
		t.add('e', 0); // Single right rotation
		assertEquals(2, t.totalRotationCount());
		assertStringByChar("edabc", t);

		m2points += m2weight;

		t.add('f', 4);
		t.add('g');
		assertEquals(2, t.totalRotationCount());

		t.add('h');
		t.add('i', 7); // Double left rotation
		assertEquals(4, t.totalRotationCount());

		assertStringByChar("edabfcgih", t);

		m2points += m2weight;

		t.add('j', 0);
		t.add('k', 2);
		t.add('l', 4);
		t.add('m', 6);
		t.add('n', 8);
		t.add('o', 10);
		t.add('p', 0);
		t.add('q', 1); // Double right rotation

		assertStringByChar("pqjekdlambnfocgih", t);

		m2points += m2weight;
	}

	// STRESS TESTS FOR ADD
	// You may want to comment out these next few methods while developing to
	// speed up your tests.
	// Once your code is complete and efficient, they should run really quickly!
	private static final int NUM_NODES = 1000000;
	private static final int SKIP_INTERVAL = 10;

	@Test
	public void test270AddManyInc() {
		EditTree t = new EditTree();
		for (int k = 0; k < NUM_NODES; k++) {
			t.add((char) k);
		}
		assertEquals(19, t.fastHeight());
		assertEquals(999980, t.totalRotationCount());
		m2points += m2weight;
	}

	@Test
	public void test271AddManyDec() {
		EditTree t = new EditTree();
		for (int k = NUM_NODES; k > 0; k--) {
			t.add((char) k);
		}

		assertEquals(19, t.fastHeight());
		assertEquals(999980, t.totalRotationCount());
		m2points += m2weight;
	}

	/**
	 * If a student should fail this test once, they might have reached the rotation
	 * count outside of the tested range. If a student should fail this test
	 * consistently - excessive rotations are being performed.
	 */
	@Test
	public void test272AddManyRandom() {

		EditTree t = new EditTree();
		// Note that with the no-argument version of the Random constructor, that every time 
		// you run it, it will generate a different sequence of random numbers 
		// and thus a different tree with different rotations because 
		// its random number generator makes use of the current system time which is constantly 
		// changing. This is nice, but hard to use when debugging, since every time you re-run the 
		// debugger, you get a different tree! So instead, we pass the Random constructor 
		// am integer parameter, called a "seed", which it will use instead of the time, 
		// and so every time you run it with the same seed, it will generate the same sequence of 
		// "random" numbers and thus the same tree/rotations. 
		// So when you are debugging, pass in an integer of your choosing. Feel free to change 
		// the integer until you get a sequence that exposes your bug with a relatively-small tree.
		// It's easier to debug and visualize a tree of height 3 than one of height 30! 
		// DONE: change to new Random(17), new Random(50), or whatever you find convenient while debugging 
		Random random = new Random(4); 
		
		for (int k = 0; k < NUM_NODES / SKIP_INTERVAL; k++) {
			for (int j = 0; j < 10; j++) {
				char toAdd = (char) ('0' + j);
				t.add(toAdd, random.nextInt(SKIP_INTERVAL * k + j + 1));

//				if (!t.balanceCodesAreCorrect()) {
//					String s = String.format(
//							"Balance codes incorrect on tree with %d nodes",
//							t.slowSize()
//					);
//					System.out.println(s);
//					// At this point, you can print more info, put in a breakpoint, 
//					// run the visualizer, whatever you need to do to debug.
//				}
//				if (!t.ranksMatchLeftSubtreeSize()) {
//					String s = String.format(
//							"Ranks incorrect on tree with %d nodes",
//							t.slowSize()
//					);
//					System.out.println(s);
//					// Same here.
//				}
				
				
			}
		}

		int height = t.fastHeight();
		int maxH = (int) Math.ceil(1.44 * (Math.log(NUM_NODES) / Math.log(2)));
		int minH = (int) Math.floor(Math.log(NUM_NODES) / Math.log(2));
		boolean heightCheck = false;
		if (height >= minH && height <= maxH) {
			heightCheck = true;
		}
		assertTrue("Expected: true", heightCheck);
		m2points += m2weight;

		// This range was created using several correct EditorTrees projects
		// that returned similar results
		// It has a slight amount of built-in padding, but even for the random
		// insertion values should still fit in this range.
		// Dr. B. found min of 696758, max of 700715 when running 150-200x.
		int maxR = 701000;
		int minR = 696500;
		int rot = t.totalRotationCount();
		// System.out.println("While debugging, rot count = " + rot);
		boolean rotCheck = false;
		if (rot >= minR && rot <= maxR) {
			rotCheck = true;
		} 
		assertTrue("Expected: true", rotCheck);
		m2points += 3 * m2weight;
	}

	@Test
	public void test280ConstructorWithEditTree() {
		EditTree t1s = new EditTree();
		EditTree t2s = makeFullTreeWithHeight(0, 'a');
		EditTree t3s = makeFullTreeWithHeight(2, 'a');

		EditTree t1 = new EditTree(t1s);
		EditTree t2 = new EditTree(t2s);
		EditTree t3 = new EditTree(t3s);

		// Test to see that nodes are clones of nodes from the original tree,
		// and not just aliases to the same nodes.
		// We make use of the getRoot() method and its left and right children.
		Node t3root = t3.root;
		Node t3originalRoot = t3s.root;
		assertFalse("Expected: false", t3root == t3originalRoot);
		assertFalse("Expected: false", t3root.left == t3originalRoot.left);
		assertFalse("Expected: false", t3root.right.left == t3originalRoot.right.left);

		// Test using sizes and heights.
		assertEquals(t1s.toString(), t1.toString());
		assertTrue("Expected: true", t1.slowHeight() <= maxHeight(t1.size()));
		assertEquals(t2s.toString(), t2.toString());
		assertTrue("Expected: true", t2.slowHeight() <= maxHeight(t2.size()));
		assertEquals(t3s.toString(), t3.toString());
		assertTrue("Expected: true", t3.slowHeight() <= maxHeight(t3.size()));
		assertEquals(0, t3.totalRotationCount());

		assertEquals(t1s.slowHeight(), t1.slowHeight());
		assertEquals(t2s.slowHeight(), t2.slowHeight());
		assertEquals(t3s.slowHeight(), t3.slowHeight());

		assertEquals(t1s.toDebugString(), t1.toDebugString());
		assertEquals(t2s.toDebugString(), t2.toDebugString());
		assertEquals(t3s.toDebugString(), t3.toDebugString());

		m2points += 2 * m2weight;

		t3.add('x');
		EditTree t4 = new EditTree(t3);
		assertEquals(t3.toString(), t4.toString());
		assertEquals(t3.toDebugString(), t4.toDebugString());
		assertTrue("Expected: true", t4.slowHeight() <= maxHeight(t4.size()));

		// Add to tree I copied to. Are balance codes and ranks set?
		t4.add('y', 2);
		EditTree t5 = new EditTree(t4);
		assertEquals(t4.toString(), t5.toString());
		assertEquals(t4.toDebugString(), t5.toDebugString());
		assertTrue("Expected: true", t5.slowHeight() <= maxHeight(t5.size()));

		// Add again to tree I copied to, but causing a rotation
		t5.add('z', 2);
		EditTree t6 = new EditTree(t5);
		assertEquals(t5.toString(), t6.toString());
		assertEquals(t5.toDebugString(), t6.toDebugString());
		assertTrue("Expected: true", t6.slowHeight() <= maxHeight(t6.size()));
		assertEquals(0, t3.totalRotationCount());
		m2points += 4 * m2weight;

	}

	private EditTree makeFullTreeWithHeight(int height, char start) {
		EditTree t = new EditTree();
		// This would be much easier to do recursively, if we could
		// depend on having a Node constructor that took two children
		// as arguments!

		for (int i = 0; i <= height; i++) {
			int offset = (int) Math.pow(2, height - i) - 1;
			int increment = (int) Math.pow(2, height + 1 - i);
			for (int j = 0; j < Math.pow(2, i); j++) {
				t.add((char) (start + offset), 2 * j);
				offset += increment;
			}
		}
		return t;
	}

	private int maxHeight(int nodes) {
		int height = -1;
		int maxNodes = 1;
		int prevMaxNodes = 0;

		while (nodes >= maxNodes) {
			int temp = prevMaxNodes;
			prevMaxNodes = maxNodes;
			maxNodes = temp + maxNodes + 1;
			height++;
		}

		return height;
	}

	@AfterClass
	public static void printSummary() {
		System.out.print("\n ===============     ");
		System.out.print("Milestone 1 again: ");
		System.out.printf("%4.1f / %d ", m1points, MAX_DESIRED_MILESTONE1);
		System.out.println("     ===============");

		System.out.print("\n ===============     ");
		System.out.print("Milestone 2:       ");
		System.out.printf("%4.1f / %d ", m2points, MAX_DESIRED_MILESTONE2);
		System.out.println("     ===============");

		double points = m1points + m2points;
		System.out.print("\n ===============     ");
		System.out.print("Total Points:      ");
		System.out.printf("%4.1f / %d ", points, MAX_POINTS);
		System.out.println("     ===============");
	}
}
