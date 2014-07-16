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

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import de.myreality.jpersis.db.DatabaseConnector;
import de.myreality.jpersis.db.DatabaseException;

/**
 * Proxy object of a given mapper. Will be created of the interface definition
 * to call self-written query methods.
 * 
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
public class MapperProxy<T> implements InvocationHandler, Serializable {

	private static final long serialVersionUID = 1L;

	// Cache of all defined methods
    private final Map<Method, MapperMethod> methodCache;
    
    private DatabaseConnector connector;
    
    /**
     * Constructor to initialize the proxy
     * 
     * @param methodCache 
     */
    public MapperProxy(Map<Method, MapperMethod> methodCache, DatabaseConnector connector) {
        this.methodCache = methodCache;
        this.connector = connector;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws DatabaseException, Throwable {
       
       if (methodIsValid(method)) {
            final MapperMethod mapperMethod = cachedMapperMethod(method);
            return mapperMethod.execute(args);
        } else {
            return method.invoke(this, args);
        }
    }
    
    // Adds a new mapper method to the proxy (only when there is only one annotation defined)
    private MapperMethod cachedMapperMethod(Method method) {
        MapperMethod mapperMethod = methodCache.get(method);
        if (mapperMethod == null) {
            if (method.getDeclaredAnnotations().length == 1) {
                mapperMethod = new MapperMethod(method, method.getDeclaredAnnotations()[0], connector);
                methodCache.put(method, mapperMethod);
            } else {
                throw new MapperException("Mapper " + method.getDeclaringClass() + " has a missing annotation");
            }
        }
        return mapperMethod;
    }
    
    
    private boolean methodIsValid(Method method) {
        return !method.getClass().equals(Object.class);
    }
    
}
