package de.bitbrain.jpersis.util;

import java.lang.reflect.Field;

public class CamelCaseNaming implements Naming {

  @Override
  public String collectionToJava(String name) {
    return name;
  }

  @Override
  public String javaToCollection(Class<?> model) {
    return "";
  }

  @Override
  public String fieldToJava(String name) {
    return name;
  }

  @Override
  public String javaToField(Field field) {
    return "";
  }

}
