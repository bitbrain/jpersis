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

import java.sql.Connection;

/**
 * Query for a driver
 * 
 * @author Miguel Gonzalez Sanchez
 * @since 1.0
 * @version 1.0
 */
public interface Query {

  /**
   * Changes the condition of the current query
   * 
   * @param condition
   *          condition to make
   * @param args
   *          arguments
   * @return this query
   */
  Query condition(String condition, Object... args);

  /**
   * Changes to select mode
   * 
   * @return this query
   */
  Query select();

  /**
   * Changes to update mode and adds the object to the update list
   * 
   * @param object
   *          object to update
   * @return this query
   */
  Query update(Object object);

  /**
   * Changes to delete mode and adds the object to the delete list
   * 
   * @param object
   *          object to delete
   * @return this query
   */
  Query delete(Object object);

  /**
   * Changes to insert mode and adds the object to the insert list
   * 
   * @param object
   *          object to insert
   * @return this query
   */
  Query insert(Object object);

  /**
   * Changes to count mode
   * 
   * @return this query
   */
  Query count();

  /**
   * Sets a limit for this query
   * 
   * @param limit
   *          limit amount
   * @return this query
   */
  Query limit(int limit);

  /**
   * Sets the order of this query
   * 
   * @param order
   *          order
   * @return this query
   */
  Query order(Order order);

  /**
   * Creates a new table of the given model
   * 
   * @throws DriverException if table could not be created
   */
  void createTable(Connection connection) throws DriverException;

  /**
   * Query orders of a query
   */
  public enum Order {
    ASC,
    DESC;
  }
}