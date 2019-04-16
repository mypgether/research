package com.gether.research.test.mq.pulsar;

import java.util.Enumeration;
import lombok.extern.slf4j.Slf4j;
import org.apache.bookkeeper.client.BookKeeper;
import org.apache.bookkeeper.client.LedgerEntry;
import org.apache.bookkeeper.client.LedgerHandle;
import org.apache.bookkeeper.conf.ClientConfiguration;
import org.junit.Test;

/**
 * @author myp
 * @date 2019/3/26 下午11:20
 */
@Slf4j
public class BookeeperLowTest {

  @Test
  public void testWrite() {
    try {
      String connectionString = "172.30.10.208:2181,172.30.10.209:2181,172.30.10.210:2181";
      ClientConfiguration config = new ClientConfiguration();
      config.setZkServers(connectionString);
      config.setAddEntryTimeout(2000);
      BookKeeper bkClient = new BookKeeper(config);

      byte[] password = "test2".getBytes();
      LedgerHandle ledger = bkClient.createLedger(BookKeeper.DigestType.MAC, password);
      long entryId = ledger.addEntry("this is data1".getBytes());
      log.info("ledger entryId {}", entryId);
      entryId = ledger.addEntry("this is data2".getBytes());
      log.info("ledger entryId {}", entryId);

      log.info("ledger last ack {}", ledger.getLastAddConfirmed());
      Enumeration<LedgerEntry> entries = ledger.readEntries(0, ledger.getLastAddConfirmed());
      while (entries.hasMoreElements()) {
        LedgerEntry entry = entries.nextElement();
        log.info("Successfully read entry: {} ,data: {}", entry.getEntryId(),
            new String(entry.getEntry()));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
