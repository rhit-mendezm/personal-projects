import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

//import editortrees.Node;

/**
 * 
 * A hash set implementation for Strings. Cannot insert null into the set. Other
 * requirements are given with each method.
 *
 * @author Matt Boutell and Marlon Mendez-Yanez. Created Oct 6, 2014.
 */
public class StringHashSet {

	// The initial size of the internal array.
	private static final int DEFAULT_CAPACITY = 5;

	// You'll want fields for the size (number of elements) and the internal
	// array of Nodes. I also added one for the capacity (the length
	// of the internal array).

	private int capacity;
	private int size;
	private Node[] nodes;
	private int currentModificationCount;

	private class Node {
		// TODO: Implement this class . These are just linked-list style
		// nodes, so you will need at least fields for the data and a reference
		// to the next node, plus a constructor. You can choose to use a
		// NULL_NODE at the end, or not. I chose not to do so this time.
		private String data;
		private Node next;

		private Node(String data, Node next) {
			this.data = data;
			this.next = next;
		} // Node
	} // Node

	/**
	 * Creates a Hash Set with the default capacity.
	 */
	public StringHashSet() {
		// Recall that using this as a method calls another constructor
		this(DEFAULT_CAPACITY);
	} // StringHashSet

	/**
	 * Creates a Hash Set with the given capacity.
	 */
	public StringHashSet(int initialCapacity) {
		initialize(initialCapacity);
	} // StringHashSet

	private void initialize(int initialCapacity) {
		// TODO: Set the capacity to the given capacity, and initialize the
		// other fields.
		// Why did we pull this out into a separate method? Perhaps another
		// method needs to initialize the hash set as well? (Hint)
		this.capacity = initialCapacity;
		this.size = 0;
		this.nodes = new Node[capacity];
		this.currentModificationCount = 0;
	} // initialize

	/**
	 * Calculates the hash code for Strings, using the x=31*x + y pattern. Follow
	 * the specification in the String.hashCode() method in the Java API. Note that
	 * the hashcode can overflow the max integer, so it can be negative. Later, in
	 * another method, you'll need to account for a negative hashcode by adding
	 * Integer.MAX_VALUE + 1 before you mod by the capacity (table size) to get the
	 * index.
	 *
	 * This method is NOT the place to calculate the index though.
	 *
	 * @param item
	 * @return The hash code for this String
	 */
	public static int stringHashCode(String item) {
		int total = 0;
		for (int i = 0; i < item.length(); i++)
			total = total * 31 + item.charAt(i);

		return total;
	} // stringHashCode

	/**
	 * Adds a new node if it is not there already. If there is a collision, then add
	 * a new node to the -front- of the linked list. Returns false if trying to add
	 * a duplicate.
	 * 
	 * Must operate in amortized O(1) time, assuming a good hashcode function.
	 *
	 * If the number of nodes in the hash table would be over double the capacity
	 * (that is, lambda > 2) as a result of adding this item, then first double the
	 * capacity and then rehash all the current items into the double-size table.
	 *
	 * @param item
	 * @return true if the item was successfully added (that is, if that hash table
	 *         was modified as a result of this call), false otherwise.
	 */
	public boolean add(String item) {
		if (contains(item))
			return false;

		double lambda = (size + 1) / (double) capacity;
		if (lambda > 2)
			doubleArray();

		int hashCode = stringHashCode(item);
		int index = getIndexFromHashCode(hashCode);
		nodes[index] = new Node(item, nodes[index]);
		size++;
		currentModificationCount++;

		return true;
	} // add

	/**
	 * Doubles the array length and recalculates each item's hashcode to place it in
	 * the newly sized array.
	 */
	private void doubleArray() {
		capacity *= 2;
		Node[] twiceNodes = new Node[capacity];
		for (int i = 0; i < capacity / 2; i++) {
			Node current = nodes[i];
			while (current != null) {
				int hashCode = stringHashCode(current.data);
				int index = getIndexFromHashCode(hashCode);
				twiceNodes[index] = new Node(current.data, twiceNodes[index]);
				current = current.next;
			} // end while
		} // end for
		nodes = twiceNodes;
	} // doubleArray

	/**
	 * Returns the index of an item based on its hashcode.
	 * 
	 * @param hashCode
	 * @param currentCapacity
	 * @return int, index
	 */
	private int getIndexFromHashCode(int hashCode) {
		if (hashCode < 0)
			hashCode += Integer.MAX_VALUE + 1;

		int index = hashCode % capacity;
		return index;
	} // getIndexFromHashCode

