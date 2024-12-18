package editortrees;

import editortrees.Node.Code;

/**
 * A height-balanced binary tree with rank that could be the basis for a text
 * editor.
 * 
 * @author Jack Cooperman
 * @author Marlon Mendez-Yanez
 * 
 *         TODO: Acknowledge anyone else you got help from here, along with the
 *         help they provided:
 * 
 *         Dr. Matt Boutell: - toRankString(), delete(int pos, NodeInfo info)
 * 
 *         TA Dominic: - add(char ch, int pos), <br>
 *         leftRightRotate(Node parent, Node child, NodeInfo info),
 *         rightLeftRotate(Node parent, Node child, NodeInfo info),
 *         EditTree(String s)
 * 
 *         TA Ben: - ranksMatchLeftSubtreeSize(), EditTree(e), delete()
 * 
 */
public class EditTree {

	Node root;
	private int size;
	private int rotationCount;
	private DisplayableBinaryTree display;

	/**
	 * MILESTONE 1
	 * 
	 * Construct an empty tree.
	 */
	public EditTree() {
		root = Node.NULL_NODE;
		size = 0;
		rotationCount = 0;
	} // EditTree

	/**
	 * MILESTONE 1
	 * 
	 * Construct a single-node tree whose element is ch.
	 * 
	 * @param ch
	 */
	public EditTree(char ch) {
		root = new Node(ch);
		size = 1;
		rotationCount = 0;
	} // EditTree

	/**
	 * MILESTONE 2
	 * 
	 * Make this tree be a copy of e, with all new nodes, but the same shape and
	 * contents. You can write this one recursively, but you may not want your
	 * helper to be in the Node class.
	 * 
	 * @param e
	 */
	public EditTree(EditTree e) {
		root = cloneTree(e.root);
		size = e.size;
		rotationCount = 0;
	} // EditTree

	private Node cloneTree(Node current) {
		if (current == Node.NULL_NODE)
			return Node.NULL_NODE;

		Node newRoot = new Node(current.data, cloneTree(current.left), cloneTree(current.right));
		newRoot.rank = current.rank;
		newRoot.balance = current.balance;
		return newRoot;
	} // cloneTree

	/**
	 * MILESTONE 3
	 * 
	 * Create an EditTree whose toString is s. This can be done in O(N) time, where
	 * N is the size of the tree (note that repeatedly calling insert() would be O(N
	 * log N), so you need to find a more efficient way to do this.
	 * 
	 * @param s
	 */
	public EditTree(String s) {
		size = 0;
		root = constructInOrderTree(s, 0, s.length() - 1);
		rotationCount = 0;
	} // EditTree

	/**
	 * Helper method to construct a tree given an in-order string representation of
	 * a tree. Traverses through each node once to have O(n) runtime. It assumes
	 * each node has a rank of 1. Then, if the current node does not have a left
	 * child, the node rank is 0. If the current node's left child has only a left
	 * child, the current node's rank is equivalent to its left child's rank plus
	 * one. If the current node's left child has only a right child, the current
	 * node's rank is equivalent to two by observation; this is mainly due to the
	 * fact the tree is height-balanced. Otherwise, if the current node's left child
	 * is a full node, its rank is equivalent to its left child's rank plus three by
	 * similar observation.
	 * 
	 * @param s
	 * @param begin
	 * @param end
	 * @return
	 */
	private Node constructInOrderTree(String s, int begin, int end) {
		if (end < begin)
			return Node.NULL_NODE;

		if (begin == end) {
			size++;
			return new Node(s.charAt(begin));
		} // end if

		int index = (end - begin) / 2 + begin;
		char data = s.charAt(index);
		Node current = new Node(data);
		size++;

		current.left = constructInOrderTree(s, begin, index - 1);
		current.right = constructInOrderTree(s, index + 1, end);
		current.rank++;

		if (current.left == Node.NULL_NODE)
			current.rank--;

		else if (current.left.hasLeftChildOnly())
			current.rank = current.left.rank + 1;

		else if (current.left.hasRightChildOnly())
			current.rank = 2;

		else if (current.left.isFullNode())
			current.rank = current.left.rank + 3;

		if (current.hasLeftChildOnly())
			current.balance = Code.LEFT;

		else if (current.hasRightChildOnly())
			current.balance = Code.RIGHT;

		return current;
	} // constructInOrderTree

	/**
	 * MILESTONE 1
	 * 
	 * return the string produced by an in-order traversal of this tree
	 */
	@Override
	public String toString() {
		return root.toString();
	} // toString

	/**
	 * MILESTONE 1
	 * 
	 * Just modify the value of this.size whenever adding or removing a node. This
	 * is O(1).
	 * 
	 * @return the number of nodes in this tree, not counting the NULL_NODE if you
	 *         have one.
	 */
	public int size() {
		return this.size; // nothing else to do here.
	} // size

