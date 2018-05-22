package com.gether.research.constants;

import com.soundcloud.prometheus.hystrix.HystrixPrometheusMetricsPublisher;
import io.prometheus.client.CollectorRegistry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * Created by myp on 2017/9/20.
 */
@Component
public class PromethusConstants implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        HystrixPrometheusMetricsPublisher.register("research", CollectorRegistry.defaultRegistry);
    }
}