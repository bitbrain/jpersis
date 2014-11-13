package de.bitbrain.jpersis.mocks;

import java.util.Collection;

import de.bitbrain.jpersis.annotations.Count;
import de.bitbrain.jpersis.annotations.Mapper;
import de.bitbrain.jpersis.annotations.Delete;
import de.bitbrain.jpersis.annotations.Insert;
import de.bitbrain.jpersis.annotations.Select;
import de.bitbrain.jpersis.annotations.Update;

@Mapper("de.bitbrain.jpersis.mocks.MinimalMock")
public interface MinimalMapperMock {

	@Insert
	boolean insert(MinimalMock mock);
	
	@Insert
	boolean insert(Collection<MinimalMock> mocks);
	
	@Update
	boolean update(ModelMock mock);
	
	@Update
	boolean update(Collection<MinimalMock> mocks);
	
	@Delete
	boolean delete(MinimalMock mock);
	
	@Delete 
	boolean delete(Collection<MinimalMock> mocks);
	
	@Count
	int count();
	
	@Select(condition = "id=$1")
	MinimalMock findById(int id);
	
	@Select
	Collection<MinimalMock> findAll();
}
