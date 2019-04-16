/*
Date: 04/16,2019, 16:29
快排
时间复杂度  O(nlogn)
空间复杂度  O(logn)
不稳定
*/
package sort;

public class QuickSout {
    public static void quickSort(int[] in, int l, int r) {
        if (l >= r) return;
        int begin = l, end = r;
        int key = in[r];
        while (l < r) {
            while (l < r && in[l] <= key) l++;
            in[r] = in[l];
            while (l < r && in[r] >= key) r--;
            in[l] = in[r];
        }
        in[l] = key;
        quickSort(in, begin, l - 1);
        quickSort(in, l + 1, end);
    }

    public static void main(String[] args) {
        int[] ints = {1, 5, 8, 3, 4, 6, 7, 2, 5};
        quickSort(ints, 0, ints.length - 1);
    }

    private static void swap(int[] in, int i, int j) {
        int t = in[i];
        in[i] = in[j];
        in[j] = t;
    }

}
