import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;

/**
 * This BinaryHeap has the min-heap-order property, and recall a heap is an
 * array but can be visualized as a tree. This BinaryHeap can insert elements,
 * remove the minimum element, and sort its elements in ascending order.
 * 
 * @param <T>, type of elements in heap
 */
public class BinaryHeap<T extends Comparable<? super T>> {
	private static final int INITIAL_CAPACITY = 31;

	private Class<T> type;
	private T[] array;
	private int currentSize;
	private Comparator<T> comparator;

	private final Comparator<T> MIN_HEAP_COMPARATOR = new Comparator<T>() {

		@Override
		public int compare(T o1, T o2) {
			return o1.compareTo(o2);
		} // compare
	};

	private final Comparator<T> MAX_HEAP_COMPARATOR = new Comparator<T>() {

		@Override
		public int compare(T o1, T o2) {
			return o2.compareTo(o1);
		} // compare
	};

	/**
	 * Construct a heap using an array of type "type". Initialize its comparator to
	 * sort in min-heap-order.
	 * 
	 * @param type of elements in heap
	 */
	@SuppressWarnings("unchecked")
	public BinaryHeap(Class<T> type) {
		this.type = type;
		this.array = (T[]) Array.newInstance(this.type, INITIAL_CAPACITY);
		this.currentSize = 0;
		this.comparator = MIN_HEAP_COMPARATOR;
	} // BinaryHeap

	/**
	 * Construct a heap from a pre-existing array. Initialize its comparator to sort
	 * in max-heap-order.
	 * 
	 * @param array
	 * @param type  of elements in heap
	 */
	private BinaryHeap(T[] array, Class<T> type) {
		this.type = type;
		this.array = array;
		this.currentSize = array.length - 1;
		this.comparator = MAX_HEAP_COMPARATOR;
		buildHeap();
	} // BinaryHeap

	/**
	 * Inserts an element such that the heap maintains the min-heap-order property.
	 * <br>
	 * <br>
	 * 
	 * For personal understanding: <br>
	 * <br>
	 * 
	 * Double the array size if inserting an element would cause an out-of-bounds
	 * index. <br>
	 * <br>
	 * 
	 * Then, create a "hole" in the heap where the element can be inserted. Since we
	 * do not use the zeroth index of the array to make our traversal simpler, we
	 * can always initialize the "element" in the zeroth index. <br>
	 * <br>
	 * 
	 * To maintain the min-heap-order property, the while loop percolates the "hole"
	 * from the bottom of the heap to the top of the heap until the min-heap-order
	 * property holds. Recall the min-heap-order property means each parent node in
	 * a heap is less than or equal to both of its children. <br>
	 * <br>
	 * 
	 * Once the "hole" is percolated to the correct location, insert the element in
	 * the "hole" and add one to the size. <br>
	 * 
	 * @param element to insert
	 */
	public void insert(T element) {
		if (currentSize + 1 == array.length)
			doubleArray();

		int hole = currentSize + 1;
		array[0] = element;

		while (element.compareTo(array[hole / 2]) < 0) {
			array[hole] = array[hole / 2];
			hole /= 2;
		} // end while

		array[hole] = element;
		++currentSize;
	} // insert

	/**
	 * Removes the minimum element. <br>
	 * <br>
	 * 
	 * For personal understanding: <br>
	 * <br>
	 * 
	 * If the heap is empty, an element cannot be removed, so return null. <br>
	 * <br>
	 * 
	 * Next, grab the minimum element, which we know is in position 1 of the heap
	 * due to the min-heap-order property. <br>
	 * <br>
	 * 
	 * Visualizing the tree representation of the heap, override the root of the
	 * heap with the rightmost leaf of the tree. Subtract one from the current size.
	 * Then, percolate the newly replaced root down the heap until the
	 * min-heap-order property holds. The previous root is overridden to null. <br>
	 * 
	 * @return the element removed or null if the heap is empty
	 */
	public T deleteMin() {
		if (isEmpty())
			return null;

		T min = findMin();
		array[1] = array[currentSize];
		currentSize--;
		percolateDown(1);
		array[currentSize + 1] = null;
		return min;
	} // deleteMin

	@Override
	public String toString() {
		if (isEmpty())
			return "[null]";

		StringBuilder sb = new StringBuilder();

		sb.append("[null, ");

		for (int i = 1; i < currentSize + 1; i++) {
			sb.append(array[i]);
			sb.append(", ");
		} // end for
		
		sb.delete(sb.length() - 2, sb.length() + 1);
		sb.append("]");

		return sb.toString();
	} // toString

