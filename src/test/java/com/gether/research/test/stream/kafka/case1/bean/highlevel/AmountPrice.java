package com.gether.research.test.stream.kafka.case1.bean.highlevel;

/**
 * Created by myp on 2017/8/22.
 */
public class AmountPrice {
    private int count;
    private double price;

    public AmountPrice() {
    }

    public AmountPrice(int count, double price) {
        this.count = count;
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "AmountPrice{" +
                "count=" + count +
                ", price=" + price +
                '}';
    }
}
