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
package de.bitbrain.jpersis.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import de.bitbrain.jpersis.JPersisException;
import de.bitbrain.jpersis.annotations.PrimaryKey;

/**
 * This extractor takes care of objects and extracts their fields
 *
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
public final class FieldExtractor {

  public static Object[] extractFieldValues(Object object) {
    List<Object> values = new ArrayList<Object>();
    for (Field f : object.getClass().getDeclaredFields()) {
      if (Modifier.isStatic(f.getModifiers())) {
        continue;
      }
      boolean accessable = f.isAccessible();
      f.setAccessible(true);
      try {
        if (f.isAnnotationPresent(PrimaryKey.class) && f.getAnnotation(PrimaryKey.class).value()) {
          continue;
        } else {
          values.add(f.get(object));
        }
      } catch (IllegalArgumentException | IllegalAccessException e) {
        throw new JPersisException(e);
      } finally {
        f.setAccessible(accessable);
      }
    }
    return values.toArray(new Object[values.size()]);
  }

  public static Field extractPrimaryKey(Object object) {
    for (Field f : object.getClass().getDeclaredFields()) {
      if (Modifier.isStatic(f.getModifiers())) {
        continue;
      }
      boolean accessable = f.isAccessible();
      f.setAccessible(true);
      try {
        if (f.isAnnotationPresent(PrimaryKey.class)) {
          return f;
        }
      } finally {
        f.setAccessible(accessable);
      }
    }
    return null;
  }

  public static Object extractPrimaryKeyValue(Object object) {
    Field f = extractPrimaryKey(object);
    if (f != null) {
      boolean accessable = f.isAccessible();
      try {
        f.setAccessible(true);
        return f.get(object);
      } catch (IllegalArgumentException | IllegalAccessException e) {
        throw new JPersisException(e);
      } finally {
        f.setAccessible(accessable);
      }
    } else {
      throw new JPersisException(object.getClass() + " has no primary key");
    }
  }
}
