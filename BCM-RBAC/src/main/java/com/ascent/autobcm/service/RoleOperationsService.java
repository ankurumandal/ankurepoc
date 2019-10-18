package com.ascent.autobcm.service;

import java.util.List;

import com.ascent.autobcm.model.Operations;
import com.ascent.autobcm.model.Role;
import com.ascent.autobcm.model.RoleOperations;

public interface RoleOperationsService {

	public RoleOperations saveRoleOperations(RoleOperations roleOperations);

	public List<RoleOperations> findByRoleId(long roleId);
	
	public boolean deleteExistingMappings(List<RoleOperations> existingMapping); 
	
	public boolean findAndDeleteExistingMappings(long roleId);

	public List<RoleOperations> saveRoleOperationsMapping(List<RoleOperations> existingMapping);
	
	public List<RoleOperations> findAllByActive(String activeStatus);
	
	public List<Operations> findByRole(long roleId);
	
	public List<Role> findByOperation(long operationId);
}
