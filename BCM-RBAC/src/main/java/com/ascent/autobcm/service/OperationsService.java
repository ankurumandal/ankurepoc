package com.ascent.autobcm.service;

import java.util.List;

import com.ascent.autobcm.exception.NoSuchOperationFound;
import com.ascent.autobcm.exception.OperationAlreadyPresentException;
import com.ascent.autobcm.model.Operations;

public interface OperationsService {

	public Operations saveOperations(Operations objToPersist) throws OperationAlreadyPresentException;

	public List<Operations> findAllByActive(String activeStatus);

	public Operations findById(long operationsId) throws NoSuchOperationFound;

	public Operations findByOperations(String operationName);

	public Operations updateOperations(Operations objToPersist)
			throws OperationAlreadyPresentException, NoSuchOperationFound;

	public Operations deleteOperation(long operationId) throws NoSuchOperationFound;

	public Operations reactivateOperation(long operationId) throws NoSuchOperationFound;

}
