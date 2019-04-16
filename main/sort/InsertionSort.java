/*
Date: 04/16,2019, 16:07
插入排序
时间复杂度： O(n^2)
空间复杂度： O(1)
稳定
*/
package sort;

public class InsertionSort {
    private static void insertionSort(int[] in) {
        for (int i =1; i< in.length;i++){
            for (int j = i; j > 0 ;j--){
                if (in[j]<in[j-1])
                    swap(in,j,j-1);
            }
        }
    }

    private static void swap(int[] in, int i, int j) {
        int t = in[i];
        in[i] = in[j];
        in[j] = t;
    }

    public static void main(String[] args) {
        insertionSort(new int[]{1, 5, 8, 3, 4, 6, 7, 2, 5});
    }
}
