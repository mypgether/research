package com.gether.research.test.java8;


import com.gether.research.jvm.producer.Consumer;
import com.gether.research.jvm.producer.Producer;
import com.gether.research.jvm.producer.ShareObjService;

/**
 * Created by myp on 2017/7/25.
 */
public class ProducerConsumerTest {

    //public static void main(String[] args) throws InterruptedException {
    //
    //    // Object on which producer and consumer thread will operate
    //    ProducerConsumerImpl sharedObject = new ProducerConsumerImpl();
    //
    //    // creating producer and consumer threads
    //    producer p = new producer(sharedObject);
    //    Consumer c = new Consumer(sharedObject);
    //
    //    // starting producer and consumer threads
    //    p.start();
    //    c.start();
    //
    //    p.join();
    //    c.join();
    //}
    public static void main(String[] args) throws InterruptedException {

        // Object on which producer and consumer thread will operate
        ShareObjService sharedObject = new ShareObjService();

        // creating producer and consumer threads
        Producer p = new Producer(sharedObject);
        Consumer c = new Consumer(sharedObject);

        // starting producer and consumer threads
        p.start();
        c.start();

        p.join();
        c.join();
    }
}