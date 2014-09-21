package de.bitbrain.jpersis.core.methods;

import java.lang.annotation.Annotation;

public abstract class AbstractMapperMethod implements MapperMethod {
	
	private Annotation annotation;

	public AbstractMapperMethod(Annotation annotation) {
		this.annotation = annotation;
	}
	
	public Annotation getAnnotation() {
		return annotation;
	}
}
