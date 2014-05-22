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
