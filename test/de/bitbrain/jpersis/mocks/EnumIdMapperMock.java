package de.bitbrain.jpersis.mocks;

import java.util.Collection;

import de.bitbrain.jpersis.annotations.Count;
import de.bitbrain.jpersis.annotations.Delete;
import de.bitbrain.jpersis.annotations.Insert;
import de.bitbrain.jpersis.annotations.Mapper;
import de.bitbrain.jpersis.annotations.Select;
import de.bitbrain.jpersis.annotations.Update;

@Mapper("de.bitbrain.jpersis.mocks.EnumIdMock")
public interface EnumIdMapperMock {

  @Insert
  boolean insert(EnumIdMock mock);

  @Insert
  boolean insert(Collection<EnumIdMock> mocks);

  @Update
  boolean update(EnumIdMock mock);

  @Update
  boolean update(Collection<EnumIdMock> mocks);

  @Delete
  boolean delete(EnumIdMock mock);

  @Delete
  boolean delete(Collection<EnumIdMock> mocks);

  @Count
  int count();

  @Select(condition = "id=$1")
  EnumIdMock findById(TestEnum id);

  @Select
  Collection<EnumIdMock> findAll();

  @Select(condition = "name=$1")
  Collection<EnumIdMock> findAllByName(String name);
}
