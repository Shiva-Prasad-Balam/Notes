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

### Next smaller element in an array
- The next smaller element for an element x in an array is the first smaller element on the right side of x in the array.
- If no such element exists, we can return -1 for that element.

```java
public int[] nextSmallerElement(int[] arr) {
    int n = arr.length;
    int[] nes = new int[n];
    Stack<Integer> stack = new Stack<>();
    for (int i = n - 1; i >= 0; i--) {
        while (!stack.isEmpty() && arr[stack.peek()] >= arr[i]) {
            stack.pop(); // Pop elements from the stack until we find a smaller element
        }
        nes[i] = stack.isEmpty() ? -1 : stack.peek(); // If stack is empty, there is no smaller element, otherwise the top of the stack is the next smaller element
        stack.push(i); // Push the current element onto the stack
    }
    return nes; // Return the array containing the next smaller elements for each element in the input
}
```