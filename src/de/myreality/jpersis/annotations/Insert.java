package de.myreality.jpersis.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Insert Statement for a data mapper.
 * <p>
 * Use this annotation to mark a method as an insert method. To do so, there are
 * a few possibilities to mark a method properly. Basically, the return value of
 * a count method needs to be always <code>boolean</code> or <code>Boolean</code>.
 * <p>
 * To insert a new row, write:
 * <p>
 * <code><pre>
 * @Insert
 * boolean insert(Type model);
 * </pre></code>
 * <p>
 * To insert multiple objects of a given model class, write:
 * <p>
 * <code><pre>
 * @Insert
 * boolean insertMany(Collection&#60;Type&#62; models);</pre></code>
 * <p>
 *
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Insert {
    
}
