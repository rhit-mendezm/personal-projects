package editortrees;

import editortrees.EditTree.NodeInfo;

/**
 * A node in a height-balanced binary tree with rank. Except for the NULL_NODE,
 * one node cannot belong to two different trees.
 * 
 * @author Jack Cooperman
 * @author Marlon Mendez-Yanez
 */
public class Node {

	static final Node NULL_NODE = new Node('\0', null, null);

	char data;
	Node left, right;
	int rank;
	Code balance;
	DisplayableNodeWrapper displayableNodeWrapper;

	enum Code {
		SAME, LEFT, RIGHT;

		// Used in the displayer and debug string
		public String toString() {
			switch (this) {
			case LEFT:
				return "/";
			case SAME:
				return "=";
			case RIGHT:
				return "\\";
			default:
				throw new IllegalStateException();
			} // switch
		} // toString
	} // Code

	/**
	 * Constructs a node with two children. Either or both children can be null.
	 * 
	 * @param data
	 * @param left
	 * @param right
	 */
	public Node(char data, Node left, Node right) {
		this.left = left;
		this.right = right;
		this.data = data;
		this.balance = Code.SAME;
		this.rank = 0;
		displayableNodeWrapper = new DisplayableNodeWrapper(this);
	} // Node

	/**
	 * Constructs a leaf node.
	 * 
	 * @param data
	 */
	public Node(char data) {
		this(data, NULL_NODE, NULL_NODE);
	} // Node

	// Provided to you to enable testing, please don't change.
	int slowHeight() {
		if (this == NULL_NODE) {
			return -1;
		}
		return Math.max(left.slowHeight(), right.slowHeight()) + 1;
	}

	// Provided to you to enable testing, please don't change.
	public int slowSize() {
		if (this == NULL_NODE) {
			return 0;
		}
		return left.slowSize() + right.slowSize() + 1;
	}

	/**
	 * Creates an in-order string representation of the tree's data.
	 * 
	 * @return String, in-order traversal of tree
	 */
	@Override
	public String toString() {
		if (this == NULL_NODE)
			return "";

		return left.toString() + this.data + right.toString();
	} // toString

	/**
	 * Constructs a pre-order string representation of a tree with each node's rank
	 * next to its data.
	 * 
	 * @param sb
	 */
	public void toRankString(StringBuilder sb) {
		if (this == NULL_NODE)
			return;

		sb.append(this.data);
		sb.append(this.rank);
		sb.append(", ");
		left.toRankString(sb);
		right.toRankString(sb);
	} // toRankString

	/**
	 * Constructs a pre-order string representation of a tree with each node's rank
	 * and balance code next to its data.
	 * 
	 * @param sb
	 */
	public void toDebugString(StringBuilder sb) {
		if (this == NULL_NODE)
			return;

		sb.append(this.data);
		sb.append(this.rank);
		sb.append(this.balance.toString());
		sb.append(", ");
		left.toDebugString(sb);
		right.toDebugString(sb);
	} // toDebugString

	/**
	 * The position of the node to be added is compared to the rank of each node
	 * traversed. If the position of the new node is less than or equal to the rank
	 * of the current node, enter the current node's left tree and add one to its
	 * rank. Otherwise, enter the current node's right tree, do nothing to its rank,
	 * and recalibrate the position argument of the new node to account for the left
	 * subtree it skipped.
	 * 
	 * @param ch
	 * @param pos
	 * 
	 * @return newly constructed Node
	 */
	public Node add(char ch, int pos, NodeInfo info) {
		if (this == NULL_NODE)
			return new Node(ch);

		if (pos <= rank) {
			rank++;
			left = left.add(ch, pos, info);
			return balanceAfterLeftInsert(info);
		} // end if

		else {
			right = right.add(ch, pos - rank - 1, info);
			return balanceAfterRightInsert(info);
		} // end else
	} // add

