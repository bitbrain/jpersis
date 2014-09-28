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
package de.bitbrain.jpersis.drivers;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract implementation of {@link Driver}
 * 
 * @author Miguel Gonzalez
 * @since 1.0
 * @version 1.0
 */
public abstract class AbstractDriver implements Driver {
	
	private Map<Class<?>, Query> queries;
	
	public AbstractDriver() {
		queries = new HashMap<Class<?>, Query>();

	}

	@Override
	public final Query query(Class<?> model) {
		Query query = queries.get(model);
		if (query  == null) {
			query = createQuery(model);
			queries.put(model, query);
		}		
		return query;
	}
	
	/**
	 * Let the child decide which query to create
	 * 
	 * @param model model of the query
	 * @return newly created query
	 */
	protected abstract Query createQuery(Class<?> model);
}
