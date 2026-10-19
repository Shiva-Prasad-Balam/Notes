## Graphs

### BFS Traversal

```java
public void bfsTraversal(int vertices, List<List<Integer>> adjacencyList) {
    boolean[] visited = new boolean[vertices]; // TC - O(V + E), SC - O(V)
    Queue<Integer> queue = new LinkedList<>();
    queue.add(0); // Start from the first vertex (0)
    visited[0] = true;
    while(!queue.isEmpty()) {
        int currentVertex = queue.poll();
        System.out.print(currentVertex + " ");
        for (int neighbor : adjacencyList.get(currentVertex)) {
            if (!visited[neighbor]) {
                visited[neighbor] = true;
                queue.add(neighbor);
            }
        }
    }
}
```

### DFS Traversal

```java
public void dfs(int vertex, boolean[] visited, List<List<Integer>> adjacencyList) {
    visited[vertex] = true;
    System.out.print(vertex + " ");
    for (int neighbor : adjacencyList.get(vertex)) {
        if (!visited[neighbor]) {
            dfs(neighbor, visited, adjacencyList);
        }
    }
}
public void dfsTraversal(int vertices, List<List<Integer>> adjacencyList) {
    boolean[] visited = new boolean[vertices]; // TC - O(V + E), SC - O(V)
    dfs(0, visited, adjacencyList); // Start from the first vertex (0)
}
```
---
## Undirected Graphs

### Cycle Detection in Undirected Graphs

- To detect cycles in an undirected graph, we can use Depth-First Search (DFS) along with a parent parameter to keep track of the vertex from which we came. If we encounter a visited vertex that is not the parent of the current vertex, it indicates a cycle.

```java
boolean dfsHasCycle(int vertex, int parent, boolean[] visited, List<List<Integer>> adj) {
    visited[vertex] = true;
    for (int neighbor : adj.get(vertex)) { 
        if (!visited[neighbor]) {
            if (dfsHasCycle(neighbor, vertex, visited, adj)) {
                return true; // Cycle detected in the recursive call
            }
        } else if (neighbor != parent) {
            return true; // Cycle detected as the neighbor is already visited and is not the parent
        }
    }
    return false; // No cycle detected from this vertex
}
public boolean detect(int V, List<List<Integer>> adj) {
    boolean[] visited = new boolean[V];
    for (int i = 0; i < V; i++) {
        if (!visited[i]) {
            if (dfsHasCycle(i, -1, visited, adj)) {
                return true;
            }
        }
    }
    return false;
}
```   

### Distance from Source in Undirected Graphs
- To find the distance from a source vertex to all other vertices in an undirected graph, we can use Breadth-First Search (BFS). We maintain a distance array initialized to -1 (indicating unvisited vertices) and update the distance as we traverse the graph.

```java
public void distanceFromSource(int vertices, List<List<Integer>> adjacencyList, int source) {
    int[] distance = new int[vertices];
    Arrays.fill(distance, -1); // Initialize distances to -1 (unvisited)
    Queue<Integer> queue = new LinkedList<>();
    queue.add(source);
    distance[source] = 0; // Distance to the source is 0
    while (!queue.isEmpty()) {
        int currentVertex = queue.poll();
        for (int neighbor : adjacencyList.get(currentVertex)) {
            if (distance[neighbor] == -1) { // If the neighbor has not been visited
                distance[neighbor] = distance[currentVertex] + 1; // Update the distance
                queue.add(neighbor); // Add the neighbor to the queue for further exploration
            }
        }
    }
    // Print the distances from the source to all vertices
    System.out.println(Arrays.toString(distance));
}
```
---
### Shortest from source to all vertices in an undirected graph
- To find the shortest distance from a source vertex to all other vertices in an undirected graph, we can use Dijkstra's algorithm. This algorithm uses a priority queue to explore the vertices with the smallest known distance first, updating the distances to neighboring vertices as shorter paths are found.
- Will fail for negative weights or cycles, as Dijkstra's algorithm assumes all edge weights are non-negative.
```java
public void dijkstra(int vertices, List<List<int[]>> adjacencyList, int source) {
    int[] distance = new int[vertices];
    Arrays.fill(distance, Integer.MAX_VALUE); // Initialize distances to infinity
    distance[source] = 0; // Distance to the source is 0
    PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1])); // Min-heap based on distance
    pq.add(new int[]{source, 0}); // Add the source vertex with distance 0
    while (!pq.isEmpty()) {
        int[] current = pq.poll();
        int currentVertex = current[0];
        int currentDistance = current[1];
        if (currentDistance > distance[currentVertex]) continue; // Skip if we have already found a shorter path
        for (int[] neighbor : adjacencyList.get(currentVertex)) {
            int neighborVertex = neighbor[0];
            int edgeWeight = neighbor[1];
            if (distance[currentVertex] + edgeWeight < distance[neighborVertex]) {
                distance[neighborVertex] = distance[currentVertex] + edgeWeight; // Update the distance
                pq.add(new int[]{neighborVertex, distance[neighborVertex]}); // Add the neighbor to the priority queue
            }
        }
    }
    // Print the shortest distances from the source to all vertices
    System.out.println(Arrays.toString(distance));
}
```
### Shortest from source to all vertices in an undirected graph with negative weights
- Bellman-Ford algorithm can be used which will handle negative weights and detect negative cycles. It relaxes all edges up to V-1 times, where V is the number of vertices.
- If we can still relax an edge in the V-th iteration, it indicates a negative cycle in the graph.

