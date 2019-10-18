package com.ascent.autobcm.service;

import java.util.List;

import com.ascent.autobcm.exception.EntityAttributeAlreadyPresentException;
import com.ascent.autobcm.exception.NoSuchEntityAttributeFoundException;
import com.ascent.autobcm.model.EntityAttributeValues;

public interface EntityAttributeValueService {

	public EntityAttributeValues saveEntitiesAttribute(EntityAttributeValues entityType)
			throws EntityAttributeAlreadyPresentException;

	public EntityAttributeValues findbyId(long entityTypeId) throws NoSuchEntityAttributeFoundException;

	public List<EntityAttributeValues> saveAllEntitiesAttribute(List<EntityAttributeValues> entityType);

//	public List<EntityAttributeValues> findByUserId(long userId);

	/*
	 * public List<EntityAttributeValues>
	 * findByEntityTypeIdAndEntityAttributeId(long entityTypeId, long
	 * entityAttributeId);
	 */

	public EntityAttributeValues findByEntitiesTypeIdAndEntitiesIdAndEntityTypeAttributeDefinitionId(long entityTypeId,
			long entityId, long attributeDefinitionId) throws NoSuchEntityAttributeFoundException;
	
	public EntityAttributeValues deleteOrReactivatePersistence(EntityAttributeValues entityType);

}
