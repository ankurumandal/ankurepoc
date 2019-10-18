package com.ascent.autobcm.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ascent.autobcm.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	public User findByEmployeeId(String employeeId);

	@Modifying
	@Transactional
	@Query("delete from User where employeeId = ?1")
	public void deleteByEmployeeId(long employeeId);

	public List<User> findByActive(String activeStatus);

	public User findByUserName(String userName);

	public User findById(long userId);

	@Query(value = "select ur From User ur where DATEDIFF(DAY, ur.createDateTime, GETDATE()) = ?1")
	public List<User> findUsersForPasswordChange(int difference);

	@Query(value = "select ur From User ur where DATEDIFF(DAY, ur.lastLoginDate, GETDATE()) = ?1")
	public List<User> findUsersByInactivity(int difference);

}
