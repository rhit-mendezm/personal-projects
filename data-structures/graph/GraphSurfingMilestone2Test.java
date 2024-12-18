package graphs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Complete test cases for Milestone 2
 * 
 * 
 * @author Nate Chenette
 * 
 */
public class GraphSurfingMilestone2Test {

	// Toggle this to false to speed up early testing process.
	private static boolean runLivingPeopleGraphTests = true;

	// Toggle this to false to suppress output.
	private static boolean verbose = true;
	
	private static int m2points = 0;
	private static int m2bonusPoints = 0;
	private static int m2weight = 1;
	private static final int MAX_POINTS = 60;
	private static Graph<String> livingPeopleALGraph;
	private static boolean livingPeopleShortestPathWorking = false;
	


	@BeforeClass
	public static void buildLivingPeopleGraph() {
		if (runLivingPeopleGraphTests) {
			livingPeopleALGraph = WikiSurfing.wikiLivingPeopleGraphAL(verbose);
		}
	}

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
	
	private void helperTestStronglyConnectedComponentExampleGraphs(Graph<String> g, Graph<Integer> g2) {
		Set<String> answer = new HashSet<String>(Arrays.asList("a"));
		assertEquals(answer, g.stronglyConnectedComponent("a"));
		answer = new HashSet<String>(Arrays.asList("c","d","f"));
		assertEquals(answer, g.stronglyConnectedComponent("f"));
		
		Set<Integer> answer2 = new HashSet<Integer>(Arrays.asList(5));
		assertEquals(answer2, g2.stronglyConnectedComponent(5));
		answer2 = new HashSet<Integer>(Arrays.asList(0,1));
		assertEquals(answer2, g2.stronglyConnectedComponent(1));
		answer2 = new HashSet<Integer>(Arrays.asList(2,3,4,6));
		assertEquals(answer2, g2.stronglyConnectedComponent(2));
		m2points += 9*m2weight;
		try {
			g.stronglyConnectedComponent("z");
			fail("Did not throw NoSuchElementException");
		} catch (Exception e) {
			if (!(e instanceof NoSuchElementException)) {
				fail("Did not throw NoSuchElementException");
			}
		}
		m2points += 1*m2weight;
	}

	@Test
	public void testALStronglyConnectedComponentExampleGraphs() {
		helperTestStronglyConnectedComponentExampleGraphs(makeExampleALGraph(), makeExample2ALGraph());
	}

	@Test
	public void testAMStronglyConnectedComponentExampleGraphs() {
		helperTestStronglyConnectedComponentExampleGraphs(makeExampleAMGraph(), makeExample2AMGraph());
	}
	
	private void helperTestShortestPathExampleGraphs(Graph<String> g, Graph<Integer> g2) {
		assertEquals(Arrays.asList("a","b"), g.shortestPath("a","b"));
		assertEquals(Arrays.asList("a","c"), g.shortestPath("a","c"));
		assertEquals(Arrays.asList("b","d","c"), g.shortestPath("b","c"));
		assertEquals(Arrays.asList("f","c","d","e"), g.shortestPath("f","e"));
		assertTrue(Arrays.asList("a","b","d").equals(g.shortestPath("a","d")) 
				|| Arrays.asList("a","c","d").equals(g.shortestPath("a","d")));
		
		assertEquals(Arrays.asList(0,1), g2.shortestPath(0,1));
		assertEquals(Arrays.asList(0,2,4,6), g2.shortestPath(0,6));
		assertEquals(Arrays.asList(6,2,3), g2.shortestPath(6,3));
		assertEquals(Arrays.asList(4,6,2,3), g2.shortestPath(4,3));
		assertEquals(Arrays.asList(1,0,2,4,5), g2.shortestPath(1,5));
		m2points += 7*m2weight;
		
		assertEquals(null, g.shortestPath("b","a"));
		assertEquals(null, g.shortestPath("f","b"));
		assertEquals(null, g2.shortestPath(2,0));
		assertEquals(null, g2.shortestPath(4,1));
		m2points += 2*m2weight;
		try {
			g.shortestPath("a","z");
			g.shortestPath("z","a");
			fail("Did not throw NoSuchElementException");
		} catch (Exception e) {
			if (!(e instanceof NoSuchElementException)) {
				fail("Did not throw NoSuchElementException");
			}
		}
		m2points += 1*m2weight;
		
	}
	
