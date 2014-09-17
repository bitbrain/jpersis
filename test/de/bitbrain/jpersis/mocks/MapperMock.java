package de.bitbrain.jpersis.mocks;

import java.util.Collection;

import de.bitbrain.jpersis.annotations.Count;
import de.bitbrain.jpersis.annotations.DataMapper;
import de.bitbrain.jpersis.annotations.Delete;
import de.bitbrain.jpersis.annotations.Insert;
import de.bitbrain.jpersis.annotations.Select;
import de.bitbrain.jpersis.annotations.Update;

@DataMapper(
		model = "de.bitbrain.jpersis.mocks.MapperMock", 
		table="mocks", 
		primaryKey = "id")
public interface MapperMock {

	@Insert
	void insert(ModelMock mock);
	
	@Update
	void update(ModelMock mock);
	
	@Delete
	void delete(ModelMock mock);
	
	@Count
	int count();
	
	@Select(condition = "id = $1")
	ModelMock findById(int id);
	
	@Select
	Collection<ModelMock> findAll();
}
