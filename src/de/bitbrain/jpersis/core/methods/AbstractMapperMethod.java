package de.bitbrain.jpersis.core.methods;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import de.bitbrain.jpersis.drivers.Driver;
import de.bitbrain.jpersis.drivers.Driver.Query;

public abstract class AbstractMapperMethod<T extends Annotation> implements MapperMethod<T> {
	
	private T annotation;

	public AbstractMapperMethod(T annotation) {
		this.annotation = annotation;
	}
	
	public T getAnnotation() {
		return annotation;
	}
	
	@Override
	public Object execute(Method method, Class<?> model, Object[] params, Driver driver) {
		Query query = driver.query(model);
		on(model, params, query);
		return query.commit();
	}
	
	protected abstract void on(Class<?> model, Object[] params, Query query);
}
