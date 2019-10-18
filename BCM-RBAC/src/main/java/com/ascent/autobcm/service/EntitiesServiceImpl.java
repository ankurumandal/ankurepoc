package com.ascent.autobcm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ascent.autobcm.dao.EntitiesRepository;
import com.ascent.autobcm.dao.EntitiesTypeRepository;
import com.ascent.autobcm.exception.EntityAlreadyPresentException;
import com.ascent.autobcm.exception.ImproperParametersPassedException;
import com.ascent.autobcm.exception.NoSuchEntityFound;
import com.ascent.autobcm.model.Entities;
import com.ascent.autobcm.model.EntitiesType;
import com.ascent.autobcm.util.Constants;

@Service
public class EntitiesServiceImpl implements EntitiesService {

	@Autowired
	EntitiesRepository entitiesRepository;

	@Autowired
	EntitiesTypeRepository entityTypeRepository;

	@Override
	public Entities saveEntities(Entities entity)
			throws EntityAlreadyPresentException, ImproperParametersPassedException {

		Optional<EntitiesType> entityType = null;
		entityType = entityTypeRepository.findById(entity.getEntityType().getId());

		Entities entityfromDb = null;
		entityfromDb = entitiesRepository.findByEntityTypeIdAndEntityName(entity.getEntityType().getId(),
				entity.getEntityName());

		if (null != entityfromDb)
			throw new EntityAlreadyPresentException(Constants.ENTITY_ALREADY_PRESENT);
		else if (!entityType.isPresent())
			throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);
		else
			entity = entitiesRepository.save(entity);
		return entity;
	}

	@Override
	public Entities findbyId(long entityTypeId) throws NoSuchEntityFound {

		Optional<Entities> optionalEntity = entitiesRepository.findById(entityTypeId);
		if (optionalEntity.isPresent())
			return optionalEntity.get();
		else
			throw new NoSuchEntityFound(Constants.NO_ENTITY_FOUND);

	}

	@Override
	public List<Entities> saveEntities(List<Entities> entities) {
		List<Entities> entitiesList = entitiesRepository.saveAll(entities);
		return entitiesList;
	}

	@Override
	public List<Entities> findByEntityTypeId(long entityTypeId) {
		List<Entities> existingMappings = entitiesRepository.findByEntityTypeId(entityTypeId);
		return existingMappings;
	}

	/*
	 * @Override public List<Entities> findByUserId(long userId) { List<Entities>
	 * existingMappings = entitiesRepository.findByUserId(userId); return
	 * existingMappings; }
	 * 
	 * @Override public List<EntitiesType> findEntityTypeByUserAssigned(long userId)
	 * { List<EntitiesType> entitiesList =
	 * entitiesRepository.findEntityTypeByUserAssigned(userId);
	 * 
	 * return entitiesList; }
	 */

	@Override
	public Entities findByEntityTypeIdAndEntityName(long entityTypeId, String entityName) {
		Entities entriesFromDb = entitiesRepository.findByEntityTypeIdAndEntityName(entityTypeId, entityName);
		return entriesFromDb;
	}

	@Override
	public Entities updateEntity(Entities entityToUpdate)
			throws EntityAlreadyPresentException, ImproperParametersPassedException {
		Entities entity = null;
		Optional<EntitiesType> entityType = null;

		entity = entitiesRepository.findByEntityTypeIdAndEntityName(entityToUpdate.getEntityType().getId(),
				entityToUpdate.getEntityName());

		entityType = entityTypeRepository.findById(entityToUpdate.getEntityType().getId());

		if (null != entity && entity.getId() != entityToUpdate.getId())
			throw new EntityAlreadyPresentException("");
		else if (!entityType.isPresent())
			throw new ImproperParametersPassedException("");
		else
			entitiesRepository.save(entityToUpdate);
		return entityToUpdate;
	}

	@Override
	public Entities deleteExistingEntity(long entityId) throws NoSuchEntityFound {
		Optional<Entities> entityFromDb = null;

		entityFromDb = entitiesRepository.findById(entityId);

		if (!entityFromDb.isPresent())
			throw new NoSuchEntityFound(Constants.NO_ENTITY_FOUND);
		else {
			Entities entityToPersist = entityFromDb.get();
			entityToPersist.setActive(Constants.INACTIVE);
			entityToPersist = entitiesRepository.save(entityToPersist);
			return entityToPersist;
		}
	}

	@Override
	public Entities reactivateExistingEntity(long entityId) throws NoSuchEntityFound {
		Optional<Entities> entityFromDb = null;

		entityFromDb = entitiesRepository.findById(entityId);

		if (!entityFromDb.isPresent())
			throw new NoSuchEntityFound("");
		else {
			Entities entityToPersist = entityFromDb.get();
			entityToPersist.setActive(Constants.ACTIVE);
			entityToPersist = entitiesRepository.save(entityToPersist);
			return entityToPersist;
		}
	}

}
