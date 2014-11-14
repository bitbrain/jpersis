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
import java.util.Collection;

import de.bitbrain.jpersis.JPersisException;
import de.bitbrain.jpersis.drivers.Driver;
import de.bitbrain.jpersis.drivers.DriverException;
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

		if (args != null && args.length == 1 && args[0] != null && args[0] instanceof Collection) {
			Object last = null;
			Collection<?> c = (Collection<?>) args[0];
			if (c == null || c.isEmpty()) {
			  return Boolean.FALSE;
			}
			for (Object o : c) {
				last = executeInternally(method, model, driver, naming, o);
			}
			return last;
		} else {
			return executeInternally(method, model, driver, naming, args);
		}
	}
	
	private Object executeInternally(Method method, Class<?> model, Driver driver, Naming naming, Object ... args) {
		if (!validateArgs(args, model)) {
			throw new JPersisException("Arguments are not supported.");
		}
		if (!validateReturnType(method.getReturnType(), model)) {
		  Class<?>[] supported = supportedReturnTypes(model);
		  String message = "Return type " + method.getReturnType() + " is not allowed. Supported are: ";
		  for (int i = 0; i < supported.length; ++i) {
		    Class<?> c = supported[i];
		    message += i < supported.length - 1 ? c.getName() + ", " : c.getName();		    
		  }		  
			throw new JPersisException(message);
		}
		if (driver == null) {
		  throw new JPersisException("No driver has been set.");
		}		
		try {
			driver.connect();
			Query query = driver.query(model, naming);
			on(model, args, query);
			Object result = driver.commit(query, method.getReturnType(), args, model, naming);
			driver.close();
			return result;
		} catch (DriverException e) {
			throw new JPersisException(e);
		}
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