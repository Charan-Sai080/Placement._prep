package tuf.basicMath;
import java.util.*;

public class printDiv
{
    static void printDivisors(int n)
    {
        for(int i =1;i<=n/2;i++)
        {
            if(n%i==0)
            {
                System.out.print(i+" ");
            }
        }
        System.out.print(n);
        return;

    }


    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        printDivisors(n);

    }
}
