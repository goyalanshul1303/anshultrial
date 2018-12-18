package com.cartonwale.common.service;

import java.util.List;

import rx.Single;

public interface GenericService<T>{

	/*Single<T> getById(String id);*/
	
	T getById(String id);

	/*Single<T> add(T obj);*/
	
	T add(T obj);

	/*Single<T> edit(T obj);*/
	
	T edit(T obj);

	/*Single<Boolean> delete(T object);*/
	
	Boolean delete(T object);

	/*Observable<T> getAll();*/
	
	List<T> getAll();
    
}
