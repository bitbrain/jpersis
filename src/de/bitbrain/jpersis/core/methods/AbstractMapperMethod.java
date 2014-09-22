package de.bitbrain.jpersis.core.methods;

import java.lang.annotation.Annotation;

public abstract class AbstractMapperMethod<T extends Annotation> implements MapperMethod<T> {
	
	private T annotation;

	public AbstractMapperMethod(T annotation) {
		this.annotation = annotation;
	}
	
	public T getAnnotation() {
		return annotation;
	}
}
