package string;
import java.util.*;

public class countDigits
{
    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
        String s = in.nextLine().strip();

        int count=0;

        // using loop conditionals
        for(char c : s.toCharArray())
        {
            if(c >= '0' && c<='9')
            {
                count++;
            }
        }
        System.out.println(count);

        int count1=0;
        // using isDigit()
        for(char c : s.toCharArray())
        {
            if(Character.isDigit(c))
            {
                count1++;
            }
        }
        System.out.println(count1);
    }
}
