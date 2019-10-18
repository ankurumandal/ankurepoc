package com.ascent.autobcm.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ascent.autobcm.dao.EntitiesTypeRepository;
import com.ascent.autobcm.exception.EntityTypeAlreadyPresentException;
import com.ascent.autobcm.exception.NoSuchEntityTypeFound;
import com.ascent.autobcm.model.EntitiesType;
import com.ascent.autobcm.util.Constants;

@Service
public class EntitiesTypeServiceImpl implements EntitiesTypeService {

	@Autowired
	EntitiesTypeRepository entitiesTypeRepository;

	@Override
	public EntitiesType saveEntitiesType(EntitiesType entityType) throws EntityTypeAlreadyPresentException {

		EntitiesType fromDb = entitiesTypeRepository.findByName(entityType.getName());

		if (null != fromDb && fromDb.getId() != entityType.getId())
			throw new EntityTypeAlreadyPresentException(Constants.ENTITY_TYPE_ALREADY_PRESENT);

		return entitiesTypeRepository.save(entityType);
	}

	@Override
	public EntitiesType findById(long entityTypeId) throws NoSuchEntityTypeFound {
		Optional<EntitiesType> optionalEntity = entitiesTypeRepository.findById(entityTypeId);
		if (optionalEntity.isPresent()) {
			return optionalEntity.get();
		} else
			throw new NoSuchEntityTypeFound(Constants.NO_ENTITY_TYPE_FOUND);
	}

	@Override
	public EntitiesType findByName(String name) throws NoSuchEntityTypeFound {
		EntitiesType fromDbObj = entitiesTypeRepository.findByName(name);

		if (null != fromDbObj)
			throw new NoSuchEntityTypeFound(Constants.NO_ENTITY_TYPE_FOUND);

		return fromDbObj;
	}

	@Override
	public EntitiesType activeOrInactivePersist(EntitiesType typeToBePersisted) {
		EntitiesType persistedEntityType = entitiesTypeRepository.save(typeToBePersisted);
		return persistedEntityType;
	}

}
