package com.gether.research.test.ms;

import java.util.Arrays;
import org.junit.Test;

/**
 * @author myp
 * @date 2019/3/4 上午10:36
 */
public class Qsort {

  @Test
  public void test() {
    int[] array = new int[]{-1, 123, 4, -391, 2, 123, 30, 19238};
    qs(array, 0, array.length - 1);
    System.out.println(Arrays.toString(array));
  }

  private int findMiddle(int[] array, int start, int end) {
    int temp = array[start];
    while (start < end) {
      while (start < end && temp <= array[end]) {
        end--;
      }
      array[start] = array[end];
      while (start < end && temp > array[start]) {
        start++;
      }
      array[end] = array[start];
    }
    array[start] = temp;
    return start;
  }

  public void qs(int[] array, int start, int end) {
    if (start < end) {
      int middle = findMiddle(array, start, end);
      qs(array, start, middle - 1);
      qs(array, middle + 1, end);
    }
  }
}