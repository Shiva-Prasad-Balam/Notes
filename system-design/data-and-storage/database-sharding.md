## Sharding
- Sharding splits a large database into smaller pieces (shards), each stored on a different server. Each shard holds a subset of the data.
```
Without sharding:
  1 billion rows → 1 database server → CPU maxed, disk full, queries slow

With sharding (4 shards):
  Shard 1: rows where userId 0-249M      (Server 1)
  Shard 2: rows where userId 250M-499M   (Server 2)
  Shard 3: rows where userId 500M-749M   (Server 3)
  Shard 4: rows where userId 750M-1B     (Server 4)
```
---
## When Do You Need Sharding?
- **NOT yet**: database is < 1TB and < 10K queries/sec, a single server with read replicas is enough. Don’t shard prematurely.
- **YES**:
  - Single DB exceeds disk capacity (> 1-5 TB)
  - Write throughput exceeds single server (> 10-50K writes/sec)
  - Read replicas aren’t enough (write-heavy workload)
  - Query latency grows despite indexes
- **Rule of thumb**: Scale reads with replicas first. Shard only when writes are the bottleneck.
---
## Sharding Strategies
1. Hash-Based Sharding: Hash the shard key and mod by number of shards.
```
shard_id = hash(userId) % num_shards

hash("user_001") % 4 = 2 → Shard 2
hash("user_002") % 4 = 0 → Shard 0
hash("user_003") % 4 = 3 → Shard 3
```
**Pros**: Even distribution (if hash function is good). No hotspots from sequential keys.
**Cons**: Range queries impossible (can’t ask “all users with ID 100-200” — they’re scattered). Adding shards requires rehashing (use consistent hashing to fix this).
**Best for**: Key-value lookups, user data, sessions.
2. Range-Based Sharding: Assign contiguous ranges to each shard.
```
Shard 0: userId 0 - 999,999
Shard 1: userId 1,000,000 - 1,999,999
Shard 2: userId 2,000,000 - 2,999,999
```
**Pros**: Range queries work naturally (“get all users from 1M to 1.5M”). Simple to understand.
**Cons**: Hotspots if data isn’t evenly distributed (new users all hit the last shard). Uneven shard sizes over time.
**Best for**: Time-series data (shard by month), geographic data (shard by region).
3. Directory-Based Sharding: Maintain a lookup table that maps each key to its shard.
```
Lookup table:
  user_001 → Shard 2
  user_002 → Shard 0
  user_003 → Shard 1

Query: look up shard in directory, then query that shard.
```
**Pros**: Flexible — can move any key to any shard. Rebalancing is easy (update directory).
**Cons**: Directory is a single point of failure. Extra hop for every query.
**Best for**: When you need fine-grained control over data placement.
4. Geographic Sharding: Shard by user’s geographic region.
```
Shard "US": all US users (servers in us-east-1)
Shard "EU": all EU users (servers in eu-west-1)
Shard "APAC": all Asia-Pacific users (servers in ap-south-1)
```
**Pros**: Data locality (low latency for users near their shard). Compliance (EU data stays in EU for GDPR).
**Cons**: Uneven distribution (US shard might be 5x larger). Cross-region queries are expensive.
---
### Choosing a Shard Key
- The shard key determines which shard a row goes to. It’s the most important decision in sharding.
- Good shard key properties:
    - High cardinality (many unique values — userId is good, country is bad)
    - Even distribution (no single value dominates)
    - Matches query patterns (queries include the shard key → single-shard lookup)
---
### Hot Partition Problem
- One shard gets disproportionately more traffic than others.
- Examples:
    - Celebrity user (10M followers → their shard gets hammered on every post)
    - Time-based shard key (all current writes go to “today” shard)
- Solutions:
    - Add salt/suffix to key: ```shard_key = userId + "_" + random(0,9)``` → spreads across 10 sub-shards. Reads must fan-out.
    - Dedicated shard for hot keys: Detect hot keys → move to a dedicated high-capacity shard
    - Caching in front: Cache hot data aggressively → most reads don’t hit the shard
    - Further split the hot shard: Break one shard into 4 smaller ones
