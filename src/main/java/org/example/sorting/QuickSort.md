# Quick Sort

## Lomuto Partition 
- It pivots last element and places all the smaller elements to the left and swaps the pivot to its correct position.
- The correct position of the pivot is returned and the array is recursively sorted on both sides of the pivot.

```java
public int lomutoPartition(int[] arr, int low, int high) {
    int pivot = arr[high];
    int i = low - 1;
    for (int j = low; j < high; j++) {
        if (arr[j] < pivot) {
            i++;
            swap(a[i], a[j]);
        }
    }
    swap(a[i + 1], a[high]);
    return i + 1;
}
public void quickSort(int[] arr, int low, int high) {
    if (low < high) {
        int pivotIndex = lomutoPartition(arr, low, high);
        quickSort(arr, low, pivotIndex - 1);
        quickSort(arr, pivotIndex + 1, high);
    }
}
```

## Hoare Partition
- It pivots the first element and takes two pointers, one from the left and moves it to the right until it finds an 
element greater than the pivot, and another from the right and moves it to the left until it finds an element smaller
than the pivot. Then, it swaps these two elements. This process continues until the two pointers meet.

```java
public int hoarePartition(int[] arr, int low, int high) {
    int pivot = arr[low];
    int i = low - 1;
    int j = high + 1;
    while (true) {
        do {
            i++;
        } while (arr[i] < pivot);
        do {
            j--;
        } while (arr[j] > pivot);
        if (i >= j) return j;
        swap(arr[i], arr[j]);
    }
}
public void quickSort(int[] arr, int low, int high) {
    if (low < high) {
        int pivotIndex = hoarePartition(arr, low, high);
        quickSort(arr, low, pivotIndex);
        quickSort(arr, pivotIndex + 1, high);
    }
}
```
