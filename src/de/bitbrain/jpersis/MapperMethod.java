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
package de.bitbrain.jpersis;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

import de.bitbrain.jpersis.annotations.Count;
import de.bitbrain.jpersis.annotations.DataMapper;
import de.bitbrain.jpersis.annotations.Delete;
import de.bitbrain.jpersis.annotations.Insert;
import de.bitbrain.jpersis.annotations.Select;
import de.bitbrain.jpersis.annotations.Update;
import de.bitbrain.jpersis.db.DatabaseConnector;
import de.bitbrain.jpersis.db.DatabaseException;

/**
 * Method of a mapper which can be invoked. This class looks up the regarding
 * annotation and translates it to a valid SQL statement.
 *
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
public class MapperMethod {

    // The regarding annotation of the method
    private Annotation annotation;
    // The regarding method
    private Method method;
    // Database connector
    private DatabaseConnector connector;

    /**
     * Creates a new mapper method
     *
     * @param method target method
     * @param annotation target annotation
     * @param models list of all loaded models
     */
    public MapperMethod(Method method, Annotation annotation, DatabaseConnector connector) {
        this.annotation = annotation;
        this.method = method;
        this.connector = connector;
    }

    /**
     * Runs a method by considering the annotation and computing the SQL
     * statement.
     *
     * @param args method arguments
     * @return a predefined return value
     */
    public Object execute(Object[] args) throws DatabaseException {

        try {
            // Open a connection
            connector.openConnection();

            // SELECT
            if (annotation instanceof Select) {
                return select((Select) annotation, args);
            } else // INSERT
            if (annotation instanceof Insert) {
                update((Insert) annotation, args);
                return Void.TYPE;
            } else // UPDATE
            if (annotation instanceof Update) {
                update((Update) annotation, args);
                return Void.TYPE;
            } else // DELETE
            if (annotation instanceof Delete) {
                update((Delete) annotation, args);
                return Void.TYPE;
            } else // COUNT
            if (annotation instanceof Count) {
                return count((Count) annotation, args);
            } else {
                throw new DatabaseException("Mapper " + method.getDeclaringClass() + " has a missing annotation at " + method.getName());
            }
        } finally {
            connector.closeConnection();
        }
        
    }

    /**
     * Runs a select statement on the database by considering the given data in
     * the annotation.
     * <p>
     * If the return value of the regarding method signature is a collection, a
     * collection will be returned. Otherwise only one new bean will be created.
     *
     * @param select the target select annotation
     * @param args arguments of the method
     * @return A collection or a single object
     */
    @SuppressWarnings("unchecked")
	private Object select(Select select, Object[] args) throws DatabaseException {

        try {
            Statement s = connector.getStatement();
            String sql = AnnotationUtils.translateToSQL(select, getDataMapper(), args);
            if (s != null) {
                ResultSet set = s.executeQuery(sql);
                if (method.getReturnType().equals(Collection.class)) {
                    return MapperUtils.convertToJavaCollection(getModelClass(), set, getDataMapper().foreignKeys());
                } else {
                    while (set != null && set.next()) {
                        return MapperUtils.convertToJavaObject(getModelClass(), set, getDataMapper().foreignKeys());
                    }

                    return null;
                }
            } else {
                throw new DatabaseException("Database connection refused. Statement is null");
            }
        } catch (SQLException ex) {
            if (method.getReturnType().equals(Collection.class)) {
                return MapperUtils.convertToJavaCollection(getModelClass(), null);
            } else {
                return null;
            }

        }
    }

    // Counts a given annotation query
    private Integer count(Count count, Object[] args) throws DatabaseException {
        Statement s = connector.getStatement();
        String sql = AnnotationUtils.translateToSQL(count, getDataMapper(), args);
        try {
            if (s != null) {
                ResultSet set = s.executeQuery(sql);
                while (set.next()) {
                    return set.getInt(1);
                }
            } else {
                throw new DatabaseException("Database connection refused. Statement is null");
            }
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }


        return new Integer(10);
    }

    // Updates a given annotation query
    @SuppressWarnings("unchecked")
	private void update(Annotation annotation, Object[] args) throws DatabaseException {
        if (method.getParameterTypes().length == 1) {

            Class<?> parameter = method.getParameterTypes()[0];

            if (parameter.equals(Collection.class)) {
                Collection<Object> data = (Collection<Object>) args[0];
                for (Object o : data) {
                    update(annotation, o, args);
                }
            } else {
                update(annotation, args[0], args);
            }
        } else {
            throw new MapperException("Method needs to have only one argument");
        }
    }

    // Updates a given annotation query. (Can contain Update and Insert and Delete)
    @SuppressWarnings("unused")
	private void update(Annotation annotation, Object object, Object[] args) throws DatabaseException {
        Statement s = connector.getStatement();
        String[] fields = new String[0];
        String sql = "";
        
        if (annotation instanceof Insert) {
            Insert insert = (Insert) annotation;
            sql = AnnotationUtils.translateToSQL(insert, getDataMapper(), object);
        } else if (annotation instanceof Update) {
            Update update = (Update) annotation;
            sql = AnnotationUtils.translateToSQL(update, getDataMapper(), object, args);
            fields = update.fields();
        } else if (annotation instanceof Delete) {
            Delete delete = (Delete) annotation;
            sql = AnnotationUtils.translateToSQL(delete, getDataMapper(), object, args);
        }
        try {
            if (s != null) {
                s.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);

                // Update the object if it has been inserted
                if (annotation instanceof Insert) {
                    MapperUtils.updatePrimaryKey(s, getDataMapper(), object);
                }
            } else {
                throw new DatabaseException("Database connection refused. Statement is null");
            }
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    // Gets the model of the parent mapper
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private Class getModelClass() {
        Class parent = method.getDeclaringClass();
        DataMapper mapper = (DataMapper) parent.getAnnotation(DataMapper.class);
        try {
            return Class.forName(getDataMapper().model());
        } catch (ClassNotFoundException ex) {
            throw new MapperException("Model " + mapper.model() + " does not exist.");
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	private DataMapper getDataMapper() {
        Class parent = method.getDeclaringClass();
        return (DataMapper) parent.getAnnotation(DataMapper.class);
    }
}