	/**
	 * MILESTONE 1
	 * 
	 * If the tree is empty, the root of the tree is constructed. Otherwise, the
	 * node is added as the rightmost leaf of the tree.
	 * 
	 * @param ch character to add to the end of this tree.
	 */
	public void add(char ch) {
		size++;
		NodeInfo info = new NodeInfo(0, true);
		root = root.add(ch, size, info);
		rotationCount += info.rotationCount;
	} // add

	/**
	 * MILESTONE 1
	 * 
	 * The node is added to the given position of the tree.
	 * 
	 * @param ch  character to add
	 * 
	 * @param pos character added in this in-order position Valid positions range
	 *            from 0 to the size of the tree, inclusive (if called with size, it
	 *            will append the character to the end of the tree).
	 * @throws IndexOutOfBoundsException if pos is negative or too large for this
	 *                                   tree.
	 */
	public void add(char ch, int pos) throws IndexOutOfBoundsException {
		if (pos < 0 || pos > size)
			throw new IndexOutOfBoundsException();

		size++;
		NodeInfo info = new NodeInfo(0, true);
		root = root.add(ch, pos, info);
		rotationCount += info.rotationCount;
	} // add

	/**
	 * Container class that tracks: <br>
	 * - rotation count <br>
	 * - status of continuing rebalancing <br>
	 * - data of removed node <br>
	 * - status of deleting or inserting <br>
	 * 
	 */
	public class NodeInfo {
		public int rotationCount;
		public boolean continueRebalancing;
		public char dataRemoved;
		public boolean isDeleting;

		NodeInfo(int rotationCount, boolean continueRebalancing) {
			this.rotationCount = rotationCount;
			this.continueRebalancing = continueRebalancing;
			this.dataRemoved = ' ';
			this.isDeleting = false;
		} // NodeInfo
	} // NodeInfo

	/**
	 * MILESTONE 1
	 * 
	 * This one asks for more info from each node. You can write it similar to the
	 * arraylist-based toString() method from the BinarySearchTree assignment.
	 * However, the output isn't just the elements, but the elements AND ranks.
	 * Former students recommended that this method, while making it a little harder
	 * to pass tests initially, saves them time later since it catches weird errors
	 * that occur when you don't update ranks correctly. For the tree with root b
	 * and children a and c, it should return the string: [b1, a0, c0] There are
	 * many more examples in the unit tests.
	 * 
	 * @return The string of elements and ranks, given in an PRE-ORDER traversal of
	 *         the tree.
	 */
	public String toRankString() {
		if (root == Node.NULL_NODE)
			return "[]";

		StringBuilder sb = new StringBuilder();
		sb.append("[");
		root.toRankString(sb);
		sb.delete(sb.length() - 2, sb.length());
		sb.append("]");

		return sb.toString();
	} // toRankString

	/**
	 * MILESTONE 1
	 * 
	 * @param pos position in the tree
	 * @return the character at that position
	 * @throws IndexOutOfBoundsException if pos is negative or too big. Note that
	 *                                   the pos is now Exclusive of the size of the
	 *                                   tree, since there is no character there.
	 *                                   But you can still use your size
	 *                                   field/method to determine this.
	 */
	public char get(int pos) throws IndexOutOfBoundsException {
		if (pos < 0 || pos >= size)
			throw new IndexOutOfBoundsException();

		return root.get(pos).data;
	} // get

	/**
	 * MILESTONE 1
	 * 
	 * The next two "slow" methods are useful for testing, debugging and the
	 * graphical debugger. They are each O(n) and don't make use of rank or size. In
	 * fact, they are the same as you used in an earlier assignment, so we are
	 * providing them for you. Please do not modify them or their recursive helpers
	 * in the Node class.
	 */
	public int slowHeight() {
		return root.slowHeight();
	}

	public int slowSize() {
		return root.slowSize();
	}

	/**
	 * MILESTONE 1
	 * 
	 * Returns true iff (read as "if and only if") for every node in the tree, the
	 * node's rank equals the size of the left subtree. This will be used to check
	 * that your ranks are being updated correctly. So when you get a subtree's
	 * size, you should NOT refer to rank but find it brute-force, similar to
	 * slowSize(), and actually calling slowSize() might be a good first-pass.
	 * 
	 * For full credit, then refactor it to make it more efficient: do this in O(n)
	 * time, so in a single pass through the tree, and with only O(1) extra storage
	 * (so no temp collections).
	 * 
	 * Instead of using slowSize(), use the same pattern as the sum of heights
	 * problem in HW5. We put our helper class inside the Node class, but you can
	 * put it anywhere it's convenient.
	 * 
	 * PLEASE feel free to call this method (or its recursive helper) in your code
	 * while you are writing your add() method if rank isn't working correctly. You
	 * may also modify it to print WHERE it is failing. It may be most important to
	 * use in Milestone 2, when you are updating ranks during rotations. (We added
	 * some commented-out calls to this method there so show you how it can be
	 * used.)
	 * 
	 * @return True iff each node's rank correctly equals its left subtree's size.
	 */
	public boolean ranksMatchLeftSubtreeSize() {
		return root.ranksMatchLeftSubtreeSize().isMatched;
	} // ranksMatchLeftSubtreeSize

