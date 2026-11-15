### Machine Coding Round
- The machine coding round is a 60-90 minute live coding exercise where you design and implement a small system from scratch. Unlike DSA rounds that test algorithmic thinking, this round tests whether you can write production-quality object-oriented code under time pressure.

### The Grading Rubric
- **Working Code (40%)** – Does the core flow actually run? Can you demo it? Partial implementations that compile and handle the happy path score better than ambitious designs that don’t run.
- **Design Quality (30%)** – Are responsibilities cleanly separated? Are you using appropriate patterns? Is there a clear separation between models, services, and orchestration?
- **Extensibility (20%)** – If the interviewer says “now add X,” how much code do you need to change? A good design means adding a new feature touches 1-2 files, not 10.
- **Code Hygiene (10%)** – Naming conventions, no magic numbers, proper access modifiers, small methods. This is the easiest 10% to earn.

### The 90-Minute Breakdown
- Clarify - 0-5 min - Ask questions, write down assumptions
- Design Sketch - 5-15 min - Identify entities, relationships, key interfaces
- Core Models - 15-30 min - Write entity classes, enums, value objects
- Service Layer - 30-55 min - Implement business logic, wire things together
- Driver/Demo - 55-70 min - Write main method that demonstrates the system
- Edge Cases - 70-80 min - Handle nulls, invalid input, boundary conditions
- Polish - 80-90 min - Rename, extract methods, add comments on non-obvious logic

- **The cardinal sin**: Spending 30 minutes on design without writing any code. Interviewers want to see working software. A mediocre design that runs beats an elegant design that doesn’t compile.

### Project Structure That Works
- Keep it simple. You don’t need Spring Boot or dependency injection frameworks.
    ```
    src/
      models/        # Entities, enums, value objects
      service/       # Business logic (one service per responsibility)
      strategy/      # If you use Strategy pattern
      exception/     # Custom exceptions (2-3 max)
      Main.java      # Driver that demos the system
    ```

### Common Mistakes in the First 5 Minutes
- Not asking about scale – “How many concurrent users?” determines whether you need thread safety
- Not asking about persistence – Most machine coding rounds are in-memory only. Don’t waste time on DB schemas
- Not asking about input format – “Will I get API calls, or should I read from stdin?”
- Jumping to code – Without identifying at least your core 3-4 entities, you’ll refactor heavily mid-round
- Sample Clarifying Questions
    - “Is this in-memory only, or should I persist to a database?”
    - “Should I handle concurrent access, or is single-threaded fine?”
    - “What’s the input mechanism – method calls, API, or stdin?”
    - “Which features are must-have vs nice-to-have if time runs short?”
    - “Should I write unit tests, or is a working demo sufficient?”

