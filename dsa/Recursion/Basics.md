### Generating subsets
- The idea is to use recursion to generate all possible subsets of the given array. 
- For each element, we have two choices: either include it in the current subset or exclude it. 
- We can represent this decision-making process using a recursive function that explores both possibilities for each element.

```java
public class Subsets {
    private static void generateSubsets(int[] arr, int index, List<Integer> current, List<List<Integer>> result) {
        if (index == arr.length) {
            result.add(new ArrayList<>(current)); // Add a copy of the current subset to the result
            return;
        }
        // Include the current element
        current.add(arr[index]);
        generateSubsets(arr, index + 1, current, result);
        // Exclude the current element (backtrack)
        current.remove(current.size() - 1);
        generateSubsets(arr, index + 1, current, result);
    }
    
    public static void main(String[] args) {
        int[] arr = {1, 2, 3};
        List<List<Integer>> result = new ArrayList<>();
        generateSubsets(arr, 0, new ArrayList<>(), result);
        System.out.println(result);
    }
}
```