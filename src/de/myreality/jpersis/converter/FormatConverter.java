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
 * Converter between Database and Java 
 * 
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
public interface FormatConverter {

	/**
	 * Converts a Java format string to a well-formed database format
	 * 
	 * @param javaFormat java format to convert
	 * @return the converted result
	 */
	String toDatabaseFormat(String javaFormat);
	
	/**
	 * Converts a database string to a well-formed Java format
	 * 
	 * @param databaseFormat database format to convert
	 * @return the converted result
	 */
	String toJavaFormat(String databaseFormat);
}
