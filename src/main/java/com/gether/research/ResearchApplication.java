package com.gether.research;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by myp on 2017/8/16.
 */
@ImportResource(locations = { "classpath:META-INF/spring/applicationContext.xml" })
@ComponentScan
@EnableAutoConfiguration
//@EnableCircuitBreaker
//@EnableDiscoveryClient
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableAsync
@SpringBootApplication
public class ResearchApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ResearchApplication.class).web(true).run(args);
    }
}
