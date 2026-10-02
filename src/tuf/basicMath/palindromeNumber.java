package tuf.basicMath;
import java.util.*;

public class palindromeNumber
{
    static Boolean palindrome(int n)
    {
        int ori = n;
        int rev = n%10;
        n/=10;
        while(n!=0)
        {
            rev=rev*10+n%10;
            n/=10;
        }

        return ori == rev;
    }


    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();


        System.out.println(palindrome(n));

    }
}
