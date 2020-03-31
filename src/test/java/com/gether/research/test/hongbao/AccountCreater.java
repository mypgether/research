package com.gether.research.test.hongbao;

import java.io.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.gether.research.test.hongbao.Contstants.*;

/**
 * 1.创建账号文件
 * 2.文件数量100
 * 3.每个文件10W行，总计1000W个用户id
 * 4.userid 有序且根据%100 分桶到100个文件中，初始金额[0,100) 随机
 */
public class AccountCreater {

    public static void main(String[] args) throws InterruptedException, IOException {
        File file = new File(accountFilePrefix);
        File outPutFile = new File(outPutAccountFilePrefix);
        if (!file.exists()) file.mkdirs();
        if (!outPutFile.exists()) file.mkdirs();
        long start = System.currentTimeMillis();
        for (int i = 0; i < accountFileNum; i++) {
            threadPool.submit(new AccountTask(i));
        }
        countDownLatch.await();
        System.out.println(System.currentTimeMillis() - start);
        threadPool.shutdown();
    }

    public static class AccountTask implements Runnable {

        public int bucket;

        public AccountTask(int bucket) {
            this.bucket = bucket;
        }

        @Override
        public void run() {
            File file = new File(accountFilePrefix + bucket);
            try (FileOutputStream fos = new FileOutputStream(file);
                 OutputStreamWriter osw = new OutputStreamWriter(fos);
                 BufferedWriter br = new BufferedWriter(osw)) {
                //0-1000W个userid顺序写入
                for (int i = 0; i < 10000000; i++) {
                    if (i % 100 == bucket) {
                        //账户余额100以内随机
                        int money = ThreadLocalRandom.current().nextInt(100);
                        //userid和初始金额中间逗号隔开
                        String row = i + " " + money;
                        br.write(row);
                        br.newLine();
                    }
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
