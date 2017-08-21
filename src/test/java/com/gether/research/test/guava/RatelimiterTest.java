package com.gether.research.test.guava;

import com.google.common.util.concurrent.RateLimiter;
import org.junit.Test;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by myp on 2017/6/28.
 */
public class RatelimiterTest {

    @Test
    public void test() {
        RateLimiter limiter = RateLimiter.create(100);
        AtomicInteger index = new AtomicInteger(0);
        ForkJoinPool fjPool = new ForkJoinPool(100);
        fjPool.submit(new NeverStopAction(limiter, index));
        while (true) {

        }
    }

    public class NeverStopAction extends RecursiveAction {
        RateLimiter limiter;
        AtomicInteger index;

        public NeverStopAction(RateLimiter limiter, AtomicInteger index) {
            this.limiter = limiter;
            this.index = index;
        }

        @Override
        protected void compute() {
            limiter.acquire();
            System.out.println(System.currentTimeMillis() + " get access i:" + index.incrementAndGet());
            invokeAll(new NeverStopAction(limiter, index));
        }
    }
}