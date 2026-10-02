package string;
import java.util.*;

public class countVowels
{
    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);

        String s = in.nextLine().strip();
        int length= s.length();
        int count = 0;

        //using for loop conditionals
        for (int i = 0 ; i<=length-1;i++)
        {
            char c = s.charAt(i);
            if(c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U' ||
               c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u')
            {
                count++;
            }
        }
        System.out.println(count);

        // USING FOR EACH LOOP

        int count1=0;

        char[] chs= s.toCharArray();

        for(char c : chs )
        {
            if("aeiouAEIOU".indexOf(c)!=-1)
            {
                count1++;
            }
        }
        System.out.println(count1);
    }
}
