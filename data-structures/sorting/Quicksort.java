
public class Quicksort {

	public static void quicksort(int[] array, int low, int high) {
		if (low >= high)
			return;

		int pivot = array[low];
		int pivotFinalPosition = partition(array, low, high, pivot);
		quicksort(array, low, pivotFinalPosition - 1);
		quicksort(array, pivotFinalPosition + 1, high);
	}

	/**
	 * Looks through the array from [low + 1, high] and swaps elements so that they
	 * are sorted with respect to the pivot which is set to the element at
	 * array[low].
	 * 
	 * @param array
	 * @param low
	 * @param high
	 * @param pivot
	 * @return Final Pivot position
	 */
	private static int partition(int[] array, int low, int high, int pivot) {
		int left = low + 1;
		int right = high;

		if (left > right)
			return left;

		while (true) {

			while (left <= high && array[left] < pivot)
				left++;

			while (right >= low && array[right] > pivot)
				right--;

			if (left >= right)
				break;

			int temp = array[left];
			array[left] = array[right];
			array[right] = temp;
		}

		int temp = array[low];
		array[low] = array[right];
		array[right] = temp;

		return right;
	}
}
