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

package de.bitbrain.jpersis;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Contains configuration for Connections tests
 * 
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
public final class Connections {

  public static final class Data {
    private Data() { }
    public String username;
    public String password;
    public String db;
    public String host;
    public String port;
  }

  public static Data MYSQL = new Data();
  public static Data POSTGRESQL = new Data();

  public static void init(MySQLContainer mysql, PostgreSQLContainer postgres) {
    MYSQL.db = "test";
    MYSQL.username = mysql.getUsername();
    MYSQL.password = mysql.getPassword();
    MYSQL.host = mysql.getContainerIpAddress();
    MYSQL.port = String.valueOf(mysql.getMappedPort((Integer)mysql.getExposedPorts().get(0)));

    POSTGRESQL.db = "postgres";
    POSTGRESQL.username = postgres.getUsername();
    POSTGRESQL.password = postgres.getPassword();
    POSTGRESQL.host = postgres.getContainerIpAddress();
    POSTGRESQL.port = String.valueOf(postgres.getMappedPort((Integer)postgres.getExposedPorts().get(0)));
  }
}
