package de.myreality.jpersis;

import java.util.HashMap;
import java.util.Map;

import de.myreality.jpersis.annotations.DataMapper;
import de.myreality.jpersis.db.DatabaseConnector;

/**
 * Manager of all current mapper to provide them. Can store new mapper and loads
 * all existing mapper at the beginning.
 *
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
public class MapperManager implements Mapperable {

    // Factories for all mappers
    private final Map<Class<?>, MapperProxyFactory<?>> mapperFactories = new HashMap<Class<?>, MapperProxyFactory<?>>();
    
    // Singleton instance
    private static final MapperManager instance = null;
    
    // Connector to the database
    private DatabaseConnector connector;

    static {
    	// TODO
        //DatabaseConnector connector = new PostgresDatabaseConnector();
        //instance = new MapperManager(connector);
    }

    private MapperManager(DatabaseConnector connector) {
        this.connector = connector;
    }

    /**
     * @return Current instance of the singleton
     */
    public static MapperManager getInstance() {
        return instance;
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        
        // Add the mapper if not exists
        if (!hasMapper(type)) {
            addMapper(type);
        }
        
        final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) mapperFactories.get(type);

        if (mapperProxyFactory == null) {
            throw new MapperException("Mapper " + type.getSimpleName() + " does not exists.");
        }

        try {
            return mapperProxyFactory.newInstance();
        } catch (Exception e) {
            throw new MapperException("Mapper " + type + " does not exists.");
        }
    }

    @Override
    public <T> boolean hasMapper(Class<T> type) {
        return mapperFactories.containsKey(type);
    }

    @Override
    public <T> void addMapper(Class<T> type) {
        if (type.isInterface()) {
            
            if (type.getAnnotation(DataMapper.class) == null) {
                throw new MapperException(type + " must use a DataMapper annotation.");                        
            }
            
            if (hasMapper(type)) {
                throw new MapperException("Mapper " + type + " has been already registered.");
            }

            mapperFactories.put(type, new MapperProxyFactory<T>(type, connector));
        } else {
            throw new MapperException("Only interfaces can be a DataMapper");
        }
    }

    @Override
    public void setConnector(DatabaseConnector connector) {
        if (connector != null) {
            this.connector = connector;
        }
    }

    @Override
    public DatabaseConnector getConnector() {
        return connector;
    }
}
