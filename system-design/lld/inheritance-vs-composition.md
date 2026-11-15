### Inheritance vs Composition
- Inheritance creates an IS-A relationship: a Dog IS-A Animal. Composition creates a HAS-A relationship: a Car HAS-A Engine. The general guidance is “favor composition over inheritance,” but that’s not absolute – inheritance has legitimate uses. Knowing when to use which is what separates good from great OOP design.

### When Inheritance Goes Wrong
```java
// PROBLEM: Deep hierarchy with fragile base class
public class Vehicle {
    public void startEngine() { /* ... */ }
    public void accelerate() { /* ... */ }
    public void brake() { /* ... */ }
}

public class Car extends Vehicle {
    public void openTrunk() { /* ... */ }
}

public class ElectricCar extends Car {
    @Override
    public void startEngine() {
        // Awkward: electric cars don't have engines
        // But we inherit startEngine() from Vehicle
        initBattery();
    }
}

public class HybridCar extends Car {
    // Now we need BOTH engine and battery behavior
    // Inheritance can't represent "sometimes gas, sometimes electric"
}

// What about a bicycle? It's a vehicle but has no engine.
// The hierarchy forces us to either:
// 1. Have empty startEngine() implementations (LSP violation)
// 2. Restructure the entire hierarchy
```
### The Composition Fix
```java
// COMPOSITION: Behaviors as components, assembled freely
public interface PowerSource {
    void start();
    void stop();
    boolean isRunning();
}

public class GasEngine implements PowerSource {
    public void start() { /* ignition */ }
    public void stop() { /* kill engine */ }
    public boolean isRunning() { return ignitionOn; }
}

public class ElectricMotor implements PowerSource {
    public void start() { /* activate motor controller */ }
    public void stop() { /* deactivate */ }
    public boolean isRunning() { return motorActive; }
}

public class HybridPower implements PowerSource {
    private final GasEngine gas;
    private final ElectricMotor electric;
    private PowerMode mode = PowerMode.ELECTRIC;
    
    public void start() {
        if (mode == PowerMode.ELECTRIC) electric.start();
        else gas.start();
    }
    // ...
}

// Vehicle composes its capabilities -- no rigid hierarchy
public class Vehicle {
    private final PowerSource power;
    private final BrakingSystem brakes;
    private final Transmission transmission;

    public Vehicle(PowerSource power, BrakingSystem brakes, Transmission transmission) {
        this.power = power;
        this.brakes = brakes;
        this.transmission = transmission;
    }

    public void start() { power.start(); }
    public void stop() { brakes.apply(); power.stop(); }
}

// Electric car: just different components, same Vehicle class
Vehicle electricCar = new Vehicle(new ElectricMotor(), new RegenerativeBrakes(), new SingleGear());
Vehicle gasCar = new Vehicle(new GasEngine(), new DiscBrakes(), new ManualTransmission());
Vehicle hybrid = new Vehicle(new HybridPower(), new RegenerativeBrakes(), new CVT());
```

### When Inheritance IS Appropriate
- True IS-A with Liskov Substitution: A CheckingAccount IS-A BankAccount and can be used anywhere a BankAccount is expected without surprises.
- Shallow hierarchy (2 levels max): Interface -> Concrete implementations.
- Template Method pattern: Shared algorithm skeleton with customizable steps.
- Framework extension points: HttpServlet, TestCase, Activity – designed for subclassing.
    ```java
    // GOOD inheritance: genuine IS-A, shallow, LSP-safe
    public abstract class BankAccount {
        protected Money balance;
    
        public Money getBalance() { return balance; }
    
        public void deposit(Money amount) {
            if (amount.isNegative()) throw new IllegalArgumentException();
            balance = balance.add(amount);
        }
    
        public abstract void withdraw(Money amount);
    }
    
    public class CheckingAccount extends BankAccount {
        private final Money overdraftLimit;
    
        @Override
        public void withdraw(Money amount) {
            if (balance.subtract(amount).isLessThan(overdraftLimit.negate())) {
                throw new OverdraftLimitExceededException();
            }
            balance = balance.subtract(amount);
        }
    }
    
    public class SavingsAccount extends BankAccount {
        @Override
        public void withdraw(Money amount) {
            if (balance.subtract(amount).isNegative()) {
                throw new InsufficientFundsException();
            }
            balance = balance.subtract(amount);
        }
    }
    ```