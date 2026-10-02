package sorting;
import java.util.*;

public class mergesort
{
    static void mergeSort(int[] a,int l , int h)
    {
        if(l<h)
        {
            int m = l+ (h-l)/2;
            mergeSort(a,l,m);
            mergeSort(a,m+1,h);
            merge(a,l,m,h);
        }

    }

    static void merge(int[] a , int l , int m , int h)
    {
        int i=l , j=m+1 , p=0;
        int n = h-l + 1;
        int[] k = new int[n];

        while(i<=m && j <= h)
        {
            if(a[i]<=a[j])
            {
                k[p++]=a[i++];
            }
            else
            {
                k[p++]=a[j++];
            }
        }
        while(i<=m)
        {
            k[p++]=a[i++];
        }
        while(j<=h)
        {
            k[p++]=a[j++];
        }

        for(int z=0;z<n;z++)
        {
            a[l++]=k[z];
        }
    }


    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[] arr = new int[n];

        for(int i = 0 ; i<n; i++)
        {
            arr[i]=in.nextInt();
        }

        mergeSort(arr,0,arr.length-1);
        System.out.println(Arrays.toString(arr));
    }
}
