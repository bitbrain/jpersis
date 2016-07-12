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

import de.bitbrain.jpersis.JPersisException;
import de.bitbrain.jpersis.annotations.PrimaryKey;
import de.bitbrain.jpersis.drivers.AbstractDriver;
import de.bitbrain.jpersis.drivers.DriverException;
import de.bitbrain.jpersis.drivers.Query;
import de.bitbrain.jpersis.util.FieldExtractor;
import de.bitbrain.jpersis.util.FieldInvoker;
import de.bitbrain.jpersis.util.FieldInvoker.InvokeException;
import de.bitbrain.jpersis.util.Naming;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * Abstract implementation for JDBC
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

  private ResultSetReader resultSetReader;

  public JDBCDriver(String host, String port, String database, String user, String password) {
    this.database = database;
    this.host = host;
    this.user = user;
    this.password = password;
    this.port = port;
    resultSetReader = new ResultSetReader();
  }

  protected abstract String getURL(String host, String port, String database);

  @Override
  protected Query createQuery(Class<?> model, Naming naming) {
    return new JDBCQuery(model, naming, statement);
  }

  @Override
  public void connect() throws DriverException {
    try {
      this.connection = DriverManager.getConnection(getURL(host, port, database), user, password);
      this.statement = this.connection.createStatement();
    } catch (SQLException ex) {
      throw new DriverException(ex);
    }
  }

  @Override
  public Object commit(Query query, Class<?> returnType, Object[] args, Class<?> model, Naming naming)
      throws DriverException {
    JDBCQuery jdbcQuery = (JDBCQuery)query;
    String sql = query.toString();
    try {
      query.createTable(connection);
      return extractObjectFromResult(jdbcQuery, returnType, model, naming);
    } catch (SQLException e) {
      throw new DriverException(e + " " + sql);
    } finally {
      invokePrimaryKey(jdbcQuery, args);
    }
  }

  private Object extractObjectFromResult(JDBCQuery query, Class<?> returnType, Class<?> model, Naming naming) throws SQLException {
    if (executeWithResult(query)) {
      return resultSetReader.read(statement.getResultSet(), returnType, model, naming);
    } else if (returnType.equals(Void.class) || returnType.equals(void.class)) {
      return void.class;
    } else {
      return statement.getUpdateCount() > 0;
    }
  }

  private boolean executeWithResult(JDBCQuery query) throws SQLException {
    if (query.primaryKeyUpdated() && generateKeyUpdateSupported()) {
      return statement.execute(query.toString(), Statement.RETURN_GENERATED_KEYS);
    } else {
      return statement.execute(query.toString());
    }
  }

  private void invokePrimaryKey(JDBCQuery query, Object[] args) throws DriverException {
      if (query.primaryKeyUpdated() && args != null && args.length == 1) {
        ResultSet keys;
        try {
          Field f = FieldExtractor.extractPrimaryKey(args[0]);
          if (f.getAnnotation(PrimaryKey.class).value()) {
            keys = statement.getGeneratedKeys();
            String value = "0";
            while (keys.next()) {
              value = String.valueOf(keys.getInt(1));
              break;
            }
            FieldInvoker.invoke(args[0], f, value);
          }
        } catch (SQLException e) {
          throw new DriverException(e + query.toString());
        } catch (InvokeException e) {
          throw new JPersisException(e + query.toString());
        }
      }
  }

  @Override
  public void close() throws DriverException {
    if (connection != null) {
      try {
        connection.close();
      } catch (SQLException ex) {
        throw new DriverException(ex);
      }
    }
  }

  // Normally JDBC should support returning generated keys. Some driver
  // may not support this feature so it can be toggled off.
  protected boolean generateKeyUpdateSupported() {
    return true;
  }
}
