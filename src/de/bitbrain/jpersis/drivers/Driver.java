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

import de.bitbrain.jpersis.util.Naming;

/**
 * Driver for database communication
 * 
 * @author Miguel Gonzalez Sanchez
 * @since 1.0
 * @version 1.0
 */
public interface Driver {

  /**
   * Creates a new query compatible with this driver
   * 
   * @param model
   *          model to create a query for
   * @param naming naming convention
   * @return
   */
  Query query(Class<?> model, Naming naming);

  /**
   * Connects this driver with the data source
   *
   * @throws DriverException when connection could not be established
   */
  void connect() throws DriverException;

  /**
   * Closes this driver from the data source
   *
   * @throws DriverException when connection could not be closed
   */
  void close() throws DriverException;

  /**
   * Sets the internal driver mode
   *
   * @param mode mode to set
   */
  void setMode(DriverMode mode);

  /**
   * Provides the current driver mode
   *
   * @return currently in use mode
   */
  DriverMode getMode();

  /**
   * Commits the given query
   *
   * @param query the query object
   * @param returnType type of the return type
   * @param args arguments
   * @param model model class
   * @param naming naming convention
   * @throws DriverException when commit went wrong
   * @return object result of the commit
   */
  Object commit(Query query, Class<?> returnType, Object[] args, Class<?> model, Naming naming) throws DriverException;

  enum DriverMode {
    /**
     * This mode requires custom connect and disconnects of the driver
     */
    CUSTOM,
    /**
     * This mode automatically starts and stops the connection on each call
     */
    AUTO
  }
}