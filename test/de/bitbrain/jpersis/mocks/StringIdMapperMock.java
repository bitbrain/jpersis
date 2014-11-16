package de.bitbrain.jpersis.mocks;

import java.util.Collection;

import de.bitbrain.jpersis.annotations.Count;
import de.bitbrain.jpersis.annotations.Mapper;
import de.bitbrain.jpersis.annotations.Delete;
import de.bitbrain.jpersis.annotations.Insert;
import de.bitbrain.jpersis.annotations.Select;
import de.bitbrain.jpersis.annotations.Update;

@Mapper("de.bitbrain.jpersis.mocks.StringIdMock")
public interface StringIdMapperMock {

	@Insert
	boolean insert(StringIdMock mock);
	
	@Insert
	boolean insert(Collection<StringIdMock> mocks);
	
	@Update
	boolean update(StringIdMock mock);
	
	@Update
	boolean update(Collection<StringIdMock> mocks);
	
	@Delete
	boolean delete(StringIdMock mock);
	
	@Delete 
	boolean delete(Collection<StringIdMock> mocks);
	
	@Count
	int count();
	
	@Select(condition = "id=$1")
	StringIdMock findById(String id);
	
	@Select
	Collection<StringIdMock> findAll();
	
	@Select(condition = "name=$1")
	Collection<StringIdMock> findAllByName(String name);
}
