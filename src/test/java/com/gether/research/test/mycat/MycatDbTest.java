package com.gether.research.test.mycat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by myp on 2017/4/26.
 */
public class MycatDbTest {

  private static final String SQL_INSERT = "insert into t_sharding_user (id,name,email,password) values(%s,'%s','email','111111111');";
  private static final String SQL_UPDATE = "update t_sharding_user set name='%s' where id=%s;";
  private static final String SQL_SELECT = "select id,name,email,password from t_sharding_user where id=%s;";

  private static final String driver = "com.mysql.jdbc.Driver";
  private static final String dbName = "springboot";
  private static final String passwrod = "123456";
  private static final String userName = "root";
  private static final String url = "jdbc:mysql://localhost:8066/" + dbName;

  private Connection conn = null;

  @Before
  public void before() throws ClassNotFoundException, SQLException {
    Class.forName(driver);
    conn = DriverManager.getConnection(url, userName, passwrod);
  }

  @Test
  public void testMycatInsertUpdateQuery() {
    try {
      int id = 1000;
      String name = "gether";
      // insert
      PreparedStatement ps = conn.prepareStatement(String.format(SQL_INSERT, id, name));
      ps.execute();

      // update
      name = "gether update";
      ps = conn.prepareStatement(String.format(SQL_UPDATE, name, id));
      int row = ps.executeUpdate();
      System.out.println("update row " + row);

      // select
      ps = conn.prepareStatement(String.format(SQL_SELECT, id));
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        System.out.println("name : " + rs.getString("name"));
      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (conn != null) {
        try {
          conn.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  @Test
  public void testMycatInsertMulti() {
    try {
      Random random = new Random();
      // insert
      for (int i = 0; i < 10; i++) {
        String name = "gether pp";
        PreparedStatement ps = conn
            .prepareStatement(String.format(SQL_INSERT, random.nextInt(1001), name));
        ps.execute();
      }
      // insert
      for (int i = 0; i < 10; i++) {
        String name = "gether mm";
        PreparedStatement ps = conn
            .prepareStatement(String.format(SQL_INSERT, random.nextInt(1001) + 1001, name));
        ps.execute();
      }
      // insert
      for (int i = 0; i < 10; i++) {
        String name = "gether yy";
        PreparedStatement ps = conn
            .prepareStatement(String.format(SQL_INSERT, random.nextInt(2001) + 2001, name));
        ps.execute();
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (conn != null) {
        try {
          conn.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }
}