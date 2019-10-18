package com.ascent.autobcm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ascent.autobcm.dao.OperationsRepository;
import com.ascent.autobcm.dao.RoleOperationsRepository;
import com.ascent.autobcm.exception.NoSuchOperationFound;
import com.ascent.autobcm.exception.OperationAlreadyPresentException;
import com.ascent.autobcm.model.Operations;
import com.ascent.autobcm.model.RoleOperations;
import com.ascent.autobcm.util.Constants;

@Service
public class OperationsServiceImpl implements OperationsService {

	@Autowired
	OperationsRepository operationRepository;

	@Autowired
	RoleOperationsRepository roleOperationsRepository;

	@Override
	public Operations saveOperations(Operations objToPersist) throws OperationAlreadyPresentException {

		Operations persistedObj = null;

		persistedObj = operationRepository.findByOperationName(objToPersist.getOperationName());
		if (null != persistedObj && objToPersist.getId() != persistedObj.getId())
			throw new OperationAlreadyPresentException(
					Constants.OPERATION_ALREADY_PRESENT + objToPersist.getOperationName());

		persistedObj = operationRepository.save(objToPersist);
		return persistedObj;
	}

	@Override
	public List<Operations> findAllByActive(String activeStatus) {
		List<Operations> operationList = operationRepository.findByActive(activeStatus);
		return operationList;
	}

	@Override
	public Operations findById(long operationsId) throws NoSuchOperationFound {
		Optional<Operations> opsFromDb = operationRepository.findById(operationsId);
		if (opsFromDb.isPresent()) {
			return opsFromDb.get();
		}else 
			throw new NoSuchOperationFound(Constants.NO_OPERATION_FOUND);
	}

	@Override
	public Operations findByOperations(String operationName) {
		Operations operationsFromDb = operationRepository.findByOperationName(operationName);
		return operationsFromDb;
	}

	@Override
	public Operations updateOperations(Operations objToPersist)
			throws OperationAlreadyPresentException, NoSuchOperationFound {

		Optional<Operations> operation = null;

		operation = operationRepository.findById(objToPersist.getId());
		if (operation.isPresent() && operation.get().getId() != objToPersist.getId())
			throw new NoSuchOperationFound(Constants.NO_OPERATION_FOUND);

		Operations operationFromDb = operationRepository.findByOperationName(objToPersist.getOperationName());
		if (null != operationFromDb && operationFromDb.getId() != objToPersist.getId())
			throw new OperationAlreadyPresentException(
					Constants.OPERATION_ALREADY_PRESENT + objToPersist.getOperationName());
		Operations persistedEntity = operationRepository.save(objToPersist);
		return persistedEntity;
	}

	@Override
	public Operations deleteOperation(long operationId) throws NoSuchOperationFound {
		Optional<Operations> operationFromDb = null;

		operationFromDb = operationRepository.findById(operationId);
		if (!operationFromDb.isPresent())
			throw new NoSuchOperationFound(Constants.NO_OPERATION_FOUND);

		// Disable Role Operations Mapping
		List<RoleOperations> existingRoleOperationsMapping = roleOperationsRepository.findByOperationId(operationId);
		if (existingRoleOperationsMapping.size() > 0) {
			existingRoleOperationsMapping.stream().forEach(item -> item.setActive(Constants.INACTIVE));
		}
		roleOperationsRepository.saveAll(existingRoleOperationsMapping);

		operationFromDb.get().setActive(Constants.INACTIVE);
		Operations persistedEntity = operationRepository.save(operationFromDb.get());

		return persistedEntity;
	}

	@Override
	public Operations reactivateOperation(long operationId) throws NoSuchOperationFound {

		Optional<Operations> operationFromDb = null;

		operationFromDb = operationRepository.findById(operationId);
		if (!operationFromDb.isPresent())
			throw new NoSuchOperationFound(Constants.NO_OPERATION_FOUND);

		// Enable Role Operations Mapping
		List<RoleOperations> existingRoleOperationsMapping = roleOperationsRepository
				.findByOperationIdAndStatus(operationId, Constants.ACTIVE);
		if (existingRoleOperationsMapping.size() > 0) {
			existingRoleOperationsMapping.stream().forEach(item -> item.setActive(Constants.ACTIVE));
		}
		roleOperationsRepository.saveAll(existingRoleOperationsMapping);

		operationFromDb.get().setActive(Constants.ACTIVE);
		Operations persistedEntity = operationRepository.save(operationFromDb.get());

		return persistedEntity;

	}

}
