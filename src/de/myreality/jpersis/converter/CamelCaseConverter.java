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
package de.myreality.jpersis.converter;

/**
 * Camel case implementation of {@see Converter}. 
 * <p>
 * This converter converts Java standard CamelCase to camel case like:
 * <code>MySpecialValue -> MySpecialValue</code> and vise versa:
 * <code MySpecialValue -> MySpecialValue</code>
 * 
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
public class CamelCaseConverter implements Converter {
	
	private LowerCaseConverter conv = new LowerCaseConverter();

	@Override
	public String toDatabaseFormat(String javaFormat) {
		return conv.toJavaFormat(javaFormat);
	}

	@Override
	public String toJavaFormat(String databaseFormat) {
		return conv.toJavaFormat(databaseFormat);
	}

}
