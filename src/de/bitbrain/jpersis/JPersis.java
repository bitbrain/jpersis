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

import de.bitbrain.jpersis.annotations.Mapper;
import de.bitbrain.jpersis.core.MapperManager;
import de.bitbrain.jpersis.core.SimpleMapperManager;
import de.bitbrain.jpersis.drivers.Driver;

/**
 * JPersis main class which provides mapper creation and database interaction
 *
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
public final class JPersis {
	
	private MapperManager manager;

	/**
	 * Constructor for a new JPersis object
	 * 
	 * @param driver database driver
	 */
	public JPersis(Driver driver) {
		manager = new SimpleMapperManager(driver);
	}

	/**
	 * Provides data mappers for further usage. If the given class is not
	 * annotated with {@see Mapper} or the model of the mapper can not be found,
	 * an {@see JPersisException} is thrown.
	 * 
	 * @param mapper
	 *            given class or interface of a mapper
	 * @return mapper instance of the given class or interface
	 */
	public <T> T map(Class<T> mapper) {
		validate(mapper);
		if (!manager.contains(mapper)) {
			manager.add(mapper);
		}
		return manager.get(mapper);
	}

	/**
	 * Sets a new database driver. This method clears the current context and
	 * all associated models.
	 * 
	 * @param driver
	 *            database driver
	 */
	public void setDriver(Driver driver) {
		manager.setDriver(driver);
	}

	private void validate(Class<?> mapper) {
		if (!mapper.isInterface()) {
			throw new JPersisException("Only interfaces can be a Mapper");
		} else if (mapper.getAnnotation(Mapper.class) == null) {
			throw new JPersisException(mapper + " must be annotated with Mapper annotation.");
		}
	}
}
