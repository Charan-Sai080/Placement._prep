package string;
import java.util.*;


public class reverse
{
   public static void main(String[] args)
   {
       Scanner in = new Scanner(System.in);
       String s = in.nextLine().strip();

       //manual reversing
       int length = s.length()-1;

       while(length>=0)
       {
           System.out.print(s.charAt(length));
           length--;

       }
       System.out.println();

       //using StringBuilder

       String sr = new StringBuilder(s).reverse().toString();
       System.out.println(sr);

   }

}
