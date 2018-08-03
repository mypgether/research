package com.gether.research.aop;

import com.alibaba.fastjson.JSON;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.AfterReturningAdvice;

@Slf4j
public class LogAdvice implements AfterReturningAdvice {

  @Override
  public void afterReturning(Object returnValue, Method method, Object[] args, Object target) {
    String methodName = method.getName();
    log.info("invoke {} ,params: {} ,result: {}", methodName, JSON.toJSON(args), JSON.toJSON(target));
  }
}