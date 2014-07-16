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
package de.myreality.jpersis.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Select Statement for a data mapper.
 * <p>
 * Use this annotation to mark a method as a select method. To do so, there are
 * a few possibilities to mark a method properly. Basically, the return value of
 * a count method needs to be always
 * <code>Type</code> or
 * <code>Collection<Type></code>, where
 * <code>Type</code> is the predefined model class in the {@link DataMapper}.
 * <p>To select all rows in a table, write:<p>
 * <code><pre>
 * @Select
 * Collection&#60;Type&#62; findAll();</pre></code>
 * <p>To fetch only the first object, e.g. for getting an object by primary key,
 * write:<p>
 * <code><pre>
 * @Select(condition = "id=$1")
 * Type findById(int id);</pre></code>
 * <p>You can search for multiple objects with a given condition:<p>
 * <code><pre>
 * @Select(condition = "name=$1)
 * Collection&#60;Type&#62; findAllWithName(String name);</pre></code>
 *
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Select {

    String condition() default "";

    String[] fields() default {};
}
