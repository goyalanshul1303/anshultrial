package com.cartonwale.common.service.impl;

import java.util.List;

import com.cartonwale.common.dao.GenericDao;
import com.cartonwale.common.exception.DataAccessException;
import com.cartonwale.common.service.GenericService;

public class GenericServiceImpl<T> implements GenericService<T> {
	
	private Class<? extends T> type;
	protected GenericDao<T> genericDao;
	
	protected void init(Class<? extends T> type, GenericDao<T> dao) {
        this.type = type;
        this.genericDao = dao;
    }
	
	/*@Override
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
	}*/
	
	@Override
	public T getById(String id){
		try {
            return genericDao.getById(id);
            
        } catch (DataAccessException de) {
        	System.out.println(de);
        	return null;
        } 
	}

	/*@Override
	public Single<T> add(T obj){
		try {
			genericDao.add(obj);
            return Single.just(obj);
        } catch (DataAccessException de) {
        	return Single.error(de);
        }
	}*/
	
	@Override
	public T add(T obj){
		try {
			genericDao.add(obj);
            return obj;
        } catch (DataAccessException de) {
        	System.out.println(de);
        	return null;
        }
	}

	/*@Override
	public Single<T> edit(T obj){
		try {
            genericDao.modify(obj);
            return Single.just(obj);
        } catch (DataAccessException de) {
        	return Single.error(de);
        }
	}*/
	
	@Override
	public T edit(T obj){
		try {
            genericDao.modify(obj);
            return obj;
        } catch (DataAccessException de) {
        	System.out.println(de);
        	return null;
        }
	}

	/*@Override
	public Single<Boolean> delete(T obj){
		try {
            genericDao.delete(obj);
            return Single.just(true);
        } catch (DataAccessException de) {
        	return Single.error(de);
        }
	}*/
	
	@Override
	public Boolean delete(T obj){
		try {
            genericDao.delete(obj);
            return true;
        } catch (DataAccessException de) {
        	System.out.println(de);
        	return false;
        }
	}

	/*@Override
	public Observable<T> getAll(){
		try {
            return Observable.from(genericDao.getAll());
        } catch (DataAccessException de) {
        	return Observable.error(de);
        } 
	}*/
	
	@Override
	public List<T> getAll(){
		try {
            return genericDao.getAll();
        } catch (DataAccessException de) {
        	System.out.println(de);
        	return null;
        } 
	}

}
