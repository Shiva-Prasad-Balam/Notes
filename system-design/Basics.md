## System Design Basics

## Horizontal Scaling vs Vertical Scaling
- Horizontal Scaling (Scaling Out):
  - Involves adding more machines or nodes to a system to handle increased load.
  - Benefits:
    - Can handle more traffic by distributing the load across multiple servers.
    - Provides redundancy and fault tolerance.
  - Drawbacks:
    - Requires more complex architecture and management.
- Vertical Scaling (Scaling Up):
  - Involves adding more resources (CPU, RAM, storage) to an existing machine to handle increased load.
  - Benefits:
    - Simpler to implement as it involves upgrading existing hardware.
    - Can be more cost-effective for small-scale applications.
  - Drawbacks:
    - Limited by the maximum capacity of the hardware.
---
## Capacity Estimation
- Capacity estimation is the process of determining the resources required to handle a given load on a system.
- Steps to estimate capacity:
  1. Identify the expected load (e.g., number of users, requests per second).
  2. Determine the resource requirements for each request (e.g., CPU, memory, disk I/O).
  3. Calculate the total resource requirements based on the expected load.
  4. Consider peak load scenarios and plan for scalability.
- Tools and techniques for capacity estimation:
  - Load testing tools (e.g., Apache JMeter, Locust)
  - Performance monitoring and profiling tools (e.g., New Relic, Datadog)
---
