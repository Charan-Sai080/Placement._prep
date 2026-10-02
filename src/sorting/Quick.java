package sorting;
import java.util.*;

public class Quick
{
    static void quicksort(int[] a, int l , int h)
    {

    }


    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();

        int[] arr = new int[n];
        for(int i =0 ; i<n;i++)
        {
            arr[i]= in.nextInt();
        }

        quicksort(arr,0,arr.length-1);

        System.out.println(Arrays.toString(arr));
    }
}
