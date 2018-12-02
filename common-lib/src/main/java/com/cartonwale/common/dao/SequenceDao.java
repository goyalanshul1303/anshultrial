package com.cartonwale.common.dao;

import com.cartonwale.common.exception.DataAccessException;
import com.cartonwale.common.model.Sequence;

public interface SequenceDao  extends GenericDao<Sequence>{

	int getNextSequenceId(String key) throws DataAccessException;
}
