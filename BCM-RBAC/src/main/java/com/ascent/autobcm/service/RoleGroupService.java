package com.ascent.autobcm.service;

import java.util.List;

import com.ascent.autobcm.model.Group;
import com.ascent.autobcm.model.Role;
import com.ascent.autobcm.model.RoleGroup;

public interface RoleGroupService {

	public RoleGroup saveRoleGroupEntity(RoleGroup roleGroupEntity);

	public List<RoleGroup> findByGroupId(long groupId);

	public List<RoleGroup> saveAllRoleGroupEntity(List<RoleGroup> roleGroupEntity);

	public List<Role> findRolesByGroup(long groupId);

	public List<Group> findGroupsByRole(long roleId);

}
