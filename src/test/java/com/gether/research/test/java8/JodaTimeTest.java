package com.gether.research.test.java8;

import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

/**
 * Created by myp on 2017/7/20.
 */
public class JodaTimeTest {

  @Test
  public void testDate() {
    DateTime dateTime1 = new DateTime().withTime(0, 0, 0, 0);
    System.out.println(dateTime1);
    System.out.println(dateTime1.getMillis());
  }

  @Test
  public void testToLocalTime() {
    DateTime nowUTC = new DateTime(DateTimeZone.UTC).withTime(0, 0, 0, 0);
    Date dUTC = nowUTC.toLocalDateTime().toDate();
    System.out.println("utc   : " + nowUTC + " | " + dUTC.getTime());
  }
}