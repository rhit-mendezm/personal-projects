import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * 
 * Implementation of most of the Set interface operations using a Binary Search
 * Tree
 *
 * @author Matt Boutell and <<< Marlon Mendez-Yanez >>>.
 * @param <T>
 */

public class BinarySearchTree<T extends Comparable<T>> {
	private Node root;
	private int modificationsCount;

	// You will like NULL NODEs (a common software design pattern) once you get used
	// to them.
	private final Node NULL_NODE = new Node();

	public BinarySearchTree() {
		root = NULL_NODE;
		modificationsCount = 0;
	} // BinarySearchTree

	public int size() {
		return root.size();
	} // size

	public int height() {
		return root.height();
	} // height

	public boolean isEmpty() {
		return root.size() == 0;
	} // isEmpty

	public boolean containsNonBST(T item) {
		return root.containsNonBST(item);
	} // containsNonBST

	public ArrayList<T> toArrayList() {
		ArrayList<T> list = new ArrayList<>();
		this.root.toArrayList(list);
		return list;
	} // toArrayList

	@Override
	public String toString() {
		return this.toArrayList().toString();
	} // toString

	public Object[] toArray() {
		return this.toArrayList().toArray();
	} // toArray

	public Iterator<T> inefficientIterator() {
		return new ArrayListIterator();
	} // inefficientIterator

	public Iterator<T> preOrderIterator() {
		return new StackIterator();
	} // preOrderIterator

	public Iterator<T> iterator() {
		return new EfficientStackIterator();
	} // iterator

	/**
	 * ensures: if o is null, throws exception. Otherwise, the item is placed in the
	 * Binary Search Tree such that the Tree maintains its Binary Search property.
	 * This method also modifies the value of a BooleanContainer object, so its
	 * value is true if the element was inserted, and its value is false if the
	 * element was not inserted. <br>
	 * 
	 * @param o
	 * @throws IllegalArgumentException
	 * @return
	 */
	public boolean insert(T o) {
		if (o == null)
			throw new IllegalArgumentException();

		BooleanContainer bc = new BooleanContainer(true);

		root = root.insert(o, bc);
		if (bc.value)
			++modificationsCount;
		return bc.value;
	} // insert

	public boolean remove(T element) {
		if (element == null)
			throw new IllegalArgumentException();

		BooleanContainer bc = new BooleanContainer(true);

		root = root.remove(element, bc);
		if (bc.value)
			++modificationsCount;
		return bc.value;
	} // remove

	public boolean contains(T item) {
		if (root == NULL_NODE)
			return false;

		return root.contains(item);
	} // contains

	// For manual tests only
	public void setRoot(Node n) {
		this.root = n;
	} // setRoot

	public void printPreOrder() {
		root.printPreOrder();
	} // printPreOrder

	public void printInOrder() {
		root.printInOrder();
	} // printInOrder

	public void printPostOrder() {
		root.printPostOrder();
	} // printPostOrder

	// Not private, since we need access for manual testing.
	class Node {
		private T data;
		private Node left;
		private Node right;

		public Node() {
			this.data = null;
			this.left = null;
			this.right = null;
		} // Node

		public void printPreOrder() {
			if (this == NULL_NODE)
				return;
			System.out.println(this.data.toString());
			left.printPreOrder();
			right.printPreOrder();
		} // printPreOrder

		public void printInOrder() {
			if (this == NULL_NODE)
				return;
			left.printInOrder();
			System.out.println(this.data.toString());
			right.printInOrder();
		} // printInOrder

		public void printPostOrder() {
			if (this == NULL_NODE)
				return;
			left.printPostOrder();
			right.printPostOrder();
			System.out.println(this.data.toString());
		} // printPostOrder

		public int size() {
			if (this == NULL_NODE)
				return 0;
			return (1 + left.size() + right.size());
		} // size

		public int height() {
			if (this == NULL_NODE)
				return -1;
			return (1 + Math.max(left.height(), right.height()));
		} // height

		public boolean containsNonBST(T item) {
			if (this == NULL_NODE)
				return false;

			return item.equals(this.data) || left.containsNonBST(item) || right.containsNonBST(item);
		} // containsNonBST

		public void toArrayList(ArrayList<T> list) {
			if (this == NULL_NODE)
				return;

			left.toArrayList(list);
			list.add(data);
			right.toArrayList(list);
		} // toArrayList

		public Node(T element) {
			this.data = element;
			this.left = NULL_NODE;
			this.right = NULL_NODE;
		} // Node

		public T getData() {
			return this.data;
		} // getData

		public Node getLeft() {
			return this.left;
		} // getLeft

		public Node getRight() {
			return this.right;
		} // getRight

