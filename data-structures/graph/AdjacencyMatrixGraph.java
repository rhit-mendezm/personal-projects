package graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class AdjacencyMatrixGraph<T> extends Graph<T> {
	Map<T, Integer> keyToIndex;
	List<T> indexToKey;
	int[][] matrix;

	AdjacencyMatrixGraph(Set<T> keys) {
		int size = keys.size();
		this.keyToIndex = new HashMap<T, Integer>();
		this.indexToKey = new ArrayList<T>();
		this.matrix = new int[size][size];
		// need to populate keyToIndex and indexToKey with info from keys
		Integer integer = 0;
		for (T key : keys) {
			this.keyToIndex.put(key, integer);
			this.indexToKey.add(key);
			integer++;
		} // end for
	} // AdjacencyMatrixGraph

	@Override
	public int size() {
		return matrix.length;
	} // size

	@Override
	public int numEdges() {
		int edges = 0;
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[i].length; j++)
				if (matrix[i][j] == 1)
					edges++;

		return edges;
	} // numEdges

	@Override
	public boolean addEdge(T from, T to) {
		if (hasEdge(from, to))
			return false;

		int row = keyToIndex.get(from);
		int col = keyToIndex.get(to);

		matrix[row][col] = 1;
		return true;
	} // addEdge

	@Override
	public boolean hasVertex(T key) {
		return keyToIndex.containsKey(key);
	} // hasVertex

	@Override
	public boolean hasEdge(T from, T to) throws NoSuchElementException {
		if (!keyToIndex.containsKey(from) || !keyToIndex.containsKey(to))
			throw new NoSuchElementException();

		int row = keyToIndex.get(from);
		int col = keyToIndex.get(to);

		return matrix[row][col] == 1;
	} // hasEdge

	@Override
	public boolean removeEdge(T from, T to) throws NoSuchElementException {
		if (!hasEdge(from, to))
			return false;

		int row = keyToIndex.get(from);
		int col = keyToIndex.get(to);
		matrix[row][col] = 0;

		return true;
	} // removeEdge

	@Override
	public int outDegree(T key) {
		if (!hasVertex(key))
			throw new NoSuchElementException();

		int row = keyToIndex.get(key);
		int successors = 0;
		for (int col = 0; col < matrix[row].length; col++)
			if (matrix[row][col] == 1)
				successors++;

		return successors;
	} // outDegree

	@Override
	public int inDegree(T key) {
		if (!hasVertex(key))
			throw new NoSuchElementException();

		int col = keyToIndex.get(key);
		int predecessors = 0;
		for (int row = 0; row < matrix.length; row++)
			if (matrix[row][col] == 1)
				predecessors++;

		return predecessors;
	} // inDegree

	@Override
	public Set<T> keySet() {
		return keyToIndex.keySet();
	} // keySet

	@Override
	public Set<T> successorSet(T key) {
		if (!hasVertex(key))
			throw new NoSuchElementException();

		int row = keyToIndex.get(key);
		Set<T> keys = new HashSet<>();
		for (int col = 0; col < matrix[row].length; col++) {
			int current = matrix[row][col];
			if (current == 1) {
				T successorKey = indexToKey.get(col);
				keys.add(successorKey);
			} // end if
		} // end for

		return keys;
	} // successorSet

	@Override
	public Set<T> predecessorSet(T key) {
		if (!hasVertex(key))
			throw new NoSuchElementException();

		int col = keyToIndex.get(key);
		Set<T> keys = new HashSet<>();
		for (int row = 0; row < matrix.length; row++) {
			int current = matrix[row][col];
			if (current == 1) {
				T predecessorKey = indexToKey.get(row);
				keys.add(predecessorKey);
			} // end if
		} // end for

		return keys;
	} // predecessorSet

	@Override
	public Iterator<T> successorIterator(T key) {
		if (!hasVertex(key))
			throw new NoSuchElementException();

		return new SuccessorIterator(key);
	} // successorIterator

	@Override
	public Iterator<T> predecessorIterator(T key) {
		if (!hasVertex(key))
			throw new NoSuchElementException();

		return new PredecessorIterator(key);
	} // predecessorIterator

	/**
	 * Given a key, the row index can be found using the keyToIndex Map. Then,
	 * iterate through each column of that row to find 1s, which represent an edge.
	 */
	private class SuccessorIterator implements Iterator<T> {

		private int row;
		private int col;
		private T next;

		private SuccessorIterator(T key) {
			this.row = keyToIndex.get(key);
			this.col = 0;
			getNext();
		} // KeyIterator

		@Override
		public boolean hasNext() {
			return getNext();
		} // hasNext

		@Override
		public T next() {
			if (!hasNext())
				throw new NoSuchElementException();

			col++;
			return next;
		} // next

		private boolean getNext() {
			while (col < matrix.length) {
				if (matrix[row][col] == 1) {
					next = indexToKey.get(col);
					return true;
				} // end if
				col++;
			} // end while
			return false;
		} // getNext
	} // SuccessorIterator

	/**
	 * Given a key, the column index can be found using the keyToIndex Map. Then,
	 * iterate through each row of that column to find 1s, which represent an edge.
	 */
	private class PredecessorIterator implements Iterator<T> {

		private int row;
		private int col;
		private T next;

		private PredecessorIterator(T key) {
			this.row = 0;
			this.col = keyToIndex.get(key);
			getNext();
		} // PredecessorIterator

		@Override
		public boolean hasNext() {
			return getNext();
		} // hasNext

		@Override
		public T next() {
			if (!hasNext())
				throw new NoSuchElementException();

			row++;
			return next;
		} // next

		private boolean getNext() {
			while (row < matrix[col].length) {
				if (matrix[row][col] == 1) {
					next = indexToKey.get(row);
					return true;
				} // end if
				row++;
			} // end while
			return false;
		} // getNext
	} // PredecessorIterator

} // AdjacencyMatrixGraph
