/*
 * Copyright 2015 Miguel Gonzalez
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

package de.bitbrain.jpersis.drivers.postgresql;

import de.bitbrain.jpersis.JPersisException;
import de.bitbrain.jpersis.drivers.Query;
import de.bitbrain.jpersis.drivers.jdbc.JDBCDriver;
import de.bitbrain.jpersis.util.Naming;

/**
 * Implementation for PostgreSQL
 *
 * @author Miguel Gonzalez Sanchez
 * @since 1.0
 * @version 1.0
 */
public class PostgreSQLDriver extends JDBCDriver {

  public PostgreSQLDriver(String host, String port, String database, String user, String password) {
    super(host, port, database, user, password);
    System.out.println(getURL(host, port, database));
  }

  @Override
  protected String getURL(String host, String port, String database) {
    try {
      Class.forName("org.postgresql.Driver");
      return "jdbc:postgresql://" + host + ":" + port + "/" + database;
    } catch (ClassNotFoundException e) {
      throw new JPersisException("Unable to find PostgreSQL driver 'org.postgresql.Driver'");
    }
  }

  @Override
  protected Query createQuery(Class<?> model, Naming naming) {
    return new PostgreSQLQuery(model, naming, statement);
  }

}
