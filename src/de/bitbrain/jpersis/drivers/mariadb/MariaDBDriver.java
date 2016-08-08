package de.bitbrain.jpersis.drivers.mariadb;

import de.bitbrain.jpersis.JPersisException;
import de.bitbrain.jpersis.drivers.jdbc.JDBCDriver;

/**
 * Implementation for MariaDB
 *
 * @author Miguel Gonzalez Sanchez
 * @since 1.1.0
 * @version 1.1.0
 */
public class MariaDBDriver extends JDBCDriver {

    public MariaDBDriver(String host, String port, String database, String user, String password) {
        super(host, port, database, user, password);
    }

    @Override
    protected String getURL(String host, String port, String database) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            return "jdbc:mariadb://" + host + ":" + port + "/" + database;
        } catch (ClassNotFoundException e) {
            throw new JPersisException("Unable to find MariaDB driver 'org.mariadb.jdbc.Driver'");
        }
    }
}
