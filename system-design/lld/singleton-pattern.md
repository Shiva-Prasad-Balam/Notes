### Singleton Pattern

- Singleton ensures a class has exactly one instance and provides a global point of access to it. The class itself controls instantiation, preventing multiple copies from being created.

### Thread-Safe Implementations

```java
// Approach 1: Eager initialization (simplest)
// Instance created at class loading time -- always thread-safe
public class ConnectionPool {
    private static final ConnectionPool INSTANCE = new ConnectionPool(10);
    
    private final List<Connection> pool;
    
    private ConnectionPool(int size) {
        pool = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            pool.add(createConnection());
        }
    }
    
    public static ConnectionPool getInstance() {
        return INSTANCE;
    }
    
    public Connection acquire() { /* ... */ }
    public void release(Connection conn) { /* ... */ }
}

// Approach 2: Double-checked locking (lazy initialization)
// Instance created on first access -- saves memory if never used
public class ConfigManager {
    private static volatile ConfigManager instance; // volatile is critical
    
    private final Map<String, String> config;
    
    private ConfigManager() {
        config = loadFromFile("application.properties");
    }
    
    public static ConfigManager getInstance() {
        if (instance == null) {                    // First check (no lock)
            synchronized (ConfigManager.class) {
                if (instance == null) {            // Second check (with lock)
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }
    
    public String get(String key) { return config.get(key); }
}

// Approach 3: Enum singleton (recommended for Java)
// Handles serialization, reflection attacks, and thread safety for free
public enum Registry {
    INSTANCE;
    
    private final Map<String, Object> services = new ConcurrentHashMap<>();
    
    public void register(String name, Object service) {
        services.put(name, service);
    }
    
    public <T> T lookup(String name, Class<T> type) {
        return type.cast(services.get(name));
    }
}

// Usage: Registry.INSTANCE.register("emailService", emailService);
```

### Why Double-Checked Locking Needs volatile (Java)

- Without volatile, the JVM can reorder instructions during object construction. Thread A might write the reference to instance before finishing constructor initialization. Thread B sees a non-null instance but reads uninitialized fields.
- The volatile keyword prevents this reordering, ensuring the object is fully constructed before any thread sees it.

### When to Use vs When to Avoid

- Singleton is Appropriate:
  - Connection pool (bounded shared resource)
  - Configuration loaded once
  - Hardware interfaces (printer spooler)
  - True global coordinator (the game board)
- Singleton is an Anti-Pattern:
  - Service classes (use DI instead)
  - Anything that needs per-test isolation
  - Objects with mutable state that tests need to reset
  - “I only need one” (that’s not a reason for Singleton)
