package com.cartonwale.common.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.cartonwale.common.dao.GenericDao;
import com.cartonwale.common.exception.DataAccessException;

@CacheConfig(cacheResolver="primaryCacheResolver")
public class GenericDaoImpl<T> implements GenericDao<T> {

	private static final Logger logger = LoggerFactory.getLogger(GenericDaoImpl.class);

	@Autowired
	protected MongoOperations mongoOperations;

	private Class<T> type;

	public GenericDaoImpl(Class<T> type) {
		this.type = type;
	}

	@Override
	@Caching(
				put={@CachePut(key="#object.id")},
				evict={@CacheEvict(cacheResolver="secondaryCacheResolver", allEntries=true)}
			)
	public T add(T object) throws DataAccessException {
		if (logger.isDebugEnabled())
			logger.debug("type {} add", type);
		try {
			mongoOperations.insert(object);
			return object;
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
	}

	@Override
	@Caching(
			put={@CachePut(key="#object.id")},
			evict={@CacheEvict(cacheResolver="secondaryCacheResolver", allEntries=true)}
			)
	public T modify(T object) throws DataAccessException {
		if (logger.isDebugEnabled())
			logger.debug("type {} modify", type);
		try {
			mongoOperations.save(object);
			return object;
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
	}
	
	@Override
	@Caching(
			evict = {
					@CacheEvict(key="#object.id"),
					@CacheEvict(cacheResolver="secondaryCacheResolver", allEntries=true)
				}
			)
	public T delete(T object) throws DataAccessException {
		if (logger.isDebugEnabled())
			logger.debug("type {} delete", type);
		try {
			mongoOperations.remove(object);
			return object;
		} catch (Exception e) {
			throw new DataAccessException(e);
		}

	}

	@Override
	@Cacheable(key="#id" ,unless="#result == null")
	public T getById(Object id) throws DataAccessException {
		if (logger.isDebugEnabled())
			logger.debug("type {} getById", type);
		try{
			return mongoOperations.findById(id, type);
		}catch(Exception e){
			throw new DataAccessException(e);
		}
	}

	@Override
	@Cacheable(cacheResolver="secondaryCacheResolver" ,unless="#result == null")
	public List<T> getAll() throws DataAccessException {
		if (logger.isDebugEnabled())
			logger.debug("type {} getAll", type);
		try {
			return mongoOperations.findAll(type);
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
	}
	
	@Override
	@Cacheable(cacheResolver="secondaryCacheResolver" ,unless="#result == null")
	public List<T> getAll(Query query) throws DataAccessException {
		if (logger.isDebugEnabled())
			logger.debug("type {} getAllByUser", type);
		try {
			
			return mongoOperations.find(query, type);
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
	}
	
	@Override
	@Cacheable(cacheResolver="secondaryCacheResolver" ,unless="#result == null")
	public List<T> getAll(Aggregation agg) throws DataAccessException {
		if (logger.isDebugEnabled())
			logger.debug("type {} getAllUsingAggregation", type);
		try {
			
			return mongoOperations.aggregate(agg, type, type).getMappedResults();
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
	}
	
	public void findAndUpdateAll(Query query, Update update) throws DataAccessException {
		if (logger.isDebugEnabled())
			logger.debug("type {} getAllByUser", type);
		try {
			mongoOperations.findAndModify(query, update, type);
			
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
	}

	@Override
	public MongoOperations getMongoOperations() {
		return mongoOperations;
	}
}
