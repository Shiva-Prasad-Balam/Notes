### Segment Trees
- Binary tree stored in an array
- Each node represents a segment of the array
- Leaf nodes represent individual elements of the array
- Internal nodes represent the combination of their child nodes (e.g., sum, min, max)
- Used for range queries and updates

```java
public class SegmentTree {
    private int[] segTree; // Tree with sum of values in a range
    private int[] givenArray;
    private int n;
    
    SegmentTree(int[] arr) {
        this.givenArray = arr;
        this.n = arr.length;
        this.segTree = new int[4 * n]; // Allocate enough space for the segment tree
        build(0, 0, n - 1);
    }
    
    void buildTree(int node, int start, int end) {
        if (start == end) {
            segTree[node] = givenArray[start];
            return;
        }
        
        int mid = (start + end) / 2;
        buildTree(2 * node + 1, start, mid);
        buildTree(2 * node + 2, mid + 1, end);
        
        segTree[node] = segTree[2 * node + 1] + segTree[2 * node + 2]; // Combine child nodes (sum in this case)
    }
    
    void updateValue(int node, int start, int end, int idx, int value) {
        if (start == end) {
            givenArray[idx] = value;
            segTree[node] = value;
            return;
        }
        
        int mid = (start + end) / 2;
        if (idx <= mid) {
            updateValue(2 * node + 1, start, mid, idx, value);
        } else {
            updateValue(2 * node + 2, mid + 1, end, idx, value);
        }
        
        segTree[node] = segTree[2 * node + 1] + segTree[2 * node + 2]; // Update the current node after updating child
    }
    
    int querySum(int node, int start, int end, int L, int R) {
        if (start > R || end < L) { 
            return 0; // No overlap
        }
        
        if (start <= L && R <= end) {
            return segTree[node]; // Total overlap
        }
        
        int mid = (start + end) / 2;
        int leftSum = querySum(2 * node + 1, start, mid, L, R);
        int rightSum = querySum(2 * node + 2, mid + 1, end, L, R);
        
        return leftSum + rightSum; // Combine results from both children
    }

}
```