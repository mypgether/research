package com.gether.research.test.ut.jmockit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.gether.research.model.User;
import com.gether.research.service.MailService;
import com.gether.research.service.UserService;
import javax.annotation.Resource;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//用MockUp来Mock Spring Bean
@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringBeanMockingByMockUpTest {

  final static String userName = "123456@qq-.com";
  final static String password = "123456";


  // 注入Spring Bean，Mock这个实例，就达到了Mock Spring Bean的目的
  @Resource
  UserService userService;
  @Resource
  MailService mailService;
  @Resource
  JdbcTemplate jdbcTemplate;

  @Test

  public void testSpringBeanMockingByMockUp() {
    new Expectations() {{
      mailService.sendMail(userName, anyString);
      result = true;
    }};
    boolean res = userService.login(userName, password);
    assertThat(true, equalTo(res));
  }

  @BeforeClass
  public static void beforeClassMethods() throws Throwable {
    // 必须在Spring容器初始化前，就对Spring Bean的类做MockUp
    addMockUps();
  }

  // 对AnOrdinaryClass的Class
  public static class AnOrdinaryClassMockUp extends MockUp<UserService> {

    @Mock
    public User getUser(String userName) {
      User user = new User();
      user.setEmail(userName);
      user.setPassword(password);
      return user;
    }

  }

  // 添加MockUp
  public static void addMockUps() {
    new AnOrdinaryClassMockUp();
  }
}