		/**
		 * ensures: item is placed into Binary Search Tree such that the Tree keeps its
		 * Binary Search property. <br>
		 * 
		 * @param o
		 * @param bc
		 * @return
		 */
		public Node insert(T o, BooleanContainer bc) {
			if (this == NULL_NODE) {
				bc.value = true;
				return new Node(o);
			} // end if

			else if (o.compareTo(data) < 0)
				left = left.insert(o, bc);

			else if (o.compareTo(data) > 0)
				right = right.insert(o, bc);

			else if (o.compareTo(data) == 0) {
				bc.value = false;
				return this;
			} // end else if

			return this;
		} // insert

		/**
		 * ensures: removes the given data from the tree if it exists. If the data does
		 * not exist in the tree, it returns false and the NULL_NODE. Otherwise, this
		 * method alters the tree according to the number of children the node about to
		 * be removed has. <br>
		 * If it has zero children, the node is simply removed. <br>
		 * If it has one child, the node is replaced with its child. <br>
		 * If it has two children, the node is replaced with the rightmost child of its
		 * left subtree. <br>
		 * 
		 * @param o
		 * @param bc
		 * @return
		 */
		public Node remove(T o, BooleanContainer bc) {
			if (this == NULL_NODE) {
				bc.value = false;
				return NULL_NODE;
			} // end if

			else if (o.compareTo(data) < 0)
				left = left.remove(o, bc);

			else if (o.compareTo(data) > 0)
				right = right.remove(o, bc);

			else {
				bc.value = true;
				if (isLeaf())
					return NULL_NODE;

				else if (haveLeftChildOnly())
					return left;

				else if (haveRightChildOnly())
					return right;

				else {
					Node predecessor = getRightMostChild(left);
					this.data = predecessor.data;
					left = left.remove(predecessor.data, bc);
					return this;
				} // end else

			} // end else
			return this;
		} // remove

		private boolean isLeaf() {
			return left == NULL_NODE && right == NULL_NODE;
		} // isLeaf

		private boolean haveLeftChildOnly() {
			return left != NULL_NODE && right == NULL_NODE;
		} // haveLeftChildOnly

		private boolean haveRightChildOnly() {
			return left == NULL_NODE && right != NULL_NODE;
		} // haveRightChildOnly

		private Node getRightMostChild(Node root) {
			Node predecessor = root;
			if (root.right != NULL_NODE)
				predecessor = getRightMostChild(root.right);

			return predecessor;
		} // getRightMostChild

		public boolean contains(T item) {
			return (item.compareTo(data) == 0 || (item.compareTo(data) < 0 && left != NULL_NODE && left.contains(item))
					|| (item.compareTo(data) > 0 && right != NULL_NODE && right.contains(item)));

		} // contains

		// For manual testing
		public void setLeft(Node left) {
			this.left = left;
		} // setLeft

		public void setRight(Node right) {
			this.right = right;
		} // setRight

	} // Node

	/**
	 * Class: ArrayListIterator
	 * 
	 * @author Marlon Mendez-Yanez <br>
	 *         Purpose: Used to iterate through a binary tree. <br>
	 * 
	 *         Limitations: This iterator is inefficient because it creates a list
	 *         of the entire binary tree instead of just the items the user wants to
	 *         iterate. <br>
	 */
	class ArrayListIterator implements Iterator<T> {

		private ArrayList<T> list;
		private int index;
		private int originalLazyModificationsCount;

		/**
		 * ensures: initializes the list to empty and the index to 0
		 */
		public ArrayListIterator() {
			list = toArrayList();
			index = 0;
			originalLazyModificationsCount = modificationsCount;
		} // ArrayListIterator

		@Override
		public boolean hasNext() {
			return index < list.size();
		} // hasNext

		@Override
		public T next() {
			if (!hasNext())
				throw new NoSuchElementException();

			if (originalLazyModificationsCount != modificationsCount)
				throw new ConcurrentModificationException();

			T temp = list.get(index);
			++index;
			return temp;
		} // next

	} // Iterator class

	/**
	 * Class: StackIterator
	 * 
	 * @author Marlon Mendez-Yanez <br>
	 *         Purpose: Used to iterate through a binary tree. Internally, this uses
	 *         a stack to manage the current element being iterated, which is more
	 *         efficient than ArrayListIterator. <br>
	 */
	class StackIterator implements Iterator<T> {

		private Stack<Node> iteratorStack;
		private T preOrderPreviousItem;
		private int preOrderModificationsCount;
		private boolean preOrderNeverCalledNext;
		private boolean preOrderCalledRemove;

		/**
		 * ensures: Initializes the stack to empty and pushes the root of the Binary
		 * Tree to it.
		 */
		public StackIterator() {
			iteratorStack = new Stack<>();
			preOrderPreviousItem = null;
			preOrderModificationsCount = modificationsCount;
			preOrderNeverCalledNext = true;
			preOrderCalledRemove = false;

			if (root != NULL_NODE)
				iteratorStack.push(root);
		} // StackIterator

