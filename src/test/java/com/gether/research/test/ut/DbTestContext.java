package com.gether.research.test.ut;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-datasources.xml")
public abstract class DbTestContext {

  private static final String TEST_DB = "test";

  @Autowired
  protected DataSource dataSource;

  @Before
  public void before() throws SQLException {
    Statement stmt = dataSource.getConnection().createStatement();
    ResultSet rs = stmt.executeQuery("select database();");
    String schemaName = "";
    while (rs.next()) {
      schemaName = rs.getString(1);
    }
    if (!StringUtils.startsWithIgnoreCase(schemaName, TEST_DB) && !StringUtils
        .endsWithIgnoreCase(schemaName, TEST_DB)) {
      throw new IllegalArgumentException("schema must start or end with test");
    }

    List<String> tableList = getTruncateTableList();
    if (CollectionUtils.isNotEmpty(tableList)) {
      tableList.forEach((tableName) -> {
        execute(String.format("truncate table %s", tableName));
      });
    }
  }

  /**
   * Execute SQL directly, these SQL may be for test purpose only and should not be included in
   * mybatis sqlMapper configuration files.
   *
   * @param sql SQL statement to execute, variable binding is NOT supported for simplicity.
   */
  protected void execute(String sql) {
    try {
      dataSource.getConnection().createStatement().execute(sql);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  protected abstract List<String> getTruncateTableList();
}