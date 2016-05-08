package de.bitbrain.jpersis.mocks;

import de.bitbrain.jpersis.annotations.PrimaryKey;

public class MinimalMock {

  @PrimaryKey(true)
  private int id;

  private String name;

  private Class<?> cl = MinimalMock.class;

  static MinimalMock mock = new MinimalMock();

  public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public static MinimalMock getMock() {
    return mock;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setClass(Class<?> cl) {
    this.cl = cl;
  }

  public Class<?> getClassType() {
    return cl;
  }
}
