package com.gether.research.test.java8.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.junit.Test;

public class ReflectTest {

  @Test
  public void modifyFiled() throws Exception {
    // 改变Private2类的私有变量的值
    ReflectBean reflectBean = new ReflectBean();

    Class<?> classType = reflectBean.getClass();
    Field nameField = classType.getDeclaredField("name");
    // 取消默认java访问控制检查，Field类的父类AccessibleObject类提供的方法
    nameField.setAccessible(true);
    String name = "lisi";
    // Field类的set(Object obj, Object value)方法将指定对象上此Field对象表示的字段设置为指定的新值
    nameField.set(reflectBean, name);
    // 输出：lisi
//    Assert.assertEquals(reflectBean.getName(), name);
    System.out.println(reflectBean.getName());

    int defaultV = 2000;
    Field staticFinalField = classType.getDeclaredField("def");
    setFinalStatic(staticFinalField, reflectBean, defaultV);

    System.out.println(ReflectBean.def);
  }

  static void setFinalStatic(Field field, Object target, Object newValue) throws Exception {
    field.setAccessible(true);
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    field.set(target, newValue);
  }
}
