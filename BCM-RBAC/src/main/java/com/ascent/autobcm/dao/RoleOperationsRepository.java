package com.ascent.autobcm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ascent.autobcm.model.Operations;
import com.ascent.autobcm.model.Role;
import com.ascent.autobcm.model.RoleOperations;

@Repository
public interface RoleOperationsRepository extends JpaRepository<RoleOperations, Long> {

	List<RoleOperations> findByRoleId(long roleId);

	public List<RoleOperations> findByActive(String activeStatus);

	@Query("select ur.operation from RoleOperations ur left join ur.role role where role.id = ?1")
	public List<Operations> findByRoleAssigned(long roleId);

	@Query("select ur.role from RoleOperations ur left join ur.operation operation where operation.id = ?1")
	List<Role> findByOperationAssigned(long operationId);

	List<RoleOperations> findByRoleIdAndActive(long roleId, String active);

	@Query("select ur from RoleOperations ur left join ur.operation operation where operation.id = ?1")
	List<RoleOperations> findByOperationId(long operationId);

	@Query("select ur from RoleOperations ur left join ur.operation operation where operation.id = ?1 and ur.active = ?2")
	List<RoleOperations> findByOperationIdAndStatus(long operationId, String activeStatus);

	@Query("select ur from RoleOperations ur left join ur.role role where role.id = ?1 and ur.active = ?2")
	List<RoleOperations> findByRoleIdAndStatus(long operationId, String activeStatus);

}
