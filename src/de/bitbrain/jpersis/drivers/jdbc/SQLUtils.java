/**
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

import java.lang.reflect.Field;

import de.bitbrain.jpersis.util.Naming;

/**
 * Utility class for SQL operations
 * 
 * @author Miguel Gonzalez
 * @since 1.0
 * @version 1.0
 */
public final class SQLUtils {

  /**
   * Generates an SQL string including braces.
   * 
   * @param model
   * @param naming
   * @return
   */
  public static String generateTableString(Class<?> model, Naming naming) {
    String r = "(";
    
    
    
    return r + ")";
  }
  
  public static String generatePreparedConditionString(Object object, Naming naming) {
    Field[] fields = object.getClass().getFields();
    String condition = "";
    for (int i = 0; i < fields.length; ++i) {
      Field f = fields[i];
      condition += naming.javaToField(f) + "=$" + i;
      if (i < fields.length - 1) {
        condition += " AND ";
      }
    }
    return condition;
  }

  /**
   * 
   * 
   * @param condition
   * @param args
   * @param naming
   * @return
   */
  public static String generateConditionString(String condition, Object[] args, Naming naming) {
    return "";
  }
}
