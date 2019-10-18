package com.ascent.autobcm.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.ascent.autobcm.util.Constants;

@Entity
@Table(name = "Users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	@Column
	public String employeeId;

	@Column
	public String userName;

	@Column
	public String firstName;

	@Column
	public String lastName;

	@Column
	public String password;

	@Column
	public String contactNumber;

	@Column
	public String emailId;

	@Column
	public String active;

	@Column
	public String adminPasswordReset = Constants.NO;

	@Column
	public String userToPasswordReset;

	@Column
	private long wrongCredentialsLogin;

	@ElementCollection
	@CollectionTable(name = "Passwords", joinColumns = @JoinColumn(name = "user_id"))
	@Column(name = "password")
	public List<String> passwords;

	@CreationTimestamp
	private LocalDateTime passwordChangeDate;

	@CreationTimestamp
	private LocalDateTime createDateTime;

	@UpdateTimestamp
	private LocalDateTime updateDateTime;

	@Column
	private LocalDateTime lastLoginDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getAdminPasswordReset() {
		return adminPasswordReset;
	}

	public void setAdminPasswordReset(String adminPasswordReset) {
		this.adminPasswordReset = adminPasswordReset;
	}

	public String getUserToPasswordReset() {
		return userToPasswordReset;
	}

	public void setUserToPasswordReset(String userToPasswordReset) {
		this.userToPasswordReset = userToPasswordReset;
	}

	public long getWrongCredentialsLogin() {
		return wrongCredentialsLogin;
	}

	public void setWrongCredentialsLogin(long wrongCredentialsLogin) {
		this.wrongCredentialsLogin = wrongCredentialsLogin;
	}

	public List<String> getPasswords() {
		return passwords;
	}

	public void setPasswords(List<String> passwords) {
		this.passwords = passwords;
	}

	public LocalDateTime getPasswordChangeDate() {
		return passwordChangeDate;
	}

	public void setPasswordChangeDate(LocalDateTime passwordChangeDate) {
		this.passwordChangeDate = passwordChangeDate;
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

	public LocalDateTime getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(LocalDateTime lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public User(long id, String employeeId, String userName, String firstName, String lastName, String password,
			String contactNumber) {
		super();
		this.id = id;
		this.employeeId = employeeId;
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.contactNumber = contactNumber;
	}

	/*
	 * public Set<Role> getRoles() { return roles; }
	 * 
	 * public void setRoles(Set<Role> roles) { this.roles = roles; }
	 */
	public User() {

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contactNumber == null) ? 0 : contactNumber.hashCode());
		result = prime * result + (int) (employeeId.hashCode() ^ (employeeId.hashCode() >>> 32));
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (contactNumber == null) {
			if (other.contactNumber != null)
				return false;
		} else if (!contactNumber.equals(other.contactNumber))
			return false;
		if (employeeId != other.employeeId)
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		return true;
	}

}