	/**
	 * Sorts the elements in ascending order. <br>
	 * <br>
	 * 
	 * For personal understanding: <br>
	 * <br>
	 * 
	 * Note: deleteMin() and percolateDown() are used to build a max-heap in sort(),
	 * so deleteMin() is a slight misnomer in this case. <br>
	 * <br>
	 * 
	 * First, swap the minimal element with the element in position 0 of the
	 * "array", so the array offset calculations in deleteMin() and percolateDown()
	 * still work. <br>
	 * <br>
	 * 
	 * Second, construct a new heap with the max-heap-order property. Then, call
	 * deleteMin() on the max-heap to place the maximal element from the heap into
	 * the "array" from right-to-left, creating a sorted array. <br>
	 * 
	 * @param <T>
	 * @param array to be sorted
	 * @param type  of elements in array
	 */
	public static <T extends Comparable<? super T>> void sort(T[] array, Class<T> type) {
		int swap = 0;
		T min = array[swap];
		for (int i = 1; i < array.length; i++) {
			if (array[i].compareTo(min) < 0) {
				swap = i;
				min = array[i];
			} // end if
		} // end for
		array[swap] = array[0];
		array[0] = min;

		BinaryHeap<T> sorted = new BinaryHeap<T>(array, type);
		for (int i = array.length - 1; i > 0; i--)
			array[i] = sorted.deleteMin();

	} // sort

	/**
	 * Builds a max-order heap. <br>
	 * <br>
	 * 
	 * For personal understanding: <br>
	 * <br>
	 * 
	 * Calling percolateDown on the elements from currentSize/2 to 1 is percolating
	 * each element in the heap that is a full-node in the tree representation. The
	 * leaves will remain leaves, so they do not need to be percolated. As a result,
	 * percolating all full nodes with a MAX_HEAP_COMPARATOR creates a max-heap.
	 * <br>
	 * 
	 * @param <T>
	 */
	@SuppressWarnings("hiding")
	private <T extends Comparable<? super T>> void buildHeap() {
		for (int i = currentSize / 2; i > 0; i--)
			percolateDown(i);

	} // buildHeap

	/**
	 * Percolates an element at array position "hole" downward until the
	 * min-heap-order property is restored. <br>
	 * <br>
	 * 
	 * For personal understanding: <br>
	 * <br>
	 * 
	 * Initialize the element being percolated as the element at the array position
	 * "hole", and initialize "childIndex" as "hole". <br>
	 * <br>
	 * 
	 * While the "childIndex" is a valid index in the array, iterate through the
	 * heap in-order. If the "childIndex" is in-bounds and the element at array
	 * position "childIndex" is greater than the element at array position
	 * "childIndex" plus one, add one to "childIndex". Essentially, this chooses the
	 * position of the smallest child. Next, if the element at position "childIndex"
	 * is less than the "percolatingElement", percolate the element downward. At the
	 * end of each iteration, set the "hole" position to "childIndex" and traverse
	 * through the heap by multiplying the "childIndex" by two. <br>
	 * <br>
	 * 
	 * After exiting the while loop, assign the element at "hole" to
	 * "percolatingElement".
	 * 
	 * @param hole
	 */
	private void percolateDown(int hole) {
		T percolatingElement = array[hole];
		int childIndex = hole * 2;

		while (childIndex <= currentSize) {
			if (childIndex != currentSize && comparator.compare(array[childIndex], array[childIndex + 1]) > 0)
				childIndex++;

			if (comparator.compare(array[childIndex], percolatingElement) < 0)
				array[hole] = array[childIndex];

			else
				break;

			hole = childIndex;
			childIndex *= 2;
		} // end while

		array[hole] = percolatingElement;
	} // percolateDown

	/**
	 * Copies all elements in current array to a new array with double the capacity.
	 */
	@SuppressWarnings("unchecked")
	private void doubleArray() {
		T[] newArray = (T[]) Array.newInstance(type, array.length * 2);
		for (int i = 0; i < array.length; i++)
			newArray[i] = array[i];

		array = newArray;
	} // doubleArray

	/**
	 * Returns the minimum element of a heap, which will always be in position one
	 * of the array because of its min-heap-order property. Recall position zero of
	 * the array is not used to simplify arithmetic while traversing the heap.
	 * 
	 * @return T, min element
	 */
	private T findMin() {
		return array[1];
	} // findMin

	private boolean isEmpty() {
		return currentSize == 0;
	} // isEmpty

} // BinaryHeap
