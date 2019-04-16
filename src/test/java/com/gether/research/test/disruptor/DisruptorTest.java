package com.gether.research.test.disruptor;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.IgnoreExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author myp
 * @date 2018/8/20 上午9:51
 */
public class DisruptorTest {

  protected final Logger log = LoggerFactory.getLogger(getClass());

  @Test
  public void testWithMultiConsumer() throws InterruptedException {
    AtomicLong threadIndex = new AtomicLong(0);
    ExecutorService executor = Executors
        .newFixedThreadPool(20,
            r -> new Thread(r, "disruptor-thread" + threadIndex.incrementAndGet()));

    AtomicLong inc = new AtomicLong(0);

    Disruptor<MsgSendDto> disruptor = new Disruptor<>(() -> {
      MsgSendDto event = new MsgSendDto();
      return event;
    }, 2, executor, ProducerType.MULTI,
        new YieldingWaitStrategy());
//    disruptor.handleEventsWith(new MyHandler("first"), new MyHandler("second"));
    disruptor.handleEventsWith(new MyHandler("first"));
    disruptor.setDefaultExceptionHandler(new IgnoreExceptionHandler());
    disruptor.start();

    for (int i = 0; i < 1 * 100; i++) {
      RingBuffer<MsgSendDto> ringBuffer = disruptor.getRingBuffer();
      long sequence = ringBuffer.next();
      try {
        MsgSendDto event = ringBuffer.get(sequence);
        event.setCount(inc.incrementAndGet());
      } finally {
        ringBuffer.publish(sequence);
      }
    }
    Thread.sleep(10000);
  }

  @Test
  public void testWithSameConsumer() throws InterruptedException {
    AtomicLong threadIndex = new AtomicLong(0);
    ExecutorService executor = Executors
        .newFixedThreadPool(20,
            r -> new Thread(r, "disruptor-thread" + threadIndex.incrementAndGet()));

    AtomicLong inc = new AtomicLong(0);

    Disruptor<MsgSendDto> disruptor = new Disruptor<>(() -> {
      MsgSendDto event = new MsgSendDto();
      return event;
    }, 2, executor, ProducerType.MULTI,
        new YieldingWaitStrategy());
    disruptor.handleEventsWithWorkerPool(new MyWorkHandler("first"), new MyWorkHandler("second"));
    disruptor.setDefaultExceptionHandler(new IgnoreExceptionHandler());
    disruptor.start();

    for (int i = 0; i < 1 * 100; i++) {
      RingBuffer<MsgSendDto> ringBuffer = disruptor.getRingBuffer();
      long sequence = ringBuffer.next();
      try {
        MsgSendDto event = ringBuffer.get(sequence);
        event.setCount(inc.incrementAndGet());
      } finally {
        ringBuffer.publish(sequence);
      }
    }
    Thread.sleep(10000);
  }

  public class MyHandler implements EventHandler<MsgSendDto> {

    private String handlerName;

    public MyHandler(String handlerName) {
      this.handlerName = handlerName;
    }


    @Override
    public void onEvent(MsgSendDto event, long sequence, boolean endOfBatch) throws Exception {
      Long id = Thread.currentThread().getId();
      String name = Thread.currentThread().getName();
      log.info("{} threadid: {} ,thread name: {} ,objContent: {} ,endOfBatch: {}", handlerName, id,
          name, event.getCount(), endOfBatch);
    }
  }

  public class MyWorkHandler implements WorkHandler<MsgSendDto> {

    private String handlerName;

    public MyWorkHandler(String handlerName) {
      this.handlerName = handlerName;
    }

    @Override
    public void onEvent(MsgSendDto event) throws Exception {
      Long id = Thread.currentThread().getId();
      String name = Thread.currentThread().getName();
      log.info("{} threadid: {} ,thread name: {} ,objContent: {}", handlerName, id, name,
          event.getCount());
    }
  }

  public class MsgSendDto implements Serializable {

    private long count;

    public long getCount() {
      return count;
    }

    public void setCount(long count) {
      this.count = count;
    }
  }
}