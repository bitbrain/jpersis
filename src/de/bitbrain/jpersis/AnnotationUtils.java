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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.bitbrain.jpersis.annotations.Count;
import de.bitbrain.jpersis.annotations.DataMapper;
import de.bitbrain.jpersis.annotations.Delete;
import de.bitbrain.jpersis.annotations.IgnoredMethod;
import de.bitbrain.jpersis.annotations.Insert;
import de.bitbrain.jpersis.annotations.Select;
import de.bitbrain.jpersis.annotations.Update;

/**
 * Utility class which provides functionality for annotations like SQL
 * translation and annotation searching.
 *
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
public class AnnotationUtils {

    // ============================================================================
    // public methods
    // ============================================================================
    /**
     * Checks if a method should be ignored
     *
     * @param method target method to check
     * @return true if it is ignored
     */
    public static boolean isIgnored(Method method) {
        return isImplementedMethodAnnotatedWith(method, IgnoredMethod.class);
    }

    /**
     * Translate the count annotation to a SQL string
     *
     * @param count Count annotation
     * @param args Method arguments
     * @return SQL string
     */
    public static String translateToSQL(Count count, DataMapper mapper, Object[] args) {

        String sql = "SELECT COUNT(*) FROM \"" + mapper.table() + "\"";

        if (!count.condition().isEmpty()) {
            sql += " WHERE " + count.condition();
        }

        return replaceWithArgs(sql, args);
    }

    /**
     * Translate the select annotation to a SQL string
     *
     * @param count Select annotation
     * @param args Method arguments
     * @return SQL string
     */
    public static String translateToSQL(Select select, DataMapper mapper, Object[] args) {
        String fields = "*";
        if (select.fields().length > 0) {
            fields = "(" + generateFromArray(select.fields()) + ")";
        }
        String sql = "SELECT " + fields + " FROM \"" + mapper.table() + "\" ";

        if (!select.condition().isEmpty()) {
            sql += "WHERE " + select.condition();
        }

        return replaceWithArgs(sql, args);
    }

    /**
     * Translate the insert annotation to a SQL string
     *
     * @param count Insert annotation
     * @param target Target object to insert
     * @return SQL string
     */
    public static String translateToSQL(Insert insert, DataMapper mapper, Object target) {
        Map<String, String> fields = MapperUtils.splitGettersToFields(target, mapper.foreignKeys());
        String sql = "INSERT INTO \"" + mapper.table() + "\" "
                + fieldsToSQL(mapper.primaryKey(), fields) + " VALUES "
                + "(" + valuesToSQL(mapper.primaryKey(), fields, true) + ")";

        return sql;
    }

    /**
     * Translate the update annotation to a SQL string
     *
     * @param count Update annotation
     * @param target Target object to insert
     * @param args Method arguments
     * @return SQL string
     */
    public static String translateToSQL(Update update, DataMapper mapper, Object target, Object[] args) {
        Map<String, String> fields = MapperUtils.splitGettersToFields(target, mapper.foreignKeys());
        String sql = "UPDATE \"" + mapper.table() + "\" SET "
                + generateSetString(fields);

        sql += generateWhereClause(update.condition(), target, mapper.primaryKey(), fields.get(mapper.primaryKey()));

        return sql;
    }

    /**
     * Translate the delete annotation to a SQL string
     *
     * @param count Delete annotation
     * @param target Target object to insert
     * @param args Method arguments
     * @return SQL string
     */
    public static String translateToSQL(Delete delete, DataMapper mapper, Object target, Object[] args) {
        String sql = "DELETE FROM \"" + mapper.table() + "\"";
        Map<String, String> fields = MapperUtils.splitGettersToFields(target, mapper.foreignKeys());

        sql += generateWhereClause(delete.condition(), target, mapper.primaryKey(), fields.get(mapper.primaryKey()));

        return replaceWithArgs(sql, args);
    }

    /**
     * Generates a string for a SQL update statement to set values.
     *
     * @param fields fields to set
     * @return a converted set string
     */
    public static String generateSetString(Map<String, String> fields) {
        return valuesToSQL("", fields, true, true);
    }

    /**
     * Scans a field map and converts it to a SQL string representation.
     *
     * @param primaryKey name of the primary key
     * @param fields fields to convert
     * @param valueBased determines if the result should contain values.
     * Otherwise it will contain keys.
     * @param set determines if the statement is for setting values or inserting
     * @return string representation of the values
     */
    public static String valuesToSQL(String primaryKey, Map<String, String> fields, boolean valueBased, boolean set) {
        String sql = "";
        
        primaryKey = primaryKey.trim();

        for (Entry<String, String> entry : fields.entrySet()) {
            
            if (!sql.isEmpty()) {
                sql += ",";
            }

            if (set) {
                sql += entry.getKey() + "=" + entry.getValue();
            } else {
                if (entry.getKey().equals(primaryKey) && valueBased) {
                    sql += "DEFAULT";
                } else {
                    if (valueBased) {
                        sql += entry.getValue();
                    } else {
                        sql += entry.getKey();
                    }
                }
            }
        }

        return sql;
    }

    /**
     * Scans a field map and converts it to a SQL string representation.
     *
     * @param primaryKey name of the primary key
     * @param fields fields to convert
     * @param valueBased determines if the result should contain values.
     * Otherwise it will contain keys.
     * @return string representation of the values
     */
    public static String valuesToSQL(String primaryKey, Map<String, String> fields, boolean valueBased) {
        return valuesToSQL(primaryKey, fields, valueBased, false);
    }

    /**
     * Scans a field map and converts it to a SQL string representation.
     *
     * @param primaryKey name of the primary key
     * @param fields fields to convert
     * @return string representation of the values
     */
    public static String valuesToSQL(String primaryKey, Map<String, String> fields) {
        return valuesToSQL(primaryKey, fields, true);
    }

    /**
     * Scans a field map and converts it to a SQL string representation.
     *
     * @param fields fields to convert
     * @return string representation of the values
     */
    public static String valuesToSQL(Map<String, String> fields) {
        return valuesToSQL("", fields, true);
    }

    /**
     * Converts a field map to a SQL field list
     *
     * @param primaryKey name of the primary key
     * @param fields fields to convert
     * @return string representation of the values
     */
    public static String fieldsToSQL(String primaryKey, Map<String, String> fields) {
        return "(" + valuesToSQL(primaryKey, fields, false) + ")";
    }

    /**
     * Check to see if a given method is annotated with the given annotation
     * 
     * @param method target method to check
     * @param annotation target annotation class to check
     * @return  true when there is an annotation in any of the classes
     */
    public static boolean isImplementedMethodAnnotatedWith(Method method, Class<IgnoredMethod> annotation) {

        //Find all interfaces/classes that are extended/implemented **explicitly** by the class
        //the given method belongs to. "Explicitly" in this context means the classes/interfaces
        //that are listed in an extends/implements clause. I.e. don't expect Object to show up.
        @SuppressWarnings("rawtypes")
		Class[] clazzes = getAllInterfaces(method.getDeclaringClass());

        //For each of these classes, see if there is a method that looks exactly like this one and
        //is annotated with the given annotation
        for (Class<?> clazz : clazzes) {
            try {
                //Throws an exception if method not found.
                Method m = clazz.getDeclaredMethod(method.getName(), method.getParameterTypes());

                if (m.isAnnotationPresent(annotation)) {
                    return true;
                }
            } catch (Exception e) {
                /*Do nothing*/
            }
        }

        return false;
    }

    // ============================================================================
    // Helper methods
    // ============================================================================
    
    // Returns all interfaces of the class
    @SuppressWarnings("rawtypes")
	private static Class[] getAllInterfaces(Class<?> clazz) {
        return getAllInterfaces(new Class[]{clazz});
    }

    //This method walks up the inheritance hierarchy to make sure we get every class/interface that could
    //possibly contain the declaration of the annotated method we're looking for.
    @SuppressWarnings("rawtypes")
	private static Class[] getAllInterfaces(Class[] classes) {
        if (0 == classes.length) {
            return classes;
        } else {
            List<Class> extendedClasses = new ArrayList<Class>();
            for (Class<?> clazz : classes) {
                extendedClasses.addAll(Arrays.asList(clazz.getInterfaces()));
            }
            //Class::getInterfaces() gets only interfaces/classes implemented/extended directly by a given class.
            //We need to walk the whole way up the tree.
            return (Class[]) addAllClasses(classes,
                    getAllInterfaces(
                    extendedClasses.toArray(new Class[extendedClasses.size()])));
        }
    }

    // Adds all classes from array A to array B
    @SuppressWarnings("rawtypes")
	private static Object[] addAllClasses(Class[] A, Class[] B) {
        int aLen = A.length;
        int bLen = B.length;
        Object[] C = new Class[aLen + bLen];
        System.arraycopy(A, 0, C, 0, aLen);
        System.arraycopy(B, 0, C, aLen, bLen);
        return C;
    }

    // Replaces prepared SQL with real data
    private static String replaceWithArgs(String raw, Object[] args) {

        if (args != null) {
            for (int argIndex = 0; argIndex < args.length; ++argIndex) {
                Object obj = args[argIndex];
                String value = String.valueOf(obj);

                if (obj instanceof String) {
                    value = "'" + value + "'";
                }
                raw = raw.replace("$" + (argIndex + 1), value);
            }
        }

        return raw;
    }

    // Generates a field string from array
    private static String generateFromArray(String[] fields) {
        String result = "";
        boolean first = true;
        for (String element : fields) {

            if (!first) {
                result += ", ";
            }

            result += "\"" + element + "\"";
            first = false;
        }
        return result;
    }

    // Generates a where clause for updates and deletes
    private static String generateWhereClause(String condition, Object target, String primaryKey, String value) {
        String sql = "";

        boolean whereAdded = false;
        if (!primaryKey.isEmpty() && target != null && value != null) {
            sql += " WHERE " + primaryKey + "=" + value;
            whereAdded = true;
        }

        if (!condition.isEmpty()) {
            if (!whereAdded) {
                sql += " WHERE ";
            } else {
                sql += " AND ";
            }

            sql += condition;
        }

        return sql;
    }
}