	/**
	 * Handles the three cases of inserting into a left tree. The balance codes are
	 * updated, and it performs a single right rotation when applicable.
	 * 
	 * @param info
	 * @return Node
	 */
	private Node balanceAfterLeftInsert(NodeInfo info) {
		if (!info.continueRebalancing)
			return this;

		if (balance == Code.SAME)
			balance = Code.LEFT;

		else if (balance == Code.LEFT) {
			if (left.balance == Code.LEFT)
				return singleRotateRight(this, left, info);

			else if (left.balance == Code.RIGHT)
				return leftRightRotate(this, left, info);

		} // end else if

		else {
			balance = Code.SAME;
			info.continueRebalancing = false;
		} // end else

		return this;
	} // afterLeftInsert

	/**
	 * Handles the three cases of inserting into a right tree. The balance codes are
	 * updated, and it performs a single left rotation when applicable.
	 * 
	 * @param info
	 * @return Node
	 */
	private Node balanceAfterRightInsert(NodeInfo info) {
		if (!info.continueRebalancing)
			return this;

		if (balance == Code.SAME)
			balance = Code.RIGHT;

		else if (balance == Code.RIGHT) {
			if (right.balance == Code.RIGHT)
				return singleRotateLeft(this, right, info);

			else if (right.balance == Code.LEFT)
				return rightLeftRotate(this, right, info);

		} // end else if

		else {
			balance = Code.SAME;
			info.continueRebalancing = false;
		} // end else

		return this;
	} // afterRightInsert

	/**
	 * Removes the node at the given position. The tree is, then, rebalanced as
	 * needed.
	 * 
	 * @param pos
	 * @param info
	 * @return Node removed
	 */
	public Node delete(int pos, NodeInfo info) {
		if (pos < rank) {
			rank--;
			left = left.delete(pos, info);
			return balanceAfterLeftDelete(info);
		} // end if

		else if (pos > rank) {
			right = right.delete(pos - rank - 1, info);
			return balanceAfterRightDelete(info);
		} // end else

		else {
			info.dataRemoved = data;
			if (isLeaf())
				return NULL_NODE;

			else if (hasLeftChildOnly())
				return left;

			else if (hasRightChildOnly())
				return right;

			else {
				Node successor = right.getLeftMostChild();
				data = successor.data;
				right = right.delete(0, info);
				return balanceAfterRightDelete(info);
			} // end else

		} // end else
	} // delete

	private Node balanceAfterLeftDelete(NodeInfo info) {
		if (!info.continueRebalancing)
			return this;

		if (balance == Code.SAME) {
			balance = Code.RIGHT;
			info.continueRebalancing = false;
		} // end if

		else if (balance == Code.RIGHT) {
			if (right.balance == Code.RIGHT) {
				return singleRotateLeft(this, right, info);
			} // end if

			else if (right.balance == Code.LEFT) {
				return rightLeftRotate(this, right, info);
			} // end else if

			else {
				Node temp = singleRotateLeft(this, right, info);
				info.continueRebalancing = false;
				temp.balance = Code.LEFT;
				temp.left.balance = Code.RIGHT;
				return temp;
			} // end else
		} // end else if

		else {
			balance = Code.SAME;
		} // end else

		return this;
	} // balanceAfterLeftDelete

	private Node balanceAfterRightDelete(NodeInfo info) {
		if (!info.continueRebalancing)
			return this;

		if (balance == Code.SAME) {
			balance = Code.LEFT;
			info.continueRebalancing = false;
		} // end if

		else if (balance == Code.LEFT) {
			if (left.balance == Code.LEFT) {
				return singleRotateRight(this, left, info);
			} // end if

			else if (left.balance == Code.RIGHT) {
				return leftRightRotate(this, left, info);
			} // end else if

			else {
				Node temp = singleRotateRight(this, left, info);
				info.continueRebalancing = false;
				temp.balance = Code.RIGHT;
				temp.right.balance = Code.LEFT;
				return temp;
			} // end else
		} // end else if

		else {
			balance = Code.SAME;
		} // end else

		return this;
	} // balanceAfterRightDelete

	/**
	 * Returns the left most child of the given node.
	 * 
	 * @param parent
	 * @return Node, left most child
	 */
	private Node getLeftMostChild() {
		Node successor = this;
		if (left != NULL_NODE)
			successor = left.getLeftMostChild();

		return successor;
	} // getLeftMostChild

