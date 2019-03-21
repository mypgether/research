package com.gether.research.test.ms;

import org.junit.After;
import org.junit.Test;

/**
 * @author myp
 * @date 2019/3/3 下午10:59
 */
public class QuickSortTest {

  /**
   * 快速排序方法
   */
  public static int[] QuickSort(int[] array, int start, int end) {
    if (array.length < 1 || start < 0 || end >= array.length || start > end) {
      return null;
    }
    int smallIndex = partition(array, start, end);
    if (smallIndex > start) {
      QuickSort(array, start, smallIndex - 1);
    }
    if (smallIndex < end) {
      QuickSort(array, smallIndex + 1, end);
    }
    return array;
  }

  /**
   * 快速排序算法——partition
   */
  public static int partition(int[] array, int start, int end) {
    int pivot = (int) (start + Math.random() * (end - start + 1));
    int smallIndex = start - 1;
    swap(array, pivot, end);
    for (int i = start; i <= end; i++) {
      if (array[i] <= array[end]) {
        smallIndex++;
        if (i > smallIndex) {
          swap(array, i, smallIndex);
        }
      }
    }
    return smallIndex;
  }

  /**
   * 交换数组内两个元素
   */
  public static void swap(int[] array, int i, int j) {
    int temp = array[i];
    array[i] = array[j];
    array[j] = temp;
  }

  int[] arrays = new int[]{10, 2, 3, 123, 5787, 3, 1, 2, 4, 0, -1323, 81273};

  @After
  public void after() {
    print();
  }

  private void print() {
    for (int i = 0; i < arrays.length; i++) {
      System.out.print(arrays[i] + " ");
    }
    System.out.println();
  }

  @Test
  public void quicksort() {
    QuickSort(arrays, 0, arrays.length - 1);
  }
}