```java
public void bellmanFord(int vertices, int[][] edges, int source) {
    int[] distance = new int[vertices];
    Arrays.fill(distance, Integer.MAX_VALUE); // Initialize distances to infinity
    distance[source] = 0; // Distance to the source is 0
    for (int i = 1; i <= vertices; i++) {
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int weight = edge[2];
            
            int newDistance = distance[u] + weight;
            if (distance[u] != Integer.MAX_VALUE && newDistance < distance[v]) {
              if (i == vertices ) { // If we are in the V-th iteration, a negative cycle is detected
                System.out.println("Graph contains a negative-weight cycle");
                return; // Negative cycle detected
              }
              distance[v] = newDistance; // Update the distance
            }
        }
    }
}
```
---
### Floyd-Warshall Algorithm
- Used to find the shortest paths between all pairs of vertices in a weighted graph.
- Uses matrix graph representation and explores x to y via z by tracking distance from x to y and checking if going through z provides a shorter path.
- This x to y via z approach is repeated for all vertices as intermediate points, ensuring that all possible paths are considered.
```java
public void floydWarshall(int vertices, int[][] edges) {
    int[][] dist = new int[vertices][vertices];
    for(int i = 0; i < vertices; i++) {
        Arrays.fill(dist[i], Integer.MAX_VALUE); // Initialize distances to infinity
        dist[i][i] = 0; // Distance from a vertex to itself is 0
    }
    for(int[] edge : edges) {
        int u = edge[0];
        int v = edge[1];
        int weight = edge[2];
        dist[u][v] = weight; // Set the distance for the edge
        dist[v][u] = weight; // For undirected graphs, set the distance in both directions
    }

    for(int k = 0; k < vertices; k++) { // Go from i to j via k which exhaustively checks all vertices as intermediate points
        for(int i = 0; i < vertices; i++) {
            for(int j = 0; j < vertices; j++) {
                if (dist[i][k] != Integer.MAX_VALUE && dist[k][j] != Integer.MAX_VALUE) {
                    dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]); // Update the shortest path
                }
            }
        }
    }
    // The values in dist[i][j] now represent the shortest distance from vertex i to vertex j
}
```
---
### Minimum Spanning Tree (MST)
- MST is a graph with n vertices and n-1 edges and sum of n-1 edges is minimum.
- All vertices are connected and there are no cycles.
```java
public void primsAlgoToFindMST(int vertices, List<List<int[]>> adjacencyList) {
    boolean[] visited = new boolean[vertices];
    PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0])); // Min-heap based on edge weight
    pq.add(new int[]{0, 0, -1}); // {weight, vertex, parentVertex}
    int mstWeight = 0; // Total weight of the MST
    List<int[]> mstEdges = new ArrayList<>(); // To store the edges of the MST
    while (!pq.isEmpty()) {
        int[] current = pq.poll();
        int currentWeight = current[0];
        int currentVertex = current[1];
        int parentVertex = current[2];
        
        if (visited[currentVertex]) continue;
        visited[currentVertex] = true;
        mstWeight += currentWeight;
        
        if (parentVertex != -1) {
            mstEdges.add(new int[]{parentVertex, currentVertex, currentWeight});
        }
        for (int[] edge : adjacencyList.get(currentVertex)) {
            int neighbor = edge[0];
            int weight = edge[1];
            if (!visited[neighbor]) {
                pq.add(new int[]{weight, neighbor, currentVertex}); // Add the edge to the priority queue
            }
        }
    }
}
```
---
## Directed Graphs

