### Factory Pattern

- Factory patterns encapsulate object creation logic. Instead of scattering new ConcreteClass() throughout your code, you centralize creation decisions in one place. When a new type is added, you modify the factory – not every call site.

### The Three Variants

- Simple Factory - One creation point, type decided by parameter - Low complexity
- Factory Method - Subclasses should decide what to instantiate - Medium complexity
- Abstract Factory - Need families of related objects that must be consistent - High complexity

### Simple Factory

- The most common in interviews. A single method that takes a type and returns the right object.
    ```java
    // Simple Factory: centralized creation logic
    public class NotificationFactory {
    
        public static NotificationSender create(NotificationType type) {
            return switch (type) {
                case EMAIL -> new EmailSender();
                case SMS -> new SmsSender();
                case PUSH -> new PushNotificationSender();
                case SLACK -> new SlackSender();
            };
        }
    }
    
    // Usage: caller doesn't know concrete classes
    public class OrderService {
        public void notifyUser(Order order, NotificationType preferred) {
            NotificationSender sender = NotificationFactory.create(preferred);
            sender.send(order.getUserId(), buildMessage(order));
        }
    }
    
    // Adding WhatsApp? Add one case to factory + new class. OrderService unchanged.
    public class WhatsAppSender implements NotificationSender {
        @Override
        public void send(String userId, String message) {
            // WhatsApp Business API call
        }
    }
    ```
  
### Factory Method

- The parent class defines the interface for creation but lets subclasses decide which class to instantiate. Useful when frameworks need to delegate creation to application-specific code.
    ```java
    // Abstract creator with factory method
    public abstract class DocumentExporter {
        
        // Factory method -- subclasses decide what renderer to create
        protected abstract DocumentRenderer createRenderer();
        
        // Template method uses the factory method
        public final byte[] export(Document doc) {
            DocumentRenderer renderer = createRenderer();
            renderer.setMargins(getDefaultMargins());
            renderer.renderHeader(doc.getTitle());
            for (Section section : doc.getSections()) {
                renderer.renderSection(section);
            }
            renderer.renderFooter(doc.getMetadata());
            return renderer.toBytes();
        }
    }
    
    // Concrete creators
    public class PdfExporter extends DocumentExporter {
        @Override
        protected DocumentRenderer createRenderer() {
            return new PdfRenderer(); // Creates PDF-specific renderer
        }
    }
    
    public class HtmlExporter extends DocumentExporter {
        @Override
        protected DocumentRenderer createRenderer() {
            return new HtmlRenderer(); // Creates HTML-specific renderer
        }
    }
    
    public class MarkdownExporter extends DocumentExporter {
        @Override
        protected DocumentRenderer createRenderer() {
            return new MarkdownRenderer();
        }
    }
    
    // Usage
    DocumentExporter exporter = new PdfExporter();
    byte[] result = exporter.export(myDocument);
    ```
  
### Abstract Factory

- Creates families of related objects. When you need a Button, TextField, and Menu that all belong to the same theme (dark mode vs light mode), Abstract Factory ensures consistency.
    ```java
    // Abstract factory: produces a family of related objects
    public interface DatabaseDriverFactory {
        Connection createConnection(String url);
        QueryBuilder createQueryBuilder();
        MigrationRunner createMigrationRunner();
    }
    
    // Concrete factory: PostgreSQL family
    public class PostgresDriverFactory implements DatabaseDriverFactory {
        @Override
        public Connection createConnection(String url) {
            return new PostgresConnection(url);
        }
        
        @Override
        public QueryBuilder createQueryBuilder() {
            return new PostgresQueryBuilder(); // Handles JSONB, arrays, etc.
        }
        
        @Override
        public MigrationRunner createMigrationRunner() {
            return new PostgresMigrationRunner();
        }
    }
    
    // Concrete factory: MySQL family
    public class MySQLDriverFactory implements DatabaseDriverFactory {
        @Override
        public Connection createConnection(String url) {
            return new MySQLConnection(url);
        }
        
        @Override
        public QueryBuilder createQueryBuilder() {
            return new MySQLQueryBuilder(); // Different syntax for JSON, no arrays
        }
        
        @Override
        public MigrationRunner createMigrationRunner() {
            return new MySQLMigrationRunner();
        }
    }
    
    // Client code works with any database -- never references concrete classes
    public class Repository {
        private final Connection connection;
        private final QueryBuilder queryBuilder;
    
        public Repository(DatabaseDriverFactory factory, String dbUrl) {
            this.connection = factory.createConnection(dbUrl);
            this.queryBuilder = factory.createQueryBuilder();
        }
        
        public List<User> findActiveUsers() {
            String query = queryBuilder.select("users").where("active", true).build();
            return connection.execute(query, User::fromRow);
        }
    }
    ```
  
### When to Use vs When to Avoid

- Use Factory When:
  - Object creation depends on a type parameter
  - You want to hide concrete class names from callers
  - Creation logic is complex (config, validation, setup)
  - New types will be added over time
  - Testing needs mock/stub objects
- Avoid When:
  - Direct new is simple and obvious
  - Only one implementation exists
  - Constructor is trivial
  - Type set is fixed and tiny
  - Mocking isn’t a concern

