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

import de.bitbrain.jpersis.drivers.Driver;
import de.bitbrain.jpersis.drivers.DriverException;
import de.bitbrain.jpersis.mocks.*;
import de.bitbrain.jpersis.util.DriverFactory;
import de.bitbrain.jpersis.util.MySQLDriverFactory;
import de.bitbrain.jpersis.util.PosgreSQLDriverFactory;
import de.bitbrain.jpersis.util.SQLiteDriverFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(value = Parameterized.class)
public class JPersisTest {

  static final String DB = "temp.sql";

  JPersis manager;

  MapperMock mapper;

  MinimalMapperMock minimalMapper;

  StringIdMapperMock stringMapper;

  EnumIdMapperMock enumMapper;

  @Parameter
  public DriverFactory factory;

  private Driver driver;

  @Parameters
  public static Collection<DriverFactory[]> getParams() {
    Features features = new Features();
    List<DriverFactory[]> infos = new ArrayList<>();
    infos.add(new DriverFactory[] { new SQLiteDriverFactory(DB) });
    if (features.isEnabled(Features.Feature.MYSQL)) {
        infos.add(new DriverFactory[] { new MySQLDriverFactory() });
    }
    if (features.isEnabled(Features.Feature.POSTGRESQL)) {
    infos.add(new DriverFactory[] { new PosgreSQLDriverFactory()});
    }
    return infos;
  }

  @Before
  public void beforeTest() throws IOException {
    driver = factory.create();
    manager = new JPersis(driver);
    mapper = manager.map(MapperMock.class);
    minimalMapper = manager.map(MinimalMapperMock.class);
    stringMapper = manager.map(StringIdMapperMock.class);
    enumMapper = manager.map(EnumIdMapperMock.class);
    dropData();
  }

  @After
  public void afterTest() throws DriverException {
    dropData();
  }

  @Test
  public void testCheckedExceptions() {
    MinimalExceptionMapperMock m = manager.map(MinimalExceptionMapperMock.class);
    MinimalMock mock = new MinimalMock();
    try {
      m.insert(new ArrayList<MinimalMock>());
      fail("Should throw an exception!");
    } catch (MapperException e) { }
  }

