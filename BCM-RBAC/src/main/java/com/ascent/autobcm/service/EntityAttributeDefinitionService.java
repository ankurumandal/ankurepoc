
package com.ascent.autobcm.service;

import java.util.List;

import com.ascent.autobcm.model.EntityAttributeDefinition;

public interface EntityAttributeDefinitionService {

	public EntityAttributeDefinition saveEntitiesAttribute(EntityAttributeDefinition entityType);

	public EntityAttributeDefinition findbyId(long entityTypeId);
	
	public List<EntityAttributeDefinition> saveAllEntitiesAttribute(List<EntityAttributeDefinition> entityType);
	
	public List<EntityAttributeDefinition> findByEntityTypeId(long entityTypeId);
}
