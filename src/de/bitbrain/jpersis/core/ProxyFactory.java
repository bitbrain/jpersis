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

import java.lang.annotation.Annotation;

import de.bitbrain.jpersis.JPersisException;
import de.bitbrain.jpersis.annotations.Mapper;
import de.bitbrain.jpersis.core.methods.MethodFactory;
import de.bitbrain.jpersis.drivers.DriverProvider;
import de.bitbrain.jpersis.util.NamingProvider;

/**
 * Factory which creates mapper proxies
 *
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
public class ProxyFactory<T> {
	
	private MethodFactory factory;
	
	private Class<T> mapper;
	
	private DriverProvider driverProvider;
	
	private NamingProvider namingProvider;
	
	public ProxyFactory(Class<T> mapper, DriverProvider driverProvider, MethodFactory factory, NamingProvider namingProvider) {
		this.factory = factory;
		this.mapper = mapper;
		this.driverProvider = driverProvider;
		this.namingProvider = namingProvider;
	}
	
	private Class<?> getModelClass(Class<?> mapperClass) {
		for (Annotation a : mapperClass.getAnnotations()) {
			if (a.annotationType().equals(Mapper.class)) {
				Mapper m = (Mapper)a;
				try {					
					return Class.forName(m.value());
				} catch (ClassNotFoundException e) {
					throw new JPersisException(" Could not find model: " + m.value());
				}
			}
		}
		throw new JPersisException("Could not retrieve model for " + mapperClass);
	}
	
	public T create() {
		final Proxy<T> mapperProxy = new Proxy<T>(getModelClass(mapper), driverProvider, factory, namingProvider.getNaming());
        return newInstance(mapperProxy);
    }

    @SuppressWarnings("unchecked")
	protected T newInstance(Proxy<T> mapperProxy) {
        return (T) java.lang.reflect.Proxy.newProxyInstance(mapper.getClassLoader(), new Class[]{mapper}, mapperProxy);
    }
}
