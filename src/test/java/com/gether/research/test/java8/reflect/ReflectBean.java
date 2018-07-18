package com.gether.research.test.java8.reflect;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

@NoArgsConstructor
@Log
@Data
public class ReflectBean {

  // 无法修改static final 基本类型的常量值
  public static final Integer def = 100;

  private String name;
  private int age;
}
