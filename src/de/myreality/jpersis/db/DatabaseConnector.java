package de.myreality.jpersis.db;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Represents a connector for a database
 * 
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
public interface DatabaseConnector {
    
    /**
     * Opens a new connection
     * 
     * @throws DatabaseException 
     */
    void openConnection() throws DatabaseException;
    
    /**
     * Closes the current connection
     * 
     * @throws DatabaseException
     */
    void closeConnection() throws DatabaseException;
    
    /**
     * Returns the current statement
     * 
     * @return statement
     */
    Statement getStatement();
    
    /**
     * Returns the current connection
     * 
     * @returns connection
     */
    Connection getConnection();
    
}
