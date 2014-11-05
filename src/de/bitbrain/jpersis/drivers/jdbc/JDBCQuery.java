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

import java.sql.SQLException;
import java.sql.Statement;

import de.bitbrain.jpersis.drivers.Query;
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
    condition = "WHERE " + SQLUtils.generateConditionString(condition, args, naming);
    return this;
  }

  @Override
  public Query select() {
    clause = "SELECT * FROM `" + tableName() + "` ";
    return this;
  }

  @Override
  public Query update(Object object) {
    clause = "UPDATE `" + tableName() + " SET ";
    return this;
  }

  @Override
  public Query delete(Object object) {
    clause = "DELETE FROM " + tableName();
    return this;
  }

  @Override
  public Query insert(Object object) {
    clause = "INSERT INTO " + tableName();
    return this;
  }

  @Override
  public Query count() {
    clause = "COUNT * ";
    return this;
  }

  @Override
  public Query limit(int limit) {
    this.limit = "LIMIT " + limit;
    return this;
  }

  @Override
  public Query order(Order order) {
    this.order = "ORDER BY " + order.name();
    return this;
  }

  @Override
  public Object commit() {    
    return null;
  }

  @Override
  public Object createTable() {
    String q = "CREATE TABLE IF NOT EXISTS " + tableName();
    q = SQLUtils.generateTableString(model, naming);
    try {
      return statement.executeUpdate(q) == 0;
    } catch (SQLException e) {
      return Boolean.FALSE;
    }
  }
  
  @Override
  public String toString() {
    return clause + condition + order + limit;
  }
  
  private String tableName() {
    return "`" + naming.javaToCollection(model) + "`";
  }
}