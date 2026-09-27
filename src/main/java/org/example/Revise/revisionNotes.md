# Algorithmic Pattern Trigger Log

## 1. End-Removal / Boundary Reduction
* **Surface Disguise (The Trap):** Problems asking you to pick, remove, or modify elements from both ends (left and right) of an array/sequence to achieve a target sum or condition.
* **Natural Instinct (The Trap):** Placing pointers at both ends (`0` and `N-1`), comparing them, and making greedy local choices. *(Fails because local choices miss global combinations).*
* **Core Invariant (The Translation):** **Inversion via Sliding Window / Subarray Mapping.**
    * *The Shift:* Stop thinking about the ends. Translate the boundary-removal into finding a contiguous **middle segment** that satisfies the inverse condition (e.g., $\text{Total} - \text{Target}$).

---

## 3. Directional State Machine (Wormhole / Teleportation)
* **Surface Disguise (The Trap):** Problems requiring nested reversals, expanding sequences, or tracking paths that fold back on themselves.
* **Natural Instinct (The Trap):** Physically slicing strings/arrays, swapping elements in place, or running repetitive $O(N^2)$ simulation loops.
* **Core Invariant (The Translation):** **Index Precomputation & Directional Traversal.**
    * *The Shift:* Instead of physically moving data, precompute structural links (wormholes) using a stack. Traverse linearly with a direction flag (`dir = 1` or `-1`), jumping across bounds and flipping direction instantly to achieve $O(N)$ time.