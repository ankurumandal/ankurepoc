package com.ascent.autobcm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ascent.autobcm.dao.EntitiesRepository;
import com.ascent.autobcm.dao.EntitiesTypeRepository;
import com.ascent.autobcm.dao.EntityAttributeValuesRepository;
import com.ascent.autobcm.dao.EntityTypeAttributeDefinitionRepository;
import com.ascent.autobcm.exception.EntityAttributeAlreadyPresentException;
import com.ascent.autobcm.exception.NoSuchEntityAttributeFoundException;
import com.ascent.autobcm.model.EntityAttributeValues;
import com.ascent.autobcm.util.Constants;

@Service
public class EntityAttributeValuesImpl implements EntityAttributeValueService {

	@Autowired
	EntityAttributeValuesRepository entitiesAttributeValueRepository;

	@Autowired
	EntitiesTypeRepository entityTypeRepository;

	@Autowired
	EntityTypeAttributeDefinitionRepository entityTypeAttributeDefinitionRepository;

	@Autowired
	EntitiesRepository entityRepository;

	@Override
	public EntityAttributeValues saveEntitiesAttribute(EntityAttributeValues objToPersist)
			throws EntityAttributeAlreadyPresentException {
		EntityAttributeValues persistedEntity = null;

		EntityAttributeValues entityTypeAttributeDefinition = entitiesAttributeValueRepository
				.findByEntityTypeIdAndEntityIdAndEntityAttributeDefinitionId(objToPersist.getEntityType().getId(),
						objToPersist.getEntity().getId(), objToPersist.getEntityAttributeDefinition().getId());
		if (null != entityTypeAttributeDefinition)
			throw new EntityAttributeAlreadyPresentException(Constants.ENTITY_ATTRIBUTE_VALUE_ALREADY_PRESENT);
		else {
			persistedEntity = entitiesAttributeValueRepository.save(objToPersist);
		}

		return persistedEntity;
	}

	@Override
	public EntityAttributeValues findbyId(long entityTypeId) throws NoSuchEntityAttributeFoundException {

		Optional<EntityAttributeValues> optionalEntity = entitiesAttributeValueRepository.findById(entityTypeId);
		if (optionalEntity.isPresent()) {
			return optionalEntity.get();
		}
		else
			throw new NoSuchEntityAttributeFoundException(Constants.NO_ENTITY_ATTRIBUTE_VALUE_FOUND);

	}

	@Override
	public List<EntityAttributeValues> saveAllEntitiesAttribute(List<EntityAttributeValues> entityType) {
		List<EntityAttributeValues> persistedList = entitiesAttributeValueRepository.saveAll(entityType);
		return persistedList;
	}

	/*
	 * @Override public List<EntityAttributeValues> findByUserId(long userId) {
	 * List<EntityAttributeValues> existingMappings =
	 * entitiesAttributeValueRepository.findByUserId(userId); return
	 * existingMappings; }
	 */

	/*
	 * @Override public List<EntityAttributeValues>
	 * findByEntityTypeIdAndEntityAttributeId(long entityTypeId, long
	 * entityAttributeId) { List<EntityAttributeValues> existingMappingsFromDb =
	 * entitiesAttributeValueRepository
	 * .findByEntityTypeIdAndEntityAttributeId(entityTypeId, entityAttributeId);
	 * return existingMappingsFromDb; }
	 */

	@Override
	public EntityAttributeValues findByEntitiesTypeIdAndEntitiesIdAndEntityTypeAttributeDefinitionId(long entityTypeId,
			long entityId, long attributeDefinitionId) throws NoSuchEntityAttributeFoundException {
		EntityAttributeValues entityTypeAttributeDefinition = null;
		entityTypeAttributeDefinition = entitiesAttributeValueRepository
				.findByEntityTypeIdAndEntityIdAndEntityAttributeDefinitionId(entityTypeId, entityId,
						attributeDefinitionId);
		if (null == entityTypeAttributeDefinition)
			throw new NoSuchEntityAttributeFoundException(Constants.NO_ENTITY_ATTRIBUTE_VALUE_FOUND);
		return entityTypeAttributeDefinition;
	}

	@Override
	public EntityAttributeValues deleteOrReactivatePersistence(EntityAttributeValues entityType) {
		EntityAttributeValues persistedEntityType = entitiesAttributeValueRepository.save(entityType);
		return persistedEntityType;
	}

}
