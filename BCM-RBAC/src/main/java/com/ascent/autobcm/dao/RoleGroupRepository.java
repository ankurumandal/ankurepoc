package com.ascent.autobcm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ascent.autobcm.model.Group;
import com.ascent.autobcm.model.Role;
import com.ascent.autobcm.model.RoleGroup;

@Repository
public interface RoleGroupRepository extends JpaRepository<RoleGroup, Long> {

	public List<RoleGroup> findByGroupId(long groupId);

	@Query(value = "select rg.role from RoleGroup rg left join rg.group groups where groups.id = ?1")
	public List<Role> findByGroupAssigned(long groupId);

	@Query(value = "select rg.group from RoleGroup rg left join rg.role role where role.id = ?1")
	public List<Group> findByRoleAssigned(long roleId);

	@Query(value = "select rg.group from RoleGroup rg left join rg.role role where role.id = ?1")
	public List<RoleGroup> findByRoleId(long roleId);

	@Query(value = "select rg from RoleGroup rg left join rg.role role where role.id = ?1")
	public List<RoleGroup> findRoleGroupByRuleId(long roleId);

	@Query(value = "select rg from RoleGroup rg left join rg.role role where role.id = ?1 and rg.active = ?2")
	public List<RoleGroup> findByRoleIdAndActive(long roleId, String active);

}
