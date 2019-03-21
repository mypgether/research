package com.gether.research.test.java8.jmm;

import com.gether.research.test.spring.aop.cglib.UserFacade;
import net.sf.cglib.proxy.Dispatcher;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

public class OomTest {

  public static void main(String[] args) {
    for (int i = 0; i < Integer.MAX_VALUE; i++) {
      Enhancer enhancer = new Enhancer();
      enhancer.setSuperclass(UserFacade.class);
      enhancer.setCallbackTypes(new Class[]{Dispatcher.class, MethodInterceptor.class});
      enhancer.setCallbackFilter(method -> 1);
      Class clazz = enhancer.createClass();
    }
  }
}