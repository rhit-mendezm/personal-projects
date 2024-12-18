import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

// Test class
public class StringHashSetTest {

	private StringHashSet mHashSet;
	private List<String> mHelloList;
	private static int sPoints = 0;
	private static int sBonusPoints = 0;
	private static boolean sPassedToStringBasic = false;

	@Before
	public void setUp() throws Exception {
		// We construct the hash set and list of test words fresh for every
		// test, so put them in the setup method.
		mHashSet = new StringHashSet();

		mHelloList = new ArrayList<String>();
		// An alternative to calling add one at a time on all 10 strings.
		// Is there a better way to build an array list?
		// Note: I hyphenate ni-hao to make it clear for debugging that it is a
		// single string.
		String[] helloArray = new String[] { "hello", "hi", "caio", "hola", "shalom", "bonjour", "ni-hao", "aloha",
				"tere", "sveiki" };
		for (String s : helloArray) {
			mHelloList.add(s);
		}
	}

	@Test
	public void testStringHashCode() {
		// Many ways to say hello! We test the hash code first because if it is
		// broken, nothing else will work.
		assertEquals(65, StringHashSet.stringHashCode("A"));
		assertEquals(2081, StringHashSet.stringHashCode("AB"));
		assertEquals(3053332, StringHashSet.stringHashCode("ciao"));
		assertEquals(3208380, StringHashSet.stringHashCode("hola"));
		assertEquals(99162322, StringHashSet.stringHashCode("hello"));
		// Note that hashcode can go negative.
		assertEquals(-903571682, StringHashSet.stringHashCode("shalom"));

		for (String s : mHelloList) {
			assertEquals(s.hashCode(), StringHashSet.stringHashCode(s));
		}

		// Also could use: lasho djes, hej, ni hao,
		// sawubona, mambote, salut, salve, oi, bok, merhaba, szia, ahoj,
		// kumusta, labas, mbote, ahoy, kia, konnichiwa, namaskara,
		// zdravo, witaj, osiyo
		sPoints += 5;
	}

	// We test using "raw" strings (simple formatting), since the method is
	// easier to write than toString, and reveals more info than toString.
	@Test
	public void testAddAndRawStringNoCollision() {
		mHashSet.add("hello");
		String expected = "0: null\n1: null\n2: hello null\n3: null\n4: null\n";
		assertEquals(expected, mHashSet.toRawString());
		mHashSet.add("hi");
		expected = "0: null\n1: null\n2: hello null\n3: null\n4: hi null\n";
		assertEquals(expected, mHashSet.toRawString());
		mHashSet.add("hola");
		expected = "0: hola null\n1: null\n2: hello null\n3: null\n4: hi null\n";
		assertEquals(expected, mHashSet.toRawString());
		mHashSet.add("bonjour");
		expected = "0: hola null\n1: bonjour null\n2: hello null\n3: null\n4: hi null\n";
		assertEquals(expected, mHashSet.toRawString());
		assertEquals(expected, mHashSet.toRawString());
		sPoints += 5;
	}

	@Test
	public void testAddAndRawStringNoRehash() {
		mHashSet.add("hello");
		mHashSet.add("hi");
		mHashSet.add("caio");
		mHashSet.add("hola");
		mHashSet.add("shalom");
		mHashSet.add("bonjour");
		String expected = "0: hola null\n1: bonjour shalom null\n2: caio hello null\n3: null\n4: hi null\n";
		assertEquals(expected, mHashSet.toRawString());
		sPoints += 5;
	}

	@Test
	public void testAddDuplicate() {
		assertTrue(mHashSet.add("hello"));
		assertFalse(mHashSet.add("hello"));
		String expected = "0: null\n1: null\n2: hello null\n3: null\n4: null\n";
		assertEquals(expected, mHashSet.toRawString());
		sPoints += 3;
	}

	@Test
	public void testContains() {
		mHashSet.add("hello");
		assertTrue(mHashSet.contains("hello"));
		assertFalse(mHashSet.contains("goodbye"));
		assertFalse(mHashSet.contains("caio"));
		sPoints += 5;
	}

	@Test
	public void testEmptyHashSet() {
		assertEquals(0, mHashSet.size());
		assertTrue(mHashSet.isEmpty());
		sPoints += 2;
	}

