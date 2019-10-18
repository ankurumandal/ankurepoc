package com.ascent.autobcm.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ascent.autobcm.dao.EntitiesAttributeRepository;
import com.ascent.autobcm.model.EntitiesAttribute;

@Service
public class EntitiesAttributeServiceImpl implements EntitiesAttributeService {

	@Autowired
	EntitiesAttributeRepository entitiesAttributeRepository;

	@Override
	public EntitiesAttribute saveEntitiesAttribute(EntitiesAttribute entityType) {
		EntitiesAttribute persistedEntity = entitiesAttributeRepository.save(entityType);
		return persistedEntity;
	}

	@Override
	public EntitiesAttribute findById(long entityTypeId) {

		Optional<EntitiesAttribute> optionalEntity = entitiesAttributeRepository.findById(entityTypeId);
		if (optionalEntity.isPresent()) {
			return optionalEntity.get();
		}
		return null;

	}

	@Override
	public EntitiesAttribute findByAttributeName(String attributeName) {
		EntitiesAttribute entityAttrib = entitiesAttributeRepository.findByAttributeName(attributeName);
		return entityAttrib;
	}

}
