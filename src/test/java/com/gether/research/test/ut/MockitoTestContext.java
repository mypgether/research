package com.gether.research.test.ut;

import org.mockito.MockitoAnnotations;

public abstract class MockitoTestContext {

  public MockitoTestContext() {
    MockitoAnnotations.initMocks(this);
  }
}