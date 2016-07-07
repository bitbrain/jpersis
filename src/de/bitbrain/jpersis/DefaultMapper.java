/*
 * Copyright 2016 miguel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.bitbrain.jpersis;

import de.bitbrain.jpersis.annotations.Count;
import de.bitbrain.jpersis.annotations.Delete;
import de.bitbrain.jpersis.annotations.Insert;
import de.bitbrain.jpersis.annotations.Select;
import de.bitbrain.jpersis.annotations.Update;
import java.util.Collection;

/**
 * Default mapper interface
 * 
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @param <T> class of the model to map
 */
public interface DefaultMapper<T> {
    
    @Insert
    void insert(T model);
    
    @Insert
    void insert(Collection<T> models);
    
    @Delete
    void delete(T model);
    
    @Delete
    void delete(Collection<T> models);
    
    @Count
    int count();
    
    @Select
    Collection<T> getAll();
    
    @Select(condition = "id = $1")
    T getById(int id);
    
    @Update
    void update(T model);
    
    @Update
    void update(Collection<T> models);
}
