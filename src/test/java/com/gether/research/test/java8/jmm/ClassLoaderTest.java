package com.gether.research.test.java8.jmm;

import java.io.FileInputStream;
import java.lang.reflect.Method;

/**
 * @author myp
 * @date 2019/2/18 上午10:18
 */
public class ClassLoaderTest {

  static class MyClassLoader extends ClassLoader {

    private String classPath;

    public MyClassLoader(String classPath) {
      this.classPath = classPath;
    }

    private byte[] loadByte(String name) throws Exception {
      name = name.replaceAll("\\.", "/");
      String filePath = classPath + "/" + name + ".class";
      FileInputStream fis = new FileInputStream(filePath);
      int len = fis.available();
      byte[] data = new byte[len];
      fis.read(data);
      fis.close();
      return data;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
      try {
        byte[] data = loadByte(name);
        return defineClass(name, data, 0, data.length);
      } catch (Exception e) {
        e.printStackTrace();
        throw new ClassNotFoundException();
      }
    }
  }

  public static void main(String args[]) throws Exception {
    MyClassLoader classLoader = new MyClassLoader("/Users/myp");
    Class clazz = classLoader.loadClass("java.lang.System");
//    Class clazz = classLoader.loadClass("com.gether.research.test.DatetimeTest");
    Object obj = clazz.newInstance();
    Method helloMethod = clazz.getDeclaredMethod("hello", null);
    helloMethod.invoke(obj, null);
  }
}
