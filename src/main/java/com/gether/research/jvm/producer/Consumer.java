package com.gether.research.jvm.producer;

/**
 * Created by myp on 2017/8/13.
 */
public class Consumer extends Thread {


  ShareObjService shareObjService;

  public Consumer(ShareObjService shareObjService) {
    this.shareObjService = shareObjService;
  }

  @Override
  public void run() {
    shareObjService.consumer();
  }
}