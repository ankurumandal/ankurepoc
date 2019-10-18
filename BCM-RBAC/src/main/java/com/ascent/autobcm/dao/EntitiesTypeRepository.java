package com.ascent.autobcm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ascent.autobcm.model.EntitiesType;

@Repository
public interface EntitiesTypeRepository extends JpaRepository<EntitiesType, Long> {

	public EntitiesType findByName(String name);

}
