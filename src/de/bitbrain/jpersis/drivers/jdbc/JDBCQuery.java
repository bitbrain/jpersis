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

import static de.bitbrain.jpersis.drivers.jdbc.SQLUtils.generateConditionString;
import static de.bitbrain.jpersis.drivers.jdbc.SQLUtils.generatePreparedConditionString;
import static de.bitbrain.jpersis.drivers.jdbc.SQLUtils.generateTableString;

import java.sql.SQLException;
import java.sql.Statement;

import de.bitbrain.jpersis.drivers.DriverException;
import de.bitbrain.jpersis.drivers.Query;
import de.bitbrain.jpersis.util.FieldExtractor;
import de.bitbrain.jpersis.util.Naming;

/**
 * SQL language implementation for a query
 * 
 * @author Miguel Gonzalez
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
  
  public JDBCQuery(Class<?> model, Naming naming, Statement statement) {
    this.naming = naming;
    this.model = model;
    this.statement = statement;
  }

  @Override
  public Query condition(String condition, Object[] args) {
    condition = SQL.WHERE + " " + generateConditionString(condition, args);
    return this;
  }

  @Override
  public Query select() {
    clause = SQL.SELECT + " " + tableName();
    return this;
  }

  @Override
  public Query update(Object object) {    
    String cond = generatePreparedConditionString(object, naming);
    Object[] values = FieldExtractor.extractFieldValues(object);
    clause = SQL.UPDATE + " " + tableName() + " " + SQL.SET + " " + generateConditionString(cond, values);
    return this;
  }

  @Override
  public Query delete(Object object) {
    clause = SQL.DELETE + " " + tableName();
    return this;
  }

  @Override
  public Query insert(Object object) {
	Object[] args = FieldExtractor.extractFieldValues(object);
    clause = SQL.INSERT + " " + tableName() + SQL.VALUES + SQLUtils.generateCommaString(args);
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
  public void createTable() throws DriverException {
    String q = SQL.CREATE_TABLE + " " + tableName();
    q += generateTableString(model, naming);
    try {
      statement.executeUpdate(q);
    } catch (SQLException e) {
      throw new DriverException(e);
    }
  }
  
  @Override
  public String toString() {
    return clause + condition + order + limit;
  }
  
  private String tableName() {
    return "`" + naming.javaToCollection(model.getSimpleName()) + "`";
  }
}