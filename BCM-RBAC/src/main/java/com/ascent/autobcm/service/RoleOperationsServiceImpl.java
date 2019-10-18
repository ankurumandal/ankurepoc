package com.ascent.autobcm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ascent.autobcm.dao.RoleOperationsRepository;
import com.ascent.autobcm.model.Operations;
import com.ascent.autobcm.model.Role;
import com.ascent.autobcm.model.RoleOperations;

@Service
public class RoleOperationsServiceImpl implements RoleOperationsService {

	@Autowired
	RoleOperationsRepository roleOperationsRepository;

	@Override
	public RoleOperations saveRoleOperations(RoleOperations objToBePersisted) {
		RoleOperations persistedObj = roleOperationsRepository.save(objToBePersisted);
		return persistedObj;
	}

	@Override
	public List<RoleOperations> findByRoleId(long roleId) {
		List<RoleOperations> existingMappings = roleOperationsRepository.findByRoleId(roleId);
		return existingMappings;
	}

	@Override
	public boolean deleteExistingMappings(List<RoleOperations> existingMapping) {
		roleOperationsRepository.deleteAll(existingMapping);
		return true;
	}

	@Override
	public boolean findAndDeleteExistingMappings(long roleId) {
		List<RoleOperations> existingMappingForRole = roleOperationsRepository.findByRoleId(roleId);
		if (existingMappingForRole.size() > 0) {
			roleOperationsRepository.deleteAll(existingMappingForRole);
			return true;
		}
		return false;
	}

	@Override
	public List<RoleOperations> saveRoleOperationsMapping(List<RoleOperations> existingMapping) {
		return roleOperationsRepository.saveAll(existingMapping);
	}

	@Override
	public List<RoleOperations> findAllByActive(String activeStatus) {
		List<RoleOperations> roleOperationList = roleOperationsRepository.findByActive(activeStatus);
		return roleOperationList;
	}

	@Override
	public List<Operations> findByRole(long roleId) {
		return roleOperationsRepository.findByRoleAssigned(roleId);
	}

	@Override
	public List<Role> findByOperation(long operationId) {
		return roleOperationsRepository.findByOperationAssigned(operationId);
	}

}
