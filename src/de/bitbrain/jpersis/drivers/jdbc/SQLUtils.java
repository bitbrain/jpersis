/**
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
package de.bitbrain.jpersis.drivers.jdbc;

import java.lang.reflect.Field;
import java.util.Date;

import de.bitbrain.jpersis.JPersisException;
import de.bitbrain.jpersis.annotations.Ignored;
import de.bitbrain.jpersis.annotations.PrimaryKey;
import de.bitbrain.jpersis.util.Naming;

/**
 * Utility class for SQL operations
 * 
 * @author Miguel Gonzalez
 * @since 1.0
 * @version 1.0
 */
public final class SQLUtils {

  /**
   * Generates an SQL string including braces.
   * 
   * @param model
   * @param naming
   * @return
   */
  public static String generateTableString(Class<?> model, Naming naming) {
    String r = "(";
    Field[] fields = model.getDeclaredFields();
    boolean primaryKeyFound = false;
    for (int i = 0; i < fields.length; ++i) {
      Field f = fields[i];
      boolean accessable = f.isAccessible();
      f.setAccessible(true);
      // Ignore annotated fields which have the @Ignored annotation
      if (f.getAnnotation(Ignored.class) != null) {
        continue;
      }
      String name = naming.javaToField(f.getName());
      r += "`" + name + "` " + convertDatatype(f.getType());
      // Add primary key information
      PrimaryKey pKey = f.getAnnotation(PrimaryKey.class);
      
      if (pKey != null) {
        if (primaryKeyFound) {
          throw new JPersisException(model.getName() + " defines multiple primary keys!");
        }
        primaryKeyFound = true;       
        if (pKey.value()) {
          r += " " + SQL.PRIMARY_KEY + " " + SQL.AUTO_INCREMENT;
        }
      }
      if (i < fields.length - 1) {
        r += ",";
      }
      f.setAccessible(accessable);
    }
    if (!primaryKeyFound) {
      throw new JPersisException(model.getName() + " does not define a primary key");
    }
    return r + ")";
  }
  
  public static String generatePreparedConditionString(Object object, Naming naming) {
    return generatePreparedConditionString(object, naming, SQL.AND);
  }

  public static String generatePreparedConditionString(Object object, Naming naming, String div) {
    Field[] fields = object.getClass().getDeclaredFields();
    String condition = "";
    int index = 0;
    for (int i = 0; i < fields.length; ++i) {
      Field f = fields[i];
      if (!f.isAnnotationPresent(PrimaryKey.class) || !f.getAnnotation(PrimaryKey.class).value()) {
	      condition += "`" + naming.javaToField(f.getName()) + "`=$" + (index++ + 1);
	      if (i < fields.length - 1) {
	        condition += div;
	      }
      }
    }
    return condition;
  }

  public static String generatePrimaryKeyCondition(Field primaryKey, Naming naming) {
    return "`" + naming.javaToField(primaryKey.getName()) + "`=$1";
  }

  public static String convertDatatype(Class<?> type) {
    if (type.isEnum()) {
      return SQL.ENUM;
      // Date
    } else if (type.equals(Date.class) || type.equals(java.sql.Date.class)) {
      return SQL.DATETIME;
      // Integer
    } else if (type.equals(Integer.TYPE)) {
      return SQL.INTEGER;
      // Boolean
    } else if (type.equals(Boolean.TYPE)) {
      return SQL.BOOL;
      // Long
    } else if (type.equals(Long.TYPE)) {
      return SQL.LONG;
      // Float
    } else if (type.equals(Float.TYPE)) {
      return SQL.FLOAT;
      // Double
    } else if (type.equals(Double.TYPE)) {
      return SQL.DOUBLE;
      // String
    } else if (type.equals(String.class)) {
      return SQL.VARCHAR;
      // Char
    } else if (type.equals(Character.TYPE)) {
      return SQL.CHAR;
    } else {
      throw new JPersisException("Type " + type.getName() + " is not supported by JPersis");
    }
  }

  /**
   * 
   * 
   * @param condition
   * @param args
   * @param naming
   * @return
   */
  public static String generateConditionString(String condition, Object[] values) {	  
	for (int i = 0; values != null &&  i < values.length; ++i) {
		Object value = values[i];
		condition = condition.replace("$" + (i + 1), typeToString(value));
	}
    return condition;
  }
  
  public static String typeToString(Object o) {
	  if (o instanceof String) {
		  return "\"" + (String)o + "\"";
	  } else return String.valueOf(o);
  }
  
  public static String generateFieldString(Object o, Naming naming, boolean ignorePrimaryKey) {
	  Field[] fields = o.getClass().getDeclaredFields();
	  String s = "(";
	  int index  = 0;
	  for (Field f : fields) {
		  if (ignorePrimaryKey && f.isAnnotationPresent(PrimaryKey.class)) {
			  continue;
		  } else {
			  s += "`" + naming.javaToField(f.getName()) + "`";
			  if (index++ < fields.length - 2) {
				  s += ",";
			  }
		  }
	  }
	  return s + ")";
  }
  
  public static String generateCommaString(Object ... collection) {
	  String s = "(";
	  int i = 0;
	  for (Object o : collection) {
		  s += typeToString(o);
		  if (i++ < collection.length - 1) {
			  s += ",";
		  }
	  }
	  return s + ")";
  }
}