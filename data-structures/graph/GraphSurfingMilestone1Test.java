package graphs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.Test;

/**
 * Complete test cases for Milestone 1
 * 
 * 
 * @author Nate Chenette. Improved lazy iterator speed tests by Henry Nunns.
 * 
 */
public class GraphSurfingMilestone1Test {

	private static int m1points = 0;
	private static int m1weight = 2;
	private static final int MAX_POINTS = 100;

	private static boolean speedTestsOn = true; // turn to false for faster testing without speed tests

	// MILESTONE 1
	// Implement the following methods first:
	// - the constructor taking a Set of data
	// - the addEdge method

	private Set<String> getThreeVerticesToAdd() {
		HashSet<String> toInsert = new HashSet<String>();
		toInsert.add("a");
		toInsert.add("b");
		toInsert.add("c");
		return toInsert;
	}


	@Test
	public void testALConstructorAndSize() {
		Graph<String> g = new AdjacencyListGraph<String>(new HashSet<String>()); // create empty graph
		assertEquals(0, g.size());
		g = new AdjacencyListGraph<String>(getThreeVerticesToAdd()); // create graph with three vertices
		assertEquals(3, g.size());
		m1points += 3*m1weight;
	}

	@Test
	public void testAMConstructorAndSize() {
		Graph<String> g = new AdjacencyMatrixGraph<String>(new HashSet<String>()); // create empty graph
		assertEquals(0, g.size());
		g = new AdjacencyMatrixGraph<String>(getThreeVerticesToAdd()); // create graph with three vertices
		assertEquals(3, g.size());
		m1points += 3*m1weight;
	}
	
	
	private void helperTestAddEdge(Graph<String> g) {
		g.addEdge("a", "b");
		assertEquals(true,g.addEdge("b", "c"));
		m1points += 2*m1weight;
		assertEquals(false,g.addEdge("b", "c"));
		try {
			g.addEdge("b", "d");
			fail("Did not throw NoSuchElementException");
		} catch (Exception e) {
			if (!(e instanceof NoSuchElementException)) {
				fail("Did not throw NoSuchElementException");
			}
		}
		m1points += m1weight;
	}
	
	@Test
	public void testALAddEdge() {
		Graph<String> g = new AdjacencyListGraph<String>(getThreeVerticesToAdd()); // create graph with three vertices
		helperTestAddEdge(g);
	}
	
	@Test
	public void testAMAddEdge() {
		Graph<String> g = new AdjacencyMatrixGraph<String>(getThreeVerticesToAdd()); // create graph with three vertices
		helperTestAddEdge(g);
	}
	
	
	
	// The following tests will work once the following are implemented correctly, for each graph
	// implementation:
	// - the constructor taking a Set of data
	// - the addEdge method
	
	private Set<String> getExampleVertexData() {
		String[] toInsert = {"a","b","c","d","e","f"};
		HashSet<String> items = new HashSet<String>();
		for (String str : toInsert) {
			items.add(str);
		}
		return items;
	}
	
	private Set<Integer> getExample2VertexData() {
		Integer[] toInsert = {0,1,2,3,4,5,6};
		HashSet<Integer> items = new HashSet<Integer>();
		for (Integer i : toInsert) {
			items.add(i);
		}
		return items;
	}
	
	private void addExampleEdges(Graph<String> g) {
		g.addEdge("a", "b");
		g.addEdge("a", "c");
		g.addEdge("b", "d");
		g.addEdge("c", "d");
		g.addEdge("d", "c");
		g.addEdge("d", "e");
		g.addEdge("d", "f");
		g.addEdge("f", "c");
	}

	private void addExample2Edges(Graph<Integer> g) {
		g.addEdge(0, 1);
		g.addEdge(1, 0);
		g.addEdge(0, 2);
		g.addEdge(2, 3);
		g.addEdge(2, 4);
		g.addEdge(3, 4);
		g.addEdge(4, 5);
		g.addEdge(4, 6);
		g.addEdge(6, 2);
	}
	
	public Graph<String> makeExampleALGraph() {
		Graph<String> g = new AdjacencyListGraph<String>(getExampleVertexData());
		addExampleEdges(g);
		return g;
	}

