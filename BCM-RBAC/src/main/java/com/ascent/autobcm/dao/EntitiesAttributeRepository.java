package com.ascent.autobcm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ascent.autobcm.model.EntitiesAttribute;

@Repository
public interface EntitiesAttributeRepository extends JpaRepository<EntitiesAttribute, Long> {

	EntitiesAttribute findByAttributeName(String attributeName);

}