	@Test
	public void testALShortestPathExampleGraphs() {
		helperTestShortestPathExampleGraphs(makeExampleALGraph(), makeExample2ALGraph());
	}

	@Test
	public void testAMShortestPathExampleGraphs() {
		helperTestShortestPathExampleGraphs(makeExampleAMGraph(), makeExample2AMGraph());
	}

	@Test(timeout=60000) // time limit: 1 minute
	public void testWikiSurfingShortestPath() {
		assertTrue(runLivingPeopleGraphTests);
		
		List<String> path1 = livingPeopleALGraph.shortestPath("Hope Solo", "Hope Solo");
		if (verbose) System.out.println("Shortest path solution: " + path1);
		assertEquals(1,path1.size()); // Check to make sure it is a minimum length path
		assertTrue(isValidPath(path1)); // Check to make sure all are valid edges
		
		List<String> path2 = livingPeopleALGraph.shortestPath("Manuel Blum", "Lenore Blum");
		if (verbose) System.out.println("Shortest path solution: " + path2);
		assertEquals(2,path2.size()); // Check to make sure it is a minimum length path
		assertTrue(isValidPath(path2)); // Check to make sure all are valid edges
		
		List<String> path3 = livingPeopleALGraph.shortestPath("Usain Bolt", "Taylor Swift");
		if (verbose) System.out.println("Shortest path solution: " + path3);
		assertEquals(3,path3.size()); // Check to make sure it is a minimum length path
		assertTrue(isValidPath(path3)); // Check to make sure all are valid edges

		List<String> path4 = livingPeopleALGraph.shortestPath("Mike Pence", "50 Cent");
		if (verbose) System.out.println("Shortest path solution: " + path4);
		assertEquals(4,path4.size()); // Check to make sure it is a minimum length path
		assertTrue(isValidPath(path4)); // Check to make sure all are valid edges

		List<String> path6 = livingPeopleALGraph.shortestPath("Ban Ki-moon", "Mae Jemison");
		if (verbose) System.out.println("Shortest path solution: " + path6);
		assertEquals(6,path6.size()); // Check to make sure it is a minimum length path
		assertTrue(isValidPath(path6)); // Check to make sure all are valid edges

		List<String> path7 = livingPeopleALGraph.shortestPath("Randall Munroe", "Randall Cobb (American football)");
		if (verbose) System.out.println("Shortest path solution: " + path7);
		assertEquals(7,path7.size()); // Check to make sure it is a minimum length path
		assertTrue(isValidPath(path7)); // Check to make sure all are valid edges

		List<String> path8 = livingPeopleALGraph.shortestPath("Nacho (footballer, born 1967)", "Tacko Fall");
		if (verbose) System.out.println("Shortest path solution: " + path8);
		assertEquals(8,path8.size()); // Check to make sure it is a minimum length path
		assertTrue(isValidPath(path8)); // Check to make sure all are valid edges
		
		List<String> path0 = livingPeopleALGraph.shortestPath("Zaza Pachulia", "Mariyam Azza");
		if (verbose) System.out.println("Shortest path solution: " + path0);
		assertTrue(path0 == null); // Check to make sure there was no solution
		
		livingPeopleShortestPathWorking = true;
		m2points += 10*m2weight;
	}
	
	private boolean isValidPath(List<String> path) {
		boolean result = true;
		for (int i = 1; i < path.size(); i++) { 
			result &= livingPeopleALGraph.hasEdge(path.get(i-1), path.get(i));
		}
		return result;
	}

