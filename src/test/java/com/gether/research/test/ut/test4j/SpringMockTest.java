package com.gether.research.test.ut.test4j;

import com.gether.research.mongo.MongoService;
import com.gether.research.mongo.MongoServiceImpl;
import mockit.Mock;
import org.junit.Test;
import org.test4j.junit.Test4J;
import org.test4j.module.spring.annotations.AutoBeanInject;
import org.test4j.module.spring.annotations.AutoBeanInject.BeanMap;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringContext;

@SpringContext
@AutoBeanInject(maps = {@BeanMap(intf = "**.*", impl = "**.*Impl"),// <br>
    @BeanMap(intf = "**.*", impl = "**.impl.*Impl")})
public class SpringMockTest extends Test4J {

  @SpringBeanByName
  MongoService mongoService;


  @Test
  public void testMock() {
    new MockUp<MongoServiceImpl>() {
      @Mock
      public String getProfile() {
        return "say hello mock1";
      }
    };

    String str = mongoService.getProfile();
    want.string(str).isEqualTo("say hello mock1");
  }

  @Test
  public void testMockV2() {
    final String name = "darui.wu";
    new MockUp<MongoServiceImpl>() {
      @Mock
      public String getProfile() {
        return name;
      }
    };

    String cust = mongoService.getProfile();
    want.object(cust).isEqualTo(name);
  }
}