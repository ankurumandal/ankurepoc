package com.ascent.autobcm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ascent.autobcm.dao.PermissionRepository;
import com.ascent.autobcm.exception.NoPermissionFoundException;
import com.ascent.autobcm.exception.PermissionAlreadyExistsException;
import com.ascent.autobcm.model.Permission;
import com.ascent.autobcm.util.Constants;

@Service
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	PermissionRepository permissionRepository;

	@Override
	public List<Permission> findByOperationIdInAndEntityIdInAndRoleIdAndEntityTypeId(List<Long> operationsIds,
			List<Long> entityIds, long roleId, long entityTypeId) throws NoPermissionFoundException {

		List<Permission> permissionFromDb = null;

		permissionFromDb = permissionRepository.findByOperationIdInAndEntityIdInAndRoleIdAndEntityTypeId(operationsIds,
				entityIds, roleId, entityTypeId);
		if (null == permissionFromDb)
			throw new NoPermissionFoundException(Constants.NO_PERMISSION_FOUND);

		return permissionFromDb;
	}

	@Override
	public List<Permission> savePermissions(List<Permission> permissionList, String entityBased, Long roleId,
			Long entityTypeId) throws PermissionAlreadyExistsException {
		List<Long> operationIdList = new ArrayList<Long>();
		List<Long> entityIdList = new ArrayList<Long>();
		List<Permission> permissionFromDb = null;

		permissionList.stream().forEach(item -> operationIdList.add(item.getOperation().getId()));
		permissionList.stream().forEach(item -> entityIdList.add(item.getEntity().getId()));

		permissionFromDb = permissionRepository.findByOperationIdInAndEntityIdInAndRoleIdAndEntityTypeId(
				operationIdList, entityIdList, roleId, entityTypeId);

		if (null != permissionFromDb)
			throw new PermissionAlreadyExistsException(Constants.PERMISSION_ALREADY_PRESENT);

		List<Permission> persistedPermission = permissionRepository.saveAll(permissionList);
		return persistedPermission;
	}

	@Override
	public Permission savePermission(Permission permissionToBePersisted) throws PermissionAlreadyExistsException {
		Permission permissionFromDb = null;

		permissionFromDb = permissionRepository
				.findByRoleIdAndEntityTypeIdAndOperationIdAndUserAttributesAndConditionAndUserValues(
						permissionToBePersisted.getRole().getId(), permissionToBePersisted.getEntityType().getId(),
						permissionToBePersisted.getOperation().getId(), permissionToBePersisted.getUserAttributes(),
						permissionToBePersisted.getCondition(), permissionToBePersisted.getUserValues());

		if (null != permissionFromDb)
			throw new PermissionAlreadyExistsException(Constants.PERMISSION_ALREADY_PRESENT);

		Permission persistedPermission = permissionRepository.save(permissionToBePersisted);
		return persistedPermission;
	}

	@Override
	public Permission updatePermission(Permission permissionToBePersisted, String entityBased)
			throws PermissionAlreadyExistsException, NoPermissionFoundException {
		Permission permissionFromDb = null;

		Optional<Permission> permissionInstanceFromDb = permissionRepository.findById(permissionToBePersisted.getId());

		if (!permissionInstanceFromDb.isPresent())
			throw new NoPermissionFoundException(Constants.NO_PERMISSION_FOUND);

		if (entityBased.equalsIgnoreCase(Constants.ACTIVE)) {
			permissionFromDb = permissionRepository.findByOperationIdAndEntityIdAndRoleIdAndEntityTypeId(
					permissionToBePersisted.getOperation().getId(), permissionToBePersisted.getEntity().getId(),
					permissionToBePersisted.getRole().getId(), permissionToBePersisted.getEntityType().getId());

			if (null != permissionFromDb)
				throw new PermissionAlreadyExistsException(Constants.PERMISSION_ALREADY_PRESENT);
		} else {

			permissionFromDb = permissionRepository
					.findByRoleIdAndEntityTypeIdAndOperationIdAndUserAttributesAndConditionAndUserValues(
							permissionToBePersisted.getRole().getId(), permissionToBePersisted.getEntityType().getId(),
							permissionToBePersisted.getOperation().getId(), permissionToBePersisted.getUserAttributes(),
							permissionToBePersisted.getCondition(), permissionToBePersisted.getUserValues());

			if (null != permissionFromDb)
				throw new PermissionAlreadyExistsException(Constants.PERMISSION_ALREADY_PRESENT);

		}

		return null;
	}

	@Override
	public Permission findByOperationIdAndEntityIdAndRoleIdAndEntityTypeId(Long operationsId, Long entityId,
			long roleId, long entityTypeId) throws PermissionAlreadyExistsException {
		Permission permission = null;
		permission = permissionRepository.findByOperationIdAndEntityIdAndRoleIdAndEntityTypeId(operationsId, entityId,
				roleId, entityTypeId);
		if (null == permission)
			throw new PermissionAlreadyExistsException(Constants.PERMISSION_ALREADY_PRESENT);
		return permission;
	}

	@Override
	public Permission findById(long permissionId) throws NoPermissionFoundException {
		Optional<Permission> permissionFromDb = null;

		permissionFromDb = permissionRepository.findById(permissionId);
		if (!permissionFromDb.isPresent())
			throw new NoPermissionFoundException(Constants.NO_PERMISSION_FOUND);
		return permissionFromDb.get();
	}

	@Override
	public Permission deletePermission(long permissionId) throws NoPermissionFoundException {
		Optional<Permission> permissionInstanceFromDb = null;

		permissionInstanceFromDb = permissionRepository.findById(permissionId);

		if (!permissionInstanceFromDb.isPresent())
			throw new NoPermissionFoundException(Constants.NO_PERMISSION_FOUND);

		permissionInstanceFromDb.get().setActive(Constants.INACTIVE);

		Permission persistedEntries = permissionRepository.save(permissionInstanceFromDb.get());
		return persistedEntries;
	}

	@Override
	public Permission reactivatePermission(long permissionId) throws NoPermissionFoundException {
		Optional<Permission> permissionInstanceFromDb = null;

		permissionInstanceFromDb = permissionRepository.findById(permissionId);

		if (!permissionInstanceFromDb.isPresent())
			throw new NoPermissionFoundException(Constants.NO_PERMISSION_FOUND);

		permissionInstanceFromDb.get().setActive(Constants.ACTIVE);

		Permission persistedEntries = permissionRepository.save(permissionInstanceFromDb.get());
		return persistedEntries;
	}

}
