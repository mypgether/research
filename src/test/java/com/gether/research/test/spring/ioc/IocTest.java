package com.gether.research.test.spring.ioc;

import com.gether.research.service.UserService;
import java.lang.reflect.Constructor;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.PathResource;

public class IocTest {

  @Test
  public void testXmlReaderIoc() {
    PathResource pathResource = new PathResource(
        this.getClass().getClassLoader().getResource("applicationContext.xml").getPath());

    DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
    XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
    xmlBeanDefinitionReader.loadBeanDefinitions(pathResource);

    Assert.assertEquals(2, beanFactory.getBeanDefinitionCount());

    String userName = "userName";
    UserService userService = (UserService) beanFactory.getBean("userUservice");

    Assert.assertEquals("this is " + userName, userService.getUserName(userName));
  }

  @Test
  public void testNewInstance() throws ClassNotFoundException, NoSuchMethodException {
    Class<?> clazz = Class.forName("com.gether.research.service.impl.UserServiceImpl");
    Constructor<?> constructorToUse = clazz.getDeclaredConstructor((Class[]) null);

    Object userServiceObj = BeanUtils.instantiateClass(constructorToUse);
    Assert.assertTrue(userServiceObj instanceof UserService);

    String userName = "userName";
    UserService userService = (UserService) userServiceObj;
    Assert.assertEquals("this is " + userName, userService.getUserName(userName));
  }


  @Test
  public void testAppCtxIoc() {
    ApplicationContext applicationContext = new FileSystemXmlApplicationContext(
        "classpath:applicationContext.xml");

    Assert.assertEquals(2, applicationContext.getBeanDefinitionCount());

    String userName = "userName";
    UserService userService = (UserService) applicationContext.getBean("userUservice");
    Assert.assertEquals("this is " + userName, userService.getUserName(userName));
  }
}
