package com.gether.research.test.ms;

import java.util.Arrays;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author myp
 * @date 2019/3/3 下午10:17
 */
public class SortTest {

  int[] arrays = new int[]{10, 2, 3, 123, 5787, 3, 1, 2, 4, 0, -1323, 81273};

  long start;

  @Before
  public void before() {
    start = System.currentTimeMillis();
  }

  @Test
  public void sort() {
    Arrays.sort(arrays);
  }

  @Test
  public void maopaosort() {
    for (int i = 0; i < arrays.length; i++) {
      for (int j = i + 1; j < arrays.length; j++) {
        if (arrays[j] < arrays[i]) {
          int temp = arrays[i];
          arrays[i] = arrays[j];
          arrays[j] = temp;
        }
      }
    }
  }

  /**
   * -1323 0 1 2 2 3 3 4 10 123 5787 81273
   */
  @Test
  public void quicksort() {
    quickSortT(arrays, 0, arrays.length - 1);
  }

  private void quickSortT(int[] arr, int start, int end) {
    if (start < end) {
      int midd = getMiddle(arr, start, end);
      quickSortT(arr, start, midd - 1);
      quickSortT(arr, midd + 1, end);
    }
  }

  private int getMiddle(int[] array, int start, int end) {
    int temp = array[start];
    while (start < end) {
      // 把小的移动到左边
      while (start < end && array[end] >= temp) {
        end--;
      }
      array[start] = array[end];
      // 把大的移动到右边
      while (start < end && array[start] < temp) {
        start++;
      }
      array[end] = array[start];
    }
    array[start] = temp;
    return start;
  }

  @After
  public void after() {
    System.out.println("cost times: " + (System.currentTimeMillis() - start));
    print();
  }

  private void print() {
    for (int i = 0; i < arrays.length; i++) {
      System.out.print(arrays[i] + " ");
    }
    System.out.println();
  }
}
