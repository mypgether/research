package com.gether.research.test.designpatterns.proxy.cglib;

import org.junit.Assert;
import org.junit.Test;

public class CglibProxyTest {

  @Test
  public void testProxy() {
    UserFacade userFacade = new UserFacade();
    UserFacade proxy = (UserFacade) new CglibUserFacadeProxy().getProxy(userFacade);

    String userName = "gether";
    Assert.assertEquals("before" + "this is " + userName + "after", proxy.getUserName(userName));
  }
}
