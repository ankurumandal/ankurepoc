package com.ascent.autobcm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ascent.autobcm.model.EntityAttributeDefinition;

@Repository
public interface EntityAttributeDefinitionRepository extends JpaRepository<EntityAttributeDefinition, Long> {

	List<EntityAttributeDefinition> findByEntityTypeId(long entityTypeId);

}
