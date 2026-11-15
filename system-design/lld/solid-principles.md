### SOLID Principles
- SOLID is a set of five design principles that make object-oriented code easier to extend, test, and maintain. Every machine coding interview implicitly evaluates whether your code follows these principles – even if the interviewer never mentions them by name.

## S – Single Responsibility Principle
- A class should have only one reason to change.
- If your class handles both “parking a vehicle” and “calculating the bill,” those are two separate reasons to change. Pricing logic changes shouldn’t require touching parking logic.
    ```java
    // VIOLATION: This class has three reasons to change
    // 1. Parking logic changes  2. Billing changes  3. Notification changes
    public class ParkingLot {
        public Ticket park(Vehicle v) { /* find slot, assign */ }
        
        public double calculateBill(Ticket t) {
            long hours = Duration.between(t.getEntryTime(), Instant.now()).toHours();
            if (t.getVehicleType() == VehicleType.CAR) return hours * 20;
            if (t.getVehicleType() == VehicleType.BIKE) return hours * 10;
            return hours * 50; // truck
        }
        
        public void sendExitSMS(String phone, double amount) {
            // SMS gateway integration
        }
    }
    
    // FIX: Split into focused classes
    public class ParkingLot {
        private final ParkingStrategy strategy;
        
        public Ticket park(Vehicle v) {
            Slot slot = strategy.findSlot(floors, v.getType());
            slot.occupy(v);
            return new Ticket(v, slot, Instant.now());
        }
        
        public Receipt unpark(Ticket t) {
            t.getSlot().free();
            double amount = billingService.calculate(t);
            return new Receipt(t, amount);
        }
    }
    
    public class BillingService {
        private final Map<VehicleType, PricingRule> rules;
        
        public double calculate(Ticket t) {
            PricingRule rule = rules.get(t.getVehicleType());
            return rule.compute(t.getEntryTime(), Instant.now());
        }
    }
    
    public class NotificationService {
        public void sendExitSMS(String phone, Receipt receipt) { /* ... */ }
    }
    ```
  
### O – Open-Closed Principle
- Classes should be open for extension but closed for modification.
- Adding a new vehicle type or pricing strategy should not require editing existing, tested code. You extend behavior by adding new classes, not by adding else if branches.
    ```java
    // VIOLATION: Adding a new discount type requires modifying this method
    public class DiscountCalculator {
        public double apply(Order order, String discountType) {
            if (discountType.equals("PERCENTAGE")) {
                return order.getTotal() * 0.10;
            } else if (discountType.equals("FLAT")) {
                return 50.0;
            } else if (discountType.equals("BUY_ONE_GET_ONE")) {
                // complex logic...
                return order.getCheapestItem().getPrice();
            }
            // Every new discount = edit this class
            return 0;
        }
    }
    
    // FIX: New discounts are new classes -- existing code untouched
    public interface DiscountStrategy {
        double calculate(Order order);
    }
    
    public class PercentageDiscount implements DiscountStrategy {
        private final double rate;
        public PercentageDiscount(double rate) { this.rate = rate; }
        
        @Override
        public double calculate(Order order) {
            return order.getTotal() * rate;
        }
    }
    
    public class FlatDiscount implements DiscountStrategy {
        private final double amount;
        public FlatDiscount(double amount) { this.amount = amount; }
        
        @Override
        public double calculate(Order order) {
            return Math.min(amount, order.getTotal());
        }
    }
    
    // Adding BOGO? Just add a new class. DiscountCalculator never changes.
    public class BuyOneGetOneDiscount implements DiscountStrategy {
        @Override
        public double calculate(Order order) {
            return order.getCheapestItem().getPrice();
        }
    }
    ```
  
