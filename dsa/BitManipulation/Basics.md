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
## Check if string is palindrome using bit manipulation
- Using xor will eliminate the even count chars and leave the odd count char in the end.
- If the final value has at most one bit set, then the string is palindrome.
- n & (n-1) will be 0 if there is at most one bit set in n. (n-1 will have all the bits set to the right of the rightmost set bit in n, so if there is only one bit set in n, then n & (n-1) will be 0) i.e 8 = 1000, 7 = 0111, 8 & 7 = 0
```java
public class PalindromeCheck {
    public static void main(String[] args) {
        String str = "racecar";
        int bitVector = 0;

        for (char c : str.toCharArray()) {
            int charIndex = c - 'a'; // Assuming only lowercase letters
            bitVector ^= (1 << charIndex); // Toggle the bit at charIndex
        }

        // Check if at most one bit is set in bitVector
        boolean isPalindrome = (bitVector & (bitVector - 1)) == 0;
        System.out.println("Is the string \"" + str + "\" a palindrome? " + isPalindrome);
    }
}
```

