package de.bitbrain.jpersis.mocks;

import java.util.Collection;

import de.bitbrain.jpersis.annotations.Count;
import de.bitbrain.jpersis.annotations.Mapper;
import de.bitbrain.jpersis.annotations.Delete;
import de.bitbrain.jpersis.annotations.Insert;
import de.bitbrain.jpersis.annotations.Select;
import de.bitbrain.jpersis.annotations.Update;

@Mapper("de.bitbrain.jpersis.mocks.ModelMock")
public interface MapperMock {

	@Insert
	boolean insert(ModelMock mock);
	
	@Update
	boolean update(ModelMock mock);
	
	@Delete
	boolean delete(ModelMock mock);
	
	@Count
	int count();
	
	@Select(condition = "id = $1")
	ModelMock findById(int id);
	
	@Select
	Collection<ModelMock> findAll();
	
	@Select(condition = "name=$1")
    Collection<ModelMock> findAllByName(String name);
}
