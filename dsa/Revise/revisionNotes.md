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
## Incremental Window Validation
* **Surface Disguise (The Trap):** Problems asking for the longest/shortest subarray where no 3 elements satisfy a relation (like $a + b = c$ or $a \times b = c$).
* **Natural Instinct (The Trap):** Calling an `isValid(s, e)` function inside the sliding window loop that re-sorts or re-scans the entire window from scratch ($O(N^3)$ TLE).
* **Core Invariant (The Translation):** **Delta Inspection.**
  * *The Shift:* Assume `[s ... e-1]` is 100% valid. When `nums[e]` enters, ONLY inspect the relationships involving `nums[e]`. Never re-check existing elements against each other.
---
## Decision Tree Guardrails (Backtracking)
* **Surface Signal:** "Generate ALL valid X", "Return all subsets/permutations".
* **Immediate Pivot:** Stop thinking about loops, shifts, or array swaps. You are building a Recursive Decision Tree.
* **The Derivation Rule:**
  1. Define the options at step `i` (e.g., add `(` or add `)`).
  2. Ask: "Under what exact state is Option A legal?" (`op < n`)
  3. Ask: "Under what exact state is Option B legal?" (`cl < op`)
---
$$\text{Trigger ("Generate ALL / Find ALL") } + \text{ Small Constraints } (N \le 25) \longrightarrow \text{ Decision Tree (Backtracking)}$$
```text
1. READ CONSTRAINTS & PROMPT
   ├── "Generate ALL..." / "Find ALL..."  ──> Signal: Recursion / Backtracking
   └── N <= 25                             ──> Signal: O(2^N) fits within time limits

2. DEFINE THE DECISION TREE
   └── At index i, what are my explicit choices?
       ├── Option 1: KEEP element s[i] (recurse to i + 1)
       └── Option 2: SKIP / DELETE element s[i] (recurse to i + 1 if budget > 0)

3. COMPUTE THE BUDGET (Pruning Guardrails)
   └── Run a linear pass first to get exact removal limits (e.g., extraOpen, extraClose)
   └── Use these limits as budgets to prune invalid recursive branches early
```
---
### Bitwise Column Invariance & Redistribution
* **Trigger:** Bitwise operators (`&`, `|`, `^`) + operations that feel like they mutate numbers across the whole array.
* **Core Insight:** Numbers are an illusion. Bit positions ($0$ to $31$) are **completely independent vertical lanes**. Operations either conserve bit counts or redistribute tokens within the same column.
* **Universal Playbook:**
  1. **Vertical Collapse:** Count `1`s per bit position across all elements into a frequency array (`bits[32]`).
  2. **De-couple:** Stop looking at numbers horizontally. Treat each column as an isolated resource pool.
  3. **Reconstruct/Calculate:** Build your target values or compute contributions column-by-column (greedily or via math) instead of simulating array swaps.
---
## Graph Tree Invariants
- **Skeleton:** $N$ nodes and $N-1$ edges guarantees a single connected tree (no unconstrained cycles).
- **Root Property:** Exactly **one** node must have an in-degree of `0` (the root).
- **Parent Property:** Every other node must have an in-degree of `1` (exactly one parent). Total in-degree sum equals $N-1$.
- **Branching Constraint:** For a Binary Tree, every node's out-degree must be $\le 2$.
- **Failure Flags:**
  - **0 nodes with in-degree 0** $\rightarrow$ Cycle detected.
  - **>1 nodes with in-degree 0** $\rightarrow$ Disconnected graph or multiple roots.
---
## Sum of Sortable Integers - Pattern Notes
- **Divisor Constraint:** Only check $k$ where $n \pmod k == 0$.
- **Global Order Rule:** $\text{prevMax}[\text{left block}] \le \text{currMin}[\text{current block}]$ across all block boundaries.
- **Internal Order Rule:** Total circular descents (internal drops + last-to-first wrap-around drop) inside each block must be $\le 1$.
- **Optimization Strategy:** Precompute prev max, current min, and previous element > current counts to evaluate every block in $O(1)$ time.
---
## Interval Overlaps & Ranges (Dates / Times / Coordinates)
* **Trigger:** Overlapping date ranges, time slots, or coordinate intervals where you need to find shared duration.
* **The Shift:** Abandon messy chronological `if-else` branching and compound unit logic.
* **Rules & Conversion:**
  * **Flatten Compound Units:** Never handle nested fields (months/days or hours/minutes) separately. Convert compound formats into a **single linear integer/counter** first (e.g., prefix-sum day of the year, total minutes from midnight).
  * **Overlap Start:** Latest start wins $\rightarrow$ `Math.max(start1, start2)`
  * **Overlap End:** Earliest end wins $\rightarrow$ `Math.min(end1, end2)`
  * **Safe Calculation:** `Math.max(0, overlapEnd - overlapStart + 1)` (automatically handles non-overlapping intervals by clamping negative values to `0`).