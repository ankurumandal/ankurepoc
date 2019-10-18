package com.ascent.autobcm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ascent.autobcm.model.Group;
import com.ascent.autobcm.model.User;
import com.ascent.autobcm.model.UserGroup;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

	List<UserGroup> findByGroupId(long groupId);

	List<UserGroup> findByUserId(long userId);

	@Query("select ug.user from UserGroup ug left join ug.group groups where groups.id = ?1")
	List<User> findUsersAssignedForGroup(long groupId);

	@Query("select ug.group from UserGroup ug left join ug.user user where user.id = ?1")
	List<Group> findGroupsAssignedForUser(long userId);
	
	List<UserGroup> findByUserIdAndActive(long userId, String activeStatus);
	
	List<UserGroup> findByGroupIdAndActive(long groupId, String activeStatus);

}
