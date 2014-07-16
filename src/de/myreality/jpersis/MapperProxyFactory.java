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

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.myreality.jpersis.db.DatabaseConnector;

/**
 * Factory which creates new proxies
 *
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
public class MapperProxyFactory<T> {

    // Interface which provides the mapper functionality
    private Class<T> mapperInterface;
    // Method cache
    private Map<Method, MapperMethod> methodCache = new ConcurrentHashMap<Method, MapperMethod>();
    
    private DatabaseConnector connector;

    /**
     * Constructor for a new factory.
     *
     * @param mapperInterface Mapper interface which is annotated by a {
     * @see DataMapper}
     */
    public MapperProxyFactory(Class<T> mapperInterface, DatabaseConnector connector) {
        this.mapperInterface = mapperInterface;
        this.connector = connector;
    }

    /**
     * @return The current interface of the mapper
     */
    public Class<T> getMapperInterface() {
        return mapperInterface;
    }

    /**
     * Creates a new proxy instance
     * 
     * @return new proxy instance of the mapper
     */
    public T newInstance() {
        final MapperProxy<T> mapperProxy = new MapperProxy<T>(methodCache, connector);
        return newInstance(mapperProxy);
    }

    @SuppressWarnings("unchecked")
	protected T newInstance(MapperProxy<T> mapperProxy) {
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, mapperProxy);
    }
}
