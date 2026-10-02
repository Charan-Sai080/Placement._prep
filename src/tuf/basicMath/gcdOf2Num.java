package tuf.basicMath;
import java.util.*;

public class gcdOf2Num
{
    static int gcd(int a,int b)
    {
       while(b!=0)
       {
           int temp = b;
           b=a%b;
           a=temp;
       }
       return a;
    }


    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
        int a = in.nextInt();
        int b = in.nextInt();

        System.out.println(gcd(a,b));
    }
}
