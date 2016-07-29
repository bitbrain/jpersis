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
package de.bitbrain.jpersis.core.methods;

import de.bitbrain.jpersis.MapperException;
import de.bitbrain.jpersis.drivers.Driver;
import de.bitbrain.jpersis.util.Naming;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Method of a mapper
 *
 * @author Miguel Gonzalez Sanchez
 * @since 1.0
 * @version 1.0
 */
public interface MapperMethod<T extends Annotation> {

  /**
   * Gets the underlying annotation
   * 
   * @return annotation
   */
  T getAnnotation();

  /**
   * Executes the method internally
   * 
   * @param method original method
   * @param model class of the model
   * @param args method arguments
   * @param driver the currently in use driver
   * @param naming current naming convention
   * @return resulting object
   * @throws MapperException when mapper is badly configured
   */
  Object execute(Method method, Class<?> model, Object[] args, Driver driver, Naming naming) throws MapperException;
}
