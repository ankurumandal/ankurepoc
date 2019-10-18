package com.ascent.autobcm.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ascent.autobcm.exception.PermissionAlreadyExistsException;
import com.ascent.autobcm.model.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

	List<Permission> findByOperationIdInAndEntityIdInAndRoleIdAndEntityTypeId(List<Long> operationsIds,
			List<Long> entityIds, long roleId, long entityTypeId);

	Permission findByRoleIdAndEntityTypeIdAndOperationIdAndUserAttributesAndConditionAndUserValues(long roleId,
			long entityTypeId, long operationId, long userAttributeId, long conditionId, long userValueId)
			throws PermissionAlreadyExistsException;

	Permission findByOperationIdAndEntityIdAndRoleIdAndEntityTypeId(Long operationsId, Long entityId, long roleId,
			long entityTypeId);

	Optional<Permission> findById(Long permissionId);

}
