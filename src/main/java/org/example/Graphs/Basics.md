### Graphs

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