	/**
	 * Returns the new root of a tree after a single left rotation.
	 * 
	 * @param parent
	 * @param child
	 * @return Node, root of tree after rotation
	 */
	private Node singleRotateLeft(Node parent, Node child, NodeInfo info) {
		if (!info.isDeleting)
			info.continueRebalancing = false;

		Node tempNode = child.left;
		child.left = parent;
		parent.right = tempNode;

		info.rotationCount++;

		parent.balance = Code.SAME;
		child.balance = Code.SAME;

		child.rank += parent.rank + 1;
		return child;
	} // singleRotateLeft

	/**
	 * Returns the new root of a tree after a single right rotation.
	 * 
	 * @param parent
	 * @param child
	 * @return Node, root of tree after rotation
	 */
	private Node singleRotateRight(Node parent, Node child, NodeInfo info) {
		if (!info.isDeleting)
			info.continueRebalancing = false;

		Node tempNode = child.right;
		child.right = parent;
		parent.left = tempNode;

		info.rotationCount++;

		parent.balance = Code.SAME;
		child.balance = Code.SAME;

		parent.rank -= (child.rank + 1);
		return child;
	} // singleRotateRight

	/**
	 * Returns the new root of a tree after a double right rotation.
	 * 
	 * @param parent
	 * @param child
	 * @param info
	 * @return Node, root of tree after rotation
	 */
	private Node leftRightRotate(Node parent, Node child, NodeInfo info) {
		Code grandChildBalance = child.right.balance;

		Node grandChild = singleRotateLeft(child, child.right, info);
		grandChild = grandChild.singleRotateRight(parent, grandChild, info);

		if (grandChildBalance == Code.LEFT) {
			grandChild.left.balance = Code.SAME;
			grandChild.right.balance = Code.RIGHT;
		} // end if

		else if (grandChildBalance == Code.RIGHT) {
			grandChild.left.balance = Code.LEFT;
			grandChild.right.balance = Code.SAME;
		} // end else if
		return grandChild;
	} // leftRightRotate

	/**
	 * Returns the new root of a tree after a double left rotation.
	 * 
	 * @param parent
	 * @param child
	 * @param info
	 * @return Node, root of tree after rotation
	 */
	private Node rightLeftRotate(Node parent, Node child, NodeInfo info) {
		Code grandChildBalance = child.left.balance;

		Node grandChild = singleRotateRight(child, child.left, info);
		grandChild = grandChild.singleRotateLeft(parent, grandChild, info);

		if (grandChildBalance == Code.LEFT) {
			grandChild.left.balance = Code.SAME;
			grandChild.right.balance = Code.RIGHT;
		} // end if

		else if (grandChildBalance == Code.RIGHT) {
			grandChild.left.balance = Code.LEFT;
			grandChild.right.balance = Code.SAME;
		} // end else if
		return grandChild;
	} // rightLeftRotate

	/**
	 * This method recursively gets the node at a given position pos. If the given
	 * position is less than the current node's rank, the node must be on the left
	 * subtree. If it is equal, we have the right node. If it is greater than, it
	 * must be on the right subtree, and we must update pos to "skip" over the left
	 * subtree nodes and the current node.
	 * 
	 * @param pos
	 * @return Node - the node at the desired position
	 */
	public Node get(int pos) {
		if (pos < rank)
			return left.get(pos);

		else if (pos > rank)
			return right.get(pos - rank - 1);

		else
			return this;
	} // get

	/**
	 * Mutates a StringBuilder object to build an in-order representation of a tree
	 * beginning at node at position "begin" and ending at node at position "end".
	 * 
	 * @param begin
	 * @param end
	 * @param sb
	 */
	public void get(int begin, int end, StringBuilder sb) {
		if (this == NULL_NODE)
			return;

		if (begin < rank)
			left.get(begin, end, sb);

		if (begin <= rank && end >= rank)
			sb.append(data);

		if (end > rank)
			right.get(begin - rank - 1, end - rank - 1, sb);
	} // get

