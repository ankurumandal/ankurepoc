package com.ascent.autobcm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ascent.autobcm.dao.EntityAttributeDefinitionRepository;
import com.ascent.autobcm.model.EntityAttributeDefinition;

@Service
public class EntityAttributeDefinitionImpl implements EntityAttributeDefinitionService {

	@Autowired
	EntityAttributeDefinitionRepository entitiesAttributeDefinitionRepository;

	@Override
	public EntityAttributeDefinition saveEntitiesAttribute(EntityAttributeDefinition entityType) {
		EntityAttributeDefinition persistedEntity = entitiesAttributeDefinitionRepository.save(entityType);
		return persistedEntity;
	}

	@Override
	public EntityAttributeDefinition findbyId(long entityTypeId) {

		Optional<EntityAttributeDefinition> optionalEntity = entitiesAttributeDefinitionRepository
				.findById(entityTypeId);
		if (optionalEntity.isPresent()) {
			return optionalEntity.get();
		}
		return null;

	}

	@Override
	public List<EntityAttributeDefinition> saveAllEntitiesAttribute(List<EntityAttributeDefinition> entityType) {
		List<EntityAttributeDefinition> persistedEntries = entitiesAttributeDefinitionRepository.saveAll(entityType);
		return persistedEntries;
	}

	@Override
	public List<EntityAttributeDefinition> findByEntityTypeId(long entityTypeId) {
		List<EntityAttributeDefinition> persistedEntries = entitiesAttributeDefinitionRepository.findByEntityTypeId(entityTypeId);
		return persistedEntries;
	}

}
