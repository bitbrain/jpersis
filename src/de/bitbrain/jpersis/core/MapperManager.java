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
package de.bitbrain.jpersis.core;

import de.bitbrain.jpersis.drivers.DriverProvider;

/**
 * Manages mappers and provides access to them
 *
 * @author Miguel Gonzalez Sanchez
 * @since 1.0
 * @version 1.0
 */
public interface MapperManager extends DriverProvider {

  /**
   * Provides access to an existing mapper
   * 
   * @param mapper
   *          existing mapper class
   * @return Mapper instance or null of not found.
   */
  <T> T get(Class<T> mapper);

  /**
   * Adds the mapper to this manager. If already there, do nothing.
   * 
   * @param mapper
   *          new mapper class
   */
  <T> void add(Class<T> mapper);

  /**
   * Indicates if the manager contains the mapper
   * 
   * @param mapper
   *          mapper class
   * @return true if contained
   */
  boolean contains(Class<?> mapper);
}
