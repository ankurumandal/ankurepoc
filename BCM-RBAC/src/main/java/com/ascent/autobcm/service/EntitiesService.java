package com.ascent.autobcm.service;

import java.util.List;

import com.ascent.autobcm.exception.EntityAlreadyPresentException;
import com.ascent.autobcm.exception.ImproperParametersPassedException;
import com.ascent.autobcm.exception.NoSuchEntityFound;
import com.ascent.autobcm.model.Entities;

public interface EntitiesService {

	public Entities saveEntities(Entities entityType)
			throws EntityAlreadyPresentException, ImproperParametersPassedException;

	public Entities findbyId(long entityTypeId)  throws NoSuchEntityFound ;

	public List<Entities> saveEntities(List<Entities> entityType);

	public List<Entities> findByEntityTypeId(long entityTypeId);

	/*
	 * public List<Entities> findByUserId(long userId);
	 * 
	 * public List<EntitiesType> findEntityTypeByUserAssigned(long userId);
	 */

	public Entities findByEntityTypeIdAndEntityName(long entityTypeId, String entityName);

	public Entities updateEntity(Entities entityToUpdate)
			throws EntityAlreadyPresentException, ImproperParametersPassedException;

	public Entities deleteExistingEntity(long entityTypeId) throws NoSuchEntityFound;

	public Entities reactivateExistingEntity(long entityTypeId) throws NoSuchEntityFound;

}
