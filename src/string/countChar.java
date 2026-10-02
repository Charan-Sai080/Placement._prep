package string;
import java.util.*;


public class countChar
{
    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);


        // SIMPLE NAIVE VERSION
        String s = in.nextLine().strip();   //for any string the whitespaces arent counted as characters
                                            // so we use strip() to remove all the whitespaces
        int length= s.length();
        System.out.println(length);

        // ERASING ALL THE WHITESPACES IN THE WHOLE STRING
        String g = s.replace(" ","");
        length = g.length();
        System.out.println(length);

        // REMOVING ALL THE WHITESPACES , TABS , NEWLINE USING replaceAll() & REGEX "\\s+"

        String e = s.replaceAll("\\s+","");
        length= e.length();
        System.out.println(length);
    }
}
