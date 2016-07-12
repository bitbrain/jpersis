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
   * @return
   */
  Query query(Class<?> model, Naming naming);

  /**
   * Connects this driver with the data source
   */
  void connect() throws DriverException;

  /**
   * Closes this driver from the data source
   */
  void close() throws DriverException;

  /**
   * Commits the given query
   */
  Object commit(Query query, Class<?> returnType, Object[] args, Class<?> model, Naming naming) throws DriverException;
}