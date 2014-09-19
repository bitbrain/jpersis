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
package de.bitbrain.jpersis.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Count Statement for a data mapper. 
 * <p>
 * Use this annotation to mark a method as a count method. To do so, there are
 * a few possibilities to mark a method properly. Basically, the return value
 * of a count method needs to be always <code>int</code> or <code>Integer</code>.
 * <p>
 * To count all rows of a given table:
 * <p>
 * <code><pre>
 * @Count
 * int count();
 * </pre></code><p>
 * Will be translated internally into:
 * <p>
 * <code>SELECT COUNT(*) FROM "my_table"</code>
 * <p>
 * Where "my_table" is the name of your table, defined in the {@link DataMapper}<p>
 * To count with conditions, you can use any parameter and insert them in the 
 * condition by referring to them with numbers:
 * <p>
 * <code>@Count(table = "my_table", condition = "attribute1=$1 AND attribute2 = $2")
 * <br/>int count(int anyNumber, String anyString);</code><p>
 * Will be translated internally into:<p>
 * SELECT COUNT(*) FROM "my_table" WHERE attribute1=valueOfAnyNumber AND attribute2 = 'ValueOfAnyString'
 * 
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Count {
    
    String condition() default "";
}