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

/**
 * Contains configuration for TravisCI tests
 * 
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
public final class TravisCI {

  // MySQL
  public static final String MYSQL_USERNAME = "travis";
  public static final String MYSQL_PASSWORD = "";
  public static final String MYSQL_PORT     = "3306";
  public static final String MYSQL_DATABASE = "jpersis";
  public static final String MYSQL_HOST     = "127.0.0.1";
  
  // PostgreSQL
  public static final String POSTGRES_USERNAME = "postgres";
  public static final String POSTGRES_PASSWORD = "";
  public static final String POSTGRES_PORT     = "5432";
  public static final String POSTGRES_DATABASE = "jpersis";
  public static final String POSTGRES_HOST     = "127.0.0.1";
}
