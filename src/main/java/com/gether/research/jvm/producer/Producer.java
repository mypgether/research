package com.gether.research.jvm.producer;

/**
 * Created by myp on 2017/8/13.
 */
public class Producer extends Thread {

  ShareObjService shareObjService;

  public Producer(ShareObjService shareObjService) {
    this.shareObjService = shareObjService;
  }

  @Override
  public void run() {
    shareObjService.produce();
  }
}