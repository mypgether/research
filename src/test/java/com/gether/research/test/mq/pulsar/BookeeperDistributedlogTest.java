package com.gether.research.test.mq.pulsar;

import com.google.common.base.Preconditions;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.apache.bookkeeper.feature.Feature;
import org.apache.bookkeeper.feature.FeatureProvider;
import org.apache.bookkeeper.feature.FixedValueFeature;
import org.apache.bookkeeper.feature.SettableFeatureProvider;
import org.apache.bookkeeper.stats.NullStatsLogger;
import org.apache.distributedlog.DistributedLogConfiguration;
import org.apache.distributedlog.namespace.DistributedLogNamespace;
import org.apache.distributedlog.namespace.DistributedLogNamespaceBuilder;
import org.junit.Test;

/**
 * @author myp
 * @date 2019/3/26 下午11:20
 */
@Slf4j
public class BookeeperDistributedlogTest {

  @Test
  public void testWriteWithCreateLedger() {
    try {
      String connectionString = "172.30.10.208:2181,172.30.10.209:2181,172.30.10.210:2181";
      DistributedLogConfiguration conf = new DistributedLogConfiguration();
      conf.setZKSessionTimeoutSeconds(1);
      conf.setBKClientZKSessionTimeout(1);
      conf.setFederatedNamespaceEnabled(true);
      URI uri = new URI(String.format("distributedlog://%s/test", connectionString));
      DistributedLogNamespaceBuilder builder = DistributedLogNamespaceBuilder.newBuilder();
      DistributedLogNamespace namespace = builder
          .conf(conf)
          .uri(uri)
          .statsLogger(new NullStatsLogger())
          .featureProvider(new FeatureProvider() {
            @Override
            public Feature getFeature(String name) {
              return new FixedValueFeature(name, true);
            }

            @Override
            public FeatureProvider scope(String name) {
              return new SettableFeatureProvider("", 1);
            }
          })
          .build();

      namespace.createLog("test-log");
      log.info("namespace log create success");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testCheckArg() {
    int conectionTimeoutMs = 3000;
    Preconditions.checkArgument(conectionTimeoutMs > 0, "Invalid connection timeout : %d",
        conectionTimeoutMs);
  }
}
