package graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class AdjacencyListGraph<T> extends Graph<T> {
	Map<T, Vertex> keyToVertex;

	private class Vertex {
		T key;
		List<Vertex> successors;
		List<Vertex> predecessors;

		Vertex(T key) {
			this.key = key;
			this.successors = new ArrayList<Vertex>();
			this.predecessors = new ArrayList<Vertex>();
		} // Vertex
	} // Vertex

	AdjacencyListGraph(Set<T> keys) {
		this.keyToVertex = new HashMap<T, Vertex>();
		for (T key : keys) {
			Vertex v = new Vertex(key);
			this.keyToVertex.put(key, v);
		} // end for
	} // AdjacencyListGraph

	@Override
	public int size() {
		return keyToVertex.size();
	} // size

	// Note: I arbitrarily chose to iterate through the vertices's predecessors
	// instead of their successors.
	@Override
	public int numEdges() {
		int sum = 0;
		for (Vertex v : keyToVertex.values())
			sum += v.predecessors.size();

		return sum;
	} // numEdges

	@Override
	public boolean addEdge(T from, T to) {
		if (hasEdge(from, to))
			return false;

		Vertex predecessor = keyToVertex.get(from);
		Vertex successor = keyToVertex.get(to);
		predecessor.successors.add(successor);
		successor.predecessors.add(predecessor);
		return true;
	} // addEdge

	@Override
	public boolean hasVertex(T key) {
		return keyToVertex.containsKey(key);
	} // hasVertex

	@Override
	public boolean hasEdge(T from, T to) throws NoSuchElementException {
		if (!keyToVertex.containsKey(from) || !keyToVertex.containsKey(to))
			throw new NoSuchElementException();

		Vertex predecessor = keyToVertex.get(from);
		Vertex successor = keyToVertex.get(to);

		return predecessor.successors.contains(successor);
	} // hasEdge

	@Override
	public boolean removeEdge(T from, T to) throws NoSuchElementException {
		if (!hasEdge(from, to))
			return false;

		Vertex predecessor = keyToVertex.get(from);
		Vertex successor = keyToVertex.get(to);

		predecessor.successors.remove(successor);
		successor.predecessors.remove(predecessor);
		return true;
	} // removeEdge

	@Override
	public int outDegree(T key) {
		if (!hasVertex(key))
			throw new NoSuchElementException();

		Vertex vertex = keyToVertex.get(key);
		return vertex.successors.size();
	} // outDegree

	@Override
	public int inDegree(T key) {
		if (!hasVertex(key))
			throw new NoSuchElementException();

		Vertex vertex = keyToVertex.get(key);
		return vertex.predecessors.size();
	} // inDegree

	@Override
	public Set<T> keySet() {
		return keyToVertex.keySet();
	} // keySet

	@Override
	public Set<T> successorSet(T key) {
		if (!hasVertex(key))
			throw new NoSuchElementException();

		Vertex vertex = keyToVertex.get(key);
		Set<T> keys = new HashSet<>();
		for (Vertex v : vertex.successors)
			keys.add(v.key);

		return keys;
	} // successorSet

	@Override
	public Set<T> predecessorSet(T key) {
		if (!hasVertex(key))
			throw new NoSuchElementException();

		Vertex vertex = keyToVertex.get(key);
		Set<T> keys = new HashSet<>();
		for (Vertex v : vertex.predecessors)
			keys.add(v.key);

		return keys;
	} // predecessorSet

	@Override
	public Iterator<T> successorIterator(T key) {
		if (!hasVertex(key))
			throw new NoSuchElementException();

		Vertex vertex = keyToVertex.get(key);
		return new KeyIterator(vertex.successors);
	} // sucessorIterator

	@Override
	public Iterator<T> predecessorIterator(T key) {
		if (!hasVertex(key))
			throw new NoSuchElementException();

		Vertex vertex = keyToVertex.get(key);
		return new KeyIterator(vertex.predecessors);
	} // predecessorIterator

	/**
	 * Given the list of predecessors/successors of a vertex, iterate through each
	 * index position in that list in increments of 1.
	 */
	private class KeyIterator implements Iterator<T> {

		private List<Vertex> vertices;
		private int iterated;
		private int originalSize;

		private KeyIterator(List<Vertex> list) {
			this.vertices = list;
			this.iterated = 0;
			this.originalSize = list.size();
		} // KeyIterator

		@Override
		public boolean hasNext() {
			return iterated != originalSize;
		} // hasNext

		@Override
		public T next() {
			if (!hasNext())
				throw new NoSuchElementException();

			Vertex next = vertices.get(iterated);
			iterated++;
			return next.key;
		} // next

	} // Iterator
} // AdjacencyListGraph
