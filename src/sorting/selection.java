package sorting;
import java.util.*;

public class selection
{
    static String selectionSort(int[] arr)
    {
        int n = arr.length;

        for(int i = 0 ; i<n-1 ; i++)
        {
            int minidx = i;
            for(int j =i+1 ;j<n;j++)
            {
                if(arr[minidx]>arr[j])
                {
                    minidx = j;
                }
            }
            int temp = arr[minidx];
            arr[minidx]=arr[i];
            arr[i]=temp;
        }
        return Arrays.toString(arr);
    }


    public static void main(String[] args)
    {

        Scanner in = new Scanner(System.in);

        int n = in.nextInt();

        int[] arr = new int[n];

        for(int i=0;i<n;i++)
        {
            arr[i]=in.nextInt();
        }

        System.out.println(selectionSort(arr));
    }
}
