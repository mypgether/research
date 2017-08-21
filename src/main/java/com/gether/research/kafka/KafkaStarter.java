package com.gether.research.kafka;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by myp on 2017/8/16.
 */
@Component
public class KafkaStarter implements InitializingBean {
    @Autowired
    private KConsumer kConsumer;
    @Autowired
    private KProducer kProducer;

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(() -> kConsumer.consume()).start();
        new Thread(() -> {
            while (true) {
                kProducer.produce();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}