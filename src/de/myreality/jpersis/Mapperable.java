package de.myreality.jpersis;

import de.myreality.jpersis.db.DatabaseConnector;


/**
 * Provides functionality to manage mappers
 * 
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
public interface Mapperable {
    
    /**
     * Returns a registered mapper. When a mapper is not registered, a 
     * {@see MapperException} will be thrown.
     * 
     * @param <T> Type of the mapper
     * @param type Class of the mapper
     * @return 
     */
    <T> T getMapper(Class<T> type);
    
    /**
     * Checks if a given mapper has been registered in the manager
     * 
     * @param <T> Type of the mapper
     * @param type Class of the mapper
     * @return True when mapper has been registered
     */
    <T> boolean hasMapper(Class<T> type);
    
    /**
     * Adds a new mapper to the manager
     * 
     * @param <T> Type of the mapper
     * @param type class of the mapper
     */
    public <T> void addMapper(Class<T> type);
    
    void setConnector(DatabaseConnector connector);
    
    DatabaseConnector getConnector();
}
