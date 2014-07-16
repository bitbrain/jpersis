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
package de.myreality.jpersis.db;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Singleton database connector to provide database access with JDBC from 
 * everywhere.
 *
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
public class PostgresDatabaseConnector extends AbstractDatabaseConnector {

    // Connection details
    private static final String DATABASE = "database-name";
    private static final String HOST = "host";
    private static final String PORT = "5432";
    private static final String USER = "postgres";
    private static final String PASSWORD = "password";

    public PostgresDatabaseConnector() {
        super(HOST, PORT, DATABASE, USER, PASSWORD);
    }
    
    

    /**
	 * @param host
	 * @param port
	 * @param database
	 * @param user
	 * @param password
	 */
	public PostgresDatabaseConnector(String host, String port, String database,
			String user, String password) {
		super(host, port, database, user, password);
	}



	@Override
    protected String getURL(String host, String port, String database) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PostgresDatabaseConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "jdbc:postgresql://" + host + ":" + port + "/" + database;
    }

    
}
