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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import de.bitbrain.jpersis.JPersis;
import de.bitbrain.jpersis.mocks.MapperMock;
import de.bitbrain.jpersis.mocks.ModelMock;

@RunWith(MockitoJUnitRunner.class)
public class AnnotationsTest {

  static final String DB = "temp.sql";

  JPersis manager = new JPersis();

  MapperMock mapper;

  @Before
  public void beforeTest() {

    createDatabase();
    //mapper = manager.map(MapperMock.class);
  }

  @After
  public void afterTest() {
    deleteDatabase();
  }

  @Test
  public void testInsert() {

    final int RUNS = 1000;

    for (int i = 0; i < RUNS; ++i) {
      ModelMock m = new ModelMock();
      m.setName("Max");
      m.setLastName("Mustermann");
      assertTrue("It should be possible to insert element nr" + i, mapper.insert(m));
      assertFalse("It should not be possible to insert the same object twice", mapper.insert(m));
      assertTrue("There should be " + (i + 1) + " elements.", mapper.count() == (i + 1));
    }
  }

  @Test
  public void testUpdate() {
    ModelMock m1 = new ModelMock();

    m1.setName("Hans");
    m1.setLastName("Kramer");
    mapper.insert(m1);

    m1.setName("Wilfred");
    mapper.update(m1);

    ModelMock updated = mapper.findById(m1.getId());
    assertTrue("It should have the same ID", updated.getId() == m1.getId());
    assertFalse("Old and new object should not be the same", m1.equals(updated));
    assertTrue("It should an updated name", "Wilfred".equals("Wilfred"));
  }

  @Test
  public void testDelete() {
    final int RUNS = 1000;

    for (int i = 0; i < RUNS; ++i) {
      ModelMock m = new ModelMock();
      m.setName("Max");
      m.setLastName("Mustermann");
      assertTrue("It should be possible to insert element nr" + i, mapper.insert(m));
      assertTrue("There should be " + (i + 1) + " elements.", mapper.count() == 1);
      assertTrue("It should be possible to delete object nr" + i, mapper.delete(m));
      assertTrue("It should not be possible to delete the same object twice", mapper.delete(m));
      assertTrue("There should be " + (i + 1) + " elements.", mapper.count() == 0);
    }
  }

  @Test
  public void testFindById() {
    final int RUNS = 1000;

    for (int i = 0; i < RUNS; ++i) {
      ModelMock m = new ModelMock();
      m.setName("Max");
      m.setLastName("Mustermann");
      mapper.insert(m);
    }
    
    Collection<ModelMock> mocks = mapper.findAll();
    assertTrue("There are not enough mocks to find", mocks.size() == RUNS);
    for (ModelMock m : mocks) {
      ModelMock found = mapper.findById(m.getId());
      assertTrue("The objects should be the same", m.equals(found));
    }
  }

  @Test
  public void testFindAll() {
    for (int i = 0; i < 200; i++) {
      ModelMock m = new ModelMock();
      m.setName("Maximilian");
      m.setLastName("Wutang");
      mapper.insert(m);
    }
    for (int i = 0; i < 300; i++) {
      ModelMock m = new ModelMock();
      m.setName("Johannes");
      m.setLastName("Wutang");
      mapper.insert(m);
    }
    for (int i = 0; i < 50; i++) {
      ModelMock m = new ModelMock();
      m.setName("Sebastian");
      m.setLastName("Walter");
      mapper.insert(m);
    }
    Collection<ModelMock> mocks1 = mapper.findAllByName("Maximilian");
    assertTrue("There are not enough models1", mocks1.size() == 200);
    
    Collection<ModelMock> mocks2 = mapper.findAllByName("Johannes");
    assertTrue("There are not enough models2", mocks2.size() == 300);
    
    Collection<ModelMock> mocks3 = mapper.findAllByName("Sebastian");
    assertTrue("There are not enough models3", mocks3.size() == 50);
  }

  private void createDatabase() {

  }

  private void deleteDatabase() {

  }
}
