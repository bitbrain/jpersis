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
import java.util.Collection;

import de.bitbrain.jpersis.drivers.Query;

/**
 * Basic implementation of {@see MapperMethod}
 *
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
public abstract class BasicMapperMethod<T extends Annotation> extends AbstractMapperMethod<T> {

	public BasicMapperMethod(T annotation) {
		super(annotation);
	}

	@Override
	public void on(Class<?> model, Object[] args, Query query) {
		Object arg = args[0];		
		if (arg.getClass().equals(model)) {
			action(arg, query);
		} else {
			Collection<?> collection = (Collection<?>)arg;
			for (Object o : collection) {
				action(o, query);
			}
		}
	}
	
	@Override
	protected boolean validateArgs(Object[] args, Class<?> model) {
		return args.length == 1 && 
				(args[0].getClass().equals(Collection.class) 
			 ||  args[0].getClass().equals(model));
	}
	
	@Override
	protected Class<?>[] supportedReturnTypes(Class<?> model) {
		return new Class<?>[]{Boolean.class, boolean.class};
	}
	
	protected abstract void action(Object object, Query query);
}