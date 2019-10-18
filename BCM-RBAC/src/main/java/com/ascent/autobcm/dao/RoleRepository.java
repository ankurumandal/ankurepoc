package com.ascent.autobcm.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ascent.autobcm.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	public Optional<Role> findByRoleName(String role);

	public Role findById(long roleId);

	public List<Role> findByActive(String activeStatus);

	public Role findByIdAndRoleName(long roleId, String roleName);

}
