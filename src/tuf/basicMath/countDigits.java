package tuf.basicMath;
import java.util.*;

public class countDigits
{
    static int BruteCountDigits(int n)
    {
        int count=0;
        while(n!=0)
        {
            n/=10;
            count++;
        }
        return count;
    }

    static int OptimalCountDigits(int n)
    {
        int count =(int) Math.log10(n)+1;
        return count;
    }

    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
        int n =in.nextInt();
        if(n==0)
            return;

        System.out.println(BruteCountDigits(n));
        System.out.println(OptimalCountDigits(n));
    }
}
