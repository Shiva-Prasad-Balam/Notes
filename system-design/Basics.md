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
## SQL vs NoSQL Databases
- SQL Databases:
  - Relational databases that use structured query language (SQL) for defining and manipulating data.
  - Examples: MySQL, PostgreSQL, Oracle Database.
  - Benefits:
    - Strong consistency and ACID properties.
    - Well-suited for complex queries and transactions.
  - Drawbacks:
    - Can be less flexible in handling unstructured data.
- NoSQL Databases:
  - Non-relational databases that can store and retrieve data in various formats (e.g., key-value, document, column-family, graph).
  - Examples: MongoDB, Cassandra, Redis.
  - Benefits:
    - Flexible schema design, suitable for unstructured or semi-structured data.
    - Can handle large volumes of data and high-velocity workloads.
  - Drawbacks:
    - May sacrifice consistency for availability and partition tolerance (CAP theorem).
---
## Load Balancing
- Load balancing is the process of distributing incoming network traffic across multiple servers to ensure no single server becomes overwhelmed.
- Benefits of load balancing:
  - Improves system reliability and availability.
  - Enhances performance by distributing the load evenly.
- Types of load balancing algorithms:
  - Round Robin: Distributes requests sequentially across servers.
  - Least Connections: Directs traffic to the server with the fewest active connections.
  - IP Hash: Routes requests based on the client's IP address, ensuring that a client consistently connects to the same server.
- Load balancers can be implemented at different layers:
  - Layer 4 (Transport Layer): Operates at the TCP/UDP level, making routing decisions based on IP addresses and ports.
  - Layer 7 (Application Layer): Operates at the HTTP/HTTPS level, making routing decisions based on application-level data (e.g., URL, headers, cookies).
- Common load balancing solutions:
  - Hardware Load Balancers: Dedicated physical devices designed for load balancing (e.g., F5 BIG-IP).
  - Software Load Balancers: Software-based solutions that can run on standard servers (e.g., HAProxy, Nginx).
  - Cloud-based Load Balancers: Managed load balancing services provided by cloud providers (e.g., AWS Elastic Load Balancing, Google Cloud Load Balancing).
## Sharding
- Sharding is a database architecture pattern that involves partitioning data across multiple databases or servers to improve performance and scalability.
- Benefits of sharding:
  - Distributes the load across multiple servers, allowing for better performance and scalability.
  - Reduces the risk of a single point of failure by isolating data across shards.
- Drawbacks of sharding:
  - Increases complexity in application logic and database management.
  - Requires careful planning of shard keys to ensure even distribution of data and load.
- Types of sharding:
  - Horizontal Sharding: Distributes rows of a table across multiple databases based on a shard key (e.g., user ID, geographic region).
  - Vertical Sharding: Distributes different tables or columns of a database across multiple databases based on their usage patterns (e.g., separating frequently accessed tables from less frequently accessed ones).
- Considerations for sharding:
  - Choose an appropriate shard key that ensures even distribution of data and load.
  - Monitor and manage shard growth to prevent hotspots and ensure balanced performance.
---
## Data Stores
- Bloom Filters:
  - Used to check if an element is already searched in a set or not.
  - Example: A string is passed through hash functions and the bits at the resulting indices are set to 1. If the string is searched again, it is passed through the same hash functions and if any of the bits at the resulting indices is 0, then it is definitely not in the set. If all bits are 1, then it may be in the set.
  - It is a space-efficient probabilistic data structure that can quickly determine if an element is definitely not in the set or may be in the set.
  - Benefits:
    - Space-efficient: Requires less memory compared to storing the entire set.
    - Fast membership testing: Can quickly determine if an element is in the set or not.
- Data Replication:
  - 2 or more DBs are used to store the same data. This is done to ensure high availability and fault tolerance.
  - One can be a master DB and the others can be slave DBs. The master DB is used for write operations and the slave DBs are used for read operations.
  - Benefits:
    - High availability: If the master DB fails, one of the slave DBs can take over.
    - Fault tolerance: Data is replicated across multiple DBs, so if one DB fails, the data is still available in other DBs.
  - Drawbacks:
    - Increased complexity: Managing multiple DBs can be complex and requires additional resources.
    - Data consistency: Ensuring data consistency across multiple DBs can be challenging, especially in distributed systems.
---
## Consistency vs Availability