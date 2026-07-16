``# Check if number is prime:

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

# Prime Factorization:

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