	public Graph<Integer> makeExample2ALGraph() {
		Graph<Integer> g = new AdjacencyListGraph<Integer>(getExample2VertexData());
		addExample2Edges(g);
		return g;
	}

	public Graph<String> makeExampleAMGraph() {
		Graph<String> g = new AdjacencyMatrixGraph<String>(getExampleVertexData());
		addExampleEdges(g);
		return g;
	}

	public Graph<Integer> makeExample2AMGraph() {
		Graph<Integer> g = new AdjacencyMatrixGraph<Integer>(getExample2VertexData());
		addExample2Edges(g);
		return g;
	}
	
	private void helperTestHasEdge(Graph<String> g, Graph<Integer> g2) {
		assertTrue("Expected: true", g.hasEdge("a","b"));
		assertTrue("Expected: true", g.hasEdge("f","c"));
		assertFalse("Expected: false", g.hasEdge("b","c"));
		assertFalse("Expected: false", g.hasEdge("b","a"));
		assertTrue("Expected: true", g2.hasEdge(0,2));
		assertTrue("Expected: true", g2.hasEdge(4,6));
		assertFalse("Expected: false", g2.hasEdge(2,5));
		assertFalse("Expected: false", g2.hasEdge(2,6));
		m1points += m1weight;
		try {
			g.hasEdge("a", "g"); // g not in graph
			fail("Did not throw NoSuchElementException");
		} catch (Exception e) {
			if (!(e instanceof NoSuchElementException)) {
				fail("Did not throw NoSuchElementException");
			}
		}
		try {
			g2.hasEdge(10, 5);   // 10 not in graph
			fail("Did not throw NoSuchElementException");
		} catch (Exception e) {
			if (!(e instanceof NoSuchElementException)) {
				fail("Did not throw NoSuchElementException");
			}
		}
		m1points += m1weight;
	}

	@Test
	public void testALHasEdge() {
		helperTestHasEdge(makeExampleALGraph(),makeExample2ALGraph());
	}
	
	@Test
	public void testAMHasEdge() {
		helperTestHasEdge(makeExampleAMGraph(),makeExample2AMGraph());
	}
	
	
	@Test
	public void testALNumEdges() {
		Graph<String> g = makeExampleALGraph();
		assertEquals(8, g.numEdges());
		Graph<Integer> g2 = makeExample2ALGraph();
		assertEquals(9, g2.numEdges());
		m1points += 1.5*m1weight;
	}
	

	@Test
	public void testAMNumEdges() {
		Graph<String> g = makeExampleAMGraph();
		assertEquals(8, g.numEdges());
		Graph<Integer> g2 = makeExample2AMGraph();
		assertEquals(9, g2.numEdges());
		m1points += 1.5*m1weight;
	}

	private void helperTestInDegree(Graph<String> g) {
		assertEquals(0, g.inDegree("a"));
		assertEquals(1, g.inDegree("b"));
		assertEquals(3, g.inDegree("c"));
		assertEquals(2, g.inDegree("d"));
		assertEquals(1, g.inDegree("e"));
		assertEquals(1, g.inDegree("f"));
		try {
			g.inDegree("z");
			fail("Did not throw NoSuchElementException");
		} catch (Exception e) {
			if (!(e instanceof NoSuchElementException)) {
				fail("Did not throw NoSuchElementException");
			}
		}
		m1points += 1.5*m1weight;
	}
	
	@Test
	public void testALInDegree() {
		helperTestInDegree(makeExampleALGraph());
	}
	
	@Test
	public void testAMInDegree() {
		helperTestInDegree(makeExampleAMGraph());
	}
	

	private void helperTestOutDegree(Graph<String> g) {
		assertEquals(2, g.outDegree("a"));
		assertEquals(1, g.outDegree("b"));
		assertEquals(1, g.outDegree("c"));
		assertEquals(3, g.outDegree("d"));
		assertEquals(0, g.outDegree("e"));
		assertEquals(1, g.outDegree("f"));
		try {
			g.outDegree("z");
			fail("Did not throw NoSuchElementException");
		} catch (Exception e) {
			if (!(e instanceof NoSuchElementException)) {
				fail("Did not throw NoSuchElementException");
			}
		}
		m1points += 1.5*m1weight;
	}

