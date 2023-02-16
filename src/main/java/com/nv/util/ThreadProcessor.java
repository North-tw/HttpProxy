package com.nv.util;

@FunctionalInterface
public interface ThreadProcessor<T> {

	public T process() throws Exception;
}