		@Override
		public boolean hasNext() {
			return !iteratorStack.isEmpty();
		} // hasNext

		@Override
		public T next() {
			if (!hasNext())
				throw new NoSuchElementException();

			preOrderNeverCalledNext = false;
			preOrderCalledRemove = false;

			if (preOrderModificationsCount != modificationsCount)
				throw new ConcurrentModificationException();

			Node current = iteratorStack.pop();

			if (current.right != NULL_NODE)
				iteratorStack.push(current.right);

			if (current.left != NULL_NODE)
				iteratorStack.push(current.left);

			preOrderPreviousItem = current.data;
			return current.data;
		} // next

		@Override
		public void remove() {
			if (preOrderNeverCalledNext || preOrderCalledRemove)
				throw new IllegalStateException();

			preOrderCalledRemove = true;
			BinarySearchTree.this.remove(preOrderPreviousItem);
		} // remove

	} // StackIterator

	/**
	 * Class: Token
	 * 
	 * @author Marlon Mendez-Yanez <br>
	 *         Purpose: Used in collaboration with EfficientStackIterator Class to
	 *         track which nodes have been visited. <br>
	 */
	class Token {
		private Node node;
		private int tag;

		/**
		 * ensures: node and tag are initialized.
		 * 
		 * @param node
		 * @param tag
		 */
		public Token(Node node, int tag) {
			this.node = node;
			this.tag = tag;
		} // Token

		/**
		 * ensures: tag is initialized.
		 * 
		 * @param tag
		 */
		public Token(int tag) {
			this.tag = tag;
		} // Token

		public int getTag() {
			return this.tag;
		} // getTag

		public void setTag(int tag) {
			this.tag = tag;
		} // setTag

		public Node getNode() {
			return this.node;
		} // getNode
	} // Token

	/**
	 * Class: EfficientStackIterator
	 * 
	 * @author Marlon Mendez-Yanez <br>
	 *         Purpose: Traverses through a Binary Tree efficiently. This class
	 *         behaves according to the token of each node. <br>
	 */
	class EfficientStackIterator implements Iterator<T> {

		private Stack<Token> tokenStack;
		private T inOrderPreviousItem;
		private int inOrderModificationsCount;
		private boolean inOrderNeverCalledNext;
		private boolean inOrderCalledRemove;

		/**
		 * ensures: tokenStack is initialized and the root is pushed onto the stack if
		 * it is not the NULL_NODE.
		 */
		public EfficientStackIterator() {
			tokenStack = new Stack<>();
			inOrderPreviousItem = null;
			inOrderModificationsCount = modificationsCount;
			inOrderNeverCalledNext = true;
			inOrderCalledRemove = false;

			if (root != NULL_NODE)
				tokenStack.push(new Token(root, 0));
		} // EfficientStackIterator

		@Override
		public boolean hasNext() {
			return !tokenStack.isEmpty();
		} // hasNext

		/**
		 * ensures: If the current node has a token of 0, next moves to the left child
		 * if it exists. Otherwise, if the current node has a token of 1, next moves to
		 * the right child and returns the previous node's data.
		 */
		@Override
		public T next() {
			if (!hasNext())
				throw new NoSuchElementException();

			inOrderNeverCalledNext = false;
			inOrderCalledRemove = false;

			if (inOrderModificationsCount != modificationsCount)
				throw new ConcurrentModificationException();

			T nextItem = tokenStack.peek().getNode().data;
			while (!tokenStack.isEmpty()) {
				Token currentToken = tokenStack.pop();
				Node currentNode = currentToken.getNode();

				if (currentToken.getTag() == 0) {
					currentToken.setTag(1);
					tokenStack.push(currentToken);
					if (currentNode.left != NULL_NODE)
						tokenStack.push(new Token(currentNode.left, 0));
				} // end if

				else {
					if (currentNode.right != NULL_NODE)
						tokenStack.push(new Token(currentNode.right, 0));

					inOrderPreviousItem = currentNode.data;
					return currentNode.data;
				} // end else
			} // end while

			inOrderPreviousItem = nextItem;
			return nextItem;
		} // next

		public void remove() {
			if (inOrderNeverCalledNext || inOrderCalledRemove)
				throw new IllegalStateException();

			inOrderCalledRemove = true;
			BinarySearchTree.this.remove(inOrderPreviousItem);
		} // remove

	} // EfficientStackIterator

	/**
	 * Class: BooleanContainer
	 * 
	 * @author Marlon Mendez-Yanez <br>
	 *         Purpose: Container class for BinarySearchTree methods for Node
	 *         objects to communicate with each other.
	 */
	class BooleanContainer {
		public boolean value;

		public BooleanContainer(boolean value) {
			this.value = value;
		} // BooleanContainer
	} // BooleanContainer

} // Binary Search Tree
