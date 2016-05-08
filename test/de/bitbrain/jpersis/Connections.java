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

import de.bitbrain.jpersis.util.PropertiesCache;

/**
 * Contains configuration for Connections tests
 * 
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
public final class Connections {

  private static final PropertiesCache cache = new PropertiesCache("connections");

  // MySQL
  public static final String MYSQL_USERNAME = cache.getProperty("MYSQL_USERNAME");
  public static final String MYSQL_PASSWORD = cache.getProperty("MYSQL_PASSWORD");
  public static final String MYSQL_PORT = cache.getProperty("MYSQL_PORT");
  public static final String MYSQL_DATABASE = cache.getProperty("MYSQL_DATABASE");
  public static final String MYSQL_HOST = cache.getProperty("MYSQL_HOST");

  // PostgreSQL
  public static final String POSTGRES_USERNAME = cache.getProperty("POSTGRES_USERNAME");
  public static final String POSTGRES_PASSWORD = cache.getProperty("POSTGRES_PASSWORD");
  public static final String POSTGRES_PORT = cache.getProperty("POSTGRES_PORT");
  public static final String POSTGRES_DATABASE = cache.getProperty("POSTGRES_DATABASE");
  public static final String POSTGRES_HOST = cache.getProperty("POSTGRES_HOST");
}
