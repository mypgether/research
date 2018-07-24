package com.gether.research.service;

import com.gether.research.model.User;

public interface UserService {

  public boolean login(String userName, String pwd);

  public User getUser(String userName);

  public String getUserName(String userName);
}
