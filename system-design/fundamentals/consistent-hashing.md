### Consistent Hashing

```java
public class ConsistentHash<T> {
    private final TreeMap<Integer, T> ring = new TreeMap<>();
    private final int virtualNodes;

    public ConsistentHash(int virtualNodes) {
        this.virtualNodes = virtualNodes;
    }

    public void addNode(T node) {
        for (int i = 0; i < virtualNodes; i++) {
            int hash = hash(node.toString() + "_" + i);
            ring.put(hash, node);
        }
    }

    public void removeNode(T node) {
        for (int i = 0; i < virtualNodes; i++) {
            int hash = hash(node.toString() + "_" + i);
            ring.remove(hash);
        }
    }

    public T getNode(String key) {
        if (ring.isEmpty()) return null;
        int hash = hash(key);
        // Find first server clockwise (ceiling)
        Map.Entry<Integer, T> entry = ring.ceilingEntry(hash);
        if (entry == null) {
            // Wrap around to first server
            entry = ring.firstEntry();
        }
        return entry.getValue();
    }

    private int hash(String key) {
        // Use MD5 or MurmurHash for good distribution
        return Math.abs(key.hashCode()) % 360;
    }
}
```