	@Test
	public void testSize() {
		mHashSet.add("hello");
		assertEquals(1, mHashSet.size());
		mHashSet.add("hi");
		assertEquals(2, mHashSet.size());
		mHashSet.add("bonjour");
		assertEquals(3, mHashSet.size());
		sPoints += 2;
	}

	@Test
	public void testClear() {
		assertEquals(0, mHashSet.size());
		mHashSet.add("hello");
		mHashSet.add("hi");
		mHashSet.add("bonjour");
		assertEquals(3, mHashSet.size());

		mHashSet.clear();
		String expected = "0: null\n1: null\n2: null\n3: null\n4: null\n";
		assertEquals(expected, mHashSet.toRawString());
		assertEquals(0, mHashSet.size());

		mHashSet.add("ciao");
		expected = "0: null\n1: null\n2: ciao null\n3: null\n4: null\n";
		assertEquals(expected, mHashSet.toRawString());
		sPoints += 3;

	}

	@Test
	public void testAddAll() {
		mHashSet.addAll(mHelloList);
		for (String s : mHelloList) {
			assertTrue(mHashSet.contains(s));
		}
		assertFalse(mHashSet.contains("mulishani"));
		sPoints += 5;
	}

	@Test
	public void testRemove() {
		mHashSet.addAll(mHelloList);
		assertTrue(mHashSet.remove("hello"));
		assertFalse(mHashSet.contains("hello"));
		assertTrue(mHashSet.contains("aloha"));
		assertEquals(9, mHashSet.size());
		sPoints += 5;
	}

	@Test
	public void testAddAndRawStringRehash() {
		for (String s : mHelloList) {
			mHashSet.add(s);
		}
		String expected = "0: hola null\n1: bonjour shalom null\n2: tere aloha caio hello null\n3: sveiki null\n4: ni-hao hi null\n";
		assertEquals(expected, mHashSet.toRawString());
		// Should be full (lambda = 2) now. The next one causes a rehash:
		mHashSet.add("mulishani");

		// This assertion is weaker than before, since the order of the items
		// after the rehash is unspecified. We just check that shalom moved to
		// position 6, because the hashcode mod the array size changed. (it was
		// originally in position 0 when the capacity of the
		// hash table was 6). It should be the only item in position 6.
		assertTrue(mHashSet.toRawString().contains("6: shalom"));
		// Then test if another string is still there.
		assertTrue(mHashSet.contains("ni-hao"));
		sPoints += 4;
	}

	@Test
	public void testIteratorNextHasNext() {
		mHashSet.add("hello");
		mHashSet.add("hi");
		mHashSet.add("caio");
		mHashSet.add("hola");
		mHashSet.add("shalom");
		mHashSet.add("bonjour");

		String s = null;
		Iterator<String> iter = mHashSet.iterator();
		assertTrue(iter.hasNext());
		for (int i = 0; i < 6; i++) {
			s = iter.next();
			assertTrue(mHashSet.contains(s));
		}
		assertFalse(iter.hasNext());
		sBonusPoints += 2;
	}

	@Test
	public void testIteratorNoSuchElementException() {
		mHashSet.add("hello");
		mHashSet.add("hi");
		mHashSet.add("caio");
		mHashSet.add("hola");
		mHashSet.add("shalom");
		mHashSet.add("bonjour");

		String s = null;
		Iterator<String> iter = mHashSet.iterator();
		assertTrue(iter.hasNext());
		for (int i = 0; i < 6; i++) {
			s = iter.next();
			assertTrue(mHashSet.contains(s));
		}
		try {
			s = iter.next();
			fail("Should have thrown NoSuchElementException");
		} catch (NoSuchElementException e) {
			sBonusPoints += 1;
		} catch (Exception e) {
			fail("Threw wrong type of exception");
		}
	}

