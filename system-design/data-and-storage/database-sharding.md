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