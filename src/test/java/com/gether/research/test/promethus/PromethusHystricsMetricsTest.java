package com.gether.research.test.promethus;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ForkJoinPool;

/**
 * Created by myp on 2017/9/27.
 */
public class PromethusHystricsMetricsTest {

    @Test
    public void testHystrics() throws IOException {
        OkHttpClient client = new OkHttpClient();
        String url = "http://localhost:8080/api/name";
        ForkJoinPool fj = new ForkJoinPool(5);
        for (int i = 0; i <= 10; i++) {
            fj.execute(() -> {
                while (true) {
                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    try {
                        Response resp = client.newCall(request).execute();
                        System.out.println(resp.body().string());
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        while (true) {

        }
    }
}