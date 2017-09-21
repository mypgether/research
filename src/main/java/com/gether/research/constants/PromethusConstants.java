package com.gether.research.constants;

import com.soundcloud.prometheus.hystrix.HystrixPrometheusMetricsPublisher;
import io.prometheus.client.CollectorRegistry;

/**
 * Created by myp on 2017/9/20.
 */
public class PromethusConstants {

    static {
        HystrixPrometheusMetricsPublisher.register("research");
        HystrixPrometheusMetricsPublisher.register("research", CollectorRegistry.defaultRegistry);
    }

    public static void a() {
        System.out.println("ok");
    }
}