package com.ascent.autobcm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ascent.autobcm.model.EntityAttributeValues;

@Repository
public interface EntityAttributeValuesRepository extends JpaRepository<EntityAttributeValues, Long> {

//	List<EntityAttributeValues> findByUserId(long userId);

//	List<EntityAttributeValues> findByEntityTypeIdAndEntityAttributeId(long entityTypeId, long entityAttributeId);

	EntityAttributeValues findByEntityTypeIdAndEntityIdAndEntityAttributeDefinitionId(long entityTypeId,
			long entityId, long entityDefinitionId);

}