	@Test
	public void testALOutDegree() {
		helperTestOutDegree(makeExampleALGraph());
	}

	@Test
	public void testAMOutDegree() {
		helperTestOutDegree(makeExampleAMGraph());
	}
	

	private void helperTestHasVertex(Graph<String> g, Graph<Integer> g2) {
		assertTrue("Expected: true", g.hasVertex("a"));
		assertTrue("Expected: true", g.hasVertex("f"));
		assertFalse("Expected: false", g.hasVertex("g"));
		assertTrue("Expected: true", g2.hasVertex(0));
		assertTrue("Expected: true", g2.hasVertex(6));
		assertFalse("Expected: false", g2.hasVertex(7));
		m1points += m1weight;
	}

	@Test
	public void testALHasVertex() {
		helperTestHasVertex(makeExampleALGraph(),makeExample2ALGraph());
	}

	@Test
	public void testAMHasVertex() {
		helperTestHasVertex(makeExampleAMGraph(),makeExample2AMGraph());
	}	

	private void helperTestKeySet(Graph<String> g, Graph<Integer> g2) {
		assertEquals(getExampleVertexData(),g.keySet());
		assertEquals(getExample2VertexData(),g2.keySet());
		m1points += m1weight;
	}

	@Test
	public void testALKeySet() {
		helperTestKeySet(makeExampleALGraph(),makeExample2ALGraph());
	}

	@Test
	public void testAMKeySet() {
		helperTestKeySet(makeExampleAMGraph(),makeExample2AMGraph());
	}
	
	private void helperTestRemoveEdge(Graph<String> g) {
		assertTrue("Expected: true", g.hasEdge("a", "b"));
		int initialNumEdges = g.numEdges();
		g.removeEdge("a","b");
		assertEquals(initialNumEdges-1, g.numEdges());
		assertFalse("Expected: false", g.hasEdge("a", "b"));
		assertTrue("Expected: true", g.hasEdge("f", "c"));
		assertTrue("Expected: true", g.removeEdge("f","c"));
		assertFalse("Expected: false", g.hasEdge("f", "c"));
		assertFalse("Expected: false", g.hasEdge("f", "c"));
		assertFalse("Expected: false", g.removeEdge("b","c"));
		assertFalse("Expected: false", g.hasEdge("f", "c"));
		assertEquals(initialNumEdges-2, g.numEdges());
		m1points += m1weight;
		try {
			g.removeEdge("a", "g"); // g not in graph
			fail("Did not throw NoSuchElementException");
		} catch (Exception e) {
			if (!(e instanceof NoSuchElementException)) {
				fail("Did not throw NoSuchElementException");
			}
		}
		m1points += m1weight;
	}
		
	@Test
	public void testALRemoveEdge() {
		helperTestRemoveEdge(makeExampleALGraph());
	}
	
	@Test
	public void testAMRemoveEdge() {
		helperTestRemoveEdge(makeExampleAMGraph());
	}
	
	private void helperTestSuccessorSet(Graph<String> g) {
		Set<String> bSucc = new HashSet<String>();
		bSucc.add("d");
		assertEquals(bSucc,g.successorSet("b"));
		
		Set<String> gSucc = new HashSet<String>();
		assertEquals(gSucc,g.successorSet("e"));
		
		Set<String> dSucc = new HashSet<String>();
		dSucc.add("c");
		dSucc.add("e");
		dSucc.add("f");
		assertEquals(dSucc,g.successorSet("d"));
		try {
			g.successorSet("z");
			fail("Did not throw NoSuchElementException");
		} catch (Exception e) {
			if (!(e instanceof NoSuchElementException)) {
				fail("Did not throw NoSuchElementException");
			}
		}
		m1points += 1.0*m1weight;
	}

	@Test
	public void testALSuccessorSet() {
		helperTestSuccessorSet(makeExampleALGraph());
	}

	@Test
	public void testAMSuccessorSet() {
		helperTestSuccessorSet(makeExampleAMGraph());
	}
	
