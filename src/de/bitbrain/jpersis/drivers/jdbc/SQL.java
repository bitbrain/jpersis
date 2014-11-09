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

/**
 * Contains SQL definitions
 * 
 * @author Miguel Gonzalez
 * @since 1.0
 * @version 1.0
 */
public class SQL {
  // Statements
  public static final String CREATE_TABLE   = "CREATE TABLE IF NOT EXISTS";
  public static final String SELECT         = "SELECT * FROM";
  public static final String INSERT         = "INSERT INTO";
  public static final String UPDATE         = "UPDATE";
  public static final String DELETE         = "DELETE FROM";
  public static final String AND            = "AND";
  public static final String WHERE          = "WHERE";
  public static final String AUTO_INCREMENT = "AUTO_INCREMENT";
  public static final String PRIMARY_KEY    = "PRIMARY KEY";
  public static final String FROM           = "FROM";
  public static final String SET            = "SET";
  public static final String COUNT          = "COUNT";
  public static final String LIMIT          = "LIMIT";
  public static final String ORDER          = "ORDER BY";
  public static final String VALUES         = " VALUES";
  
  // Primary Types
  public static final String CHAR           = "CHAR";
  public static final String VARCHAR        = "VARCHAR";
  public static final String FLOAT          = "FLOAT";
  public static final String DOUBLE         = "DOUBLE";
  public static final String LONG           = "LONG";
  public static final String ENUM           = "VARCHAR";
  public static final String BOOL           = "BOOL";
  public static final String DATETIME       = "DATETIME";
  public static final String INTEGER        = "INTEGER";
}