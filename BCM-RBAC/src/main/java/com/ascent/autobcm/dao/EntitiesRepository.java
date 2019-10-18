package com.ascent.autobcm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ascent.autobcm.model.Entities;

@Repository
public interface EntitiesRepository extends JpaRepository<Entities, Long> {

	List<Entities> findByEntityTypeId(long entityTypeId);

//	List<Entities> findByUserId(long userId);

	/*
	 * @Query("select et.entityType from Entities et left join et.entityType entityType where entityType.id = ?1"
	 * ) List<EntitiesType> findEntityTypeByUserAssigned(long userId);
	 */

	Entities findByEntityTypeIdAndEntityName(long entityTypeId, String entityName);

}