### L – Liskov Substitution Principle
- Subtypes must be substitutable for their base types without breaking correctness.
- If code works with a Bird reference, replacing it with a Penguin (which can’t fly) should not crash or produce nonsensical results. If it does, the hierarchy is wrong.
    ```java
    // VIOLATION: Penguin breaks the contract of Bird
    public class Bird {
        public void fly() { System.out.println("Flying..."); }
    }
    
    public class Penguin extends Bird {
        @Override
        public void fly() {
            throw new UnsupportedOperationException("Penguins can't fly!");
        }
    }
    
    // Any code doing bird.fly() now blows up with Penguin
    public void migrateBirds(List<Bird> birds) {
        for (Bird b : birds) b.fly(); // BOOM on Penguin
    }
    
    // FIX: Separate capabilities into interfaces
    public interface Bird {
        String getName();
    }
    
    public interface Flyable {
        void fly();
    }
    
    public class Sparrow implements Bird, Flyable {
        public String getName() { return "Sparrow"; }
        public void fly() { System.out.println("Sparrow flying"); }
    }
    
    public class Penguin implements Bird {
        public String getName() { return "Penguin"; }
        // No fly() -- Penguin never promises to fly
    }
    
    // Migration only takes Flyable birds
    public void migrateBirds(List<Flyable> birds) {
        for (Flyable b : birds) b.fly(); // Safe -- only flyable birds here
    }
    ```
  
### I – Interface Segregation Principle
- Clients should not be forced to depend on methods they don’t use.
- A fat interface that forces implementors to write empty or throwing methods is a design smell. Split it into focused interfaces.
  ```java
  // VIOLATION: Robot is forced to implement eat() and sleep()
  public interface Worker {
      void work();
      void eat();
      void sleep();
  }
  
  public class Robot implements Worker {
      public void work() { /* welding */ }
      public void eat() { /* ??? */ throw new UnsupportedOperationException(); }
      public void sleep() { /* ??? */ throw new UnsupportedOperationException(); }
  }
  
  // FIX: Split into focused interfaces
  public interface Workable {
      void work();
  }
  
  public interface Feedable {
      void eat();
  }
  
  public interface Restable {
      void sleep();
  }
  
  public class Human implements Workable, Feedable, Restable {
      public void work() { /* ... */ }
      public void eat() { /* ... */ }
      public void sleep() { /* ... */ }
  }
  
  public class Robot implements Workable {
      public void work() { /* welding -- no fake eat/sleep */ }
  }
  ```
  
### D – Dependency Inversion Principle
- High-level modules should not depend on low-level modules. Both should depend on abstractions.
- Your OrderService should not directly instantiate MySQLRepository. It should depend on a Repository interface. This enables testing with mocks and swapping implementations without touching business logic.
  ```java
  // VIOLATION: High-level OrderService depends on concrete MySQL class
  public class OrderService {
      private MySQLOrderRepository repo = new MySQLOrderRepository(); // tight coupling
      
      public void placeOrder(Order order) {
          repo.save(order); // Can't test without MySQL, can't switch to Postgres
      }
  }
  
  // FIX: Depend on abstraction, inject implementation
  public interface OrderRepository {
      void save(Order order);
      Optional<Order> findById(String id);
  }
  
  public class MySQLOrderRepository implements OrderRepository {
      public void save(Order order) { /* MySQL logic */ }
      public Optional<Order> findById(String id) { /* ... */ }
  }
  
  public class InMemoryOrderRepository implements OrderRepository {
      private final Map<String, Order> store = new HashMap<>();
      public void save(Order order) { store.put(order.getId(), order); }
      public Optional<Order> findById(String id) { return Optional.ofNullable(store.get(id)); }
  }
  
  // High-level module depends on interface, not concrete class
  public class OrderService {
      private final OrderRepository repo; // abstraction
      
      public OrderService(OrderRepository repo) { // injected
          this.repo = repo;
      }
      
      public void placeOrder(Order order) {
          repo.save(order); // Works with MySQL, Postgres, or InMemory
      }
  }
  ```
  
