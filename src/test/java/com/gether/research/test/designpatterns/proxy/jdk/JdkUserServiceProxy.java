package com.gether.research.test.designpatterns.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.apache.commons.lang3.StringUtils;

public class JdkUserServiceProxy implements InvocationHandler {

  private Object source;

  public JdkUserServiceProxy(Object source) {
    this.source = source;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    String methodName = method.getName();
    if (StringUtils.equals(methodName, "getUserName")) {
      before();
    }
    Object result = method.invoke(source, args);
    result = "before" + result + "after";
    if (StringUtils.equals(methodName, "getUserName")) {
      after();
    }
    return result;
  }

  public Object getProxy() {
    return Proxy
        .newProxyInstance(getClass().getClassLoader(), source.getClass().getInterfaces(), this);
  }

  private void before() {
    System.out.println(this.getClass().getSimpleName() + " before getUserName");
  }

  private void after() {
    System.out.println(this.getClass().getSimpleName() + " after getUserName");
  }
}