package com.gether.research.test.spring.aop;

import com.gether.research.service.UserService;
import com.gether.research.service.impl.UserServiceImpl;
import com.gether.research.test.spring.aop.jdk.JdkProxyTest;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;
import org.aopalliance.aop.Advice;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.aop.SpringProxy;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/**
 * @see JdkProxyTest#testProxy
 * @see com.gether.research.test.spring.aop.cglib.CglibProxyTest#testProxy
 */
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
  public void testProxyFactory() {
    ApplicationContext applicationContext = new FileSystemXmlApplicationContext(
        "classpath:applicationContext-aop.xml");
    ProxyFactory proxyFactory = (ProxyFactory) applicationContext.getBean("proxyFactory");
    Advice advice = (Advice) applicationContext.getBean("afterReturningAdviceInterceptor");

    proxyFactory.addAdvice(advice);
    UserService proxy = (UserService) proxyFactory.getProxy();
    String userName = "gether";
    Assert.assertEquals("this is " + userName, proxy.getUserName(userName));
  }


  @Test
  public void testIsAssignableFrom() {
    System.out.println(SpringProxy.class.isAssignableFrom(UserServiceImpl.class));
    System.out.println(SpringProxy.class.isAssignableFrom(SpringProxy.class));
    System.out.println(SpringProxy.class.isAssignableFrom(UserService.class));
  }

  @Test
  public void testClazzInterface() {
    Class<UserServiceImpl> userClazz = UserServiceImpl.class;
    Set<Class<?>> classes = new LinkedHashSet<Class<?>>(
        ClassUtils.getAllInterfacesForClassAsSet(userClazz));
    classes.add(userClazz);
    for (Class<?> clazz : classes) {
      Method[] methods = ReflectionUtils.getAllDeclaredMethods(clazz);
      for (Method method : methods) {
        System.out.println(clazz.getSimpleName() + "==>" + method.getName());
      }
    }
  }
}
