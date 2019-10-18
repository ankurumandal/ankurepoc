package com.ascent.autobcm.service;

import java.util.List;

import com.ascent.autobcm.exception.NoSuchRoleFoundException;
import com.ascent.autobcm.exception.RoleAlreadyPresentException;
import com.ascent.autobcm.model.Role;

public interface RoleService {

	public Role saveRole(Role roleObjToPersist) throws RoleAlreadyPresentException;

	public Role findByRoleName(String role);

	public Role findById(long roleId) throws NoSuchRoleFoundException;

	public boolean deleteById(long roleId);

	public List<Role> findAllByActive(String activeStatus);

	public Role findByIdAndRoleName(long roleId, String roleName);

	public Role updateRole(Role roleObjToPersist) throws RoleAlreadyPresentException, NoSuchRoleFoundException;;

	public Role deleteRole(long roleId) throws NoSuchRoleFoundException;

	public Role reactivateRole(long roleId) throws NoSuchRoleFoundException;

}
