package com.gether.research.service.impl;

import com.gether.research.model.User;
import com.gether.research.service.MailService;
import com.gether.research.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service("userService")
@Slf4j
public class UserServiceImpl implements UserService {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private MailService mailService;

  @Override
  public boolean login(String userName, String pwd) {
    System.out.println("login username" + userName);
    User user = this.getUser(userName);
    if (user != null && StringUtils.equalsIgnoreCase(pwd, user.getPassword())) {
      // do logic...
      boolean success = mailService.sendMail(userName, "login success");
      System.out.println("login sendMail success: " + success);
      return true;
    } else {
      System.err.println("user is null");
    }
    return false;
  }

  @Override
  public User getUser(String userName) {
    User user = jdbcTemplate
        .queryForObject("select * from t_user where email = ?",
            new Object[]{userName}, (resultSet, i) -> {
              User user1 = new User();
              user1.setId(resultSet.getLong("id"));
              user1.setName(resultSet.getString("name"));
              user1.setEmail(resultSet.getString("email"));
              user1.setPassword(resultSet.getString("password"));
              return user1;
            });
    return user;
  }
}
