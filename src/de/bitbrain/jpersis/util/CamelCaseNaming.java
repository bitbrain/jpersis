package de.bitbrain.jpersis.util;


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
