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