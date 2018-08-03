package com.gether.research.service.impl;

import com.gether.research.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("mailService")
@Slf4j
public class MailServiceImpl implements MailService {

  @Override
  public boolean sendMail(String userName, String content) {
    System.out.println("sendMail to: " + userName + ", content: " + content);
    return true;
  }
}