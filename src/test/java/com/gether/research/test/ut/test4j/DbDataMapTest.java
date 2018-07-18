package com.gether.research.test.ut.test4j;

import org.junit.Test;
import org.test4j.junit.Test4J;

public class DbDataMapTest extends Test4J {

  // id, name, email
  public static final String DB_NAME = "t_user";

  @Test
  public void testDatamapInsert() {
    db.table(DB_NAME).clean().insert(1, new DataMap() {
      {
        this.put("name", "jwen");
      }
    }).commit();
  }

  @Test
  public void testDatamapInsertBatch() {
    db.table(DB_NAME).clean().insert(5, new DataMap() {
      {
        this.put("id", DataGenerator.increase(100, 1));
        this.put("name", DataGenerator.random(String.class));
        this.put("email", new DataGenerator() {
          @Override
          public Object generate(int i) {
            return value("name") + "@163.com";
          }
        });
      }
    }).commit();
  }

  @Test
  public void testInsertDataByJsonString() {
    db.table(DB_NAME).clean()
        .insert("{\"id\":1,\"name\":\"hehe\",\"email\":\"hehe@163.com\"}")
        .commit();
  }
}