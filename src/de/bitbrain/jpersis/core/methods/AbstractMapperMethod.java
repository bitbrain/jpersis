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
package de.bitbrain.jpersis.core.methods;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import de.bitbrain.jpersis.JPersisException;
import de.bitbrain.jpersis.drivers.Driver;
import de.bitbrain.jpersis.drivers.Query;
import de.bitbrain.jpersis.util.Naming;

public abstract class AbstractMapperMethod<T extends Annotation> implements MapperMethod<T> {
	
	private T annotation;

	public AbstractMapperMethod(T annotation) {
		this.annotation = annotation;
	}
	
	public T getAnnotation() {
		return annotation;
	}
	
	@Override
	public Object execute(Method method, Class<?> model, Object[] args, Driver driver, Naming naming) {
		
		if (!validateArgs(args, model)) {
			throw new JPersisException("Arguments are not supported.");
		}
		if (!validateReturnType(method.getReturnType(), model)) {
			throw new JPersisException("Return type " + method.getReturnType() + " is not allowed.");
		}
		
		Query query = driver.query(model, naming);
		on(model, args, query);
		driver.connect();
		Object result = query.commit();
		driver.close();
		return result;
	}
	
	protected abstract void on(Class<?> model, Object[] params, Query query);
	
	protected boolean validateArgs(Object[] args, Class<?> model) {
		return true;
	}
	
	protected Class<?>[] supportedReturnTypes(Class<?> model) {
		return new Class<?>[]{Object.class};
	}
	
	private boolean validateReturnType(Class<?> type, Class<?> model) {
		Class<?>[] types = supportedReturnTypes(model);
		for (Class<?> c : types) {
			if (c.equals(type)) {
				return true;
			}
		}
		return false;
	}
}