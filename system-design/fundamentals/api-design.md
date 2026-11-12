### API Design
- API design is defining the contract between clients and servers — what endpoints exist, what data goes in, what comes out, and how errors are communicated.
    ```
    Client                          Server
      │                               │
      │  POST /orders                 │
      │  { "item": "pizza",          │
      │    "size": "large" }          │
      │ ─────────────────────────────▶│
      │                               │  Validate
      │                               │  Process
      │  201 Created                  │
      │  { "orderId": "abc123",      │
      │    "status": "confirmed" }    │
      │◀───────────────────────────── │
    ```

### REST (Representational State Transfer)
- The most common API style. Uses HTTP methods + resource-based URLs.
- REST Principles:
    - Resources - Everything is a noun - /users, /orders, /products
    - HTTP Methods - Verbs = actions - GET, POST, PUT, DELETE, PATCH
    - Stateless - Each request is self-contained - Token in every request header
    - Uniform Interface - 	Predictable URL patterns - GET /users/123/orders
- HTTP Methods
    ```
    GET    /users          → List all users (read)
    GET    /users/123      → Get user 123 (read)
    POST   /users          → Create a new user (create)
    PUT    /users/123      → Replace user 123 entirely (full update)
    PATCH  /users/123      → Partially update user 123 (partial update)
    DELETE /users/123      → Delete user 123 (delete)
    ```
- REST Response Codes
    - 200 - OK - Successful GET, PUT, PATCH
    - 201 - Created - Successful POST
    - 204 - No Content - Successful DELETE
    - 400 - Bad Request - Validation failed
    - 401 - Unauthorized - Missing/invalid auth
    - 403 - Forbidden - Valid auth but no permission
    - 404 - Not Found - Resource doesn’t exist
    - 409 - Conflict - Duplicate or state conflict
    - 429 - Too Many Requests - Rate limited
    - 500 - Internal Server Error - Server bug

### GraphQL
- A query language for APIs. Client specifies exactly what data it needs.
    ```
    # REST: multiple round trips
    GET /users/123          → { id, name, email, avatar, ... }
    GET /users/123/posts    → [{ id, title, body, ... }, ...]
    GET /users/123/friends  → [{ id, name, ... }, ...]
    
    # GraphQL: one request, exact data
    POST /graphql
    {
      user(id: 123) {
        name
        email
        posts(limit: 5) { title }
        friends { name }
      }
    }
    → Returns exactly what you asked for, nothing more
    ```

### gRPC (Google Remote Procedure Call)
- Binary protocol using Protocol Buffers. Fast, strongly typed, designed for service-to-service communication.
    ```
    // user.proto - defines the contract
    service UserService {
      rpc GetUser (GetUserRequest) returns (User);
      rpc ListUsers (ListUsersRequest) returns (stream User);
    }
    
    message GetUserRequest {
      string user_id = 1;
    }
    
    message User {
      string id = 1;
      string name = 2;
      string email = 3;
    }
    ```
  
### Pagination
1. Offset-Based (simplest):
    ```
    GET /posts?page=3&limit=20
    → Skip 40 items, return next 20
    
    Response:
    {
      "data": [...],
      "pagination": {
        "page": 3,
        "limit": 20,
        "total": 1500,
        "totalPages": 75
      }
    }
    ```
- Pros: Simple, supports “jump to page N”
- Problem: Slow for large offsets (DB must scan and skip). Items can shift if new data is inserted.
- Best For: Admin panels, small datasets

2. Cursor-Based (recommended for feeds):
    ```
    GET /posts?cursor=eyJpZCI6MTAwfQ&limit=20
    → Get 20 items after cursor position
    
    Response:
    {
      "data": [...],
      "pagination": {
        "nextCursor": "eyJpZCI6MTIwfQ",
        "hasMore": true
      }
    }
    ```
- Cursor = encoded pointer (usually base64 of last item’s ID or timestamp). Stable even if new items are added.
- Pros - Stable, consistent performance
- Cons - Can’t jump to page N
- Best For - Infinite scroll, feeds

3. Keyset-Based (most performant)
    ```
    GET /posts?after_id=100&limit=20
    → WHERE id > 100 ORDER BY id LIMIT 20
    ```
- Uses indexed column directly. O(1) regardless of offset.
- Pros - Fastest, uses index directly
- Cons - Can’t jump, needs sortable key - High-volume data, timelines

### API Versioning
- URL Path Versioning (most common):
    ```
    GET /v1/users/123
    GET /v2/users/123
    ```
    - Pros: Obvious, cacheable, easy to route
    - Cons: URL pollution

- Header Versioning:
    ```
    GET /users/123
    Accept: application/vnd.api+json; version=2
    ```
    - Clean URLs
    - Hidden, hard to test in browser

- Query Parameter Versioning:
    ```
    GET /users/123?version=2
    ```
    - Easy to add
    - Caching complications

- In interviews, use URL path versioning. It’s simplest and most widely adopted.

### API Design Best Practices for Interviews
```
1. Use nouns, not verbs:
   ✓ GET /users/123/orders
   ✗ GET /getUserOrders?id=123

2. Plural resource names:
   ✓ /users, /orders, /products
   ✗ /user, /order, /product

3. Nest for relationships (max 2 levels):
   ✓ /users/123/orders
   ✓ /orders/456/items
   ✗ /users/123/orders/456/items/789/reviews

4. Use query params for filtering:
   GET /orders?status=pending&created_after=2024-01-01

5. Return meaningful errors:
   {
     "error": {
       "code": "INSUFFICIENT_BALANCE",
       "message": "Account balance is $50, transfer requires $100",
       "field": "amount"
     }
   }

6. Always include request IDs for debugging:
   X-Request-Id: req_abc123def456
```
### When to Use Which
- Public-facing API - REST - Simple, cacheable, well-understood
- Mobile app with complex views - GraphQL - Reduce round trips, fetch exactly needed data
- Microservice-to-microservice - gRPC - Fast, typed, streaming support
- Real-time updates - WebSocket + REST - REST for CRUD, WS for push
- File uploads - REST (multipart) - Well-supported, simple
- Internal tooling - REST or gRPC - Depends on team expertise

### Real-World Examples
- GitHub - REST v3 + GraphQL v4. Moved to GraphQL for complex nested data (repos, issues, PRs, comments).
- Google - gRPC for all internal services. Public APIs are REST with gRPC transcoding.
