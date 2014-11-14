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

import static de.bitbrain.jpersis.TravisCI.MYSQL_DATABASE;
import static de.bitbrain.jpersis.TravisCI.MYSQL_HOST;
import static de.bitbrain.jpersis.TravisCI.MYSQL_PASSWORD;
import static de.bitbrain.jpersis.TravisCI.MYSQL_PORT;
import static de.bitbrain.jpersis.TravisCI.MYSQL_USERNAME;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import de.bitbrain.jpersis.drivers.Driver;
import de.bitbrain.jpersis.drivers.DriverException;
import de.bitbrain.jpersis.drivers.mysql.MySQLDriver;
import de.bitbrain.jpersis.drivers.sqllite.SQLiteDriver;
import de.bitbrain.jpersis.mocks.MapperMock;
import de.bitbrain.jpersis.mocks.MinimalMapperMock;
import de.bitbrain.jpersis.mocks.MinimalMock;
import de.bitbrain.jpersis.mocks.ModelMock;

@RunWith(value = Parameterized.class)
public class JPersisTest {

  static final String DB = "temp.sql";

  JPersis manager;

  MapperMock mapper;
  
  MinimalMapperMock minimalMapper;
  
  @Parameter
  public Driver driver;
  
  @Parameters
  public static Collection<Driver[]> getParams() {
    List<Driver[]> infos = new ArrayList<Driver[]>();
    infos.add(new Driver[]{new SQLiteDriver(DB)});
    infos.add(new Driver[]{new MySQLDriver(MYSQL_HOST, MYSQL_PORT, MYSQL_DATABASE, MYSQL_USERNAME, MYSQL_PASSWORD)});
    return infos;
  }

  @Before
  public void beforeTest() throws IOException {
    manager = new JPersis(driver);
    mapper = manager.map(MapperMock.class);
    minimalMapper = manager.map(MinimalMapperMock.class);
    dropData();
  }

  @After
  public void afterTest() throws DriverException {
    dropData();
  }

  @Test
  public void testInsert() {

    final int RUNS = 5;

    for (int i = 0; i < RUNS; ++i) {
      ModelMock m = new ModelMock();
      m.setName("Max");
      m.setLastName("Mustermann");
      assertTrue("It should be possible to insert element nr" + i, mapper.insert(m));
      assertTrue("There should be " + (i + 1) + " elements.", mapper.count() == (i + 1));
      assertTrue("Primary key should be " + (i + 1) + " instead of " + m.getId(), m.getId() == i + 1);
    }

    for (int i = 0; i < RUNS; ++i) {
      MinimalMock m = new MinimalMock(); 
      m.setName("Meh");
      assertTrue("It should be possible to insert element nr" + i, minimalMapper.insert(m));
      assertTrue("There should be " + (i + 1) + " elements.", minimalMapper.count() == (i + 1));
      assertTrue("Primary key should be " + (i + 1) + " instead of " + m.getId(), m.getId() == i + 1);
    }
  }
  
  @Test
  public void testInsertCollection() {
	  
	  final int AMOUNT = 10;
	  
	  List<ModelMock> mocks = new ArrayList<ModelMock>();
	  
	  for (int i = 0; i < AMOUNT; ++i) {
		  ModelMock m = new ModelMock();
		  m.setName("Hans" + i);
		  m.setLastName("ImGlueck" + i);
		  mocks.add(m);
	  }
	  mapper.insert(mocks);
	  
	  for (int i = 0; i < AMOUNT; ++i) {
		  ModelMock m = mocks.get(i);
		  assertTrue("The id of " + m + " should be " + (i + 1), m.getId() == (i + 1));
	  }
	  
	  Collection<ModelMock> dbMocks = mapper.findAll();
	  assertTrue("There should be the same amount as inserted", dbMocks.size() == AMOUNT);
	  
	  int i = 0;
	  for (ModelMock m : dbMocks) {
		  assertTrue("The id of " + m + " should be " + (i + 1), m.getId() == (i + 1));
		  assertTrue("The name should be Hans" + i, m.getName().equals("Hans" + i));
		  assertTrue("The last name should be ImGlueck" + i, m.getLastName().equals("ImGlueck" + i));
		  i++;
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
    assertTrue("It should not be null", updated != null);
    assertTrue("It should have the same ID", updated.getId() == m1.getId());
    assertTrue("Old and new object should be the same", m1.equals(updated));
    assertTrue("It should be an updated name instead of " + updated.getName(), "Wilfred".equals(updated.getName()));
  }

  @Test
  public void testDelete() {
    final int RUNS = 5;
    for (int i = 0; i < RUNS; ++i) {
      ModelMock m = new ModelMock();
      m.setName("Max");
      m.setLastName("Mustermann");
      assertTrue("It should be possible to insert element nr" + i, mapper.insert(m));
      assertTrue("There should be " + (i + 1) + " elements.", mapper.count() == 1);
      assertTrue("It should be possible to delete object nr" + i, mapper.delete(m));
      assertTrue("There should be " + (i + 1) + " elements.", mapper.count() == 0);
    }
  }

  @Test
  public void testFindById() {
    final int RUNS = 5;
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
    for (int i = 0; i < 5; i++) {
      ModelMock m = new ModelMock();
      m.setName("Maximilian");
      m.setLastName("Wutang");
      mapper.insert(m);
    }
    for (int i = 0; i < 6; i++) {
      ModelMock m = new ModelMock();
      m.setName("Johannes");
      m.setLastName("Wutang");
      mapper.insert(m);
    }
    for (int i = 0; i < 7; i++) {
      ModelMock m = new ModelMock();
      m.setName("Sebastian");
      m.setLastName("Walter");
      mapper.insert(m);
    }
    Collection<ModelMock> mocks1 = mapper.findAllByName("Maximilian");
    assertTrue("There are not enough models1 -> " + mocks1.size(), mocks1.size() == 5);
    
    Collection<ModelMock> mocks2 = mapper.findAllByName("Johannes");
    assertTrue("There are not enough models2 -> " + mocks2.size(), mocks2.size() == 6);
    
    Collection<ModelMock> mocks3 = mapper.findAllByName("Sebastian");
    assertTrue("There are not enough models3 -> " + mocks3.size(), mocks3.size() == 7);
  }
  
  private void dropData() {
    Collection<ModelMock> mocks = mapper.findAll();
    mapper.delete(mocks);
    Collection<MinimalMock> minimals = minimalMapper.findAll();
    minimalMapper.delete(minimals);
  }
}