package string;
import java.util.*;

//Remove All Adjacent Duplicates In String

/*Edge Cases / Issues Not Covered
null input — Only s.isEmpty() is checked.  A null argument throws a NullPointerException before the loop ever runs.
O(n²) time complexity — StringBuilder.deleteCharAt(stack.length()-1) is not O(1); it shifts all subsequent characters left,
making each pop O(k) where k is the current stack size. For a worst-case input like "aaaa…a" (all same char), the total
cost becomes O(n²) instead of the expected O(n). A real char[] stack with a pointer would fix this.
Unicode surrogate pairs — char is 16-bit, so supplementary code points (emoji, CJK Extension B, etc.) are split into two char values.
Two identical emoji would be compared as two separate surrogate halves rather than one logical character, producing incorrect results.
No whitespace / character-class filtering — All characters (spaces, newlines, tabs) are treated as removable duplicates.
If the intended problem only targets alphanumeric or specific character classes, this is a gap.

 */

public class removeDummy
{
    static String removeDummy(String s)
    {
        if(s.isEmpty()) return "";
        StringBuilder stack = new StringBuilder();
        char[] chars= s.toCharArray();
        stack.append(chars[0]);
        for(int i=1; i<chars.length;i++)
        {
            char current = chars[i];
            if(stack.length() > 0 && stack.charAt(stack.length()-1) == current)
            {
                stack.deleteCharAt(stack.length()-1);//pop
            }
            else
            {
                stack.append(current);
            }
        }
        return stack.toString();
    }

    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();

        System.out.println(removeDummy(s));

    }
}
