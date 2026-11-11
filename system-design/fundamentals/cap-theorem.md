### CAP Theorem
- CAP theorem states that a distributed system can only guarantee TWO of these three properties simultaneously:
  - Consistency — Every read gets the most recent write (all nodes see the same data at the same time)
  - Availability — Every request gets a response (even if it’s stale data)
  - Partition Tolerance — System works even when network communication between nodes breaks

- The catch: Network partitions WILL happen in distributed systems (routers fail, cables break, cloud AZs lose connectivity). So Partition Tolerance is not optional — you must have P.
- This means your real choice is: C or A (during a partition).
    ```
    During normal operation: You get all three (C + A + P)
    During a network partition: You must choose C OR A
    
    Choose C (Consistency): System refuses to serve potentially stale data → some requests fail
    Choose A (Availability): System serves whatever data it has → some responses might be stale
    ```
  
### The Analogy
- Imagine two bank branches (Node A and Node B) that share account data. The phone line between them breaks (partition).
- If you choose Consistency (CP):
    - Customer at Branch B wants to withdraw $500
    - Branch B can’t verify the balance with Branch A (partition)
    - Branch B says: “Sorry, system is unavailable. Come back later.”
    - No wrong transaction happens, but service is down.
- If you choose Availability (AP):
    - Customer at Branch B wants to withdraw $500
    - Branch B can’t verify with Branch A, but serves anyway using its last known balance
    - Customer withdraws $500
    - Meanwhile, Customer ALSO withdraws at Branch A (balance was already $500)
    - Total withdrawn: $1000 from a $500 account (inconsistency!)
    - But both branches stayed “available.”

### CP vs AP Systems
- CP Systems (Consistency + Partition Tolerance): During a partition, the system becomes unavailable rather than serve stale data.
    - Postgres (single primary): Writes go to one node. If primary is unreachable, writes fail.
    - MongoDB (default write concern): Writes to primary only. If primary is down, writes fail until election.
    - When to choose CP: Financial transactions, inventory management, any system where wrong data is worse than no data.
- AP Systems (Availability + Partition Tolerance): During a partition, the system continues serving requests but might return stale data.
    - Cassandra: Always writable to any node. Resolves conflicts later (last-write-wins).
    - DynamoDB: Configurable — default is eventually consistent reads (AP).
    - DNS: Returns cached (possibly stale) records. Always available.
    - CDN: Serves cached content even if origin is down.
    - When to choose AP: Social media feeds, product catalogs, DNS, session stores — where stale data is acceptable for a few seconds.

### Consistency Models (Spectrum)
- It’s not binary (consistent vs inconsistent). There’s a spectrum:

| Model | Meaning | Latency | Example |
|---|---|---|---|
| Strong Consistency | Read always returns the latest write | Highest | Bank balance, Postgres primary |
| Linearizability | Operations appear in real-time order | Very high | ZooKeeper |
| Sequential Consistency | Operations appear in the same order on all nodes, but not necessarily in real time | High | — |
| Causal Consistency | If A caused B, everyone sees A before B | Medium | Comments thread |
| Read-Your-Writes | You always see your own latest write | Medium | After posting, you see your post |
| Eventual Consistency | All nodes converge eventually (seconds to minutes) | Lowest | DNS, social feeds, CDN |

- For interviews, you only need to know:
    - Strong consistency (CP) — for money/critical data 
    - Eventual consistency (AP) — for everything else 
    - Read-your-writes — compromise (after write, read from primary; otherwise read from replica)

### Real-World Examples in System Design
1. Digital Wallet (CP)
    ```
    User A has $100.
    User A transfers $100 to User B.
    User A tries to spend $100 again.
    
    CP (correct): Second spend is rejected — balance is 0.
    AP (dangerous): Second spend might succeed if nodes are partitioned → $100 created from nothing.
    ```
   - Decision: Financial systems MUST be CP. Lost availability (retry later) is better than lost money.

2. Instagram Feed (AP):
    ```
    User posts a photo.
    Follower opens app 2 seconds later.
    
    CP: Follower sees "loading..." until all replicas are consistent.
    AP: Follower might not see the photo for 2-5 seconds (eventual consistency). That's fine.
    ```
   - Decision: Social feeds use AP. Seeing a post 3 seconds late doesn’t matter. Unavailability would be worse.

3. Chat System (Compromise):
    ```
    Message ordering within a conversation: needs consistency (can't show messages out of order).
    Read receipts: eventual consistency is fine (delay of 1-2s is OK).
    Online status: eventual consistency (green dot can be 10s stale).
    ```
   - Decision: Different parts of the same system can have different consistency requirements.

### PACELC Theorem (Extended CAP)
- CAP only talks about what happens DURING a partition. PACELC adds: what happens during NORMAL operation?
    ```
    PAC: During Partition → choose Availability or Consistency
    ELC: Else (no partition) → choose Latency or Consistency
    ```

1. "PAC"(Partition / Availability / Consistency) Part (When things break / Partition):
    - Partition $\rightarrow$ Choose Availability or Consistency.
    - This is identical to CAP. If a network cable is cut between Server Node 1 and Server Node 2, does the system stay up and accept potentially conflicting data (Available), or does it shut down parts of itself to stay accurate (Consistent)?

2. "ELC" Part (When things are normal / Else):
    - Else (Normal Operation) $\rightarrow$ Choose Latency or Consistency.
    - This is the new part. Even when the network is 100% healthy and sunny, you still have to make a trade-off on every single read and write.
        - If you choose Consistency (C) during normal operation: When the write comes in, the primary node must pause, send the update over the network to the other 2 replicas, wait for both of them to write it to disk and reply "ACK", and only then return a success response to the user.
          - The Result: Data is perfectly consistent everywhere, but the user had to stare at a loading spinner for 300ms (Higher Latency).
        - If you choose Latency (L) during normal operation: The primary node writes the data locally, immediately returns a success response to the user in 5ms (Low Latency), and lets the other replicas catch up quietly in the background.
          - The Result: Super fast, but if another user reads from a replica 10 milliseconds later, they might see the old profile picture temporarily (Eventual Consistency).

- When to choose Consistency (PC / EC) — SQL & Relational DBs:
  - PostgreSQL, MySQL, AWS Aurora, Google Spanner.
  - When to use it: Whenever correctness, money, or state accuracy matters more than raw speed.
  - Interview justification: "For the wallet and ledger service, we need strong consistency. We will use PostgreSQL because a user cannot see a stale balance, even if it adds 10ms of replication latency."
- When to choose Availability & Low Latency (PA / EL) — NoSQL & Distributed DBs
  - Examples: Apache Cassandra, ScyllaDB, DynamoDB (configured for eventual consistency).
  - When to use it: Massive scale, high write throughput, real-time feeds, chat messages, metrics, or IoT logs where losing a millisecond of sync or seeing a slightly old profile picture doesn't break the business.
  - Interview justification: "For the chat message history or clickstream analytics, throughput and ultra-low latency are our top priorities. We will use Cassandra because it favors availability and horizontal scaling, allowing us to absorb millions of writes without locking up."

- Translation: Even without partitions, you trade latency for consistency. Strong consistency requires waiting for all replicas to acknowledge. Eventual consistency returns immediately (faster) but might be stale.\

