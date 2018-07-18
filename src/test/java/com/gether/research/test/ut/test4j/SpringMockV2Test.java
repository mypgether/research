package com.gether.research.test.ut.test4j;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.gether.research.mongo.MongoService;
import com.gether.research.mongo.MongoServiceImpl;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Test;

public class SpringMockV2Test {

  @Mocked
  MongoService mongoService;

  @Test
  public void testMockAll() {
    String profile = "this is profile A";
    new Expectations() {{
      mongoService.getProfile();
      result = profile;
    }};
    assertThat(profile, equalTo(mongoService.getProfile()));

    mongoService.addProfile();
    System.out.println("mongoService.addProfile() ");
  }

  @Test
  public void testMockOneMethod() {
    String profile = "this is profile A";
    MongoService mongoService = new MongoServiceImpl();
    new Expectations(mongoService) {{
      mongoService.getProfile();
      result = profile;
    }};
    assertThat(profile, equalTo(mongoService.getProfile()));

    mongoService.addProfile();
    System.out.println("mongoService.addProfile() ");
  }
}