	@Test(timeout=60000) // time limit: 1 minute
	public void testWikiSurfingStronglyConnectedComponent() {
		assertTrue(runLivingPeopleGraphTests);
		
		Set<String> result;
		
		Set<String> cyclocross = new HashSet<String>();
		cyclocross.add("Jeremy Powers");
		cyclocross.add("Jonathan Page (cyclist)");
		cyclocross.add("Tim Johnson (cyclist)");
		cyclocross.add("Ryan Trebon");
		cyclocross.add("Richard Sachs");
		cyclocross.add("Todd Wells");
		result = livingPeopleALGraph.stronglyConnectedComponent("Jeremy Powers");
		assertEquals(cyclocross,result);
		if (verbose) System.out.println("Found cyclocross component");
		
		result = livingPeopleALGraph.stronglyConnectedComponent("Mirtha Marrero");
		assertEquals(5,result.size());
		if (verbose) System.out.println("Found All-American Girls Professional Baseball League component");

		result = livingPeopleALGraph.stronglyConnectedComponent("Ken Climo");
		assertEquals(20,result.size());
		if (verbose) System.out.println("Found professional disc golf component");
		
		result = livingPeopleALGraph.stronglyConnectedComponent("Nivaleta Iloai");
		assertEquals(20,result.size());
		if (verbose) System.out.println("Found politicians of Wallis and Futuna component");

		result = livingPeopleALGraph.stronglyConnectedComponent("Gary McCloy");
		assertEquals(21,result.size());
		if (verbose) System.out.println("Found lawn bowling component");
		
		result = livingPeopleALGraph.stronglyConnectedComponent("Ben van den Bogaart");
		assertEquals(29,result.size());
		if (verbose) System.out.println("Found sidecarcross component");
		
		result = livingPeopleALGraph.stronglyConnectedComponent("K. B. Yesu Vara Prasad");
		assertEquals(41,result.size()); 
		if (verbose) System.out.println("Found Bible Society of India, Bangalore component");

		result = livingPeopleALGraph.stronglyConnectedComponent("Mariyam Afeefa");
		assertEquals(102,result.size()); 
		if (verbose) System.out.println("Found Maldivian film industry component");
		
		result = livingPeopleALGraph.stronglyConnectedComponent("Larry Bird");
		assertEquals(329699,result.size());
		if (verbose) System.out.println("Found the huge component");
		
		result = livingPeopleALGraph.stronglyConnectedComponent("Donald Knuth");
		assertEquals(329699,result.size());
		if (verbose) System.out.println("Found the huge component again");
		

		m2points += 10*m2weight;
	}
	
	@Test
	public void testWikiSurfingFindChallengePair() {
		assertTrue(runLivingPeopleGraphTests);
		Scanner sc = null;
		String from = "";
		String to = "";
		String pairFileName = "challenge-pair.txt";
		try {
			sc = new Scanner(new File(pairFileName));
		} catch (FileNotFoundException e) {
			System.err.printf("Could not find file %s%n",pairFileName);
		}
		if (sc.hasNextLine()) {
			from = sc.nextLine();
		}
		if (sc.hasNextLine()) {
			to = sc.nextLine();
		}
		List<String> sol = livingPeopleALGraph.longestShortestPath(from,to);
		int len = sol.size();
		int minLength = 8;
		if (len > minLength) {
			m2bonusPoints = (int) (m2weight * (len - minLength) * 2 / 3);
		}
		if (verbose) {
			System.out.printf("Challenge pair [%s, %s] has shortest path length %d: %d bonus points awarded%n",from,to,len,m2bonusPoints);
		}
	}

	

	@AfterClass
	public static void printSummary() {
		if (livingPeopleShortestPathWorking) {
			m2points += m2bonusPoints;
		}
		System.out.print("\n ===============     ");
		System.out.print("Total points: ");
		System.out.print(m2points + "/" + MAX_POINTS);
		System.out.println("     ===============");
	}
}
