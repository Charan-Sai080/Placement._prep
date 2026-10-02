package sorting;
import java.util.*;

public class bubble
{
    static String bubbleSort(int[] arr)
    {
        int n = arr.length;
        for(int i =0; i<n-1 ; i++)
        {
            boolean swapped = false;
            for(int j = 0 ; j<n-i-1;j++)
            {
                if(arr[j]>arr[j+1])
                {
                    swapped = true;
                    int temp = arr[j+1];
                    arr[j+1]=arr[j];
                    arr[j]=temp;
                }
            }
            if(!swapped) break;
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

        System.out.println(bubbleSort(arr));
    }
}
