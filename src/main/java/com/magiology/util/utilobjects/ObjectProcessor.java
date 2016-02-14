package com.magiology.util.utilobjects;

public interface ObjectProcessor<T>{
	public abstract T pocess(T object, Object...objects);
}