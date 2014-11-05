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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.bitbrain.jpersis.JPersisException;
import de.bitbrain.jpersis.drivers.AbstractDriver;
import de.bitbrain.jpersis.drivers.Query;
import de.bitbrain.jpersis.util.Naming;

/**
 * Implementation for SQLite
 * 
 * @author Miguel Gonzalez
 * @since 1.0
 * @version 1.0
 */
public abstract class JDBCDriver extends AbstractDriver {

  private String database;

  private String host;

  private String port;

  private String user;

  private String password;

  protected Statement statement;
  
  protected Connection connection;

  public JDBCDriver(String host, String port, String database, String user, String password) {
    this.database = database;
    this.host = host;
    this.user = user;
    this.password = password;
    this.port = port;
  }

  protected abstract String getURL(String host, String port, String database);

  @Override
  protected Query createQuery(Class<?> model, Naming naming) {
    return new JDBCQuery(model, naming, statement);
  }

  @Override
  public void connect() {
    try {
      this.connection = DriverManager.getConnection(getURL(host, port, database), user, password);
    } catch (SQLException ex) {
      throw new JPersisException(ex);
    }
  }
  
  @Override
  public Object commit(Query query) {
    String sql = query.toString();
    try {
      query.createTable();
      if (statement.execute(sql)) {
        ResultSet resultSet = statement.getResultSet();
        return null; // TODO
      } else {
        throw new JPersisException("Could not execute SQL: " + sql);
      }
    } catch (SQLException e) {
      throw new JPersisException(e);
    }
  }

  @Override
  public void close() {
    if (connection != null) {
      try {
        connection.close();
      } catch (SQLException ex) {
        throw new JPersisException(ex);
      }
    }
  }
}
