/*
Date: 04/16,2019, 19:35
堆排序
时间复杂度： O(nlogn)
空间复杂度： O(1)
不稳定
*/
package sort;

public class HeapSort {
    public static void heapSort(int[] in) {
        for (int i = in.length / 2; i >= 0; i--) {
            heapAdjuct(in, i, in.length - 1);
        }

        for (int i = in.length - 1; i >= 0; i--) {
            swap(in, i, 0);
            heapAdjuct(in, 0, i-1);
        }
    }

    public static void heapAdjuct(int[] in, int index, int end) {
        while (index * 2 + 1 <= end) {
            int child = index * 2 + 1;
            if (child + 1 <= end && in[child + 1] > in[child]) {
                child++;
            }
            if (in[child] > in[index]) {
                swap(in, child, index);
                index = child;
            } else {
                break;
            }
        }
    }

    public static void main(String[] args){
        int[] ints = {1, 5, 8, 3, 4, 6, 7, 2, 5};
        heapSort(ints);
    }
    private static void swap(int[] in, int i, int j) {
        int t = in[i];
        in[i] = in[j];
        in[j] = t;
    }
}
