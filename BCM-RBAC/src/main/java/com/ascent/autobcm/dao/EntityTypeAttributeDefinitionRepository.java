package com.ascent.autobcm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ascent.autobcm.model.EntityTypeAttributeDefinition;

@Repository
public interface EntityTypeAttributeDefinitionRepository extends JpaRepository<EntityTypeAttributeDefinition, Long> {

	EntityTypeAttributeDefinition findByEntityTypeIdAndAttributeName(long id, String attributeName);

}
