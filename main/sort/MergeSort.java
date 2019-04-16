/*
Date: 04/16,2019, 19:19
归并排序
时间复杂度： O(nlogn)
空间复杂度：O(n)
稳定
*/
package sort;

public class MergeSort {
    public static void mergeSort(int[] in, int l, int r) {
        if (l >= r)
            return;

        int mid = l + (r - l) / 2;
        mergeSort(in, l, mid);
        mergeSort(in, mid + 1, r);

        mergeHelper(in, l, mid, r);
    }

    public static void mergeHelper(int[] in, int l, int mid, int r) {
        int[] temp = new int[r - l + 1];
        // 左半边边界
        int l1 = l, r1 = mid;
        // 右半边边界
        int l2 = mid + 1, r2 = r;

        for (int i = 0; i < temp.length; i++) {
            if (l1 <= r1 && l2 <= r2) {
                if (in[l1] <= in[l2]) {
                    temp[i] = in[l1++];
                } else {
                    temp[i] = in[l2++];
                }
            } else if (l2 > r2) {
                temp[i] = in[l1++];
            } else {
                temp[i] = in[l2++];
            }
        }

        System.arraycopy(temp, 0, in, l, temp.length);
    }

    public static void main(String[] args) {
        int[] ints = {1, 5, 8, 3, 4, 6, 7, 2, 5};
        mergeSort(ints, 0, ints.length - 1);
    }
}
