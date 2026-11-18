### The 5-Minute Design Sketch
- The first 10 minutes of a machine coding round determine whether you’ll finish. A structured approach to identifying classes, relationships, and patterns saves you from refactoring mid-round. This page gives you a repeatable process.

1. Extract Nouns (Entities) – 2 Minutes
- Read the problem statement and underline every noun. These are your candidate classes.
- Example problem: “Design a parking lot with multiple floors. Each floor has slots for different vehicle types (car, bike, truck). Users can park and unpark vehicles. The system generates a ticket on entry and a receipt with billing on exit.”
    - Nouns extracted:
        - Parking Lot
        - Floor
        - Slot
        - Vehicle (Car, Bike, Truck)
        - Ticket
        - Receipt
        - User (probably not a class – just the caller)
    - Filtering: Remove nouns that are:
        - Primitive values (name, id, amount – these are fields, not classes)
        - Duplicates (car/bike/truck are vehicle types, not separate classes)
        - Out of scope (user authentication, payment gateway)
    - Result: 5-7 core entities is the sweet spot.

2. Extract Verbs (Responsibilities) – 1 Minute
- Verbs tell you what each entity does and what services you need.
- Park - System - ParkingLot.park()
- Unpark - System - ParkingLot.unpark()
- Generate ticket - System - Ticket created in park()
- Calculate billing - BillingService - BillingService.calculate()
- Find available slot - Strategy - ParkingStrategy.findSlot()

3. Map Relationships – 1 Minute
    ```
    // Relationships become fields and constructor parameters:
    public class ParkingLot {
        private List<Floor> floors;          // ParkingLot OWNS floors (composition)
        private ParkingStrategy strategy;     // ParkingLot USES strategy (dependency)
        private BillingService billing;       // ParkingLot USES billing (dependency)
    }
    
    public class Floor {
        private int floorNumber;
        private List<Slot> slots;            // Floor OWNS slots (composition)
    }
    
    public class Slot {
        private SlotType type;
        private Vehicle vehicle;             // Slot REFERENCES vehicle (association)
        private boolean available;
    }
    
    public class Ticket {
        private Vehicle vehicle;             // Ticket REFERENCES vehicle
        private Slot slot;                   // Ticket REFERENCES slot
        private Instant entryTime;
    }
    ```
   
4. Spot Patterns – 1 Minute
- Look for these triggers in the problem statement:
    - “Multiple types of X” or “different strategies” - Strategy
    - “States: idle, active, done” or lifecycle - 	State
    - “Notify when X happens” - Observer
    - “Undo/redo” or “transaction history” - Command
    - “Contains groups of groups” or tree structure - Composite
    - “Optional add-ons” or layered behavior - Decorator
    - “Create objects based on type” - Factory
    - “Many optional fields” - Builder
- For our parking lot example:
    - “Different allocation strategies” -> Strategy pattern for slot finding
    - “Different vehicle types” -> Enum (not a pattern – just VehicleType.CAR, BIKE, TRUCK)
    - “Billing” -> Could be Strategy if multiple pricing models exist, otherwise just a service

5. Decide What to Build First – 30 Seconds
- Priority order:
  1. Models first – Vehicle, Slot, Floor, Ticket, Receipt (just data, no logic)
  2. Core service – ParkingLot.park() and unpark() – the happy path
  3. Supporting services – BillingService, ParkingStrategy
  4. Main/Driver – Demo that shows it working
  5. Edge cases and polish – Validation, exceptions, if time permits
- The Complete Sketch (What You’d Write on Paper)
    ```
    Entities: ParkingLot, Floor, Slot, Vehicle, Ticket, Receipt
    Enums: VehicleType (CAR, BIKE, TRUCK), SlotType (SMALL, MEDIUM, LARGE)
    
    Relationships:
      ParkingLot --owns--> Floor[] --owns--> Slot[]
      Slot --refs--> Vehicle (nullable)
      Ticket --refs--> Vehicle, Slot, entryTime
    
    Services:
      ParkingStrategy (interface): findSlot(floors, vehicleType) -> Slot
        - NearestFirstStrategy
      BillingService: calculate(ticket) -> Money
    
    Patterns: Strategy (parking allocation)
    
    Build order: models -> ParkingLot.park/unpark -> billing -> main
    ```