---
### Cross-Shard Queries (The Pain)
- Queries that span multiple shards are expensive.
```
"Get the top 10 orders across all users sorted by amount"
→ Query ALL shards → each returns its top 10 → merge → pick global top 10
→ Scatter-gather pattern (slow, expensive)
```
- To handle:
    - Denormalize: Store duplicate data to avoid cross-shard JOINs (i.e. store product name in orders table).
      - Tradeoff: more storage, more complexity on writes (coz when product name changes, all copies in orders tables need to be updated).
    - Application-side join: Query both shards, merge in app code.
      - Tradeoff: Complex (Querying data from different shards and merging), higher latency (multiple round trips to different shards takes time).
    - Scatter-gather: Fan out query to all shards, aggregate results (Products table sharded across 3 tables,  sends "'shoes'" to Shard 1, Shard 2, and Shard 3 at the exact same time (Fan out)).
      - Tradeoff: Latency = slowest shard (If Shard 1 and 2 finish in 10 ms, but Shard 3 is overloaded and takes 500 ms = slow).
    - Secondary index (Elasticsearch): Copy a subset of that data over to Elasticsearch, can scan millions of records in ms using an "inverted index".
      - Sync data to a search index that isn’t sharded the same way (if sharded by userId, then for search by product name, we need to sync product name to a search index that is sharded by product name. Meaning for each product name search, we can query the search index which will return all the product IDs that match name (Reducing the number of shards we need to query), then we can query the product table to get the product details).
      - Tradeoff: Extra infra (Pay for, manage, secure, and monitor a completely separate server cluster (Elasticsearch)), lag(Eventual Consistency:  When a user updates their profile in your primary database, it takes time (ms to seconds) for that change to sync over to Elasticsearch).
---
### Rebalancing
- When shards become uneven (one grows too large), you need to move data between shards.
  - Approaches:
      - Fixed partitions: Pre-create many partitions (e.g., 1000), assign groups to nodes. Rebalance = move partition groups.
          - Downtime: Minimal (When single node is becomes hotspot, you can move some of the data to another node. Fractional ms is needed to point the shard key to the new node).
      - Dynamic splitting: When a shard exceeds size threshold, split into two.
          - Downtime: Zero (if background).
      - Consistent hashing: Add virtual nodes for new server, only ~K/N keys move.
        ```
        Imagine you have (K = 1,000,000) database keys spread across (N = 4) servers.
      
        The Traditional Way (Modulus Hashing)
      
        In a traditional setup, you find a server using math like: Hash(Key) % Number of Servers.
        If you add a 5th server, the formula changes to % 5. Suddenly, almost 100% of your keys calculate to a completely different server. Your app has to move all 1 million keys across the network. The database effectively goes down during this massive reshuffle.
      
        The Consistent Hashing Way
      
        If you add a 5th server (Server E) to the Hash Ring, you just drop it onto its hashed position on the circle (e.g., at position 600,000).
        Now, look at who is affected:
        • Only the keys sitting between 500,000 and 600,000 (which used to belong to Server B) now bump into Server E first.
        • All other keys on the rest of the ring stay exactly where they are.
        Mathematically, you only have to move roughly (k/N) keys (Total Keys/Total Servers). Instead of moving all 1,000,000 keys, you only move (1,000,000/4 = 250,000) keys. The other 75% of your data doesn't move a single inch. This is why it achieves Zero Downtime.
        ```
---
## Sharding vs Replication
- Sharding:
    - Scale writes + storage
    - Each shard has DIFFERENT data
    - Shard down = that data unavailable
    - High (routing, cross-shard queries)
    - Write-heavy, large data
- Replication:
    - Scale reads + availability
    - Each replica has SAME data
    - Replica down = other replicas serve
    - Medium (replication lag, failover)
    - Read-heavy, high availability
- **You usually need BOTH**: Shard for writes/storage, replicate each shard for reads/HA.