- A directed graph is a graph in which edges have a direction, meaning they go from one vertex to another. In a directed graph, an edge from vertex u to vertex v is represented as (u, v), indicating that there is a connection from u to v, but not necessarily from v to u.

### Cycle Detection in Directed Graphs

- To detect cycles in a directed graph, we can use Depth-First Search (DFS) along with two boolean arrays: one to keep track of visited vertices and another to keep track of the vertices in the current path of the DFS. If we encounter a vertex that is already in the current path, it indicates a cycle.

```java
boolean dfsHasCycle(int vertex, boolean[] visited, boolean[] pathVisited, List<List<Integer>> adj) {
    visited[vertex] = true;
    pathVisited[vertex] = true;
    for (int neighbor : adj.get(vertex)) {
        if (!visited[neighbor]) {
            if (dfsHasCycle(neighbor, visited, pathVisited, adj)) {
                return true; // Cycle detected in the recursive call
            }
        } else if (pathVisited[neighbor]) {
            return true; // Cycle detected as the neighbor is already in the current path
        }
    }
    pathVisited[vertex] = false; // Backtrack: remove the vertex from the current path
    return false; // No cycle detected from this vertex
}
public boolean detect(int V, List<List<Integer>> adj) {
    boolean[] visited = new boolean[V];
    boolean[] pathVisited = new boolean[V];
    for (int i = 0; i < V; i++) {
        if (!visited[i]) {
            if (dfsHasCycle(i, visited, pathVisited, adj)) {
                return true;
            }
        }
    }
    return false;
}
```

### Topological Sort

- Topological sorting for a Directed Acyclic Graph (DAG) is a linear ordering of vertices such that for every directed
  edge u → v, vertex u comes before v in the ordering.

```java
public void dfs(List<List<Integer>> adj, int vertex, boolean[] visited, Stack<Integer> stack) {
    visited[vertex] = true;
    for (int neighbor : adj.get(vertex)) {
        if (!visited[neighbor]) {
            dfs(adj, neighbor, visited, stack);
        }
    }
    stack.push(vertex); // Push the vertex onto the stack after visiting all its neighbors
}
public void topologicalSort(int vertices, List<List<Integer>> adj) {
    boolean[] visited = new boolean[vertices];
    Stack<Integer> stack = new Stack<>();
    for (int i = 0; i < vertices; i++) {
        if (!visited[i]) {
            dfs(adj, i, visited, stack);
        }
    }
    while (!stack.isEmpty()) {
        System.out.print(stack.pop() + " ");
    }
}
```
- BFS-based Kahn's algorithm for topological sorting uses in-degree of vertices and a queue to process vertices with zero in-degree.

```java
public void topologicalSortKahn(int vertices, List<List<Integer>> adj) {
    int[] inDegree = new int[vertices];
    for (int i = 0; i < vertices; i++) {
        for (int neighbor : adj.get(i)) {
            inDegree[neighbor]++;
        }
    }
    Queue<Integer> queue = new LinkedList<>();
    for (int i = 0; i < vertices; i++) {
        if (inDegree[i] == 0) {
            queue.add(i);
        }
    }
    while (!queue.isEmpty()) {
        int currentVertex = queue.poll();
        System.out.print(currentVertex + " ");
        for (int neighbor : adj.get(currentVertex)) {
            inDegree[neighbor]--;
            if (inDegree[neighbor] == 0) {
                queue.add(neighbor);
            }
        }
    }
}
```