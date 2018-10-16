package com.gether.research.test.java8;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

/**
 * Created by myp on 2017/7/20.
 */
public class DatetimeTest {

  @Test
  public void testDate() {
    LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Europe/Paris"));
    System.out.println(localDateTime.toString());
    localDateTime = localDateTime.withYear(2008);
    System.out.println(localDateTime.toString());

    localDateTime.plusWeeks(4);
    System.out.println(localDateTime.toString());
    localDateTime = localDateTime.plusWeeks(4);
    System.out.println(localDateTime.toString());
    System.out.println(Thread.currentThread().getId());
  }

  @Test
  public void testDateDiff() {
    LocalDateTime a = LocalDateTime.now();
    System.out.println(a.toString());
    System.out.println(a.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    System.out.println(System.currentTimeMillis());

    LocalDateTime b = LocalDateTime.now().withYear(2008).plusWeeks(4);
    System.out.println(b.toString());

    System.out.println(b.until(a, ChronoUnit.DAYS));
    System.out.println(b.until(a, ChronoUnit.YEARS));
    System.out.println(b.until(a, ChronoUnit.HOURS));

    System.out.println(ChronoUnit.YEARS.between(b, a));
    System.out.println(ChronoUnit.HOURS.between(b, a));
    // a- b
    System.out.println(ChronoUnit.DAYS.between(b, a));
    System.out.println(ChronoUnit.HALF_DAYS.between(b, a));
  }
}