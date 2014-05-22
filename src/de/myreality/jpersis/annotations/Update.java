package de.myreality.jpersis.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Update Statement for a data mapper. 
 * <p>
 * Use this annotation to mark a method as an update method. To do so, there are
 * a few possibilities to mark a method properly. Basically, the return value
 * of a count method needs to be always <code>boolean</code> or <code>Boolean</code>.
 * <p>To update a row of a given table:<p>
 * <code>@Update(table = "my_table", primaryKey = "id")
 * <br/>boolean updateModel(Type model);
 * <p>To update only specific fields of the given object:<p>
 * <code>@Update(table = "my_table, primaryKey = "id", fields = {"field1", "field2"})
 * <br/>boolean updateOnlySpecificFields(Type model);</code>
 * <p>To update many objects with specific fields and a given condition:<p>
 * <code>@Update(table = "my_table", primaryKey = "id", fields = {"field"}, condition = "field=$1")
 * <br/>boolean updateObjectsWithCondition(Collection&#60;Type&#62; models, int conditionField);</code>
 * 
 * 
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Update {
    
    String[] fields() default {};
    
    String condition() default "";
}
