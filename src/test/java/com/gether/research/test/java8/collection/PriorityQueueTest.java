package com.gether.research.test.java8.collection;

import com.google.common.collect.Lists;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import org.junit.Test;

/**
 * @author myp
 * @date 2019/3/11 下午1:59
 */
public class PriorityQueueTest {

  public static class URLInfo {

    private int count;
    private String url;

    public URLInfo(int count, String url) {
      this.count = count;
      this.url = url;
    }

    public int getCount() {
      return count;
    }

    public void setCount(int count) {
      this.count = count;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }

    @Override
    public String toString() {
      return "URLInfo{" +
          "count=" + count +
          ", url='" + url + '\'' +
          '}';
    }
  }

  @Test
  // 队列中添加数据
  public void testAdd() {
    Queue priorityQueue = new PriorityQueue(5);
    int num = 10;
    Random random = new Random();
    for (int i = 0; i < num; i++) {
      priorityQueue.add(random.nextInt(11000));
    }
    printQueue(priorityQueue);
  }

  @Test
  // 获取出现次数最大的10个url
  public void getTop10Url() {
    int maxUrl = 1000;
    int num = 10;

    List<URLInfo> urlInfoList = Lists.newArrayList();
    for (int i = 0; i < maxUrl; i++) {
      urlInfoList.add(new URLInfo(i + 1, "http://www.gether.me?index=" + i));
    }

    Queue<URLInfo> priorityQueue = new PriorityQueue(num,
        Comparator.comparingInt(URLInfo::getCount));
    for (int i = 0; i < urlInfoList.size(); i++) {
      if (i < num) {
        priorityQueue.add(urlInfoList.get(i));
      } else if (priorityQueue.peek().getCount() < urlInfoList.get(i).getCount()) {
        // remove first
        priorityQueue.poll();
        priorityQueue.add(urlInfoList.get(i));
      }
    }

    printQueue(priorityQueue);
  }

  private void printQueue(Queue priorityQueue) {
    while (true) {
      Object a = priorityQueue.poll();
      if (null == a) {
        break;
      }
      System.out.println(String.valueOf(a));
    }
  }
}
