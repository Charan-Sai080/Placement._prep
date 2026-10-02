package string;
import java.util.*;
public class convertCase
{
    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
        String s = in.nextLine().strip();

        // covert to lowercase using + 32 and - 32 trick first caps then small;
        // also we can use Character.toLowercase()
        StringBuilder sb = new StringBuilder();

        for(char c : s.toCharArray())
        {
            if (c >= 'A' && c <= 'Z')
                c = (char)(c + 32); // or c = Character.toLowerCase(c);
            sb.append(c);
        }
        System.out.println(sb);


        StringBuilder sc = new StringBuilder();

        for(char c : s.toCharArray())
        {
            if (c >= 'a' && c <= 'z')
                c = (char)(c - 32); // or c = Character.toUpperCase(c);
            sc.append(c);
        }
        System.out.println(sc);


    }
}
