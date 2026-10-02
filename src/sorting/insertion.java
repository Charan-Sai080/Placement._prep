package sorting;
import java.util.*;

public class insertion
{
    static String insertionSort(int[] arr)
    {
        int n = arr.length;

        for(int i = 1 ; i<n;i++)
        {
            int key = arr[i];
            int j = i-1;
            while(j>=0 && arr[j]>key)
            {
                arr[j+1] = arr[j];
                j--;
            }
            arr[j+1]=key;

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

        System.out.println(insertionSort(arr));
    }
}
