# Get last set bit:
- n = 18 (in binary:             10010)
- n-1 = 17 (in binary:           10001)
- n & (n-1) = 10010 & 10001 =    10000 (in decimal: 16)
- (n & (n-1)) ^ n =10000^10010 = 00010 (in decimal: 2)

**OR**
- n = 18 (in binary:       10010)
- -n = -18 (in binary:     01110) (two's complement)
- n & -n = 10010 & 01110 = 00010 (in decimal: 2)

 ```java
public class LastSetBit {
    public static void main (String[] args) {
        int n = 18;
        int lastSetBit = (n & (n - 1)) ^ n;
        System.out.println("Last set bit of " + n + " is: " + lastSetBit);
    }
}
 ```

