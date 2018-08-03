package com.gether.research.aop;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;

public class LogAdvisor implements PointcutAdvisor {

  @Override
  public Advice getAdvice() {
    return new LogAdvice();
  }

  @Override
  public boolean isPerInstance() {
    return false;
  }

  @Override
  public Pointcut getPointcut() {
    return new LogPointcut();
  }
}