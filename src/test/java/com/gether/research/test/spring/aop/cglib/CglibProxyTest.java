package com.gether.research.test.spring.aop.cglib;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.cglib.core.DebuggingClassWriter;

public class CglibProxyTest {

  @Test
  public void testProxy() {
    String path = System.getProperty("user.dir") + "/aop";
    System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, path + "/cglib");

    UserFacade userFacade = new UserFacade();
    UserFacade proxy = (UserFacade) new CglibUserFacadeProxy().getProxy(userFacade);

    String userName = "gether";
    Assert.assertEquals("before" + "this is " + userName + "after", proxy.getUserName(userName));
  }
}