	/**
	 * This method recursively calculates the size of the left subtree and uses that
	 * value to compare against the current node's rank.
	 * 
	 * @return IsMatchedAndSize with a boolean field to pass to the tree class and
	 *         an int field to track the current node's tree size.
	 */
	public IsMatchedAndSize ranksMatchLeftSubtreeSize() {

		if (this == NULL_NODE)
			return new IsMatchedAndSize(true, 0);

		IsMatchedAndSize leftContainer = left.ranksMatchLeftSubtreeSize();
		IsMatchedAndSize rightContainer = right.ranksMatchLeftSubtreeSize();

		if (!leftContainer.isMatched || !rightContainer.isMatched)
			return new IsMatchedAndSize(false, 0);

		IsMatchedAndSize currentContainer = new IsMatchedAndSize(false, 0);

		if (leftContainer.size == this.rank)
			currentContainer.isMatched = true;
		else
			currentContainer.isMatched = false;

		currentContainer.size = leftContainer.size + rightContainer.size + 1;
		return currentContainer;
	} // ranksMatchLeftSubtreeSize

	/**
	 * Wrapper class to track the size of a tree and the status of whether a Node's
	 * left subtree matches its rank.
	 */
	public class IsMatchedAndSize {
		boolean isMatched;
		int size;

		public IsMatchedAndSize(boolean isMatched, int size) {
			this.isMatched = isMatched;
			this.size = size;
		} // IsMatchedAndSize
	} // IsMatchedAndSize

	/**
	 * Iterates through each node and verifies the balance code of each node by
	 * comparing its balance code to the difference of the heights of its subtrees.
	 * 
	 * @return IsBalancedAndHeight with a boolean field to pass to the tree class
	 *         and an int field to track the height of each node.
	 */
	public IsBalancedAndHeight balanceCodesAreCorrect() {
		if (this == NULL_NODE)
			return new IsBalancedAndHeight(true, -1);

		IsBalancedAndHeight leftContainer = left.balanceCodesAreCorrect();
		IsBalancedAndHeight rightContainer = right.balanceCodesAreCorrect();
		int maxHeight = Math.max(leftContainer.height, rightContainer.height) + 1;
		int heightDifference = leftContainer.height - rightContainer.height;
		boolean areChildrenBalanced = leftContainer.isBalancedCorrectly && rightContainer.isBalancedCorrectly;

		if ((balance == Code.SAME && heightDifference != 0) || (balance == Code.LEFT && heightDifference != 1)
				|| (balance == Code.RIGHT && heightDifference != -1))
			return new IsBalancedAndHeight(false, maxHeight);

		return new IsBalancedAndHeight(areChildrenBalanced, maxHeight);
	} // balanceCodesAreCorrect

	/**
	 * Wrapper class to track the size of a tree and the status of whether a Node's
	 * height corresponds to its balance code.
	 */
	public class IsBalancedAndHeight {
		boolean isBalancedCorrectly;
		int height;

		public IsBalancedAndHeight(boolean isBalancedCorrectly, int height) {
			this.isBalancedCorrectly = isBalancedCorrectly;
			this.height = height;
		} // isBalancedAndSize
	} // isBalancedAndSize

	/**
	 * Traverses the tree, from top to bottom, to calculate height in O(log n) time.
	 * 
	 * @return int, height
	 */
	public int fastHeight() {
		if (this == NULL_NODE)
			return -1;

		if (balance == Code.RIGHT)
			return right.fastHeight() + 1;
		else
			return left.fastHeight() + 1;
	} // fastHeight

	/**
	 * The following methods describe the number of children a node has.
	 */
	public boolean isLeaf() {
		return left == NULL_NODE && right == NULL_NODE;
	} // isLeaf

	public boolean hasLeftChildOnly() {
		return left != NULL_NODE && right == NULL_NODE;
	} // haveLeftChildOnly

	public boolean hasRightChildOnly() {
		return left == NULL_NODE && right != NULL_NODE;
	} // haveRightChildOnly

	public boolean isFullNode() {
		return left != NULL_NODE && right != NULL_NODE;
	} // isFullNode

	/**
	 * The following methods are for the graphical debugger.
	 */
	public boolean hasLeft() {
		return this.left != NULL_NODE;
	} // hasLeft

	public boolean hasRight() {
		return this.right != NULL_NODE;
	} // hasRight

	public boolean hasParent() {
		return false;
	} // hasParent

	public Node getParent() {
		return NULL_NODE;
	} // getParent
} // Node