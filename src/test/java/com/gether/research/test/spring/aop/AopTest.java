package com.gether.research.test.spring.aop;

import com.gether.research.service.UserService;
import com.gether.research.service.impl.UserServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.aop.SpringProxy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class AopTest {

  @Test
  public void testAop() {
    ApplicationContext applicationContext = new FileSystemXmlApplicationContext(
        "classpath:applicationContext-aop.xml");
    UserService testProxyFactoryBean = (UserService) applicationContext
        .getBean("testProxyFactoryBean");

    String userName = "gether";
    Assert.assertEquals("this is " + userName, testProxyFactoryBean.getUserName(userName));
  }


  @Test
  public void testIsAssignableFrom(){
    System.out.println(SpringProxy.class.isAssignableFrom(UserServiceImpl.class));
    System.out.println(SpringProxy.class.isAssignableFrom(SpringProxy.class));
    System.out.println(SpringProxy.class.isAssignableFrom(UserService.class));
  }
}
