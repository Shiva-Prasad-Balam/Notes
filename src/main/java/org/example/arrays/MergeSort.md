## MergeSort
- The idea is to divide the array into two halves, sort each half recursively, and then merge the sorted halves back together.

```java
public class MergeSort {
    public static void main(String[] args) {
        int[] arr = {38, 27, 43, 3, 9, 82, 10};
        MergeSort mergeSort = new MergeSort();
        mergeSort.mergeSort(arr, 0, arr.length - 1);
        System.out.println("Sorted array: " + Arrays.toString(arr));
    }
    public void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2; // Find the middle point
            mergeSort(arr, left, mid); // Sort the first half
            mergeSort(arr, mid + 1, right); // Sort the second half
            merge(arr, left, mid, right); // Merge the sorted halves
        }
    }
    public void merge(int[] arr, int left, int mid, int right) {
        // Create temporary arrays to hold the two halves
        int[] leftArr = new int[mid - left + 1];
        int[] rightArr = new int[right - mid];
        
        for (int i = 0; i < leftArr.length; i++) {
            leftArr[i] = arr[left + i];
        }
        for (int j = 0; j < rightArr.length; j++) {
            rightArr[j] = arr[mid + 1 + j];
        }
        
        // Merge the temporary arrays back into arr
        int i = 0, j = 0, k = left;
        while (i < leftArr.length && j < rightArr.length) {
            if (leftArr[i] <= rightArr[j]) {
                arr[k++] = leftArr[i++];
            } else {
                arr[k++] = rightArr[j++];
            }
        }
        
        // Copy the remaining elements of leftArr, if any
        while (i < leftArr.length) {
            arr[k++] = leftArr[i++];
        }
        
        // Copy the remaining elements of rightArr, if any
        while (j < rightArr.length) {
            arr[k++] = rightArr[j++];
        }
    }
}
```