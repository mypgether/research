package com.gether.research.test.designpatterns.proxy.cglib;

import java.lang.reflect.Method;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

public class CglibUserFacadeProxy implements MethodInterceptor {

  private Object source;

  @Override
  public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy)
      throws Throwable {
    String methodName = method.getName();
    if (StringUtils.equals(methodName, "getUserName")) {
      before();
    }

    Object result = methodProxy.invokeSuper(o, objects);
    result = "before" + result + "after";
    if (StringUtils.equals(methodName, "getUserName")) {
      after();
    }
    return result;
  }

  public Object getProxy(Object source) {
    this.source = source;
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(this.source.getClass());
    // 设置回调方法
    enhancer.setCallback(this);
    // 创建代理对象
    return enhancer.create();
  }

  private void before() {
    System.out.println(this.getClass().getSimpleName() + " before");
  }

  private void after() {
    System.out.println(this.getClass().getSimpleName() + " after");
  }
}
