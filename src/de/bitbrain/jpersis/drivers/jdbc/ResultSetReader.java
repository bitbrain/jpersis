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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

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
	public Object read(ResultSet set, Class<?> returnType) throws SQLException {
		// Collections
		if (returnType.isAssignableFrom(Collection.class)) {
			ArrayList<?> coll = new ArrayList<Object>();
			return coll;
		// Integers
		} else if (returnType.isAssignableFrom(Integer.class) || returnType.isAssignableFrom(int.class)){
			while (set.next()) {
                return set.getInt(1);
            }
		// Boolean
		} else if (!returnType.isAssignableFrom(Boolean.class) && !returnType.isAssignableFrom(boolean.class)) {
			
		}
		return null;
	}
}
