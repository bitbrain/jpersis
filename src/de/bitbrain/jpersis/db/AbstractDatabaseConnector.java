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
package de.bitbrain.jpersis.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Abstract implementation of a database connector
 *
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
public abstract class AbstractDatabaseConnector implements DatabaseConnector {

    // =======================================================================
    // Fields
    // =======================================================================
    // Name of the database
    private String database;
    // Name of the host
    private String host;
    // Portnumber 
    private String port;
    // Username
    private String user;
    // Password of the database user
    private String password;
    // Active connection
    private Connection connection;
    // Active statement
    private Statement statement;

    // =======================================================================
    // Constructors
    // =======================================================================
    public AbstractDatabaseConnector(String host, String port, String database, String user, String password) {
        this.database = database;
        this.host = host;
        this.user = user;
        this.password = password;
        this.port = port;
    }

    // =======================================================================
    // Methods from superclass/interface
    // =======================================================================
    @Override
    public void openConnection() throws DatabaseException {
        try {
            this.connection = DriverManager.getConnection(
                   getURL(host, port, database), user, password);
            this.statement = connection.createStatement();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    @Override
    public void closeConnection() throws DatabaseException {
        if (connection != null) {            
            try {
                statement.close();
                connection.close();
            } catch (SQLException ex) {
                throw new DatabaseException(ex);
            }
        }
    }

    @Override
    public Statement getStatement() {
        return statement;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    // =======================================================================
    // Methods
    // =======================================================================
    
    protected abstract String getURL(String host, String port, String database);
}
