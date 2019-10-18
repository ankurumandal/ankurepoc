package com.ascent.autobcm.service;

import com.ascent.autobcm.exception.EntityTypeAlreadyPresentException;
import com.ascent.autobcm.exception.NoSuchEntityTypeFound;
import com.ascent.autobcm.model.EntitiesType;

public interface EntitiesTypeService {

	public EntitiesType saveEntitiesType(EntitiesType entityType) throws EntityTypeAlreadyPresentException;

	public EntitiesType findById(long entityTypeId) throws NoSuchEntityTypeFound;

	public EntitiesType findByName(String name) throws NoSuchEntityTypeFound;
	
	public EntitiesType activeOrInactivePersist(EntitiesType typeToBePersisted) ;
	
	

}
