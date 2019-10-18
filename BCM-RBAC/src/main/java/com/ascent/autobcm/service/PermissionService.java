package com.ascent.autobcm.service;

import java.util.List;

import com.ascent.autobcm.exception.NoPermissionFoundException;
import com.ascent.autobcm.exception.PermissionAlreadyExistsException;
import com.ascent.autobcm.model.Permission;

public interface PermissionService {

	public List<Permission> findByOperationIdInAndEntityIdInAndRoleIdAndEntityTypeId(List<Long> operationsIds,
			List<Long> entityIds, long roleId, long entityTypeId) throws NoPermissionFoundException;

	public List<Permission> savePermissions(List<Permission> permissionList, String entityBased, Long roleId,
			Long entityTypeId) throws PermissionAlreadyExistsException;

	public Permission savePermission(Permission permissionToBePersisted) throws PermissionAlreadyExistsException;

	public Permission updatePermission(Permission permissionToBePersisted, String entityBased)
			throws PermissionAlreadyExistsException, NoPermissionFoundException;

	public Permission findByOperationIdAndEntityIdAndRoleIdAndEntityTypeId(Long operationsId, Long entityId,
			long roleId, long entityTypeId) throws PermissionAlreadyExistsException;

	public Permission findById(long permissionId) throws NoPermissionFoundException;
	
	public Permission deletePermission(long permissionId) throws NoPermissionFoundException;
	
	public Permission reactivatePermission(long permissionId) throws NoPermissionFoundException;
	
	

}
