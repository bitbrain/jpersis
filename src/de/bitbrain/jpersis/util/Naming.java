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
 * JPersis main class which provides mapper creation and database interaction
 *
 * @author Miguel Gonzalez Sanchez
 * @since 1.0
 * @version 1.0
 */
public interface Naming {

  /** Default naming is CamelCase */
  static Naming DEFAULT = new CamelCaseNaming();

  /**
   * Convention for collections in Java
   * 
   * @param name original name
   * @return java name
   */
  public String collectionToJava(String name);

  /**
   * Convention for collections in data stores
   *
   * @param name original name
   * @return data store name
   */
  public String javaToCollection(String name);

  /**
   * Convention for fields in Java
   *
   * @param name original name
   * @return java name
   */
  public String fieldToJava(String name);

  /**
   * Convention for fields in data stores
   *
   * @param name original name
   * @return java name
   */
  public String javaToField(String name);
}
