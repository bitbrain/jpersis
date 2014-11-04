package de.bitbrain.jpersis.drivers.sqllite;

import java.sql.DriverManager;
import java.sql.SQLException;

import de.bitbrain.jpersis.JPersisException;
import de.bitbrain.jpersis.drivers.JDBCDriver;

public class SQLiteDriver extends JDBCDriver {
  
  private String file;

  public SQLiteDriver(String file) {
    super("", "", "", "", "");
    this.file = file;
  }

  @Override
  protected String getURL(String host, String port, String database) {
    return null;
  }

  @Override
  public void connect() {
    try {
      Class.forName("org.sqlite.JDBC");
      connection = DriverManager.getConnection("jdbc:sqlite:" + file);
      statement = connection.createStatement();
    } catch (ClassNotFoundException e) {
      throw new JPersisException(e);
    } catch (SQLException e) {
      throw new JPersisException(e);
    }
  }

}