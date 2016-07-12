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
package de.bitbrain.jpersis.util;

/**
 * Naming implementation for CamelCase
 */
public class CamelCaseNaming implements Naming {

  @Override
  public String collectionToJava(String name) {
    String[] parts = name.split("_");
    if (parts.length > 1) {
      String camelCaseString = "";
      for (String part : parts) {
        camelCaseString += toProperCase(part);
      }
      return camelCaseString;
    } else {
      return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
  }

  @Override
  public String javaToCollection(String name) {
    String regex = "([a-z])([A-Z])";
    String replacement = "$1_$2";
    return name.replaceAll(regex, replacement).toLowerCase();
  }

  @Override
  public String fieldToJava(String name) {
    return collectionToJava(name);
  }

  @Override
  public String javaToField(String name) {
    return javaToCollection(name);
  }

  /**
   * Converts a target string to proper case
   *
   * @param s
   *          target string
   * @return proper cased string
   */
  private static String toProperCase(String s) {
    return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
  }

}