	private void helperTestPredecessorSet(Graph<String> g) {
		Set<String> bSucc = new HashSet<String>();
		bSucc.add("a");
		assertEquals(bSucc,g.predecessorSet("b"));
		
		Set<String> gSucc = new HashSet<String>();
		assertEquals(gSucc,g.predecessorSet("a"));
		
		Set<String> dSucc = new HashSet<String>();
		dSucc.add("a");
		dSucc.add("d");
		dSucc.add("f");
		assertEquals(dSucc,g.predecessorSet("c"));
		try {
			g.predecessorSet("z");
			fail("Did not throw NoSuchElementException");
		} catch (Exception e) {
			if (!(e instanceof NoSuchElementException)) {
				fail("Did not throw NoSuchElementException");
			}
		}
		m1points += 0.5*m1weight;
	}

	@Test
	public void testALPredecessorSet() {
		helperTestPredecessorSet(makeExampleALGraph());
	}

	@Test
	public void testAMPredecessorSet() {
		helperTestPredecessorSet(makeExampleAMGraph());
	}
	
	private void helperTestSuccessorIterator(Graph<String> g) {
		Iterator<String> it = g.successorIterator("b");
		assertTrue("Expected: true", it.hasNext());
		assertEquals("d",it.next());
		assertFalse("Expected: false", it.hasNext());
		m1points += 0.5*m1weight;
		
		it = g.successorIterator("d");
		Set<String> returned = new HashSet<String>();
		Set<String> expected = new HashSet<String>();
		expected.add("c");
		expected.add("e");
		expected.add("f");
		while (it.hasNext()) {
			returned.add(it.next());
		}
		assertEquals(expected, returned);
		try {
			g.successorIterator("z");
			fail("Did not throw NoSuchElementException");
		} catch (Exception e) {
			if (!(e instanceof NoSuchElementException)) {
				fail("Did not throw NoSuchElementException");
			}
		}
		m1points += 0.5*m1weight;
	}

	@Test
	public void testALSuccessorIterator() {
		helperTestSuccessorIterator(makeExampleALGraph());
	}

	@Test
	public void testAMSuccessorIterator() {
		helperTestSuccessorIterator(makeExampleAMGraph());
	}
	
	private void helperTestPredecessorIterator(Graph<String> g) {
		Iterator<String> it = g.predecessorIterator("b");
		assertTrue("Expected: true", it.hasNext());
		assertEquals("a",it.next());
		assertFalse("Expected: false", it.hasNext());
		m1points += 0.5*m1weight;
		
		it = g.predecessorIterator("c");
		Set<String> returned = new HashSet<String>();
		Set<String> expected = new HashSet<String>();
		expected.add("a");
		expected.add("d");
		expected.add("f");
		while (it.hasNext()) {
			returned.add(it.next());
		}
		assertEquals(expected, returned);
		try {
			g.predecessorIterator("z");
			fail("Did not throw NoSuchElementException");
		} catch (Exception e) {
			if (!(e instanceof NoSuchElementException)) {
				fail("Did not throw NoSuchElementException");
			}
		}
		m1points += 0.5*m1weight;
	}

	@Test
	public void testALPredecessorIterator() {
		helperTestPredecessorIterator(makeExampleALGraph());
	}

	@Test
	public void testAMPredecessorIterator() {
		helperTestPredecessorIterator(makeExampleAMGraph());
	}
	
	
	@Test
	public void testALSuccessorIteratorIsLazy() {
		assertTrue("Expected: true", speedTestsOn);
		int numVertices = 10000;
		Set<Integer> keySet = new HashSet<Integer>();
		for (int i = 0; i < numVertices; i++) {
			keySet.add(i); // add 10000 vertices
		}
		Graph<Integer> g = new AdjacencyListGraph<Integer>(keySet);
		for (int j = 0; j < numVertices; j++) {
			g.addEdge(0, j);  // add edges from 0 to all other vertices
		}
		
		// Run the speed test for iterator construction and first next()
		
		// check to make sure that the implementation is not extremely slow,
		// so that the more intensive test does not appear to freeze
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			Iterator<Integer> it = g.successorIterator(0);
			it.next();
		}
		long endTime = System.currentTimeMillis();
		
