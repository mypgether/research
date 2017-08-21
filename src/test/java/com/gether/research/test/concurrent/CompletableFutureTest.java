package com.gether.research.test.concurrent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by myp on 2017/7/12.
 */
public class CompletableFutureTest {

    /**
     * 多个任务同时执行，不依赖接口的前后数据返回
     */
    @Test
    public void testAll() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "one ";
        });

        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "two ";
            }
        });
        String f2Result = f2.get();
        System.out.println("f2Result:" + f2Result);
        System.out.println(System.currentTimeMillis() - start);

        String f1Result = f1.get();
        System.out.println("f1Result:" + f1Result);
        System.out.println(System.currentTimeMillis() - start);
    }


    /**
     * 2个任务同时执行，后面依赖前面的结果
     */
    @Test
    public void testFirstThenSecond() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return " hehe ";
        });
        CompletableFuture<String> f2 = f1.thenApplyAsync(new Function<String, String>() {
            @Override
            public String apply(String s) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return s + "two ";
            }
        });
        String f2Result = f2.get();
        System.out.println("f2Result:" + f2Result);
        System.out.println(System.currentTimeMillis() - start);

        String f1Result = f1.get();
        System.out.println("f1Result:" + f1Result);
        System.out.println(System.currentTimeMillis() - start);
    }

    /**
     * 3个任务同时执行，后面依赖前面的结果
     */
    @Test
    public void testFirstThenSecondThird() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return " hehe ";
        });
        CompletableFuture<String> f2 = f1.thenApplyAsync(new Function<String, String>() {
            @Override
            public String apply(String s) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return s + "two ";
            }
        });
        String f2Result = f2.get();
        System.out.println("f2Result:" + f2Result);
        System.out.println(System.currentTimeMillis() - start);

        String f1Result = f1.get();
        System.out.println("f1Result:" + f1Result);
        System.out.println(System.currentTimeMillis() - start);
    }


    /**
     * 3个任务同时执行，全部结果一起返回
     */
    @Test
    public void testAllResult() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return " hehe ";
        });
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return " hehe1 ";
        });
        CompletableFuture<String> f3 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return " hehe2 ";
        });
        CompletableFuture<List<String>> resultFuture = sequence(Lists.newArrayList(f1, f2, f3));
        List<String> resultList = resultFuture.get();
        System.out.println(resultFuture);
        System.out.println(resultList);

        //CompletableFuture<Void> combine = CompletableFuture.allOf(f1, f2, f3);
        //while (true) {
        //    if (combine.isDone()) {
        //        System.out.println(" combine done ");
        //        break;
        //    }
        //}
        //combine.thenRun(new Runnable() {
        //    @Override
        //    public void run() {
        //        try {
        //            System.out.println(f1.get());
        //            System.out.println(f2.get());
        //            System.out.println(f3.get());
        //        } catch (InterruptedException e) {
        //            e.printStackTrace();
        //        } catch (ExecutionException e) {
        //            e.printStackTrace();
        //        }
        //    }
        //});
        System.out.println(System.currentTimeMillis() - start);
    }

    /**
     * N个任务同时执行，全部结果一起返回
     */
    @Test
    public void testN10BatchResult() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();

        List<Integer> ids = Arrays.asList(4, 3, 2, 4, 6, 2, 1, 5);
        List<List<Integer>> pages = Lists.partition(ids, 3);

        for (List<Integer> page : pages) {
            long tempSt = System.currentTimeMillis();
            System.out.println(page);
            List<CompletableFuture<String>> futures = Lists.newArrayList();
            for (Integer t : page) {
                futures.add(CompletableFuture.supplyAsync(new Supplier<String>() {
                    @Override
                    public String get() {
                        try {
                            Thread.sleep(t * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return "this is " + t;
                    }
                }));
            }
            CompletableFuture<List<String>> resultFuture = sequence(futures);
            List<String> resultList = resultFuture.get();
            System.out.println(resultList);
            System.out.println(System.currentTimeMillis() - tempSt);
        }
        System.out.println(System.currentTimeMillis() - start);
    }


    @Test
    public void testListsPartitionModify() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();

        JSONObject j1 = new JSONObject();
        j1.put("a", "1");
        j1.put("b", "2");

        JSONObject j2 = new JSONObject();
        j2.put("f", "1");
        j2.put("g", "2");

        JSONObject j3 = new JSONObject();
        j3.put("d", "1");
        j3.put("e", "2");

        List<JSONObject> ids = Arrays.asList(j1, j2, j3);
        List<List<JSONObject>> pages = Lists.partition(ids, 3);

        for (List<JSONObject> page : pages) {
            long tempSt = System.currentTimeMillis();
            System.out.println(page);
            List<CompletableFuture<JSONObject>> futures = Lists.newArrayList();
            for (JSONObject t : page) {
                futures.add(CompletableFuture.supplyAsync(new Supplier<JSONObject>() {
                    @Override
                    public JSONObject get() {
                        t.put("nihao", "hehe");
                        return t;
                    }
                }));
            }
            CompletableFuture<List<JSONObject>> resultFuture = sequence(futures);
            List<JSONObject> resultList = resultFuture.get();
            System.out.println(JSON.toJSONString(resultList));
            System.out.println(System.currentTimeMillis() - tempSt);
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    public static <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
        CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        return allDoneFuture.thenApply(v -> futures.stream().map(CompletableFuture::join).collect(Collectors.<T>toList()));
    }

    public static <T> CompletableFuture<Stream<T>> sequence(Stream<CompletableFuture<T>> futures) {
        List<CompletableFuture<T>> futureList = futures.filter(f -> f != null).collect(Collectors.toList());
        return sequence((Stream<CompletableFuture<T>>) futureList);
    }
}