	/**
	 * MILESTONE 2
	 * 
	 * Similar to toRankString(), but adding in balance codes too.
	 * 
	 * For the tree with root b and a left child a, it should return the string:
	 * [b1/, a0=] There are many more examples in the unit tests.
	 * 
	 * @return The string of elements and ranks, given in an pre-order traversal of
	 *         the tree.
	 */
	public String toDebugString() {
		if (size == 0)
			return "[]";

		StringBuilder sb = new StringBuilder();
		sb.append("[");
		root.toDebugString(sb);
		sb.delete(sb.length() - 2, sb.length());
		sb.append("]");

		return sb.toString();
	} // toDebugString

	/**
	 * MILESTONE 2
	 * 
	 * returns the total number of rotations done in this tree since it was created.
	 * A double rotation counts as two.
	 *
	 * @return number of rotations since this tree was created.
	 */
	public int totalRotationCount() {
		return rotationCount; // replace by a real calculation.
	} // totalRotationCount

	/**
	 * MILESTONE 2
	 * 
	 * Returns true iff (read as "if and only if") for every node in the tree, the
	 * node's balance code is correct based on its childrens' heights. Like
	 * ranksMatchLeftSubtreeSize() above, you'll need to compare your balance code
	 * to the actual brute-force height calculation. You may start with calling
	 * slowHeight(). But then, for full credit, do this in O(n) time, so in a single
	 * pass through the tree, and with only O(1) extra storage (so no temp
	 * collections). Instead of slowHeight(), use the same pattern as the sum of
	 * heights problem in HW5. We put our helper class inside the Node class, but
	 * you can put it anywhere it's convenient.
	 * 
	 * The notes for ranksMatchLeftSubtreeSize() above apply here - this method is
	 * to help YOU as the developer.
	 * 
	 * @return True iff each node's balance code is correct.
	 */
	public boolean balanceCodesAreCorrect() {
		return root.balanceCodesAreCorrect().isBalancedCorrectly;
	} // balanceCodesAreCorrect

	/**
	 * MILESTONE 2
	 * 
	 * Only write this one once your balance codes are correct. It will rely on
	 * correct balance codes to find the height of the tree in O(log n) time.
	 * 
	 * @return the height of this tree
	 */
	public int fastHeight() {
		return root.fastHeight();
	} // fastHeight

	/**
	 * MILESTONE 3
	 * 
	 * @param pos position of character to delete from this tree
	 * @return the character that is deleted
	 * @throws IndexOutOfBoundsException
	 */
	public char delete(int pos) throws IndexOutOfBoundsException {
		if (pos >= size || pos < 0) {
			throw new IndexOutOfBoundsException();
		}

		NodeInfo info = new NodeInfo(0, true);
		info.isDeleting = true;

		root = root.delete(pos, info);
		size--;
		rotationCount += info.rotationCount;
		return info.dataRemoved;
	}

	/**
	 * MILESTONE 3
	 * 
	 * This method operates in O(length), where length is the parameter provided.
	 * The way to do this is to recurse/iterate only over the nodes of the tree (and
	 * possibly their children) that contribute to the output string.
	 * 
	 * @param pos    location of the beginning of the string to retrieve
	 * @param length length of the string to retrieve
	 * @return string of length that starts in position pos
	 * @throws IndexOutOfBoundsException unless both pos and pos+length-1 are
	 *                                   legitimate indexes within this tree.
	 */
	public String get(int pos, int length) throws IndexOutOfBoundsException {
		if (pos < 0 || pos >= size || (pos + length - 1) < 0 || (pos + length - 1) >= size)
			throw new IndexOutOfBoundsException();

		StringBuilder sb = new StringBuilder();
		root.get(pos, pos + length - 1, sb);
		return sb.toString();
	} // get

	/**
	 * The following methods are for the graphical debugger.
	 */
	public void show() {
		if (this.display == null)
			this.display = new DisplayableBinaryTree(this, 960, 1080, true);
		else
			this.display.show(true);
	} // show

	public void close() {
		if (this.display != null)
			this.display.close();
	} // close

} // EditTree
