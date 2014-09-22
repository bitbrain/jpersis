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
package de.bitbrain.jpersis.core;

import java.util.HashMap;
import java.util.Map;

import de.bitbrain.jpersis.core.methods.MethodFactory;
import de.bitbrain.jpersis.drivers.Driver;

/**
 * Simple implementation of {@see MapperManager}
 *
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
public class SimpleMapperManager implements MapperManager {
	
	private Driver driver;
	
    private final Map<Class<?>, ProxyFactory<?> > factories;
    
    private MethodFactory factory;
	
	public SimpleMapperManager(Driver driver, MethodFactory factory) {
		factories = new HashMap<Class<?>, ProxyFactory<?>>();
		this.driver = driver;
		this.factory = factory;
	}

	@Override
	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	@Override
	public Driver getDriver() {
		return driver;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> mapper) {
		final ProxyFactory<T> factory = (ProxyFactory<T>) factories.get(mapper);
		return factory.create();
	}

	@Override
	public <T> void add(Class<T> mapper) {
		factories.put(mapper, new ProxyFactory<T>(mapper, this, factory));
	}

	@Override
	public boolean contains(Class<?> mapper) {
		return mapper != null && factories.containsKey(mapper);
	}
}
