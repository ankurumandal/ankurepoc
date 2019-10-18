package com.ascent.autobcm.service;

import com.ascent.autobcm.model.EntitiesAttribute;

public interface EntitiesAttributeService {

	public EntitiesAttribute saveEntitiesAttribute(EntitiesAttribute entityType);

	public EntitiesAttribute findById(long entityTypeId);
	
	public EntitiesAttribute findByAttributeName(String attributeName);

}
