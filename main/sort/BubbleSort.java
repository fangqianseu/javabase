/*
Date: 04/16,2019, 15:31
冒泡排序
时间复杂度： O(n^2)
空间复杂度：O(1)
稳定排序
*/
package sort;

public class BubbleSort {
    public static void bubbleSort(int[] in) {
        for (int i = in.length - 1; i >= 1; i--) {
            boolean isSwap = false;
            for (int j = 0; j < i; j++) {
                if (in[j] > in[j + 1]) {
                    swap(in, j, j + 1);
                    isSwap = true;
                }
            }
            if (!isSwap) break;
        }
    }

    private static void swap(int[] in, int i, int j) {
        int t = in[i];
        in[i] = in[j];
        in[j] = t;
    }

    public static void main(String[] args) {
        int[] ints = {1, 5, 8, 3, 4, 6, 7, 2, 5};
        bubbleSort(ints);
    }


}
