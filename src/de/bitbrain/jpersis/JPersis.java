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

import de.bitbrain.jpersis.annotations.Mapper;
import de.bitbrain.jpersis.annotations.Select;
import de.bitbrain.jpersis.core.MapperManager;
import de.bitbrain.jpersis.core.SimpleMapperManager;
import de.bitbrain.jpersis.core.methods.MapperMethod;
import de.bitbrain.jpersis.core.methods.MethodFactory;
import de.bitbrain.jpersis.core.methods.MethodPool;
import de.bitbrain.jpersis.core.methods.SelectMethod;
import de.bitbrain.jpersis.drivers.Driver;
import de.bitbrain.jpersis.util.Naming;
import de.bitbrain.jpersis.util.NamingProvider;

/**
 * JPersis main class which provides mapper creation and database interaction
 *
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
public final class JPersis {
	
	private MapperManager manager;
	
	private MethodPool pool;
	
	private Naming naming = Naming.DEFAULT;

	/**
	 * Constructor for a new JPersis object
	 * 
	 * @param driver database driver
	 */
	public JPersis(Driver driver) {
		pool = new MethodPool();
		manager = new SimpleMapperManager(driver, new MethodFactory(pool), new NamingProvider() {
      @Override
      public Naming getNaming() {
        return naming;
      }		  
		});
		initDefaults();
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
	
	public void setNaming(Naming naming) {
	  this.naming = naming;
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
	
	/**
	 * Register a custom method and annotation
	 * 
	 * @param annotation target expected annotation
	 * @param method 
	 */
	public void register(Class<? extends Annotation> annotation, Class<? extends MapperMethod<?> > method) {
		pool.register(annotation, method);
	}
	
	private void initDefaults() {
		register(Select.class, SelectMethod.class);
	}

	private void validate(Class<?> mapper) {
		if (!mapper.isInterface()) {
			throw new JPersisException("Only interfaces can be a Mapper");
		} else if (mapper.getAnnotation(Mapper.class) == null) {
			throw new JPersisException(mapper + " must be annotated with Mapper annotation.");
		}
	}
}
