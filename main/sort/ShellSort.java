/*
Date: 04/16,2019, 16:18

shell排序： 对插入排序的改进
时间复杂度: O(nlogn)
空间复杂度: O(1)
不稳定
*/
package sort;

public class ShellSort {
    private static void shellSort(int[] in) {
        int n = in.length;
        while (n > 1) {
            n = n / 3 + 1;
            for (int i = n; i < in.length; i = i + n)
                for (int j = i; j - n >= 0; j = j - n) {
                    if (in[j - n] > in[j])
                        swap(in, j, j - n);
                }
        }
    }

    private static void swap(int[] in, int i, int j) {
        int t = in[i];
        in[i] = in[j];
        in[j] = t;
    }

    public static void main(String[] args) {
        shellSort(new int[]{1, 5, 8, 3, 4, 6, 7, 2, 5});
    }
}
