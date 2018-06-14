package com.gether.research.test.java8.single;

public class Singleton {

  private String name;

  private volatile static Singleton instance;

  private Singleton() {
    this.name = "name";
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public static Singleton getSingleton() {
    if (instance == null) {
      synchronized (Singleton.class) {
        if (instance == null) {
          instance = new Singleton();
        }
      }
    }
    return instance;
  }
}