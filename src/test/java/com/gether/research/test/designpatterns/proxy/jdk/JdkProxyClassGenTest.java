package com.gether.research.test.designpatterns.proxy.jdk;

import com.gether.research.service.UserService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.junit.Test;
import sun.misc.ProxyGenerator;

public class JdkProxyClassGenTest {

  @Test
  public void generateClass() throws IOException {
    String proxyName = "UserServiceProxy";
    byte[] classFile = ProxyGenerator
        .generateProxyClass(proxyName, new Class[]{UserService.class});
    String path = System.getProperty("user.dir");
    File file = new File(path + "/" + proxyName + ".class");
    FileOutputStream fos = new FileOutputStream(file);
    fos.write(classFile);
    fos.flush();
    fos.close();
  }
}