# Algorithmic Pattern Trigger Log

## End-Removal / Boundary Reduction
* **Surface Disguise (The Trap):** Problems asking you to pick, remove, or modify elements from both ends (left and right) of an array/sequence to achieve a target sum or condition.
* **Natural Instinct (The Trap):** Placing pointers at both ends (`0` and `N-1`), comparing them, and making greedy local choices. *(Fails because local choices miss global combinations).*
* **Core Invariant (The Translation):** **Inversion via Sliding Window / Subarray Mapping.**
    * *The Shift:* Stop thinking about the ends. Translate the boundary-removal into finding a contiguous **middle segment** that satisfies the inverse condition (e.g., $\text{Total} - \text{Target}$).
---
## Directional State Machine (Wormhole / Teleportation)
* **Surface Disguise (The Trap):** Problems requiring nested reversals, expanding sequences, or tracking paths that fold back on themselves.
* **Natural Instinct (The Trap):** Physically slicing strings/arrays, swapping elements in place, or running repetitive $O(N^2)$ simulation loops.
* **Core Invariant (The Translation):** **Index Precomputation & Directional Traversal.**
    * *The Shift:* Instead of physically moving data, precompute structural links (wormholes) using a stack. Traverse linearly with a direction flag (`dir = 1` or `-1`), jumping across bounds and flipping direction instantly to achieve $O(N)$ time.
---
## Load Balancing & Fair Splitting
* **Surface Disguise (The Trap):** Splitting a string, array, or graph into two groups to "minimize the maximum" depth, weight, or cost.
* **Natural Instinct (The Trap):** Using Stacks to track pairs, or trying to logically group things by halves (e.g., `depth > max/2`).
* **Core Invariant (The Translation):** **Parity Partitioning (`% 2`).**
  * *The Shift:* Don't try to find a perfect middle ground. Deal the items out like a deck of cards. Assign odd depths/indices to Group 0, and even depths/indices to Group 1. This guarantees a mathematically perfect 50/50 split of the maximum load.
---
## 7. Incremental Window Validation
* **Surface Disguise (The Trap):** Problems asking for the longest/shortest subarray where no 3 elements satisfy a relation (like $a + b = c$ or $a \times b = c$).
* **Natural Instinct (The Trap):** Calling an `isValid(s, e)` function inside the sliding window loop that re-sorts or re-scans the entire window from scratch ($O(N^3)$ TLE).
* **Core Invariant (The Translation):** **Delta Inspection.**
  * *The Shift:* Assume `[s ... e-1]` is 100% valid. When `nums[e]` enters, ONLY inspect the relationships involving `nums[e]`. Never re-check existing elements against each other.