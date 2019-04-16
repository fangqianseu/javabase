/*
Date: 04/16,2019, 16:00

选择排序
时间复杂度: O(n^2)
空间复杂度: O(1)
不稳定
*/
package sort;

public class SelectionSort {
    public static void selectionSort(int[] in) {
        for (int i = in.length - 1; i > 0; i--) {
            int max = 0;
            for (int j = 1; j <= i; j++) {
                if (in[j] > in[max])
                    max = j;
            }
            swap(in, max, i);
        }
    }

    private static void swap(int[] in, int i, int j) {
        int t = in[i];
        in[i] = in[j];
        in[j] = t;
    }

    public static void main(String[] args){
        selectionSort(new int[]{1, 5, 8, 3, 4, 6, 7, 2, 5});
    }
}
