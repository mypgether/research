package com.gether.research.test.hongbao;

import java.io.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.gether.research.test.hongbao.Contstants.*;

/**
 * 1.创建红包文件
 * 2.文件数量50
 * 3.每个文件200W行，总计1亿条数据
 * 4.userid 范围 [0,10000000)
 * 5.红包金额范围 [0,100)
 * ps:跑完后每个文件大概23M左右
 */
public class HongBaoCreater {

    public static void main(String[] args) throws InterruptedException, IOException {
        File file = new File(hongBaofilePrefix);
        if (!file.exists()) file.mkdirs();
        long start = System.currentTimeMillis();
        for (int i = 0; i < hongBaoFileNum; i++) {//创建50个红包文件
            threadPool.submit(new Task(String.valueOf(i)));
        }
        countDownLatch.await();
        System.out.println(System.currentTimeMillis() - start);
        threadPool.shutdown();
    }

    //创建红包
    public static class Task implements Runnable {

        public String fileName = null;

        public Task(String fileName) {
            this.fileName = fileName;
        }

        @Override
        public void run() {
            File file = new File(hongBaofilePrefix + fileName);
            try (FileOutputStream fos = new FileOutputStream(file);
                 OutputStreamWriter osw = new OutputStreamWriter(fos);
                 BufferedWriter br = new BufferedWriter(osw)) {
                //每个文件200W行
                for (int i = 0; i < 2000000; i++) {
                    //1到1000W userid随机
                    int userid = ThreadLocalRandom.current().nextInt(10000000);
                    //金额100以内随机
                    int money = ThreadLocalRandom.current().nextInt(100);
                    //userid和金额中间用空格分隔
                    String row = userid + " " + money;
                    br.write(row);
                    br.newLine();
                }
                br.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }
        }
    }
}
