package com.gether.research.test.ut.jmockit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.gether.research.controller.ApiController;
import com.gether.research.service.MailService;
import com.gether.research.service.UserService;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Test;

public class ControllerMockTest {

//  @Mocked
//  ApiController apiController;

  @Test
  public void testMockController() {
    String loginResult = "login result : success";
    new Expectations() {{
      apiController.login();
      result = loginResult;
    }};
    String res = apiController.login();
    System.err.println(res);
    assertThat(loginResult, equalTo(res));
  }

  @Tested
  ApiController apiController;

  @Test
  public void testMockUserService(@Injectable UserService userService) {
    String userName = "123456@qq.com";
    String password = "123456";
    new Expectations() {{
      userService.login(userName, password);
      result = true;
    }};
    String res = apiController.login();
    System.err.println(res);
    assertThat("login success", equalTo(res));
  }

  /**
   * 一定要userservice也mock不然会有问题，一直是false
   */
  @Test
  public void testMockMailService(@Injectable UserService userService,
      @Injectable MailService mailService) {
    String userName = "123456@qq.com";
    String password = "123456";
    new Expectations() {{
      mailService.sendMail(userName, "login success");
      result = true;
    }};
    String res = apiController.login();
    System.err.println(res);
    assertThat("login success", equalTo(res));
  }
}
