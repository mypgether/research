package com.gether.research.test.hongbao;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Contstants {

    //账号文件的目录
    public static final String accountFilePrefix = "/Users/gether/Downloads/code/account/";
    //红包文件目录
    public static final String hongBaofilePrefix = "/Users/gether/Downloads/code/red/";
    //最终结果输出目录
    public static final String outPutAccountFilePrefix = "/Users/gether/Downloads/code/res/";
    //红包文件数量
    public static final int hongBaoFileNum = 50;
    //账号文件数量
    public static final int accountFileNum = 100;

    public static CountDownLatch countDownLatch = new CountDownLatch(hongBaoFileNum);

    public static ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2 + 1);
}
