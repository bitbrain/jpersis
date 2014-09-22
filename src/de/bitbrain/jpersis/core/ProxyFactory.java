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

import de.bitbrain.jpersis.core.methods.MethodFactory;
import de.bitbrain.jpersis.drivers.DriverProvider;

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
	
	private DriverProvider provider;
	
	public ProxyFactory(Class<T> mapper, DriverProvider driverProvider, MethodFactory factory) {
		this.factory = factory;
		this.mapper = mapper;
		this.provider = driverProvider;
	}
	
	public T create() {
		final Proxy<T> mapperProxy = new Proxy<T>(provider, factory);
        return newInstance(mapperProxy);
    }

    @SuppressWarnings("unchecked")
	protected T newInstance(Proxy<T> mapperProxy) {
        return (T) java.lang.reflect.Proxy.newProxyInstance(mapper.getClassLoader(), new Class[]{mapper}, mapperProxy);
    }

}
