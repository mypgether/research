package com.gether.research.test.designpatterns.proxy.jdk;

import com.gether.research.service.UserService;
import com.gether.research.service.impl.UserServiceImpl;
import org.junit.Assert;
import org.junit.Test;

public class JdkProxyTest {

  @Test
  public void test() {
    UserService obj = new UserServiceImpl();

    String userName = "gether";
    UserService proxy = (UserService) new JdkUserServiceProxy(obj).getProxy();
    Assert.assertEquals("before" + "this is " + userName + "after", proxy.getUserName(userName));
  }
}