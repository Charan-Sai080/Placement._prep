package tuf.basicMath;
import java.util.*;

public class reverseNumer
{
    static int reverse(int n)
    {
        int rev = 0;

        rev=n%10;
        n/=10;
        while(n!=0)
        {
            rev=rev*10+n%10;
            n/=10;
        }
        return rev;
    }

    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        System.out.println(reverse(n));

    }
}
