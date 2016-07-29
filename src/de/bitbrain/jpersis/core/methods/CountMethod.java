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
package de.bitbrain.jpersis.core.methods;

import de.bitbrain.jpersis.annotations.Count;
import de.bitbrain.jpersis.drivers.Query;

/**
 * Count implementation of {@link MapperMethod}
 *
 * @author Miguel Gonzalez Miguel Gonzalez Sanchez
 * @since 1.0
 * @version 1.0
 */
public class CountMethod extends AbstractMapperMethod<Count> {

  public CountMethod(Count count) {
    super(count);
  }

  @Override
  public void on(Class<?> model, Object[] params, Query query) {
    Count a = getAnnotation();
    query.count();
    if (!a.condition().isEmpty()) {
      query.condition(a.condition(), params);
    }
  }

  @Override
  protected Class<?>[] supportedReturnTypes(Class<?> model) {
    return new Class<?>[] { Integer.class, int.class };
  }
}