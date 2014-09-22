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
import java.util.HashMap;
import java.util.Map;

import de.bitbrain.jpersis.JPersisException;

/**
 * Stores method annotations
 *
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
public class MethodPool {
	
	private Map<Class<?>,Class<?> > map = new HashMap<>();

	public void register(Class<? extends Annotation> annotation, Class<? extends MapperMethod<?>> method) {
		map.put(annotation, method);
	}
	
	@SuppressWarnings("unchecked")
	public Class<? extends MapperMethod<?>> get(Class<? extends Annotation> annotation) {
		return (Class<? extends MapperMethod<?>>) map.get(annotation);
	}
	
	public Annotation getSupported(Method method) {
		
		for (Annotation annotation : method.getAnnotations()) {
			if (map.containsKey(annotation.annotationType())) {
				return annotation;
			}
		}
		throw new JPersisException(method + " does not provide any supported annotations");
	}
}
