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

package de.bitbrain.jpersis.drivers.sqllite;

import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

import de.bitbrain.jpersis.JPersisException;
import de.bitbrain.jpersis.drivers.Query;
import de.bitbrain.jpersis.drivers.jdbc.JDBCDriver;
import de.bitbrain.jpersis.util.Naming;

/**
 * Implementation for SQLite
 * 
 * @author Miguel Gonzalez
 * @since 1.0
 * @version 1.0
 */
public class SQLiteDriver extends JDBCDriver {
  
  private String path;

  public SQLiteDriver(String file) {
    super("", "", "", "", "");
    this.path = file;
  }

  @Override
  protected String getURL(String host, String port, String database) {
    return null;
  }
  
  @Override
  protected Query createQuery(Class<?> model, Naming naming) {
    return new SQLiteQuery(model, naming, statement);
  }

  @Override
  public void connect() {
    try {
      ensureFile();
      Class.forName("org.sqlite.JDBC");
      connection = DriverManager.getConnection("jdbc:sqlite:" + path);
      statement = connection.createStatement();
    } catch (ClassNotFoundException e) {
      throw new JPersisException(e);
    } catch (SQLException e) {
      throw new JPersisException(e);
    }
  }
  
  private void ensureFile() {
    File file = new File(path);
    try {
      file.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}