package com.gether.research.test.promethus;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.PushGateway;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by myp on 2017/9/20.
 */
public class PromethusTest {

    @Test
    public void testpush() {
        CollectorRegistry registry = new CollectorRegistry();
        Gauge duration = Gauge.build()
                .name("my_batch_job_duration_seconds")
                .help("Duration of my batch job in seconds.")
                .register(registry);
        Gauge.Timer durationTimer = duration.startTimer();
        try {
            // Your code here.

            // This is only added to the registry after success,
            // so that a previous success in the Pushgateway is not overwritten on failure.
            Gauge lastSuccess = Gauge.build()
                    .name("my_batch_job_last_success_unixtime")
                    .help("Last time my batch job succeeded, in unixtime.")
                    .register(registry);
            lastSuccess.setToCurrentTime();


            Counter versionCount = Counter.build().name("version_count").help("indicate version times").register(registry);
            versionCount.inc(380);
        } finally {
            durationTimer.setDuration();
            PushGateway pg = new PushGateway("127.0.0.1:9091");
            try {
                pg.pushAdd(registry, "my_batch_job_hehe");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}