		assertTrue("Expected: true", endTime - startTime < 5);
			
		// run a much more intense test to ensure efficiency
		startTime = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
			Iterator<Integer> it = g.successorIterator(0);
			it.next();
		}
		endTime = System.currentTimeMillis();
		long time = endTime - startTime;
		System.out.printf("AdjList   SuccessorIterator time for construction and first "
				+ "next:   %3d ms%n",time);
		assertTrue("Expected: true", time < 70); // testing indicated that no lazy iterator should  
							   // ever take much longer than 50 ms
		m1points += 1.0*m1weight;
	}

	@Test
	public void testAMSuccessorIteratorIsLazy() {
		assertTrue("Expected: true", speedTestsOn);
		int numVertices = 10000;
		Set<Integer> keySet = new HashSet<Integer>();
		for (int i = 0; i < numVertices; i++) {
			keySet.add(i); // add 10000 vertices
		}
		Graph<Integer> g = new AdjacencyMatrixGraph<Integer>(keySet);
		for (int j = 0; j < numVertices; j++) {
			g.addEdge(0, j);  // add edges from 0 to all other vertices
		}
		// Run the speed test for iterator construction and first next()
		long startTime = System.currentTimeMillis();
		// Repeat the following 1000 times to make sure inefficiency is apparent
		for (int i = 0; i < 1000; i++) {
			Iterator<Integer> it = g.successorIterator(0);
			it.next();
		}
		long endTime = System.currentTimeMillis();
		long time = endTime - startTime;
		System.out.printf("AdjMatrix SuccessorIterator time for construction and first "
				+ "next:   %3d ms%n",time);
		assertTrue("Expected: true", time < 50); // testing indicated that no lazy iterator should ever
							   // take longer than 5 ms, while non-lazy could take hundreds
		m1points += 1.0*m1weight;
	}


	@Test
	public void testALPredecessorIteratorIsLazy() {
		assertTrue("Expected: true", speedTestsOn);
		int numVertices = 10000;
		Set<Integer> keySet = new HashSet<Integer>();
		for (int i = 0; i < numVertices; i++) {
			keySet.add(i); // add 10000 vertices
		}
		Graph<Integer> g = new AdjacencyListGraph<Integer>(keySet);
		for (int j = 0; j < numVertices; j++) {
			g.addEdge(0, j);  // add edges from 0 to all other vertices
		}
		
		// Run the speed test for iterator construction and first next()
		
		// check to make sure that the implementation is not extremely slow,
		// so that the more intensive test does not appear to freeze
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			Iterator<Integer> it = g.predecessorIterator(0);
			it.next();
		}
		long endTime = System.currentTimeMillis();
		
		assertTrue("Expected: true", endTime - startTime < 5);
			
		// run a much more intense test to ensure efficiency
		startTime = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
			Iterator<Integer> it = g.predecessorIterator(0);
			it.next();
		}
		endTime = System.currentTimeMillis();
		long time = endTime - startTime;
		System.out.printf("AdjList   PredecessorIterator time for construction and first "
				+ "next: %3d ms%n",time);
		assertTrue("Expected: true", time < 70); // testing indicated that no lazy iterator should  
							   // ever take much longer than 50 ms
		m1points += 1.0*m1weight;
	}

	@Test
	public void testAMPredecessorIteratorIsLazy() {
		assertTrue("Expected: true", speedTestsOn);
		int numVertices = 10000;
		Set<Integer> keySet = new HashSet<Integer>();
		for (int i = 0; i < numVertices; i++) {
			keySet.add(i); // add 10000 vertices
		}
		Graph<Integer> g = new AdjacencyMatrixGraph<Integer>(keySet);
		for (int j = 0; j < numVertices; j++) {
			g.addEdge(j, 0);  // add edges from all other vertices to 0
		}
		// Run the speed test for iterator construction and first next()
		long startTime = System.currentTimeMillis();
		// Repeat the following 1000 times to make sure inefficiency is apparent
		for (int i = 0; i < 1000; i++) {
			Iterator<Integer> it = g.predecessorIterator(0);
			it.next();
		}
		long endTime = System.currentTimeMillis();
		long time = endTime - startTime;
		System.out.printf("AdjMatrix PredecessorIterator time for construction and first "
				+ "next: %3d ms%n",time);
		assertTrue("Expected: true", time < 50); // testing indicated that no lazy iterator should ever
							   // take longer than 5 ms, while non-lazy could take hundreds
		m1points += 1.0*m1weight;
	}

	
	
	
	private long helperTestRelativeSpeedforAddRemoveEdge(Graph<Integer> g, int numVertices) {
		// Add edges everywhere
		for (int i = 0; i < numVertices; i++) {
			for (int j = 0; j < numVertices; j++) {
				assertFalse("Expected: false", g.hasEdge(i, j));
				g.addEdge(i, j);
				assertTrue("Expected: true", g.hasEdge(i, j));
			}
		}
		long startTime = System.currentTimeMillis();
		// Remove edges everywhere
		for (int i = 0; i < numVertices; i++) {
			for (int j = 0; j < numVertices; j++) {
				assertTrue("Expected: true", g.hasEdge(i, j));
				g.removeEdge(i, j);
				assertFalse("Expected: false", g.hasEdge(i, j));
			}
		}
		long endTime = System.currentTimeMillis();
		return (endTime - startTime);
	}

	@Test
	public void testRelativeSpeedforRemoveEdge() {
		assertTrue("Expected: true", speedTestsOn);
		int numVertices = 1000;
		Set<Integer> keySet = new HashSet<Integer>();
		for (int i = 0; i < numVertices; i++) {
			keySet.add(i);
		}
		Graph<Integer> gAL = new AdjacencyListGraph<Integer>(keySet);
		Graph<Integer> gAM = new AdjacencyMatrixGraph<Integer>(keySet);
		long timeAL = helperTestRelativeSpeedforAddRemoveEdge(gAL,numVertices);
		long timeAM = helperTestRelativeSpeedforAddRemoveEdge(gAM,numVertices);
		System.out.printf("RemoveEdges speed test: %4d ms for AdjList, "
				+ "%4d ms for AdjMatrix%n",timeAL,timeAM);
		// Would expect AdjList to be at least twice as slow at this task.
		assertTrue("Expected: true", timeAL > 2*timeAM);  
		m1points += 3*m1weight;
	}
	
	private long helperTestRelativeSpeedforOutDegree(Graph<Integer> g, int numVertices) {
		long startTime = System.currentTimeMillis();
		// Start with the empty graph
		for (int i = 0; i < numVertices; i++) {
			for (int j = 0; j < numVertices; j++) {
				if (j == i + 1) {
					g.addEdge(i, j); // add one edge for each vertex, at a specific point,
									 // to get a bit of variety in the out-degrees
				}
				// main test: out-degree calculation
				assertEquals(((j >= i + 1) ? 1 : 0), g.outDegree(i)); 
			}
		}
		long endTime = System.currentTimeMillis();
		return (endTime - startTime);
	}

	@Test
	public void testRelativeSpeedforOutDegree() {
		assertTrue("Expected: true", speedTestsOn);
		int numVertices = 800;
		Set<Integer> keySet = new HashSet<Integer>();
		for (int i = 0; i < numVertices; i++) {
			keySet.add(i);
		}
		Graph<Integer> gAL = new AdjacencyListGraph<Integer>(keySet);
		Graph<Integer> gAM = new AdjacencyMatrixGraph<Integer>(keySet);
		long timeAL = helperTestRelativeSpeedforOutDegree(gAL,numVertices);
		long timeAM = helperTestRelativeSpeedforOutDegree(gAM,numVertices);
		System.out.printf("OutDegree speed test:   %4d ms for AdjList, "
				+ "%4d ms for AdjMatrix%n",timeAL,timeAM);
		// Would expect AdjMatrix to be at least twice as slow at this task.
		assertTrue("Expected: true", timeAM > 2*timeAL);  
		m1points += 3*m1weight;
	}
	

	@AfterClass
	public static void printSummary() {
		System.out.print("\n ===============     ");
		System.out.print("Total points: ");
		System.out.print(m1points + "/" + MAX_POINTS);
		System.out.println("     ===============");
	}
}
