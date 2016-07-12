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
package de.bitbrain.jpersis;

import de.bitbrain.jpersis.annotations.*;
import de.bitbrain.jpersis.core.MapperManager;
import de.bitbrain.jpersis.core.SimpleMapperManager;
import de.bitbrain.jpersis.core.methods.*;
import de.bitbrain.jpersis.drivers.Driver;
import de.bitbrain.jpersis.util.Naming;
import de.bitbrain.jpersis.util.NamingProvider;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;

import java.lang.annotation.Annotation;

/**
 * JPersis main class which provides mapper creation and database interaction
 *
 * @author Miguel Gonzalez Sanchez
 * @since 1.0
 * @version 1.0
 */
public final class JPersis {

  private MapperManager manager;

  private MethodPool pool;

  private Naming naming = Naming.DEFAULT;

  private ByteBuddy buddy;

  /**
   * Constructor for a new JPersis object
   * 
   * @param driver
   *          database driver
   */
  public JPersis(Driver driver) {
    buddy = new ByteBuddy();
    pool = new MethodPool();
    manager = new SimpleMapperManager(driver, new MethodFactory(pool), new NamingProvider() {
      @Override
      public Naming getNaming() {
        return naming;
      }
    });
    initDefaults();
  }

  /**
   * Provides data mappers for further usage. If the given class is not annotated with {@see Mapper} or the model of the
   * mapper can not be found, an {@see JPersisException} is thrown.
   * 
   * @param mapper
   *          given class or interface of a mapper
   * @return mapper instance of the given class or interface
   */
  public <T> T map(Class<T> mapper) {
    validate(mapper);
    if (!manager.contains(mapper)) {
      manager.add(mapper);
    }
    return manager.get(mapper);
  }
  
  /**
   * Provides data mapping for further usage. This returns a default implementation of a data mapper.
   *
   * @param modelClass the class of the model
   * @param <T> class type
   * @return default mapper
   */
  public <T> DefaultMapper<T> mapDefault(Class<T> modelClass) {
    TypeDescription.Generic generic = TypeDescription.Generic.Builder
            .parameterizedType(DefaultMapper.class, modelClass)
            .build();
    Builder<DefaultMapper<T> > builder = (Builder<DefaultMapper<T> >) buddy.makeInterface(generic);
    builder = builder.annotateType(
      AnnotationDescription.Builder.ofType(Mapper.class)
        .define("value", modelClass.getName())
        .build());
    return map(builder.make()
        .load(modelClass.getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
        .getLoaded());
  }

  public void setNaming(Naming naming) {
    this.naming = naming;
  }

  /**
   * Sets a new database driver. This method clears the current context and all associated models.
   * 
   * @param driver
   *          database driver
   */
  public void setDriver(Driver driver) {
    manager.setDriver(driver);
  }

  /**
   * Register a custom method and annotation
   * 
   * @param annotation
   *          target expected annotation
   * @param method
   */
  public void register(Class<? extends Annotation> annotation, Class<? extends MapperMethod<?>> method) {
    pool.register(annotation, method);
  }

  private void initDefaults() {
    register(Select.class, SelectMethod.class);
    register(Count.class, CountMethod.class);
    register(Insert.class, InsertMethod.class);
    register(Update.class, UpdateMethod.class);
    register(Delete.class, DeleteMethod.class);
  }

  private void validate(Class<?> mapper) {
    if (!mapper.isInterface()) {
      throw new JPersisException("Only interfaces can be a Mapper");
    } else if (mapper.getAnnotation(Mapper.class) == null) {
      throw new JPersisException(mapper + " must be annotated with Mapper annotation.");
    }
  }
}
