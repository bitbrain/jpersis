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

package de.bitbrain.jpersis.mocks;

import de.bitbrain.jpersis.IdProvider;
import de.bitbrain.jpersis.annotations.PrimaryKey;

/**
 * Mock model
 * 
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.1
 * @version 1.1
 */
public class ModelMock implements IdProvider {

  static String TEST = "TEST1";

  static String TEST2 = "TEST2";

  @PrimaryKey(true)
  private int id;

  private String name;

  private String lastName;

  private TestEnum enumTest = TestEnum.TEST1;

  private Class<?> cl = ModelMock.class;

  @Override
  public int getId() {
    return id;
  }

  public String getLastName() {
    return lastName;
  }

  public String getName() {
    return name;
  }

  public void setId(int id) {
    this.id = id;
  }

  public TestEnum getEnumTest() {
    return enumTest;
  }

  public void setEnumTest(TestEnum enumTest) {
    this.enumTest = enumTest;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  public void setClass(Class<?> cl) {
    this.cl = cl;
  }

  public Class<?> getClassType() {
    return cl;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ModelMock other = (ModelMock) obj;
    if (id != other.id)
      return false;
    if (lastName == null) {
      if (other.lastName != null)
        return false;
    } else if (!lastName.equals(other.lastName))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }
}
