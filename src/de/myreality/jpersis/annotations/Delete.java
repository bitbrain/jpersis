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
 * Delete Statement for a data mapper.
 * <p>
 * Use this annotation to mark a method as a delete method. To do so, there are
 * a few possibilities to mark a method properly. Basically, the return value of
 * a count method needs to be always
 * <code>boolean</code> or
 * <code>Boolean</code>.
 * <p>
 * To delete all rows of a given table:
 * <p>
 * <code><pre>
 *
 * @Delete boolean delete();
 * </pre></code><p>
 * Will be translated internally into:
 * <p>
 * <code>DELETE FROM "my_table"</code>
 * <p>
 * Where "my_table" is the name of your table, defined in the
 * {@link DataMapper}<p>
 * To count with conditions, you can use any parameter and insert them in the
 * condition by referring to them with numbers:
 * <p>
 * <code><pre>
 * @Delete(condition = "attribute1=$1 AND attribute2 = $2") boolean delete(int
 * anyNumber, String anyString);</pre></code><p>
 * Will be translated internally into:<p>
 * DELETE FROM "my_table" WHERE attribute1=valueOfAnyNumber AND attribute2 =
 * 'ValueOfAnyString'
 * <p>
 * You can delete objects which match the primary key of your tupel in the
 * table:
 * <p>
 * <code><pre>@Delete
 * boolean deleteThisModel(Type model);</pre></code><p>
 * or<p>
 * <code><pre>@Delete
 * boolean deleteThisModel(Collection&#60;Type&#62; model);</pre></code><p>
 * or you can mix it with additional conditions:<p>
 * <code><pre>
 * @Delete(conditions = "attribute = $2")
 * boolean deleteManyModels(Collection&#60;Type&#62; models, int
 * anyNymber);
 * </pre></code>
 * <p>
 * It is important to use always only objects types (except the native objects)
 * which are the same type as the defined model in the {@link DataMapper}.
 * </code>
 *
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Delete {

    String condition() default "";
}
