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
package de.bitbrain.jpersis.drivers;

import java.sql.Statement;

/**
 * SQL language implementation for a query
 * 
 * @author Miguel Gonzalez
 * @since 1.0
 * @version 1.0
 */
public class JDBCQuery implements Query {
  
  private String table = "";
  private String clause = "";
  private String order = "";
  private String limit = "";
  private String condition = "";
  private String create = "";
  
  private Statement statement;
  
  public JDBCQuery(String table, Statement statement) {
    this.table = table;
    this.statement = statement;
  }

  @Override
  public Query condition(String condition, Object[] args) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Query select() {
    clause = "SELECT * FROM `" + table + "` ";
    return this;
  }

  @Override
  public Query update(Object object) {
    clause = "UPDATE ";
    return this;
  }

  @Override
  public Query delete(Object object) {
    clause = "DELETE ";
    return this;
  }

  @Override
  public Query insert(Object object) {
    clause = "INSERT INTO ";
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
    return create + clause + condition + order + limit;
  }

  @Override
  public Object createTable(Object model) {
    return null;
  }
}
