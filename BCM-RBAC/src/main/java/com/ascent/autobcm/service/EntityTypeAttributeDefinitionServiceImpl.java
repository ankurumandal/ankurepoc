package com.ascent.autobcm.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ascent.autobcm.dao.EntityTypeAttributeDefinitionRepository;
import com.ascent.autobcm.exception.EntityTypeAttributeDefinitionAlreadyPresent;
import com.ascent.autobcm.exception.NoSuchEntityTypeAttributeDefinitionFound;
import com.ascent.autobcm.model.EntityTypeAttributeDefinition;
import com.ascent.autobcm.util.Constants;

@Service
public class EntityTypeAttributeDefinitionServiceImpl implements EntityTypeAttributeDefinitionService {

	@Autowired
	EntityTypeAttributeDefinitionRepository entityTypeAttributeDefinitionRepository;

	@Override
	public EntityTypeAttributeDefinition saveEntityTypeAttributeDefinition(EntityTypeAttributeDefinition objToPersist)
			throws EntityTypeAttributeDefinitionAlreadyPresent {

		EntityTypeAttributeDefinition fromDb = entityTypeAttributeDefinitionRepository
				.findByEntityTypeIdAndAttributeName(objToPersist.getEntityType().getId(),
						objToPersist.getAttributeName());

		if (null != fromDb && fromDb.getId() != objToPersist.getId())
			throw new EntityTypeAttributeDefinitionAlreadyPresent(Constants.ENTITY_TYPE_ATTRIBUTE_DEFINITION_PRESENT);

		EntityTypeAttributeDefinition persistedObj = entityTypeAttributeDefinitionRepository.save(objToPersist);
		return persistedObj;
	}

	@Override
	public EntityTypeAttributeDefinition findByEntitiesTypeIdAndAttributeName(long entityTypeId,
			String attributeNameToPersist) throws NoSuchEntityTypeAttributeDefinitionFound {

		EntityTypeAttributeDefinition fromDb = entityTypeAttributeDefinitionRepository
				.findByEntityTypeIdAndAttributeName(entityTypeId, attributeNameToPersist);

		if (null == fromDb)
			throw new NoSuchEntityTypeAttributeDefinitionFound(Constants.NO_ENTITY_TYPE_ATTRIBUTE_DEFINITION_FOUND);
		return fromDb;
	}

	@Override
	public EntityTypeAttributeDefinition findById(long entityTypeAttributeId) throws NoSuchEntityTypeAttributeDefinitionFound {
		Optional<EntityTypeAttributeDefinition> attribDefinition = entityTypeAttributeDefinitionRepository
				.findById(entityTypeAttributeId);
		if (attribDefinition.isPresent())
			return attribDefinition.get();
		else
			throw new NoSuchEntityTypeAttributeDefinitionFound(Constants.NO_ENTITY_TYPE_ATTRIBUTE_DEFINITION_FOUND);
	}

	@Override
	public EntityTypeAttributeDefinition activateOrReactivateEntityTypeAttributeDefinition(
			EntityTypeAttributeDefinition objToPersist) {
		EntityTypeAttributeDefinition entityTypeAttributeDefinition = entityTypeAttributeDefinitionRepository
				.save(objToPersist);
		return entityTypeAttributeDefinition;
	}

}
