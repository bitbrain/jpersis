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
import de.bitbrain.jpersis.annotations.AnnotationUtils;
import de.bitbrain.jpersis.annotations.Count;
import de.bitbrain.jpersis.annotations.Delete;
import de.bitbrain.jpersis.annotations.Insert;
import de.bitbrain.jpersis.annotations.Select;
import de.bitbrain.jpersis.annotations.Update;

/**
 * Creates methods for queries
 *
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
public class MethodFactory {

	public MapperMethod create(Method method) {
		Annotation a = AnnotationUtils.getSupported(method);
		
		if (a != null) {
			Class<?> c = a.getClass();
			if (c.equals(Select.class))
				return new SelectMethod(a);
			if (c.equals(Count.class))
				throw new UnsupportedOperationException("Not supported!");
			if (c.equals(Update.class))
				throw new UnsupportedOperationException("Not supported!");
			if (c.equals(Delete.class))
				throw new UnsupportedOperationException("Not supported!");
			if (c.equals(Insert.class))
				throw new UnsupportedOperationException("Not supported!");			
			
			throw new JPersisException(method + " does not provide any supported annotation");
		} else {
			throw new JPersisException(method + " does not provide any supported annotation");
		}
	}
}
