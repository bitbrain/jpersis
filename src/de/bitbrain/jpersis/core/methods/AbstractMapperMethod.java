package de.bitbrain.jpersis.core.methods;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import de.bitbrain.jpersis.JPersisException;
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
	public Object execute(Method method, Class<?> model, Object[] args, Driver driver) {
		
		if (!validateArgs(args)) {
			throw new JPersisException("Arguments are not supported.");
		}
		if (!validateReturnType(method.getReturnType(), model)) {
			throw new JPersisException("Return type " + method.getReturnType() + " is not allowed.");
		}
		
		Query query = driver.query(model);
		on(model, args, query);
		return query.commit();
	}
	
	protected abstract void on(Class<?> model, Object[] params, Query query);
	
	protected boolean validateArgs(Object[] args) {
		return true;
	}
	
	protected Class<?>[] supportedReturnTypes(Class<?> model) {
		return new Class<?>[]{Object.class};
	}
	
	private boolean validateReturnType(Class<?> type, Class<?> model) {
		Class<?>[] types = supportedReturnTypes(model);
		for (Class<?> c : types) {
			if (c.equals(type)) {
				return true;
			}
		}
		return false;
	}
}
