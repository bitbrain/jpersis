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

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import de.bitbrain.jpersis.core.methods.MapperMethod;
import de.bitbrain.jpersis.core.methods.MethodFactory;
import de.bitbrain.jpersis.drivers.DriverProvider;
import de.bitbrain.jpersis.util.Naming;

/**
 * Proxy for mapper interfaces
 *
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
public class Proxy<T> implements InvocationHandler, Serializable {

	private static final long serialVersionUID = 886746435635193772L;
	
	private final Map<Method, MapperMethod<?> > cache;
	
	private DriverProvider driverProvider;
	
	private MethodFactory factory;
	
	private Class<?> model;
	
	private Naming naming;
	
	public Proxy(Class<?> model, DriverProvider driverProvider, MethodFactory factory, Naming naming) {
		cache = new HashMap<>();
		this.factory = factory;
		this.driverProvider = driverProvider;
		this.model = model;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		if (valid(method)) {
			return getCached(method).execute(method, model, args, driverProvider.getDriver(), naming);
		} else {
			return method.invoke(this, args);
		}
	}
	
	private MapperMethod<?> getCached(Method method) {
		 MapperMethod<?> mapped = cache.get(method);
		 
		 if (mapped == null) {
			 mapped = factory.create(method);
			 cache.put(method, mapped);
		 }
		
		return mapped;
	}

	private boolean valid(Method method) {
		return !method.getClass().equals(Object.class);
	}
}
