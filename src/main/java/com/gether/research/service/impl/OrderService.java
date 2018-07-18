package com.gether.research.service.impl;

import com.gether.research.service.MailService;
import com.gether.research.service.UserCheckService;
import javax.annotation.Resource;

public class OrderService {

  MailService mailService;
  @Resource
  UserCheckService userCheckService;

  // 构造函数
  public OrderService(MailService mailService) {
    this.mailService = mailService;
  }

  /**
   * 下订单
   *
   * @param buyerId 买家ID
   * @param itemId 商品id
   * @return 返回 下订单是否成功
   */
  public boolean submitOrder(long buyerId, long itemId) {
    // 先校验用户身份
    if (!userCheckService.check(buyerId)) {
      // 用户身份不合法
      return false;
    }
    // 下单逻辑代码，
    // 省略...
    // 下单完成，给买家发邮件
    if (!this.mailService.sendMail(String.valueOf(buyerId), "下单成功")) {
      // 邮件发送成功
      return false;
    }
    return true;
  }
}