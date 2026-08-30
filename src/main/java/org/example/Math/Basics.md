### Check if number is prime:

- why until sqrt(n)? Because if n is not prime, it can be factored into two factors a and b: n = a * b. If both a and b
  were greater than the square root of n, then a * b would be greater than n. Therefore, at least one of those factors
  must be less than or equal to the square root of n, and we only need to check for factors up to that point.
- Example for explanation let's take 48
    - sqrt(48) = 6.9282
    - Factors of 48: 1, 2, 3, 4, 6, 8, 12, 16, 24, 48
    - Factors less than or equal to sqrt(48): 1, 2, 3, 4, 6
    - Factors greater than sqrt(48): 8, 12, 16, 24, 48

```java
public boolean isPrime(int n) {
    if (n <= 1) return false; // 0 and 1 are not prime numbers
    for (int i = 2; i <= Math.sqrt(n); i++) {
        if (n % i == 0) return false; // Found a divisor, not prime
    }
    return true; // No divisors found, number is prime
}
```

### Prime Factorization:

- We don't have to check for every prime from beginning because once a number is divided by a prime, it will not be
  divisible by that prime and multiples of it again. So we can keep dividing the number by the same prime until it is no
  longer divisible, and then move on to the next number and this number if it's not prime it will not divide the
  remaining n because it will be a multiple of the prime we already divided by.
- So, ideally the number will be only divided by prime numbers.

```java
public void primeFactorization(int n) {
    for (int i = 2; i <= Math.sqrt(n); i++) {
        while (n % i == 0) {
            System.out.print(i + " ");
            n /= i;
        }
    }
    if (n > 1) {
        System.out.print(n); // If n is a prime number greater than 1
    }
}
`````

### Calculate power

- The idea is to use the property of exponents: a^b = a^(b/2) * a^(b/2) if b is even, and a^b = a * a^(b-1) if b is odd.
- TC: O(log b) because we are dividing the exponent by 2 in each recursive call.

```java
class Solution {
  public double power(double x, int n) {
    double ans = 1.0;
    long nn = Math.abs((long) n); // Safely convert to long to prevent Integer.MIN_VALUE overflow
    while (nn > 0) {
      if (nn % 2 != 0) {
        ans = ans * x; // If the current bit is 1 (odd number), multiply ans by the current base
      }
      x = x * x; // Square the base and divide exponent by 2 on every iteration
      nn = nn / 2;
    }
    // Invert the result if the original exponent was negative
    return (n < 0) ? 1.0 / ans : ans;
  }
}

```