package com.ascent.autobcm.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.joda.time.DateTime;

@Entity
@Table(name = "Users_Role")
public class UserRole {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	@OneToOne // (fetch = FetchType.EAGER)
	public User user;

	@OneToOne // (fetch = FetchType.EAGER)
	public Role role;

	@Column
	public String active;

	@Column
	private String specialPermissions;

	@Column
	private DateTime tenureSpecialPermissions;

	@CreationTimestamp
	private LocalDateTime createDateTime;

	@UpdateTimestamp
	private LocalDateTime updateDateTime;

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getSpecialPermissions() {
		return specialPermissions;
	}

	public void setSpecialPermissions(String specialPermissions) {
		this.specialPermissions = specialPermissions;
	}

	public DateTime getTenureSpecialPermissions() {
		return tenureSpecialPermissions;
	}

	public void setTenureSpecialPermissions(DateTime tenureSpecialPermissions) {
		this.tenureSpecialPermissions = tenureSpecialPermissions;
	}

	public LocalDateTime getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(LocalDateTime createDateTime) {
		this.createDateTime = createDateTime;
	}

	public LocalDateTime getUpdateDateTime() {
		return updateDateTime;
	}

	public void setUpdateDateTime(LocalDateTime updateDateTime) {
		this.updateDateTime = updateDateTime;
	}

}
