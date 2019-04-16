package com.gether.research.test.mq.pulsar;

import lombok.extern.slf4j.Slf4j;
import org.apache.bookkeeper.client.BookKeeper;
import org.apache.bookkeeper.client.LedgerHandle;
import org.apache.bookkeeper.client.LedgerHandleAdv;
import org.apache.bookkeeper.client.api.DigestType;
import org.apache.bookkeeper.client.api.LedgerEntries;
import org.apache.bookkeeper.conf.ClientConfiguration;
import org.junit.Test;

/**
 * @author myp
 * @date 2019/3/26 下午11:20
 */
@Slf4j
public class BookeeperAdvanceTest {

  @Test
  public void testWriteWithCreateLedger() {
    try {
      String connectionString = "172.30.10.208:2181,172.30.10.209:2181,172.30.10.210:2181";
      ClientConfiguration config = new ClientConfiguration();
      config.setZkServers(connectionString);
      config.setAddEntryTimeout(2000);
      BookKeeper bkClient = new BookKeeper(config);

      long ledgerId = System.currentTimeMillis();
      byte[] password = "test".getBytes();
      LedgerHandleAdv wh = (LedgerHandleAdv) bkClient.newCreateLedgerOp()
          .withDigestType(DigestType.CRC32)
          .withPassword(password)
          .withEnsembleSize(3)
          .withWriteQuorumSize(3)
          .withAckQuorumSize(2)
          .makeAdv()
          .withLedgerId(ledgerId)
          .execute()
          .get();

      long entryId = wh.write(0, "this is data12".getBytes());
      log.info("ledger entryId {}", entryId);
      entryId = wh.write(1, "this is data1212390kxzc".getBytes());
      log.info("ledger entryId {}", entryId);
      LedgerEntries entries = wh.read(0, wh.getLastAddConfirmed());
      entries.forEach((entrie -> log
          .info("Successfully read entry: {} ,data: {}", entrie.getEntryId(),
              new String(entrie.getEntryBytes()))));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testReadOnly() {
    try {
      String connectionString = "172.30.10.208:2181,172.30.10.209:2181,172.30.10.210:2181";
      ClientConfiguration config = new ClientConfiguration();
      config.setZkServers(connectionString);
      config.setAddEntryTimeout(2000);
      BookKeeper bkClient = new BookKeeper(config);

      long ledgerId = 1553661776970L;
      byte[] password = "test".getBytes();
      LedgerHandle ledgerHandle = bkClient
          .openLedger(ledgerId, BookKeeper.DigestType.CRC32, password);
      LedgerEntries entries = ledgerHandle.read(0, ledgerHandle.getLastAddConfirmed());
      entries.forEach((entrie -> log
          .info("Successfully read entry: {} ,data: {}", entrie.getEntryId(),
              new String(entrie.getEntryBytes()))));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