  @Test
  public void testInsert() {
    final int RUNS = 5;
    boolean firstKey = true;
    int expected = 1;
    for (int i = 0; i < RUNS; ++i) {
      ModelMock m = new ModelMock();
      m.setName("Max");
      m.setLastName("Mustermann");
      assertTrue("It should be possible to insert element nr" + i, mapper.insert(m));
      if (firstKey) {
        firstKey = false;
        expected = m.getId();
      }
      assertTrue("There should be " + (i + 1) + " elements.", mapper.count() == (i + 1));
      assertTrue("Primary key should be " + expected + " instead of " + m.getId(), m.getId() == expected);
      expected = m.getId() + 1;
    }

    firstKey = true;
    for (int i = 0; i < RUNS; ++i) {
      MinimalMock m = new MinimalMock();
      m.setName("Meh");
      assertTrue("It should be possible to insert element nr" + i, minimalMapper.insert(m));
      if (firstKey) {
        firstKey = false;
        expected = m.getId();
      }
      assertTrue("There should be " + (i + 1) + " elements.", minimalMapper.count() == (i + 1));
      assertTrue("Primary key should be " + expected + " instead of " + m.getId(), m.getId() == expected);
      expected = m.getId() + 1;
    }

    for (int i = 0; i < RUNS; ++i) {
      StringIdMock m = new StringIdMock("id_" + i);
      m.setName("Meh");
      assertTrue("It should be possible to insert element nr" + i, stringMapper.insert(m));
      assertTrue("There should be " + (i + 1) + " elements.", stringMapper.count() == (i + 1));
      assertTrue("Primary key should be id_" + i + " instead of " + m.getId(), m.getId().equals("id_" + i));
    }

    TestEnum[] enums = TestEnum.values();
    for (int i = 0; i < enums.length; i++) {
      TestEnum id = enums[i];
      EnumIdMock m = new EnumIdMock(id);
      m.setName("Meh");
      assertTrue("It should be possible to insert element " + id, enumMapper.insert(m));
      int amount = enumMapper.count();
      assertTrue("There should be " + (i + 1) + " elements instead of " + amount, amount == (i + 1));
      assertTrue("Primary key should be " + id + " instead of " + m.getId(), m.getId().equals(id));
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

    Collection<ModelMock> dbMocks = mapper.findAll();
    assertTrue("There should be the same amount as inserted", dbMocks.size() == AMOUNT);

    int i = 0;
    for (ModelMock m : dbMocks) {
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

    StringIdMock m2 = new StringIdMock("test");
    m2.setName("Hans");
    stringMapper.insert(m2);
    m2.setName("Wilfred");
    stringMapper.update(m2);
    StringIdMock updated2 = stringMapper.findById(m2.getId());
    assertTrue("It should not be null", updated2 != null);
    assertTrue("It should have the same ID", updated2.getId().equals(m2.getId()));
    assertTrue("Old and new object should be the same", m2.equals(updated2));
    assertTrue("It should be an updated name instead of " + updated2.getName(), "Wilfred".equals(updated2.getName()));

    EnumIdMock m3 = new EnumIdMock(TestEnum.TEST1);
    m3.setName("Hans");
    enumMapper.insert(m3);
    m3.setName("Wilfred");
    enumMapper.update(m3);
    EnumIdMock updated3 = enumMapper.findById(m3.getId());
    assertTrue("It should not be null", updated3 != null);
    assertTrue("It should have the same ID", updated3.getId().equals(m3.getId()));
    assertTrue("Old and new object should be the same", m3.equals(updated3));
    assertTrue("It should be an updated name instead of " + updated3.getName(), "Wilfred".equals(updated3.getName()));

  }

  @Test
  public void testDelete() {
    final int RUNS = 5;
    int[] ids = new int[RUNS];
    for (int i = 0; i < RUNS; ++i) {
      ModelMock m = new ModelMock();
      m.setName("Max");
      m.setLastName("Mustermann");
      assertTrue("It should be possible to insert element nr" + i, mapper.insert(m));
      ids[i] = m.getId();
    }
    for (int i = 0; i < RUNS; ++i) {
      ModelMock m = mapper.findById(ids[i]);
      assertTrue("ModelMock should be there", m != null);
      assertTrue("It should be possible to delete object nr" + i, mapper.delete(m));
      assertTrue("There should be " + (RUNS - (i + 1)) + " elements.", mapper.count() == (RUNS - (i + 1)));
    }
    for (int i = 0; i < RUNS; ++i) {
      StringIdMock m = new StringIdMock("id_" + i);
      m.setName("Max");
      assertTrue("It should be possible to insert element nr" + i, stringMapper.insert(m));
      assertTrue("There should be " + (i + 1) + " elements instead of " + stringMapper.count(),
          stringMapper.count() == 1);
      assertTrue("It should be possible to delete object nr" + i + " with id " + m.getId(), stringMapper.delete(m));
      assertTrue("There should be " + (i + 1) + " elements.", stringMapper.count() == 0);
    }
    TestEnum[] enums = TestEnum.values();
    for (int i = 0; i < enums.length; ++i) {
      TestEnum id = enums[i];
      EnumIdMock m = new EnumIdMock(id);
      m.setName("Max");
      assertTrue("It should be possible to insert element nr" + i, enumMapper.insert(m));
      assertTrue("There should be " + (i + 1) + " elements instead of " + enumMapper.count(), enumMapper.count() == 1);
      assertTrue("It should be possible to delete object nr" + i + " with id " + m.getId(), enumMapper.delete(m));
      assertTrue("There should be " + (i + 1) + " elements.", enumMapper.count() == 0);
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

    for (int i = 0; i < RUNS; ++i) {
      StringIdMock m = new StringIdMock("id_" + i);
      m.setName("Max");
      stringMapper.insert(m);
    }
    Collection<StringIdMock> mocks2 = stringMapper.findAll();
    assertTrue("There are not enough mocks to find", mocks2.size() == RUNS);
    for (StringIdMock m : mocks2) {
      StringIdMock found = stringMapper.findById(m.getId());
      assertTrue("The objects should be the same", m.equals(found));
    }

    for (TestEnum id : TestEnum.values()) {
      EnumIdMock m = new EnumIdMock(id);
      m.setName("Max");
      enumMapper.insert(m);
    }
    Collection<EnumIdMock> mocks3 = enumMapper.findAll();
    assertTrue("There are not enough mocks to find", mocks3.size() == TestEnum.values().length);
    for (EnumIdMock m : mocks3) {
      EnumIdMock found = enumMapper.findById(m.getId());
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

    for (TestEnum id : TestEnum.values()) {
      EnumIdMock m = new EnumIdMock();
      m.setName("Sebastian");
      m.setId(id);
      enumMapper.insert(m);
    }

    Collection<EnumIdMock> enumMocks = enumMapper.findAllByName("Sebastian");
    assertEquals(enumMocks.size(), TestEnum.values().length);
  }

  @Test
  public void testDefaultMapping() {
    DefaultMapper<ModelMock> defaultMapper = manager.mapDefault(ModelMock.class);
    ModelMock m = new ModelMock();
    m.setName("Sebastian");
    m.setLastName("Walter");
    defaultMapper.insert(m);
    assertTrue("There needs to be a model for default insertion", defaultMapper.count() == 1);
    ModelMock retrieved = defaultMapper.getById(m.getId());
    assertTrue("There needs to be a model for default insertion", retrieved.equals(m));
    defaultMapper.delete(m);
    assertTrue("There should not be any model anymore", defaultMapper.count() == 0);
  }

  @Test
  public void testCachedDefaultMapping() {
    DefaultMapper<ModelMock> cachedMapper = manager.mapDefaultCached(ModelMock.class);
    DefaultMapper<ModelMock> directMapper = manager.mapDefault(ModelMock.class);
    ModelMock m = new ModelMock();
    m.setName("Sebastian");
    m.setLastName("Walter");
    cachedMapper.insert(m);
    assertTrue("There needs to be a model for default insertion", cachedMapper.count() == 1);
    ModelMock retrieved = cachedMapper.getById(m.getId());
    assertTrue("There needs to be a model for default insertion", retrieved.equals(m));
    cachedMapper.delete(m);
    assertTrue("There should not be any model anymore", cachedMapper.count() == 0);

    // Remove the model from the database
    cachedMapper.insert(m);
    directMapper.delete(m);
    // Model should still be in the cache
    ModelMock result = cachedMapper.getById(m.getId());
    assertTrue("The model should persist in the cache", result.equals(m));
    // ..but not in the database
    result = directMapper.getById(m.getId());
    assertNull(result);
  }

  private void dropData() {
    Collection<ModelMock> mocks = mapper.findAll();
    mapper.delete(mocks);
    Collection<MinimalMock> minimals = minimalMapper.findAll();
    minimalMapper.delete(minimals);
    Collection<StringIdMock> strings = stringMapper.findAll();
    stringMapper.delete(strings);
    Collection<EnumIdMock> enums = enumMapper.findAll();
    enumMapper.delete(enums);
  }
}