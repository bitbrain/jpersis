package de.bitbrain.jpersis.mocks;

import de.bitbrain.jpersis.MapperException;
import de.bitbrain.jpersis.annotations.*;

import java.util.Collection;

@Mapper("de.bitbrain.jpersis.mocks.MinimalMock")
public interface MinimalExceptionMapperMock {

  @Insert
  void insert(MinimalMock mock) throws MapperException;

  @Insert
  void insert(Collection<MinimalMock> mocks) throws MapperException;

  @Update
  void update(MinimalMock mock) throws MapperException;

  @Update
  void update(Collection<MinimalMock> mocks) throws MapperException;

  @Delete
  void delete(MinimalMock mock) throws MapperException;

  @Delete
  void delete(Collection<MinimalMock> mocks) throws MapperException;

  @Count
  int count() throws MapperException;

  @Select(condition = "id=$1")
  MinimalMock findById(int id) throws MapperException;

  @Select
  Collection<MinimalMock> findAll() throws MapperException;
}
