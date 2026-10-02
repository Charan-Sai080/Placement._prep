package tuf.basicMath;
import java.util.*;



public class armstrongNumber
{
    static boolean isArmstrong(char[] chars, int n)
    {

        int arm = 0;
        for(int i = 0;i< chars.length;i++)
        {
            int digit =  chars[i]-'0';
            int pow=1;
            for(int j =0;j<chars.length;j++)
            {
                pow*=digit;
            }
            arm+=pow;
        }


        return n == arm;
    }


    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
        String s = in.nextLine().strip();
        int n = Integer.parseInt(s);
        char[] chars= s.toCharArray();

        System.out.println(isArmstrong(chars,n));

    }
}
