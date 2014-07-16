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
 * Lower case implementation of {@see Converter}. 
 * <p>
 * This converter converts Java standard CamelCase to lowe case like:
 * <code>MySpecialValue -> my_special_value</code> and vise versa:
 * <code my_special_value -> MySpecialValue</code>
 * 
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
public class LowerCaseConverter implements Converter {

	@Override
	public String toDatabaseFormat(String javaFormat) {
        String regex = "([a-z])([A-Z])";
        String replacement = "$1_$2";
        return javaFormat.replaceAll(regex, replacement).toLowerCase();
	}

	@Override
	public String toJavaFormat(String databaseFormat) {
		String[] parts = databaseFormat.split("_");
        String camelCaseString = "";
        for (String part : parts) {
            camelCaseString = camelCaseString + toProperCase(part);
        }
        return camelCaseString;
	}

    /**
     * Converts a target string to proper case
     *
     * @param s target string
     * @return proper cased string
     */
    private static String toProperCase(String s) {
        return s.substring(0, 1).toUpperCase()
                + s.substring(1).toLowerCase();
    }

}
