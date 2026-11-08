## Load Balancing
- A load balancer distributes incoming requests across multiple servers so no single server gets overwhelmed.
```
Without LB:
  All 10,000 users → Server 1 → crashes
With LB:
  10,000 users → Load Balancer → Server 1 (3,333 users)
                                → Server 2 (3,333 users)
                                → Server 3 (3,334 users)
```
## Why You Need It
- **High availability**: If one server dies, LB routes to healthy servers. Zero downtime.
- **Scalability**: Add more servers behind the LB when traffic grows.
- **Performance**: Spread load evenly so no server is a bottleneck.
- **Maintenance**: Take a server offline for updates without affecting users.
![load-balancer](./load-balancer-diagram.png)