package string;
import java.util.*;

public class removeChar
{
    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        // Removing duplicate chars

        boolean[] seen = new boolean[256];
        StringBuilder sb = new StringBuilder();

        for(char c : s.toCharArray())
        {
            if(!seen[c])
            {
                seen[c] = true;
                sb.append(c);
            }
        }
        System.out.println(sb);

    }
}