	@Test
	public void testIteratorConcurrentModificationException() {
		// Test that the code throws a
		// ConcurrentModificationException if next() is called and the set has
		// been mutated since the iterator was created.

		// Test ConcurrentModificationException for add
		mHashSet.clear();
		mHashSet.addAll(mHelloList);
		Iterator<String> iterAdd = mHashSet.iterator();

		try {
			mHashSet.add("item");
			iterAdd.next();
			fail("Did not throw ConcurrentModificationException for add");
		} catch (Exception e) {
			if (!(e instanceof ConcurrentModificationException)) {
				fail("Did not throw ConcurrentModificationException for add");
			}

			else {
				System.out.println("[PASSED] ConcurrentModificationException Test for add");
			}
		}

		// Test ConcurrentModificationException for clear
		mHashSet.clear();
		mHashSet.addAll(mHelloList);
		Iterator<String> iterClear = mHashSet.iterator();

		try {
			mHashSet.clear();
			iterClear.next();
			fail("Did not throw ConcurrentModificationException for clear");
		} catch (Exception e) {
			if (!(e instanceof ConcurrentModificationException)) {
				fail("Did not throw ConcurrentModificationException for clear");
			}

			else {
				System.out.println("[PASSED] ConcurrentModificationException Test for clear");
			}
		}

		// Test ConcurrentModificationException for remove
		mHashSet.clear();
		mHashSet.addAll(mHelloList);
		Iterator<String> iterRemove = mHashSet.iterator();

		try {
			mHashSet.remove("hello");
			iterRemove.next();
			fail("Did not throw ConcurrentModificationException for remove");
		} catch (Exception e) {
			if (!(e instanceof ConcurrentModificationException)) {
				fail("Did not throw ConcurrentModificationException for remove");
			}

			else {
				System.out.println("[PASSED] ConcurrentModificationException Test for remove");
			}
		}

		// Test ConcurrentModificationException for addAll
		mHashSet.clear();
		Iterator<String> iterAddAll = mHashSet.iterator();

		try {
			mHashSet.addAll(mHelloList);
			iterAddAll.next();
			fail("Did not throw ConcurrentModificationException for addAll");
		} catch (Exception e) {
			if (!(e instanceof ConcurrentModificationException)) {
				fail("Did not throw ConcurrentModificationException for addAll");
			}

			else {
				System.out.println("[PASSED] ConcurrentModificationException Test for addAll");
			}
		}
		sBonusPoints += 1;
	}

	@Test
	public void testIteratorDifferentReturns() {
		mHashSet.add("hello");
		mHashSet.add("hi");
		mHashSet.add("caio");
		mHashSet.add("hola");
		mHashSet.add("shalom");
		mHashSet.add("bonjour");
		String s = null;
		Iterator<String> iter = mHashSet.iterator();
		assertTrue(iter.hasNext());
		ArrayList<String> returned = new ArrayList<String>();
		for (int i = 0; i < 6; i++) {
			s = iter.next();
			assertTrue(mHashSet.contains(s) && !returned.contains(s));
			returned.add(s);
		}
		sBonusPoints += 1;
	}

	@Test
	public void testToString() {
		sPassedToStringBasic = false;
		mHashSet.add("hello");
		assertEquals("[hello]", mHashSet.toString());
		mHashSet.add("hi");
		assertTrue("[hello, hi]".equals(mHashSet.toString()) || "[hi, hello]".equals(mHashSet.toString()));
		mHashSet.add("caio");
		mHashSet.add("hola");
		mHashSet.add("shalom");
		mHashSet.add("bonjour");

		sPassedToStringBasic = true;

		// Check visually and assign 3 points if in this format, even if order
		// is different.
		System.out.println("toString: " + mHashSet.toString());

		/*
		 * From Marlon, the code returns "[hola, bonjour, shalom, caio, hello, hi]"
		 * which is the correct form. The order is different.
		 */
//		 assertEquals("[shalom, hola, bonjour, caio, hello, hi]",
//		 mHashSet.toString());
	}

	@AfterClass
	public static void displayPoints() {
		if (sPassedToStringBasic) {
			System.out.println("Check toString() output for correct formatting since passed basic test.");
		} else {
			System.out.println("toString didn't pass basic test.");
		}
		System.out.printf("Base points (without toString): %2d/44\n", (sPoints));
		System.out.printf("toString points:                 ?/3 (checked manually)\n");
		System.out.printf("Bonus Points (up to 5):          %d\n", sBonusPoints);
	}
}
