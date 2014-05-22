package de.myreality.jpersis.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Is used to mark interfaces as a data mapper.
 * <p>
 * You need to set the following attributes in the annotation:
 * <ul>
 * <li>model: Path to the target model class</li>
 * <li>table: Name of the table in the database</li>
 * <li>primaryKey: Name of the primary key in the table</li>
 * <li>foreignKeys: An array of foreign keys inside of the table (Optional)</li>
 * </ul>
 * <p>
 * A data mapper can be used by the mapper manager to make queries to a
 * database, e.g.:
 * <p>
 * <code>MyMapper mapper = MapperManager.getInstance().getMapper(MyMapper.class);</code>
 * <p>
 * To mark an interface as a mapper, simply add this annotation above the interface. 
 * After that you can write your own query methods by using {@link Select}, 
 * {@link Update}, {@link Insert}, {@link Count} ands {@link Delete}
 * annotations.
 * <p>
 * The referenced model class needs to be a plain java bean. The getters and
 * setters are translated to database fields and vise versa:
 * <p>
 * <ul>
 * <li><code>Type getUserName()</code> will be translated to <code>user_name</code> where
 *     the Type has to be the same as in the database field</li>
 * <li><code>user_name</code> will be translated to <code>setUserName(Type value)</code>
 *     where the parameter Type has to be the same type as in the database field</li>
 * </ul>
 * <p>
 * If there is a field in a table but no getter or setter for it in the model, a {@link nl.fontys.logistica.mapper.MapperException}
 * will be thrown.
 *
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataMapper {

    /**
     * Path to the model class. That class needs to be a java bean.
     */
    String model();
    
    String table();
    
    String primaryKey();
    
    String[] foreignKeys() default {};
}
