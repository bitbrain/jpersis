/*
 * Copyright 2014 Miguel Gonzalez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.bitbrain.jpersis.drivers.jdbc;

import de.bitbrain.jpersis.drivers.DriverException;
import de.bitbrain.jpersis.drivers.Query;
import de.bitbrain.jpersis.util.FieldExtractor;
import de.bitbrain.jpersis.util.Naming;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static de.bitbrain.jpersis.drivers.jdbc.SQLUtils.*;

/**
 * SQL language implementation for a query
 * 
 * @author Miguel Gonzalez Sanchez
 * @since 1.0
 * @version 1.0
 */
public class JDBCQuery implements Query {

  private String clause = "";
  private String order = "";
  private String limit = "";
  private String condition = "";

  private Statement statement;
  private Naming naming;
  private Class<?> model;
  private boolean updated;
  private Slang slang = createSlang();

  public JDBCQuery(Class<?> model, Naming naming, Statement statement) {
    this.naming = naming;
    this.model = model;
    this.statement = statement;
  }

  @Override
  public Query condition(String condition, Object... args) {
    this.condition = " " + SQL.WHERE + " " + generateConditionString(condition, args);
    return this;
  }

  @Override
  public Query select() {
    clause = SQL.SELECT + " " + tableName();
    return this;
  }

  @Override
  public Query update(Object object) {
    String cond = generatePreparedConditionString(object, naming, ",");
    Object[] values = FieldExtractor.extractFieldValues(object);
    clause = SQL.UPDATE + " " + tableName() + " " + SQL.SET + " " + generateConditionString(cond, values);
    Field primaryKey = FieldExtractor.extractPrimaryKey(object);
    String primaryKeyCondition = SQLUtils.generatePrimaryKeyCondition(primaryKey, naming);
    Object primaryKeyValue = FieldExtractor.extractPrimaryKeyValue(object);
    condition(primaryKeyCondition, primaryKeyValue);
    return this;
  }

  @Override
  public Query delete(Object object) {
    clause = SQL.DELETE + " " + tableName();
    Field primaryKey = FieldExtractor.extractPrimaryKey(object);
    String primaryKeyCondition = SQLUtils.generatePrimaryKeyCondition(primaryKey, naming);
    Object primaryKeyValue = FieldExtractor.extractPrimaryKeyValue(object);
    condition(primaryKeyCondition, primaryKeyValue);
    return this;
  }

  @Override
  public Query insert(Object object) {
    Object[] args = FieldExtractor.extractFieldValues(object);
    clause =
        SQL.INSERT + " " + tableName() + " " + SQLUtils.generateFieldString(object, naming, true) + " " + SQL.VALUES
            + SQLUtils.generateCommaString(args);
    updated = true;
    return this;
  }

  @Override
  public Query count() {
    clause = SQL.COUNT + " " + tableName();
    return this;
  }

  @Override
  public Query limit(int limit) {
    this.limit = SQL.LIMIT + " " + limit;
    return this;
  }

  @Override
  public Query order(Order order) {
    this.order = SQL.ORDER + " " + order.name();
    return this;
  }

  @Override
  public void createTable(Connection connection) throws DriverException {
    String q = SQL.CREATE_TABLE + " " + tableName();
    try {
      if (!SQLUtils.tableExists(tableName(), connection)) {
        System.out.println("Creating new table '" + tableName() + "'..");
        q += generateTableString(model, naming, slang);
        statement.executeUpdate(q);
      }
    } catch (SQLException e) {
      throw new DriverException(e + q);
    }
  }

  @Override
  public String toString() {
    return clause + condition + order + limit;
  }

  private String tableName() {
    return naming.javaToCollection(model.getSimpleName());
  }

  public boolean primaryKeyUpdated() {
    return updated;
  }

  protected Slang createSlang() {
    return new Slang() {

      @Override
      public String getAutoIncrement() {
        return SQL.AUTOINCREMENT_MYSQL;
      }

      @Override
      public String getTypeRangeString() {
        return "(255)";
      }

      @Override
      public String getPrimaryKey() {
        return SQL.PRIMARY_KEY;
      }

      @Override
      public boolean isAutoIncrementTyped() {
        return false;
      }

      @Override
      public String getReturningOptional(String key) {
        return "";
      }
    };
  }

  protected interface Slang {

    String getAutoIncrement();

    String getPrimaryKey();

    String getTypeRangeString();

    boolean isAutoIncrementTyped();

    String getReturningOptional(String key);

  }
}