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
public class EditTreeMilestone3Test {

	private static double m1points = 0;
	private static double m2points = 0;
	private static double m3points = 0;

	private static final int MAX_MILESTONE1 = 25;
	private static final int MAX_MILESTONE2 = 60;
	private static final int MAX_MILESTONE3 = 120;
	private static final int MAX_DESIRED_MILESTONE1 = 10;
	private static final int MAX_DESIRED_MILESTONE2 = 30;
	private static final int MAX_DESIRED_MILESTONE3 = 120;
	private static final int MAX_POINTS = MAX_DESIRED_MILESTONE1 + MAX_DESIRED_MILESTONE2 + MAX_DESIRED_MILESTONE3;

	private static final double m1weight = (double) MAX_DESIRED_MILESTONE1 / MAX_MILESTONE1;
	private static final double m2weight = (double) MAX_DESIRED_MILESTONE2 / MAX_MILESTONE2;
	private static final double m3weight = (double) MAX_DESIRED_MILESTONE3 / MAX_MILESTONE3; // 1

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
		Random random = new Random();
		for (int k = 0; k < NUM_NODES / SKIP_INTERVAL; k++) {
			for (int j = 0; j < 10; j++) {
				char toAdd = (char) ('0' + j);
				t.add(toAdd, random.nextInt(SKIP_INTERVAL * k + j + 1));
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

	// MILESTONE 3 TESTS
	// The tests for delete all assume that your add() methods work correctly.
	// This first set of tests should pass even if you don't rotate after you
	// delete. They do test to make sure you deleted the right nodes.
	// Then we test that the ranks are set correctly.
	// Then we test that rotations work.
	// Finally, we test the other couple methods in this milestone.

	/**
	 * Test method for {@link editortrees.EditTree#delete(int)}.
	 */
	@Test
	public void test300DeleteInt() {
		// Tests all the simplest rotation cases for delete
		// (causing each type of rotation)
		EditTree t = new EditTree();
		t.add('o');
		t.add('u');
		t.add('i', 0);
		t.add('e', 0);
		t.delete(3); // u

		assertEquals("eio", t.toString());
		m3points += m3weight;

		t.add('g', 1);
		t.delete(3); // o

		assertEquals("egi", t.toString());
		m3points += m3weight;

		t.add('o');
		t.delete(0); // e

		assertEquals("gio", t.toString());
		m3points += m3weight;

		t.add('k', 2);
		t.delete(0); // g

		assertEquals("iko", t.toString());
		m3points += m3weight;

		t.add('e', 0);
		t.delete(2); // k (root)

		assertEquals("eio", t.toString());
		m3points += m3weight;

		t.add('g', 1);
		t.delete(2); // i (root)

		assertEquals("ego", t.toString());
		m3points += m3weight;

		t.add('u');
		t.delete(1); // g (root)

		assertEquals("eou", t.toString());
		m3points += m3weight;

		t.add('m', 2);
		t.delete(1); // o (root)

		assertEquals("emu", t.toString());
		m3points += m3weight;
	}

	@Test
	public void test301DeleteSimpleLeaf() {
		EditTree t = new EditTree();

		t.add('d');
		t.add('b', 0);
		t.add('f');
		t.add('a', 0);
		t.add('c', 2);
		t.add('e', 4);
		t.add('g'); // three full levels in alphabetical order
//		t.show();
//		while (true);

		// Now delete the nodes in level order (by reverse level) so no
		// rotations happen

		t.delete(0); // a
		assertEquals("bcdefg", t.toString());
		assertEquals("[d2, b0, c0, f1, e0, g0]", t.toRankString());

		t.delete(1); // c
		assertEquals("bdefg", t.toString());
		assertEquals("[d1, b0, f1, e0, g0]", t.toRankString());

		t.delete(2); // e
		assertEquals("bdfg", t.toString());
		assertEquals("[d1, b0, f0, g0]", t.toRankString());

		t.delete(3); // g
		assertEquals("bdf", t.toString());
		assertEquals("[d1, b0, f0]", t.toRankString());

		t.delete(0); // b
		assertEquals("df", t.toString());
		assertEquals("[d0, f0]", t.toRankString());

		t.delete(1); // f
		assertEquals("d", t.toString());
		assertEquals("[d0]", t.toRankString());

		t.delete(0); // d
		assertEquals("", t.toString());
		assertEquals("[]", t.toRankString());

		m3points += m3weight;
	}

	@Test
	public void test306DeleteRootWithSingleChild() {
		// Root has left child
		EditTree t1 = new EditTree();
		t1.add('X');
		t1.add('a', 0);

		t1.delete(1);

		assertEquals("a", t1.toString());
		assertEquals("[a0]", t1.toRankString());
		m3points += m3weight;

		// Root has right child
		EditTree t2 = new EditTree();
		t2.add('X');
		t2.add('a');

		t2.delete(0);

		assertEquals("a", t2.toString());
		assertEquals("[a0]", t2.toRankString());
		m3points += m3weight;
	}

	@Test
	public void test307DeleteNodeWithSingleChild() {
		// Delete nodes with only left children from second level
		EditTree t1 = new EditTree();

		t1.add('b');
		t1.add('X', 0);
		t1.add('X');
		t1.add('a', 0);
		t1.add('c', 3);

		t1.delete(1);

		assertEquals("abcX", t1.toString());
		assertEquals("[b1, a0, X1, c0]", t1.toRankString());
		assertEquals(2, t1.slowHeight());

		t1.delete(3);

		assertEquals("abc", t1.toString());
		assertEquals("[b1, a0, c0]", t1.toRankString());
		assertEquals(1, t1.slowHeight());

		m3points += m3weight;

		// Delete nodes with only right children from second level
		EditTree t2 = new EditTree();
		t2.add('b');
		t2.add('X', 0);
		t2.add('X');
		t2.add('a', 1);
		t2.add('c');

		t2.delete(0);

		assertEquals("abXc", t2.toString());
		assertEquals("[b1, a0, X0, c0]", t2.toRankString());
		assertEquals(2, t2.slowHeight());

		t2.delete(2);

		assertEquals("abc", t2.toString());
		assertEquals("[b1, a0, c0]", t2.toRankString());
		assertEquals(1, t2.slowHeight());

		m3points += m3weight;

		// Delete nodes with only left children from third level
		EditTree t3 = new EditTree();

		t3.add('d');
		t3.add('b', 0);
		t3.add('f');
		t3.add('X', 0);
		t3.add('X', 2);
		t3.add('X', 4);
		t3.add('X');
		t3.add('a', 0);
		t3.add('c', 3);
		t3.add('e', 6);
		t3.add('g', 9);

		t3.delete(1);

		assertEquals("abcXdeXfgX", t3.toString());
		assertEquals("[d4, b1, a0, X1, c0, f2, X1, e0, X1, g0]", t3.toRankString());
		assertEquals(3, t3.slowHeight());

		t3.delete(3);

		assertEquals("abcdeXfgX", t3.toString());
		assertEquals("[d3, b1, a0, c0, f2, X1, e0, X1, g0]", t3.toRankString());
		assertEquals(3, t3.slowHeight());

		t3.delete(5);

		assertEquals("abcdefgX", t3.toString());
		assertEquals("[d3, b1, a0, c0, f1, e0, X1, g0]", t3.toRankString());
		assertEquals(3, t3.slowHeight());

		t3.delete(7);

		assertEquals("abcdefg", t3.toString());
		assertEquals("[d3, b1, a0, c0, f1, e0, g0]", t3.toRankString());
		assertEquals(2, t3.slowHeight());

		m3points += m3weight;

		// Delete nodes with only right children from third level
		EditTree t4 = new EditTree();

		t4.add('d');
		t4.add('b', 0);
		t4.add('f');
		t4.add('X', 0);
		t4.add('X', 2);
		t4.add('X', 4);
		t4.add('X');
		t4.add('a', 1);
		t4.add('c', 4);
		t4.add('e', 7);
		t4.add('g');

		t4.delete(0);

		assertEquals("abXcdXefXg", t4.toString());
		assertEquals("[d4, b1, a0, X0, c0, f2, X0, e0, X0, g0]", t4.toRankString());
		assertEquals(3, t4.slowHeight());

		t4.delete(2);

		assertEquals("abcdXefXg", t4.toString());
		assertEquals("[d3, b1, a0, c0, f2, X0, e0, X0, g0]", t4.toRankString());
		assertEquals(3, t4.slowHeight());

		t4.delete(4);

		assertEquals("abcdefXg", t4.toString());
		assertEquals("[d3, b1, a0, c0, f1, e0, X0, g0]", t4.toRankString());
		assertEquals(3, t4.slowHeight());

		t4.delete(6);

		assertEquals("abcdefg", t4.toString());
		assertEquals("[d3, b1, a0, c0, f1, e0, g0]", t4.toRankString());
		assertEquals(2, t4.slowHeight());

		m3points += m3weight;
	}

	@Test
	public void test308DeleteRootWithTwoChildrenNoRotation() {
		// Two levels
		EditTree t1 = new EditTree();

		t1.add('X');
		t1.add('a', 0);
		t1.add('b');

		t1.delete(1);

		assertEquals("ab", t1.toString());
		assertEquals("[b1, a0]", t1.toRankString());
		assertEquals(1, t1.slowHeight());

		m3points += m3weight;

		// Three levels
		EditTree t2 = new EditTree();
		t2.add('X');
		t2.add('b', 0);
		t2.add('e');
		t2.add('a', 0);
		t2.add('c', 2);
		t2.add('d', 4);
		t2.add('f');

		t2.delete(3);

		assertEquals("abcdef", t2.toString());
		assertEquals("[d3, b1, a0, c0, e0, f0]", t2.toRankString());
		assertEquals(2, t2.slowHeight());

		m3points += m3weight;
	}

	@Test
	public void test309DeleteNodeWithTwoChildrenNoRotation() {
		// Left subtree
		EditTree t1 = new EditTree();

		t1.add('c');
		t1.add('X', 0);
		t1.add('e');
		t1.add('a', 0);
		t1.add('b', 2);
		t1.add('d', 4);
		t1.add('f');

		t1.delete(1);

		assertEquals("abcdef", t1.toString());
		assertEquals("[c2, b1, a0, e1, d0, f0]", t1.toRankString());
		assertEquals(2, t1.slowHeight());

		m3points += m3weight;

		// Right subtree
		EditTree t2 = new EditTree();

		t2.add('d');
		t2.add('b', 0);
		t2.add('X');
		t2.add('a', 0);
		t2.add('c', 2);
		t2.add('e', 4);
		t2.add('f');

		t2.delete(5);

		assertEquals("abcdef", t2.toString());
		assertEquals("[d3, b1, a0, c0, f1, e0]", t2.toRankString());
		assertEquals(2, t2.slowHeight());

		m3points += m3weight;
	}

	@Test
	public void test310DeleteMultipleNodesWithTwoChildrenNoRotation() {
		EditTree t = new EditTree();

		t.add('X');
		t.add('X', 0);
		t.add('X');
		t.add('b', 0);
		t.add('e', 2);
		t.add('h', 4);
		t.add('k', 6);
		t.add('a', 0);
		t.add('c', 2);
		t.add('d', 4);
		t.add('f', 6);
		t.add('g', 8);
		t.add('i', 10);
		t.add('j', 12);
		t.add('l');

		t.delete(3);

		assertEquals("abcdefXghiXjkl", t.toString());
		assertEquals("[X6, d3, b1, a0, c0, e0, f0, X3, h1, g0, i0, k1, j0, l0]", t.toRankString());
		assertEquals(3, t.slowHeight());

		m3points += m3weight;

		t.delete(10);

		assertEquals("abcdefXghijkl", t.toString());
		assertEquals("[X6, d3, b1, a0, c0, e0, f0, j3, h1, g0, i0, k0, l0]", t.toRankString());
		assertEquals(3, t.slowHeight());

		m3points += m3weight;

		t.delete(6);

		assertEquals("abcdefghijkl", t.toString());
		assertEquals("[g6, d3, b1, a0, c0, e0, f0, j2, h0, i0, k0, l0]", t.toRankString());
		assertEquals(3, t.slowHeight());

		m3points += m3weight;
	}

	@Test
	public void test320DeleteInvalidNode() {
		EditTree t = new EditTree();

		try {
			t.delete(-1);
			fail("Did not throw IndexOutOfBoundsException for negative index");
		} catch (IndexOutOfBoundsException e) {
			// Success
		}

		try {
			t.delete(0);
			fail("Did not throw IndexOutOfBoundsException for index 0");
		} catch (IndexOutOfBoundsException e) {
			// Success
		}

		t.add('b');

		try {
			t.delete(-1);
			fail("Did not throw IndexOutOfBoundsException for negative index");
		} catch (IndexOutOfBoundsException e) {
			// Success
		}

		try {
			t.delete(1);
			fail("Did not throw IndexOutOfBoundsExcpeption for index 1");
		} catch (IndexOutOfBoundsException e) {
			// Success
		}

		t.add('a', 0);
		t.add('c');

		try {
			t.delete(-1);
			fail("Did not throw IndexOutOfBoundsException for negative index");
		} catch (IndexOutOfBoundsException e) {
			// Success
		}

		try {
			t.delete(3);
			fail("Did not throw IndexOutOfBoundsExcpetion for index 3");
		} catch (IndexOutOfBoundsException e) {
			// Success
		}

		try {
			t.delete(230);
			fail("Did not throw IndexOutOfBoundsException for index 230");
		} catch (IndexOutOfBoundsException e) {
			// Success
		}

		m3points += m3weight;
	}

	// MILESTONE 3 FULL
	// Tests repeated again checking balancing as well.

	/**
	 * Test method for {@link editortrees.EditTree#delete(int)}.
	 */
	@Test
	public void test350DeleteInt() {
		// Tests all the simplest rotation cases for delete (causing each type
		// of rotation)
		EditTree t = new EditTree();
		t.add('o');
		t.add('u');
		t.add('i', 0);
		t.add('e', 0);
		t.delete(3); // u
		assertEquals("eio", t.toString());
		assertEquals("[i1=, e0=, o0=]", t.toDebugString());
		assertEquals(1, t.slowHeight());
		assertEquals(1, t.totalRotationCount());

		m3points += m3weight;

		t.add('g', 1);
		t.delete(3); // o
		assertEquals("egi", t.toString());
		assertEquals("[g1=, e0=, i0=]", t.toDebugString());
		assertEquals(1, t.slowHeight());
		assertEquals(3, t.totalRotationCount());

		m3points += m3weight;

		t.add('o');
		t.delete(0); // e
		assertEquals("gio", t.toString());
		assertEquals("[i1=, g0=, o0=]", t.toDebugString());
		assertEquals(1, t.slowHeight());
		assertEquals(4, t.totalRotationCount());

		m3points += m3weight;

		t.add('k', 2);
		t.delete(0); // g
		assertEquals("iko", t.toString());
		assertEquals("[k1=, i0=, o0=]", t.toDebugString());
		assertEquals(1, t.slowHeight());
		assertEquals(6, t.totalRotationCount());

		m3points += m3weight;

		t.add('e', 0);
		t.delete(2); // k (root)
		assertEquals("eio", t.toString());
		assertEquals("[i1=, e0=, o0=]", t.toDebugString());
		assertEquals(1, t.slowHeight());
		// CONSIDER: Change to 7 only if delete successor of node with 2
		// children
		assertTrue("Expected: true", 7 == t.totalRotationCount() || 6 == t.totalRotationCount());

		m3points += m3weight;

		t.add('g', 1);
		t.delete(2); // i (root)
		assertEquals("ego", t.toString());
		assertEquals("[g1=, e0=, o0=]", t.toDebugString());
		assertEquals(1, t.slowHeight());

		// CONSIDER: Change to 9 only if delete successor of node with 2
		// children
		assertTrue("Expected: true", 9 == t.totalRotationCount() || 6 == t.totalRotationCount());

		m3points += m3weight;

		t.add('u');
		t.delete(1); // g (root)
		assertEquals("eou", t.toString());
		assertEquals("[o1=, e0=, u0=]", t.toDebugString());
		assertEquals(1, t.slowHeight());

		t.add('m', 2);
		t.delete(1); // o (root)
		assertEquals("emu", t.toString());
		assertEquals("[m1=, e0=, u0=]", t.toDebugString());
		assertEquals(1, t.slowHeight());

		m3points += m3weight;
	}

	@Test
	public void test351DeleteSimpleLeaf() {
		EditTree t = new EditTree();

		t.add('d');
		t.add('b', 0);
		t.add('f');
		t.add('a', 0);
		t.add('c', 2);
		t.add('e', 4);
		t.add('g'); // three full levels in alphabetical order

		// Now delete the nodes in level order (by reverse level) so no
		// rotations happen

		t.delete(0); // a
		assertEquals("bcdefg", t.toString());
		assertEquals("[d2=, b0\\, c0=, f1=, e0=, g0=]", t.toDebugString());
		assertEquals(2, t.slowHeight());

		t.delete(1); // c
		assertEquals("bdefg", t.toString());
		assertEquals("[d1\\, b0=, f1=, e0=, g0=]", t.toDebugString());
		assertEquals(2, t.slowHeight());

		t.delete(2); // e
		assertEquals("bdfg", t.toString());
		assertEquals("[d1\\, b0=, f0\\, g0=]", t.toDebugString());
		assertEquals(2, t.slowHeight());

		t.delete(3); // g
		assertEquals("bdf", t.toString());
		assertEquals("[d1=, b0=, f0=]", t.toDebugString());
		assertEquals(1, t.slowHeight());

		t.delete(0); // b
		assertEquals("df", t.toString());
		assertEquals("[d0\\, f0=]", t.toDebugString());
		assertEquals(1, t.slowHeight());

		t.delete(1); // f
		assertEquals("d", t.toString());
		assertEquals("[d0=]", t.toDebugString());
		assertEquals(0, t.slowHeight());

		t.delete(0); // d
		assertEquals("", t.toString());
		assertEquals("[]", t.toDebugString());
		assertEquals(-1, t.slowHeight());

		m3points += m3weight;
	}

	@Test
	public void test352DeleteLeafCausingSingleRightRotation() {
		// Cause a single right rotation at the root
		EditTree t1 = new EditTree();

		t1.add('c');
		t1.add('b', 0);
		t1.add('X');
		t1.add('a', 0);

		t1.delete(3);

		assertEquals("abc", t1.toString());
		assertEquals("[b1=, a0=, c0=]", t1.toDebugString());
		assertEquals(1, t1.slowHeight());

		m3points += m3weight;

		// Cause a single right rotation on the left subtree
		EditTree t2 = new EditTree();
		t2.add('d');
		t2.add('c', 0);
		t2.add('f');
		t2.add('b', 0);
		t2.add('X', 2);
		t2.add('e', 4);
		t2.add('g');
		t2.add('a', 0);

//		t2.show();
//		while (true);

		t2.delete(3);
		assertEquals("abcdefg", t2.toString());
		assertEquals("[d3=, b1=, a0=, c0=, f1=, e0=, g0=]", t2.toDebugString());
		assertEquals(2, t2.slowHeight());

		// Cause a single right rotation on the right subtree
		EditTree t3 = new EditTree();
		t3.add('d');
		t3.add('b', 0);
		t3.add('g');
		t3.add('a', 0);
		t3.add('c', 2);
		t3.add('f', 4);
		t3.add('X');
		t3.add('e', 4);

		t3.delete(7);

		assertEquals("abcdefg", t3.toString());
		assertEquals("[d3=, b1=, a0=, c0=, f1=, e0=, g0=]", t3.toDebugString());
		assertEquals(2, t3.slowHeight());

		m3points += m3weight;
	}

	@Test
	public void test353DeleteLeafCausingSingleLeftRotation() {
		// Cause a single left rotation at the root
		EditTree t1 = new EditTree();
		t1.add('a');
		t1.add('X', 0);
		t1.add('b');
		t1.add('c');

		t1.delete(0);

		assertEquals("abc", t1.toString());
		assertEquals("[b1=, a0=, c0=]", t1.toDebugString());
		assertEquals(1, t1.slowHeight());

		m3points += m3weight;

		// Cause a single left rotation on the right subtree
		EditTree t2 = new EditTree();
		t2.add('d');
		t2.add('b', 0);
		t2.add('e');
		t2.add('a', 0);
		t2.add('c', 2);
		t2.add('x', 4);
		t2.add('f');
		t2.add('g');

		t2.delete(4);

		assertEquals("abcdefg", t2.toString());
		assertEquals("[d3=, b1=, a0=, c0=, f1=, e0=, g0=]", t2.toDebugString());
		assertEquals(2, t2.slowHeight());

		// Cause a single left rotation on the left subtree
		EditTree t3 = new EditTree();
		t3.add('d');
		t3.add('a', 0);
		t3.add('f');
		t3.add('X', 0);
		t3.add('b', 2);
		t3.add('e', 4);
		t3.add('g', 6);
		t3.add('c', 3);

		t3.delete(0);

		assertEquals("abcdefg", t3.toString());
		assertEquals("[d3=, b1=, a0=, c0=, f1=, e0=, g0=]", t3.toDebugString());
		assertEquals(2, t3.slowHeight());

		m3points += m3weight;
	}

	@Test
	public void test354DeleteLeafCausingDoubleRightRotation() {
		// Cause a double right rotation at the root
		EditTree t1 = new EditTree();

		t1.add('c');
		t1.add('a', 0);
		t1.add('X');
		t1.add('b', 1);

		t1.delete(3);

		assertEquals("abc", t1.toString());
		assertEquals("[b1=, a0=, c0=]", t1.toDebugString());
		assertEquals(1, t1.slowHeight());

		m3points += m3weight;

		// Cause a double right rotation on the left subtree
		EditTree t2 = new EditTree();

		t2.add('d');
		t2.add('c', 0);
		t2.add('f');
		t2.add('a', 0);
		t2.add('X', 2);
		t2.add('e', 4);
		t2.add('g');
		t2.add('b', 1);

		t2.delete(3);

		assertEquals("abcdefg", t2.toString());
		assertEquals("[d3=, b1=, a0=, c0=, f1=, e0=, g0=]", t2.toDebugString());
		assertEquals(2, t2.slowHeight());

		// Cause a double right rotation on the right subtree
		EditTree t3 = new EditTree();

		t3.add('d');
		t3.add('a', 0);
		t3.add('f', 2);
		t3.add('X', 0);
		t3.add('c', 2);
		t3.add('e', 4);
		t3.add('g', 6);
		t3.add('b', 2);

		t3.delete(0);

		assertEquals("abcdefg", t3.toString());
		assertEquals("[d3=, b1=, a0=, c0=, f1=, e0=, g0=]", t3.toDebugString());
		assertEquals(2, t3.slowHeight());

		m3points += m3weight;
	}

	@Test
	public void test355DeleteLeafCausingDoubleLeftRotation() {
		// Cause a double left rotation at the root
		EditTree t1 = new EditTree();

		t1.add('a');
		t1.add('X', 0);
		t1.add('c');
		t1.add('b', 2);

		t1.delete(0);

		assertEquals("abc", t1.toString());
		assertEquals("[b1=, a0=, c0=]", t1.toDebugString());
		assertEquals(1, t1.slowHeight());

		m3points += m3weight;

		// Cause a double left rotation on the right subtree
		EditTree t2 = new EditTree();
		t2.add('d');
		t2.add('b', 0);
		t2.add('e');
		t2.add('a', 0);
		t2.add('c', 2);
		t2.add('X', 4);
		t2.add('g');
		t2.add('f', 6);

		t2.delete(4);

		assertEquals("abcdefg", t2.toString());
		assertEquals("[d3=, b1=, a0=, c0=, f1=, e0=, g0=]", t2.toDebugString());
		assertEquals(2, t2.slowHeight());

		// Cause a double left rotation on the left subtree
		EditTree t3 = new EditTree();
		t3.add('d');
		t3.add('a', 0);
		t3.add('f');
		t3.add('X', 0);
		t3.add('c', 2);
		t3.add('e', 4);
		t3.add('g', 6);
		t3.add('b', 2);

		t3.delete(0);

		assertEquals("abcdefg", t3.toString());
		assertEquals("[d3=, b1=, a0=, c0=, f1=, e0=, g0=]", t3.toDebugString());
		assertEquals(2, t3.slowHeight());

		m3points += m3weight;
	}

	@Test
	public void test356DeleteRootWithSingleChild() {
		// Root has left child
		EditTree t1 = new EditTree();

		t1.add('X');
		t1.add('a', 0);

		t1.delete(1);

		assertEquals("a", t1.toString());
		assertEquals("[a0=]", t1.toDebugString());
		assertEquals(0, t1.slowHeight());

		m3points += m3weight;

		// Root has right child
		EditTree t2 = new EditTree();

		t2.add('X');
		t2.add('a');

		t2.delete(0);

		assertEquals("a", t2.toString());
		assertEquals("[a0=]", t2.toDebugString());
		assertEquals(0, t2.slowHeight());

		m3points += m3weight;
	}

	@Test
	public void test357DeleteNodeWithSingleChild() {
		// Delete nodes with only left children from second level
		EditTree t1 = new EditTree();

		t1.add('b');
		t1.add('X', 0);
		t1.add('X');
		t1.add('a', 0);
		t1.add('c', 3);

		t1.delete(1);

		assertEquals("abcX", t1.toString());
		assertEquals("[b1\\, a0=, X1/, c0=]", t1.toDebugString());
		assertEquals(2, t1.slowHeight());

		t1.delete(3);

		assertEquals("abc", t1.toString());
		assertEquals("[b1=, a0=, c0=]", t1.toDebugString());
		assertEquals(1, t1.slowHeight());

		m3points += m3weight;

		// Delete nodes with only right children from second level
		EditTree t2 = new EditTree();
		t2.add('b');
		t2.add('X', 0);
		t2.add('X');
		t2.add('a', 1);
		t2.add('c');

		t2.delete(0);

		assertEquals("abXc", t2.toString());
		assertEquals("[b1\\, a0=, X0\\, c0=]", t2.toDebugString());
		assertEquals(2, t2.slowHeight());

		t2.delete(2);

		assertEquals("abc", t2.toString());
		assertEquals("[b1=, a0=, c0=]", t2.toDebugString());
		assertEquals(1, t2.slowHeight());

		m3points += m3weight;

		// Delete nodes with only left children from third level
		EditTree t3 = new EditTree();

		t3.add('d');
		t3.add('b', 0);
		t3.add('f');
		t3.add('X', 0);
		t3.add('X', 2);
		t3.add('X', 4);
		t3.add('X');
		t3.add('a', 0);
		t3.add('c', 3);
		t3.add('e', 6);
		t3.add('g', 9);

		t3.delete(1);

		assertEquals("abcXdeXfgX", t3.toString());
		assertEquals("[d4=, b1\\, a0=, X1/, c0=, f2=, X1/, e0=, X1/, g0=]", t3.toDebugString());
		assertEquals(3, t3.slowHeight());

		t3.delete(3);

		assertEquals("abcdeXfgX", t3.toString());
		assertEquals("[d3\\, b1=, a0=, c0=, f2=, X1/, e0=, X1/, g0=]", t3.toDebugString());
		assertEquals(3, t3.slowHeight());

		t3.delete(5);

		assertEquals("abcdefgX", t3.toString());
		assertEquals("[d3\\, b1=, a0=, c0=, f1\\, e0=, X1/, g0=]", t3.toDebugString());
		assertEquals(3, t3.slowHeight());

		t3.delete(7);

		assertEquals("abcdefg", t3.toString());
		assertEquals("[d3=, b1=, a0=, c0=, f1=, e0=, g0=]", t3.toDebugString());
		assertEquals(2, t3.slowHeight());

		m3points += m3weight;

		// Delete nodes with only right children from third level
		EditTree t4 = new EditTree();

		t4.add('d');
		t4.add('b', 0);
		t4.add('f');
		t4.add('X', 0);
		t4.add('X', 2);
		t4.add('X', 4);
		t4.add('X');
		t4.add('a', 1);
		t4.add('c', 4);
		t4.add('e', 7);
		t4.add('g');

		t4.delete(0);

		assertEquals("abXcdXefXg", t4.toString());
		assertEquals("[d4=, b1\\, a0=, X0\\, c0=, f2=, X0\\, e0=, X0\\, g0=]", t4.toDebugString());
		assertEquals(3, t4.slowHeight());

		t4.delete(2);

		assertEquals("abcdXefXg", t4.toString());
		assertEquals("[d3\\, b1=, a0=, c0=, f2=, X0\\, e0=, X0\\, g0=]", t4.toDebugString());
		assertEquals(3, t4.slowHeight());

		t4.delete(4);

		assertEquals("abcdefXg", t4.toString());
		assertEquals("[d3\\, b1=, a0=, c0=, f1\\, e0=, X0\\, g0=]", t4.toDebugString());
		assertEquals(3, t4.slowHeight());

		t4.delete(6);

		assertEquals("abcdefg", t4.toString());
		assertEquals("[d3=, b1=, a0=, c0=, f1=, e0=, g0=]", t4.toDebugString());
		assertEquals(2, t4.slowHeight());

		m3points += m3weight;
	}

	@Test
	public void test358DeleteRootWithTwoChildrenNoRotation() {
		// Two levels
		EditTree t1 = new EditTree();

		t1.add('X');
		t1.add('a', 0);
		t1.add('b');

		t1.delete(1);

		assertEquals("ab", t1.toString());
		assertEquals("[b1/, a0=]", t1.toDebugString());
		assertEquals(1, t1.slowHeight());

		m3points += m3weight;

		// Three levels
		EditTree t2 = new EditTree();
		t2.add('X');
		t2.add('b', 0);
		t2.add('e');
		t2.add('a', 0);
		t2.add('c', 2);
		t2.add('d', 4);
		t2.add('f');

		t2.delete(3);

		assertEquals("abcdef", t2.toString());
		assertEquals("[d3=, b1=, a0=, c0=, e0\\, f0=]", t2.toDebugString());
		assertEquals(2, t2.slowHeight());

		m3points += m3weight;
	}

	@Test
	public void test359DeleteNodeWithTwoChildrenNoRotation() {
		// Left subtree
		EditTree t1 = new EditTree();

		t1.add('c');
		t1.add('X', 0);
		t1.add('e');
		t1.add('a', 0);
		t1.add('b', 2);
		t1.add('d', 4);
		t1.add('f');

		t1.delete(1);

		assertEquals("abcdef", t1.toString());
		assertEquals("[c2=, b1/, a0=, e1=, d0=, f0=]", t1.toDebugString());
		assertEquals(2, t1.slowHeight());

		m3points += m3weight;

		// Right subtree
		EditTree t2 = new EditTree();

		t2.add('d');
		t2.add('b', 0);
		t2.add('X');
		t2.add('a', 0);
		t2.add('c', 2);
		t2.add('e', 4);
		t2.add('f');

		t2.delete(5);

		assertEquals("abcdef", t2.toString());
		assertEquals("[d3=, b1=, a0=, c0=, f1/, e0=]", t2.toDebugString());
		assertEquals(2, t2.slowHeight());

		m3points += m3weight;
	}

	@Test
	public void test360DeleteMultipleNodesWithTwoChildrenNoRotation() {
		EditTree t = new EditTree();

		t.add('X');
		t.add('X', 0);
		t.add('X');
		t.add('b', 0);
		t.add('e', 2);
		t.add('h', 4);
		t.add('k', 6);
		t.add('a', 0);
		t.add('c', 2);
		t.add('d', 4);
		t.add('f', 6);
		t.add('g', 8);
		t.add('i', 10);
		t.add('j', 12);
		t.add('l');

		t.delete(3);

		assertEquals("abcdefXghiXjkl", t.toString());
		assertEquals("[X6=, d3=, b1=, a0=, c0=, e0\\, f0=, X3=, h1=, g0=, i0=, k1=, j0=, l0=]", t.toDebugString());
		assertEquals(3, t.slowHeight());

		m3points += m3weight;

		t.delete(10);

		assertEquals("abcdefXghijkl", t.toString());
		assertEquals("[X6=, d3=, b1=, a0=, c0=, e0\\, f0=, j3=, h1=, g0=, i0=, k0\\, l0=]", t.toDebugString());
		assertEquals(3, t.slowHeight());

		m3points += m3weight;

		t.delete(6);

		assertEquals("abcdefghijkl", t.toString());
		assertEquals("[g6=, d3=, b1=, a0=, c0=, e0\\, f0=, j2=, h0\\, i0=, k0\\, l0=]", t.toDebugString());
		assertEquals(3, t.slowHeight());

		m3points += m3weight;
	}

	@Test
	public void test370DeleteRootWithTwoChildrenCausingSingleRotation() {
		// Two level tree, rotation caused if substitution happens from right
		// subtree
		EditTree t1 = new EditTree();

		t1.add('X');
		t1.add('b', 0);
		t1.add('c');
		t1.add('a', 0);

		t1.delete(2);

		assertEquals("abc", t1.toString());
		assertEquals("[b1=, a0=, c0=]", t1.toDebugString());
		assertEquals(1, t1.slowHeight());

		m3points += m3weight;

		// Two level tree, rotation caused if substitution happens from left
		// subtree
		EditTree t2 = new EditTree();

		t2.add('X');
		t2.add('a', 0);
		t2.add('b');
		t2.add('c');

		t2.delete(1);

		assertEquals("abc", t2.toString());
		assertEquals("[b1=, a0=, c0=]", t2.toDebugString());
		assertEquals(1, t2.slowHeight());

		m3points += m3weight;

		// Three level tree, rotation caused if substitution happens from right
		// subtree
		EditTree t3 = new EditTree();

		t3.add('X');
		t3.add('b', 0);
		t3.add('e');
		t3.add('a', 0);
		t3.add('c', 2);
		t3.add('d', 4);
		t3.add('f');
		t3.add('g');

		t3.delete(3);

		assertEquals("abcdefg", t3.toString());
		assertEquals("[d3=, b1=, a0=, c0=, f1=, e0=, g0=]", t3.toDebugString());
		assertTrue("Expected: true", t3.slowHeight() == 2 || t3.slowHeight() == 3);

		m3points += m3weight;

		// Three level tree, rotation caused if substitution happens from left
		// subtree
		EditTree t4 = new EditTree();

		t4.add('X');
		t4.add('c', 0);
		t4.add('f');
		t4.add('b', 0);
		t4.add('d', 2);
		t4.add('e', 4);
		t4.add('g');
		t4.add('a', 0);

		t4.delete(4);

		assertEquals("abcdefg", t4.toString());
		assertEquals("[e4/, c2/, b1/, a0=, d0=, f0\\, g0=]", t4.toDebugString());
		assertTrue("Expected: true", t4.slowHeight() == 2 || t4.slowHeight() == 3);

		m3points += m3weight;
	}

	@Test
	public void test371DeleteRootWithTwoChildrenCausingDoubleRotation() {
		// Two level tree, rotation caused if substitution happens from right
		// subtree
		EditTree t1 = new EditTree();

		t1.add('X');
		t1.add('a', 0);
		t1.add('c');
		t1.add('b', 1);

		t1.delete(2);

		assertEquals("abc", t1.toString());
		assertEquals("[b1=, a0=, c0=]", t1.toDebugString());
		assertEquals(1, t1.slowHeight());

		m3points += m3weight;

		// Two level tree, rotation caused if substitution happens from left
		// subtree
		EditTree t2 = new EditTree();

		t2.add('X');
		t2.add('a', 0);
		t2.add('c');
		t2.add('b', 2);

		t2.delete(1);

		assertEquals("abc", t2.toString());
		assertEquals("[b1=, a0=, c0=]", t2.toDebugString());
		assertEquals(1, t2.slowHeight());

		m3points += m3weight;

		// Three level tree, rotation caused if substitution happens from right
		// subtree
		EditTree t3 = new EditTree();

		t3.add('X');
		t3.add('b', 0);
		t3.add('e');
		t3.add('a', 0);
		t3.add('c', 2);
		t3.add('d', 4);
		t3.add('g');
		t3.add('f', 6);

		t3.delete(3);

		assertEquals("abcdefg", t3.toString());
		assertEquals("[d3=, b1=, a0=, c0=, f1=, e0=, g0=]", t3.toDebugString());
		assertTrue("Expected: true", t3.slowHeight() == 2 || t3.slowHeight() == 3);

		m3points += m3weight;

		// Three level tree, rotation caused if substitution happens from left
		// subtree
		EditTree t4 = new EditTree();

		t4.add('X');
		t4.add('c', 0);
		t4.add('f');
		t4.add('a', 0);
		t4.add('d', 2);
		t4.add('e', 4);
		t4.add('g');
		t4.add('b', 1);

		t4.delete(4);

		assertEquals("abcdefg", t4.toString());
		assertEquals("[e4/, c2/, a0\\, b0=, d0=, f0\\, g0=]", t4.toDebugString());
		assertTrue("Expected: true", t4.slowHeight() == 2 || t4.slowHeight() == 3);

		m3points += m3weight;
	}

	@Test
	public void test372DeleteNodeWithTwoChildrenCausingSingleRotation() {
		// Rotation caused if substitution happens from right subtree
		EditTree t1 = new EditTree();

		t1.add('d');
		t1.add('b', 0);
		t1.add('X');
		t1.add('a', 0);
		t1.add('c', 2);
		t1.add('f', 4);
		t1.add('g', 6);
		t1.add('e', 4);

		t1.delete(6);

		assertEquals("abcdefg", t1.toString());
		assertEquals("[d3=, b1=, a0=, c0=, f1=, e0=, g0=]", t1.toDebugString());
		assertEquals(2, t1.slowHeight());

		m3points += m3weight;

		// Rotation caused if substitution happens from left subtree
		EditTree t2 = new EditTree();

		t2.add('d');
		t2.add('X', 0);
		t2.add('f');
		t2.add('a', 0);
		t2.add('b', 2);
		t2.add('e', 4);
		t2.add('g');
		t2.add('c', 3);

		t2.delete(1);

		assertEquals("abcdefg", t2.toString());
		assertEquals("[d3=, b1=, a0=, c0=, f1=, e0=, g0=]", t2.toDebugString());
		assertEquals(2, t2.slowHeight());

		m3points += m3weight;
	}

	@Test
	public void test373DeleteNodeWithTwoChildrenCausingDoubleRotation() {
		// Rotation caused if substitution happens from right subtree
		EditTree t1 = new EditTree();

		t1.add('d');
		t1.add('b', 0);
		t1.add('X');
		t1.add('a', 0);
		t1.add('c', 2);
		t1.add('e', 4);
		t1.add('g');
		t1.add('f', 5);

		t1.delete(6);

		assertEquals("abcdefg", t1.toString());
		assertEquals("[d3=, b1=, a0=, c0=, f1=, e0=, g0=]", t1.toDebugString());
		assertEquals(2, t1.slowHeight());

		m3points += m3weight;

		// Rotation caused if substitution happens from left subtree
		EditTree t2 = new EditTree();

		t2.add('d');
		t2.add('X', 0);
		t2.add('f');
		t2.add('a', 0);
		t2.add('c', 2);
		t2.add('e', 4);
		t2.add('g', 6);
		t2.add('b', 2);

		t2.delete(1);

		assertEquals("abcdefg", t2.toString());
		assertEquals("[d3=, b1=, a0=, c0=, f1=, e0=, g0=]", t2.toDebugString());
		assertEquals(2, t2.slowHeight());

		m3points += m3weight;
	}

	@Test
	public void test380DeleteCausingTwoRotationsStartingWithSingleLeft() {
		// single left => single left
		EditTree t1 = new EditTree();

		t1.add('d');
		t1.add('a', 0);
		t1.add('h');
		t1.add('X', 0);
		t1.add('b', 2);
		t1.add('f', 4);
		t1.add('j');
		t1.add('c', 3);
		t1.add('e', 5);
		t1.add('g', 7);
		t1.add('i', 9);
		t1.add('k');
		t1.add('l');
		t1.delete(0);

		assertEquals("abcdefghijkl", t1.toString());
		assertEquals("[h7=, d3=, b1=, a0=, c0=, f1=, e0=, g0=, j1\\, i0=, k0\\, l0=]", t1.toDebugString());
		assertEquals(3, t1.slowHeight());

		m3points += m3weight;

		// single left => single right
		EditTree t2 = new EditTree();

		t2.add('i');
		t2.add('e', 0);
		t2.add('j');
		t2.add('c', 0);
		t2.add('g', 2);
		t2.add('X', 4);
		t2.add('k');
		t2.add('b', 0);
		t2.add('d', 2);
		t2.add('f', 4);
		t2.add('h', 6);
		t2.add('l');
		t2.add('a', 0);

		t2.delete(9);

		assertEquals("abcdefghijkl", t2.toString());
		assertEquals("[e4=, c2/, b1/, a0=, d0=, i3=, g1=, f0=, h0=, k1=, j0=, l0=]", t2.toDebugString());
		assertEquals(3, t2.slowHeight());

		m3points += m3weight;

		// single left => double left
		EditTree t3 = new EditTree();

		t3.add('d');
		t3.add('a', 0);
		t3.add('i');
		t3.add('X', 0);
		t3.add('b', 2);
		t3.add('g', 4);
		t3.add('k');
		t3.add('c', 3);
		t3.add('f', 5);
		t3.add('h', 7);
		t3.add('j', 9);
		t3.add('l');
		t3.add('e', 5);

		t3.delete(0);

		assertEquals("abcdefghijkl", t3.toString());
		assertEquals("[g6=, d3=, b1=, a0=, c0=, f1/, e0=, i1\\, h0=, k1=, j0=, l0=]", t3.toDebugString());
		assertEquals(3, t3.slowHeight());

		m3points += m3weight;

		// single left => double right
		EditTree t4 = new EditTree();

		t4.add('i');
		t4.add('d', 0);
		t4.add('j');
		t4.add('b', 0);
		t4.add('f', 2);
		t4.add('X', 4);
		t4.add('k');
		t4.add('a', 0);
		t4.add('c', 2);
		t4.add('e', 4);
		t4.add('g', 6);
		t4.add('l');
		t4.add('h', 7);

		t4.delete(9);

		assertEquals("abcdefghijkl", t4.toString());
		assertEquals("[f5=, d3/, b1=, a0=, c0=, e0=, i2=, g0\\, h0=, k1=, j0=, l0=]", t4.toDebugString());
		assertEquals(3, t4.slowHeight());

		m3points += m3weight;
	}

	@Test
	public void test381DeleteCausingTwoRotationsStartingWithSingleRight() {
		// single right => single right
		EditTree t1 = new EditTree();

		t1.add('i');
		t1.add('e', 0);
		t1.add('l');
		t1.add('c', 0);
		t1.add('g', 2);
		t1.add('k', 4);
		t1.add('X');
		t1.add('b', 0);
		t1.add('d', 2);
		t1.add('f', 4);
		t1.add('h', 6);
		t1.add('j', 8);
		t1.add('a', 0);

		t1.delete(12);

		assertEquals("abcdefghijkl", t1.toString());
		assertEquals("[e4=, c2/, b1/, a0=, d0=, i3=, g1=, f0=, h0=, k1=, j0=, l0=]", t1.toDebugString());
		assertEquals(3, t1.slowHeight());

		m3points += m3weight;

		// single right => single left
		EditTree t2 = new EditTree();

		t2.add('d');
		t2.add('c', 0);
		t2.add('h');
		t2.add('b', 0);
		t2.add('X', 2);
		t2.add('f', 4);
		t2.add('j');
		t2.add('a', 0);
		t2.add('e', 5);
		t2.add('g', 7);
		t2.add('i', 9);
		t2.add('k');
		t2.add('l');

		t2.delete(3);

		assertEquals("abcdefghijkl", t2.toString());
		assertEquals("[h7=, d3=, b1=, a0=, c0=, f1=, e0=, g0=, j1\\, i0=, k0\\, l0=]", t2.toDebugString());
		assertEquals(3, t2.slowHeight());

		m3points += m3weight;

		// single right => double right
		EditTree t3 = new EditTree();
		t3.add('i');
		t3.add('d', 0);
		t3.add('l');
		t3.add('b', 0);
		t3.add('f', 2);
		t3.add('k', 4);
		t3.add('X');
		t3.add('a', 0);
		t3.add('c', 2);
		t3.add('e', 4);
		t3.add('g', 6);
		t3.add('j', 8);
		t3.add('h', 7);

		t3.delete(12);

		assertEquals("abcdefghijkl", t3.toString());
		assertEquals("[f5=, d3/, b1=, a0=, c0=, e0=, i2=, g0\\, h0=, k1=, j0=, l0=]", t3.toDebugString());
		assertEquals(3, t3.slowHeight());
		assertEquals(3, t3.totalRotationCount());
		m3points += m3weight;

		// single right => double left
		EditTree t4 = new EditTree();
		t4.add('d');
		t4.add('c', 0);
		t4.add('i');
		t4.add('b', 0);
		t4.add('X', 2);
		t4.add('g', 4);
		t4.add('k');
		t4.add('a', 0);
		t4.add('f', 5);
		t4.add('h', 7);
		t4.add('j', 9);
		t4.add('l');
		t4.add('e', 5);

		t4.delete(3);

		assertEquals("abcdefghijkl", t4.toString());
		assertEquals("[g6=, d3=, b1=, a0=, c0=, f1/, e0=, i1\\, h0=, k1=, j0=, l0=]", t4.toDebugString());
		assertEquals(3, t4.slowHeight());

		m3points += m3weight;
	}

	@Test
	public void test382DeleteCausingTwoRotationsStartingWithDoubleLeft() {
		// double left => single left
		EditTree t1 = new EditTree();

		t1.add('d');
		t1.add('a', 0);
		t1.add('h');
		t1.add('X', 0);
		t1.add('c', 2);
		t1.add('f', 4);
		t1.add('j');
		t1.add('b', 2);
		t1.add('e', 5);
		t1.add('g', 7);
		t1.add('i', 9);
		t1.add('k');
		t1.add('l');

		t1.delete(0);

		assertEquals("abcdefghijkl", t1.toString());
		assertEquals("[h7=, d3=, b1=, a0=, c0=, f1=, e0=, g0=, j1\\, i0=, k0\\, l0=]", t1.toDebugString());
		assertEquals(3, t1.slowHeight());

		m3points += m3weight;

		// double left => single right
		EditTree t2 = new EditTree();

		t2.add('i');
		t2.add('e', 0);
		t2.add('j');
		t2.add('c', 0);
		t2.add('g', 2);
		t2.add('X', 4);
		t2.add('l');
		t2.add('b', 0);
		t2.add('d', 2);
		t2.add('f', 4);
		t2.add('h', 6);
		t2.add('k', 10);
		t2.add('a', 0);

		t2.delete(9);

		assertEquals("abcdefghijkl", t2.toString());
		assertEquals("[e4=, c2/, b1/, a0=, d0=, i3=, g1=, f0=, h0=, k1=, j0=, l0=]", t2.toDebugString());
		assertEquals(3, t2.slowHeight());

		m3points += m3weight;

		// double left => double left
		EditTree t3 = new EditTree();

		t3.add('d');
		t3.add('a', 0);
		t3.add('i');
		t3.add('X', 0);
		t3.add('c', 2);
		t3.add('g', 4);
		t3.add('k');
		t3.add('b', 2);
		t3.add('f', 5);
		t3.add('h', 7);
		t3.add('j', 9);
		t3.add('l');
		t3.add('e', 5);

		t3.delete(0);

		assertEquals("abcdefghijkl", t3.toString());
		assertEquals("[g6=, d3=, b1=, a0=, c0=, f1/, e0=, i1\\, h0=, k1=, j0=, l0=]", t3.toDebugString());
		assertEquals(3, t3.slowHeight());

		m3points += m3weight;

		// double left => double right
		EditTree t4 = new EditTree();

		t4.add('i');
		t4.add('d', 0);
		t4.add('j');
		t4.add('b', 0);
		t4.add('f', 2);
		t4.add('X', 4);
		t4.add('l');
		t4.add('a', 0);
		t4.add('c', 2);
		t4.add('e', 4);
		t4.add('g', 6);
		t4.add('k', 10);
		t4.add('h', 7);

		t4.delete(9);

		assertEquals("abcdefghijkl", t4.toString());
		assertEquals("[f5=, d3/, b1=, a0=, c0=, e0=, i2=, g0\\, h0=, k1=, j0=, l0=]", t4.toDebugString());
		assertEquals(3, t4.slowHeight());

		m3points += m3weight;
	}

	@Test
	public void test383DeleteCausingTwoRotationsStartingWithDoubleRight() {
		// double right => single right
		EditTree t1 = new EditTree();
		t1.add('i');
		t1.add('e', 0);
		t1.add('l');
		t1.add('c', 0);
		t1.add('g', 2);
		t1.add('j', 4);
		t1.add('X');
		t1.add('b', 0);
		t1.add('d', 2);
		t1.add('f', 4);
		t1.add('h', 6);
		t1.add('k', 9);
		t1.add('a', 0);

		t1.delete(12);

		assertEquals("abcdefghijkl", t1.toString());
		assertEquals("[e4=, c2/, b1/, a0=, d0=, i3=, g1=, f0=, h0=, k1=, j0=, l0=]", t1.toDebugString());
		assertEquals(3, t1.slowHeight());

		m3points += m3weight;

		// double right => single left
		EditTree t2 = new EditTree();

		t2.add('d');
		t2.add('c', 0);
		t2.add('h');
		t2.add('a', 0);
		t2.add('X', 2);
		t2.add('f', 4);
		t2.add('j');
		t2.add('b', 1);
		t2.add('e', 5);
		t2.add('g', 7);
		t2.add('i', 9);
		t2.add('k');
		t2.add('l');

		t2.delete(3);

		assertEquals("abcdefghijkl", t2.toString());
		assertEquals("[h7=, d3=, b1=, a0=, c0=, f1=, e0=, g0=, j1\\, i0=, k0\\, l0=]", t2.toDebugString());
		assertEquals(3, t2.slowHeight());

		m3points += m3weight;

		// double right => double right
		EditTree t3 = new EditTree();

		t3.add('i');
		t3.add('d', 0);
		t3.add('l');
		t3.add('b', 0);
		t3.add('f', 2);
		t3.add('j', 4);
		t3.add('X');
		t3.add('a', 0);
		t3.add('c', 2);
		t3.add('e', 4);
		t3.add('g', 6);
		t3.add('k', 9);
		t3.add('h', 7);

		t3.delete(12);

		assertEquals("abcdefghijkl", t3.toString());
		assertEquals("[f5=, d3/, b1=, a0=, c0=, e0=, i2=, g0\\, h0=, k1=, j0=, l0=]", t3.toDebugString());
		assertEquals(3, t3.slowHeight());

		m3points += m3weight;

		// double right => double left
		EditTree t4 = new EditTree();

		t4.add('d');
		t4.add('c', 0);
		t4.add('i');
		t4.add('a', 0);
		t4.add('X', 2);
		t4.add('g', 4);
		t4.add('k');
		t4.add('b', 1);
		t4.add('f', 5);
		t4.add('h', 7);
		t4.add('j', 9);
		t4.add('l');
		t4.add('e', 5);

		t4.delete(3);

		assertEquals("abcdefghijkl", t4.toString());
		assertEquals("[g6=, d3=, b1=, a0=, c0=, f1/, e0=, i1\\, h0=, k1=, j0=, l0=]", t4.toDebugString());
		assertEquals(3, t4.slowHeight());

		m3points += m3weight;
	}

	@Test
	public void test384DeleteCausingTwoRotationsBelowRoot() {
		// single left => single right all on left subtree
		EditTree t1 = new EditTree();

		t1.add('m');
		t1.add('i', 0);
		t1.add('s');
		t1.add('e', 0);
		t1.add('j', 2);
		t1.add('p', 4);
		t1.add('v');
		t1.add('c', 0);
		t1.add('g', 2);
		t1.add('X', 4);
		t1.add('k', 6);
		t1.add('o', 8);
		t1.add('q', 10);
		t1.add('u', 12);
		t1.add('w');
		t1.add('b', 0);
		t1.add('d', 2);
		t1.add('f', 4);
		t1.add('h', 6);
		t1.add('l', 11);
		t1.add('n', 13);
		t1.add('r', 17);
		t1.add('t', 19);
		t1.add('x');
		t1.add('a', 0);

		t1.delete(9);

		assertEquals("abcdefghijklmnopqrstuvwx", t1.toString());
		assertEquals(
				"[m12=, e4=, c2/, b1/, a0=, d0=, i3=, g1=, f0=, h0=, k1=, j0=, l0=, s5=, p2=, o1/, n0=, q0\\, r0=, v2=, u1/, t0=, w0\\, x0=]",
				t1.toDebugString());
		assertEquals(4, t1.slowHeight());
		assertEquals(2, t1.totalRotationCount());

		m3points += m3weight;

		// at this point, a, f, h, j, l, n, r, t, x should make up your bottom
		// level.
		// if not, you've over-rotated, which is tested next

		t1.delete(23);
		t1.delete(19);
		t1.delete(17);
		t1.delete(13);
		t1.delete(11);
		t1.delete(9);
		t1.delete(7);
		t1.delete(5);

		assertEquals(4, t1.slowHeight());
		assertEquals(2, t1.totalRotationCount());

		t1.delete(0); // last node of bottom level

		assertEquals(3, t1.slowHeight());

		m3points += m3weight;

		// double right => double left all on right subtree
		EditTree t2 = new EditTree();

		t2.add('l');
		t2.add('f', 0);
		t2.add('p');
		t2.add('c', 0);
		t2.add('i', 2);
		t2.add('o', 4);
		t2.add('u');
		t2.add('b', 0);
		t2.add('d', 2);
		t2.add('h', 4);
		t2.add('j', 6);
		t2.add('m', 8);
		t2.add('X', 10);
		t2.add('s', 12);
		t2.add('w');
		assertEquals(3, t2.slowHeight());
		t2.add('a', 0);
		assertEquals(4, t2.slowHeight());
		t2.add('e', 4);
		t2.add('g', 6);
		t2.add('k', 10);
		t2.add('n', 13);
		t2.add('r', 17);
		t2.add('t', 19);
		t2.add('v', 21);
		t2.add('x');
		t2.add('q', 17);
		assertEquals(5, t2.slowHeight());
		assertEquals("abcdefghijklmnoXpqrstuvwx", t2.toString());
		assertEquals(
				"[l11\\, f5=, c2=, b1/, a0=, d0\\, e0=, i2=, h1/, g0=, j0\\, k0=, p4\\, o2/, m0\\, n0=, X0=, u4/, s2/, r1/, q0=, t0=, w1=, v0=, x0=]",
				t2.toDebugString());

		m3points += m3weight;

		t2.delete(15); // node X
		assertEquals(4, t2.totalRotationCount());

		assertEquals("abcdefghijklmnopqrstuvwx", t2.toString());
		assertEquals(
				"[l11=, f5=, c2=, b1/, a0=, d0\\, e0=, i2=, h1/, g0=, j0\\, k0=, s6=, p3=, n1=, m0=, o0=, r1/, q0=, u1\\, t0=, w1=, v0=, x0=]",
				t2.toDebugString());
		assertEquals(4, t2.slowHeight());

		m3points += m3weight;

		// at this point, a, e, g, k, m, o, q, v, x should make up your bottom
		// level.
		// if not, you've over-rotated, which is tested next

		t2.delete(23);
		t2.delete(21);
		t2.delete(16);
		t2.delete(14);
		t2.delete(12);
		t2.delete(10);
		t2.delete(6);
		t2.delete(4);

		assertEquals(4, t2.slowHeight());

		m3points += m3weight;

		t2.delete(0); // last node of bottom level

		m3points += m3weight;
	}

	// Final tests for deletion

	// This is the big tree from the day 14 slides:
	// http://www.rose-hulman.edu/class/csse/csse230/201710/Slides/14-AVLRotationPractice.pdf
	// It's the slide titled "A sample AVL tree" with root H.
	public EditTree makeBigTreeFromSlides() {
		// Level-order insertion so no rotations (we're pretty sure).
		EditTree t = new EditTree();
		t.add('H', 0);
		t.add('C', 0);
		t.add('U', 2);
		t.add('A', 0);
		t.add('F', 2);
		t.add('K', 4);
		t.add('W', 6);
		t.add('B', 1);
		t.add('E', 3);
		t.add('G', 5);
		t.add('J', 7);
		t.add('P', 9);
		t.add('V', 11);
		t.add('Y', 13);
		t.add('D', 3);
		t.add('I', 8);
		t.add('M', 11);
		t.add('R', 13);
		t.add('X', 17);
		t.add('L', 11);
		t.add('N', 13);
		t.add('Q', 15);
		t.add('S', 17);
		assertEquals(0, t.totalRotationCount());
		return t;
	}

	@Test
	public void test385ComplexRotationsOnTreeFromSlides() {
		char deleted;
		EditTree t = makeBigTreeFromSlides();
		assertEquals("ABCDEFGHIJKLMNPQRSUVWXY", t.toString());
		assertEquals(
				"[H7\\, C2\\, A0\\, B0=, F2/, E1/, D0=, G0=, U10/, K2\\, J1/, I0=, P3=, M1=, L0=, N0=, R1=, Q0=, S0=, W1\\, V0=, Y1/, X0=]",
				t.toDebugString());
		assertTrue("Expected: true", t.ranksMatchLeftSubtreeSize() && t.balanceCodesAreCorrect());

		// Interesting case. While technically it could be handled with either
		// single-left or double-left rotation, since single left is slightly
		// faster, we should prefer that.
		deleted = t.delete(8);
		assertEquals('I', deleted);
		assertEquals(
				"[H7\\, C2\\, A0\\, B0=, F2/, E1/, D0=, G0=, U9/, P5/, K1\\, J0=, M1=, L0=, N0=, R1=, Q0=, S0=, W1\\, V0=, Y1/, X0=]",
				t.toDebugString());
		assertTrue("Expected: true", t.ranksMatchLeftSubtreeSize() && t.balanceCodesAreCorrect());
		m3points += 1 * m3weight;
	}

	@Test
	public void test386MoreOfComplexRotationsOnTreeFromSlides() {
		char deleted;
		EditTree t = makeBigTreeFromSlides();
		assertEquals("ABCDEFGHIJKLMNPQRSUVWXY", t.toString());
		assertEquals(
				"[H7\\, C2\\, A0\\, B0=, F2/, E1/, D0=, G0=, U10/, K2\\, J1/, I0=, P3=, M1=, L0=, N0=, R1=, Q0=, S0=, W1\\, V0=, Y1/, X0=]",
				t.toDebugString());
		assertTrue("Expected: true", t.ranksMatchLeftSubtreeSize() && t.balanceCodesAreCorrect());

		// Causes two rotations
		deleted = t.delete(6);
		assertEquals('G', deleted);
		assertEquals("ABCDEFHIJKLMNPQRSUVWXY", t.toString());
		assertEquals(
				"[K9=, H6/, C2=, A0\\, B0=, E1=, D0=, F0=, J1/, I0=, U7=, P3=, M1=, L0=, N0=, R1=, Q0=, S0=, W1\\, V0=, Y1/, X0=]",
				t.toDebugString());
		assertTrue("Expected: true", t.ranksMatchLeftSubtreeSize() && t.balanceCodesAreCorrect());
		m3points += 2 * m3weight;

		// Interesting case. While technically it could be handled with either
		// single-right or double-right rotation, since single right is slightly
		// faster, we should prefer that.
		deleted = t.delete(7);

		assertEquals('I', deleted);
		assertEquals("ABCDEFHJKLMNPQRSUVWXY", t.toString());

		boolean gotSingleLeftRotationCorrect = t.toDebugString().equals(
				"[K8=, C2\\, A0\\, B0=, H3/, E1=, D0=, F0=, J0=, U7=, P3=, M1=, L0=, N0=, R1=, Q0=, S0=, W1\\, V0=, Y1/, X0=]");

		if (gotSingleLeftRotationCorrect) {
			System.out.println("Chose to do a single rotation in ambiguous case");
		}

		boolean gotDoubleLeftRotationCorrect = t.toDebugString().equals(
				"[K8=, E4/, C2/, A0\\, B0=, D0=, H1=, F0=, J0=, U7=, P3=, M1=, L0=, N0=, R1=, Q0=, S0=, W1\\, V0=, Y1/, X0=]");
		if (gotDoubleLeftRotationCorrect) {
			System.out.println(
					"Chose to do a double rotation in ambiguous case. It would pass this, but not the next ones.");
		}
		assertTrue("Expected: true", gotSingleLeftRotationCorrect || gotDoubleLeftRotationCorrect);
		m3points += 1 * m3weight;

		// Make sure we did single rotation.
		assertEquals(
				"[K8=, C2\\, A0\\, B0=, H3/, E1=, D0=, F0=, J0=, U7=, P3=, M1=, L0=, N0=, R1=, Q0=, S0=, W1\\, V0=, Y1/, X0=]",
				t.toDebugString());
		assertTrue("Expected: true", t.ranksMatchLeftSubtreeSize() && t.balanceCodesAreCorrect());

		// The following tests assume you did single rotation on the ambiguous
		// case.
		deleted = t.delete(7);
		assertEquals('J', deleted);
		assertEquals("ABCDEFHKLMNPQRSUVWXY", t.toString());
		assertEquals(
				"[K7=, C2\\, A0\\, B0=, E1\\, D0=, H1/, F0=, U7=, P3=, M1=, L0=, N0=, R1=, Q0=, S0=, W1\\, V0=, Y1/, X0=]",
				t.toDebugString());
		assertTrue("Expected: true", t.ranksMatchLeftSubtreeSize() && t.balanceCodesAreCorrect());

		deleted = t.delete(1);
		assertEquals('B', deleted);
		assertEquals("ACDEFHKLMNPQRSUVWXY", t.toString());
		assertEquals(
				"[K6\\, E3=, C1=, A0=, D0=, H1/, F0=, U7=, P3=, M1=, L0=, N0=, R1=, Q0=, S0=, W1\\, V0=, Y1/, X0=]",
				t.toDebugString());
		assertTrue("Expected: true", t.ranksMatchLeftSubtreeSize() && t.balanceCodesAreCorrect());

		m3points += 1 * m3weight;
	}

	/*
	 * Random add/delete test case by James Breen
	 * 
	 * Modified by Jimmy Theis 2011-05-02: - Added randomized position to insert
	 * into
	 */
	@Test
	public void test387RandomAddDelete() {
		Random gen = new Random();
		int alphabetSize = 26;
		int lengthWord = 1;

		StringBuffer analog = new StringBuffer(""); // String to test against

		// create a random tree
		EditTree t = new EditTree();
		for (int i = 0; i < lengthWord; i++) {
			char c = (char) ('a' + (gen.nextInt(alphabetSize)));
			int pos = gen.nextInt(t.size() + 1);
			t.add(c, pos);
			analog.insert(pos, c);
		}

		assertEquals(analog.toString(), t.toString());

		m3points += 3 * m3weight;

		// delete at random location from tree
		for (int i = 0; i < lengthWord; i++) {
			int pos = gen.nextInt(lengthWord - i);
			t.delete(pos);
			analog.deleteCharAt(pos);
		}

		assertEquals(analog.toString(), t.toString());

		m3points += 4 * m3weight;
	}

	@Test
	public void test390ConstructorWithString() {
		EditTree t1 = new EditTree();
		EditTree t2 = new EditTree("abc");
		EditTree t3 = new EditTree("abcdefghijkl");

		int h, max;
		assertEquals(0, t1.totalRotationCount() + t2.totalRotationCount() + t3.totalRotationCount());
		// Can build these trees in ways that require no rotations.
		assertEquals("", t1.toString());
		h = t1.slowHeight();
		max = maxHeight(t1.size());
		assertTrue("Expected: true", h <= max);
		assertEquals("abc", t2.toString());
		assertTrue("Expected: true", t2.slowHeight() <= maxHeight(t2.size()));
		assertEquals("abcdefghijkl", t3.toString());
		h = t3.slowHeight();
		max = maxHeight(t3.size());
		assertTrue("Expected: true", h <= max);

		m3points += 3 * m3weight;

		// Test using additions to make sure that rank and balance codes
		// have been set.

		t2.add('d');
		assertEquals(0, t2.totalRotationCount());
		t2.add('f'); // causes rotation
		assertEquals(1, t2.totalRotationCount());
		t2.add('e', 1);
		t2.add('g', 0);
		t2.add('h', 2);
		assertEquals("gahebcdf", t2.toString());

		t3.add('m', 5);
		t3.add('n', 7);
		t3.add('o', 2);
		t3.add('p', 7);
		t3.add('q', 11);
		t3.add('r', 3);
		t3.add('s', 15);

		assertEquals("aborcdempfngqhisjkl", t3.toString());

		m3points += 2 * m3weight;

		// Add many random nodes and make sure height is under control.
		Random rand = new Random();
		for (int i = 0; i < 2000; i++) {
			int randIndex = rand.nextInt(t3.size() + 1);
			t3.add('a', randIndex);
		}
		max = maxHeight(t3.size());
		assertTrue("Expected: true", h <= max);

		m3points += 2 * m3weight;
	}

	@Test
	public void test391ConstructorWithStringNotFullTree() {
		EditTree t1 = new EditTree("ab");
		EditTree t2 = new EditTree("abcde");
		EditTree t3 = new EditTree("abcdefghijklmnopqrstuvwxyz");
		int h, max;
		assertEquals(0, t1.totalRotationCount() + t2.totalRotationCount() + t3.totalRotationCount());
		// Can build these trees in ways that require no rotations.
		assertEquals("ab", t1.toString());
		h = t1.slowHeight();
		max = maxHeight(t1.size());
		assertTrue("Expected: true", h <= max);
		assertEquals("abcde", t2.toString());
		assertTrue("Expected: true", t2.slowHeight() <= maxHeight(t2.size()));
		assertEquals("abcdefghijklmnopqrstuvwxyz", t3.toString());
		h = t3.slowHeight();
		max = maxHeight(t3.size());
		assertTrue("Expected: true", h <= max);
		assertTrue("Expected: true",
				t1.balanceCodesAreCorrect() && t2.balanceCodesAreCorrect() && t3.balanceCodesAreCorrect());
		m3points += 3 * m3weight;
	}

	@Test
	public void test393GetEntireTree() {
		EditTree t1 = new EditTree();
		t1.add('c');
		t1.add('a', 0);
		t1.add('d');
		t1.add('b', 1);

		EditTree t2 = new EditTree();
		t2.add('b');
		t2.add('a', 0);
		t2.add('d');
		t2.add('c', 2);

		EditTree t3 = makeFullTreeWithHeight(3, 'b');
		t3.add('a', 0);

		EditTree t4 = makeFullTreeWithHeight(3, 'a');
		t4.add('p');

		EditTree t5 = makeFullTreeWithHeight(1, 'a');
		EditTree t6 = makeFullTreeWithHeight(3, 'a');

		assertEquals(t1.toString(), t1.get(0, t1.size()));
		assertEquals(t2.toString(), t2.get(0, t2.size()));
		assertEquals(t3.toString(), t3.get(0, t3.size()));
		assertEquals(t4.toString(), t4.get(0, t4.size()));
		assertEquals(t5.toString(), t5.get(0, t5.size()));
		assertEquals(t6.toString(), t6.get(0, t6.size()));

		m3points += 2 * m3weight;
	}

	@Test
	public void test394GetSubstring() {
		EditTree t1 = new EditTree();
		t1.add('c');
		t1.add('a', 0);
		t1.add('d');
		t1.add('b', 1);

		EditTree t2 = new EditTree();
		t2.add('b');
		t2.add('a', 0);
		t2.add('d');
		t2.add('c', 2);

		EditTree t3 = makeFullTreeWithHeight(3, 'b');
		t3.add('a', 0);

		EditTree t4 = makeFullTreeWithHeight(3, 'a');
		t4.add('p');

		assertEquals("abc", t1.get(0, 3));
		assertEquals("bcd", t1.get(1, 3));
		assertEquals("d", t2.get(3, 1));
		assertEquals("abcdef", t3.get(0, 6));
		assertEquals("lmnop", t4.get(11, 5));

		m3points += 1 * m3weight;
	}

	@Test
	public void test395GetInvalidRange() {
		EditTree t1 = new EditTree();
		EditTree t2 = makeFullTreeWithHeight(2, 'a');

		// empty tree; beginning of string out of bounds
		try {
			t1.get(-1, 2);
			fail("Did not throw IndexOutofBoundsException");
		} catch (IndexOutOfBoundsException e) {
			// assertTrueed
		}

		// empty tree; end of string out of bounds
		try {
			t1.get(0, 1);
			fail("Did not throw IndexOutofBoundsException");
		} catch (IndexOutOfBoundsException e) {
			// assertTrueed
		}

		// beginning of string out of bounds
		try {
			t2.get(-1, 3);
			fail("Did not throw IndexOutofBoundsException");
		} catch (IndexOutOfBoundsException e) {
			// assertTrueed
		}

		// end of string out of bounds
		try {
			t2.get(4, 4);
			fail("Did not throw IndexOutofBoundsException");
		} catch (IndexOutOfBoundsException e) {
			// assertTrueed
		}

		try {
			t2.get(0, 8);
			fail("Did not throw IndexOutofBoundsException");
		} catch (IndexOutOfBoundsException e) {
			// assertTrueed
		}

		// beginning of string too large
		try {
			t2.get(7, 1);
			fail("Did not throw IndexOutofBoundsException");
		} catch (IndexOutOfBoundsException e) {
			// assertTrueed
		}

		m3points += 1 * m3weight;
	}

	@Test
	public void test396GetEfficiency() {
		EditTree t1 = makeFullTreeWithHeight(18, 'a'); // Make a large tree
		assertEquals("abcdef", t1.get(0, 6)); // make sure get(pos, length) is working
		assertEquals("lmnop", t1.get(11, 5)); // before doing stress tests
		String t1Str = t1.toString();
		int quarterIndex = t1.size() / 4;
		int halfSize = t1.size() / 2;
		String halfStr = t1Str.substring(quarterIndex, quarterIndex + halfSize);
		assertEquals(halfStr, t1.get(quarterIndex, halfSize));

		int numIters = 30;

		// CONTROL TEST: Run toString [O(N)] numIters times
		Long startTime = System.currentTimeMillis();
		for (int i = 0; i < numIters; i++) {
			t1.toString();
		}
		Long toStringTime = System.currentTimeMillis() - startTime;

		// EXPERIMENTAL TEST: Run get(pos, len) numIters times, where len ~= size / 2.
		// If truly O(len), then should take about half the time of the control test
		// above.
		startTime = System.currentTimeMillis();
		for (int i = 0; i < numIters; i++) {
			t1.get(quarterIndex, halfSize);
		}
		Long getMiddleHalfTime = System.currentTimeMillis() - startTime;

		System.out.printf("get(pos, length) efficiency test. toString time: %dms, getMiddleHalf time: %dms\n",
				toStringTime, getMiddleHalfTime);

		double acceptableFraction = 3.0 / 4;
		if (getMiddleHalfTime <= acceptableFraction * toStringTime) {
			m3points += 8 * m3weight;
		}
		assertTrue("get(pos, length) on the middle half of the tree is taking longer than 3/4 the time"
				+ "of toString on the entire tree.\n It's likely (but not guaranteed) that your get(pos, length) "
				+ "method is not efficient enough. Please make sure it is O(length).",
				getMiddleHalfTime <= acceptableFraction * toStringTime);
		// NOTE: If you're not getting these points, but your get(pos, length) method is
		// indeed
		// O(length), you may be awarded the points manually by the grader.
	}

	@AfterClass
	public static void printSummary() {
		System.out.print("\n ===============     ");
		System.out.print("Milestone 1 again: ");
		System.out.printf("%5.1f / %d ", m1points, MAX_DESIRED_MILESTONE1);
		System.out.println("     ===============");

		System.out.print("\n ===============     ");
		System.out.print("Milestone 2 again: ");
		System.out.printf("%5.1f / %d ", m2points, MAX_DESIRED_MILESTONE2);
		System.out.println("     ===============");

		System.out.print("\n ===============     ");
		System.out.print("Milestone 3:       ");
		System.out.printf("%5.1f / %d ", m3points, MAX_DESIRED_MILESTONE3);
		System.out.println("     ===============");

		double points = m1points + m2points + m3points;
		System.out.print("\n ===============     ");
		System.out.print("Total Points:      ");
		System.out.printf("%5.1f / %d ", points, MAX_POINTS);
		System.out.println("     ===============");
	}
}
