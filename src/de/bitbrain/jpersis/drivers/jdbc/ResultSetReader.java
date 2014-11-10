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
package de.bitbrain.jpersis.drivers.jdbc;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import de.bitbrain.jpersis.JPersisException;
import de.bitbrain.jpersis.util.FieldInvoker;
import de.bitbrain.jpersis.util.FieldInvoker.InvokeException;
import de.bitbrain.jpersis.util.Naming;

/**
 * SQL language implementation for a query
 * 
 * @author Miguel Gonzalez
 * @since 1.0
 * @version 1.0
 */
public class ResultSetReader {

	/**
	 * Reads a result set and converts it into a valid java object
	 * 
	 * @param set data to read
	 * @param returnType type to assign to
	 * @return new object of the return type
	 * @throws SQLException is thrown when something goes wrong
	 */
	public Object read(ResultSet set, Class<?> returnType, Class<?> model, Naming naming) throws SQLException {
		// Collections
		if (returnType.isAssignableFrom(Collection.class)) {
			ArrayList<Object> coll = new ArrayList<Object>();
			while (set.next()) {
				coll.add(readSingle(set, model, naming));
			}
			return coll;
		// Integers
		} else if (returnType.isAssignableFrom(Integer.class) || returnType.isAssignableFrom(int.class)){
			while (set.next()) {
                return set.getInt(1);
            }
		} else if (!returnType.isAssignableFrom(Boolean.class) && !returnType.isAssignableFrom(boolean.class)) {
			while (set.next()) { 
				return readSingle(set, returnType, naming);
			};
		}
		return null;
	}
	
	private Object readSingle(ResultSet set, Class<?> returnType, Naming naming) throws SQLException {
		// 1. Create new object
		try {
			Object o = returnType.getConstructor().newInstance();
			Field[] fields = returnType.getDeclaredFields();
			for (Field f : fields) {
				String value = set.getString(naming.javaToField(f.getName()));
				FieldInvoker.invoke(o, f, value);
			}
			return o;
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | InvokeException e) {
			throw new JPersisException(e);
		}
	}
}
