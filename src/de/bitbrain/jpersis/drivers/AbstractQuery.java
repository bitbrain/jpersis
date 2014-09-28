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

/**
 * Abstract implementation of {@link Query}
 * 
 * @author Miguel Gonzalez
 * @since 1.0
 * @version 1.0
 */
public class AbstractQuery implements Query {

	@Override
	public Query condition(String condition, Object[] args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Query select() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Query update(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Query delete(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Query insert(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Query count() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Query limit(int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Query order(Order order) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object commit() {
		// TODO Auto-generated method stub
		return null;
	}
}