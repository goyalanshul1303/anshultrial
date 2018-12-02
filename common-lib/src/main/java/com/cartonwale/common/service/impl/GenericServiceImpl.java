package com.cartonwale.common.service.impl;

import com.cartonwale.common.dao.GenericDao;
import com.cartonwale.common.exception.DataAccessException;
import com.cartonwale.common.exception.ResourceNotFoundException;
import com.cartonwale.common.service.GenericService;

import rx.Observable;
import rx.Single;
import rx.exceptions.Exceptions;

public class GenericServiceImpl<T> implements GenericService<T> {
	
	private Class<? extends T> type;
	protected GenericDao<T> genericDao;
	
	protected void init(Class<? extends T> type, GenericDao<T> dao) {
        this.type = type;
        this.genericDao = dao;
    }
	
	@Override
	public Single<T> getById(String id){
		try {
            return Single.just(genericDao.getById(id)).map(o->{
            	if(o == null)
            		throw Exceptions.propagate(new ResourceNotFoundException(type.getSimpleName() + " : " + id));
            	return o;
            });
        } catch (DataAccessException de) {
        	return Single.error(de);
        } 
	}

	@Override
	public Single<T> add(T obj){
		try {
			genericDao.add(obj);
            return Single.just(obj);
        } catch (DataAccessException de) {
        	return Single.error(de);
        }
	}

	@Override
	public Single<T> edit(T obj){
		try {
            genericDao.modify(obj);
            return Single.just(obj);
        } catch (DataAccessException de) {
        	return Single.error(de);
        }
	}

	@Override
	public Single<Boolean> delete(T obj){
		try {
            genericDao.delete(obj);
            return Single.just(true);
        } catch (DataAccessException de) {
        	return Single.error(de);
        }
	}

	@Override
	public Observable<T> getAll(){
		try {
            return Observable.from(genericDao.getAll());
        } catch (DataAccessException de) {
        	return Observable.error(de);
        } 
	}

}
