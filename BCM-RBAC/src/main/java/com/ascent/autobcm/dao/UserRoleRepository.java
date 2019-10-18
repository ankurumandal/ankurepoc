package com.ascent.autobcm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ascent.autobcm.model.Role;
import com.ascent.autobcm.model.User;
import com.ascent.autobcm.model.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

//	@Query("select u from UserRole u left join fetch u.roles where u.user.employeeId = 1")
	List<UserRole> findByUserId(long id);

	public List<UserRole> findByActive(String activeStatus);

	@Query("select ur.user from UserRole ur left join ur.role role where role.id = ?1")
	List<User> findByRoleAssigned(long roleId);

	@Query("select ur.role from UserRole ur left join ur.user users where users.id = ?1")
	List<Role> findByUserAssigned(long userId);

	List<UserRole> findByRoleRoleName(String roleName);

	List<UserRole> findByUserIdAndActive(long userId, String activeStatus);

	List<UserRole> findByRoleId(long roleId);

	List<UserRole> findByRoleIdAndActive(long roleId, String activeStatus);

	List<UserRole> findByRoleIdInAndUserIdInAndActive(List<Long> roleIds, List<Long> userIds, String activeStatus);
	// List<Role> findBy(long userId);

	List<UserRole> findByRoleIdInAndUserIdIn(List<Long> roleIds, List<Long> userIds);
}
