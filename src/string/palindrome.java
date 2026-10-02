package string;
import java.util.*;
public class palindrome
{
    static Boolean palindrome(String s)
    {
        int left = 0;
        int right = s.length()-1;
        while(left<right)
        {
            if(s.charAt(left)==s.charAt(right))
            {
                left++;
                right--;
            }
            else
            {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();

        System.out.println(palindrome(s));
    }
}
