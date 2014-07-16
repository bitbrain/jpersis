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
