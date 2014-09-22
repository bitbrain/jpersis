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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import de.bitbrain.jpersis.JPersisException;

/**
 * Creates methods for queries
 *
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
public class MethodFactory {

	private MethodPool pool;

	public MethodFactory(MethodPool pool) {
		this.pool = pool;
	}

	public MapperMethod<?> create(Method method) {
		Annotation a = pool.getSupported(method);

		if (a != null) {
			Class<?> methodClass = pool.get(a.annotationType());
			try {
				Constructor<?> c = methodClass.getConstructor(a.annotationType());
				return (MapperMethod<?>) c.newInstance(a);
			} catch (NoSuchMethodException e) {
				throw new JPersisException(methodClass + " does not provide a valid constructor");
			} catch (SecurityException e) {
				throw new JPersisException(methodClass + " needs public methods and constructors");
			} catch (InstantiationException e) {
				throw new JPersisException("Could'nt instantiate " + methodClass + ": " + e.getMessage());
			} catch (IllegalAccessException e) {
				throw new JPersisException(methodClass + " needs public methods and constructors");
			} catch (IllegalArgumentException e) {
				throw new JPersisException(methodClass + " does not provide a valid constructor");
			} catch (InvocationTargetException e) {
				throw new JPersisException("Could'nt instantiate " + methodClass + ": " + e.getMessage());
			}
		} else {
			throw new JPersisException(method
					+ " does not provide any supported annotation");
		}
	}
}
