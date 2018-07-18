package com.gether.research.test.ut.jmockit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.gether.research.model.User;
import com.gether.research.service.MailService;
import com.gether.research.service.impl.UserServiceImpl;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mock;
import mockit.MockUp;
import mockit.Tested;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class ServiceMockTest {

  @Tested
  UserServiceImpl userServiceMe;
  @Injectable
  MailService mailService;
  @Injectable
  JdbcTemplate jdbcTemplate;

  @Test
  public void testMockInnerService() {
    String userName = "123456@qq-.com";
    String password = "123456";

    new MockUp<UserServiceImpl>() {
      @Mock
      public User getUser(String userName) {
        User user = new User();
        user.setEmail(userName);
        user.setPassword(password);
        return user;
      }
    };

    new Expectations() {{
      mailService.sendMail(userName, anyString);
      result = true;
    }};
    boolean res = userServiceMe.login(userName, password);
    assertThat(true, equalTo(res));
  }
}
