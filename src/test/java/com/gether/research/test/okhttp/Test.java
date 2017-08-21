package com.gether.research.test.okhttp;

public class Test implements Cloneable{
    private int foo;

    public Test(int foo) {
        this.foo = foo;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public int getFoo() {
        return foo;
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        Test test = new Test(1);
        Test cloned = (Test) test.clone();
        System.out.println(cloned.getFoo());
    }
}