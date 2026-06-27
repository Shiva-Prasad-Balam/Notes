### Kadane's Algorithm
- Kadane's algorithm is used to find the maximum sum of a contiguous subarray in an array of integers.
- The idea is to iterate through the array while keeping track of the current sum of the subarray and the maximum sum found so far. 
- If the current sum becomes negative, we reset it to zero, as a negative sum would not contribute to a maximum sum in future iterations.

```java
public int kadane(int[] arr) {
    int ans = Integer.MIN_VALUE;
    int sum = 0;
    for (int i=0; i<n; i++)  {
        sum += arr[i];
        ans = Math.max(ans, sum);
        if (sum < 0) sum = 0;
    }
    return ans; // Return the maximum sum of a contiguous subarray
}
```

### The MEX (Minimum Excluded value) 
- MEX of an array is the smallest non-negative integer (0, 1, 2, ...) that does not appear in the array.

```java
public void mex(int []a) {
    boolean []track = new boolean[a.length + 1]; //Store the presence of numbers from 0 to a.length
    for (int num : a) {
        if (a[num] <= a.length) {
            track[num] = true;
        }
    }
    int mex = 0;
    while(track[mex]) mex++; // Increment mex until we find a number that is not present in the array
    return mex;
}
```