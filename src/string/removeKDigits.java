package string;
import java.util.*;
/*9. Remove K Digits (LeetCode 402)
Problem:
Given a string num representing a non-negative integer and an integer k, return the smallest possible
integer after removing exactly k digits from num.

Examples:

Input	Output	Explanation
num = "1432219", k = 3	"1219"	Remove 4, 3, 2 → "1219"
num = "10200", k = 1	"200"	Remove 1 → "200"
num = "10", k = 2	"0"	Remove both digits

Constraints:

1 <= k <= num.length <= 10^5
num consists of only digits.
num does not contain any leading zeros except for the zero itself.
Java Solution (Monotonic Stack):

class Solution {
    public String removeKdigits(String num, int k) {
        if (num.length() == k) return "0";

        StringBuilder stack = new StringBuilder();

        for (char c : num.toCharArray()) {
            // While we can still remove, and top is GREATER than current → pop
            while (k > 0 && stack.length() > 0 && stack.charAt(stack.length() - 1) > c) {
                stack.deleteCharAt(stack.length() - 1);
                k--;
            }
            stack.append(c);
        }

        // If k > 0, digits were in ascending order → remove from the end
        while (k > 0) {
            stack.deleteCharAt(stack.length() - 1);
            k--;
        }

        // Strip leading zeros
        int i = 0;
        while (i < stack.length() && stack.charAt(i) == '0') i++;

        String result = stack.substring(i);
        return result.isEmpty() ? "0" : result;
    }
}

Time: O(n) | Space: O(n)

Key intuition: To minimize the number, you want smaller digits as early (left) as possible.
So whenever you see a digit smaller than the one on top of the stack, you "remove" the larger one (greedy).
If the number is already in ascending order ("12345"), you can only remove from the right end.
 */
public class removeKDigits
{
    static String removeKDigi(String s, int k )
    {

    }
    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);

    }
}
