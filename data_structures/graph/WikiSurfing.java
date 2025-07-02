package graphs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WikiSurfing {

	/**
	 * Generates the Wikipedia graph for the Living People category, using adjacency lists.
	 * @return graph
	 */
	public static AdjacencyListGraph<String> wikiLivingPeopleGraphAL(boolean verbose) {
		String pageNamesFileName = "../GraphSurfingData/wiki-livingpeople-names.txt";
		String linksFileName = "../GraphSurfingData/wiki-livingpeople-links.txt";
		Map<Integer,String> indexToKey = new HashMap<Integer,String>();
		Map<String,Integer> keyToIndex = new HashMap<String,Integer>();
		if (verbose) {
			System.out.println("Reading vertices");
		}
		readVertices(indexToKey, keyToIndex, pageNamesFileName);
		AdjacencyListGraph<String> graph = new AdjacencyListGraph<String>(keyToIndex.keySet());
		if (verbose) {
			System.out.println("Reading edges");
		}
		readEdges(graph, indexToKey, linksFileName);
		if (verbose) {
			System.out.printf("Constructed LivingPeople graph with %d vertices and %d edges%n",graph.size(),graph.numEdges());
		}
		return graph;
	}
	
	
	/**
	 * Reads in the page names (vertex labels). 
	 * @param indexToKey, a map to populate with index-to-key translations
	 * @param keyToIndex, a map to populate with key-to-index translations
	 * @param pageNamesFileName, the file containing all keys and associated indices
	 */
	private static void readVertices(Map<Integer,String> indexToKey, 
			Map<String,Integer> keyToIndex, String pageNamesFileName) {
		Scanner sc = null;
		try {
			sc = new Scanner(new File(pageNamesFileName), "utf-8");
		} catch (FileNotFoundException e) {
			System.err.printf("Could not find file %s%n",pageNamesFileName);
		}
		// Searches for a number (-> index), followed by a space, followed by the remainder of the line (-> pageName)
		Pattern pageNamePattern = Pattern.compile("(\\d*)( )(.*)"); 
		while (sc.hasNextLine()) {
			Matcher matcher = pageNamePattern.matcher(sc.nextLine());
			if (matcher.find()) {
				int index = Integer.parseInt(matcher.group(1));
				String pageName = matcher.group(3);
				indexToKey.put(index, pageName);
				keyToIndex.put(pageName, index);
			}
		}
	}
		
		
	/**
	 * Reads in the edges from the given file and adds them to the graph.
	 * Only adds edges for which there exist vertices already in the graph.
	 * @param graph, the graph to add edges to
	 * @param indexToKey, a map from indices to keys
	 * @param linksFileName, the file of index pairs to read edges from
	 */
	private static void readEdges(Graph<String> graph, Map<Integer,String> indexToKey, String linksFileName) {
		/*
		 * Read in the edges
		 */
		Scanner sc = null;
		try {
			sc = new Scanner(new File(linksFileName));
		} catch (FileNotFoundException e) {
			System.err.printf("Could not find file %s%n",linksFileName);
		}
		while (sc.hasNext()) {
			Integer fromInd = sc.nextInt();
			String from = indexToKey.get(fromInd);
			Integer toInd = sc.nextInt();
			String to = indexToKey.get(toInd);
			if (graph.hasVertex(to) && graph.hasVertex(from)) {
				graph.addEdge(from, to);
			}
		}
	}
}
