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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.bitbrain.jpersis.annotations.DataMapper;

/**
 * Utility class which maps between databases and java objects.
 *
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
public class MapperUtils {

    // ============================================================================
    // Converting methods
    // ============================================================================
    
    public static <T> Collection<T> convertToJavaCollection(Class<T> clazz, ResultSet resultSet, String[] foreignKeys) {
        Collection<T> result = new ArrayList<T>();
        try {
            while (resultSet != null && resultSet.next()) {
                T single = MapperUtils.convertToJavaObject(clazz, resultSet, foreignKeys);

                if (single != null) {
                    result.add(single);
                }
            }

            return result;
        } catch (SQLException ex) {
            Logger.getLogger(MapperUtils.class.getName()).log(Level.SEVERE, null, ex);
            return result;
        }
    }

    public static <T> Collection<T> convertToJavaCollection(Class<T> clazz, ResultSet resultSet) {
        return convertToJavaCollection(clazz, resultSet, new String[0]);
    }

    public static <T> T convertToJavaObject(Class<T> clazz, ResultSet set, String[] foreignKeys) {

        if (set != null) {
            @SuppressWarnings("unchecked")
			T result = (T) generateObject(clazz);
            invokeFromResultSet(result, set, foreignKeys);
            return result;
        } else {
            return null;
        }

    }

    public static <T> T convertToJavaObject(Class<T> clazz, ResultSet set) {
        return convertToJavaObject(clazz, set, new String[0]);
    }

    /**
     * Updates an inserted object by inserting the primary key
     *
     * @param statement
     * @param insert
     * @param object
     * @throws SQLException
     */
    public static void updatePrimaryKey(Statement statement, DataMapper mapper, Object object) throws SQLException {



        ResultSet keys = statement.getGeneratedKeys();
        String value = "0";

        while (keys.next()) {
            value = String.valueOf(keys.getInt(1));
            break;
        }

        MapperUtils.storeValueIntoObject(object, mapper.primaryKey(), value, new String[0]);
    }

    /**
     * Splits the getters of an object to database fields
     *
     * @param object target object
     * @return map containing all fields with values
     */
    public static Map<String, String> splitGettersToFields(Object object, String[] foreignKeys) {
        Map<String, String> fields = new HashMap<String, String>();

        if (object != null) {
            try {

                BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
                PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();

                for (PropertyDescriptor propertyDescriptor : descriptors) {
                    Method method = propertyDescriptor.getReadMethod();

                    if (method == null) {
                        throw new MapperException("There is a corrupt getter inside of class " + object.getClass().getSimpleName());
                    }

                    computeField(method, object, foreignKeys, fields);
                }
            } catch (IntrospectionException ex) {
                Logger.getLogger(MapperUtils.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(MapperUtils.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(MapperUtils.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(MapperUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return fields;
    }

    // ============================================================================
    // Helper methods
    // ============================================================================
    /**
     * Filters a signature of at the beginning of a string.
     *
     * @param methodName name of the string
     * @param signature target signature to remove
     * @return filtered string
     */
    private static String filterSignature(String string, String signature) {
    	
    	if (string.isEmpty()) {
    		throw new MapperException("Try to filter empty method name");
    	}

    	if (signature.length() > string.length()) {
    		throw new MapperException("The method " + string + " is not valid.");
    	}
    	
        String signaturePart = string.substring(0, signature.length());
        String otherPart = string.substring(signature.length(), string.length());

        if (signaturePart.equals(signature)) {
            signaturePart = "";
        }

        return signaturePart + otherPart;
    }

    /**
     * Generates a new object of the given class by calling the default
     * constructor.
     *
     * @param clazz target class
     * @return new instance of an object
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private static Object generateObject(Class clazz) {
        try {
            Constructor<?> ctor = clazz.getConstructor();
            return ctor.newInstance(new Object[]{});
        } catch (NoSuchMethodException ex) {
            throw new MapperException("Missing default constructor in class " + clazz.getSimpleName());
        } catch (InstantiationException ex) {
            throw new MapperException("Can't create an instance of " + clazz.getSimpleName());
        } catch (IllegalAccessException ex) {
            throw new MapperException("No access to default constructor of class " + clazz.getSimpleName());
        } catch (IllegalArgumentException ex) {
            throw new MapperException("Missing default constructor in class " + clazz.getSimpleName());
        } catch (InvocationTargetException ex) {
            Logger.getLogger(MapperMethod.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Invokes the data inside of a result set to the given object.
     *
     * @param object target object to inject the data
     * @param set target result set to take the data from
     */
    private static void invokeFromResultSet(Object object, ResultSet set, String[] foreignKeys) {
        try {
            ResultSetMetaData meta = set.getMetaData();

            for (int column = 1; column <= meta.getColumnCount(); ++column) {
                String columnName = meta.getColumnName(column);
                String value = set.getString(column);

                if (value != null) {
                    value = value.trim();
                } else {
                    value = "";
                }
                storeValueIntoObject(object, columnName, value, foreignKeys);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MapperUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Stores a given value into an object
     *
     * @param object instance of the object
     * @param field name of the database field
     * @param value value to insert in
     */
    private static void storeValueIntoObject(Object object, String field, String value, String[] foreignKeys) {
        Method method = convertFieldToSetter(object, field);

        if (!AnnotationUtils.isIgnored(method)) {

            // Method exists, store data into object
            if (method != null) {
                if (method.getParameterTypes().length == 1) {
                    Class<?> type = method.getParameterTypes()[0];
                    invokeSetter(object, method, type, value, foreignKeys);
                } else {
                    throw new MapperException("Setter " + method + " should have exactly 1 argument.");
                }
            } else {
                throw new MapperException("Setter is missing for field " + field);
            }
        }
    }

    /**
     * Fetches the setter of an object by considering the field name
     *
     * @param object target object with the setter
     * @param field name of the database field
     * @return Setter which represents the field
     */
    private static Method convertFieldToSetter(Object object, String field) {

        field = field.trim();

        String expected = "set" + MapperManager.getInstance().getConverter().toJavaFormat(field);

        for (Method method : object.getClass().getMethods()) {


            if (method.getName().equals(expected)) {
                return method;
            }
        }

        throw new MapperException("Setter " + expected + " does not exists in class " + object.getClass().getSimpleName());
    }

    /**
     * Converts a method to a field name
     *
     * @param methodName name of the method
     * @return name of the field
     */
    private static String convertMethodToField(Method method) {

        String methodName = method.getName();

        // Filter get signature
        methodName = filterSignature(methodName, "get");

        // Filter is signature
        methodName = filterSignature(methodName, "is");

        // Filter set signature
        methodName = filterSignature(methodName, "set");

        return MapperManager.getInstance().getConverter().toDatabaseFormat(methodName);
    }

    /**
     * Invokes a setter by setting the given value
     *
     * @param object object to invoke
     * @param method setter method
     * @param argument class of the argument
     * @param value value to set
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private static void invokeSetter(Object object, Method method, Class argument, String value, String[] foreignKeys) {
        
        try {
            // ENUM
            if (argument.isEnum()) {
                Enum enumObject = Enum.valueOf(argument, value);
                method.invoke(object, enumObject);

                // DATE                
            } else if (argument.equals(Date.class) || argument.equals(java.sql.Date.class)) {
                try {
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = (Date) format.parse(value);
                    method.invoke(object, date);
                } catch (ParseException ex) { 
                    System.out.println(ex.getMessage());
                }
                // INTEGER
            } else if (argument.equals(Integer.TYPE)) {

                Integer integerValue = 0;

                if (!value.isEmpty()) {
                    integerValue = Integer.valueOf(value);
                }
                method.invoke(object, integerValue);

                // BOOLEAN
            } else if (argument.equals(Boolean.TYPE) || argument.equals(boolean.class)) {
                boolean result = (value.equals("t")) ? true : false;
                method.invoke(object, result);

                // CHAR
            } else if (argument.equals(Character.TYPE)) {
                if (value.length() == 1) {
                    method.invoke(object, Character.valueOf(value.toCharArray()[0]));
                } else {
                    throw new MapperException(method + " has wrong arguments.");
                }

                // LONG
            } else if (argument.equals(Long.TYPE)) {
                method.invoke(object, Long.valueOf(value));

                // FLOAT
            } else if (argument.equals(Float.TYPE)) {
                method.invoke(object, Float.valueOf(value));

                // DOUBLE
            } else if (argument.equals(Double.TYPE)) {
                method.invoke(object, Double.valueOf(value));

                // STRING
            } else if (argument.equals(String.class)) {
                method.invoke(object, value);
            } else {
                throw new MapperException("Can't incoke setter '" + method.getName() + "': Argument '"
                        + value + "' + of type " + argument.getSimpleName() + " is not valid.");
            }
        } catch (IllegalAccessException ex) {
            throw new MapperException("Setter " + method + " does not exists.");
        } catch (IllegalArgumentException ex) {
            throw new MapperException(method + " has no argument: " + ex.getMessage());
        } catch (InvocationTargetException ex) {
            throw new MapperException("Setter " + method + " does not exists.");
        }
    }

    @SuppressWarnings("rawtypes")
	private static void computeField(Method method, Object object, String[] foreignKeys, Map<String, String> map) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        boolean isInCustomClass = !method.getDeclaringClass().equals(Object.class);
        boolean isNotIgnored = !AnnotationUtils.isIgnored(method);
        boolean isEnum = false;
        boolean isDate = method.getReturnType().equals(Date.class) || method.getReturnType().equals(java.sql.Date.class);
        boolean isString = method.getReturnType().equals(String.class);

        if (isNotIgnored && isInCustomClass) {
            Object returnValue = method.invoke(object, new Object[0]);
            String fieldValue = convertMethodToField(method);
            String value = "";

            if (returnValue != null) {

                if (returnValue.getClass().isEnum()) {
                    value = ((Enum) returnValue).name();
                    isEnum = true;
                } else {
                    value = String.valueOf(returnValue);
                }
            }

            for (String foreignKey : foreignKeys) {
                if (foreignKey.equals(fieldValue)) {
                    if (value.equals("0")) {
                        value = "null";
                    }
                }
            }
            // Create a new date when there is no current date
            if (isDate) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (value.isEmpty()) {                    
                    Calendar cal = Calendar.getInstance();
                    value = dateFormat.format(cal.getTime());
                } else {
                    value = dateFormat.format(returnValue);
                }
            }
            
            // Converting date in timestamp -> Timestamp converting bug
            

            if (isDate || isString || isEnum) {
                value = "'" + value + "'";
            }
             
            map.put(fieldValue, value);
        }
    }
}