	/**
	 * Prints an array value on each line. Each line will be an array index followed
	 * by a colon and a list of Node data values, ending in null. For example, 0:
	 * hola null\n1: bonjour shalom null\n2: caio hello null\n3: null\n4: hi null\n
	 * This is from the unit test testAddAndRawStringNoRehash - there are more
	 * examples there. Use a StringBuilder, so you can build the string in O(n)
	 * time. (Repeatedly concatenating n strings onto a growing string gives O(n^2)
	 * time)
	 * 
	 * @return A slightly-formatted string, mostly used for debugging
	 */
	public String toRawString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < capacity; i++) {
			sb.append(i);
			sb.append(": ");
			Node current = nodes[i];
			while (current != null) {
				sb.append(current.data);
				sb.append(" ");
				current = current.next;
			} // end while
			sb.append("null\n");
		} // end for
		return sb.toString();
	} // toRawString

	/**
	 * Checks if the given item is in the hash table.
	 * 
	 * Must operate in O(1) time, assuming a good hashcode function.
	 *
	 * @param item
	 * @return True if and only if the item is in the hash table.
	 */
	public boolean contains(String item) {
		int hashCode = stringHashCode(item);
		int index = getIndexFromHashCode(hashCode);
		Node current = nodes[index];
		while (current != null) {
			if (current.data.equals(item))
				return true;

			current = current.next;
		} // end while
		return false;
	} // contains

	/**
	 * Returns the number of items added to the hash table. Must operate in O(1)
	 * time.
	 *
	 * @return The number of items in the hash table.
	 */
	public int size() {
		return size;
	} // size

	/**
	 * @return True iff the hash table contains no items.
	 */
	public boolean isEmpty() {
		return size == 0;
	} // isEmpty

	/**
	 * Removes all the items from the hash table. Resets the capacity to the
	 * DEFAULT_CAPACITY
	 */
	public void clear() {
		this.initialize(DEFAULT_CAPACITY);
	} // clear

	/**
	 * Removes the given item from the hash table if it is there. You do NOT need to
	 * resize down if the load factor decreases below the threshold.
	 * 
	 * @param item
	 * @return True If the item was in the hash table (or equivalently, if the table
	 *         changed as a result).
	 */
	public boolean remove(String item) {
		int hashCode = stringHashCode(item);
		int index = getIndexFromHashCode(hashCode);
		Node current = nodes[index];

		if (current == null)
			return false;

		if (current.data.equals(item)) {
			nodes[index] = current.next;
			size--;
			currentModificationCount++;
			return true;
		} // end if

		Node previous = current;
		current = current.next;

		while (current != null) {
			if (current.data.equals(item)) {
				previous.next = current.next;
				size--;
				currentModificationCount++;
				return true;
			} // end if
			previous = previous.next;
			current = current.next;
		} // end while

		return false;
	} // remove

	/**
	 * Adds all the items from the given collection to the hash table.
	 *
	 * @param collection
	 * @return True if the hash table is modified in any way.
	 */
	public boolean addAll(Collection<String> collection) {
		for (String string : collection)
			add(string);

		return true;
	} // addAll

	/**
	 * Challenge Feature: Returns an iterator over the set. Return the items in any
	 * order that you can do efficiently. Should throw a NoSuchElementException if
	 * there are no more items and next() is called. Should throw a
	 * ConcurrentModificationException if next() is called and the set has been
	 * mutated since the iterator was created.
	 *
	 * @return an iterator.
	 */
	public Iterator<String> iterator() {
		return new StringIterator();
	} // iterator

	/**
	 * Iterates through each chain in the HashSet in-order.
	 */
	private class StringIterator implements Iterator<String> {

		private int index;
		private Node node;
		private int iterations;
		private int originalModificationCount;
		private int originalSize;

		private StringIterator() {
			IndexAndNode container = getNextNode(0);
			index = container.index;
			node = container.node;
			iterations = 0;
			originalModificationCount = currentModificationCount;
			originalSize = size;
		} // StringIterator

		@Override
		public boolean hasNext() {
			return iterations != originalSize;
		} // hasNext

		@Override
		public String next() {
			if (currentModificationCount != originalModificationCount)
				throw new ConcurrentModificationException();

			if (!hasNext())
				throw new NoSuchElementException();

			Node next = node;
			if (node.next != null)
				node = node.next;

			else {
				IndexAndNode nextContainer = getNextNode(index + 1);
				index = nextContainer.index;
				node = nextContainer.node;
			} // end else

			iterations++;
			return next.data;
		} // next

	} // StringIterator

	private class IndexAndNode {
		private int index;
		private Node node;

		private IndexAndNode(int index, Node node) {
			this.index = index;
			this.node = node;
		} // FirstIndexAndNode
	} // FirstIndexAndNode

	/**
	 * Finds the first occupied slot of the nodes array and returns the node and its
	 * index.
	 * 
	 * @param index
	 * @return IndexAndNode, object containing node and its index
	 */
	private IndexAndNode getNextNode(int index) {
		for (int i = index; i < nodes.length; i++)
			if (nodes[i] != null)
				return new IndexAndNode(i, nodes[i]);

		return new IndexAndNode(0, new Node(null, null));
	} // IndexAndNode

	// Challenge Feature: If you have an iterator, this is easy. Use a
	// StringBuilder, so you can build the string in O(n) time. (Repeatedly
	// concatenating n strings onto a string gives O(n^2) time)
	// Format it like any other Collection's toString (like [a, b, c])
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator<String> iter = new StringIterator();

		sb.append("[");
		while (iter.hasNext()) {
			sb.append(iter.next());
			sb.append(", ");
		} // end for

		sb.delete(sb.length() - 2, sb.length());
		sb.append("]");
		return sb.toString();
	} // toString
} // StringHashSet
